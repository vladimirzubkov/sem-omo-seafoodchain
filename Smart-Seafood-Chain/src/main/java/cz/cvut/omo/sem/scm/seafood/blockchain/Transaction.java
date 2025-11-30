package cz.cvut.omo.sem.scm.seafood.blockchain;

import cz.cvut.omo.sem.scm.seafood.model.item.Item;
import cz.cvut.omo.sem.scm.seafood.model.party.Party;
import cz.cvut.omo.sem.scm.seafood.type.operation.OperationType;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class Transaction {
    private String transactionId;
    private Party from;
    private Party to;
    private Item item;
    private LocalDateTime timestamp;
    private OperationType operationType;
    private String previousHash;
    private String hash;

    public static Object getHash(Object t) {
        return null;
    }
}