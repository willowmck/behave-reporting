package org.orangesoft.behave.generators;

import static org.assertj.core.api.Assertions.assertThat;

import org.apache.velocity.VelocityContext;
import org.junit.Before;
import org.junit.Test;

import mockit.Deencapsulation;
import org.orangesoft.behave.ReportBuilder;
import org.orangesoft.behave.generators.integrations.PageTest;

/**
 * @author Damian Szczepanik (damianszczepanik@github)
 */
public class FeaturesOverviewPageTest extends PageTest {

    @Before
    public void setUp() throws Exception {
        setUpWithJson(SAMPLE_JSON);
    }

    @Test
    public void getWebPage_ReturnsFeatureFileName() {

        // give
        page = new FeaturesOverviewPage(reportResult, configuration);

        // when
        String fileName = page.getWebPage();

        // then
        assertThat(fileName).isEqualTo(ReportBuilder.HOME_PAGE);
    }

    @Test
    public void prepareReportAddsCustomProperties() {

        // give
        page = new FeaturesOverviewPage(reportResult, configuration);

        // when
        page.prepareReport();

        // then
        VelocityContext context = Deencapsulation.getField(page, "context");
        assertThat(context.getKeys()).hasSize(10);

        assertThat(context.get("all_features")).isEqualTo(features);
        assertThat(context.get("report_summary")).isEqualTo(reportResult.getFeatureReport());
        assertThat(context.get("all_features_passed")).isEqualTo(reportResult.getAllPassedFeatures());
        assertThat(context.get("all_features_failed")).isEqualTo(reportResult.getAllFailedFeatures());
    }
}
