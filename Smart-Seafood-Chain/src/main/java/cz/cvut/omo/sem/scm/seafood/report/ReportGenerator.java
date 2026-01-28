package cz.cvut.omo.sem.scm.seafood.report;

import cz.cvut.omo.sem.scm.seafood.event.Event;
import cz.cvut.omo.sem.scm.seafood.model.party.Party;
// Ensure we use the Visitor implementation from THIS package

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
            // Truncate timestamp to 23 chars (approx 5 decimal places for seconds)
            String timeStr = e.timestamp().toString();
            if (timeStr.length() > 23) {
                timeStr = timeStr.substring(0, 23);
            }

            // Determine semantic tag based on EventType
            String tag = switch (e.type()) {
                case ITEM_CAUGHT -> "[PRODUCER]";
                case DELIVERY_ARRIVED -> "[IMPORTER]";
                case ITEM_PROCESSED, ITEM_COOKED, ITEM_PACKAGED -> "[PROCESSOR]";
                case TRANSACTION_COMPLETED, ORDER_PLACED -> "[MERCHANT]";
                case ITEM_SOLD -> "[CUSTOMER]";
                case SHIFT_STARTED, SHIFT_ENDED, MAINTENANCE_PERFORMED, DEVICE_REPAIR_STARTED, DEVICE_REPAIRED ->
                    "[WORKER]";
                case DEVICE_BREAKDOWN -> "[DEVICE]";
                case SECURITY_BREACH, DOUBLE_SPENDING_DETECTED, BLOCKCHAIN_TAMPERING_DETECTED -> "[SECURITY]";
                default -> "[LOG]";
            };

            sb.append("[%s] %-12s %s | %s\n".formatted(timeStr, tag, e.type(), e.description()));
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
        FoodChainSnapshot visitor = new FoodChainSnapshot();

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

    public static void writeReport(String fileName, String content) {
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