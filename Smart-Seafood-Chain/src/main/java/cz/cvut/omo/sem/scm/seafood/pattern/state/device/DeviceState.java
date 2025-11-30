package cz.cvut.omo.sem.scm.seafood.pattern.state.device;

import cz.cvut.omo.sem.scm.seafood.model.device.Device;

/**
 * State Interface for Devices.
 * Controls behavior during ticks (working, broken, repairing).
 */
public interface DeviceState {

    /**
     * Main action per simulation tick.
     */
    void onTick(Device context);

    /**
     * Triggered when wear level reaches critical point.
     */
    void onFailure(Device context);

    /**
     * Triggered when a technician arrives/finishes.
     */
    void onRepair(Device context);

    boolean isOperational();
}