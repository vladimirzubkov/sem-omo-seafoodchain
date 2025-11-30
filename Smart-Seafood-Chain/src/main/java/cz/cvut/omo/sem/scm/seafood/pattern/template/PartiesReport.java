package cz.cvut.omo.sem.scm.seafood.pattern.template;

import cz.cvut.omo.sem.scm.seafood.event.Event;
import java.util.List;

public class PartiesReport extends ReportTemplate {

    @Override
    protected String getReportName() {
        return "PartiesReport";
    }

    @Override
    protected String formatContent(List<Event> history) {
        // TODO: Calculate margins, delays, and channel shares for each Party
        return "TODO: Statistics for Fisher, Processor, Distributor...\n";
    }
}