package net.bitbylogic.stomarcade.command;

import net.bitbylogic.rps.listener.ListenerComponent;
import net.bitbylogic.stomarcade.StomArcadeServer;
import net.bitbylogic.stomarcade.permission.command.PermissionedCommand;
import net.bitbylogic.stomarcade.util.message.MessageUtil;
import net.bitbylogic.utils.StringUtil;
import net.minestom.server.command.builder.arguments.ArgumentStringArray;
import net.minestom.server.entity.Player;

public class StaffChatCommand extends PermissionedCommand {

    public StaffChatCommand() {
        super("staffchat", "sc");

        setPermission("stomarcade.staffchat");

        setDefaultExecutor((sender, _) -> sender.sendMessage(MessageUtil.error("Usage: /staffchat <message>")));

        ArgumentStringArray message = new ArgumentStringArray("message");

        addSyntax((sender, context) -> {
            String announcementMessage = StringUtil.join(0, context.get(message), " ");
            StomArcadeServer.redisClient().sendListenerMessage(
                    new ListenerComponent("staff_message")
                            .selfActivation(true)
                            .addData("message", announcementMessage)
                            .addData("playerName", (sender instanceof Player player) ? player.getUsername() : "CONSOLE")
            );
        }, message);
    }

}
