package cz.cvut.omo.sem.scm.seafood.model.party.role;

import cz.cvut.omo.sem.scm.seafood.blockchain.Transaction;
import cz.cvut.omo.sem.scm.seafood.event.EventBus;
import cz.cvut.omo.sem.scm.seafood.model.item.Item;
import cz.cvut.omo.sem.scm.seafood.model.party.Party;
import cz.cvut.omo.sem.scm.seafood.pattern.builder.EventBuilder;
import cz.cvut.omo.sem.scm.seafood.type.operation.EventType;
import cz.cvut.omo.sem.scm.seafood.type.operation.OperationType;
import cz.cvut.omo.sem.scm.seafood.type.resource.Currency;
import cz.cvut.omo.sem.scm.seafood.type.role.BusinessRoleType;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class MerchantRole implements BusinessRole {

    private final boolean isBuyer;
    private final boolean isSeller;

    // The partner to trade with (pushes goods to the next link in the chain)
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

        // Directly retrieve currency (handled by Party class)
        Currency currency = seller.getCurrency();
        if (currency == null) {
            currency = Currency.USD; // Safety fallback just in case config is empty
        }

        for (Item item : snapshot) {
            // Logic: Sell everything that is ready in the inventory.
            // CHECK: Do not sell raw materials (Ingredients) intended for production!
            if (item instanceof cz.cvut.omo.sem.scm.seafood.model.item.Material) {
                continue;
            }

            // 1. Calculate Price (Simple mock: 100 units per kg)
            double priceVal = item.getWeightKg() * 100.0;

            // 2. Execute Trade (Move item from Seller to Buyer)
            transferItem(seller, targetPartner, item);

            // 3. Record in Blockchain (Critical for traceability)
            if (seller.getBlockchain() != null) {
                seller.getBlockchain().addTransaction(
                        seller,
                        targetPartner,
                        item,
                        OperationType.SALE,
                        LocalDateTime.now());
            }

            // 4. Publish Event (Using dynamic currency)
            EventBus.getInstance().publish(new EventBuilder()
                    .type(EventType.TRANSACTION_COMPLETED)
                    .sourceId(seller.getId())
                    .targetId(targetPartner.getId())
                    .description("Sold %s to %s for %.2f %s".formatted(
                            item.getItemId(),
                            targetPartner.getName(),
                            priceVal,
                            currency))
                    .payload(item)
                    .build());

            // 5. Console Log
            System.out.println("[MERCHANT] %s sold %s to %s for %.2f %s".formatted(
                    seller.getName(),
                    item.getItemId(),
                    targetPartner.getName(),
                    priceVal,
                    currency));
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