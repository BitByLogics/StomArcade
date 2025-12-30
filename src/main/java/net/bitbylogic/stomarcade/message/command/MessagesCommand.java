package net.bitbylogic.stomarcade.message.command;

import net.bitbylogic.stomarcade.StomArcadeServer;
import net.bitbylogic.stomarcade.message.MessageKey;
import net.bitbylogic.stomarcade.permission.command.PermissionedCommand;
import net.bitbylogic.stomarcade.util.message.MessageUtil;
import net.minestom.server.command.builder.arguments.ArgumentLiteral;
import net.minestom.server.command.builder.arguments.ArgumentString;
import net.minestom.server.command.builder.arguments.ArgumentType;

import java.io.IOException;

public class MessagesCommand extends PermissionedCommand {

    public MessagesCommand() {
        super("messages");

        setPermission("stomarcade.messages");

        setDefaultExecutor((sender, _) -> {
            sender.sendMessage(MessageUtil.error("Messages Commands"));
            sender.sendMessage(MessageUtil.error("/messages <key>"));
            sender.sendMessage(MessageUtil.error("/messages reload"));
        });

        ArgumentString key = ArgumentType.String("key");
        ArgumentLiteral reload = ArgumentType.Literal("reload");

        addSyntax((sender, context) -> {
            String keyString = context.get(key);
            MessageKey messageKey = StomArcadeServer.messages().getByPath(keyString);

            if (messageKey == null) {
                sender.sendMessage(MessageUtil.error("Unable to find message key: " + keyString));
                return;
            }

            messageKey.send(sender);
        }, key);

        addSyntax((sender, _) -> {
            try {
                StomArcadeServer.messages().reload();
            } catch (IOException e) {
                sender.sendMessage(MessageUtil.error("Failed to reload messages"));
                StomArcadeServer.LOGGER.error("Failed to reload messages", e);
                return;
            }

            sender.sendMessage(MessageUtil.success("Messages reloaded!"));
        }, reload);
    }

}
