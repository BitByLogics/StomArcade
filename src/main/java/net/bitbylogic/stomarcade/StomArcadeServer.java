package net.bitbylogic.stomarcade;

import net.bitbylogic.orm.BormAPI;
import net.bitbylogic.stomarcade.command.*;
import net.bitbylogic.stomarcade.feature.ServerFeature;
import net.bitbylogic.stomarcade.feature.manager.FeatureManager;
import net.bitbylogic.stomarcade.loot.LootTableManager;
import net.bitbylogic.stomarcade.message.command.MessagesCommand;
import net.bitbylogic.stomarcade.message.manager.MessageManager;
import net.bitbylogic.stomarcade.message.messages.BrandingMessages;
import net.bitbylogic.stomarcade.permission.manager.PermissionManager;
import net.bitbylogic.stomarcade.util.PermissionUtil;
import net.bitbylogic.stomarcade.util.message.MessageUtil;
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

    private static SharedInstance sharedInstance;

    static void main(String[] args) {
        String velocitySecret = System.getenv("VELOCITY_SECRET");

        if (velocitySecret == null) {
            throw new RuntimeException("Velocity secret not set");
        }

        MinecraftServer minecraftServer = MinecraftServer.init(new Auth.Velocity(velocitySecret));

        bormAPI = loadBORM();
        lootTableManager = new LootTableManager();
        messageManager = new MessageManager();
        permissionManager = new PermissionManager();
        featureManager = new FeatureManager();

        messageManager.registerGroup(new BrandingMessages());

        featureManager.enableFeature(
                ServerFeature.BLOCK_DROP,
                ServerFeature.ITEM_PICKUP,
                ServerFeature.ITEM_DROP,
                ServerFeature.TABLIST,
                ServerFeature.CHAT,
                ServerFeature.SERVER_LIST
        );

        MinecraftServer.getCommandManager().register(
                new GamemodeCommand(),
                new PermissionCommand(),
                new VersionCommand(),
                new TeleportCommand(),
                new MessageTestCommand(),
                new MessagesCommand()
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
        int serverPort = 25566;

        try {
            serverPort = Integer.parseInt(System.getenv("SERVER_PORT"));
        } catch (NumberFormatException ignored) {}

        minecraftServer.start(serverAddress, serverPort);
        MinecraftServer.setBrandName(MessageUtil.serialize(BrandingMessages.SERVER_BRANDING.get()));

        LOGGER.info("Server started on {}:{}", serverAddress, serverPort);

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
    public static SharedInstance getSharedInstance() { return sharedInstance; }

    private static BormAPI loadBORM() {
        String host = System.getenv("DB_HOST");
        String database = System.getenv("DB_DATABASE");
        String port = System.getenv("DB_PORT");
        String username = System.getenv("DB_USERNAME");
        String password = System.getenv("DB_PASSWORD");

        if (host == null || database == null || username == null || password == null) {
            LOGGER.info("DB information not provided, falling back to SQLite. (db.sqlite)");
            return new BormAPI(new File("db.sqlite"));
        }

        LOGGER.info("Connecting to remote database {}@{}:{}", username, host, port);
        return new BormAPI(host, database, port, username, password);
    }

}