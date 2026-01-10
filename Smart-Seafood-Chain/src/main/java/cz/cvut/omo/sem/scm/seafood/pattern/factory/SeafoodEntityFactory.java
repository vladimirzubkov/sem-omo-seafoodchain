package cz.cvut.omo.sem.scm.seafood.pattern.factory;

import cz.cvut.omo.sem.scm.seafood.config.Configuration;
import cz.cvut.omo.sem.scm.seafood.model.SimulationEntity;
import cz.cvut.omo.sem.scm.seafood.model.device.Device;
import cz.cvut.omo.sem.scm.seafood.model.device.FoodProcessingRobot;
import cz.cvut.omo.sem.scm.seafood.model.device.IndustrialFreezer;
import cz.cvut.omo.sem.scm.seafood.model.employee.Employee;
import cz.cvut.omo.sem.scm.seafood.model.employee.role.*;
import cz.cvut.omo.sem.scm.seafood.model.geo.SeaRegion;
import cz.cvut.omo.sem.scm.seafood.model.party.Party;
import cz.cvut.omo.sem.scm.seafood.model.party.role.*;
import cz.cvut.omo.sem.scm.seafood.model.recipe.ChristmasCarpRecipe;
import cz.cvut.omo.sem.scm.seafood.model.recipe.FriedSalmonRecipe;
import cz.cvut.omo.sem.scm.seafood.model.recipe.Recipe;
import cz.cvut.omo.sem.scm.seafood.model.recipe.SushiBoxRecipe;
import cz.cvut.omo.sem.scm.seafood.resource.Money;
import cz.cvut.omo.sem.scm.seafood.type.device.RobotCapability;
import cz.cvut.omo.sem.scm.seafood.type.domain.SeafoodType;
import cz.cvut.omo.sem.scm.seafood.type.operation.StorageTemperature;

import java.util.*;

public class SeafoodEntityFactory extends EntityFactory {

    @Override
    public List<SimulationEntity> createEntities(Configuration config) {
        List<SimulationEntity> entities = new ArrayList<>();
        System.out.println("Factory: Creating entities from config...");

        // 1. Create Sea Regions map for lookups
        Map<String, SeaRegion> regionMap = new HashMap<>();
        if (config.getSeaRegions() != null) {
            for (Configuration.SeaRegionConfig rc : config.getSeaRegions()) {
                SeaRegion region = new SeaRegion();
                region.setName(rc.getName());
                region.setSeasonMultiplier(rc.getSeasonMultiplier());

                // Parse fish types available in the region
                if (rc.getFishTypes() != null) {
                    rc.getFishTypes().forEach(s -> {
                        try {
                            // Removing spaces to safely map Enums (e.g. "King Crab" -> "KING_CRAB")
                            String enumName = s.toUpperCase().replace(" ", "_");
                            region.getSeaFoodList().add(SeafoodType.valueOf(enumName));
                        } catch (IllegalArgumentException e) {
                            System.err.println("Warning: Unknown seafood type: %s".formatted(s));
                        }
                    });
                }
                regionMap.put(rc.getName(), region);
            }
        }

        // 2. Create Parties AND Employees (mixed in YAML under 'parties')
        if (config.getParties() != null) {
            for (Configuration.PartyConfig pc : config.getParties()) {
                String type = pc.getType().toUpperCase();

                if (isEmployeeType(type)) {
                    // Create Employee and add to entities list
                    entities.add(createEmployeeFromConfig(pc));
                } else {
                    // Create Organization (Party)
                    Party party = createPartyFromConfig(pc, regionMap);
                    entities.add(party);
                }
            }
        }

        // 3. Create Devices
        if (config.getDevices() != null) {
            for (Configuration.DeviceConfig dc : config.getDevices()) {
                Device device = createDeviceFromConfig(dc);
                if (device != null) {
                    entities.add(device);
                }
            }
        }

        System.out.println("Factory: Created %d entities.".formatted(entities.size()));
        return entities;
    }

    /**
     * Helper to detect if the "Party" entry in YAML is actually a human worker.
     */
    private boolean isEmployeeType(String type) {
        return List.of("COOK", "REPAIRTECHNICIAN", "SCMMANAGER", "INSPECTOR", "WORKER").contains(type);
    }

    /**
     * Creates an Employee entity and assigns the Strategy Role.
     */
    private Employee createEmployeeFromConfig(Configuration.PartyConfig pc) {
        // Default salary 200 CZK/h if not specified
        Money salary = Money.czk(200);
        Employee emp = new Employee(pc.getId(), pc.getName(), salary);

        String type = pc.getType().toUpperCase();
        switch (type) {
            case "COOK" -> emp.addRole(new ManualLaborRole());
            case "REPAIRTECHNICIAN" -> emp.addRole(new MaintenanceRole());
            case "SCMMANAGER" -> emp.addRole(new ManagementRole());
            case "INSPECTOR" -> emp.addRole(new InspectionRole());
        }

        // Default shift: 09:00 - 17:00 (8 hours)
        emp.setShiftStartHour(9);
        emp.setShiftDurationHours(8);

        return emp;
    }

    private Party createPartyFromConfig(Configuration.PartyConfig pc, Map<String, SeaRegion> regionMap) {
        Party party = new Party(pc.getId(), pc.getName());
        party.setBalance(parseMoney(pc.getBalance()));

        String type = pc.getType().toUpperCase();

        // --- Business Roles (Strategy Pattern) ---
        // CRITICAL FIX: Adding MerchantRole to everyone who needs to trade.
        switch (type) {
            case "FISHER" -> {
                SeaRegion region = regionMap.get(pc.getFishingZone());
                party.addRole(new ProducerRole(region, 0.3));
                // Fisher sells, but doesn't buy
                party.addRole(new MerchantRole(false, true));
            }
            case "PROCESSOR" -> {
                List<Recipe> recipes = new ArrayList<>();
                recipes.add(new SushiBoxRecipe());
                recipes.add(new FriedSalmonRecipe());
                recipes.add(new ChristmasCarpRecipe());

                party.addRole(new ProcessorRole(recipes));
                party.addRole(new StorageRole(1000.0, StorageTemperature.FROZEN));
                // Processor buys raw material and sells products
                party.addRole(new MerchantRole(true, true));
            }
            case "WAREHOUSE" -> {
                party.addRole(new StorageRole(5000.0, StorageTemperature.FROZEN));
                party.addRole(new TransportRole(60.0));
                // Warehouse acts as a middleman (buys/stores/sells)
                party.addRole(new MerchantRole(true, true));
            }
            case "DISTRIBUTOR" -> {
                party.addRole(new TransportRole(80.0));
                party.addRole(new StorageRole(2000.0, StorageTemperature.CHILLED));
                // Distributor buys and sells
                party.addRole(new MerchantRole(true, true));
            }
            case "KITCHEN", "RESTAURANT" -> {
                party.addRole(new ConsumerRole(5.0)); // Consumes ingredients
                party.addRole(new StorageRole(500.0, StorageTemperature.CHILLED));
                // Restaurant buys ingredients. Can also sell meals (hence true, true)
                party.addRole(new MerchantRole(true, true));
            }
            case "CUSTOMER" -> {
                party.addRole(new ConsumerRole(2.0));
                // Customer buys, doesn't sell
                party.addRole(new MerchantRole(true, false));
            }
            case "IMPORTER" -> {
                // LOGIC CHANGE: Importer acts as a Producer (Source) in this simulation.
                // It simulates receiving cargo as "catching" from a specific region.

                // 1. Try to find the import region (e.g., "Imported Pacific")
                SeaRegion importRegion = regionMap.get("Imported Pacific");

                // Fallback: If specific region not found, use the first available one to prevent errors
                if (importRegion == null && !regionMap.isEmpty()) {
                    importRegion = regionMap.values().iterator().next();
                }

                if (importRegion != null) {
                    // Assign ProducerRole with high probability (0.8) to simulate steady supply
                    party.addRole(new ProducerRole(importRegion, 0.8));
                }

                // 2. MerchantRole: Importer sells items but does not buy from local fishers
                // Buyer = false, Seller = true
                party.addRole(new MerchantRole(false, true));
            }
        }
        return party;
    }

    private Device createDeviceFromConfig(Configuration.DeviceConfig dc) {
        Money maintenance = parseMoney(dc.getMaintenanceCost());

        // Added common industrial machines to Robot category to ensure they work
        return switch (dc.getType()) {
            case "IndustrialFreezer", "BlastFreezer", "SuperFreezer" ->
                    new IndustrialFreezer(dc.getId(), dc.getEnergyPerHour(), maintenance, StorageTemperature.FROZEN);

            case "FoodProcessingRobot", "PrecisionRobot", "SushiRobotPremium", "PackingRobot", "FilletingMachine", "HighSpeedFilletLine" -> {
                Set<RobotCapability> caps = new HashSet<>();
                caps.add(RobotCapability.CHOPPING);
                caps.add(RobotCapability.FILLETING);
                caps.add(RobotCapability.COOKING);
                yield new FoodProcessingRobot(dc.getId(), dc.getEnergyPerHour(), maintenance, caps);
            }

            default -> {
                System.out.println("Factory warning: Unknown device type '%s', creating generic Freezer.".formatted(dc.getType()));
                yield new IndustrialFreezer(dc.getId(), dc.getEnergyPerHour(), maintenance, StorageTemperature.ROOM_TEMP);
            }
        };
    }

    private Money parseMoney(String moneyString) {
        if (moneyString == null || moneyString.isBlank()) return Money.czk(0);

        try {
            String[] parts = moneyString.trim().split("\\s+");
            double amount = Double.parseDouble(parts[0]);
            String currency = parts.length > 1 ? parts[1] : "CZK";

            return switch (currency.toUpperCase()) {
                case "EUR" -> Money.eur(amount);
                case "USD" -> Money.usd(amount);
                default -> Money.czk(amount);
            };
        } catch (Exception e) {
            System.err.println("Error parsing money: %s".formatted(moneyString));
            return Money.czk(0);
        }
    }
}