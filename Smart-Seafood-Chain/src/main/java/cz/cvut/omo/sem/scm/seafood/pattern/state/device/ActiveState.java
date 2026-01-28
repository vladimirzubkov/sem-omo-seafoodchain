package cz.cvut.omo.sem.scm.seafood.pattern.state.device;

import cz.cvut.omo.sem.scm.seafood.model.device.Device;
import cz.cvut.omo.sem.scm.seafood.type.operation.EventType;

public class ActiveState implements DeviceState {

    @Override
    public void onTick(Device context) {
        // 1. Consume Energy - Unleash the bills!
        context.consumeEnergy();

        // 2. Update Sensors (Physics & IoT Check)
        context.updateSensors();

        // 2. Increase Wear
        double wear = context.getWearLevel() + 0.05;
        context.setWearLevel(wear);

        // 3. Check for automatic failure
        if (wear >= 1.0) {
            onFailure(context);
        }
    }

    @Override
    public void onFailure(Device context) {
        System.out.println("DEVICE FAILED: %s (Wear limit)".formatted(context.getName()));
        context.setState(new BrokenState());
        context.fireEvent(EventType.DEVICE_BREAKDOWN, "Critical wear level reached.");
    }

    @Override
    public void onRepair(Device context) {
        System.out.println("Device is running fine, no repair needed.");
    }

    @Override
    public boolean isOperational() {
        return true;
    }
}