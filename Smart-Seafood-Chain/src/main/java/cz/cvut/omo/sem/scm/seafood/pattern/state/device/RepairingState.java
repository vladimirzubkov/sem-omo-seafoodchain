package cz.cvut.omo.sem.scm.seafood.pattern.state.device;

import cz.cvut.omo.sem.scm.seafood.model.device.Device;

/**
 * State when technician is actively working on the device.
 */
public class RepairingState implements DeviceState {

    private int repairTicksRemaining = 3; // Stub duration

    @Override
    public void onTick(Device context) {
        repairTicksRemaining--;
        if (repairTicksRemaining <= 0) {
            System.out.println("Repair finished for " + context.getName());
            context.setWearLevel(0.0); // Reset wear
            context.setState(new ActiveState()); // Back to work
        }
    }

    @Override
    public void onFailure(Device context) {
        // Cannot fail while being repaired
    }

    @Override
    public void onRepair(Device context) {
        // Already repairing
    }

    @Override
    public boolean isOperational() {
        return false;
    }
}