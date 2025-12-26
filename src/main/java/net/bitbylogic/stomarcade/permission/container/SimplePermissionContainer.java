package net.bitbylogic.stomarcade.permission.container;

import org.jetbrains.annotations.NotNull;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class SimplePermissionContainer implements PermissionContainer {

    private final ConcurrentHashMap<String, Boolean> permissions = new ConcurrentHashMap<>();

    @Override
    public boolean has(@NotNull String permission) {
        if (!permissions.containsKey(permission.toLowerCase())) {
            return false;
        }

        return permissions.get(permission.toLowerCase());
    }

    @Override
    public void set(@NotNull String permission, boolean value) {
        permissions.put(permission.toLowerCase(), value);
    }

    @Override
    public void unset(@NotNull String permission) {
        permissions.remove(permission.toLowerCase());
    }

    @Override
    public Set<String> all() {
        return Set.copyOf(permissions.keySet());
    }

}
