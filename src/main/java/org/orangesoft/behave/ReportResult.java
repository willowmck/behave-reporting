package org.orangesoft.behave;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.apache.commons.lang.StringUtils;

import org.orangesoft.behave.json.Element;
import org.orangesoft.behave.json.Feature;
import org.orangesoft.behave.json.Match;
import org.orangesoft.behave.json.Result;
import org.orangesoft.behave.json.Step;
import org.orangesoft.behave.json.support.Resultsable;
import org.orangesoft.behave.json.support.Status;
import org.orangesoft.behave.json.support.StatusCounter;
import org.orangesoft.behave.json.support.StepObject;
import org.orangesoft.behave.json.support.TagObject;
import org.orangesoft.behave.reports.OverviewReport;
import org.orangesoft.behave.reports.Reportable;

public class ReportResult {

    private final List<Feature> allFeatures = new ArrayList<>();
    private final Map<String, TagObject> allTags = new TreeMap<>();
    private final Map<String, StepObject> allSteps = new TreeMap<>();

    private final StatusCounter featureCounter = new StatusCounter();

    private final String buildTime;

    private final OverviewReport featuresReport = new OverviewReport("Features");
    private final OverviewReport tagsReport = new OverviewReport("Tags");

    public ReportResult(List<Feature> features) {
        this.buildTime = getCurrentTime();

        for (Feature feature : features) {
            processFeature(feature);
        }
    }

    public List<Feature> getAllFeatures() {
        return mapToSortedList(allFeatures);
    }

    public List<TagObject> getAllTags() {
        return mapToSortedList(allTags.values());
    }

    public List<StepObject> getAllSteps() {
        return mapToSortedList(allSteps.values());
    }

    public Reportable getFeatureReport() {
        return featuresReport;
    }

    public Reportable getTagReport() {
        return tagsReport;
    }

    public int getAllPassedFeatures() {
        return featureCounter.getValueFor(Status.PASSED);
    }

    public int getAllFailedFeatures() {
        return featureCounter.getValueFor(Status.FAILED);
    }

    public String getBuildTime() {
        return buildTime;
    }

    private void processFeature(Feature feature) {
        allFeatures.add(feature);

        for (Element element : feature.getElements()) {
            if (element.isScenario()) {
                featuresReport.incScenarioFor(element.getElementStatus());

                // all feature tags should be linked with scenario
                for (String tag : feature.getTags()) {
                    processTag(tag, element, feature.getStatus());
                }
            }

            // all element tags should be linked with element
            for (String tag : element.getTags()) {
                processTag(tag, element, element.getElementStatus());
            }

            Step[] steps = element.getSteps();
            for (Step step : steps) {
                featuresReport.incStepsFor(step.getStatus());
                featuresReport.incDurationBy(step.getDuration());
            }
            countSteps(steps);

            countSteps(element.getBefore());
            countSteps(element.getAfter());
        }
        featureCounter.incrementFor(feature.getStatus());
    }

    private void processTag(String tag, Element element, Status status) {

        TagObject tagObject = addTagObject(tag);

        // if this element was not added by feature tag, add it as element tag
        if (tagObject.addElement(element)) {
            tagsReport.incScenarioFor(status);

            Step[] steps = element.getSteps();
            for (Step step : steps) {
                tagsReport.incStepsFor(step.getStatus());
                tagsReport.incDurationBy(step.getDuration());
            }
        }
    }

    private void countSteps(Resultsable[] steps) {
        for (Resultsable resultsable : steps) {

            Step step = (Step) resultsable;
            Match match = step.getMatch();
            // no match = could not find method that was matched to this step -> status is missing
            if (match == null) {
                continue;
            }

           //String methodName = match.getLocation();
           String methodName = step.getKeyword() + " " + step.getName();
            // location is missing so there is no way to identify step
            if (StringUtils.isEmpty(methodName)) {
                continue;
            }
            addNewStep(step.getResult(), methodName);
        }
    }

    private void addNewStep(Result result, String methodName) {
        StepObject stepObject = allSteps.get(methodName);
        // if first occurrence of this location add element to the map
        if (stepObject == null) {
            stepObject = new StepObject(methodName);
        }

        // happens that report is not valid - does not contain information about result
        if (result != null) {
            stepObject.addDuration(result.getDuration(), Status.toStatus(result.getStatus()));
        } else {
            // when result is not available it means that something really went wrong (report is incomplete)
            // and for this case FAILED status is used to avoid problems during parsing
            stepObject.addDuration(0, Status.FAILED);
        }
        allSteps.put(methodName, stepObject);
    }

    private TagObject addTagObject(String name) {
        TagObject tagObject = allTags.get(name);
        if (tagObject == null) {
            tagObject = new TagObject(name);
            allTags.put(tagObject.getName(), tagObject);
        }
        return tagObject;
    }

    public static String getCurrentTime() {
        return new SimpleDateFormat("dd MMM yyyy, HH:mm").format(new Date());
    }

    private static <T extends Comparable<? super T>> List<T> mapToSortedList(Collection<T> values) {
        List<T> list = new ArrayList<>(values);
        Collections.sort(list);
        return list;
    }
}
