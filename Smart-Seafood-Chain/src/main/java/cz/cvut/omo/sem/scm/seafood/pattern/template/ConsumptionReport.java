package cz.cvut.omo.sem.scm.seafood.pattern.template;

import cz.cvut.omo.sem.scm.seafood.event.Event;
import cz.cvut.omo.sem.scm.seafood.type.operation.EventType;
import java.util.List;

public class ConsumptionReport extends ReportTemplate {

    @Override
    protected String getReportName() {
        return "FactoryConsumptionReport";
    }

    @Override
    protected String formatContent(List<Event> history) {
        // 1. Counting SUCCESSFUL transformations
        long successCount = history.stream()
                .filter(e -> e.type() == EventType.ITEM_PROCESSED)
                .count();

        // 2. Counting FAILURES (using our new event type)
        long failureCount = history.stream()
                .filter(e -> e.type() == EventType.PRODUCTION_FAILED)
                .count();

        StringBuilder sb = new StringBuilder();
        sb.append("PRODUCTION PERFORMANCE:\n");
        sb.append("----------------------------------\n");
        sb.append("Successfully Processed: ").append(successCount).append("\n");
        sb.append("Failed Attempts:        ").append(failureCount).append("\n");

        // Efficiency metric
        double rate = (successCount + failureCount == 0) ? 0 : (double) successCount / (successCount + failureCount) * 100;
        sb.append("Production Efficiency:  ").append(String.format("%.2f%%", rate)).append("\n");

        sb.append("\n--- DETAILED PRODUCTION LOG ---\n");

        // 3. Showing exactly WHAT happened
        history.stream()
                .filter(e -> e.type() == EventType.ITEM_PROCESSED || e.type() == EventType.PRODUCTION_FAILED)
                .forEach(e -> {
                    String status = (e.type() == EventType.ITEM_PROCESSED) ? "[SUCCESS]" : "[FAILED ]";
                    sb.append(String.format("%s [%s] %s\n", status, e.timestamp(), e.description()));
                });

        return sb.toString();
    }
}