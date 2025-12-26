package net.bitbylogic.stomarcade.util;

import net.kyori.adventure.text.logger.slf4j.ComponentLogger;
import net.minestom.server.MinecraftServer;
import net.minestom.server.command.builder.Command;
import net.minestom.server.event.Event;
import net.minestom.server.event.EventNode;
import net.minestom.server.event.player.PlayerCommandEvent;
import net.minestom.server.event.player.PlayerDisconnectEvent;
import net.minestom.server.event.player.PlayerLoadedEvent;
import org.jetbrains.annotations.NotNull;

public class ServerLogger {

    private static final EventNode<Event> node = EventNode.all("server_logger");

    public ServerLogger(@NotNull ComponentLogger logger) {
        node.addListener(PlayerLoadedEvent.class, event -> logger.info("{} connected.", event.getPlayer().getUsername()))
                .addListener(PlayerDisconnectEvent.class, event -> logger.info("{} disconnected.", event.getPlayer().getUsername()))
                .addListener(PlayerCommandEvent.class, event -> {
                    if (event.isCancelled()) {
                        return;
                    }

                    String commandString = event.getCommand();
                    String commandName = commandString.split(" ")[0];

                    Command command = MinecraftServer.getCommandManager().getCommand(commandName);

                    if (command == null || (command.getCondition() != null && !command.getCondition().canUse(event.getPlayer(), commandString))) {
                        return;
                    }

                    logger.info("{} executed command: {}", event.getPlayer().getUsername(), commandString);
                });
    }

    public EventNode<Event> node() {
        return node;
    }

}
