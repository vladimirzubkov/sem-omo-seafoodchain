package cz.cvut.omo.sem.scm.seafood.pattern.template;

import cz.cvut.omo.sem.scm.seafood.event.Event;
import java.util.List;

public class TransactionReport extends ReportTemplate {

    @Override
    protected String getReportName() {
        return "TransactionReport";
    }

    @Override
    protected String formatContent(List<Event> history) {
        // TODO: List all financial transactions and inventory states per tick
        return "TODO: Financial overview and stock levels.\n";
    }
}