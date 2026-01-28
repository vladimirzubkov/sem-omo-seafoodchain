package cz.cvut.omo.sem.scm.seafood.simulation;

import lombok.Getter;
import java.time.LocalDateTime;

/**
 * Global utility class for time management.
 * Contains static public methods and maintains a global state (Mono-state).
 */
public final class Time {

    // Note: this state (memory) distinguishes this class from a pure utility class, which is stateless.
    @Getter
    private static LocalDateTime currentTime = LocalDateTime.of(2025, 1, 1, 8, 0);

    // The default simulation step in minutes is 1 hour
    private static int minutesPerTick = 60;

    /**
     * Configures the time step. Called by SimulationController on startup.
     */
    public static void setTickDuration(int minutes) {
        minutesPerTick = minutes;
    }

    // Advance global simulation time by configured step;
    // we use somewhat real days, hours and minutes when advancing time
    public static void advanceTime() {
        // Dynamic step instead of hardcoded plusHours(1)
        currentTime = currentTime.plusMinutes(minutesPerTick);
    }

    // Returns a current hour of the simulation day when asked
    public static int getCurrentHour() {
        return currentTime.getHour();
    }

    // Private constructor prohibits to make an instance of a class (utility class)
    private Time() {}
}