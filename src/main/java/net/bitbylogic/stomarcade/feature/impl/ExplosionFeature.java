package net.bitbylogic.stomarcade.feature.impl;

import io.github.togar2.pvp.feature.CombatFeatures;
import io.github.togar2.pvp.feature.FeatureType;
import net.minestom.server.MinecraftServer;
import net.minestom.server.instance.Instance;

public class ExplosionFeature extends MinestomPVPFeature {

    public ExplosionFeature() {
        super("explosion", CombatFeatures.VANILLA_EXPLOSION);
    }

    @Override
    public void onEnable() {
        super.onEnable();

        for (Instance instance : MinecraftServer.getInstanceManager().getInstances()) {
            instance.setExplosionSupplier(featureSet().get(FeatureType.EXPLOSION).getExplosionSupplier());
        }
    }

    @Override
    public void onDisable() {
        super.onDisable();

        for (Instance instance : MinecraftServer.getInstanceManager().getInstances()) {
            instance.setExplosionSupplier(null);
        }
    }
}
