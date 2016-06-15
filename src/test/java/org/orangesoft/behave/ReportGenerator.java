package org.orangesoft.behave;

import org.orangesoft.behave.ValidationException;
import org.orangesoft.behave.ReportResult;
import org.orangesoft.behave.Configuration;
import org.orangesoft.behave.ReportParser;
import java.io.File;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.orangesoft.behave.json.Feature;
import org.orangesoft.behave.json.support.StepObject;
import org.orangesoft.behave.json.support.TagObject;

/**
 * @author Damian Szczepanik (damianszczepanik@github)
 */
public abstract class ReportGenerator {

    private final static String JSON_DIRECTORY = "json/";

    protected static final String SAMPLE_JSON = "sample.json";
    protected static final String SIMPLE_JSON = "simple.json";
    protected static final String EMPTY_JSON = "empty.json";
    protected static final String INVALID_JSON = "invalid.json";
    protected static final String INVALID_REPORT_JSON = "invalid-report.json";

    private final File reportDirectory;

    protected Configuration configuration;
    private String projectName = "test cucumberProject";
    protected final List<String> jsonReports = new ArrayList<>();
    protected ReportResult reportResult;

    protected List<Feature> features;
    protected List<TagObject> tags;
    protected List<StepObject> steps;

    public ReportGenerator() {
        try {
            // points to target/test-classes output
            reportDirectory = new File(ReportGenerator.class.getClassLoader().getResource("").toURI());
        } catch (URISyntaxException e) {
            throw new ValidationException(e);
        }
    }

    protected void setUpWithJson(String... jsonFiles) {
        for (String jsonFile : jsonFiles)
            addReport(jsonFile);

        configuration = new Configuration(reportDirectory, projectName);

        createEmbeddingsDirectory();
        createReportBuilder();
    }

    private void addReport(String jsonReport) {
        try {
            URL path = ReportGenerator.class.getClassLoader().getResource(JSON_DIRECTORY + jsonReport);
            jsonReports.add(new File(path.toURI()).getAbsolutePath());
        } catch (URISyntaxException e) {
            throw new ValidationException(e);
        }
    }

    private void createReportBuilder() {
        ReportParser reportParser = new ReportParser(configuration);

        List<Feature> featuresFromJson = reportParser.parseJsonResults(jsonReports);
        reportResult = new ReportResult(featuresFromJson);

        features = reportResult.getAllFeatures();
        tags = reportResult.getAllTags();
        steps = reportResult.getAllSteps();
    }

    private void createEmbeddingsDirectory() {
        configuration.getEmbeddingDirectory().mkdirs();
    }
}
