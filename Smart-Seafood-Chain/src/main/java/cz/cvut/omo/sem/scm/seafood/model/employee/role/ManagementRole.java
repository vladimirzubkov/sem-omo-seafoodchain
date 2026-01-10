package cz.cvut.omo.sem.scm.seafood.model.employee.role;

import cz.cvut.omo.sem.scm.seafood.model.employee.Employee;
import cz.cvut.omo.sem.scm.seafood.simulation.Time;
import cz.cvut.omo.sem.scm.seafood.type.operation.EventType;
import cz.cvut.omo.sem.scm.seafood.type.role.LaborRoleType;

public class ManagementRole implements JobRole {

    @Override
    public void work(Employee context) {
        // 1. Analyze Chain Efficiency (Periodic)
        // Check everyday at 8:00 AM
        if (Time.getCurrentHour() == 8) {
            System.out.println("[MANAGEMENT] %s is reviewing daily supply chain KPIs.".formatted(context.getName()));

            // 2. Fire Optimization Event
            context.reportAction(EventType.SCM_OPTIMIZATION,
                    "Adjusting logistics routes based on daily report.", null);
        }
    }

    @Override
    public LaborRoleType getRoleType() {
        return LaborRoleType.MANAGEMENT;
    }
}