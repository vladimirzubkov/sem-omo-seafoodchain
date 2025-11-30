package cz.cvut.omo.sem.scm.seafood.model.employee.role;

import cz.cvut.omo.sem.scm.seafood.model.employee.Employee;
import cz.cvut.omo.sem.scm.seafood.type.role.LaborRoleType;

public class InspectionRole implements JobRole {

    @Override
    public void work(Employee context) {
        // TODO: Implement inspection logic (Visitor Pattern)
        // 1. Visit random Party
        // 2. Check Blockchain integrity (Bonus FRQ18)
        // 3. Check sensor anomalies
    }

    @Override
    public LaborRoleType getRoleType() {
        return LaborRoleType.INSPECTION;
    }
}