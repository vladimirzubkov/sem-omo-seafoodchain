package cz.cvut.omo.sem.scm.seafood.pattern.visitor;

import cz.cvut.omo.sem.scm.seafood.blockchain.Blockchain;
import cz.cvut.omo.sem.scm.seafood.event.EventBus;
import cz.cvut.omo.sem.scm.seafood.model.device.Device;
import cz.cvut.omo.sem.scm.seafood.model.employee.Employee;
import cz.cvut.omo.sem.scm.seafood.model.party.Party;
import cz.cvut.omo.sem.scm.seafood.type.operation.EventType;

/**
 * Visitor implementation solely for Dependency Injection / Initialization.
 * Replaces instanceof checks in Simulator.
 */
public class WorldSetupVisitor implements EntityVisitor {

    private final EventBus eventBus;
    private final Blockchain blockchain;

    public WorldSetupVisitor(EventBus eventBus, Blockchain blockchain) {
        this.eventBus = eventBus;
        this.blockchain = blockchain;
    }

    @Override
    public void visit(Party party) {
        // Party needs both EventBus and Blockchain
        party.setEventBus(eventBus);
        party.setBlockchain(blockchain);
    }

    @Override
    public void visit(Device device) {
        // Devices need EventBus to report breakdowns
        device.setEventBus(eventBus);
    }

    @Override
    public void visit(Employee employee) {
        // Employees need EventBus to report shift starts/ends
        employee.setEventBus(eventBus);

        // Bonus: Auto-subscribe employee to relevant events here if needed
        // eventBus.subscribe(EventType.DEVICE_BREAKDOWN, employee);
    }
}