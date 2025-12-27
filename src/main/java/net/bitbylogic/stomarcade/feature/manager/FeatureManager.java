package net.bitbylogic.stomarcade.feature.manager;

import net.bitbylogic.stomarcade.feature.ArcadeFeature;
import net.bitbylogic.stomarcade.feature.Feature;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

public class FeatureManager {

    private final Map<String, Feature> enabledFeatures = new HashMap<>();

    public void enableFeature(@NotNull ArcadeFeature... features) {
        for (ArcadeFeature feature : features) {
            enableFeature(feature.getFeature());
        }
    }

    public void enableFeature(@NotNull Feature feature) {
        if (enabledFeatures.containsKey(feature.id())) {
            return;
        }

        enabledFeatures.put(feature.id(), feature);
        feature.onEnable();
    }

    public void disableFeature(@NotNull ArcadeFeature... features) {
        for (ArcadeFeature feature : features) {
            disableFeature(feature.getFeature());
        }
    }

    public void disableFeature(@NotNull Feature feature) {
        boolean removed = enabledFeatures.remove(feature.id()) != null;

        if (!removed) {
            return;
        }

        feature.onDisable();
    }

    public Map<String, Feature> getEnabledFeatures() {
        return Map.copyOf(enabledFeatures);
    }

}
