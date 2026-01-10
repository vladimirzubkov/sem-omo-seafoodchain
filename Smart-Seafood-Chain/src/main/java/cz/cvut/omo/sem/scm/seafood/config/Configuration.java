package cz.cvut.omo.sem.scm.seafood.config;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import java.util.List;
import java.util.ArrayList;

@JsonIgnoreProperties(ignoreUnknown = true)
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
        private String type; // e.g., "Fisher", "Processor"
        private String id;
        private String name;
        private String balance; // String to parse currency later
        private String fishingZone; // Optional (for Fisher)
        private boolean acceptsDineIn; // Optional (for Kitchen)
        private boolean supportsDelivery; // Optional
        private boolean suppliesSupermarkets; // Optional
    }

    @Data
    public static class DeviceConfig {
        private String type; // e.g., "IndustrialFreezer"
        private String id;
        private double energyPerHour;
        private String maintenanceCost;
    }

    @Data
    public static class ProductionLineConfig {
        private String name;
        private List<String> devices;
        private List<String> cooks;
        private List<String> technicians;
    }
}