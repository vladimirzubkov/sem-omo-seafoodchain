package cz.cvut.omo.sem.scm.seafood.type.operation;

/**
 * Defines all possible events that can occur within the simulation.
 * Used for logic triggers (Observer) and reporting.
 */
public enum EventType {
    // Supply Chain Operations - positive
    ITEM_CAUGHT,
    ITEM_TRANSFERRED,
    ITEM_PROCESSED,
    ITEM_COOKED,
    ITEM_PACKAGED,
    ITEM_SOLD,

    // Supply Chain Operations - negative
    PRODUCTION_FAILED,

    // Inventory & Logistics
    RESOURCE_DEPLETED,     // Critical: Stops ManualLaborRole
    RESOURCE_REFILLED,     // Critical: Resumes ManualLaborRole
    ORDER_PLACED,          // Critical: Triggers LogisticsRole
    DELIVERY_ARRIVED,      // Useful for logistics completion

    // IoT & Maintenance
    DEVICE_BREAKDOWN,      // Triggered by wear > 100%
    DEVICE_REPAIR_STARTED, // Technician started work
    DEVICE_REPAIRED,       // Repair finished successfully

    // Personnel & Shifts
    SHIFT_STARTED,
    SHIFT_ENDED,
    MAINTENANCE_PERFORMED, // Work log entry for technician
    INSPECTION_COMPLETED,  // Work log entry for inspector

    // Management & Security
    DEMAND_CREATED,
    INSPECTION_PERFORMED,
    SCM_OPTIMIZATION,
    SECURITY_BREACH,               // General security violation
    DOUBLE_SPENDING_DETECTED,      // Critical: Attempt to sell same item twice
    BLOCKCHAIN_TAMPERING_DETECTED, // Critical: Hash mismatch in chain history

    // Financial & Blockchain
    TRANSACTION_COMPLETED, // Used in TransactionReport
}