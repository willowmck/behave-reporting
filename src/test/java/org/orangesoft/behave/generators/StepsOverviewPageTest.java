package org.orangesoft.behave.generators;

import static org.assertj.core.api.Assertions.assertThat;

import org.apache.velocity.VelocityContext;
import org.junit.Before;
import org.junit.Test;

import mockit.Deencapsulation;
import org.orangesoft.behave.generators.integrations.PageTest;
import org.orangesoft.behave.json.support.StepObject;
import org.orangesoft.behave.util.Util;

/**
 * @author Damian Szczepanik (damianszczepanik@github)
 */
public class StepsOverviewPageTest extends PageTest {

    @Before
    public void setUp() throws Exception {
        setUpWithJson(SAMPLE_JSON);
    }

    @Test
    public void getWebPage_ReturnsFeatureFileName() {

        // give
        page = new StepsOverviewPage(reportResult, configuration);

        // when
        String fileName = page.getWebPage();

        // then
        assertThat(fileName).isEqualTo("step-overview.html");
    }

    @Test
    public void prepareReportAddsCustomProperties() {

        // give
        page = new StepsOverviewPage(reportResult, configuration);

        // when
        page.prepareReport();

        // then
        VelocityContext context = Deencapsulation.getField(page, "context");
        assertThat(context.getKeys()).hasSize(9);
        assertThat(context.get("all_steps")).isEqualTo(steps);

        int allOccurrences = 0;
        long allDurations = 0;
        for (StepObject stepObject : reportResult.getAllSteps()) {
            allOccurrences += stepObject.getTotalOccurrences();
            allDurations += stepObject.getDurations();
        }
        assertThat(context.get("all_occurrences")).isEqualTo(allOccurrences);
        assertThat(context.get("all_durations")).isEqualTo(Util.formatDuration(allDurations));
        long average = allDurations / (allOccurrences == 0 ? 1 : allOccurrences);
        assertThat(context.get("all_average")).isEqualTo(Util.formatDuration(average));
    }
}
