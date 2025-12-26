package net.bitbylogic.stomarcade.command;

import net.bitbylogic.stomarcade.util.MessageUtil;
import net.kyori.adventure.text.Component;
import net.minestom.server.command.builder.Command;

public class VersionCommand extends Command {

    private static final Component INFO = MessageUtil.miniDeserialize(
            "<#fca311>Stom Arcade <dark_gray>• <#f5ebe0>Minestom" +
            "<newline>" +
            "<newline>" +
            "<#e5e5e5>Stom Arcade is running the latest version of <#ffb703><click:open_url:https://github.com/Minestom/Minestom>Minestom</click>" +
            "<#e5e5e5>, a powerful library for creating your own Minecraft servers from scratch."
    );

    public VersionCommand() {
        super("version", "plugins", "pl", "plugin");

        setDefaultExecutor((sender, context) -> sender.sendMessage(INFO));
    }

}
