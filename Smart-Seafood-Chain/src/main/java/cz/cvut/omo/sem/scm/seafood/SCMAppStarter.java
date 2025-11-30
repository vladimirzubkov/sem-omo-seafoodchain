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

        // 1. Get the configuration filename from CLI arguments (or null for default)
        String configFileName = args.length > 0 ? args[0] : null;

        try {
            System.out.println("Loading configuration...");

            // 2. Load Configuration (Satisfies NFRQ1)
            // YamlConfigLoader handles file parsing and fallback logic
            Configuration config = YamlConfigLoader.load(configFileName);

            System.out.println("Initializing simulation world...");

            // 3. Initialize Simulator with the loaded configuration (Dependency Injection)
            // The Simulator receives a ready-to-use config object
            Simulator simulator = new Simulator(config);

            // 4. Start the simulation loop
            simulator.start();

        } catch (Exception e) {
            System.err.println("CRITICAL ERROR: Could not start simulation.");
            System.err.println("Reason: %s".formatted(e.getMessage()));
            e.printStackTrace();
        }
    }
}