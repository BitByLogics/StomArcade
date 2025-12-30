package net.bitbylogic.stomarcade.feature.impl.serverlist;

import net.bitbylogic.stomarcade.message.MessageGroup;
import net.bitbylogic.stomarcade.message.MessageKey;

import java.util.List;

public class ServerListMessages extends MessageGroup {

    public static MessageKey MOTD;
    public static MessageKey VERSION_NAME;

    public ServerListMessages() {
        super("Server-List");
    }

    @Override
    public void register() {
        MOTD = register("MOTD", List.of("Nice", "One"));
        VERSION_NAME = register("Version-Name", "1.21.11");
    }

}
