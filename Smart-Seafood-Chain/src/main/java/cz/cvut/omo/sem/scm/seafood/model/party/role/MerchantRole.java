package cz.cvut.omo.sem.scm.seafood.model.party.role;

import cz.cvut.omo.sem.scm.seafood.blockchain.Transaction;
import cz.cvut.omo.sem.scm.seafood.model.item.Item;
import cz.cvut.omo.sem.scm.seafood.model.party.Party;
import cz.cvut.omo.sem.scm.seafood.resource.Money;
import cz.cvut.omo.sem.scm.seafood.type.operation.OperationType;
import cz.cvut.omo.sem.scm.seafood.type.role.BusinessRoleType;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class MerchantRole implements BusinessRole {

    private final boolean isBuyer;
    private final boolean isSeller;

    // The partner to trade with (simplified logic: pushing goods to the next link in the chain)
    @Setter
    private Party targetPartner;

    public MerchantRole(boolean isBuyer, boolean isSeller) {
        this.isBuyer = isBuyer;
        this.isSeller = isSeller;
    }

    @Override
    public void performLogic(Party context) {
        // Logic: Seller actively pushes items to the buyer (Target Partner)
        if (isSeller && targetPartner != null) {
            sellItems(context);
        }
    }

    private void sellItems(Party seller) {
        // Use a copy to safely remove items while iterating
        List<Item> snapshot = new ArrayList<>(seller.getInventory());

        for (Item item : snapshot) {
            // Logic: Sell only items that are NOT raw materials (unless selling to Processor)
            // For simplicity in this simulation: Sell everything that is ready.

            // 1. Calculate Price (Simple mock: 100 CZK/kg)
            double priceVal = item.getWeightKg() * 100.0;

            // 2. Execute Trade
            transferItem(seller, targetPartner, item);

            // 3. Record in Blockchain (Critical for FRQ2)
            if (seller.getBlockchain() != null) {
                seller.getBlockchain().addTransaction(
                        seller,
                        targetPartner,
                        item,
                        OperationType.SALE,
                        LocalDateTime.now()
                );
            }

            System.out.println("[MERCHANT] %s sold %s to %s for %.2f CZK".formatted(
                    seller.getName(), item.getItemId(), targetPartner.getName(), priceVal));
        }
    }

    private void transferItem(Party from, Party to, Item item) {
        from.getInventory().remove(item);
        to.getInventory().add(item);
    }

    @Override
    public BusinessRoleType getRoleType() {
        return BusinessRoleType.MERCHANT;
    }
}