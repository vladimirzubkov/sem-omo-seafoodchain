package cz.cvut.omo.sem.scm.seafood.pattern.template;

import cz.cvut.omo.sem.scm.seafood.event.Event;
import java.util.List;

public class FoodChainReport extends ReportTemplate {

    @Override
    protected String getReportName() {
        return "FoodChainReport";
    }

    @Override
    protected String formatContent(List<Event> history) {
        // TODO: Filter events related to ITEM_TRANSFERRED, ITEM_PROCESSED
        return "TODO: List all items movements from catch to sale.\n";
    }
}