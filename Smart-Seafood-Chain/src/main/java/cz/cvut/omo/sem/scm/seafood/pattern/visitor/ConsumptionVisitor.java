package cz.cvut.omo.sem.scm.seafood.pattern.visitor;

import cz.cvut.omo.sem.scm.seafood.model.device.Device;
import cz.cvut.omo.sem.scm.seafood.model.employee.Employee;
import cz.cvut.omo.sem.scm.seafood.model.party.Party;
import lombok.Getter;

/**
 * Visitor implementation for FRQ15 (Reports).
 * Aggregates total energy consumption across all devices in the simulation.
 */
public class ConsumptionVisitor implements EntityVisitor {

    @Getter
    private double totalKWh = 0.0;

    @Getter
    private double totalCostEstimate = 0.0; // Estimated cost (e.g. 5 CZK per kWh)

    @Override
    public void visit(Device device) {
        // Double Dispatch allows us to access Device-specific methods safely here
        double consumed = device.getTotalEnergyConsumed();

        totalKWh += consumed;
        totalCostEstimate += (consumed * 5.0); // Mock cost calculation

        // Optional: Detailed log for audit
        // if (consumed > 0) {
        //     System.out.println("Audit: Device %s consumed %.2f kWh".formatted(device.getName(), consumed));
        // }
    }

    @Override
    public void visit(Employee employee) {
        // Employees do not consume electricity (ignored)
    }

    @Override
    public void visit(Party party) {
        // Parties acts as containers, the devices inside them are visited separately
        // via the main entity list in the SimulationController.
    }
}