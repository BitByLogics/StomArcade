package net.bitbylogic.stomarcade.redis;

import net.bitbylogic.rps.listener.ListenerComponent;
import net.bitbylogic.rps.listener.RedisMessageListener;
import net.bitbylogic.stomarcade.message.messages.ServerMessages;
import net.bitbylogic.stomarcade.util.PermissionUtil;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import net.minestom.server.MinecraftServer;
import net.minestom.server.entity.Player;

public class StaffMessageListener extends RedisMessageListener {

    public StaffMessageListener() {
        super("staff_message");
    }

    @Override
    public void onReceive(ListenerComponent component) {
        String message = component.getData("message", String.class);

        for (Player onlinePlayer : MinecraftServer.getConnectionManager().getOnlinePlayers()) {
            if (!PermissionUtil.has(onlinePlayer, "stomarcade.staff")) {
                continue;
            }

            ServerMessages.STAFF_CHAT_FORMAT.send(onlinePlayer, Placeholder.parsed("player", onlinePlayer.getUsername()), Placeholder.parsed("message", message));
        }
    }
}
