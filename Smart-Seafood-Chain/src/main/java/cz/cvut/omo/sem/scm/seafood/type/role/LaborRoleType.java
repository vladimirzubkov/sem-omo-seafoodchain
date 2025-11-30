package cz.cvut.omo.sem.scm.seafood.type.role;

/**
 * Defines the job functions of an individual employee.
 * Used to assign tasks and calculate salaries.
 */
public enum LaborRoleType {
    MANUAL_LABOR, // Worker, Cook (Physical work)
    MAINTENANCE,  // Technician (Repairs)
    MANAGEMENT,   // SCM Manager (Strategy)
    INSPECTION,   // Inspector (Audit)
    DRIVER,       // Logistics Driver (Transport operation)
    COURIER       // In-house Courier, Last-Mile delivery
}