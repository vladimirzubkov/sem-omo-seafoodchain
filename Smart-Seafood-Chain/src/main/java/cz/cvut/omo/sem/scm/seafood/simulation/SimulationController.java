package cz.cvut.omo.sem.scm.seafood.simulation;

import cz.cvut.omo.sem.scm.seafood.blockchain.Blockchain;
import cz.cvut.omo.sem.scm.seafood.config.Configuration;
import cz.cvut.omo.sem.scm.seafood.event.Event;
import cz.cvut.omo.sem.scm.seafood.event.EventBus;
import cz.cvut.omo.sem.scm.seafood.model.SimulationEntity;
import cz.cvut.omo.sem.scm.seafood.model.party.Party;
import cz.cvut.omo.sem.scm.seafood.model.party.role.*; // Import all roles
import cz.cvut.omo.sem.scm.seafood.pattern.factory.EntityFactory;
import cz.cvut.omo.sem.scm.seafood.pattern.factory.SeafoodEntityFactory;
import cz.cvut.omo.sem.scm.seafood.pattern.memento.Caretaker;
import cz.cvut.omo.sem.scm.seafood.pattern.memento.SimulationMemento;
import cz.cvut.omo.sem.scm.seafood.pattern.prototype.Prototype;
import cz.cvut.omo.sem.scm.seafood.pattern.visitor.Visitable;
import cz.cvut.omo.sem.scm.seafood.pattern.visitor.WorldSetupVisitor;
import cz.cvut.omo.sem.scm.seafood.type.operation.EventType;
import cz.cvut.omo.sem.scm.seafood.report.EventLogger;
import cz.cvut.omo.sem.scm.seafood.report.ReportGenerator;
import cz.cvut.omo.sem.scm.seafood.pattern.template.*;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class SimulationController {
    @Getter
    private final List<SimulationEntity> entities = new ArrayList<>();
    private int currentTick = 0;
    private final Configuration config;
    private final String configName;

    private final EventBus eventBus = EventBus.getInstance();
    @Getter
    private final Blockchain blockchain = Blockchain.getInstance();

    private final EventLogger systemLogger = new EventLogger();

    private final Caretaker caretaker = new Caretaker();

    public SimulationController(Configuration config, String configName) {
        this.config = config;
        this.configName = configName;

        // 1. Configure global Time
        Time.setTickDuration(config.getSimulation().getTickDurationMinutes());

        // 2. Create entities using Factory # PATTERN 01 Abstract Factory
        // uses contract (EntityFactory) and creates a family of related objects
        // (Seafood context)
        // simulator doesn't know how to parse config or create specific classes
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

        // Save initial state
        saveCheckpoint();

        while (currentTick < config.getSimulation().getMaxTicks()) {
            tick();

            // Artificial delay for visualization
            try {
                int delay = config.getSimulation().getTickDelayMs();
                if (delay > 0) {
                    Thread.sleep(delay);
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                System.err.println("Simulation interrupted");
                break;
            }

            // 1. Regular persistence (e.g., every 24 hours)
            if (currentTick % 24 == 0) {
                saveCheckpoint();
            }

            // 2. Scenario: Failure injection for Baltic configuration
            if (configName.contains("baltic") && currentTick == 100) {
                triggerEmergencyBreach();
            }

            // 3. Security Check: If breach detected, perform rollback
            if (isSecurityBreached()) {
                System.out.println("[AUDIT] Security violation detected! Rolling back to last stable state...");
                restoreCheckpoint();
            }
        }
        generateReports();
    }

    private void tick() {
        System.out.println("--------------------------------------------------");
        currentTick++;
        if (currentTick % 24 == 0) {
            System.out.println("--- Day %d ---".formatted(currentTick / 24));
        }
        for (SimulationEntity entity : entities) {
            entity.handleTick();
        }
        Time.advanceTime();
    }

    private void triggerEmergencyBreach() {
        System.out.println("[SCENARIO] Simulating double-spending attack in Baltic Frozen Chain...");

        // The Event record requires 6 arguments:
        // timestamp, type, sourceId, targetId, description, payload
        eventBus.publish(new Event(
                null, // timestamp (will be set to now() in constructor)
                EventType.SECURITY_BREACH, // type
                "MaliciousActor", // sourceId
                "System", // targetId
                "Double spending attempt detected.", // description
                null // payload
        ));
    }

    private boolean isSecurityBreached() {
        // Audit history for any recent security incidents
        // Note: Record components are accessed via name() method, e.g., e.type()
        return systemLogger.getHistory().stream()
                .anyMatch(e -> e.type() == EventType.SECURITY_BREACH &&
                        e.timestamp().isAfter(Time.getCurrentTime().minusHours(1)));
    }

    // --- MEMENTO: CREATE SNAPSHOT ---
    public void saveCheckpoint() {
        List<SimulationEntity> deepCopyList = new ArrayList<>();

        for (SimulationEntity entity : this.entities) {
            if (entity instanceof Prototype<?>) {
                // If it implements our Prototype pattern, we clone it safely
                deepCopyList.add((SimulationEntity) ((Prototype<?>) entity).clone());
            } else {
                // Fallback: If not cloneable, we keep the reference but log a warning.
                // Or you can decide to ignore non-cloneable entities in restore.
                System.out.println(
                        "Warning: Entity %s is not a Prototype, saving reference.".formatted(entity.getName()));
                deepCopyList.add(entity);
            }
        }

        SimulationMemento memento = new SimulationMemento(
                this.currentTick,
                Time.getCurrentTime(), // Assuming Time has a getter, or capture distinct value
                deepCopyList);

        caretaker.save(memento);
        System.out.println("[MEMENTO] Checkpoint saved at tick %d".formatted(currentTick));
    }

    // --- MEMENTO: RESTORE SNAPSHOT ---
    public void restoreCheckpoint() {
        SimulationMemento memento = caretaker.undo();
        if (memento == null) {
            System.out.println("[MEMENTO] No checkpoint found to restore.");
            return;
        }

        // 1. Restore Simulation State
        this.currentTick = memento.tick();
        this.entities.clear();
        // We probably want to clone again on restore to allow re-restoring the same
        // memento later
        // Or simply assign if we treat memento as read-only source.
        this.entities.addAll(memento.entitiesSnapshot());

        // 2. Restore Global Time (You might need to add a setter in Time class)
        // Time.setCurrentTime(memento.timestamp());

        System.out.println("[MEMENTO] World restored to tick %d".formatted(this.currentTick));

        // 3. Clear/Reset Singleton states if necessary (EventBus, Blockchain)
        // Note: Blockchain is usually append-only. Restoring simulation state might
        // create
        // a desync with Blockchain history. This is a valid "feature" discussion for
        // defense.
        // Ideally, you would mark the Blockchain with a "ROLLBACK" event.
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
            if (hasRole(p, ProducerRole.class) || hasRole(p, ImporterRole.class)) {
                producers.add(p);
            } else if (hasRole(p, ProcessorRole.class)) {
                processors.add(p);
            } else if (hasRole(p, ConsumerRole.class)) {
                consumers.add(p);
            } else if (hasRole(p, MerchantRole.class)) {
                // If it's a Merchant but NOT a producer/processor/consumer, it's a middleman
                // (Distributor/Shop)
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
                connectOneToOne(middlemen.get(i), middlemen.get(i + 1));
            }
        }
    }

    /**
     * Connects all sellers to available buyers in a Round-Robin fashion.
     */
    private void connectGroups(List<Party> sellers, List<Party> buyers) {
        if (sellers.isEmpty() || buyers.isEmpty())
            return;

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
                () -> System.err
                        .println("ERROR: %s is supposed to sell but has no MerchantRole!".formatted(seller.getName())));
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

        // Filter only 'Party' instances from the general entities list
        List<Party> partiesOnly = entities.stream()
                .filter(e -> e instanceof Party)
                .map(e -> (Party) e)
                .toList();

        // 1. Snapshot Report (Visitor Pattern) - uses FoodChainSnapshot internally
        ReportGenerator.generateStateReport(partiesOnly, configName);

        // 2. Event History Report (Legacy Log)
        ReportGenerator.generateFoodChainReport(systemLogger.getHistory(), configName);

        // 3. Analytical Pattern Reports (Template Method)
        List<ReportTemplate> analyticalReports = List.of(
                new SecurityReport(),
                new FoodChainReport(),
                new TransactionReport(),
                new ConsumptionReport(),
                new OutagesReport(),
                new PartiesReport());

        // Retrieve event history once
        var history = systemLogger.getHistory();

        // Generate analytics
        for (ReportTemplate report : analyticalReports) {
            report.setConfigName(this.configName);
            report.generate(history);
            System.out.println(" -> Generated analysis: " + report.getClass().getSimpleName());
        }

        System.out.println("Reports generated in /reports folder.");
    }
}