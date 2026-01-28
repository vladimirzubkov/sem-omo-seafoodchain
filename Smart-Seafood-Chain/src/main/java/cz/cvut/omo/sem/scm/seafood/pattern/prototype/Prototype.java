package cz.cvut.omo.sem.scm.seafood.pattern.prototype;

/**
 * Interface for the Prototype pattern.
 * Allows creating deep copies of simulation entities.
 */
public interface Prototype<T> extends Cloneable {
    T clone();
}