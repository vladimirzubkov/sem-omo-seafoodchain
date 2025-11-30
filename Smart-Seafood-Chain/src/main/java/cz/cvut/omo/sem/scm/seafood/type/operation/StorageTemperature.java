package cz.cvut.omo.sem.scm.seafood.type.operation;

/**
 * Standard storage temperature zones for seafood products.
 * Based on EU food safety regulations.
 */
public enum StorageTemperature {
    FROZEN(-18.0, -25.0, -10.0, "Deep freeze storage"),
    CHILLED(4.0, 0.0, 6.0, "Refrigerated storage"),
    LIVE_FISH_TANK(4.0, 2.0, 8.0, "Live fish aquarium"),
    ROOM_TEMP(20.0, 15.0, 25.0, "Dry goods storage");

    private final double optimal;
    private final double minSafe;
    private final double maxSafe;
    private final String description;

    StorageTemperature(double optimal, double minSafe, double maxSafe, String description) {
        this.optimal = optimal;
        this.minSafe = minSafe;
        this.maxSafe = maxSafe;
        this.description = description;
    }

    public double getOptimal() { return optimal; }
    public double getMinSafe() { return minSafe; }
    public double getMaxSafe() { return maxSafe; }
    public String getDescription() { return description; }

    public boolean isSafe(double temperature) {
        return temperature >= minSafe && temperature <= maxSafe;
    }
}