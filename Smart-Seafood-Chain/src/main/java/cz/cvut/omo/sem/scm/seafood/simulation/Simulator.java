package cz.cvut.omo.sem.scm.seafood.simulation;

import cz.cvut.omo.sem.scm.seafood.blockchain.Blockchain;
import cz.cvut.omo.sem.scm.seafood.config.Configuration;
import cz.cvut.omo.sem.scm.seafood.event.EventBus;
import cz.cvut.omo.sem.scm.seafood.model.SimulationEntity;
import cz.cvut.omo.sem.scm.seafood.model.party.Party;
import cz.cvut.omo.sem.scm.seafood.model.party.role.*; // Import all roles
import cz.cvut.omo.sem.scm.seafood.pattern.factory.EntityFactory;
import cz.cvut.omo.sem.scm.seafood.pattern.factory.SeafoodEntityFactory;
import cz.cvut.omo.sem.scm.seafood.pattern.visitor.Visitable;
import cz.cvut.omo.sem.scm.seafood.pattern.visitor.WorldSetupVisitor;
import cz.cvut.omo.sem.scm.seafood.report.EventLogger;
import cz.cvut.omo.sem.scm.seafood.report.ReportGenerator;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class Simulator {
    @Getter
    private final List<SimulationEntity> entities = new ArrayList<>();
    private int currentTick = 0;
    private final Configuration config;
    private final String configName;

    private final EventBus eventBus = new EventBus();
    private final EventLogger systemLogger = new EventLogger();

    @Getter
    private final Blockchain blockchain = new Blockchain();

    public Simulator(Configuration config, String configName) {
        this.config = config;
        this.configName = configName;

        // 1. Configure Time
        Time.setTickDuration(config.getSimulation().getTickDurationMinutes());

        // 2. Create entities using Factory
        EntityFactory factory = new SeafoodEntityFactory();
        this.entities.addAll(factory.createEntities(config));

        // 3. Register visitors (EventBus, Blockchain injection)
        WorldSetupVisitor setupVisitor = new WorldSetupVisitor(eventBus, blockchain);
        for (SimulationEntity entity : entities) {
            if (entity instanceof Visitable visitable) {
                visitable.accept(setupVisitor);
            }
        }

        // 4. Subscribe logger
        for (var type : cz.cvut.omo.sem.scm.seafood.type.operation.EventType.values()) {
            eventBus.subscribe(type, systemLogger);
        }

        // 5. Link Supply Chain based on ROLES (Best Practice)
        linkSupplyChainByRoles(getParties());
    }

    public void start() {
        System.out.println("--- Starting Simulation: %s ---".formatted(configName));
        while (currentTick < config.getSimulation().getMaxTicks()) {
            tick();
        }
        generateReports();
    }

    private void tick() {
        currentTick++;
        if (currentTick % 24 == 0) {
            System.out.println("--- Day %d ---".formatted(currentTick / 24));
        }
        for (SimulationEntity entity : entities) {
            entity.handleTick();
        }
        Time.advanceTime();
    }

    private List<Party> getParties() {
        return entities.stream()
                .filter(e -> e instanceof Party)
                .map(e -> (Party) e)
                .collect(Collectors.toList());
    }

    /**
     * Advanced Wiring: Links parties based on their capabilities (Roles).
     * This makes the system independent of class names or config strings.
     */
    private void linkSupplyChainByRoles(List<Party> parties) {
        List<Party> producers = new ArrayList<>();
        List<Party> processors = new ArrayList<>();
        List<Party> middlemen = new ArrayList<>(); // Distributors, Shops
        List<Party> consumers = new ArrayList<>();

        // 1. Classify parties by their Role
        for (Party p : parties) {
            if (hasRole(p, ProducerRole.class)) {
                producers.add(p);
            } else if (hasRole(p, ProcessorRole.class)) {
                processors.add(p);
            } else if (hasRole(p, ConsumerRole.class)) {
                consumers.add(p);
            } else if (hasRole(p, MerchantRole.class)) {
                // If it's a Merchant but NOT a producer/processor/consumer, it's a middleman (Distributor/Shop)
                middlemen.add(p);
            }
        }

        System.out.println("Role Analysis: Producers=%d, Processors=%d, Middlemen=%d, Consumers=%d".formatted(
                producers.size(), processors.size(), middlemen.size(), consumers.size()));

        // 2. Wiring Logic (Linear Chain)

        // A. Producers -> Processors (or Middlemen if no Processor)
        if (!processors.isEmpty()) {
            connectGroups(producers, processors);
        } else {
            connectGroups(producers, middlemen);
        }

        // B. Processors -> Middlemen (or Consumers if no Middleman)
        if (!processors.isEmpty()) {
            if (!middlemen.isEmpty()) {
                connectGroups(processors, middlemen);
            } else {
                connectGroups(processors, consumers);
            }
        }

        // C. Middlemen -> Consumers
        if (!middlemen.isEmpty()) {
            connectGroups(middlemen, consumers);
        }

        // Special Case: Middleman -> Middleman (e.g. Distributor -> Shop)
        // If we have multiple middlemen types, we might need smarter logic,
        // but for now, we assume one layer of middlemen or they are already sorted.
        if (middlemen.size() > 1) {
            // Optional: Chain the middlemen together (1 -> 2 -> 3)
            for (int i = 0; i < middlemen.size() - 1; i++) {
                connectOneToOne(middlemen.get(i), middlemen.get(i+1));
            }
        }
    }

    /**
     * Connects all sellers to available buyers in a Round-Robin fashion.
     */
    private void connectGroups(List<Party> sellers, List<Party> buyers) {
        if (sellers.isEmpty() || buyers.isEmpty()) return;

        int buyerIndex = 0;
        for (Party seller : sellers) {
            Party buyer = buyers.get(buyerIndex);

            // Set the target
            setMerchantTarget(seller, buyer);

            // Move to next buyer (distribute load)
            buyerIndex = (buyerIndex + 1) % buyers.size();
        }
    }

    private void connectOneToOne(Party seller, Party buyer) {
        setMerchantTarget(seller, buyer);
    }

    /**
     * Helper to set target in MerchantRole.
     */
    private void setMerchantTarget(Party seller, Party buyer) {
        getRole(seller, MerchantRole.class).ifPresentOrElse(
                role -> {
                    role.setTargetPartner(buyer);
                    System.out.println("LINKED (by Role): %s -> %s".formatted(seller.getName(), buyer.getName()));
                },
                () -> System.err.println("ERROR: %s is supposed to sell but has no MerchantRole!".formatted(seller.getName()))
        );
    }

    /**
     * Checks if a party has a specific role type.
     */
    private boolean hasRole(Party p, Class<? extends BusinessRole> roleClass) {
        return p.getRoles().stream().anyMatch(roleClass::isInstance);
    }

    /**
     * Retrieves a specific role instance.
     */
    private <T extends BusinessRole> Optional<T> getRole(Party p, Class<T> roleClass) {
        return p.getRoles().stream()
                .filter(roleClass::isInstance)
                .map(roleClass::cast)
                .findFirst();
    }

    private void generateReports() {
        System.out.println("\n=== GENERATING REPORTS ===");
        ReportGenerator.generateFoodChainReport(systemLogger.getHistory(), configName);
        ReportGenerator.generateStateReport(getParties(), configName);
        System.out.println("Reports generated in /reports folder.");
    }
}