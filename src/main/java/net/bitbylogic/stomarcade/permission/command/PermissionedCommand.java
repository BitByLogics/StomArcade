package net.bitbylogic.stomarcade.permission.command;

import net.bitbylogic.stomarcade.permission.manager.PermissionManager;
import net.bitbylogic.stomarcade.util.PermissionUtil;
import net.minestom.server.command.CommandSender;
import net.minestom.server.command.ConsoleSender;
import net.minestom.server.command.builder.Command;
import org.jetbrains.annotations.NotNull;

public class PermissionedCommand extends Command {

    private String permission;

    public PermissionedCommand(@NotNull String name, String... aliases) {
        super(name, aliases);

        setCondition((sender, _) -> permissionCheck(sender));
    }

    private boolean permissionCheck(CommandSender sender) {
        if (sender instanceof ConsoleSender || permission == null) {
            return true;
        }

        return PermissionUtil.has(sender, permission);
    }

    public void setPermission(@NotNull String permission) {
        if (this.permission != null) {
            PermissionManager.unregisterPermission(this.permission);
        }

        this.permission = permission;
        PermissionManager.registerPermission(permission);
    }

}
