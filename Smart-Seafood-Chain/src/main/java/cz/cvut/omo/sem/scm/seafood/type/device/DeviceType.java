package cz.cvut.omo.sem.scm.seafood.type.device;

import cz.cvut.omo.sem.scm.seafood.util.YamlEnumHelper;
import java.util.Optional;

public enum DeviceType {
    INDUSTRIAL_FREEZER,
    FOOD_PROCESSING_ROBOT,
    PACKAGING_MACHINE,
    REFRIGERATOR,
//    MIXER,
    CONVEYOR_BELT;

    /**
     * Resolves the device type using the shared utility logic.
     */
    public static Optional<DeviceType> fromYamlString(String typeStr) {
        return YamlEnumHelper.resolve(typeStr, DeviceType.class);
    }
}