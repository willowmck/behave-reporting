package org.orangesoft.behave.json;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import org.orangesoft.behave.Configuration;
import org.orangesoft.behave.json.support.Status;
import org.orangesoft.behave.json.support.StatusCounter;
import org.orangesoft.behave.reports.Reportable;
import org.orangesoft.behave.util.Util;

public class Feature implements Reportable, Comparable<Feature> {

    // Start: attributes from JSON file report
    private final String id = null;
    private final String name = null;
    private final String uri = null;
    private final String location = null;
    private final String description = null;
    private final String keyword = null;

    private final Element[] elements = new Element[0];
    private final String[] tags = new String[0];
    // End: attributes from JSON file report

    private String jsonFile;
    private String reportFileName;
    private String deviceName;
    private final List<Element> scenarios = new ArrayList<>();
    private final StatusCounter scenarioCounter = new StatusCounter();
    private Status featureStatus;
    private final StatusCounter statusCounter = new StatusCounter();
    private long totalDuration;
    private int totalSteps;

    @Override
    public String getDeviceName() {
        return deviceName;
    }

    public String getId() {
        return id;
    }

    public Element[] getElements() {
        return elements;
    }

    public String getReportFileName() {
        return reportFileName;
    }

    public String[] getTags() {
        return tags;
    }

    @Override
    public Status getStatus() {
        return featureStatus;
    }

    @Override
    public String getName() {
        return StringUtils.defaultString(name);
    }

    public String getUri() {
        return uri;
    }
    
    public String getLocation() {
        return location;
    }

    public String getKeyword() {
        return StringUtils.defaultString(keyword);
    }

    public String getRawStatus() {
        return getStatus().toString().toLowerCase();
    }

    public String getDescription() {
        return StringUtils.defaultString(description);
    }

    @Override
    public int getScenarios() {
        return scenarios.size();
    }

    @Override
    public int getSteps() {
        return totalSteps;
    }

    @Override
    public int getPassedSteps() {
        return statusCounter.getValueFor(Status.PASSED);
    }

    @Override
    public int getFailedSteps() {
        return statusCounter.getValueFor(Status.FAILED);
    }

    @Override
    public int getPendingSteps() {
        return statusCounter.getValueFor(Status.PENDING);
    }

    @Override
    public int getSkippedSteps() {
        return statusCounter.getValueFor(Status.SKIPPED);
    }

    @Override
    public int getMissingSteps() {
        return statusCounter.getValueFor(Status.MISSING);
    }

    @Override
    public int getUndefinedSteps() {
        return statusCounter.getValueFor(Status.UNDEFINED);
    }

    @Override
    public long getDurations() {
        return totalDuration;
    }

    @Override
    public String getFormattedDurations() {
        return Util.formatDuration(getDurations());
    }

    @Override
    public int getPassedScenarios() {
        return scenarioCounter.getValueFor(Status.PASSED);
    }

    @Override
    public int getFailedScenarios() {
        return scenarioCounter.getValueFor(Status.FAILED);
    }

    /** Sets additional information and calculates values which should be calculated during object creation. */
    public void setMetaData(String jsonFile, int jsonFileNo, Configuration configuration) {
        this.jsonFile = StringUtils.substringAfterLast(jsonFile, File.pathSeparator);
        System.out.println("[setMetadata] : jsonFile=" + jsonFile);

        for (Element element : elements) {
            element.setMedaData(this, configuration);

             System.out.println("[setMetadata] : setMetadata in element");
            if (element.isScenario()) {
                scenarios.add(element);
                System.out.println("[setMetadata] : added element to scenario");
            }
        }

        setDeviceName();
        System.out.println("[setMetadata] : set device name");
        setReportFileName(jsonFileNo, configuration);
        System.out.println("[setMetadata] : set report filename");
        calculateFeatureStatus();
        System.out.println("[setMetadata] : calculated feature status");

        calculateSteps();
         System.out.println("[setMetadata] : calculated steps");
    }

    private void setDeviceName() {
        String[] splitedJsonFile = jsonFile.split("[^\\d\\w]");
        if (splitedJsonFile.length > 1) {
            // file name without path and extension (usually path/{jsonfIle}.json)
            deviceName = splitedJsonFile[splitedJsonFile.length - 2];
        } else {
            // path name without special characters
            deviceName = splitedJsonFile[0];
        }
    }
    
    private String getReportLocation() {
        if (location != null)
            return location;
        return uri;
    }

    private void setReportFileName(int jsonFileNo, Configuration configuration) {
        // remove all characters that might not be valid file name
        reportFileName = Util.toValidFileName(getReportLocation());
        System.out.println("[setReportFileName] : report filename=" + reportFileName);

        // If we expect to have parallel executions, we add postfix to file name
        if (configuration.isParallelTesting()) {
            reportFileName += "_" + getDeviceName();
            System.out.println("[setReportFileName] : modified report filename=" + reportFileName);
        }

        // if there is only one JSON file - skip unique prefix
        if (jsonFileNo > 0) {
            // add jsonFile index to the file name so if two the same features are reported
            // in two different JSON files then file name must be different
            reportFileName += "_" + jsonFileNo;
             System.out.println("[setReportFileName] : 2nd modified report filename=" + reportFileName);
        }

        reportFileName += ".html";
        System.out.println("[setReportFileName] : 3rd modified report filename=" + reportFileName);
    }

    private void calculateFeatureStatus() {
        for (Element element : elements) {
            if (element.getElementStatus() != Status.PASSED) {
                featureStatus = Status.FAILED;
                return;
            }
        }
        featureStatus = Status.PASSED;
    }

    private void calculateSteps() {
        for (Element element : elements) {
            if (element.isScenario()) {
                scenarioCounter.incrementFor(element.getElementStatus());
            }

            totalSteps += element.getSteps().length;

            for (Step step : element.getSteps()) {
                statusCounter.incrementFor(step.getStatus());
                totalDuration += step.getDuration();
            }
        }
    }

    @Override
    public int compareTo(Feature o) {
        // feature without name seems to be invalid
        return Integer.signum(name.compareTo(o.getName()));
    }
}
