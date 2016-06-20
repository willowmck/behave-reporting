package org.orangesoft.behave.generators.integrations;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;

import org.orangesoft.behave.generators.ErrorPage;
import org.orangesoft.behave.generators.integrations.helpers.DocumentAssertion;
import org.orangesoft.behave.generators.integrations.helpers.LeadAssertion;
import org.orangesoft.behave.generators.integrations.helpers.WebAssertion;

/**
 * @author Damian Szczepanik (damianszczepanik@github)
 */
public class ErrorPageIntegrationTest extends PageTest {

    private final Exception cause = new IllegalArgumentException("Help me!");

    @Test
    public void generatePage_generatesTitle() throws Exception{

        // given
        setUpWithJson(SAMPLE_JSON);
        page = new ErrorPage(reportResult, configuration, cause, jsonReports);
        final String titleValue = String.format("Behave Html Reports  - Error Page");

        // when
        page.generatePage();

        // then
        DocumentAssertion document = documentFrom(page.getWebPage());
        String title = document.getHead().getTitle();

        assertThat(title).isEqualTo(titleValue);
    }

    @Test
    public void generatePage_generatesLead() throws Exception{

        // given
        setUpWithJson(SAMPLE_JSON);
        configuration.setBuildNumber("12");
        page = new ErrorPage(reportResult, configuration, cause, jsonReports);

        // when
        page.generatePage();

        // then
        DocumentAssertion document = documentFrom(page.getWebPage());
        LeadAssertion lead = document.getLead();

        assertThat(lead.getHeader()).isEqualTo("Error");
        assertThat(lead.getDescription()).isEqualTo(String.format("Something went wrong with project %s, build %s",
                configuration.getProjectName(), configuration.getBuildNumber()));
    }

    @Test
    public void generatePage_generatesErrorMessage() throws Exception {

        // given
        setUpWithJson(SAMPLE_JSON);
        page = new ErrorPage(reportResult, configuration, cause, jsonReports);

        // when
        page.generatePage();

        // then
        DocumentAssertion document = documentFrom(page.getWebPage());
        String error = getErrorMessage(document).text();
        assertThat(error).contains(cause.getMessage());
        assertThat(error).contains(cause.getClass().getName());

        String details = getReportList(document).text();
        for (String fileName : jsonReports) {
            assertThat(details).contains(fileName);
        }
    }

    private WebAssertion getErrorMessage(WebAssertion document) {
        return getErrorSection(document).oneByClass("message", WebAssertion.class);
    }

    private WebAssertion getReportList(WebAssertion document) {
        return getErrorSection(document).oneByClass("error-files", WebAssertion.class);
    }

    private WebAssertion getErrorSection(WebAssertion document) {
        return document.byId("report", WebAssertion.class);
    }
}
