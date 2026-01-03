package net.bitbylogic.stomarcade.util;

import net.bitbylogic.stomarcade.util.message.DefaultFontInfo;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.Style;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.jetbrains.annotations.NotNull;

import java.util.Random;
import java.util.UUID;

public class ServerUtil {

    public static final Random RANDOM = new Random();

    public static final UUID FALLBACK_UUID = UUID.fromString("00000000-0000-0000-0000-000000000000");

    private static final int MOTD_CENTER_PX = 125;

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
