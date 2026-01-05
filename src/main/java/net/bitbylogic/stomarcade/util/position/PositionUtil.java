package net.bitbylogic.stomarcade.util.position;

import net.minestom.server.coordinate.Pos;
import org.jetbrains.annotations.NotNull;

public class PositionUtil {

    public static String serialize(@NotNull Pos pos) {
        return String.format("%s:%s:%s:%s:%s", pos.x(), pos.y(), pos.z(), pos.yaw(), pos.pitch());
    }

    public static Pos deserialize(@NotNull String serialized) {
        String[] split = serialized.split(":");

        if (split.length != 5) {
            return Pos.ZERO;
        }

        return new Pos(Double.parseDouble(split[0]), Double.parseDouble(split[1]), Double.parseDouble(split[2]), Float.parseFloat(split[3]), Float.parseFloat(split[4]));
    }

}
