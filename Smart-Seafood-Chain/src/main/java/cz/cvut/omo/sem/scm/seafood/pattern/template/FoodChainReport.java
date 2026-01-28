package cz.cvut.omo.sem.scm.seafood.pattern.template;

import cz.cvut.omo.sem.scm.seafood.event.Event;
import cz.cvut.omo.sem.scm.seafood.type.operation.EventType;
import java.util.List;
import java.util.stream.Collectors;

public class FoodChainReport extends ReportTemplate {

    @Override
    protected String getReportName() {
        return "FoodChainHistoryReport"; // Changed name to avoid confusion with Snapshot
    }

    @Override
    protected String formatContent(List<Event> history) {
        StringBuilder sb = new StringBuilder();

        // Filter events related to physical item movement
        List<Event> logistics = history.stream()
                .filter(e -> isLogisticsEvent(e.type()))
                .toList();

        if (logistics.isEmpty()) {
            return "No logistics data recorded.\n";
        }

        sb.append("Total Logistics Events: ").append(logistics.size()).append("\n\n");

        for (Event e : logistics) {
            sb.append(String.format("[%s] %-15s | %s\n",
                    e.timestamp().toLocalTime(),
                    e.type(),
                    e.description()));
        }

        return sb.toString();
    }

    private boolean isLogisticsEvent(EventType type) {
        return type == EventType.ITEM_CAUGHT
                || type == EventType.ITEM_PROCESSED
                || type == EventType.ITEM_TRANSFERRED
                || type == EventType.TRANSACTION_COMPLETED; // Sale is also movement
    }
}