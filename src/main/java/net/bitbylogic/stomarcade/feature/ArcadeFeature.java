package net.bitbylogic.stomarcade.feature;

import net.bitbylogic.stomarcade.feature.impl.BlockDropFeature;
import net.bitbylogic.stomarcade.feature.impl.ItemPickupFeature;
import org.jetbrains.annotations.NotNull;

public enum ArcadeFeature {

    BLOCK_DROP(new BlockDropFeature()),
    ITEM_PICKUP(new ItemPickupFeature());

    final Feature feature;

    ArcadeFeature(@NotNull Feature feature) {
        this.feature = feature;
    }

    public Feature getFeature() {
        return feature;
    }

}
