package net.bitbylogic.stomarcade.feature.manager;

import net.bitbylogic.stomarcade.feature.ServerFeature;
import net.bitbylogic.stomarcade.feature.EventFeature;
import net.bitbylogic.stomarcade.feature.Feature;
import net.minestom.server.MinecraftServer;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

public class FeatureManager {

    private final Map<String, Feature> enabledFeatures = new HashMap<>();

    public void enableFeature(@NotNull ServerFeature... features) {
        for (ServerFeature feature : features) {
            enableFeature(feature.getFeature());
        }
    }

    public void enableFeature(@NotNull Feature feature) {
        if (enabledFeatures.containsKey(feature.id())) {
            return;
        }

        enabledFeatures.put(feature.id(), feature);

        if (feature instanceof EventFeature eventFeature) {
            MinecraftServer.getGlobalEventHandler().addChild(eventFeature.node());
        }

        feature.onEnable();
    }

    public void disableFeature(@NotNull ServerFeature... features) {
        for (ServerFeature feature : features) {
            disableFeature(feature.getFeature());
        }
    }

    public void disableFeature(@NotNull Feature feature) {
        boolean removed = enabledFeatures.remove(feature.id()) != null;

        if (!removed) {
            return;
        }

        if (feature instanceof EventFeature eventFeature) {
            MinecraftServer.getGlobalEventHandler().removeChild(eventFeature.node());
        }

        feature.onDisable();
    }

    public Map<String, Feature> getEnabledFeatures() {
        return Map.copyOf(enabledFeatures);
    }

}
