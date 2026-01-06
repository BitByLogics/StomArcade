package net.bitbylogic.stomarcade.block;

import net.kyori.adventure.key.Key;
import net.minestom.server.instance.block.BlockHandler;
import net.minestom.server.tag.Tag;
import org.jspecify.annotations.NonNull;

import java.util.Set;

public final class SkullBlockHandler implements BlockHandler {

    private static final Key key = Key.key("minecraft:skull");

    private static final Tag<?> PROFILE = Tag.NBT("profile");

    @Override
    public @NonNull Key getKey() {
        return key;
    }

    @Override
    public @NonNull Set<Tag<?>> getBlockEntityTags() {
        return Set.of(PROFILE);
    }

}

