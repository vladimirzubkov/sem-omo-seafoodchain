package cz.cvut.omo.sem.scm.seafood.model.employee.role;

import cz.cvut.omo.sem.scm.seafood.model.employee.Employee;
import cz.cvut.omo.sem.scm.seafood.type.operation.EventType;
import cz.cvut.omo.sem.scm.seafood.type.role.LaborRoleType;

public class ManualLaborRole implements JobRole {

    @Override
    public void work(Employee context) {
        // 1. Simulate routine work
        // Real implementation would check context.getAssignedProductionLine()

        // 2. Consume stamina/energy (mock logic)
        // If we tracked energy, we would decrease it here.

        // 3. Occasionally report status
        // Only report sometimes to avoid spamming the console
        if (Math.random() < 0.05) {
            context.reportAction(EventType.ITEM_PROCESSED,
                    "Worker manual task completed", null);
            System.out.println("[MANUAL LABOR] %s is processing items on the line.".formatted(context.getName()));
        }
    }

    @Override
    public LaborRoleType getRoleType() {
        return LaborRoleType.MANUAL_LABOR;
    }
}