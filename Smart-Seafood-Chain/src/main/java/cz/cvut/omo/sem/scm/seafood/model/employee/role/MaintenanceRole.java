package cz.cvut.omo.sem.scm.seafood.model.employee.role;

import cz.cvut.omo.sem.scm.seafood.model.employee.Employee;
import cz.cvut.omo.sem.scm.seafood.type.role.LaborRoleType;

public class MaintenanceRole implements JobRole {

    @Override
    public void work(Employee context) {
        // TODO: Implement maintenance logic
        // 1. Check for open Repair Tickets
        // 2. Move to broken machine
        // 3. Perform repair (block for N ticks)
    }

    @Override
    public LaborRoleType getRoleType() {
        return LaborRoleType.MAINTENANCE;
    }
}