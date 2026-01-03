package net.bitbylogic.stomarcade.command;

import net.bitbylogic.rps.listener.ListenerComponent;
import net.bitbylogic.stomarcade.StomArcadeServer;
import net.bitbylogic.stomarcade.permission.command.PermissionedCommand;
import net.bitbylogic.stomarcade.util.message.MessageUtil;
import net.bitbylogic.utils.StringUtil;
import net.minestom.server.command.builder.arguments.ArgumentStringArray;

public class AnnounceCommand extends PermissionedCommand {

    public AnnounceCommand() {
        super("announce", "broadcast");

        setPermission("stomarcade.announce");

        setDefaultExecutor((sender, _) -> sender.sendMessage(MessageUtil.error("Usage: /announce <message>")));

        ArgumentStringArray message = new ArgumentStringArray("message");

        addSyntax((_, context) -> {
            String announcementMessage = StringUtil.join(0, context.get(message), " ");
            StomArcadeServer.redisClient().sendListenerMessage(
                    new ListenerComponent("announce")
                    .selfActivation(true)
                    .addData("message", announcementMessage)
            );
        }, message);
    }

}
