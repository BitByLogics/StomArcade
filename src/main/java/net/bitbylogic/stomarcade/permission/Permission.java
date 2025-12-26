package net.bitbylogic.stomarcade.permission;

import net.minestom.server.entity.Entity;
import org.jetbrains.annotations.NotNull;

public interface Permission {

    boolean has(@NotNull Entity entity);

}
