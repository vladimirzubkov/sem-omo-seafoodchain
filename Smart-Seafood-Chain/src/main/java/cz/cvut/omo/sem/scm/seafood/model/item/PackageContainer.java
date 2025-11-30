package cz.cvut.omo.sem.scm.seafood.model.item;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents packaging (Box, Pallet, Crate) that holds other Items.
 * Implements the COMPOSITE Pattern (as the Composite).
 */
@Getter
@Setter
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class PackageContainer extends Item {

    private final String packagingType; // e.g., "Retail Box", "Shipping Pallet"
    private final double tareWeight;    // Weight of the empty box itself

    // The items inside this package
    private final List<Item> contents = new ArrayList<>();

    public PackageContainer(String id, String packagingType, double tareWeight) {
        this.setItemId(id);
        this.packagingType = packagingType;
        this.tareWeight = tareWeight;
    }

    /**
     * Adds an item to this package.
     */
    public void addItem(Item item) {
        contents.add(item);
    }

    /**
     * Removes an item from this package.
     */
    public void removeItem(Item item) {
        contents.remove(item);
    }

    /**
     * Empties the container.
     * @return List of items that were inside.
     */
    public List<Item> unpack() {
        List<Item> unpackedItems = new ArrayList<>(contents);
        contents.clear();
        return unpackedItems;
    }

    // --- COMPOSITE PATTERN OVERRIDES ---

    @Override
    public boolean isContainer() {
        return true;
    }

    @Override
    public List<Item> getContents() {
        return contents;
    }

    @Override
    public double getTotalWeight() {
        // Recursive calculation: weight of box + weight of all items inside
        return tareWeight + contents.stream()
                .mapToDouble(Item::getTotalWeight)
                .sum();
    }

    /**
     * Propagates temperature recording to all items inside.
     * If the box gets hot, the fish inside gets hot too.
     */
    @Override
    public void recordTemperature(double temp) {
        // Record history for the box itself
        super.recordTemperature(temp);

        // Propagate to contents
        for (Item item : contents) {
            item.recordTemperature(temp);
        }
    }
}