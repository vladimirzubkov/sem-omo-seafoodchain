package cz.cvut.omo.sem.scm.seafood.pattern.factory;

import cz.cvut.omo.sem.scm.seafood.config.Configuration;
import cz.cvut.omo.sem.scm.seafood.model.SimulationEntity;
import java.util.ArrayList;
import java.util.List;

public class SeafoodEntityFactory extends EntityFactory {

    @Override
    public List<SimulationEntity> createEntities(Configuration config) {
        List<SimulationEntity> entities = new ArrayList<>();

        System.out.println("Factory: Creating entities from config...");
        // Parsing logic would go here:
        // 1. Create Parties
        // 2. Create Employees and assign to Parties
        // 3. Create Devices and assign to ProductionLines

        return entities;
    }
}