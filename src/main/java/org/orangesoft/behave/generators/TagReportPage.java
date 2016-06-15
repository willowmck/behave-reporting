package org.orangesoft.behave.generators;

import org.orangesoft.behave.Configuration;
import org.orangesoft.behave.ReportResult;
import org.orangesoft.behave.json.support.TagObject;

public class TagReportPage extends AbstractPage {

    private final TagObject tagObject;

    public TagReportPage(ReportResult reportResult, Configuration configuration, TagObject tagObject) {
        super(reportResult, "tagReport.vm", configuration);
        this.tagObject = tagObject;
    }

    @Override
    public String getWebPage() {
        return tagObject.getReportFileName();
    }

    @Override
    public void prepareReport() {
        context.put("tag", tagObject);
    }

}
