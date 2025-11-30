package cz.cvut.omo.sem.scm.seafood.config;

import lombok.Data;

/**
 * Holds only simulation-wide parameters.
 * Separated from Configuration for Single Responsibility Principle:
 *   - Easy to test/mock simulation settings independently
 *   - Clear boundary when extending config with parties/devices etc.
 */
@Data
public class SimulationConfig {
    private int maxTicks = 10;

    // NEW: How much virtual time passes in one tick? Default = 60 min (1 hour)
    private int tickDurationMinutes = 60;

    // NEW: Real-time delay between ticks in milliseconds (for better UX)
    // 0 = run as fast as possible
    private int tickDelayMs = 0;
}