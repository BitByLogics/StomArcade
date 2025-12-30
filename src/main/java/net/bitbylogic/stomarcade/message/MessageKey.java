package net.bitbylogic.stomarcade.message;

import net.bitbylogic.stomarcade.util.MessageUtil;
import net.bitbylogic.stomarcade.util.context.ContextKeys;
import net.bitbylogic.utils.context.Context;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import net.minestom.server.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public class MessageKey {

    private final @NotNull String path;
    private final @NotNull Map<Locale, List<String>> values;

    public MessageKey(@NotNull String path, @NotNull String defaultValue) {
        this.path = path;
        this.values = new HashMap<>();

        values.put(Locale.ENGLISH, new ArrayList<>(List.of(defaultValue)));
    }

    public MessageKey(@NotNull String path, @NotNull List<String> defaultValue) {
        this.path = path;
        this.values = new HashMap<>();

        values.put(Locale.ENGLISH, new ArrayList<>(defaultValue));
    }

    public List<String> values(@NotNull Locale locale) {
        return values.computeIfAbsent(locale, _ -> new ArrayList<>());
    }

    public Component get(@NotNull Context context) {
        Locale locale = context.getOrDefault(ContextKeys.LOCALE, Locale.ENGLISH);

        return get(locale);
    }

    public Component get(Audience audience) {
        Locale locale = audience instanceof Player player ? player.getLocale() : Locale.ENGLISH;

        return get(locale);
    }

    public Component get(@NotNull Player player, TagResolver.Single... modifiers) {
        String localeString = player.getLocale().toString();

        if (localeString.isEmpty()) {
            return get(Locale.ENGLISH);
        }

        String[] parts = localeString.split("_", 2);

        Locale locale = Locale.of(parts[0]);

        return get(locale, modifiers);
    }

    public Component get(@NotNull Locale locale, TagResolver.Single... modifiers) {
        return MessageUtil.miniDeserialize(values.getOrDefault(locale, values.get(Locale.ENGLISH)).getFirst(), modifiers);
    }

    public Component get(TagResolver.Single... modifiers) {
        return MessageUtil.miniDeserialize(values.get(Locale.ENGLISH).getFirst(), modifiers);
    }

    public void send(@NotNull Context context, TagResolver.Single... modifiers) {
        Player player = context.get(ContextKeys.PLAYER).orElse(null);

        if (player == null) {
            return;
        }

        Locale locale = context.getOrDefault(ContextKeys.LOCALE, Locale.ENGLISH);

        send(player, locale, modifiers);
    }

    public void send(@NotNull Audience audience, TagResolver.Single... modifiers) {
        send(audience, Locale.ENGLISH, modifiers);
    }

    public void send(@NotNull Audience audience, @NotNull Locale locale, TagResolver.Single... modifiers) {
        values.getOrDefault(locale, values.get(Locale.ENGLISH)).forEach(message -> audience.sendMessage(MessageUtil.miniDeserialize(message, modifiers)));
    }

    public @NotNull String path() {
        return path;
    }

    public @NotNull Map<Locale, List<String>> values() {
        return values;
    }

}
