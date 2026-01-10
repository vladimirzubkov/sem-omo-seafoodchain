package cz.cvut.omo.sem.scm.seafood.model.employee.role;

import cz.cvut.omo.sem.scm.seafood.model.employee.Employee;
import cz.cvut.omo.sem.scm.seafood.type.operation.EventType;
import cz.cvut.omo.sem.scm.seafood.type.role.LaborRoleType;

import java.util.Random;

public class InspectionRole implements JobRole {

    private final Random random = new Random();

    @Override
    public void work(Employee context) {
        // TODO: Access global entity list to use Visitor Pattern effectively.
        // Since Role doesn't have direct access to Simulator.entities here, we simulate the "Audit" process.

        // 1. Simulate Inspection activity probability
        if (random.nextDouble() < 0.1) { // 10% chance per tick to perform inspection

            // 2. Log the inspection
            System.out.println("[INSPECTOR] %s is conducting a random audit...".formatted(context.getName()));

            // 3. Report findings (Bonus FRQ18 - Integrity Check simulation)
            context.reportAction(EventType.INSPECTION_PERFORMED,
                    "Routine check of facility and blockchain records.", null);

            // In a full implementation:
            // entities.forEach(e -> e.accept(new InspectorVisitor()));
        }
    }

    @Override
    public LaborRoleType getRoleType() {
        return LaborRoleType.INSPECTION;
    }
}