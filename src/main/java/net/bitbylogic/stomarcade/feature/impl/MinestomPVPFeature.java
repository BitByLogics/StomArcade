package net.bitbylogic.stomarcade.feature.impl;

import io.github.togar2.pvp.feature.CombatFeatureSet;
import io.github.togar2.pvp.feature.CombatFeatures;
import io.github.togar2.pvp.feature.config.DefinedFeature;
import net.bitbylogic.stomarcade.feature.SimpleFeature;
import net.minestom.server.MinecraftServer;
import net.minestom.server.event.EventNode;
import net.minestom.server.event.trait.EntityInstanceEvent;
import org.jetbrains.annotations.NotNull;

public class MinestomPVPFeature extends SimpleFeature {

    private final CombatFeatureSet featureSet;
    private final EventNode<EntityInstanceEvent> node;

    public MinestomPVPFeature(@NotNull String id, @NotNull DefinedFeature<?> feature) {
        super(id);

        this.featureSet = CombatFeatures.empty().add(feature).build();
        this.node = featureSet.createNode();
    }

    public MinestomPVPFeature(@NotNull String id, @NotNull CombatFeatureSet featureSet) {
        super(id);

        this.featureSet = featureSet;
        this.node = featureSet.createNode();
    }

    @Override
    public void onEnable() {
        MinecraftServer.getGlobalEventHandler().addChild(node);
    }

    @Override
    public void onDisable() {
        MinecraftServer.getGlobalEventHandler().removeChild(node);
    }

    protected CombatFeatureSet getFeatureSet() {
        return featureSet;
    }

}
