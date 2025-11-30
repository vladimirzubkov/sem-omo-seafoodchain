package cz.cvut.omo.sem.scm.seafood.pattern.visitor;

/**
 * Interface for elements that can accept a visitor.
 * Must be implemented by SimulationEntity (or specific subclasses).
 */
public interface Visitable {
    void accept(EntityVisitor visitor);
}