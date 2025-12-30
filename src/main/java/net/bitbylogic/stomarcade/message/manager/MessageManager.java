package net.bitbylogic.stomarcade.message.manager;

import net.bitbylogic.stomarcade.StomArcadeServer;
import net.bitbylogic.stomarcade.message.MessageGroup;
import net.bitbylogic.stomarcade.message.MessageKey;
import org.jetbrains.annotations.NotNull;
import org.simpleyaml.configuration.file.FileConfiguration;
import org.simpleyaml.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.*;

public final class MessageManager {

    private static final File MESSAGES_FOLDER = new File("messages");

    private static final Map<String, MessageKey> REGISTRY = new LinkedHashMap<>();
    private static final List<MessageGroup> GROUP_REGISTRY = new ArrayList<>();

    private static final Locale[] SUPPORTED_LOCALES = { Locale.ENGLISH };

    public MessageManager() {
        try {
            if(!MESSAGES_FOLDER.exists()) {
                MESSAGES_FOLDER.mkdirs();
            }

            reload();
        } catch (IOException e) {
            StomArcadeServer.LOGGER.error("Failed to reload messages", e);
        }
    }

    public MessageKey register(String path, String defaultValue) {
        MessageKey key = new MessageKey(path, defaultValue);
        REGISTRY.put(path, key);

        try {
            loadKey(key);
        } catch (IOException e) {
            StomArcadeServer.LOGGER.error("Failed to load message key", e);
        }

        return key;
    }

    public MessageKey register(String path, List<String> defaultValue) {
        MessageKey key = new MessageKey(path, defaultValue);
        REGISTRY.put(path, key);

        try {
            loadKey(key);
        } catch (IOException e) {
            StomArcadeServer.LOGGER.error("Failed to load message key", e);
        }

        return key;
    }

    public void registerGroup(@NotNull MessageGroup... groups) {
        for (MessageGroup group : groups) {
            GROUP_REGISTRY.add(group);

            group.register();
        }
    }

    public Collection<MessageKey> all() {
        return REGISTRY.values();
    }

    public MessageKey getByPath(String path) {
        return REGISTRY.get(path);
    }

    public void reload() throws IOException {
        for (MessageKey key : REGISTRY.values()) {
            loadKey(key);
        }
    }

    private void loadKey(@NotNull MessageKey key) throws IOException {
        for (Locale locale : SUPPORTED_LOCALES) {
            File localeConfig = new File(MESSAGES_FOLDER, locale.toLanguageTag() + ".yml");

            if (!localeConfig.exists()) {
                localeConfig.createNewFile();
            }

            FileConfiguration config = YamlConfiguration.loadConfiguration(localeConfig);

            if(!config.isSet(key.path())) {
                List<String> valuesToSave = key.values(locale);

                if(valuesToSave.isEmpty()) {
                    continue;
                }

                if (valuesToSave.size() == 1) {
                    config.set(key.path(), valuesToSave.getFirst());

                    try {
                        config.save(localeConfig);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    continue;
                }

                config.set(key.path(), valuesToSave);

                try {
                    config.save(localeConfig);
                } catch (IOException e) {
                    e.printStackTrace();
                }

                continue;
            }

            key.values().remove(locale);

            if (config.isList(key.path())) {
                List<String> value = new ArrayList<>(config.getStringList(key.path()));

                key.values(locale).addAll(value);
                continue;
            }

            String value = config.getString(key.path());

            if (value == null) {
                continue;
            }

            key.values(locale).add(value);
        }
    }

}
