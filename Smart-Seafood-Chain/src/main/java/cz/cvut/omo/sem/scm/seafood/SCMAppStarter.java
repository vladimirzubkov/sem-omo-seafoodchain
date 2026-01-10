package cz.cvut.omo.sem.scm.seafood;

import cz.cvut.omo.sem.scm.seafood.config.Configuration;
import cz.cvut.omo.sem.scm.seafood.config.YamlConfigLoader;
import cz.cvut.omo.sem.scm.seafood.simulation.Simulator;

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
        String configFileName = args.length > 0 ? args[0] : "russian-sea-dominator.yaml"; // default, a month of continuous deliberate hard work
//        String configFileName = "scm-test.yaml"; // 5 hours
//        String configFileName = "prague-sushi-luxury.yaml"; // 3 days
//        String configFileName = "nordic-fresh.yaml"; // one week
//        String configFileName = "baltic-frozen.yaml"; // two weeks stress test

        try {
            System.out.println("Loading configuration: %s...".formatted(configFileName));

            Configuration config = YamlConfigLoader.load(configFileName);

            System.out.println("Initializing simulation world...");

            // 2. Initialize Simulator with config AND config name
            Simulator simulator = new Simulator(config, configFileName);

            // 3. Start
            simulator.start();

        } catch (Exception e) {
            System.err.println("Critical Error: %s".formatted(e.getMessage()));
            e.printStackTrace();
        }
    }
}