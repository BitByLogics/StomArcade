package net.bitbylogic.stomarcade.redis;

import net.bitbylogic.rps.listener.ListenerComponent;
import net.bitbylogic.rps.listener.RedisMessageListener;
import net.minestom.server.MinecraftServer;

public class CommandListener extends RedisMessageListener {

    public CommandListener() {
        super("server_command");
    }

    @Override
    public void onReceive(ListenerComponent component) {
        String command = component.getData("command", String.class);

        MinecraftServer.getSchedulerManager().scheduleNextTick(() -> {
            MinecraftServer.getCommandManager().execute(MinecraftServer.getCommandManager().getConsoleSender(), command);
        });
    }

}
