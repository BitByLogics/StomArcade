package net.bitbylogic.stomarcade;

import io.github.togar2.pvp.MinestomPvP;
import net.bitbylogic.kardia.server.KardiaServer;
import net.bitbylogic.orm.BormAPI;
import net.bitbylogic.rps.RedisManager;
import net.bitbylogic.rps.client.RedisClient;
import net.bitbylogic.stomarcade.command.*;
import net.bitbylogic.stomarcade.feature.ServerFeature;
import net.bitbylogic.stomarcade.feature.manager.FeatureManager;
import net.bitbylogic.stomarcade.loot.LootTableManager;
import net.bitbylogic.stomarcade.message.command.MessagesCommand;
import net.bitbylogic.stomarcade.message.manager.MessageManager;
import net.bitbylogic.stomarcade.message.messages.BrandingMessages;
import net.bitbylogic.stomarcade.message.messages.ServerMessages;
import net.bitbylogic.stomarcade.permission.manager.PermissionManager;
import net.bitbylogic.stomarcade.redis.AnnounceListener;
import net.bitbylogic.stomarcade.redis.CommandListener;
import net.bitbylogic.stomarcade.redis.PlayerMessageListener;
import net.bitbylogic.stomarcade.redis.StaffMessageListener;
import net.bitbylogic.stomarcade.server.ServerManager;
import net.bitbylogic.stomarcade.util.PermissionUtil;
import net.bitbylogic.stomarcade.util.message.MessageUtil;
import net.bitbylogic.utils.EnumUtil;
import net.hollowcube.polar.PolarLoader;
import net.kyori.adventure.text.logger.slf4j.ComponentLogger;
import net.minestom.server.Auth;
import net.minestom.server.MinecraftServer;
import net.minestom.server.command.ConsoleSender;
import net.minestom.server.entity.Player;
import net.minestom.server.event.player.AsyncPlayerConfigurationEvent;
import net.minestom.server.event.player.PlayerLoadedEvent;
import net.minestom.server.instance.InstanceContainer;
import net.minestom.server.instance.InstanceManager;
import net.minestom.server.instance.SharedInstance;
import org.redisson.client.codec.StringCodec;
import org.redisson.config.Config;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Scanner;

public final class StomArcadeServer {

    public static final ComponentLogger LOGGER = ComponentLogger.logger("Stom Arcade");

    private static LootTableManager lootTableManager;
    private static MessageManager messageManager;
    private static PermissionManager permissionManager;
    private static FeatureManager featureManager;
    private static BormAPI bormAPI;

    private static RedisManager redisManager;
    private static RedisClient redisClient;

    private static ServerManager serverManager;

    private static SharedInstance sharedInstance;

    static void main(String[] args) {
        String velocitySecret = System.getenv("VELOCITY_SECRET");

        if (velocitySecret == null) {
            throw new RuntimeException("Velocity secret not set");
        }

        MinecraftServer minecraftServer = MinecraftServer.init(new Auth.Velocity(velocitySecret));

        loadRedis();

        bormAPI = loadBORM();
        lootTableManager = new LootTableManager();
        messageManager = new MessageManager();
        permissionManager = new PermissionManager();
        featureManager = new FeatureManager();

        serverManager = new ServerManager();

        messageManager.registerGroup(new BrandingMessages(), new ServerMessages());

        MinestomPvP.init();

        String features = System.getenv("FEATURES");

        if(features != null && !features.isBlank()) {
            String[] featureNames = features.split(",");
            LOGGER.info("Enabling features: {}", features);

            for (String featureName : featureNames) {
                ServerFeature feature = EnumUtil.getValue(ServerFeature.class, featureName, null);

                if (feature == null) {
                    continue;
                }

                featureManager.enableFeature(feature);
            }
        }

        featureManager.enableFeature(
                ServerFeature.BLOCK_DROP,
                ServerFeature.ITEM_PICKUP,
                ServerFeature.ITEM_DROP,
                ServerFeature.TABLIST,
                ServerFeature.CHAT,
                ServerFeature.SERVER_LIST,
                ServerFeature.MODERN_VANILLA
        );

        MinecraftServer.getCommandManager().register(
                new GamemodeCommand(),
                new PermissionCommand(),
                new VersionCommand(),
                new TeleportCommand(),
                new MessageTestCommand(),
                new MessagesCommand(),
                new DispatchCommand(),
                new AnnounceCommand(),
                new StaffChatCommand()
        );

        InstanceManager instanceManager = MinecraftServer.getInstanceManager();
        InstanceContainer instanceContainer = instanceManager.createInstanceContainer();

        String worldName = System.getenv("WORLD_NAME");

        if (worldName == null) {
            worldName = "world";
        }

        Path worldPath = Path.of(String.format("./%s.polar", worldName));

        if (!worldPath.toFile().exists()) {
            throw new RuntimeException("World file does not exist: " + worldPath);
        }

        try {
            instanceContainer.setChunkLoader(new PolarLoader(worldPath));
        } catch (IOException e) {
            throw new RuntimeException("Failed to load world", e);
        }

        sharedInstance = instanceManager.createSharedInstance(instanceContainer);

        MinecraftServer.getGlobalEventHandler()
                .addListener(AsyncPlayerConfigurationEvent.class,
                        event -> event.setSpawningInstance(sharedInstance))
                .addChild(permissionManager.node()).addListener(PlayerLoadedEvent.class, event -> {
                    Player player = event.getPlayer();

                    permissionManager.registeredPermissions().forEach(permission -> PermissionUtil.set(player, permission, true));

                    player.refreshCommands();
                });

        String serverAddress = System.getenv().getOrDefault("SERVER_ADDRESS", "0.0.0.0");
        int serverPort = 25565;

        try {
            serverPort = Integer.parseInt(System.getenv("SERVER_PORT"));
        } catch (NumberFormatException ignored) {}

        minecraftServer.start(serverAddress, serverPort);
        MinecraftServer.setBrandName(MessageUtil.serialize(BrandingMessages.SERVER_BRANDING.get()));

        serverManager.start();

        LOGGER.info("Server started on {}:{}", serverAddress, serverPort);
        serverManager.getServerSettings().setJoinState(KardiaServer.JoinState.JOINABLE);

        Scanner scanner = new Scanner(System.in);

        while (scanner.hasNextLine()) {
            String command = scanner.nextLine();
            MinecraftServer.getCommandManager().execute(new ConsoleSender(), command);
        }
    }

    public static LootTableManager loot() { return lootTableManager; }
    public static MessageManager messages() { return messageManager; }
    public static PermissionManager permissions() { return permissionManager; }
    public static FeatureManager features() { return featureManager; }
    public static BormAPI borm() { return bormAPI; }
    public static RedisManager redis() { return redisManager; }
    public static RedisClient redisClient() { return redisClient; }
    public static SharedInstance sharedInstance() { return sharedInstance; }

    private static BormAPI loadBORM() {
        String host = System.getenv("SQL_HOST");
        String database = System.getenv("SQL_DATABASE");
        String port = System.getenv("SQL_PORT");
        String username = System.getenv("SQL_USERNAME");
        String password = System.getenv("SQL_PASSWORD");

        if (host == null || database == null || username == null || password == null) {
            LOGGER.info("DB information not provided, falling back to SQLite. (db.sqlite)");
            return new BormAPI(new File("db.sqlite"));
        }

        LOGGER.info("Connecting to remote database {}@{}:{}", username, host, port);
        return new BormAPI(host, database, port, username, password);
    }

    private static void loadRedis() {
        String host = System.getenv("REDIS_HOST");
        int port = 6379;

        try {
            port = Integer.parseInt(System.getenv("REDIS_PORT"));
        } catch (NumberFormatException ignored) {}

        String password = System.getenv("REDIS_PASSWORD");
        String sourceId = System.getenv("REDIS_SOURCE_ID");

        redisManager = new RedisManager(host, port, password, sourceId, new Config().setCodec(StringCodec.INSTANCE));
        redisClient = redisManager.registerClient(sourceId);

        redisClient.registerListener(new AnnounceListener());
        redisClient.registerListener(new CommandListener());
        redisClient.registerListener(new PlayerMessageListener());
        redisClient.registerListener(new StaffMessageListener());
    }

}