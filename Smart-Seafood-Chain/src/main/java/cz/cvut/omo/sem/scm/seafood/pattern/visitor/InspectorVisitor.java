package cz.cvut.omo.sem.scm.seafood.pattern.visitor;

import cz.cvut.omo.sem.scm.seafood.model.device.Device;
import cz.cvut.omo.sem.scm.seafood.model.employee.Employee;
import cz.cvut.omo.sem.scm.seafood.model.party.Party;

public class InspectorVisitor implements EntityVisitor {

    @Override
    public void visit(Device device) {
        double wear = device.getWearLevel(); // Assuming getter exists
        System.out.println("Inspector checking Device %s. Wear level: %.2f%%".formatted(device.getName(), wear * 100));

        if (wear > 0.8) {
            System.out.println("ALERT: Device %s needs immediate maintenance!".formatted(device.getId()));
            // Trigger Maintenance Event here
        }
    }

    @Override
    public void visit(Employee employee) {
        System.out.println("Inspector auditing Employee %s.".formatted(employee.getName()));
        // Check shift compliance or certifications
    }

    @Override
    public void visit(Party party) {
        System.out.println("Inspector auditing Party %s finances.".formatted(party.getName()));
        // Check blockchain integrity for this party
    }
}