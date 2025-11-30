package cz.cvut.omo.sem.scm.seafood.model.party.role;


import cz.cvut.omo.sem.scm.seafood.model.party.Party;
import cz.cvut.omo.sem.scm.seafood.type.role.BusinessRoleType;

public class MerchantRole implements BusinessRole {

    private final boolean isBuyer;
    private final boolean isSeller;

    public MerchantRole(boolean isBuyer, boolean isSeller) {
        this.isBuyer = isBuyer;
        this.isSeller = isSeller;
    }

    @Override
    public void performLogic(Party context) {
        // TODO: Implement trading logic
        // 1. Post offers to Market Channel (if Seller)
        // 2. Send Pull Requests (if Buyer)
        // 3. Execute Transactions (Blockchain)
    }

    @Override
    public BusinessRoleType getRoleType() {
        return BusinessRoleType.MERCHANT;
    }
}