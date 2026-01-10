package cz.cvut.omo.sem.scm.seafood.blockchain;

import cz.cvut.omo.sem.scm.seafood.model.item.Item;
import cz.cvut.omo.sem.scm.seafood.model.party.Party;
import cz.cvut.omo.sem.scm.seafood.type.operation.OperationType;
import lombok.Builder;
import lombok.Data;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;

/**
 * Represents an immutable record in the ledger.
 * Satisfies FRQ2 (Implementation).
 */
@Data
@Builder
public class Transaction {
    private String transactionId;
    private Party from;
    private Party to;
    private Item item;
    private LocalDateTime timestamp;
    private OperationType operationType;

    // Cryptographic links
    private String previousHash;
    private String hash;

    /**
     * Calculates SHA-256 hash of the transaction content.
     * Used to ensure integrity (Tamper-proof).
     */
    public String calculateHash() {
        String dataToHash = ""
                + (previousHash != null ? previousHash : "GENESIS")
                + (from != null ? from.getId() : "NULL")
                + (to != null ? to.getId() : "NULL")
                + (item != null ? item.getItemId() : "NULL")
                + (timestamp != null ? timestamp.toString() : "NULL")
                + (operationType != null ? operationType.name() : "NULL");

        return applySha256(dataToHash);
    }

    private String applySha256(String input) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hashBytes = digest.digest(input.getBytes(StandardCharsets.UTF_8));
            StringBuilder hexString = new StringBuilder();
            for (byte b : hashBytes) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }
            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("SHA-256 algorithm not found", e);
        }
    }
}