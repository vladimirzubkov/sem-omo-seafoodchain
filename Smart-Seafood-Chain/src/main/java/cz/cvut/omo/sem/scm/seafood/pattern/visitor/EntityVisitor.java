package cz.cvut.omo.sem.scm.seafood.pattern.visitor;

import cz.cvut.omo.sem.scm.seafood.model.device.Device;
import cz.cvut.omo.sem.scm.seafood.model.employee.Employee;
import cz.cvut.omo.sem.scm.seafood.model.party.Party;

/**
 * Visitor Pattern Interface.
 * Allows the Inspector to perform operations on different simulation entities
 * without modifying their classes.
 */
public interface EntityVisitor {

    void visit(Device device);

    void visit(Employee employee);

    void visit(Party party);
}