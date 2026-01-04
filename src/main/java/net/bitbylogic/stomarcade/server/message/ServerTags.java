package net.bitbylogic.stomarcade.server.message;

import net.bitbylogic.stomarcade.StomArcadeServer;
import net.bitbylogic.stomarcade.server.ServerEnvironment;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.tag.Tag;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;

public class ServerTags {

    public static final TagResolver KARDIA_ID = TagResolver.resolver(
            "kardia_id",
            (_, _) -> Tag.inserting(Component.text(StomArcadeServer.serverManager().environment().getEnv(ServerEnvironment.EnvVariable.KARDIA_ID)))
    );

    public static final TagResolver ALL = TagResolver.resolver(KARDIA_ID);

    private ServerTags() {}

}
