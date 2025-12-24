package net.bitbylogic.stomarcade;

import net.minestom.server.Auth;
import net.minestom.server.MinecraftServer;

public class StomArcadeServer {

    static void main(String[] args) {
        String velocitySecret = System.getenv("VELOCITY_SECRET");

        if (velocitySecret == null) {
            throw new RuntimeException("Velocity secret not set");
        }

        MinecraftServer.setCompressionThreshold(0);

        MinecraftServer minecraftServer = MinecraftServer.init(new Auth.Velocity(velocitySecret));

        String serverAddress = System.getenv("SERVER_ADDRESS");

        if (serverAddress == null) {
            serverAddress = "0.0.0.0";
        }

        int serverPort = 25565;

        try {
            serverPort = Integer.parseInt(System.getenv("SERVER_PORT"));
        } catch (NumberFormatException e) {
            // Ignored
        }

        minecraftServer.start(serverAddress, serverPort);
    }

}
