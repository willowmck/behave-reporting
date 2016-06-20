package org.orangesoft.behave;

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

    protected static final String SAMPLE_JSON = "behave_sample.json";
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

    protected void setUpWithJson(String... jsonFiles) throws Exception {
        for (String jsonFile : jsonFiles) {
            System.out.println("Adding report " + jsonFile);
            addReport(jsonFile);
            System.out.println("Completed adding report");
        }

        configuration = new Configuration(reportDirectory, projectName);

        createEmbeddingsDirectory();
        System.out.println("Done creating embeddings directory");
        createReportBuilder();
        System.out.println("Done creating reports builder");
    }

    private void addReport(String jsonReport)  throws Exception{
       // try {
            URL path = ReportGenerator.class.getClassLoader().getResource(JSON_DIRECTORY + jsonReport);
            jsonReports.add(new File(path.toURI()).getAbsolutePath());
        //} catch (URISyntaxException e) {
         //   throw new ValidationException(e);
        //}
    }

    private void createReportBuilder() throws Exception {
        ReportParser reportParser = new ReportParser(configuration);
        System.out.println("Created report parser");

        List<Feature> featuresFromJson = reportParser.parseJsonResults(jsonReports);
        System.out.println("Retrieved features from report parser");
        reportResult = new ReportResult(featuresFromJson);
        System.out.println("Got report result");

        features = reportResult.getAllFeatures();
        System.out.println("Retrieved all features");
        tags = reportResult.getAllTags();
        System.out.println("Retrieved all tags");
        steps = reportResult.getAllSteps();
        System.out.println("Retrieved all steps");
    }

    private void createEmbeddingsDirectory() {
        configuration.getEmbeddingDirectory().mkdirs();
    }
}
