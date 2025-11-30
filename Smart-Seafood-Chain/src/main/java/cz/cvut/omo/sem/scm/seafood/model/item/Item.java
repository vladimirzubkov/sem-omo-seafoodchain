package cz.cvut.omo.sem.scm.seafood.model.item;

import cz.cvut.omo.sem.scm.seafood.blockchain.Blockchain;
import cz.cvut.omo.sem.scm.seafood.blockchain.Transaction;
import cz.cvut.omo.sem.scm.seafood.model.geo.SeaRegion;
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
 */
@Data
@EqualsAndHashCode(of = "itemId")
public abstract class Item {

    private String itemId;
    private String name; // Common name field for all item types (supports polymorphism)

    private LocalDateTime catchTime;
    private SeaRegion originRegion;

    private StorageTemperature requiredStorage = StorageTemperature.CHILLED; // default

    // Traceability – required for Inspector and reports (FRQ4)
    private List<TemperatureRecord> temperatureHistory = new ArrayList<>();

    private double currentTemperature = 0.0; // Current state
    private double qualityLevel = 100.0;     // 0-100%, decreases if temp is wrong

    // Common weight field (for single items, overridden by containers)
    private double weightKg = 0.0;

    /**
     * Records the temperature for this item at the current tick.
     * If this is a container, it propagates the record to all contents.
     * @param temp The measured temperature in Celsius.
     */
    public void recordTemperature(double temp) {
        temperatureHistory.add(new TemperatureRecord(LocalDateTime.now(), temp));
        currentTemperature = temp;

        // Quality degradation logic: if temperature out of safe range
        if (!requiredStorage.isSafe(temp)) {
            qualityLevel = Math.max(0, qualityLevel - 2.0);
        }
    }

    /**
     * Initializes the item origin (called by ProducerRole).
     */
    public void markCaught(SeaRegion region, LocalDateTime time) {
        this.originRegion = region;
        this.catchTime = time;
    }

    /**
     * Retrieves the origin proof from the Blockchain.
     */
    public String getOriginCertificate(Blockchain blockchain) {
        return blockchain.findFirstTransaction(this.itemId)
                .map(tx -> (String) Transaction.getHash(tx)) // Assuming Transaction has getHash
                .orElse("UNCERTIFIED");
    }

    // --- COMPOSITE PATTERN METHODS ---

    public boolean isContainer() {
        return false;
    }

    public List<Item> getContents() {
        return Collections.emptyList();
    }

    public double getTotalWeight() {
        return this.weightKg;
    }
}