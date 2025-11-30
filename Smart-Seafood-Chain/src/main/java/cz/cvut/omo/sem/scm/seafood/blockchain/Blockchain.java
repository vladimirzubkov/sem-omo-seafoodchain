package cz.cvut.omo.sem.scm.seafood.blockchain;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Data
public class Blockchain {
    private final List<SupplyChainChannel> channels = new ArrayList<>();

    public <T> Optional<Transaction> findFirstTransaction(String itemId) {
        return null;
    }

    //  Merkle Tree, validation, etc.

}