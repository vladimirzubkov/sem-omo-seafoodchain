package cz.cvut.omo.sem.scm.seafood.simulation;

import lombok.Getter;
import java.time.LocalDateTime;

public class Time {

    @Getter
    private static LocalDateTime currentTime = LocalDateTime.of(2025, 1, 1, 8, 0);

    // Default step is 1 hour
    private static int minutesPerTick = 60;

    /**
     * Configures the time step. Called by Simulator on startup.
     */
    public static void setTickDuration(int minutes) {
        minutesPerTick = minutes;
    }

    public static void advanceTime() {
        // Dynamic step instead of hardcoded plusHours(1)
        currentTime = currentTime.plusMinutes(minutesPerTick);
    }

    public static int getCurrentHour() {
        return currentTime.getHour();
    }

    private Time() {}
}