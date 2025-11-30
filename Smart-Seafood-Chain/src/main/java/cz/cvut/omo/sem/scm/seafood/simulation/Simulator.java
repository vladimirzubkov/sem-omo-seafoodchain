package cz.cvut.omo.sem.scm.seafood.simulation;

import cz.cvut.omo.sem.scm.seafood.config.Configuration;
import cz.cvut.omo.sem.scm.seafood.event.Event;
import cz.cvut.omo.sem.scm.seafood.event.EventBus;
import cz.cvut.omo.sem.scm.seafood.model.SimulationEntity;
import cz.cvut.omo.sem.scm.seafood.pattern.template.*;
import cz.cvut.omo.sem.scm.seafood.report.EventLogger;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

public class Simulator {
    @Getter
    private final List<SimulationEntity> entities = new ArrayList<>();
    private int currentTick = 0;
    private final Configuration config;

    private final EventBus eventBus = new EventBus();
    private final EventLogger systemLogger = new EventLogger();

    public Simulator(Configuration config) {
        this.config = config;
        // TODO: Factory method to convert Config -> Entities with Roles

        // 1. Configure the Time step globally
        Time.setTickDuration(config.getSimulation().getTickDurationMinutes());

        initWorld();
    }

    private void initWorld() {
        // Example of populating world (Logic moved from YAML loader here or Factory)
        // entities.add(partyFactory.createFromConfig(...));
    }

    public void start() {
        while (currentTick < config.getSimulation().getMaxTicks()) {
            tick();
        }
    }

    private void tick() {
        currentTick++;
        System.out.println("--- Tick %d ---".formatted(currentTick));

        // Fixes: God Class antipattern.
        // Instead of calling updateDevices(), handleRepairs(), etc.
        // We just let every entity do its job.
        for (SimulationEntity entity : entities) {
            entity.handleTick();
        }

        // Global events (Observer) can be processed here

        // 2. Advance Time
        Time.advanceTime(); // Now respects the configured minutes

        // 3. (Optional) Visual Delay for better UX
        applyRealTimeDelay();
    }

    private void applyRealTimeDelay() {
        int delay = config.getSimulation().getTickDelayMs();
        if (delay > 0) {
            try {
                Thread.sleep(delay);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }

    private void generateReports() {
        System.out.println("Generating reports...");
        List<Event> events = systemLogger.getHistory();

        List<ReportTemplate> reports = List.of(
                new FoodChainReport(),
                new PartiesReport(),
                new ConsumptionReport(),
                new SecurityReport(),
                new TransactionReport(),
                new OutagesReport()
        );

        for (ReportTemplate report : reports) {
            report.generate(events);
        }
    }
}