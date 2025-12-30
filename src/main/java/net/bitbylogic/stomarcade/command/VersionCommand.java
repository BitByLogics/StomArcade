package net.bitbylogic.stomarcade.command;

import net.bitbylogic.stomarcade.message.messages.BrandingMessages;
import net.bitbylogic.stomarcade.util.MessageUtil;
import net.kyori.adventure.text.Component;
import net.minestom.server.command.builder.Command;

public class VersionCommand extends Command {

    public VersionCommand() {
        super("version", "plugins", "pl", "plugin");

        setDefaultExecutor((sender, context) -> BrandingMessages.VERSION_INFO.send(sender));
    }

}
