package cz.cvut.omo.sem.scm.seafood.type.operation;

import lombok.Getter;

/**
 * Represents the physical state of an electronic lock.
 * Mapped to double values for IoT sensor readings.
 */
@Getter
public enum LockState {
    LOCKED(1.0),    // Secure, closed
    UNLOCKED(0.0),  // Authorized opening (loading/unloading)
    BREACHED(-1.0); // Unauthorized opening (alarm condition)

    private final double signalValue;

    LockState(double signalValue) {
        this.signalValue = signalValue;
    }
}