package net.bitbylogic.stomarcade.server;

import com.google.gson.Gson;
import net.bitbylogic.kardia.server.KardiaServer;
import net.bitbylogic.kardia.util.RedisKeys;
import net.bitbylogic.stomarcade.StomArcadeServer;
import net.bitbylogic.stomarcade.server.player.KardiaPlayer;
import net.bitbylogic.stomarcade.server.settings.ServerSettings;
import net.minestom.server.MinecraftServer;
import net.minestom.server.entity.Entity;
import net.minestom.server.timer.Task;
import net.minestom.server.timer.TaskSchedule;
import org.jetbrains.annotations.NotNull;
import org.redisson.api.RMap;
import org.redisson.api.RedissonClient;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

public class ServerManager {

    public static final String NETWORK_PLAYERS_KEY = "kardia:players:%s";
    public static final String NETWORK_PLAYERS_BY_NAME_KEY = "kardia:players:name";

    private String serverName;
    private ServerType serverType;

    private ServerSettings serverSettings;
    private ServerEnvironment serverEnvironment;

    private Gson gson;
    private Task serverUpdater;

    public ServerManager() {
        this.serverType = ServerType.UNKNOWN;
        this.serverSettings = new ServerSettings();
        this.serverEnvironment = new ServerEnvironment();
        this.serverName = serverEnvironment.getEnv(ServerEnvironment.EnvVariable.KARDIA_ID);

        this.gson = new Gson();
    }

    /**
     * Starts all server related processes.
     */
    public void start() {
        this.serverUpdater = MinecraftServer.getInstanceManager().getInstances().stream().findAny().get().scheduler().scheduleTask(() -> {
            RMap<String, String> servers = StomArcadeServer.redis().getRedissonClient().getMap(RedisKeys.SERVERS);

            String kardiaId = serverEnvironment.getEnv(ServerEnvironment.EnvVariable.KARDIA_ID);
            servers.getAsync(kardiaId).thenAccept(value -> {
                if (value == null) {
                    return;
                }

                KardiaServer kardiaServer = gson.fromJson(value, KardiaServer.class);

                KardiaServer modifiedServer = new KardiaServer(kardiaServer.kardiaId(),
                        kardiaServer.instance(),
                        kardiaServer.ids(),
                        kardiaServer.serverType(),
                        kardiaServer.ip(),
                        kardiaServer.boundPort(),
                        serverSettings.maxPlayers(),
                        serverSettings.joinState(),
                        serverSettings.isPrivateServer(),
                        MinecraftServer.getConnectionManager()
                                .getOnlinePlayers().stream()
                                .map(Entity::getUuid)
                                .toList()
                );

                servers.putAsync(kardiaId, gson.toJson(modifiedServer));
            });
        }, TaskSchedule.duration(Duration.ZERO), TaskSchedule.duration(Duration.ofSeconds(5)));
    }

    public void stop() {
        this.serverUpdater.cancel();
    }

    public CompletionStage<KardiaPlayer> getPlayer(UUID uuid) {
        RedissonClient redisson = StomArcadeServer.redis().getRedissonClient();
        RMap<String, String> playerData = redisson.getMap(String.format(NETWORK_PLAYERS_KEY, uuid));

        return playerData.readAllMapAsync()
                .thenApply(data -> {
                    if (data.isEmpty()) {
                        return null;
                    }

                    return new KardiaPlayer(uuid, data.get("name"), data.get("server"));
                });
    }

    public CompletionStage<KardiaPlayer> getPlayer(String name) {
        RedissonClient redisson = StomArcadeServer.redis().getRedissonClient();
        RMap<String, String> byName = redisson.getMap(NETWORK_PLAYERS_BY_NAME_KEY);

        String normalizedName = name.toLowerCase();

        return byName.getAsync(normalizedName)
                .thenCompose(uuidStr -> {
                    if (uuidStr == null) {
                        return CompletableFuture.completedFuture(null);
                    }

                    return getPlayer(UUID.fromString(uuidStr));
                });
    }

    public void addPlayer(KardiaPlayer player) {
        updatePlayer(player);
    }

    public CompletionStage<Boolean> removePlayer(KardiaPlayer player) {
        RedissonClient redisson = StomArcadeServer.redis().getRedissonClient();
        RMap<String, String> byName = redisson.getMap(NETWORK_PLAYERS_BY_NAME_KEY);
        RMap<String, String> playerData = redisson.getMap(String.format(NETWORK_PLAYERS_KEY, player.getUuid()));

        return byName.removeAsync(player.getName().toLowerCase()).thenCompose(_ -> playerData.deleteAsync());
    }

    public CompletionStage<Void> updatePlayer(@NotNull KardiaPlayer player) {
        RedissonClient redisson = StomArcadeServer.redis().getRedissonClient();
        RMap<String, String> byName = redisson.getMap(NETWORK_PLAYERS_BY_NAME_KEY);
        RMap<String, String> playerData = redisson.getMap(String.format(NETWORK_PLAYERS_KEY, player.getUuid()));

        return playerData.getAsync("name").thenCompose(oldName -> {
            CompletableFuture<String> removeFuture = (oldName != null && !oldName.equals(player.getName()))
                    ? byName.removeAsync(oldName.toLowerCase()).toCompletableFuture()
                    : CompletableFuture.completedFuture(null);

            return removeFuture.thenCompose(v -> {
                CompletableFuture<String> byNameFuture = byName.putAsync(player.getName().toLowerCase(), player.getUuid().toString())
                        .toCompletableFuture();

                CompletableFuture<String> playerDataFuture = playerData.putAsync("name", player.getName())
                        .thenCompose(__ -> playerData.putAsync("server", player.getServer()))
                        .toCompletableFuture();

                return CompletableFuture.allOf(byNameFuture, playerDataFuture);
            });
        });
    }

    public CompletableFuture<List<KardiaPlayer>> getAllPlayers() {
        RedissonClient redisson = StomArcadeServer.redis().getRedissonClient();
        RMap<String, String> byName = redisson.getMap(NETWORK_PLAYERS_BY_NAME_KEY);

        return byName.readAllMapAsync().thenCompose(nameToUuid -> {
            List<CompletableFuture<KardiaPlayer>> futures = new ArrayList<>();

            nameToUuid.values().forEach(uuidStr -> {
                UUID uuid = UUID.fromString(uuidStr);
                RMap<String, String> playerData = redisson.getMap(String.format(NETWORK_PLAYERS_KEY, uuid));
                CompletableFuture<KardiaPlayer> playerFuture = playerData.readAllMapAsync()
                        .thenApply(data -> {
                            if (data.isEmpty()) {
                                return null;
                            }

                            return new KardiaPlayer(uuid, playerData.get("name"), playerData.get("server"));
                        }).toCompletableFuture();

                futures.add(playerFuture);
            });

            return CompletableFuture.allOf(futures.toArray(new CompletableFuture[0]))
                    .thenApply(v -> futures.stream()
                            .map(CompletableFuture::join)
                            .filter(Objects::nonNull)
                            .toList()
                    );
        }).toCompletableFuture();
    }

    public ServerSettings getServerSettings() {
        return serverSettings;
    }

    public enum ServerType {
        LOBBY,
        GAME,
        TEST,
        UNKNOWN
    }

}
