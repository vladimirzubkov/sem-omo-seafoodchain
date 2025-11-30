package cz.cvut.omo.sem.scm.seafood.model.item;

import java.time.LocalDateTime;

/**
 * Immutable record of a temperature measurement.
 */
public record TemperatureRecord(LocalDateTime timestamp, double temperature) {
}