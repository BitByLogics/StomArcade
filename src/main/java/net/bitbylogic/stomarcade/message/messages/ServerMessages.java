package net.bitbylogic.stomarcade.message.messages;

import net.bitbylogic.stomarcade.message.MessageGroup;
import net.bitbylogic.stomarcade.message.MessageKey;

public class ServerMessages extends MessageGroup {

    public static MessageKey ANNOUNCEMENT;

    public static MessageKey ANNOUNCEMENT_TITLE;
    public static MessageKey ANNOUNCEMENT_SUBTITLE;

    public static MessageKey STAFF_CHAT_FORMAT;

    public ServerMessages() {
        super("Server");
    }

    @Override
    public void register() {
        ANNOUNCEMENT = register("Announcement", "<primary><bold>ᴀɴɴᴏᴜɴᴄᴇᴍᴇɴᴛ <reset><separator>☰ <secondary><message>");

        ANNOUNCEMENT_TITLE = register("Announcement-Title", "<primary><bold>ᴀɴɴᴏᴜɴᴄᴇᴍᴇɴᴛ");
        ANNOUNCEMENT_SUBTITLE = register("Announcement-Subtitle", "<secondary><message>");

        STAFF_CHAT_FORMAT = register("Staff-Chat-Format", "<red>ꜱᴛᴀꜰꜰ <separator>☰ <highlight><player> <secondary><message>");
    }

}
