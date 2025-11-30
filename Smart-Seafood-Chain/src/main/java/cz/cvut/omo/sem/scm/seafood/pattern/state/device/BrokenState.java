package cz.cvut.omo.sem.scm.seafood.pattern.state.device;

import cz.cvut.omo.sem.scm.seafood.model.device.Device;

/**
 * State when device has failed.
 * No work is done, no energy consumed (or minimal).
 * Waiting for repair.
 */
public class BrokenState implements DeviceState {

    @Override
    public void onTick(Device context) {
        // Do nothing. The line is stopped.
        // Maybe emit distinct "beep" signal (Log)
    }

    @Override
    public void onFailure(Device context) {
        // Already broken
    }

    @Override
    public void onRepair(Device context) {
        System.out.println("Technician started repairing " + context.getName());
        context.setState(new RepairingState());
    }

    @Override
    public boolean isOperational() {
        return false;
    }
}