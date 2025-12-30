package net.bitbylogic.stomarcade.feature.impl.serverlist;

import net.bitbylogic.stomarcade.StomArcadeServer;
import net.bitbylogic.stomarcade.feature.EventFeature;
import net.bitbylogic.stomarcade.message.event.MessagesReloadedEvent;
import net.bitbylogic.stomarcade.util.ServerUtil;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.minestom.server.MinecraftServer;
import net.minestom.server.event.server.ServerListPingEvent;
import net.minestom.server.ping.Status;

public class ServerListFeature extends EventFeature {

    private Status status;

    public ServerListFeature() {
        super("server_list");

        node().addListener(ServerListPingEvent.class, event -> {
            if (status == null) {
                return;
            }

            event.setStatus(status);
        }).addListener(MessagesReloadedEvent.class, _ -> buildStatus());
    }

    @Override
    public void onEnable() {
        StomArcadeServer.messages().registerGroup(new ServerListMessages());

        buildStatus();
    }

    private void buildStatus() {
        TextComponent motdComponent = Component.empty();

        for (Component component : ServerListMessages.MOTD.getAll()) {
            motdComponent = motdComponent.append(ServerUtil.center(component)).append(Component.newline());
        }

        status = Status.builder()
                .description(motdComponent)
                .versionInfo(new Status.VersionInfo(ServerListMessages.VERSION_NAME.getPlain(), MinecraftServer.PROTOCOL_VERSION))
                .build();
    }

}
