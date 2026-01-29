package net.bitbylogic.stomarcade.util;

import net.bitbylogic.stomarcade.util.message.DefaultFontInfo;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.Style;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.minestom.server.MinecraftServer;
import net.minestom.server.entity.Entity;
import net.minestom.server.entity.Player;
import net.minestom.server.network.packet.server.play.TeamsPacket;
import net.minestom.server.scoreboard.Team;
import net.minestom.server.scoreboard.TeamBuilder;
import org.jetbrains.annotations.NotNull;

import java.util.Random;
import java.util.UUID;

public class ServerUtil {

    public static final Random RANDOM = new Random();

    public static final UUID FALLBACK_UUID = UUID.fromString("00000000-0000-0000-0000-000000000000");

    private static final int MOTD_CENTER_PX = 125;

    private static final Team COLLISION_TEAM;

    static {
        COLLISION_TEAM = new TeamBuilder("no_collision", MinecraftServer.getTeamManager())
                .collisionRule(TeamsPacket.CollisionRule.NEVER)
                .build();
    }

    public static void preventCollision(@NotNull Entity entity) {
        String identifier = entity instanceof Player player ? player.getUsername() : entity.getUuid().toString();

        if (COLLISION_TEAM.getMembers().contains(identifier)) {
            return;
        }

        COLLISION_TEAM.addMember(identifier);

        if(!(entity instanceof Player player)) {
            return;
        }

        player.sendPacket(COLLISION_TEAM.createTeamsCreationPacket());
    }

    public static void allowCollision(@NotNull Entity entity) {
        String identifier = entity instanceof Player player ? player.getUsername() : entity.getUuid().toString();

        COLLISION_TEAM.removeMember(identifier);

        if(!(entity instanceof Player player)) {
            return;
        }

        player.sendPacket(COLLISION_TEAM.createTeamDestructionPacket());
    }

    public static Component center(@NotNull Component component) {
        String plain = MiniMessage.miniMessage().serialize(component.style(Style.style().build()));

        int messagePxSize = 0;

        for (char c : plain.toCharArray()) {
            messagePxSize += DefaultFontInfo.getDefaultFontInfo(c).getLength() + 1;
        }

        int halvedMessageSize = messagePxSize / 2;
        int toCompensate = MOTD_CENTER_PX - halvedMessageSize;

        int spacePx = DefaultFontInfo.getDefaultFontInfo(' ').getLength();
        int compensatedSpaces = toCompensate / (spacePx + 1);

        return Component.text(" ".repeat(Math.max(0, compensatedSpaces))).append(component);
    }

}
