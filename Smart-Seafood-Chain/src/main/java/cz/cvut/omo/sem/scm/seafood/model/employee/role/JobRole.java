package cz.cvut.omo.sem.scm.seafood.model.employee.role;

import cz.cvut.omo.sem.scm.seafood.model.employee.Employee;
import cz.cvut.omo.sem.scm.seafood.type.role.LaborRoleType;

public interface JobRole {
    void work(Employee context);

    // Returns the specific enum for Labor functions
    LaborRoleType getRoleType();
}