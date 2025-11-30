package cz.cvut.omo.sem.scm.seafood.pattern.template;

import cz.cvut.omo.sem.scm.seafood.event.Event;
import java.util.List;

public class ConsumptionReport extends ReportTemplate {

    @Override
    protected String getReportName() {
        return "FactoryConsumptionReport";
    }

    @Override
    protected String formatContent(List<Event> history) {
        // TODO: Sum up electricity and material usage from production lines
        return "TODO: Energy and Material consumption per Device/Line.\n";
    }
}