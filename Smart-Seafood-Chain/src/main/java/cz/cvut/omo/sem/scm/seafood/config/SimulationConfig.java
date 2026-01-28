package cz.cvut.omo.sem.scm.seafood.config;

import lombok.Data;

/**
 * Holds only simulation-wide parameters.
 * Separated from Configuration for Single Responsibility Principle:
 *   - Easy to test/mock simulation settings independently
 *   - Clear boundary when extending config with parties/devices etc.
 *   - Holds default values, which can be overridden from configuration YAML file
 */
@Data
public class SimulationConfig {

    // Default simulation length
    private int maxTicks = 10;

    // How much virtual time passes in one tick? Default = 60 min (1 hour);
    // otherwise, value from a YAML configuration file is used if present;
    // value is passed through SimulationController class to global Time class.
    private int tickDurationMinutes = 60;

    // Real-time delay between ticks in milliseconds (for better UX)
    // 1000 - one second, 0 = run as fast as possible
    private int tickDelayMs = 350;
}