package cz.cvut.omo.sem.scm.seafood.pattern.state.device;

import cz.cvut.omo.sem.scm.seafood.model.device.Device;
import cz.cvut.omo.sem.scm.seafood.type.operation.EventType;

/**
 * Normal operational state.
 * Device consumes energy, wears out, and does work.
 */
public class ActiveState implements DeviceState {

    @Override
    public void onTick(Device context) {
        // 1. Consume Energy
        // context.consumeEnergy(...);

        // 2. Increase Wear
        double wear = context.getWearLevel() + 0.05; // Stub increment
        context.setWearLevel(wear);

        // 3. Check for automatic failure
        if (wear >= 1.0) {
            onFailure(context);
        }
    }

    @Override
    public void onFailure(Device context) {
        System.out.println("DEVICE FAILED: " + context.getName());
        // Switch to Broken State
        context.setState(new BrokenState());

        // Context should fire event
        // context.fireEvent(EventType.DEVICE_BREAKDOWN);
    }

    @Override
    public void onRepair(Device context) {
        // Cannot repair if not broken (or maybe maintenance?)
        System.out.println("Device is running fine, no repair needed.");
    }

    @Override
    public boolean isOperational() {
        return true;
    }
}