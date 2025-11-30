package cz.cvut.omo.sem.scm.seafood.pattern.template;

import cz.cvut.omo.sem.scm.seafood.event.Event;
import cz.cvut.omo.sem.scm.seafood.report.ReportGenerator;
import java.time.LocalDateTime;
import java.util.List;

/**
 * TEMPLATE METHOD Pattern.
 * Defines the skeleton of the reporting algorithm.
 * Concrete subclasses implement specific formatting and filtering logic.
 */
public abstract class ReportTemplate {

    /**
     * The Template Method.
     * Defines the strict sequence of steps to generate any report.
     * This method is 'final' so subclasses cannot change the workflow.
     */
    public final void generate(List<Event> history) {
        StringBuilder sb = new StringBuilder();

        // Step 1: Standard Header
        sb.append("=== SMART SEAFOOD CHAIN REPORT ===\n");
        sb.append("Type: ").append(getReportName()).append("\n");
        sb.append("Generated: ").append(LocalDateTime.now()).append("\n");
        sb.append("----------------------------------\n\n");

        // Step 2: Specific Content (The Hook)
        String content = formatContent(history);
        sb.append(content);

        // Step 3: Standard Footer
        sb.append("\n----------------------------------\n");
        sb.append("End of Report.\n");

        // Step 4: Save to File (Delegated to Utility)
        // Removes spaces for filename: "Security Audit Report" -> "SecurityAuditReport.txt"
//        ReportGenerator.writeReport(getReportName().replace(" ", "") + ".txt", sb.toString());
    }

    // --- Abstract Steps to be implemented by specific reports ---

    protected abstract String getReportName();

    protected abstract String formatContent(List<Event> history);
}