package net.bitbylogic.stomarcade.feature.impl;

import net.bitbylogic.stomarcade.feature.EventFeature;
import net.minestom.server.MinecraftServer;
import net.minestom.server.event.Event;
import net.minestom.server.event.EventNode;
import net.minestom.server.event.player.PlayerBlockBreakEvent;

public class BlockBreakFeature extends EventFeature {

    public static final EventNode<Event> BREAK_DENY_NODE = EventNode.all("block_break_deny").addListener(PlayerBlockBreakEvent.class, event -> event.setCancelled(true));

    public BlockBreakFeature() {
        super("block_break");
    }

    @Override
    public void onEnable() {
        super.onEnable();

        MinecraftServer.getGlobalEventHandler().removeChild(BREAK_DENY_NODE);
    }

    @Override
    public void onDisable() {
        super.onDisable();

        MinecraftServer.getGlobalEventHandler().addChild(BREAK_DENY_NODE);
    }

}
