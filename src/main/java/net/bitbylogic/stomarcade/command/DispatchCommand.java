package net.bitbylogic.stomarcade.command;

import net.bitbylogic.rps.listener.ListenerComponent;
import net.bitbylogic.stomarcade.StomArcadeServer;
import net.bitbylogic.stomarcade.permission.command.PermissionedCommand;
import net.bitbylogic.stomarcade.util.ServerUtil;
import net.bitbylogic.stomarcade.util.message.MessageUtil;
import net.minestom.server.command.builder.arguments.ArgumentString;
import net.minestom.server.command.builder.arguments.ArgumentType;
import net.minestom.server.entity.Player;

public class DispatchCommand extends PermissionedCommand {

    public DispatchCommand() {
        super("dispatch");

        setPermission("stomarcade.dispatch");

        setDefaultExecutor((sender, _) -> sender.sendMessage(MessageUtil.error("Usage: /dispatch <command>")));

        ArgumentString command = ArgumentType.String("command");

        addSyntax((sender, context) -> {
            String commandString = context.get(command);

            StomArcadeServer.redisClient().sendListenerMessage(new ListenerComponent("kardia_command")
                    .addData("playerId", (sender instanceof Player player) ? player.getUuid().toString() : ServerUtil.FALLBACK_UUID)
                    .addData("command", commandString)
            );
        }, command);
    }

}
