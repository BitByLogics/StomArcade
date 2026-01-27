package net.bitbylogic.stomarcade.cosmetic;

import net.kyori.adventure.text.Component;
import net.minestom.server.item.ItemStack;
import org.jetbrains.annotations.NotNull;

public interface Cosmetic {

    @NotNull String id();

    @NotNull String type();

    @NotNull String category();

    @NotNull String permission();

    @NotNull Component displayName();

    @NotNull ItemStack displayItem();

    long price();

}
