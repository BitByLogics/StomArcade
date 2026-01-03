package net.bitbylogic.stomarcade.loot;

import net.bitbylogic.stomarcade.StomArcadeServer;
import net.goldenstack.loot.LootTable;
import net.goldenstack.loot.Trove;
import net.kyori.adventure.key.Key;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.stream.Stream;

public class LootTableManager {

    private static final File LOOT_TABLE_FOLDER = new File("loot_tables");
    private static final Map<Key, LootTable> LOOT_TABLES = new HashMap<>();

    public LootTableManager() {
        if(LOOT_TABLE_FOLDER.exists()) {
            loadLootTables();
            return;
        }

        try {
            extractLootTables();
            loadLootTables();
        } catch (IOException | URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }

    private void extractLootTables() throws IOException, URISyntaxException {
        String resourceFolder = "/loot_tables";
        String folderPrefix = resourceFolder.substring(1);
        URL resource = getClass().getResource(resourceFolder);

        if (resource == null) {
            return;
        }

        if (resource.getProtocol().equals("jar")) {
            String path = resource.getPath();
            String jarPath = path.substring(5, path.indexOf("!"));

            try (JarFile jar = new JarFile(jarPath)) {
                Enumeration<JarEntry> entries = jar.entries();

                while (entries.hasMoreElements()) {
                    JarEntry entry = entries.nextElement();

                    if (!entry.getName().startsWith(folderPrefix)) {
                        continue;
                    }

                    if (entry.getName().equals(folderPrefix) || entry.getName().equals(folderPrefix + "/")) {
                        continue;
                    }

                    String relativePath = entry.getName().substring(folderPrefix.length());

                    if (relativePath.startsWith("/")) {
                        relativePath = relativePath.substring(1);
                    }

                    Path destination = LOOT_TABLE_FOLDER.toPath().resolve(relativePath);

                    if (entry.isDirectory()) {
                        Files.createDirectories(destination);
                        return;
                    }

                    Files.createDirectories(destination.getParent());

                    try (InputStream in = jar.getInputStream(entry)) {
                        Files.copy(in, destination, StandardCopyOption.REPLACE_EXISTING);
                    }
                }
            }

            return;
        }

        Path sourceFolder = Paths.get(resource.toURI());

        try (Stream<Path> walk = Files.walk(sourceFolder)) {
            walk.forEach(path -> {
                try {
                    Path relative = sourceFolder.relativize(path);
                    Path destination = LOOT_TABLE_FOLDER.toPath().resolve(relative);

                    if (Files.isDirectory(path)) {
                        Files.createDirectories(destination);
                        return;
                    }

                    Files.copy(path, destination, StandardCopyOption.REPLACE_EXISTING);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
        }
    }

    public void loadLootTables() {
        LOOT_TABLES.clear();
        LOOT_TABLES.putAll(Trove.readTables(LOOT_TABLE_FOLDER.toPath()));

        StomArcadeServer.LOGGER.info("[Loot Table Manager]: Loaded " + LOOT_TABLES.size() + " loot tables.");
    }

    public static File getLootTableFolder() {
        return LOOT_TABLE_FOLDER;
    }

    public static Map<Key, LootTable> getLootTables() {
        return Map.copyOf(LOOT_TABLES);
    }

}
