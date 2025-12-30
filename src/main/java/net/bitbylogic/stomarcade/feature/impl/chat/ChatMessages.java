package net.bitbylogic.stomarcade.feature.impl.chat;

import net.bitbylogic.stomarcade.message.MessageGroup;
import net.bitbylogic.stomarcade.message.MessageKey;

public class ChatMessages extends MessageGroup {

    public static MessageKey FORMAT;

    public ChatMessages() {
        super("Chat");
    }

    @Override
    public void register() {
        FORMAT = register("Format", "<#deab90><player> <separator>• <white><message>");
    }

}
