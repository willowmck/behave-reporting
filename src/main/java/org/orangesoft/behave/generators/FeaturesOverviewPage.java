package org.orangesoft.behave.generators;

import org.orangesoft.behave.Configuration;
import org.orangesoft.behave.ReportBuilder;
import org.orangesoft.behave.ReportResult;

public class FeaturesOverviewPage extends AbstractPage {

    public FeaturesOverviewPage(ReportResult reportResult, Configuration configuration) {
        super(reportResult, "featuresOverview.vm", configuration);
    }

    @Override
    public String getWebPage() {
        return ReportBuilder.HOME_PAGE;
    }

    @Override
    public void prepareReport() {
        context.put("all_features", report.getAllFeatures());
        context.put("report_summary", report.getFeatureReport());
        context.put("all_features_passed", report.getAllPassedFeatures());
        context.put("all_features_failed", report.getAllFailedFeatures());

        context.put("parallel", configuration.isParallelTesting());
    }
}
