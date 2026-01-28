package cz.cvut.omo.sem.scm.seafood.pattern.factory;

import cz.cvut.omo.sem.scm.seafood.config.Configuration;
import cz.cvut.omo.sem.scm.seafood.model.SimulationEntity;
import cz.cvut.omo.sem.scm.seafood.model.device.*;
import cz.cvut.omo.sem.scm.seafood.model.employee.Employee;
import cz.cvut.omo.sem.scm.seafood.model.employee.role.*;
import cz.cvut.omo.sem.scm.seafood.model.production.ProductionLine;
import cz.cvut.omo.sem.scm.seafood.model.geo.SeaRegion;
import cz.cvut.omo.sem.scm.seafood.model.party.Party;
import cz.cvut.omo.sem.scm.seafood.model.party.role.*;
import cz.cvut.omo.sem.scm.seafood.model.recipe.ChristmasCarpRecipe;
import cz.cvut.omo.sem.scm.seafood.model.recipe.FriedSalmonRecipe;
import cz.cvut.omo.sem.scm.seafood.model.recipe.FrozenBlockRecipe;
import cz.cvut.omo.sem.scm.seafood.model.recipe.Recipe;
import cz.cvut.omo.sem.scm.seafood.model.recipe.SushiBoxRecipe;
import cz.cvut.omo.sem.scm.seafood.resource.Money;
import cz.cvut.omo.sem.scm.seafood.type.device.DeviceType;
import cz.cvut.omo.sem.scm.seafood.type.device.RobotCapability;
import cz.cvut.omo.sem.scm.seafood.type.domain.SeafoodType;
import cz.cvut.omo.sem.scm.seafood.type.operation.StorageTemperature;
import cz.cvut.omo.sem.scm.seafood.type.role.BusinessRoleType;
import cz.cvut.omo.sem.scm.seafood.type.role.LaborRoleType;
import cz.cvut.omo.sem.scm.seafood.type.resource.Currency;

import java.util.*;
import java.util.stream.Collectors;

public class SeafoodEntityFactory extends EntityFactory {

    @Override
    public List<SimulationEntity> createEntities(Configuration config) {
        List<SimulationEntity> entities = new ArrayList<>();
        System.out.println("Factory: Creating entities from config...");

        // 1. Create Sea Regions map for lookups
        Map<String, SeaRegion> regionMap = new HashMap<>();
        if (config.getSeaRegions() != null) {
            // for-each, iterates through a config file, which holds all data from the YAML
            // file
            // Configuration.SeaRegionConfig is a datatype, referred so because it is an
            // inner static class in Configuration
            // rc - region config
            for (Configuration.SeaRegionConfig rc : config.getSeaRegions()) {
                SeaRegion region = new SeaRegion();
                region.setName(rc.getName());
                region.setSeasonMultiplier(rc.getSeasonMultiplier());

                // Parse fish types available in the region, mapping to Enum
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

        // 2. Create and filter Parties AND Employees (mixed in YAML under 'parties'),
        // by their type
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

        // 4. Link Production Lines
        // Now that all entities (Parties, Devices, Employees) exist, we can wire them
        // together.
        linkProductionLines(config, entities);

        System.out.println("Factory: Created %d entities.".formatted(entities.size()));
        return entities;
    }

    /**
     * Logic to assemble ProductionLines from configuration and inject them into
     * Processor parties.
     */
    private void linkProductionLines(Configuration config, List<SimulationEntity> entities) {
        if (config.getProductionLines() == null || config.getProductionLines().isEmpty())
            return;

        // Map entities by ID for fast lookup
        Map<String, SimulationEntity> entityMap = entities.stream()
                .collect(Collectors.toMap(SimulationEntity::getId, e -> e));

        for (Configuration.ProductionLineConfig plConfig : config.getProductionLines()) {
            // A. Find Owner Party
            String ownerId = plConfig.getOwnerId();
            if (ownerId == null || !entityMap.containsKey(ownerId)) {
                System.err.println("Factory Warning: Production Line '%s' has invalid ownerId '%s'"
                        .formatted(plConfig.getName(), ownerId));
                continue;
            }

            if (entityMap.get(ownerId) instanceof Party ownerParty) {
                // B. Check for ProcessorRole
                Optional<ProcessorRole> procRoleOpt = ownerParty.getRoles().stream()
                        .filter(r -> r instanceof ProcessorRole)
                        .map(r -> (ProcessorRole) r)
                        .findFirst();

                if (procRoleOpt.isEmpty()) {
                    System.err.println("Factory Warning: Owner '%s' is not a PROCESSOR, cannot assign production line."
                            .formatted(ownerParty.getName()));
                    continue;
                }

                // C. Assemble Line
                ProductionLine line = new ProductionLine();
                line.setLineName(plConfig.getName());

                // Link Devices
                if (plConfig.getDevices() != null) {
                    for (String devId : plConfig.getDevices()) {
                        if (entityMap.get(devId) instanceof Device device) {
                            line.getCurrentConfiguration().add(device);
                            // add devices to the party so that Technician can find it
                            ownerParty.getDevices().add(device);
                        } else {
                            System.err.println("Factory Warning: Device '%s' not found for line '%s'".formatted(devId,
                                    plConfig.getName()));
                        }
                    }
                }

                // Link Employees (Cooks & Technicians are both Employees)
                List<String> allStaffIds = new ArrayList<>();
                if (plConfig.getCooks() != null)
                    allStaffIds.addAll(plConfig.getCooks());
                if (plConfig.getTechnicians() != null)
                    allStaffIds.addAll(plConfig.getTechnicians());

                for (String empId : allStaffIds) {
                    if (entityMap.get(empId) instanceof Employee employee) {
                        line.getAssignedEmployees().add(employee);
                        // Link Employee to Employer so they know who they work for
                        ownerParty.getEmployees().add(employee);
                        employee.setEmployer(ownerParty);
                    } else {
                        System.err.println("Factory Warning: Employee '%s' not found for line '%s'".formatted(empId,
                                plConfig.getName()));
                    }
                }

                // D. Inject into Role
                procRoleOpt.get().addProductionLine(line);
                System.out.println(
                        "Factory: Linked Line '%s' to Party '%s'".formatted(line.getLineName(), ownerParty.getName()));
            }
        }
    }

    /**
     * Helper to detect if the "Party" entry in YAML is actually a human worker.
     * Delegates the check to the LaborRoleType enum, which holds all valid aliases.
     */
    private boolean isEmployeeType(String type) {
        // We ask the Enum: "Can you parse this string?"
        // If it returns an Optional with a value (isPresent), then it is a valid
        // employee type.
        return LaborRoleType.fromYamlString(type).isPresent();
    }

    /**
     * Creates an Employee entity and assigns the Strategy Role.
     */
    private Employee createEmployeeFromConfig(Configuration.PartyConfig pc) {
        // Default salary 200 CZK/h if not specified
        Money salary = Money.czk(200);
        Employee emp = new Employee(pc.getId(), pc.getName(), salary);

        // If the YAML has a typo or unknown type, we default to MANUAL_LABOR (Fallback
        // logic).
        LaborRoleType roleType = LaborRoleType.fromYamlString(pc.getType())
                .orElse(LaborRoleType.MANUAL_LABOR);

        // Switch based on the Enum value
        switch (roleType) {
            case MANUAL_LABOR -> emp.addRole(new ManualLaborRole());
            case MAINTENANCE -> emp.addRole(new MaintenanceRole());
            case MANAGEMENT -> emp.addRole(new ManagementRole());
            case INSPECTION -> emp.addRole(new InspectionRole());
            case DRIVER, COURIER -> emp.addRole(new LogisticsRole(roleType));
        }
        // Default shift: 09:00 - 17:00 (8 hours)
        emp.setShiftStartHour(9);
        emp.setShiftDurationHours(8);

        return emp;
    }

    private Party createPartyFromConfig(Configuration.PartyConfig pc, Map<String, SeaRegion> regionMap) {
        Party party = new Party(pc.getId(), pc.getName());
        party.setBalance(parseMoney(pc.getBalance()));
        party.setType(pc.getType()); // Store the original type string for debugging purposes

        // --- Currency Parsing via Enum ---
        Currency currency = Currency.USD;

        if (pc.getBalance() != null && !pc.getBalance().isBlank()) {
            String[] parts = pc.getBalance().trim().split("\\s+");
            if (parts.length > 1) {
                try {
                    // Parse string directly to Enum
                    currency = Currency.valueOf(parts[1].toUpperCase());
                } catch (IllegalArgumentException e) {
                    System.err.println(
                            "Factory Warning: Unknown currency '%s' for party %s".formatted(parts[1], pc.getName()));
                }
            }
        }

        // Now we pass the Enum object, matching the new Party.java signature
        party.setCurrency(currency);

        // 1. Resolve the primary role using the Rich Enum mapping
        // Defaults to MERCHANT if the type is unknown
        BusinessRoleType primaryRole = BusinessRoleType.fromYamlString(pc.getType())
                .orElse(BusinessRoleType.MERCHANT);

        // 2. Configure the Party entity based on its resolved Primary Role
        switch (primaryRole) {
            case PRODUCER -> {
                // LOGIC: Space Scaling (Geographical presence)
                // If a company is assigned multiple zones, we create multiple ProducerRole
                // instances.
                if (pc.getFishingZones() != null && !pc.getFishingZones().isEmpty()) {
                    for (String zoneName : pc.getFishingZones()) {
                        SeaRegion region = regionMap.get(zoneName);
                        if (region != null) {
                            // Each region gets its own role, simulating parallel production
                            party.addRole(new ProducerRole(region, 0.3));
                        } else {
                            System.err.println("Factory Warning: Region '%s' not found for party %s"
                                    .formatted(zoneName, pc.getName()));
                        }
                    }
                } else {
                    System.err.println("Factory Warning: Producer '%s' has no fishing zones assigned!"
                            .formatted(pc.getName()));
                }

                // Producers always act as sellers in the market
                party.addRole(new MerchantRole(false, true));
            }

            case IMPORTER -> {
                // LOGIC: Time Frequency Scaling (Scheduled arrivals)
                // Use custom period from configuration or default to a 24-hour cycle
                int period = pc.getImportPeriodHours() != null ? pc.getImportPeriodHours() : 24;

                party.addRole(new ImporterRole(period));
                // Importers act as sellers (distributing foreign goods)
                party.addRole(new MerchantRole(false, true));
            }

            case PROCESSOR -> {
                List<Recipe> recipes = new ArrayList<>();
                recipes.add(new SushiBoxRecipe());
                recipes.add(new FriedSalmonRecipe());
                recipes.add(new ChristmasCarpRecipe());
                recipes.add(new FrozenBlockRecipe());

                // Processors transform raw materials into finished products
                party.addRole(new ProcessorRole(recipes));
                party.addRole(new StorageRole(1000.0, StorageTemperature.FROZEN));
                party.addRole(new MerchantRole(true, true));

                // INJECT STARTER INGREDIENTS so they can actually cook
                // 1000 units of rice and boxes to start with
                party.getInventory().add(new cz.cvut.omo.sem.scm.seafood.model.item.Material(
                        "INIT-RICE-" + pc.getId(),
                        cz.cvut.omo.sem.scm.seafood.type.domain.MaterialType.SUSHI_RICE,
                        1000.0));
                party.getInventory().add(new cz.cvut.omo.sem.scm.seafood.model.item.Material(
                        "INIT-BOX-" + pc.getId(),
                        cz.cvut.omo.sem.scm.seafood.type.domain.MaterialType.CARDBOARD_BOX,
                        1000.0));
            }

            case STORAGE -> {
                // Warehouses focus on bulk storage and initial logistics
                party.addRole(new StorageRole(5000.0, StorageTemperature.FROZEN));
                party.addRole(new TransportRole(60.0));
                party.addRole(new MerchantRole(true, true));
            }

            case TRANSPORT -> {
                // Distributors focus on fast transit and chilled storage
                party.addRole(new TransportRole(80.0));
                party.addRole(new StorageRole(2000.0, StorageTemperature.CHILLED));
                party.addRole(new MerchantRole(true, true));
            }

            case RESTAURANT -> { // B2B Consumer
                // Restaurants consume high volumes, require storage, and sell finished meals
                party.addRole(new ConsumerRole(BusinessRoleType.RESTAURANT, 50.0));
                party.addRole(new StorageRole(500.0, StorageTemperature.CHILLED));
                party.addRole(new MerchantRole(true, true));

                // Allow Restaurants to have Production Lines (cook fresh sushi onsite)
                List<Recipe> recipes = new ArrayList<>();
                recipes.add(new SushiBoxRecipe());
                recipes.add(new FriedSalmonRecipe());
                party.addRole(new ProcessorRole(recipes));
            }

            case CUSTOMER -> { // B2C Consumer
                // End customers consume small volumes and do not resell goods
                party.addRole(new ConsumerRole(BusinessRoleType.CUSTOMER, 2.0));
                party.addRole(new MerchantRole(true, false));
            }

            case MERCHANT -> {
                // Generic retail entity that buys and sells without processing
                party.addRole(new MerchantRole(true, true));
            }
        }
        return party;
    }

    private Device createDeviceFromConfig(Configuration.DeviceConfig dc) {
        Money maintenance = parseMoney(dc.getMaintenanceCost());

        // 1. Resolve Strict Type (using our Helper/Enum logic)
        DeviceType type = DeviceType.fromYamlString(dc.getType())
                .orElseThrow(() -> new IllegalArgumentException(
                        "Factory Error: Unknown device type '%s' in config.".formatted(dc.getType())));

        // 2. Create a specific instance based on Type and Config Fields
        // note, we are using a keyword 'yield' here as we are returning something
        // (available from Java 14)
        return switch (type) {
            case INDUSTRIAL_FREEZER -> {
                // Default to FROZEN, but allow override from config
                StorageTemperature temp = StorageTemperature.FROZEN;
                if (dc.getStorageMode() != null) {
                    try {
                        temp = StorageTemperature.valueOf(dc.getStorageMode().toUpperCase());
                    } catch (IllegalArgumentException e) {
                        System.err.println(
                                "Warning: Unknown storage mode '%s', using FROZEN.".formatted(dc.getStorageMode()));
                    }
                }
                yield new IndustrialFreezer(dc.getId(), dc.getEnergyPerHour(), maintenance, temp);
            }

            case FOOD_PROCESSING_ROBOT -> {
                Set<RobotCapability> caps = new HashSet<>();

                // Load capabilities from YAML
                if (dc.getCapabilities() != null) {
                    for (String capStr : dc.getCapabilities()) {
                        try {
                            caps.add(RobotCapability.valueOf(capStr.toUpperCase()));
                        } catch (IllegalArgumentException e) {
                            System.err.println(
                                    "Warning: Unknown capability '%s' for robot %s".formatted(capStr, dc.getId()));
                        }
                    }
                }

                // Fallback: If nothing specified, give basic CHOPPING ability so it's not
                // useless
                if (caps.isEmpty()) {
                    caps.add(RobotCapability.CHOPPING);
                }

                yield new FoodProcessingRobot(dc.getId(), dc.getEnergyPerHour(), maintenance, caps);
            }

            case PACKAGING_MACHINE -> {
                int speed = dc.getSpeed() != null ? dc.getSpeed() : 100;
                yield new PackagingMachine(dc.getId(), dc.getEnergyPerHour(), maintenance, speed);
            }

            case REFRIGERATOR ->
                new Refrigerator(dc.getId(), dc.getEnergyPerHour(), maintenance);

            case CONVEYOR_BELT -> {
                int speed = dc.getSpeed() != null ? dc.getSpeed() : 500;
                yield new ConveyorBelt(dc.getId(), dc.getEnergyPerHour(), maintenance, speed);
            }

            // the compiler will remind to add new cases if they will be added to DeviceType
            // enum
        };
    }

    private Money parseMoney(String moneyString) {
        if (moneyString == null || moneyString.isBlank())
            return Money.czk(0);

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