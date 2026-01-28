package cz.cvut.omo.sem.scm.seafood.pattern.template;

import cz.cvut.omo.sem.scm.seafood.event.Event;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class PartiesReport extends ReportTemplate {

    @Override
    protected String getReportName() {
        return "PartiesReport";
    }

    @Override
    protected String formatContent(List<Event> history) {
        // Group events by SourceID (Party Name/ID) and count them
        Map<String, Long> activityMap = history.stream()
                .collect(Collectors.groupingBy(Event::sourceId, Collectors.counting()));

        StringBuilder sb = new StringBuilder();
        sb.append("PARTY ACTIVITY RANKING (Event Count):\n");
        sb.append("-----------------------------------\n");

        // Sort by activity (Desc)
        activityMap.entrySet().stream()
                .sorted((e1, e2) -> Long.compare(e2.getValue(), e1.getValue()))
                .forEach(entry -> sb.append(String.format(" - %-25s : %d events\n", entry.getKey(), entry.getValue())));

        return sb.toString();
    }
}