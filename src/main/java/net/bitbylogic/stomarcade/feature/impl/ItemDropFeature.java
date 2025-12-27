package net.bitbylogic.stomarcade.feature.impl;

import net.bitbylogic.stomarcade.feature.EventFeature;
import net.bitbylogic.stomarcade.util.ServerUtil;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.coordinate.Vec;
import net.minestom.server.entity.ItemEntity;
import net.minestom.server.entity.Player;
import net.minestom.server.event.item.ItemDropEvent;

import java.time.Duration;

public class ItemDropFeature extends EventFeature {

    public ItemDropFeature(double throwSpeed, double verticalBoost) {
        super("item_drop");

        node().addListener(ItemDropEvent.class, event -> {
            Player player = event.getPlayer();

            Pos eyePos = player.getPosition().add(0, player.getEyeHeight() - 0.3, 0);

            ItemEntity item = new ItemEntity(event.getItemStack());
            item.setPickupDelay(Duration.ofSeconds(2));
            item.setInstance(player.getInstance(), eyePos);

            Vec direction = eyePos.direction().normalize();

            item.setVelocity(direction.mul(throwSpeed).add(0, verticalBoost, 0));
            item.spawn();
        });

    }

}
