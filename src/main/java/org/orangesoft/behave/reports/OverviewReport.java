package org.orangesoft.behave.reports;

import org.apache.commons.lang.NotImplementedException;

import org.orangesoft.behave.json.support.Status;
import org.orangesoft.behave.json.support.StatusCounter;
import org.orangesoft.behave.util.Util;

public class OverviewReport implements Reportable {

    private final String reportName;

    private long duration;

    private final StatusCounter scenariosCounter = new StatusCounter();
    private final StatusCounter stepsCounter = new StatusCounter();

    public OverviewReport(String reportName) {
        this.reportName = reportName;
    }

    @Override
    public int getScenarios() {
        return scenariosCounter.size();
    }

    @Override
    public int getPassedScenarios() {
        return scenariosCounter.getValueFor(Status.PASSED);
    }

    @Override
    public int getFailedScenarios() {
        return scenariosCounter.getValueFor(Status.FAILED);
    }

    public void incScenarioFor(Status status) {
        this.scenariosCounter.incrementFor(status);
    }

    @Override
    public int getSteps() {
        return stepsCounter.size();
    }

    @Override
    public int getPassedSteps() {
        return stepsCounter.getValueFor(Status.PASSED);
    }

    @Override
    public int getFailedSteps() {
        return stepsCounter.getValueFor(Status.FAILED);
    }

    @Override
    public int getSkippedSteps() {
        return stepsCounter.getValueFor(Status.SKIPPED);
    }

    @Override
    public int getUndefinedSteps() {
        return stepsCounter.getValueFor(Status.UNDEFINED);
    }

    @Override
    public int getMissingSteps() {
        return stepsCounter.getValueFor(Status.MISSING);
    }

    @Override
    public int getPendingSteps() {
        return stepsCounter.getValueFor(Status.PENDING);
    }

    public void incStepsFor(Status status) {
        this.stepsCounter.incrementFor(status);
    }

    @Override
    public long getDurations() {
        return duration;
    }

    @Override
    public String getFormattedDurations() {
        return Util.formatDuration(getDurations());
    }

    public void incDurationBy(long duration) {
        this.duration += duration;
    }

    @Override
    public String getDeviceName() {
        throw new NotImplementedException();
    }

    @Override
    public String getName() {
        return reportName;
    }

    @Override
    public Status getStatus() {
        throw new NotImplementedException();
    }
}
