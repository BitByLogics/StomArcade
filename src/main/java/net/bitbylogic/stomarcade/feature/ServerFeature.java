package net.bitbylogic.stomarcade.feature;

import io.github.togar2.pvp.feature.CombatFeatures;
import net.bitbylogic.stomarcade.feature.impl.*;
import net.bitbylogic.stomarcade.feature.impl.chat.ChatFeature;
import net.bitbylogic.stomarcade.feature.impl.serverlist.ServerListFeature;
import net.bitbylogic.stomarcade.feature.impl.tablist.TablistFeature;
import org.jetbrains.annotations.NotNull;

public enum ServerFeature {

    BLOCK_BREAK(new BlockBreakFeature()),
    BLOCK_DROP(new BlockDropFeature()),
    ITEM_PICKUP(new ItemPickupFeature()),
    ITEM_DROP(new ItemDropFeature(5, 1.2)),
    TABLIST(new TablistFeature()),
    CHAT(new ChatFeature()),
    SERVER_LIST(new ServerListFeature()),
    PICK_BLOCK(new PickBlockFeature()),
    SPAWN(new SpawnFeature()),

    // Minestom PVP
    MODERN_VANILLA(new MinestomPVPFeature("modern_vanilla", CombatFeatures.modernVanilla())),
    LEGACY_VANILLA(new MinestomPVPFeature("legacy_vanilla", CombatFeatures.legacyVanilla())),
    ARMOR(new MinestomPVPFeature("armor", CombatFeatures.VANILLA_ARMOR)),
    ATTACK(new MinestomPVPFeature("attack", CombatFeatures.VANILLA_ATTACK)),
    CRITICAL(new MinestomPVPFeature("critical", CombatFeatures.VANILLA_CRITICAL)),
    SWEEPING(new MinestomPVPFeature("sweeping", CombatFeatures.VANILLA_SWEEPING)),
    EQUIPMENT(new MinestomPVPFeature("equipment", CombatFeatures.VANILLA_EQUIPMENT)),
    BLOCK(new MinestomPVPFeature("block", CombatFeatures.VANILLA_BLOCK)),
    ATTACK_COOLDOWN(new MinestomPVPFeature("attack_cooldown", CombatFeatures.VANILLA_ATTACK_COOLDOWN)),
    ITEM_COOLDOWN(new MinestomPVPFeature("item_cooldown", CombatFeatures.VANILLA_ITEM_COOLDOWN)),
    DAMAGE(new MinestomPVPFeature("damage", CombatFeatures.VANILLA_DAMAGE)),
    EFFECT(new MinestomPVPFeature("effect", CombatFeatures.VANILLA_EFFECT)),
    ENCHANTMENT(new MinestomPVPFeature("enchantment", CombatFeatures.VANILLA_ENCHANTMENT)),
    EXPLOSION(new ExplosionFeature()),
    EXPLOSIVE(new MinestomPVPFeature("explosive", CombatFeatures.VANILLA_EXPLOSIVE)),
    FALL(new MinestomPVPFeature("fall", CombatFeatures.VANILLA_FALL)),
    EXHAUSTION(new MinestomPVPFeature("exhaustion", CombatFeatures.VANILLA_EXHAUSTION)),
    FOOD(new MinestomPVPFeature("food", CombatFeatures.VANILLA_FOOD)),
    REGENERATION(new MinestomPVPFeature("regeneration", CombatFeatures.VANILLA_REGENERATION)),
    ITEM_DAMAGE(new MinestomPVPFeature("item_damage", CombatFeatures.VANILLA_ITEM_DAMAGE)),
    KNOCKBACK(new MinestomPVPFeature("knockback", CombatFeatures.VANILLA_KNOCKBACK)),
    POTION(new MinestomPVPFeature("potion", CombatFeatures.VANILLA_POTION)),
    BOW(new MinestomPVPFeature("bow", CombatFeatures.VANILLA_BOW)),
    CROSSBOW(new MinestomPVPFeature("crossbow", CombatFeatures.VANILLA_CROSSBOW)),
    FISHING_ROD(new MinestomPVPFeature("fishing_rod", CombatFeatures.VANILLA_FISHING_ROD)),
    MISC_PROJECTILE(new MinestomPVPFeature("misc_projectile", CombatFeatures.VANILLA_MISC_PROJECTILE)),
    PROJECTILE_ITEM(new MinestomPVPFeature("projectile_item", CombatFeatures.VANILLA_PROJECTILE_ITEM)),
    TRIDENT(new MinestomPVPFeature("trident", CombatFeatures.VANILLA_TRIDENT)),
    SPECTATE(new MinestomPVPFeature("spectate", CombatFeatures.VANILLA_SPECTATE)),
    PLAYER_STATE(new MinestomPVPFeature("player_state", CombatFeatures.VANILLA_PLAYER_STATE)),
    TOTEM(new MinestomPVPFeature("totem", CombatFeatures.VANILLA_TOTEM)),
    DEATH_MESSAGE(new MinestomPVPFeature("death_message", CombatFeatures.VANILLA_DEATH_MESSAGE)),
    LEGACY_BLOCK(new MinestomPVPFeature("block", CombatFeatures.LEGACY_VANILLA_BLOCK)),
    FAIR_RAISING_KNOCKBACK(new MinestomPVPFeature("fair_raising_knockback", CombatFeatures.FAIR_RISING_KNOCKBACK)),
    FAIR_FALLING_KNOCKBACK(new MinestomPVPFeature("fair_falling_knockback", CombatFeatures.FAIR_RISING_FALLING_KNOCKBACK));

    final Feature feature;

    ServerFeature(@NotNull Feature feature) {
        this.feature = feature;
    }

    public Feature feature() {
        return feature;
    }

}
