package cz.cvut.omo.sem.scm.seafood.model.employee.role;

import cz.cvut.omo.sem.scm.seafood.model.employee.Employee;
import cz.cvut.omo.sem.scm.seafood.type.role.LaborRoleType;

public class ManagementRole implements JobRole {

    @Override
    public void work(Employee context) {
        // TODO: Implement management logic
        // 1. Analyze Chain Efficiency (Bonus FRQ19)
        // 2. Adjust KPIs
        // 3. Reconfigure Supply Chain (Strategic)
    }

    @Override
    public LaborRoleType getRoleType() {
        return LaborRoleType.MANAGEMENT;
    }
}