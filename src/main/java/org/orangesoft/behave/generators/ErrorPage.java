package org.orangesoft.behave.generators;

import java.util.List;

import org.apache.commons.lang3.exception.ExceptionUtils;

import org.orangesoft.behave.Configuration;
import org.orangesoft.behave.ReportBuilder;
import org.orangesoft.behave.ReportResult;

public class ErrorPage extends AbstractPage {

    private final Exception exception;
    private final List<String> jsonFiles;

    public ErrorPage(ReportResult reportResult, Configuration configuration, Exception exception,
            List<String> jsonFiles) {
        super(reportResult, "errorPage.vm", configuration);
        this.exception = exception;
        this.jsonFiles = jsonFiles;
    }

    @Override
    public String getWebPage() {
        return ReportBuilder.HOME_PAGE;
    }

    @Override
    public void prepareReport() {
        context.put("output_message", ExceptionUtils.getStackTrace(exception));
        context.put("json_files", jsonFiles);
    }
}
