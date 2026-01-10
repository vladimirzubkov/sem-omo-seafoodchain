package cz.cvut.omo.sem.scm.seafood.blockchain;

import cz.cvut.omo.sem.scm.seafood.model.item.Item;
import cz.cvut.omo.sem.scm.seafood.model.party.Party;
import cz.cvut.omo.sem.scm.seafood.type.operation.OperationType;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Central Ledger.
 * In a real decentralized system, this would be distributed.
 * For this simulation, it's a Singleton-like service managed by the Simulator.
 */
public class Blockchain {

    // The single source of truth - sequential list of blocks/transactions
    @Getter
    private final List<Transaction> chain = new ArrayList<>();

    public Blockchain() {
        // No genesis block needed strictly for this logic, but chain starts empty
    }

    /**
     * Creates and records a new transaction.
     * Automatically links it to the previous transaction hash.
     */
    public void addTransaction(Party from, Party to, Item item, OperationType type, LocalDateTime time) {
        String prevHash = "0";
        if (!chain.isEmpty()) {
            prevHash = chain.getLast().getHash();
        }

        Transaction tx = Transaction.builder()
                .transactionId(UUID.randomUUID().toString())
                .from(from)
                .to(to)
                .item(item)
                .operationType(type)
                .timestamp(time)
                .previousHash(prevHash)
                .build();

        // Calculate hash after setting all data
        tx.setHash(tx.calculateHash());

        // Add to ledger
        chain.add(tx);

        System.out.println("[BLOCKCHAIN] New block recorded: %s for item %s".formatted(type, item.getItemId()));
    }

    /**
     * Finds the VERY FIRST transaction for a specific item ID, regardless of operation type.
     * Added to resolve compilation error in Item class.
     */
    public Optional<Transaction> findFirstTransaction(String itemId) {
        return chain.stream()
                .filter(t -> t.getItem().getItemId().equals(itemId))
                .findFirst();
    }

    /**
     * FRQ1: Traceability.
     * Finds the creation transaction (Origin) for a specific item.
     */
    public Optional<Transaction> findOrigin(String itemId) {
        return chain.stream()
                .filter(t -> t.getItem().getItemId().equals(itemId))
                .filter(t -> t.getOperationType() == OperationType.FISHING || t.getOperationType() == OperationType.PROCESSING)
                .findFirst();
    }

    /**
     * FRQ1: Traceability.
     * Returns full history for an item.
     */
    public List<Transaction> getItemHistory(String itemId) {
        return chain.stream()
                .filter(t -> t.getItem().getItemId().equals(itemId))
                .collect(Collectors.toList());
    }

    /**
     * FRQ3: Security & Integrity.
     * Checks if the blockchain has been tampered with.
     * Re-calculates hashes and compares links.
     */
    public boolean validateChain() {
        for (int i = 1; i < chain.size(); i++) {
            Transaction current = chain.get(i);
            Transaction previous = chain.get(i - 1);

            // 1. Check data integrity (re-hash)
            if (!current.getHash().equals(current.calculateHash())) {
                System.err.println("Blockchain Tampering Detected: Invalid Hash at block %d".formatted(i));
                return false;
            }

            // 2. Check chain links
            if (!current.getPreviousHash().equals(previous.getHash())) {
                System.err.println("Blockchain Tampering Detected: Broken Chain Link at block %d".formatted(i));
                return false;
            }
        }
        return true;
    }
}