package cz.cvut.omo.sem.scm.seafood.config;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import java.util.List;
import java.util.ArrayList;

@JsonIgnoreProperties(ignoreUnknown = true) // Ignores properties present in YAML but not defined in this class, prevents crash
@Data
public class Configuration {
    private SimulationConfig simulation;
    private CompanyConfig company;
    private List<SeaRegionConfig> seaRegions = new ArrayList<>();
    private List<PartyConfig> parties = new ArrayList<>();
    private List<DeviceConfig> devices = new ArrayList<>();
    private List<ProductionLineConfig> productionLines = new ArrayList<>();

    // --- Inner DTOs matching YAML structure ---

    @Data
    public static class CompanyConfig {
        private String name;
        private String description;
    }

    @Data
    public static class SeaRegionConfig {
        private String name;
        private double seasonMultiplier;
        private List<String> fishTypes;
    }

    @Data
    public static class PartyConfig {
        // --- Common Fields ---
        private String type;        // Mapped to BusinessRoleType (Fisher, Importer, Restaurant, etc.)
        private String id;
        private String name;
        private String balance;     // Parsed into Money object

        // --- Producer Specific ---
        private List<String> fishingZones; // Mega corps can be present at various sea regions - producing space scale

        // --- Importer Specific ---
        private Integer importPeriodHours; // Optional: Override default 24h period for Importers - importing time frequency scale

        // --- Consumer / Merchant Flags (Optional logic) ---
        private boolean acceptsDineIn;       // Can be used to tweak Restaurant behavior
        private boolean supportsDelivery;    // Can be used for Logistics logic
        private boolean suppliesSupermarkets;// Can be used for B2B contracts
    }

    @Data
    public static class DeviceConfig {
        private String type; // e.g., "IndustrialFreezer"
        private String id;
        private double energyPerHour;
        private String maintenanceCost;

        // --- when we use strict typing for devices, we may consider using additional properties from config file
        private List<String> capabilities; // For FoodProcessingRobot
        private String storageMode;        // For IndustrialFreezer
        private Integer speed;             // For PackagingMachine / Conveyor
    }

    @Data
    public static class ProductionLineConfig {
        private String name;
        private String ownerId;
        private List<String> devices;
        private List<String> cooks;
        private List<String> technicians;
    }
}