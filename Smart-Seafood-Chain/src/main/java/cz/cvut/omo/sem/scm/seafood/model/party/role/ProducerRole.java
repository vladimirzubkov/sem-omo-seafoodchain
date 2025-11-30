package cz.cvut.omo.sem.scm.seafood.model.party.role;

import cz.cvut.omo.sem.scm.seafood.model.geo.SeaRegion;
import cz.cvut.omo.sem.scm.seafood.model.party.Party;
import cz.cvut.omo.sem.scm.seafood.type.role.BusinessRoleType;

public class ProducerRole implements BusinessRole {

    private final SeaRegion region;
    private final double catchProbability;

    public ProducerRole(SeaRegion region, double catchProbability) {
        this.region = region;
        this.catchProbability = catchProbability;
    }

    @Override
    public void performLogic(Party context) {
        // TODO: Implement harvesting/fishing logic
        // 1. Random check against probability
        // 2. Create new Item (Seafood)
        // 3. Add to context.inventory
    }

    @Override
    public BusinessRoleType getRoleType() {
        return BusinessRoleType.PRODUCER;
    }
}