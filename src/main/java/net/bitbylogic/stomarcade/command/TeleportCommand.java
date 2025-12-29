package net.bitbylogic.stomarcade.command;

import net.bitbylogic.stomarcade.permission.command.PermissionedCommand;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.minestom.server.command.builder.arguments.ArgumentType;
import net.minestom.server.command.builder.arguments.minecraft.ArgumentEntity;
import net.minestom.server.command.builder.arguments.relative.ArgumentRelativeVec3;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.entity.Player;
import net.minestom.server.utils.location.RelativeVec;

public class TeleportCommand extends PermissionedCommand {

    public TeleportCommand() {
        super("teleport", "tp");

        setPermission("stomarcade.teleport");

        setDefaultExecutor((sender, _) -> sender.sendMessage("Usage: /teleport <position>"));

        ArgumentRelativeVec3 position = ArgumentType.RelativeVec3("position");
        ArgumentEntity target = ArgumentType.Entity("target").onlyPlayers(true);

        addSyntax((sender, context) -> {
            if (!(sender instanceof Player player)) {
                sender.sendMessage(Component.text("Please run this command in-game.", NamedTextColor.RED));
                return;
            }

            RelativeVec relative = context.get(position);
            Pos pos = relative.from(player).asPos();

            player.teleport(pos);

            player.sendMessage(Component.text("Teleported to " + pos.x() + ", " + pos.y() + ", " + pos.z(), NamedTextColor.GREEN));
        }, position);

        addSyntax((sender, context) -> {
            if (!(sender instanceof Player player)) {
                sender.sendMessage(Component.text("Please run this command in-game.", NamedTextColor.RED));
                return;
            }

            Player targetPlayer = context.get(target).findFirstPlayer(sender);

            if (targetPlayer == null) {
                sender.sendMessage(Component.text("Unable to locate player with the username: " + context.getRaw(target), NamedTextColor.RED));
                return;
            }

            player.teleport(targetPlayer.getPosition());
            player.sendMessage(Component.text("Teleported to " + targetPlayer.getUsername(), NamedTextColor.GREEN));
        }, target);
    }

}
