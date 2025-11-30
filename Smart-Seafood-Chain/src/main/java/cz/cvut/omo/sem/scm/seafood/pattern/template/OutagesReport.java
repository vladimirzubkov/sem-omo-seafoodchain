package cz.cvut.omo.sem.scm.seafood.pattern.template;

import cz.cvut.omo.sem.scm.seafood.event.Event;
import java.util.List;

public class OutagesReport extends ReportTemplate {

    @Override
    protected String getReportName() {
        return "OutagesReport";
    }

    @Override
    protected String formatContent(List<Event> history) {
        // TODO: Calculate MTTR (Mean Time To Repair), downtime duration
        return "TODO: Breakdown statistics and repair waiting times.\n";
    }
}