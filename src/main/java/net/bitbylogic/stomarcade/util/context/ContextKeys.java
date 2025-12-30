package net.bitbylogic.stomarcade.util.context;

import net.bitbylogic.utils.context.ContextKey;
import net.bitbylogic.utils.reflection.TypeToken;
import net.minestom.server.entity.Player;

import java.util.Locale;

public class ContextKeys {

    // Minestom
    public static final ContextKey<Player> PLAYER = ContextKey.key("player", Player.class, Player.class);

    // Messages
    public static final ContextKey<Locale> LOCALE = ContextKey.key("locale", Locale.class, (new TypeToken<Locale>() {}).getType());

}
