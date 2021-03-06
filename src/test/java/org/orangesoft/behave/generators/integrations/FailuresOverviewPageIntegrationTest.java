package org.orangesoft.behave.generators.integrations;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;

import org.orangesoft.behave.generators.FailuresOverviewPage;
import org.orangesoft.behave.generators.integrations.helpers.DocumentAssertion;
import org.orangesoft.behave.generators.integrations.helpers.ElementAssertion;
import org.orangesoft.behave.generators.integrations.helpers.LeadAssertion;
import org.orangesoft.behave.generators.integrations.helpers.SummaryAssertion;

/**
 * @author Damian Szczepanik (damianszczepanik@github)
 */
public class FailuresOverviewPageIntegrationTest extends PageTest {

    @Test
    public void generatePage_generatesTitle() throws Exception {

        // given
        setUpWithJson(SAMPLE_JSON);
        configuration.setRunWithJenkins(true);
        configuration.setBuildNumber("1");
        page = new FailuresOverviewPage(reportResult, configuration);
        final String titleValue = String.format("Behave Html Reports (no %s) - Failures Overview",
                configuration.getBuildNumber());

        // when
        page.generatePage();

        // then
        DocumentAssertion document = documentFrom(page.getWebPage());
        String title = document.getHead().getTitle();

        assertThat(title).isEqualTo(titleValue);
    }

    @Test
    public void generatePage_generatesLead() throws Exception {

        // given
        setUpWithJson(SAMPLE_JSON);
        page = new FailuresOverviewPage(reportResult, configuration);

        // when
        page.generatePage();

        // then
        DocumentAssertion document = documentFrom(page.getWebPage());
        LeadAssertion lead = document.getLead();

        assertThat(lead.getHeader()).isEqualTo("Failures Overview");
        assertThat(lead.getDescription()).isEqualTo("The following summary displays scenarios that failed.");
    }

    @Test
    public void generatePage_onEmptyJsons_generatesProperMessage() throws Exception {

        // given
        setUpWithJson(EMPTY_JSON);
        page = new FailuresOverviewPage(reportResult, configuration);

        // when
        page.generatePage();

        // then
        DocumentAssertion document = documentFrom(page.getWebPage());
        SummaryAssertion summary = document.getSummary();
        assertThat(summary.getEmptyReportMessage()).isEqualTo("You have no failed scenarios in your behave report");
    }

    public void generatePage_generatesSummary() throws Exception {

        // given
        setUpWithJson(SAMPLE_JSON);
        page = new FailuresOverviewPage(reportResult, configuration);

        // when
        page.generatePage();

        // then
        DocumentAssertion document = documentFrom(page.getWebPage());
        ElementAssertion[] elements = document.getElements();
        assertThat(elements).hasSize(1);
        assertThat(elements[0].getBrief().getName()).isEqualTo(features.get(1).getElements()[0].getName());

    }
}
