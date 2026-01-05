package net.bitbylogic.stomarcade.feature;

import org.jetbrains.annotations.NotNull;

public interface Feature {

    @NotNull String id();

    void onEnable();

    void onDisable();

    void reloadConfig();

}
