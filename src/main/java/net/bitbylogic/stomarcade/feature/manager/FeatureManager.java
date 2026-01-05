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
            enableFeature(feature.feature());
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

    public Feature getFeature(@NotNull String id) {
        return enabledFeatures.get(id);
    }

    public void disableFeature(@NotNull ServerFeature... features) {
        for (ServerFeature feature : features) {
            disableFeature(feature.feature());
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

    public boolean isFeatureEnabled(@NotNull ServerFeature feature) {
        return enabledFeatures.containsKey(feature.feature().id());
    }

    public Map<String, Feature> enabledFeatures() {
        return Map.copyOf(enabledFeatures);
    }

}
