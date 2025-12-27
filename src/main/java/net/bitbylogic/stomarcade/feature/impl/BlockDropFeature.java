package net.bitbylogic.stomarcade.feature.impl;

import net.bitbylogic.stomarcade.feature.EventFeature;
import net.bitbylogic.stomarcade.loot.LootTableManager;
import net.bitbylogic.stomarcade.util.ServerUtil;
import net.goldenstack.loot.LootContext;
import net.goldenstack.loot.util.VanillaInterface;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.sound.Sound;
import net.minestom.server.component.DataComponents;
import net.minestom.server.entity.GameMode;
import net.minestom.server.event.player.PlayerBlockBreakEvent;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.enchant.Enchantment;
import net.minestom.server.sound.SoundEvent;

import java.util.HashMap;
import java.util.Map;

public class BlockDropFeature extends EventFeature {

    public BlockDropFeature() {
        super("block_drop");

        node().addListener(PlayerBlockBreakEvent.class, event -> {

            if (event.getPlayer().getGameMode() == GameMode.CREATIVE) return;

            var material = event.getBlock().registry().material();

            if (material != null) {
                var held = event.getPlayer().getItemInMainHand();
                var enchs = held.get(DataComponents.ENCHANTMENTS);
                var tool = held.get(DataComponents.TOOL);

                if (!event.getBlock().registry().requiresTool() || (tool != null && tool.isCorrectForDrops(event.getBlock()))) {
                    Map<LootContext.Key<?>, Object> l = new HashMap<>();
                    l.put(LootContext.RANDOM, ServerUtil.RANDOM);

                    l.put(LootContext.TOOL, event.getPlayer().getItemInMainHand());
                    l.put(LootContext.BLOCK_STATE, event.getBlock());
                    l.put(LootContext.ORIGIN, event.getBlockPosition());

                    if (enchs.has(Enchantment.FORTUNE)) {
                        l.put(LootContext.ENCHANTMENT_ACTIVE, true);
                        l.put(LootContext.ENCHANTMENT_LEVEL, enchs.level(Enchantment.FORTUNE));
                    }

                    Key blockKey = event.getBlock().key();
                    String value = blockKey.value();
                    Key lootTableKey = Key.key("minecraft", "blocks/" + value);

                    LootTableManager.getLootTables().get(lootTableKey).blockDrop(
                            LootContext.from(
                                    VanillaInterface.defaults(),
                                    l
                            ),
                            event.getInstance(),
                            event.getBlockPosition()
                    );
                }

                if (held.has(DataComponents.DAMAGE)) {
                    var maxDmg = held.get(DataComponents.MAX_DAMAGE, 1);
                    var dmg = held.get(DataComponents.DAMAGE, 0) + tool.damagePerBlock();

                    if (dmg >= maxDmg) {
                        event.getPlayer().setItemInMainHand(ItemStack.AIR);
                        event.getPlayer().playSound(
                                Sound.sound()
                                        .type(SoundEvent.ENTITY_ITEM_BREAK)
                                        .build()
                        );
                    } else {
                        event.getPlayer().setItemInMainHand(
                                held.with(DataComponents.DAMAGE, dmg)
                        );
                    }
                }

            }
        });
    }

}
