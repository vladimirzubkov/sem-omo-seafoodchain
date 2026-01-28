package cz.cvut.omo.sem.scm.seafood.pattern.template;

import cz.cvut.omo.sem.scm.seafood.event.Event;
import cz.cvut.omo.sem.scm.seafood.type.operation.EventType;
import java.util.List;

public class TransactionReport extends ReportTemplate {

    @Override
    protected String getReportName() {
        return "TransactionReport";
    }

    @Override
    protected String formatContent(List<Event> history) {
        StringBuilder sb = new StringBuilder();

        List<Event> transactions = history.stream()
                .filter(e -> e.type() == EventType.TRANSACTION_COMPLETED)
                .toList();

        sb.append("TOTAL TRANSACTIONS: ").append(transactions.size()).append("\n");
        sb.append("--------------------------------------------------\n");

        for (Event e : transactions) {
            // Description usually contains "Sold [ITEM] for [PRICE]"
            sb.append(String.format("[%s] %s\n", e.timestamp(), e.description()));
        }

        return sb.toString();
    }
}