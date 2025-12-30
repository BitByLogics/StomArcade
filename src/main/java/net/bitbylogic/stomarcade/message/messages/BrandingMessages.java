package net.bitbylogic.stomarcade.message.messages;

import net.bitbylogic.stomarcade.message.MessageGroup;
import net.bitbylogic.stomarcade.message.MessageKey;

import java.util.List;

public class BrandingMessages extends MessageGroup {

    public static MessageKey PRIMARY_COLOR;
    public static MessageKey SECONDARY_COLOR;
    public static MessageKey HIGHLIGHT_COLOR;
    public static MessageKey SEPARATOR_COLOR;

    public static MessageKey ERROR_PRIMARY_COLOR;
    public static MessageKey ERROR_SECONDARY_COLOR;
    public static MessageKey ERROR_HIGHLIGHT_COLOR;

    public static MessageKey SUCCESS_PRIMARY_COLOR;
    public static MessageKey SUCCESS_SECONDARY_COLOR;
    public static MessageKey SUCCESS_HIGHLIGHT_COLOR;

    public static MessageKey SERVER_BRANDING;
    public static MessageKey VERSION_INFO;

    public BrandingMessages() {
        super("Branding");
    }

    @Override
    public void register() {
        PRIMARY_COLOR = register("Colors.Primary", "<#90caf9>");
        SECONDARY_COLOR = register("Colors.Secondary", "<#bbdefb>");
        HIGHLIGHT_COLOR = register("Colors.Highlight", "<#64b5f6>");
        SEPARATOR_COLOR = register("Colors.Separator", "<#555555>");

        ERROR_PRIMARY_COLOR = register("Colors.Error.Primary", "<#ff758f>");
        ERROR_SECONDARY_COLOR = register("Colors.Error.Secondary", "<#ffb3c1>");
        ERROR_HIGHLIGHT_COLOR = register("Colors.Error.Highlight", "<#ff4d6d>");

        SUCCESS_PRIMARY_COLOR = register("Colors.Success.Primary", "<#3dd67b>");
        SUCCESS_SECONDARY_COLOR = register("Colors.Success.Secondary", "<#7be4a5>");
        SUCCESS_HIGHLIGHT_COLOR = register("Colors.Success.Highlight", "<#00c851>");

        SERVER_BRANDING = register("Server-Branding", "Stom Arcade (Minestom)");
        VERSION_INFO = register("Version-Info", List.of(
                "<primary>Stom Arcade <separator>• <highlight>Minestom",
                "",
                " <secondary>Stom Arcade is running the latest version of " +
                        "<highlight><click:open_url:https://github.com/Minestom/Minestom>Minestom</click><secondary>, a powerful",
                " <secondary>library for creating your own Minecraft servers from scratch.",
                ""
        ));
    }

}
