package cz.cvut.omo.sem.scm.seafood.model.employee.role;

import cz.cvut.omo.sem.scm.seafood.model.employee.Employee;
import cz.cvut.omo.sem.scm.seafood.type.role.LaborRoleType;

public class ManualLaborRole implements JobRole {

    @Override
    public void work(Employee context) {
        // TODO: Implement manual labor logic
        // 1. Check assigned Production Line
        // 2. Consume energy/stamina
        // 3. Process items (Cook/Pack)
    }

    @Override
    public LaborRoleType getRoleType() {
        return LaborRoleType.MANUAL_LABOR;
    }
}