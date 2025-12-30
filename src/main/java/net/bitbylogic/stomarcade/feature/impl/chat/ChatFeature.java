package net.bitbylogic.stomarcade.feature.impl.chat;

import net.bitbylogic.stomarcade.StomArcadeServer;
import net.bitbylogic.stomarcade.feature.EventFeature;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import net.minestom.server.entity.Player;
import net.minestom.server.event.player.PlayerChatEvent;

public class ChatFeature extends EventFeature {

    public ChatFeature() {
        super("chat");

        node().addListener(PlayerChatEvent.class, event -> {
            Player player = event.getPlayer();
            event.setFormattedMessage(ChatMessages.FORMAT.get(Placeholder.unparsed("player", player.getUsername()), Placeholder.unparsed("message", event.getRawMessage())));
        });
    }

    @Override
    public void onEnable() {
        StomArcadeServer.messages().registerGroup(new ChatMessages());
    }
}
