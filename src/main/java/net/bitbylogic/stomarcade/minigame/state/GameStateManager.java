package net.bitbylogic.stomarcade.minigame.state;

import net.bitbylogic.stomarcade.StomArcadeServer;
import net.bitbylogic.stomarcade.minigame.Minigame;
import net.bitbylogic.stomarcade.minigame.state.event.GameStateChangeEvent;
import net.minestom.server.MinecraftServer;
import net.minestom.server.timer.Task;
import net.minestom.server.timer.TaskSchedule;

import java.time.Duration;
import java.util.List;

public class GameStateManager {

    private final Minigame minigame;
    private final List<GameState> states;

    private long startTime;
    private Task stateTask;

    private GameState currentState;

    private long stateElapsedMillis;
    private long lastTickTime;

    public GameStateManager(Minigame minigame, List<GameState> states) {
        this.minigame = minigame;
        this.states = states;

        if (states.isEmpty()) {
            throw new IllegalArgumentException("States cannot be empty!");
        }
    }

    public void start() {
        if (stateTask != null) {
            return;
        }

        this.startTime = System.currentTimeMillis();
        this.currentState = states.getFirst();
        this.stateElapsedMillis = 0;
        this.lastTickTime = System.currentTimeMillis();

        currentState.onStart(minigame);

        this.stateTask = MinecraftServer.getSchedulerManager().scheduleTask(
                this::onTick,
                TaskSchedule.immediate(),
                TaskSchedule.tick(1)
        );
    }

    public void stop() {
        if (stateTask == null) {
            return;
        }

        stateTask.cancel();
        stateTask = null;

        StomArcadeServer.minigames().endMinigame();
    }

    private void onTick() {
        long now = System.currentTimeMillis();

        if (currentState.isPaused()) {
            lastTickTime = now;
            return;
        }

        stateElapsedMillis += (now - lastTickTime);
        lastTickTime = now;

        if (stateElapsedMillis >= currentState.stateDuration().toMillis()) {
            advanceOrEnd();
            return;
        }

        currentState.onTick(minigame);
    }

    private void advanceOrEnd() {
        int index = states.indexOf(currentState);

        if (currentState.stateDuration().isNegative()) {
            return;
        }

        currentState.onEnd(minigame);

        if (index + 1 >= states.size()) {
            stop();
            return;
        }

        GameState previousState = currentState;

        currentState = states.get(index + 1);
        stateElapsedMillis = 0;
        lastTickTime = System.currentTimeMillis();

        currentState.onStart(minigame);

        MinecraftServer.getGlobalEventHandler().call(new GameStateChangeEvent(minigame, previousState, currentState));
    }

    public void extend(Duration duration) {
        if (duration.isNegative() || duration.isZero()) {
            return;
        }

        stateElapsedMillis = Math.max(0, stateElapsedMillis - duration.toMillis());
    }

    public void resetElapsedTime() {
        stateElapsedMillis = 0;
    }

    public void skip() {
        advanceOrEnd();
    }

    public long startTime() {
        return startTime;
    }

    public GameState currentState() {
        return currentState;
    }

    public long stateElapsedMillis() {
        return stateElapsedMillis;
    }

    public Duration remainingTime() {
        long remaining = currentState.stateDuration().toMillis() - stateElapsedMillis;
        return Duration.ofMillis(Math.max(0, remaining));
    }

}