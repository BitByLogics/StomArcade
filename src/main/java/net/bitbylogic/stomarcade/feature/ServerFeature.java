package net.bitbylogic.stomarcade.feature;

import net.bitbylogic.stomarcade.feature.impl.BlockDropFeature;
import net.bitbylogic.stomarcade.feature.impl.ItemDropFeature;
import net.bitbylogic.stomarcade.feature.impl.ItemPickupFeature;
import net.bitbylogic.stomarcade.feature.impl.chat.ChatFeature;
import net.bitbylogic.stomarcade.feature.impl.serverlist.ServerListFeature;
import net.bitbylogic.stomarcade.feature.impl.tablist.TablistFeature;
import org.jetbrains.annotations.NotNull;

public enum ServerFeature {

    BLOCK_DROP(new BlockDropFeature()),
    ITEM_PICKUP(new ItemPickupFeature()),
    ITEM_DROP(new ItemDropFeature(5, 1.2)),
    TABLIST(new TablistFeature()),
    CHAT(new ChatFeature()),
    SERVER_LIST(new ServerListFeature());

    final Feature feature;

    ServerFeature(@NotNull Feature feature) {
        this.feature = feature;
    }

    public Feature getFeature() {
        return feature;
    }

}
