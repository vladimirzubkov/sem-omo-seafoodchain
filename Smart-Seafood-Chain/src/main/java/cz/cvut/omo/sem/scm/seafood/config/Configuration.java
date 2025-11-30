package cz.cvut.omo.sem.scm.seafood.config;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

/**
 * Root configuration object loaded from YAML files.
 * Contains simulation settings and (later) all entities: parties, devices, production lines, etc.
 *
 * @JsonIgnoreProperties(ignoreUnknown = true) is crucial right now:
 *   - Allows loading any YAML even if it contains fields not yet mapped in this class
 *   - Prevents deserialization errors when using demo/stress configs with many extra keys
 *   - Will be removed later when full config structure is implemented
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class Configuration {
    private SimulationConfig simulation;
}