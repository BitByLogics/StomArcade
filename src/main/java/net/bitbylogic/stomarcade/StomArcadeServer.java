package net.bitbylogic.stomarcade;

import net.bitbylogic.stomarcade.command.GamemodeCommand;
import net.bitbylogic.stomarcade.command.PermissionCommand;
import net.bitbylogic.stomarcade.command.VersionCommand;
import net.bitbylogic.stomarcade.permission.manager.PermissionManager;
import net.hollowcube.polar.PolarLoader;
import net.kyori.adventure.text.logger.slf4j.ComponentLogger;
import net.minestom.server.Auth;
import net.minestom.server.MinecraftServer;
import net.minestom.server.command.ConsoleSender;
import net.minestom.server.event.player.AsyncPlayerConfigurationEvent;
import net.minestom.server.instance.InstanceContainer;
import net.minestom.server.instance.InstanceManager;
import net.minestom.server.instance.SharedInstance;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Scanner;

public class StomArcadeServer {

    private static final ComponentLogger LOGGER = ComponentLogger.logger("Stom Arcade");

    static void main(String[] args) {
        String velocitySecret = System.getenv("VELOCITY_SECRET");

        if (velocitySecret == null) {
            throw new RuntimeException("Velocity secret not set");
        }

        MinecraftServer.setCompressionThreshold(0);

        MinecraftServer minecraftServer = MinecraftServer.init(new Auth.Velocity(velocitySecret));

        PermissionManager permissionManager = new PermissionManager();

        MinecraftServer.getCommandManager().register(new GamemodeCommand(), new PermissionCommand(), new VersionCommand());

        InstanceManager instanceManager = MinecraftServer.getInstanceManager();
        InstanceContainer instanceContainer = instanceManager.createInstanceContainer();

        String worldName = System.getenv("WORLD_NAME");
        Path worldPath = Path.of(String.format("./%s.polar", worldName));

        if (!worldPath.toFile().exists()) {
            throw new RuntimeException("World file does not exist");
        }

        try {
            instanceContainer.setChunkLoader(new PolarLoader(worldPath));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        SharedInstance sharedInstance = instanceManager.createSharedInstance(instanceContainer);

        MinecraftServer.getGlobalEventHandler().addListener(AsyncPlayerConfigurationEvent.class,
                        event -> event.setSpawningInstance(sharedInstance))
                .addChild(permissionManager.node());

        String serverAddress = System.getenv("SERVER_ADDRESS");

        if (serverAddress == null) {
            serverAddress = "0.0.0.0";
        }

        int serverPort = 25566;

        try {
            serverPort = Integer.parseInt(System.getenv("SERVER_PORT"));
        } catch (NumberFormatException e) {
            // Ignored
        }

        minecraftServer.start(serverAddress, serverPort);
        MinecraftServer.setBrandName("StomArcade (Minestom)");

        Scanner scanner = new Scanner(System.in);

        while (scanner.hasNext()) {
            String command = scanner.nextLine();

            MinecraftServer.getCommandManager().execute(new ConsoleSender(), command);
        }
    }

}
