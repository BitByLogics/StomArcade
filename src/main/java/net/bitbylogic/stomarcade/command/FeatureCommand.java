package net.bitbylogic.stomarcade.command;

import net.bitbylogic.stomarcade.StomArcadeServer;
import net.bitbylogic.stomarcade.feature.Feature;
import net.bitbylogic.stomarcade.feature.ServerFeature;
import net.bitbylogic.stomarcade.permission.command.PermissionedCommand;
import net.bitbylogic.stomarcade.util.message.MessageUtil;
import net.bitbylogic.utils.StringUtil;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import net.minestom.server.command.builder.arguments.ArgumentEnum;
import net.minestom.server.command.builder.arguments.ArgumentLiteral;
import net.minestom.server.command.builder.arguments.ArgumentType;

import java.util.ArrayList;
import java.util.List;

public class FeatureCommand extends PermissionedCommand {

    public FeatureCommand() {
        super("feature");

        setPermission("stomarcade.feature");

        setDefaultExecutor((sender, _) -> sender.sendMessage(MessageUtil.primary("Usage: /feature <disable/enable/reload> <feature>")));

        ArgumentLiteral enable = ArgumentType.Literal("enable");
        ArgumentLiteral disable = ArgumentType.Literal("disable");
        ArgumentLiteral list = ArgumentType.Literal("list");
        ArgumentLiteral reload = ArgumentType.Literal("reload");

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

        addSyntax((sender, _) -> {
            List<String> enabledFeatures = new ArrayList<>();
            StomArcadeServer.features().enabledFeatures().values().forEach(eFeature -> enabledFeatures.add(eFeature.id()));

            sender.sendMessage(MessageUtil.primary("Enabled Features: <highlight><features>", Placeholder.parsed("features", String.join("<separator>, <highlight>", enabledFeatures))));
        }, list);

        addSyntax((sender, context) -> {
            ServerFeature serverFeature = context.get(feature);

            if (!StomArcadeServer.features().isFeatureEnabled(serverFeature)) {
                sender.sendMessage(MessageUtil.error("Feature <error_highlight>" + serverFeature.name() + " <error_secondary>is not enabled!"));
                return;
            }

            Feature loadedFeature = StomArcadeServer.features().enabledFeatures().get(serverFeature.feature().id());

            loadedFeature.reloadConfig();
            sender.sendMessage(MessageUtil.success("Reloaded feature <success_highlight>" + serverFeature.name()));
        }, reload, feature);
    }

}
