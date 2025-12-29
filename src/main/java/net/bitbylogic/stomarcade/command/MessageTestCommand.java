package net.bitbylogic.stomarcade.command;

import net.bitbylogic.stomarcade.permission.command.PermissionedCommand;
import net.bitbylogic.stomarcade.util.MessageUtil;
import net.kyori.adventure.text.Component;
import net.minestom.server.command.builder.arguments.Argument;
import net.minestom.server.command.builder.arguments.ArgumentString;

public class MessageTestCommand extends PermissionedCommand {

    public MessageTestCommand() {
        super("messagetest");

        setPermission("stomarcade.messagetest");

        setDefaultExecutor((sender, _) -> sender.sendMessage(Component.text("Usage: /messagetest <message>")));

        Argument<String> message = new ArgumentString("message");

        addSyntax((sender, context) -> sender.sendMessage(MessageUtil.miniDeserialize(context.get(message))), message);
    }

}
