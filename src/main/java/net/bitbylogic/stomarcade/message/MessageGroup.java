package net.bitbylogic.stomarcade.message;

import net.bitbylogic.stomarcade.StomArcadeServer;

import java.util.List;

public abstract class MessageGroup implements MessageRegistry {

    private final String pathPrefix;

    public MessageGroup(String pathPrefix) {
        this.pathPrefix = pathPrefix.endsWith(".") ? pathPrefix : pathPrefix + ".";
    }

    protected MessageKey register(String key, String defaultValue) {
        return StomArcadeServer.messages().register(pathPrefix + key, defaultValue);
    }

    protected MessageKey register(String key, List<String> defaultValues) {
        return StomArcadeServer.messages().register(pathPrefix + key, defaultValues);
    }

}
