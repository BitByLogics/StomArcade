package net.bitbylogic.stomarcade.command;

import net.bitbylogic.stomarcade.StomArcadeServer;
import net.bitbylogic.stomarcade.feature.ServerFeature;
import net.bitbylogic.stomarcade.permission.command.PermissionedCommand;
import net.bitbylogic.stomarcade.util.message.MessageUtil;
import net.minestom.server.command.builder.arguments.ArgumentEnum;
import net.minestom.server.command.builder.arguments.ArgumentLiteral;
import net.minestom.server.command.builder.arguments.ArgumentType;

public class FeatureCommand extends PermissionedCommand {

    public FeatureCommand() {
        super("feature");

        setPermission("stomarcade.feature");

        setDefaultExecutor((sender, _) -> sender.sendMessage(MessageUtil.primary("Usage: /feature <disable/enable> <feature>")));

        ArgumentLiteral enable = ArgumentType.Literal("enable");
        ArgumentLiteral disable = ArgumentType.Literal("disable");

        ArgumentEnum<ServerFeature> feature = ArgumentType.Enum("feature", ServerFeature.class);

        addSyntax((sender, context) -> {
            ServerFeature serverFeature = context.get(feature);

            if (StomArcadeServer.features().isFeatureEnabled(serverFeature)) {
                sender.sendMessage(MessageUtil.error("Feature <error_highlight>" + serverFeature.name() + " <error_secondary>is already enabled!"));
                return;
            }

            StomArcadeServer.features().enableFeature(serverFeature);
            sender.sendMessage(MessageUtil.success("Enabled feature <success_highlight>" + serverFeature.name()));
        }, enable, feature);

        addSyntax((sender, context) -> {
            ServerFeature serverFeature = context.get(feature);

            if (!StomArcadeServer.features().isFeatureEnabled(serverFeature)) {
                sender.sendMessage(MessageUtil.error("Feature <error_highlight>" + serverFeature.name() + " <error_secondary>is already disabled!"));
                return;
            }

            StomArcadeServer.features().disableFeature(serverFeature);
            sender.sendMessage(MessageUtil.success("Disabled feature <success_highlight>" + serverFeature.name()));
        }, disable, feature);
    }

}
