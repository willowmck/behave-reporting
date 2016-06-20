package org.orangesoft.behave.generators.integrations;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;

import org.orangesoft.behave.generators.TagReportPage;
import org.orangesoft.behave.generators.integrations.helpers.BriefAssertion;
import org.orangesoft.behave.generators.integrations.helpers.DocumentAssertion;
import org.orangesoft.behave.generators.integrations.helpers.ElementAssertion;
import org.orangesoft.behave.generators.integrations.helpers.TableRowAssertion;
import org.orangesoft.behave.json.Step;
import org.orangesoft.behave.json.support.TagObject;

/**
 * @author Damian Szczepanik (damianszczepanik@github)
 */
public class TagReportPageIntegrationTest extends PageTest {

    @Test
    public void generatePage_generatesTitle() throws Exception {

        // given
        setUpWithJson(SAMPLE_JSON);
        final TagObject tag = tags.get(0);
        page = new TagReportPage(reportResult, configuration, tag);
        final String titleValue = String.format("Behave Html Reports  - Tag: %s", tag.getName());

        // when
        page.generatePage();

        // then
        DocumentAssertion document = documentFrom(page.getWebPage());
        String title = document.getHead().getTitle();

        assertThat(title).isEqualTo(titleValue);
    }

    @Test
    public void generatePage_generatesStatsTableBody()  throws Exception{

        // given
        setUpWithJson(SAMPLE_JSON);
        configuration.setStatusFlags(true, false, false, true);
        final TagObject tag = tags.get(0);
        page = new TagReportPage(reportResult, configuration, tag);

        // when
        page.generatePage();

        // then
        DocumentAssertion document = documentFrom(page.getWebPage());
        TableRowAssertion bodyRow = document.getSummary().getTableStats().getBodyRow();

        bodyRow.hasExactValues(tag.getName(), "1", "1", "0", "7", "7", "0", "0", "0", "0", "0", "000ms", "Passed");
        bodyRow.hasExactCSSClasses("tagname", "", "", "", "", "", "", "", "", "", "", "duration", "passed");
    }

    @Test
    public void generatePage_generatesTagsList() throws Exception {

        // given
        setUpWithJson(SAMPLE_JSON);
        final TagObject tag = tags.get(0);
        page = new TagReportPage(reportResult, configuration, tag);

        // when
        page.generatePage();

        // then
        DocumentAssertion document = documentFrom(page.getWebPage());
        ElementAssertion[] elements = document.getElements();

        assertThat(elements).hasSameSizeAs(tags.get(0).getElements());
    }

    @Test
    public void generatePage_generatesSampleStep() throws Exception{

        // given
        setUpWithJson(SAMPLE_JSON);
        final TagObject tag = tags.get(1);
        page = new TagReportPage(reportResult, configuration, tag);

        // when
        page.generatePage();

        // then
        DocumentAssertion document = documentFrom(page.getWebPage());

        BriefAssertion stepElement = document.getElements()[0].getStepsSection().getSteps()[3].getBrief();
        Step step = tag.getElements().get(0).getSteps()[3];
        assertThat(stepElement.getName()).hasSameSizeAs(step.getName());
    }
}
