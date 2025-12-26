package net.bitbylogic.stomarcade.permission.container;

import org.jetbrains.annotations.NotNull;

import java.util.Set;

public interface PermissionContainer {

    boolean has(@NotNull String permission);

    void set(@NotNull String permission, boolean value);

    void unset(@NotNull String permission);

    Set<String> all();

}
