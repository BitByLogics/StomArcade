package net.bitbylogic.stomarcade.util;

import net.bitbylogic.stomarcade.message.tag.BrandingTags;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import net.kyori.adventure.text.minimessage.tag.standard.StandardTags;
import org.jetbrains.annotations.NotNull;

public class MessageUtil {

    private static final MiniMessage MINI_MESSAGE = MiniMessage.builder()
            .tags(
                    TagResolver.builder()
                            .resolver(StandardTags.defaults())
                            .resolvers(BrandingTags.ALL)
                            .build()
            ).build();

    public static Component miniDeserialize(@NotNull String message, TagResolver.Single... placeholders) {
        return MINI_MESSAGE.deserialize(message, placeholders);
    }

}
