package net.bitbylogic.stomarcade.feature;

import net.bitbylogic.stomarcade.feature.impl.BlockDropFeature;
import org.jetbrains.annotations.NotNull;

public enum ArcadeFeature {

    BLOCK_DROP(new BlockDropFeature());

    final Feature feature;

    ArcadeFeature(@NotNull Feature feature) {
        this.feature = feature;
    }

    public Feature getFeature() {
        return feature;
    }

}
