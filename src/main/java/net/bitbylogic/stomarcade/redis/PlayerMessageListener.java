package net.bitbylogic.stomarcade.redis;

import net.bitbylogic.rps.listener.ListenerComponent;
import net.bitbylogic.rps.listener.RedisMessageListener;
import net.bitbylogic.stomarcade.StomArcadeServer;
import net.bitbylogic.stomarcade.util.ServerUtil;
import net.bitbylogic.stomarcade.util.message.MessageUtil;
import net.kyori.adventure.text.Component;
import net.minestom.server.MinecraftServer;
import net.minestom.server.entity.Player;

import java.util.UUID;

public class PlayerMessageListener extends RedisMessageListener {

    public PlayerMessageListener() {
        super("player_message");
    }

    @Override
    public void onReceive(ListenerComponent component) {
        UUID playerId = component.getDataOrElse("playerId", UUID.class, ServerUtil.FALLBACK_UUID);
        Component message = MessageUtil.deserialize(component.getData("message", String.class));

        Player player = MinecraftServer.getConnectionManager().getOnlinePlayerByUuid(playerId);

        if (player == null) {
            StomArcadeServer.LOGGER.info(message);
            return;
        }

        player.sendMessage(message);
    }

}
