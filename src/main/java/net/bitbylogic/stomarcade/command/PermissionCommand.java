package net.bitbylogic.stomarcade.command;

import net.bitbylogic.stomarcade.permission.command.PermissionedCommand;
import net.bitbylogic.stomarcade.permission.manager.PermissionManager;
import net.bitbylogic.stomarcade.util.PermissionUtil;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.minestom.server.command.CommandSender;
import net.minestom.server.command.builder.arguments.Argument;
import net.minestom.server.command.builder.arguments.ArgumentBoolean;
import net.minestom.server.command.builder.arguments.ArgumentLiteral;
import net.minestom.server.command.builder.arguments.ArgumentType;
import net.minestom.server.command.builder.arguments.minecraft.ArgumentEntity;
import net.minestom.server.command.builder.suggestion.SuggestionEntry;
import net.minestom.server.entity.Player;
import org.jetbrains.annotations.NotNull;

public class PermissionCommand extends PermissionedCommand {

    public PermissionCommand() {
        super("permission", "permissions");

        setPermission("stomarcade.permission");

        ArgumentLiteral check = ArgumentType.Literal("check");
        ArgumentLiteral set = ArgumentType.Literal("set");
        ArgumentLiteral unset = ArgumentType.Literal("unset");

        Argument<String> permission = ArgumentType.String("permission").setSuggestionCallback((sender, context, suggestion) ->
                PermissionManager.registeredPermissions().forEach(s -> suggestion.addEntry(new SuggestionEntry(s))));

        ArgumentEntity target = ArgumentType.Entity("target").onlyPlayers(true);
        ArgumentBoolean value = ArgumentType.Boolean("value");

        setDefaultExecutor((sender, _) -> {
            sender.sendMessage(Component.text("Usage: /permission <check|set|unset> <target> <permission> [true/false]", NamedTextColor.GRAY));
        });

        addSyntax((sender, context) -> {
            Player targetPlayer = context.get(target).findFirstPlayer(sender);

            if (targetPlayer == null) {
                sender.sendMessage(Component.text("Unable to locate player with the username: " + context.getRaw(target), NamedTextColor.RED));
                return;
            }

            String permissionString = context.get(permission);
            boolean has = PermissionUtil.has(targetPlayer, permissionString);

            if (!has) {
                sender.sendMessage(Component.text(targetPlayer.getUsername() + " does not have permission " + permissionString, NamedTextColor.RED));
                return;
            }

            sender.sendMessage(Component.text(targetPlayer.getUsername() + " has permission " + permissionString, NamedTextColor.GREEN));
        }, check, permission, target);

        addSyntax((sender, context) -> {
            Player targetPlayer = context.get(target).findFirstPlayer(sender);

            if (targetPlayer == null) {
                sender.sendMessage(Component.text("Unable to locate player with the username: " + context.getRaw(target), NamedTextColor.RED));
                return;
            }

            String permissionString = context.get(permission);
            boolean has = PermissionUtil.has(targetPlayer, permissionString);

            boolean valueBoolean = context.getOrDefault(value, !has);

            PermissionUtil.set(targetPlayer, permissionString, valueBoolean);
            targetPlayer.refreshCommands();

            sender.sendMessage(Component.text("Permission " + permissionString + " set to " + valueBoolean + " for " + targetPlayer.getUsername(), NamedTextColor.GREEN));
        }, set, target, permission, value);

        addSyntax((sender, context) -> {
            Player targetPlayer = context.get(target).findFirstPlayer(sender);

            if (targetPlayer == null) {
                sender.sendMessage(Component.text("Unable to locate player with the username: " + context.getRaw(target), NamedTextColor.RED));
                return;
            }

            String permissionString = context.get(permission);

            PermissionUtil.unset(targetPlayer, permissionString);
            targetPlayer.refreshCommands();

            sender.sendMessage(Component.text("Permission " + permissionString + " unset for " + targetPlayer.getUsername(), NamedTextColor.GREEN));
        }, unset, permission, target);
    }

    private boolean validatePermission(@NotNull CommandSender sender) {
        if (!(sender instanceof Player player)) {
            return true;
        }

        boolean hasPermission = player.getPermissionLevel() >= 4;

        if (!hasPermission) {
            sender.sendMessage(Component.text("Requires elevated privileges.", NamedTextColor.RED));
        }

        return hasPermission;
    }

}
