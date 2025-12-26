package net.bitbylogic.stomarcade.command;

import net.bitbylogic.stomarcade.permission.command.PermissionedCommand;
import net.bitbylogic.stomarcade.util.PermissionUtil;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.minestom.server.command.CommandSender;
import net.minestom.server.command.builder.arguments.ArgumentEnum;
import net.minestom.server.command.builder.arguments.ArgumentType;
import net.minestom.server.command.builder.arguments.minecraft.ArgumentEntity;
import net.minestom.server.entity.Entity;
import net.minestom.server.entity.GameMode;
import net.minestom.server.entity.Player;
import net.minestom.server.utils.entity.EntityFinder;

import java.util.List;
import java.util.Locale;

public class GamemodeCommand extends PermissionedCommand {

    public GamemodeCommand() {
        super("gamemode", "gm");

        setPermission("stomarcade.gamemode");

        ArgumentEnum<GameMode> gamemode = ArgumentType.Enum("gamemode", GameMode.class).setFormat(ArgumentEnum.Format.LOWER_CASED);

        gamemode.setCallback((sender, exception) -> {
            sender.sendMessage(
                    Component.text("Invalid gamemode ", NamedTextColor.RED)
                            .append(Component.text(exception.getInput(), NamedTextColor.WHITE))
                            .append(Component.text("!")));
        });

        ArgumentEntity player = ArgumentType.Entity("targets").onlyPlayers(true);

        setDefaultExecutor((sender, context) -> {
            String commandName = context.getCommandName();

            sender.sendMessage(Component.text("Usage: /" + commandName + " <gamemode> [targets]", NamedTextColor.RED));
        });

        addSyntax((sender, context) -> {
            if (!(sender instanceof Player p)) {
                sender.sendMessage(Component.text("Please run this command in-game.", NamedTextColor.RED));
                return;
            }

            GameMode mode = context.get(gamemode);

            executeSelf(p, mode);
        }, gamemode);

        addConditionalSyntax((sender, _) -> PermissionUtil.has(sender, "stomarcade.gamemode.other"), (sender, context) -> {
            EntityFinder finder = context.get(player);
            GameMode mode = context.get(gamemode);

            executeOthers(sender, mode, finder.find(sender));
        }, gamemode, player);
    }

    private void executeOthers(CommandSender sender, GameMode mode, List<Entity> entities) {
        if (entities.isEmpty()) {
            if (sender instanceof Player) {
                sender.sendMessage(Component.translatable("argument.entity.notfound.player", NamedTextColor.RED));
                return;
            }

            sender.sendMessage(Component.text("No player was found", NamedTextColor.RED));
        }

        for (Entity entity : entities) {
            if (!(entity instanceof Player p)) {
                continue;
            }

            if (p == sender) {
                executeSelf((Player) sender, mode);
                continue;
            }

            p.setGameMode(mode);

            String gamemodeString = "gameMode." + mode.name().toLowerCase(Locale.ROOT);
            Component gamemodeComponent = Component.translatable(gamemodeString);
            Component playerName = p.getDisplayName() == null ? p.getName() : p.getDisplayName();

            p.sendMessage(Component.translatable("gameMode.changed", gamemodeComponent));
            sender.sendMessage(Component.translatable("commands.gamemode.success.other", playerName, gamemodeComponent));
        }
    }

    private void executeSelf(Player sender, GameMode mode) {
        sender.setGameMode(mode);

        String gamemodeString = "gameMode." + mode.name().toLowerCase(Locale.ROOT);
        Component gamemodeComponent = Component.translatable(gamemodeString);

        sender.sendMessage(Component.translatable("commands.gamemode.success.self", gamemodeComponent));
    }
}