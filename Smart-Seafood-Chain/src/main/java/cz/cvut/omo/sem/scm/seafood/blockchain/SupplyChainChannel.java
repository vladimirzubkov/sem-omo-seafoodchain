package cz.cvut.omo.sem.scm.seafood.blockchain;

import cz.cvut.omo.sem.scm.seafood.model.party.Party;
import lombok.Data;

import java.util.HashSet;
import java.util.Set;

@Data
public class SupplyChainChannel {
    private String channelId;
    private String name;
    private Set<Party> participants = new HashSet<>();
    // trasnsations, rules, etc.
}