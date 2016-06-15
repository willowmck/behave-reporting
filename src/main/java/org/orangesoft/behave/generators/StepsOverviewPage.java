package org.orangesoft.behave.generators;

import org.orangesoft.behave.Configuration;
import org.orangesoft.behave.ReportResult;
import org.orangesoft.behave.json.support.StepObject;
import org.orangesoft.behave.util.Util;

/**
 * Presents details about how long steps are executed (adds the same steps and presents sum).
 * 
 * @author Damian Szczepanik (damianszczepanik@github)
 */
public class StepsOverviewPage extends AbstractPage {

    public StepsOverviewPage(ReportResult reportResult, Configuration configuration) {
        super(reportResult, "stepsOverview.vm", configuration);
    }

    @Override
    public String getWebPage() {
        return "step-overview.html";
    }

    @Override
    public void prepareReport() {
        context.put("all_steps", report.getAllSteps());

        int allOccurrences = 0;
        long allDurations = 0;
        for (StepObject stepObject : report.getAllSteps()) {
            allOccurrences += stepObject.getTotalOccurrences();
            allDurations += stepObject.getDurations();
        }
        context.put("all_occurrences", allOccurrences);
        context.put("all_durations", Util.formatDuration(allDurations));
        // make sure it does not divide by 0 - may happens if there is no step at all or all results have 0 ms durations
        long average = allDurations / (allOccurrences == 0 ? 1 : allOccurrences);
        context.put("all_average", Util.formatDuration(average));

    }
}
