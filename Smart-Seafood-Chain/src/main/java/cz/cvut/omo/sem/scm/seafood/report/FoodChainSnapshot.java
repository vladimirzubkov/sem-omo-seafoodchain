package cz.cvut.omo.sem.scm.seafood.report;

import cz.cvut.omo.sem.scm.seafood.blockchain.Blockchain;
import cz.cvut.omo.sem.scm.seafood.blockchain.Transaction;
import cz.cvut.omo.sem.scm.seafood.model.item.Item;
import cz.cvut.omo.sem.scm.seafood.model.party.Party;

import java.util.List;
import java.util.stream.Collectors;

public class FoodChainSnapshot implements ReportVisitor {

    @Override
    public String visit(Party party) {
        StringBuilder report = new StringBuilder();
        report.append("=== REPORT FOR PARTY: ").append(party.getName()).append(" ===\n");
        report.append("Type: ").append(party.getClass().getSimpleName()).append("\n");
        report.append("Inventory Size: ").append(party.getInventory().size()).append("\n");

        // Inventory Details
        if (!party.getInventory().isEmpty()) {
            report.append("--- Inventory Items ---\n");
            for (Item item : party.getInventory()) {
                report.append(String.format("- %s (ID: %s) | %.2f kg | Quality: %.1f%%\n",
                        item.getName(), item.getItemId(), item.getWeightKg(), item.getQualityLevel()));
            }
        }

        return report.toString();
    }

    /**
     * Generates a global report for the entire Blockchain (Audit).
     */
    public String generateBlockchainReport(Blockchain blockchain) {
        StringBuilder sb = new StringBuilder();
        sb.append("=== GLOBAL BLOCKCHAIN AUDIT ===\n");
        sb.append("Total Blocks: ").append(blockchain.getChain().size()).append("\n");
        sb.append("Chain Validity: ").append(blockchain.validateChain() ? "VALID" : "CORRUPTED!").append("\n");

        sb.append("\n--- Latest Transactions ---\n");
        // Show last 5 transactions
        List<Transaction> tail = blockchain.getChain().stream()
                .skip(Math.max(0, blockchain.getChain().size() - 5))
                .collect(Collectors.toList());

        for (Transaction tx : tail) {
            sb.append(String.format("[%s] %s | Item: %s | From: %s -> To: %s\n",
                    tx.getTimestamp(), tx.getOperationType(),
                    (tx.getItem() != null ? tx.getItem().getName() : "N/A"),
                    (tx.getFrom() != null ? tx.getFrom().getName() : "null"),
                    (tx.getTo() != null ? tx.getTo().getName() : "null")
            ));
        }

        return sb.toString();
    }
}