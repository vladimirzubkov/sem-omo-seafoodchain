package cz.cvut.omo.sem.scm.seafood.pattern.template;

import cz.cvut.omo.sem.scm.seafood.event.Event;
import cz.cvut.omo.sem.scm.seafood.type.operation.EventType;
import java.util.List;

public class SecurityReport extends ReportTemplate {

    @Override
    protected String getReportName() {
        return "SecurityReport";
    }

    @Override
    protected String formatContent(List<Event> history) {
        long violations = history.stream()
                .filter(e -> e.type() == EventType.DOUBLE_SPENDING_DETECTED
                        || e.type() == EventType.BLOCKCHAIN_TAMPERING_DETECTED)
                .count();

        if (violations == 0) {
            return "System Integrity: OK. No incidents detected.\n";
        }

        // TODO: List details of each violation
        return "WARNING: Detected " + violations + " security incidents!\n";
    }
}