package net.bitbylogic.stomarcade.redis;

import net.bitbylogic.rps.listener.ListenerComponent;
import net.bitbylogic.rps.listener.RedisMessageListener;
import net.bitbylogic.stomarcade.message.messages.ServerMessages;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import net.kyori.adventure.title.Title;
import net.minestom.server.MinecraftServer;
import net.minestom.server.entity.Player;

public class AnnounceListener extends RedisMessageListener {

    public AnnounceListener() {
        super("announce");

        setSelfActivation(true);
    }

    @Override
    public void onReceive(ListenerComponent component) {
        String message = component.getData("message", String.class);
        boolean title = component.getDataOrElse("title", Boolean.class, false);

        for (Player onlinePlayer : MinecraftServer.getConnectionManager().getOnlinePlayers()) {
            ServerMessages.ANNOUNCEMENT.send(onlinePlayer, Placeholder.parsed("message", message));

            if (title) {
                onlinePlayer.showTitle(Title.title(
                        ServerMessages.ANNOUNCEMENT_TITLE.get(onlinePlayer, Placeholder.parsed("message", message)),
                        ServerMessages.ANNOUNCEMENT_SUBTITLE.get(onlinePlayer, Placeholder.parsed("message", message))
                ));
            }
        }
    }

}
