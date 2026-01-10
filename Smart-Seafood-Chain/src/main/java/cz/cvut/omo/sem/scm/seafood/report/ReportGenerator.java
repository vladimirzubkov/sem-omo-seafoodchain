package cz.cvut.omo.sem.scm.seafood.report;

import cz.cvut.omo.sem.scm.seafood.event.Event;
import cz.cvut.omo.sem.scm.seafood.model.party.Party;
// Ensure we use the Visitor implementation from THIS package
import cz.cvut.omo.sem.scm.seafood.report.FoodChainReport;

import java.io.IOException;
import java.nio.file.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class ReportGenerator {

    private static final String REPORT_DIR = "reports";
    private static final DateTimeFormatter FMT = DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss");

    static {
        try {
            Files.createDirectories(Paths.get(REPORT_DIR));
        } catch (IOException e) {
            throw new RuntimeException("Cannot create reports directory", e);
        }
    }

    /**
     * Generates a historical report based on events.
     */
    public static void generateFoodChainReport(List<Event> history, String configName) {
        StringBuilder sb = new StringBuilder();
        sb.append("=== EVENT HISTORY REPORT ===\n");
        sb.append("Configuration: %s\n".formatted(configName));
        sb.append("Generated at: %s\n\n".formatted(LocalDateTime.now()));

        for (Event e : history) {
            sb.append("[%s] %s | %s\n".formatted(e.timestamp(), e.type(), e.description()));
        }

        writeReport("EventLog", sb.toString());
    }

    /**
     * Generates a snapshot of the current state using Visitor Pattern.
     */
    public static void generateStateReport(List<Party> parties, String configName) {
        StringBuilder sb = new StringBuilder();
        sb.append("=== FINAL STATE & INVENTORY REPORT ===\n");
        sb.append("Configuration: %s\n".formatted(configName));
        sb.append("Generated at: %s\n\n".formatted(LocalDateTime.now()));

        // Instantiate the Visitor
        FoodChainReport visitor = new FoodChainReport();

        // Visit each party
        for (Party party : parties) {
            String partyReport = visitor.visit(party);
            sb.append(partyReport).append("\n");

            // Audit blockchain if present
            if (party.getBlockchain() != null) {
                sb.append(visitor.generateBlockchainReport(party.getBlockchain()));
                sb.append("\n--------------------------------------------------\n");
            }
        }

        writeReport("StateReport", sb.toString());
    }

    private static void writeReport(String fileName, String content) {
        String timestampedName = "%s_%s.txt".formatted(LocalDateTime.now().format(FMT), fileName);
        try {
            Files.writeString(Paths.get(REPORT_DIR, timestampedName), content,
                    StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
            System.out.println("Report generated: %s".formatted(timestampedName));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}