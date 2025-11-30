package cz.cvut.omo.sem.scm.seafood.type.device;

/**
 * Defines specific operations a FoodProcessingRobot can perform.
 * Used to configure versatile robots via YAML.
 */
public enum RobotCapability {
    CHOPPING,   // Slicing
    COOKING,    // Heat treatment
    FILLETING,  // Filleting fish
    MIXING,     // Mixing ingredients
    PEELING     // Peeling, obviously
}