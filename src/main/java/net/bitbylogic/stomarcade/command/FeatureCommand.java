package net.bitbylogic.stomarcade.command;

import io.github.togar2.pvp.feature.config.DefinedFeature;
import io.github.togar2.pvp.feature.food.VanillaFoodFeature;
import net.bitbylogic.stomarcade.StomArcadeServer;
import net.bitbylogic.stomarcade.feature.Feature;
import net.bitbylogic.stomarcade.feature.ServerFeature;
import net.bitbylogic.stomarcade.feature.impl.MinestomPVPFeature;
import net.bitbylogic.stomarcade.permission.command.PermissionedCommand;
import net.bitbylogic.stomarcade.util.message.MessageUtil;
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

            if (serverFeature.feature() instanceof MinestomPVPFeature pvpFeatureToEnable) {
                for (Feature enabledFeature : StomArcadeServer.features().enabledFeatures().values()) {
                    if (!(enabledFeature instanceof MinestomPVPFeature pvpFeature) || pvpFeatureToEnable.featureSet().listTypes().stream()
                            .noneMatch(featureType -> pvpFeature.featureSet().listTypes().contains(featureType))) {
                        continue;
                    }

                    sender.sendMessage(MessageUtil.error("Failed to enable feature <error_highlight><feature> <error_secondary>due to it conflicting with <error_highlight><enabled_feature><error_secondary>",
                            Placeholder.unparsed("feature", serverFeature.feature().id()),
                            Placeholder.unparsed("enabled_feature", enabledFeature.id())
                    ));
                    return;
                }
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
