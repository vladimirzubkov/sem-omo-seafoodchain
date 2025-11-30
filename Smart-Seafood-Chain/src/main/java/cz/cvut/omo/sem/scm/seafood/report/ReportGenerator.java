package cz.cvut.omo.sem.scm.seafood.report;

import cz.cvut.omo.sem.scm.seafood.event.Event;
import cz.cvut.omo.sem.scm.seafood.type.operation.EventType;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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

    // --- Business logic (FRQ15) ---

    /**
     * FRQ15: FoodChainReport - Movement of goods.
     */
    public static void generateFoodChainReport(List<Event> history) {
        StringBuilder sb = new StringBuilder();
        sb.append("=== FOOD CHAIN REPORT ===\n");
        sb.append("Generated at: ").append(LocalDateTime.now()).append("\n\n");

        // Filter events regarding goods movement
        List<Event> chainEvents = history.stream()
                .filter(e -> isChainEvent(e.type()))
                .toList();

        for (Event e : chainEvents) {
            sb.append(String.format("[%s] %s: %s (Source: %s)\n",
                    e.timestamp(), e.type(), e.description(), e.sourceId()));
        }

        writeReport("FoodChainReport", sb.toString());
    }

    /**
     * FRQ15: OutagesReport - Breakdowns and Repairs.
     */
    public static void generateOutagesReport(List<Event> history) {
        StringBuilder sb = new StringBuilder();
        sb.append("=== OUTAGES & MAINTENANCE REPORT ===\n\n");

        List<Event> breakdowns = history.stream()
                .filter(e -> e.type() == EventType.DEVICE_BREAKDOWN)
                .toList();

        sb.append("Total Breakdowns: ").append(breakdowns.size()).append("\n");

        // Группировка по девайсам
        Map<String, Long> failuresByDevice = breakdowns.stream()
                .collect(Collectors.groupingBy(Event::sourceId, Collectors.counting()));

        failuresByDevice.forEach((id, count) ->
                sb.append(String.format("Device %s failed %d times.\n", id, count))
        );

        writeReport("OutagesReport", sb.toString());
    }

    /**
     * FRQ15: SecurityReport - Manipulations.
     */
    public static void generateSecurityReport(List<Event> history) {
        StringBuilder sb = new StringBuilder();
        sb.append("=== SECURITY AUDIT REPORT ===\n\n");

        long issues = history.stream()
                .filter(e -> e.type() == EventType.DOUBLE_SPENDING_DETECTED ||
                        e.type() == EventType.BLOCKCHAIN_TAMPERING_DETECTED)
                .peek(e -> sb.append(String.format("[ALERT] %s at %s. Details: %s\n",
                        e.type(), e.timestamp(), e.description())))
                .count();

        if (issues == 0) {
            sb.append("No security incidents detected. System integrity is 100%.\n");
        }

        writeReport("SecurityReport", sb.toString());
    }

    // --- Helper Methods ---

    private static boolean isChainEvent(EventType type) {
        return type == EventType.ITEM_CAUGHT ||
                type == EventType.ITEM_TRANSFERRED ||
                type == EventType.ITEM_SOLD;
    }

    private static void writeReport(String fileName, String content) {
        String timestampedName = "%s_%s.txt".formatted(LocalDateTime.now().format(FMT), fileName);
        try {
            Files.writeString(Paths.get(REPORT_DIR, timestampedName), content,
                    StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
            System.out.println("Report generated: " + timestampedName);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}