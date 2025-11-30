package cz.cvut.omo.sem.scm.seafood.model.party.role;

import cz.cvut.omo.sem.scm.seafood.model.party.Party;
import cz.cvut.omo.sem.scm.seafood.type.role.BusinessRoleType;

public class TransportRole implements BusinessRole {

    private final double speedKmH;

    public TransportRole(double speedKmH) {
        this.speedKmH = speedKmH;
    }

    @Override
    public void performLogic(Party context) {
        // TODO: Implement transport logic
        // 1. Move items towards destination
        // 2. Handle arrival and handover
    }

    @Override
    public BusinessRoleType getRoleType() {
        return BusinessRoleType.TRANSPORT;
    }
}