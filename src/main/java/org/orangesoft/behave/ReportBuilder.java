package org.orangesoft.behave;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import org.orangesoft.behave.generators.ErrorPage;
import org.orangesoft.behave.generators.FailuresOverviewPage;
import org.orangesoft.behave.generators.FeatureReportPage;
import org.orangesoft.behave.generators.FeaturesOverviewPage;
import org.orangesoft.behave.generators.StepsOverviewPage;
import org.orangesoft.behave.generators.TagReportPage;
import org.orangesoft.behave.generators.TagsOverviewPage;
import org.orangesoft.behave.json.Feature;
import org.orangesoft.behave.json.support.TagObject;

public class ReportBuilder {

    private static final Logger LOG = LogManager.getLogger(ReportBuilder.class);

    /**
     * Page that should be displayed when the reports is generated. Shared between {@link FeaturesOverviewPage} and
     * {@link ErrorPage}.
     */
    public static final String HOME_PAGE = "feature-overview.html";

    private ReportResult reportResult;
    private final ReportParser reportParser;

    private Configuration configuration;
    private List<String> jsonFiles;

    public ReportBuilder(List<String> jsonFiles, Configuration configuration) {
        this.jsonFiles = jsonFiles;
        this.configuration = configuration;
        reportParser = new ReportParser(configuration);
    }

    public void generateReports() {
        try {
            // first copy static resources so ErrorPage is displayed properly
            copyStaticResources();

            // create directory for embeddings before files are generated
            createEmbeddingsDirectory();

            List<Feature> features = reportParser.parseJsonResults(jsonFiles);
            reportResult = new ReportResult(features);

            generateAllPages();

            // whatever happens we want to provide at least error page instead of empty report
        } catch (Exception e) {
            generateErrorPage(e);
        }
    }

    /**
     * Checks if all features pass.
     * 
     * @return true if all feature pass otherwise false
     */
    public boolean hasBuildPassed() {
        return reportResult != null && reportResult.getAllFailedFeatures() == 0;
    }

    private void copyStaticResources() {
        copyResources("css", "reporting.css", "bootstrap.min.css", "font-awesome.min.css");
        copyResources("js", "jquery.min.js", "jquery.tablesorter.min.js", "bootstrap.min.js", "Chart.min.js");
        copyResources("fonts", "FontAwesome.otf", "fontawesome-webfont.svg", "fontawesome-webfont.woff",
                "fontawesome-webfont.eot", "fontawesome-webfont.ttf", "fontawesome-webfont.woff2",
                "glyphicons-halflings-regular.eot", "glyphicons-halflings-regular.eot",
                "glyphicons-halflings-regular.woff2", "glyphicons-halflings-regular.woff",
                "glyphicons-halflings-regular.ttf", "glyphicons-halflings-regular.svg");
    }

    public void createEmbeddingsDirectory() {
        configuration.getEmbeddingDirectory().mkdirs();
    }

    private void copyResources(String resourceLocation, String... resources) {
        for (String resource : resources) {
            File tempFile = new File(configuration.getReportDirectory().getAbsoluteFile(),
                    resourceLocation + File.separatorChar + resource);
            // don't change this implementation unless you verified it works on Jenkins
            try {
                FileUtils.copyInputStreamToFile(
                        this.getClass().getResourceAsStream("/" + resourceLocation + "/" + resource), tempFile);
            } catch (IOException e) {
                throw new ValidationException(e);
            }
        }
    }

    private void generateAllPages() {
        new FeaturesOverviewPage(reportResult, configuration).generatePage();
        for (Feature feature : reportResult.getAllFeatures()) {
            new FeatureReportPage(reportResult, configuration, feature).generatePage();
        }

        new TagsOverviewPage(reportResult, configuration).generatePage();
        for (TagObject tagObject : reportResult.getAllTags()) {
            new TagReportPage(reportResult, configuration, tagObject).generatePage();
        }

        new StepsOverviewPage(reportResult, configuration).generatePage();
        new FailuresOverviewPage(reportResult, configuration).generatePage();
    }

    private void generateErrorPage(Exception exception) {
        LOG.info(exception);
        ErrorPage errorPage = new ErrorPage(reportResult, configuration, exception, jsonFiles);
        errorPage.generatePage();
    }
}
