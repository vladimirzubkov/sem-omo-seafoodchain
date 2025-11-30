package cz.cvut.omo.sem.scm.seafood.model.party.role;

import cz.cvut.omo.sem.scm.seafood.model.party.Party;
import cz.cvut.omo.sem.scm.seafood.type.role.BusinessRoleType;

public interface BusinessRole {
    /**
     * Main execution method called every simulation tick.
     */
    void performLogic(Party context);

    /**
     * Returns the specific enum for Party capabilities.
     */
    BusinessRoleType getRoleType();
}