package cz.cvut.omo.sem.scm.seafood.type.role;

/**
 * Defines the strategic capabilities of an organization (Party).
 * Used to configure the supply chain nodes.
 */
public enum BusinessRoleType {
    PRODUCER,   // Catching fish, farming
    PROCESSOR,  // Manufacturing, cooking
    STORAGE,    // Warehousing, freezing
    TRANSPORT,  // Logistics, moving goods
    MERCHANT,   // Buying and selling, transactions
    CONSUMER    // End-of-life consumption (Sink)
}