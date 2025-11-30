package cz.cvut.omo.sem.scm.seafood.type.device;

public enum SensorType {
    TEMPERATURE, // For Freezers, Storage, Transport
    HUMIDITY,    // For Freezers, Storage, Transport
    VIBRATION,   // For Conveyors/Motors/Robots (detects breakdown)
    GPS,         // For Distance tracking and Time control at Transport
    CAMERA,       // Security, quality control - Transport, at Work afterhours
    ELECTRONIC_LOCK // Security, authorization - Transport, at Work afterhours
}