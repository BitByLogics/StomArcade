package net.bitbylogic.stomarcade.minigame.manager;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import net.bitbylogic.rps.listener.ListenerComponent;
import net.bitbylogic.stomarcade.StomArcadeServer;
import net.bitbylogic.stomarcade.minigame.Minigame;
import net.bitbylogic.stomarcade.server.ServerEnvironment;
import net.minestom.server.MinecraftServer;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MinigameManager {

    private static final File MINIGAMES_FOLDER = new File("minigames");

    private final Map<String, Minigame> minigames = new HashMap<>();

    private Minigame activeMinigame;

    public MinigameManager() {
        loadMinigames();

        if (minigames.isEmpty()) {
            return;
        }

        startMinigame(minigames.values().iterator().next());
    }

    private void loadMinigames() {
        if (!MINIGAMES_FOLDER.exists() && !MINIGAMES_FOLDER.mkdirs()) {
            StomArcadeServer.LOGGER.error("Failed to create minigames folder");
            return;
        }

        File[] minigameJars = MINIGAMES_FOLDER.listFiles(file ->
                file.isFile() && file.getName().endsWith(".jar")
        );

        if (minigameJars == null || minigameJars.length == 0) {
            StomArcadeServer.LOGGER.info("No minigames found in minigames folder");
            return;
        }

        for (File jar : minigameJars) {
            try {
                loadMinigame(jar);
            } catch (Exception e) {
                StomArcadeServer.LOGGER.error(
                        "Failed to load minigame from {}", jar.getName(), e
                );
            }
        }
    }

    private void loadMinigame(File jarFile) throws Exception {
        Path jarPath = jarFile.toPath();

        JsonObject descriptor;

        try (FileSystem fs = FileSystems.newFileSystem(jarPath)) {
            Path jsonPath = fs.getPath("/stom-game.json");

            if (!Files.exists(jsonPath)) {
                throw new IllegalStateException("Missing stom-game.json");
            }

            descriptor = JsonParser.parseString(
                    Files.readString(jsonPath)
            ).getAsJsonObject();
        }

        String mainClass = descriptor.get("main").getAsString();

        URLClassLoader classLoader = new URLClassLoader(
                new URL[]{jarFile.toURI().toURL()},
                getClass().getClassLoader()
        );

        Class<?> clazz = Class.forName(mainClass, true, classLoader);

        if (!Minigame.class.isAssignableFrom(clazz)) {
            throw new IllegalStateException(
                    "Main class does not implement Minigame: " + mainClass
            );
        }

        Minigame minigame = (Minigame) clazz
                .getDeclaredConstructor()
                .newInstance();

        registerMinigame(minigame);

        StomArcadeServer.LOGGER.info(
                "Loaded minigame '{}' from {}",
                minigame.id(),
                jarFile.getName()
        );
    }

    public void registerMinigame(@NotNull Minigame minigame) {
        if (minigames.containsKey(minigame.id())) {
            throw new IllegalStateException("Duplicate minigame id: " + minigame.id());
        }

        minigame.onLoad();

        minigames.put(minigame.id(), minigame);
    }

    public void startMinigame(@NotNull Minigame minigame) {
        if (activeMinigame != null) {
            activeMinigame.onEnd();
            activeMinigame.stateManager().stop();
        }

        minigame.onStart();
        minigame.stateManager().start();

        MinecraftServer.getGlobalEventHandler().addChild(minigame.node());

        this.activeMinigame = minigame;

        StomArcadeServer.LOGGER.info("Started Minigame {}", minigame.id());
    }

    public void endMinigame() {
        if (activeMinigame == null) {
            return;
        }

        MinecraftServer.getGlobalEventHandler().removeChild(activeMinigame.node());

        activeMinigame.onEnd();
        activeMinigame.stateManager().stop();

        if (new ArrayList<>(minigames.values()).indexOf(activeMinigame) == minigames.size() - 1) {
            String kardiaId = StomArcadeServer.serverManager().environment().getEnv(ServerEnvironment.EnvVariable.KARDIA_ID);

            if (!kardiaId.equalsIgnoreCase("none")) {
                StomArcadeServer.redisClient().sendListenerMessage(new ListenerComponent("server_shutdown").addData("serverId", kardiaId));
            }
        }

        this.activeMinigame = null;
    }

    public Minigame activeMinigame() {
        return activeMinigame;
    }

    public Minigame getMinigame(@NotNull String id) {
        return minigames.get(id);
    }

    public Map<String, Minigame> minigames() {
        return Map.copyOf(minigames);
    }

}