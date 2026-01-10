package cz.cvut.omo.sem.scm.seafood.report;

import cz.cvut.omo.sem.scm.seafood.model.party.Party;

/**
 * Interface for the Visitor Pattern (FRQ15, SW3 Requirements).
 * Decouples reporting logic from the business entities.
 */
public interface ReportVisitor {
    /**
     * Visits a Party to extract its state for reporting.
     * @param party The element being visited.
     * @return Formatted string report.
     */
    String visit(Party party);
}