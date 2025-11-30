package cz.cvut.omo.sem.scm.seafood.model.production;

import cz.cvut.omo.sem.scm.seafood.model.device.Device;
import cz.cvut.omo.sem.scm.seafood.model.employee.Employee;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class ProductionLine {
    private String lineName;
    private List<Device> currentConfiguration = new ArrayList<>();
    private List<Employee> assignedEmployees = new ArrayList<>();
}