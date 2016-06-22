package org.orangesoft.behave.generators.integrations.helpers;

import org.orangesoft.behave.Configuration;

/**
 * @author Damian Szczepanik (damianszczepanik@github)
 */
public class NavigationItemAssertion extends LinkAssertion {

    public void hasLinkToJenkins(Configuration configuration) {
        hasLabelAndAddress("Jenkins", "/job/" + configuration.getProjectName() + "/" + configuration.getBuildNumber());
    }

    public void hasLinkToPreviousResult(Configuration configuration, String page) {
        final Integer prevBuildNumber = Integer.parseInt(configuration.getBuildNumber()) - 1;
        hasLabelAndAddress("Previous results", "/job/" + configuration.getProjectName() + "/" + prevBuildNumber
                + "/behave-html-reports/" + page);
    }

    public void hasLinkToLastResult(Configuration configuration, String page) {
        hasLabelAndAddress("Last results", "/job/" + configuration.getProjectName() + "/behave-html-reports/" + page);
    }

    public void hasLinkToFeatures() {
        hasLabelAndAddress("Features", "feature-overview.html");
    }

    public void hasLinkToTags() {
        hasLabelAndAddress("Tags", "tag-overview.html");
    }

    public void hasLinkToSteps() {
        hasLabelAndAddress("Steps", "step-overview.html");
    }

    public void hasLinkToFailures() {
        hasLabelAndAddress("Failures", "failures-overview.html");
    }
}
