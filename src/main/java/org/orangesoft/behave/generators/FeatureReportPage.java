package org.orangesoft.behave.generators;

import org.orangesoft.behave.Configuration;
import org.orangesoft.behave.ReportResult;
import org.orangesoft.behave.json.Feature;

public class FeatureReportPage extends AbstractPage {

    private final Feature feature;

    public FeatureReportPage(ReportResult reportResult, Configuration configuration, Feature feature) {
        super(reportResult, "featureReport.vm", configuration);
        this.feature = feature;
    }

    @Override
    public String getWebPage() {
        return feature.getReportFileName();
    }

    @Override
    public void prepareReport() {
        context.put("parallel", configuration.isParallelTesting());
        context.put("feature", feature);
    }

}
