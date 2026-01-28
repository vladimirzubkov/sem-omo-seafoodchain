package cz.cvut.omo.sem.scm.seafood.model.item;

import cz.cvut.omo.sem.scm.seafood.blockchain.Blockchain;
import cz.cvut.omo.sem.scm.seafood.blockchain.Transaction;
import cz.cvut.omo.sem.scm.seafood.model.geo.SeaRegion;
import cz.cvut.omo.sem.scm.seafood.pattern.prototype.Prototype;
import cz.cvut.omo.sem.scm.seafood.pattern.state.item.CaughtState;
import cz.cvut.omo.sem.scm.seafood.pattern.state.item.ItemLifecycleState;
import cz.cvut.omo.sem.scm.seafood.type.operation.StorageTemperature;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Abstract base class representing any goods in the supply chain.
 * Implements the COMPOSITE Pattern (as the Component).
 * Implements PROTOTYPE Pattern (Cloneable) for Memento snapshots.
 */
@Data
@EqualsAndHashCode(of = "itemId")
public abstract class Item implements Prototype<Item> {

    private String itemId;
    private String name;
    private LocalDateTime catchTime;
    private SeaRegion originRegion;
    private StorageTemperature requiredStorage = StorageTemperature.CHILLED;
    private List<TemperatureRecord> temperatureHistory = new ArrayList<>();
    private double currentTemperature = 0.0;
    private double qualityLevel = 100.0;
    private double weightKg = 0.0;

    // --- STATE PATTERN ---
    // Initial state is CaughtState
    private ItemLifecycleState lifecycleState = new CaughtState();

    /**
     * Transitions the item to the next logical state in its lifecycle.
     */
    public void nextState() {
        if (this.lifecycleState != null) {
            this.lifecycleState = this.lifecycleState.nextState(this);
            System.out.println("[ITEM STATE] Item %s transitioned to: %s".formatted(
                    itemId, this.lifecycleState.getStateName()));
        }
    }

    public void recordTemperature(double temp) {
        temperatureHistory.add(new TemperatureRecord(LocalDateTime.now(), temp));
        currentTemperature = temp;

        // Delegate logic to the current State
        if (lifecycleState != null) {
            lifecycleState.handleTemperature(this, temp);
        }
    }

    public void markCaught(SeaRegion region, LocalDateTime time) {
        this.originRegion = region;
        this.catchTime = time;
        // Reset state ensures we start correctly
        this.lifecycleState = new CaughtState();
    }

    public String getOriginCertificate(Blockchain blockchain) {
        return blockchain.findFirstTransaction(this.itemId)
                .map(Transaction::getHash)
                .orElse("UNCERTIFIED");
    }

    // --- COMPOSITE PATTERN METHODS ---
    public boolean isContainer() { return false; }
    public List<Item> getContents() { return Collections.emptyList(); }
    public double getTotalWeight() { return this.weightKg; }

    // --- PROTOTYPE PATTERN (For Memento) ---
    @Override
    public Item clone() {
        try {
            // Shallow copy via super.clone()
            Item cloned = (Item) super.clone();
            // Deep copy of mutable lists
            cloned.temperatureHistory = new ArrayList<>(this.temperatureHistory);
            // State objects are usually stateless or shared, but if they carry data, they should be cloned too.
            // Here we assume State classes are essentially singletons/stateless logic containers.
            return cloned;
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException("Cloning failed for Item", e);
        }
    }
}