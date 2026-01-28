package cz.cvut.omo.sem.scm.seafood.pattern.template;

import cz.cvut.omo.sem.scm.seafood.event.Event;
import cz.cvut.omo.sem.scm.seafood.type.operation.EventType;
import java.util.List;

public class OutagesReport extends ReportTemplate {

    @Override
    protected String getReportName() {
        return "OutagesReport";
    }

    @Override
    protected String formatContent(List<Event> history) {
        long breakdowns = history.stream()
                .filter(e -> e.type() == EventType.DEVICE_BREAKDOWN)
                .count();

        long repairs = history.stream()
                .filter(e -> e.type() == EventType.MAINTENANCE_PERFORMED)
                .count();

        StringBuilder sb = new StringBuilder();
        sb.append("SUMMARY STATISTICS:\n");
        sb.append("  Total Breakdowns: ").append(breakdowns).append("\n");
        sb.append("  Repairs Performed: ").append(repairs).append("\n\n");
        sb.append("--- INCIDENT LOG ---\n");

        history.stream()
                .filter(e -> e.type() == EventType.DEVICE_BREAKDOWN || e.type() == EventType.MAINTENANCE_PERFORMED)
                .forEach(e -> sb.append(String.format("[%s] %-20s | %s\n",
                        e.timestamp(), e.type(), e.description())));

        return sb.toString();
    }
}