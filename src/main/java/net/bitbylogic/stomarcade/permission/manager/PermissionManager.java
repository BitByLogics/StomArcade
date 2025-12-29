package net.bitbylogic.stomarcade.permission.manager;

import net.bitbylogic.stomarcade.permission.container.SimplePermissionContainer;
import net.bitbylogic.stomarcade.util.PermissionUtil;
import net.minestom.server.entity.Player;
import net.minestom.server.event.Event;
import net.minestom.server.event.EventNode;
import net.minestom.server.event.player.AsyncPlayerConfigurationEvent;
import net.minestom.server.event.player.PlayerLoadedEvent;
import org.jetbrains.annotations.NotNull;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class PermissionManager {

    private static final Set<String> REGISTERED_PERMISSIONS = ConcurrentHashMap.newKeySet();

    private final EventNode<Event> node = EventNode.all("permission");

    public PermissionManager() {
        node.addListener(AsyncPlayerConfigurationEvent.class, this::containerApply)
                .addListener(PlayerLoadedEvent.class, this::loaded);
    }

    private void containerApply(AsyncPlayerConfigurationEvent event) {
        Player player = event.getPlayer();

        player.setTag(PermissionUtil.PERMISSION_CONTAINER_TAG, new SimplePermissionContainer());
    }

    private void loaded(PlayerLoadedEvent event) {
        Player player = event.getPlayer();

        if (player.getPermissionLevel() < 4) {
            return;
        }

        PermissionUtil.set(player, "stomarcade.permission", true);
    }

    public static boolean registerPermission(@NotNull String permission) {
        return REGISTERED_PERMISSIONS.add(permission);
    }

    public static boolean unregisterPermission(@NotNull String permission) {
        return REGISTERED_PERMISSIONS.remove(permission);
    }

    public static Set<String> registeredPermissions() {
        return Set.copyOf(REGISTERED_PERMISSIONS);
    }

    public EventNode<Event> node() {
        return node;
    }

}
