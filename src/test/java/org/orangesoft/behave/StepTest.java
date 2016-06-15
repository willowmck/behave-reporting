package org.orangesoft.behave;

import static org.orangesoft.behave.FileReaderUtil.getAbsolutePathFromResource;
import static org.assertj.core.api.Assertions.assertThat;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import org.orangesoft.behave.json.Feature;
import org.orangesoft.behave.json.Step;
import org.orangesoft.behave.json.support.Status;

public class StepTest {

    private final Configuration configuration = new Configuration(new File(""), "testProject");

    private Step passingStep;
    private Step failingStep;

    @Before
    public void setUpJsonReports() throws IOException {
        configuration.setStatusFlags(false, false, false, false);

        List<String> jsonReports = new ArrayList<>();
        jsonReports.add(getAbsolutePathFromResource("org/orangesoft/behave/project1.json"));
        List<Feature> features = new ReportParser(configuration).parseJsonResults(jsonReports);
        Feature passingFeature = features.get(0);
        Feature failingFeature = features.get(1);

        passingStep = passingFeature.getElements()[0].getSteps()[0];
        failingStep = failingFeature.getElements()[0].getSteps()[5];
    }

    @Test
    public void shouldKnowIfHasRows() {
        assertThat(passingStep.hasRows()).isFalse();
    }

    @Test
    public void shouldReturnStatus() {
        assertThat(passingStep.getStatus()).isEqualTo(Status.PASSED);
        assertThat(failingStep.getStatus()).isEqualTo(Status.FAILED);
    }

    @Test
    public void shouldReturnDuration() {
        assertThat(passingStep.getDuration()).isEqualTo(107447000L);
    }
}
