package net.bitbylogic.stomarcade.feature.impl;

import net.bitbylogic.stomarcade.feature.EventFeature;
import net.minestom.server.entity.Entity;
import net.minestom.server.entity.Player;
import net.minestom.server.event.item.PickupItemEvent;

public class ItemPickupFeature extends EventFeature {

    public ItemPickupFeature() {
        super("item_pickup");

        node().addListener(PickupItemEvent.class, event -> {
            Entity entity = event.getEntity();

            if (!(entity instanceof Player player)) {
                return;
            }

            boolean added = player.getInventory().addItemStack(event.getItemStack());
            event.setCancelled(!added);
        });
    }

}
