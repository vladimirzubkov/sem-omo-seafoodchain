package cz.cvut.omo.sem.scm.seafood;

import cz.cvut.omo.sem.scm.seafood.blockchain.Blockchain;
import cz.cvut.omo.sem.scm.seafood.config.Configuration;
import cz.cvut.omo.sem.scm.seafood.config.YamlConfigLoader;
import cz.cvut.omo.sem.scm.seafood.event.EventBus;
import cz.cvut.omo.sem.scm.seafood.simulation.SimulationController;

public class SCMAppStarter {

    public static void main(String[] args) {
        System.out.println("""
                ========================================
                   Smart Seafood Chain – Simulation
                   B6B36OMO – Semester Project 2025
                ========================================
                """);

        // 1. Get configuration filename
        // Default to the complex one for testing if no args provided
//        String configFileName = args.length > 0 ? args[0] : null;
        // Accept from Command Line OR default to "baltic-frozen.yaml"
        String configFileName = args.length > 0 ? args[0] : "baltic-frozen.yaml";
//        String configFileName = args.length > 0 ? args[0] : "russian-sea-dominator.yaml"; // default, a month of continuous deliberate hard work
//        String configFileName = "scm-test.yaml"; // 5 hours
//        String configFileName = "prague-sushi-luxury.yaml"; // 3 days
//        String configFileName = "nordic-fresh.yaml"; // one week

        try {
            System.out.println("Loading configuration: %s...".formatted(configFileName));

            Configuration config = YamlConfigLoader.load(configFileName);

            // --- Clear memory (Singletons) before start ---
            // This prevents old data from appearing in new reports
            EventBus.getInstance().reset();
            Blockchain.getInstance().reset();
            // -----------------------------------------------------

            System.out.println("Initializing simulation world...");

            // 2. Initialize SimulationController with config AND config name
            SimulationController simulationController = new SimulationController(config, configFileName);

            // 3. Start
            simulationController.start();

        } catch (Exception e) {
            System.err.println("Critical Error: %s".formatted(e.getMessage()));
            e.printStackTrace();
        }
    }
}