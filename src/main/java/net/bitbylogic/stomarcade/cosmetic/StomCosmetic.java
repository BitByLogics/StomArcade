package net.bitbylogic.stomarcade.cosmetic;

import net.kyori.adventure.text.Component;
import net.minestom.server.item.ItemStack;
import org.jetbrains.annotations.NotNull;

public class StomCosmetic implements Cosmetic {

    private final @NotNull String id;
    private final @NotNull String type;
    private final @NotNull String category;
    private final @NotNull String permission;
    private final @NotNull Component displayName;
    private final @NotNull ItemStack displayItem;

    private final long price;

    public StomCosmetic(@NotNull String id, @NotNull String type, @NotNull String category,
                        @NotNull String permission, @NotNull Component displayName,
                        @NotNull ItemStack displayItem, long price) {
        this.id = id;
        this.type = type;
        this.category = category;
        this.permission = permission;
        this.displayName = displayName;
        this.displayItem = displayItem;
        this.price = Math.max(price, 0);
    }

    @Override
    public @NotNull String id() {
        return id;
    }

    @Override
    public @NotNull String type() {
        return type;
    }

    @Override
    public @NotNull String category() {
        return category;
    }

    @Override
    public @NotNull String permission() {
        return permission;
    }

    @Override
    public @NotNull Component displayName() {
        return displayName;
    }

    @Override
    public @NotNull ItemStack displayItem() {
        return displayItem;
    }

    @Override
    public long price() {
        return price;
    }

}
