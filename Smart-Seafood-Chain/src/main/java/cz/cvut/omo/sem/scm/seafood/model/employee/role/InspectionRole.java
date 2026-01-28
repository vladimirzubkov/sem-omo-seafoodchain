package cz.cvut.omo.sem.scm.seafood.model.employee.role;

import cz.cvut.omo.sem.scm.seafood.model.device.Device;
import cz.cvut.omo.sem.scm.seafood.model.employee.Employee;
import cz.cvut.omo.sem.scm.seafood.model.party.Party;
import cz.cvut.omo.sem.scm.seafood.pattern.visitor.EntityVisitor;
import cz.cvut.omo.sem.scm.seafood.pattern.visitor.InspectorVisitor;
import cz.cvut.omo.sem.scm.seafood.type.operation.EventType;
import cz.cvut.omo.sem.scm.seafood.type.role.LaborRoleType;

import java.util.Random;

public class InspectionRole implements JobRole {

    private final Random random = new Random();

    @Override
    public void work(Employee context) {
        // 1. Determine chance of inspection (e.g., 5% probability per tick)
        if (random.nextDouble() > 0.05) {
            return; // Inspector is drinking coffee or doing paperwork
        }

        // 2. Identify the scope (The company this inspector works for)
        Party employer = context.getEmployer();
        if (employer == null) {
            // Freelance inspector or unassigned (should not happen in normal flow)
            return;
        }

        // 3. Prepare the Visitor (The "Checklist" logic)
        EntityVisitor inspectorVisitor = new InspectorVisitor();

        // 4. Log the start of the audit
        System.out.println("[INSPECTOR] %s started auditing facilities of %s...".formatted(
                context.getName(), employer.getName()));

        // 5. EXECUTE VISITOR PATTERN (Double Dispatch)
        // The Inspector visits the Company itself (finances check)
        employer.accept(inspectorVisitor);

        // The Inspector visits all devices owned by the Company (wear & tear check)
        // We assume Party has a getter for devices. If not, this loop requires Party modification.
        if (employer.getDevices() != null) {
            for (Device device : employer.getDevices()) {
                device.accept(inspectorVisitor);
            }
        }

        // The Inspector could also visit other employees (compliance check)
        if (employer.getEmployees() != null) {
            for (Employee colleague : employer.getEmployees()) {
                // Don't inspect yourself, it's a conflict of interest!
                if (!colleague.getId().equals(context.getId())) {
                    colleague.accept(inspectorVisitor);
                }
            }
        }

        // 6. Report completion
        context.reportAction(EventType.INSPECTION_PERFORMED,
                "Audit completed for %s".formatted(employer.getName()), null);
    }

    @Override
    public LaborRoleType getRoleType() {
        return LaborRoleType.INSPECTION;
    }
}