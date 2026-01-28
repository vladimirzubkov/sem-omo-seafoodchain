package cz.cvut.omo.sem.scm.seafood.pattern.factory;

import cz.cvut.omo.sem.scm.seafood.config.Configuration;
import cz.cvut.omo.sem.scm.seafood.model.SimulationEntity;
import java.util.List;

/**
 * Abstract Factory / Factory Method pattern.
 * Responsible for creating the simulation world from configuration.
 */
public abstract class EntityFactory {

    public abstract List<SimulationEntity> createEntities(Configuration config);

}