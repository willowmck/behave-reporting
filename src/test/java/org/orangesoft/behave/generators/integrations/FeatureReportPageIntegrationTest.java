package org.orangesoft.behave.generators.integrations;


import java.io.File;

import org.junit.Test;

import org.orangesoft.behave.generators.FeatureReportPage;
import org.orangesoft.behave.generators.integrations.helpers.BriefAssertion;
import org.orangesoft.behave.generators.integrations.helpers.DocumentAssertion;
import org.orangesoft.behave.generators.integrations.helpers.ElementAssertion;
import org.orangesoft.behave.generators.integrations.helpers.EmbeddingAssertion;
import org.orangesoft.behave.generators.integrations.helpers.FeatureAssertion;
import org.orangesoft.behave.generators.integrations.helpers.HookAssertion;
import org.orangesoft.behave.generators.integrations.helpers.HooksAssertion;
import org.orangesoft.behave.generators.integrations.helpers.OutputAssertion;
import org.orangesoft.behave.generators.integrations.helpers.StepAssertion;
import org.orangesoft.behave.generators.integrations.helpers.StepsAssertion;
import org.orangesoft.behave.generators.integrations.helpers.TableAssertion;
import org.orangesoft.behave.generators.integrations.helpers.TableRowAssertion;
import org.orangesoft.behave.generators.integrations.helpers.TagAssertion;
import org.orangesoft.behave.json.Element;
import org.orangesoft.behave.json.Embedding;
import org.orangesoft.behave.json.Feature;
import org.orangesoft.behave.json.Hook;
import org.orangesoft.behave.json.Output;
import org.orangesoft.behave.json.Row;
import org.orangesoft.behave.json.Step;
import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Damian Szczepanik (damianszczepanik@github)
 */
public class FeatureReportPageIntegrationTest extends PageTest {

    @Test
    public void generatePage_generatesTitle() {

        // given
        setUpWithJson(SAMPLE_JSON);
        final Feature feature = features.get(0);
        page = new FeatureReportPage(reportResult, configuration, feature);
        final String titleValue = String.format("Behave Html Reports  - Feature: %s", feature.getName());

        // when
        page.generatePage();

        // then
        DocumentAssertion document = documentFrom(page.getWebPage());
        String title = document.getHead().getTitle();

        assertThat(title).isEqualTo(titleValue);
    }

    @Test
    public void generatePage_generatesStatsTableBody() {

        // given
        setUpWithJson(SAMPLE_JSON);
        configuration.setStatusFlags(true, false, false, true);
        final Feature feature = features.get(0);
        page = new FeatureReportPage(reportResult, configuration, feature);

        // when
        page.generatePage();

        // then
        DocumentAssertion document = documentFrom(page.getWebPage());
        TableRowAssertion odyRow = document.getSummary().getTableStats().getBodyRow();

        odyRow.hasExactValues(feature.getName(), "1", "1", "0", "11", "8", "0", "0", "2", "1", "0", "1m 39s 353ms", "Passed");
        odyRow.hasExactCSSClasses("tagname", "", "", "", "", "", "", "", "pending", "undefined", "", "duration", "passed");
    }

    @Test
    public void generatePage_generatesFeatureDetails() {

        // given
        setUpWithJson(SAMPLE_JSON);
        final Feature feature = features.get(0);
        page = new FeatureReportPage(reportResult, configuration, feature);

        // when
        page.generatePage();

        // then
        DocumentAssertion document = documentFrom(page.getWebPage());
        FeatureAssertion featureDetails = document.getFeature();

        BriefAssertion brief = featureDetails.getBrief();
        assertThat(brief.getKeyword()).isEqualTo(feature.getKeyword());
        assertThat(brief.getName()).isEqualTo(feature.getName());
        brief.hasStatus(feature.getStatus());

        TagAssertion[] testTags = featureDetails.getTags();
        assertThat(testTags).hasSize(1);
        testTags[0].getLink().hasLabelAndAddress("@featureTag", "featureTag.html");

        assertThat(featureDetails.getDescription()).isEqualTo(feature.getDescription());
    }

    @Test
    public void generatePage_generatesScenarioDetails() {

        // given
        setUpWithJson(SAMPLE_JSON);
        final Feature feature = features.get(0);
        page = new FeatureReportPage(reportResult, configuration, feature);

        // when
        page.generatePage();

        // then
        DocumentAssertion document = documentFrom(page.getWebPage());

        ElementAssertion[] elements = document.getFeature().getElements();
        assertThat(elements).hasSize(feature.getElements().length);

        ElementAssertion firstElement = elements[1];
        Element scenario = feature.getElements()[1];

        TagAssertion[] testTags = firstElement.getTags();
        assertThat(testTags).hasSize(scenario.getTags().length);
        for (int i = 0; i < testTags.length; i++) {
            testTags[i].getLink().hasLabelAndAddress(scenario.getTags()[i].getName(), scenario.getTags()[i].getFileName());
        }

        BriefAssertion brief = firstElement.getBrief();
        assertThat(brief.getKeyword()).isEqualTo(scenario.getKeyword());
        assertThat(brief.getName()).isEqualTo(scenario.getName());
        brief.hasStatus(scenario.getElementStatus());

        assertThat(firstElement.getDescription()).isEqualTo(scenario.getDescription());
    }

    @Test
    public void generatePage_generatesHooks() {

        // given
        setUpWithJson(SAMPLE_JSON);
        final Feature feature = features.get(1);
        page = new FeatureReportPage(reportResult, configuration, feature);

        // when
        page.generatePage();

        // then
        DocumentAssertion document = documentFrom(page.getWebPage());

        ElementAssertion secondElement = document.getFeature().getElements()[0];

        Element element = feature.getElements()[0];

        HooksAssertion beforeHooks = secondElement.getBefore();
        HookAssertion[] before = beforeHooks.getHooks();
        assertThat(before).hasSize(element.getBefore().length);
        validateHook(before, element.getBefore(), "Before");
        BriefAssertion beforeBrief = beforeHooks.getBrief();
        beforeBrief.hasStatus(element.getBeforeStatus());

        HooksAssertion afterHooks = secondElement.getAfter();
        HookAssertion[] after = afterHooks.getHooks();
        assertThat(after).hasSize(element.getAfter().length);
        validateHook(after, element.getAfter(), "After");
        BriefAssertion afterBrief = afterHooks.getBrief();
        afterBrief.hasStatus(element.getAfterStatus());
    }

    @Test
    public void generatePage_generatesSteps() {

        // given
        setUpWithJson(SAMPLE_JSON);
        final Feature feature = features.get(1);
        page = new FeatureReportPage(reportResult, configuration, feature);

        // when
        page.generatePage();

        // then
        DocumentAssertion document = documentFrom(page.getWebPage());

        ElementAssertion secondElement = document.getFeature().getElements()[0];
        Element element = feature.getElements()[0];

        StepsAssertion stepsSection = secondElement.getStepsSection();
        stepsSection.getBrief().hasStatus(element.getStepsStatus());
        StepAssertion[] testSteps = stepsSection.getSteps();
        assertThat(testSteps).hasSameSizeAs(element.getSteps());

        for (int i = 0; i < testSteps.length; i++) {
            BriefAssertion brief = testSteps[i].getBrief();
            Step step = element.getSteps()[i];

            brief.hasStatus(step.getStatus());
            assertThat(brief.getKeyword()).isEqualTo(step.getKeyword());
            assertThat(brief.getName()).isEqualTo(step.getName());
            brief.hasDuration(step.getDuration());
        }
    }

    @Test
    public void generatePage_OnBiDimentionalArray_generatesOutput() {

        // given
        setUpWithJson(SAMPLE_JSON);
        final Feature feature = features.get(1);
        page = new FeatureReportPage(reportResult, configuration, feature);

        // when
        page.generatePage();

        // then
        DocumentAssertion document = documentFrom(page.getWebPage());

        Output outputElement = features.get(1).getElements()[0].getSteps()[7].getOutput();
        OutputAssertion output = document.getFeature().getElements()[0].getStepsSection().getSteps()[7].getOutput();
        output.hasMessages(outputElement.getMessages());
    }

    @Test
    public void generatePage_OnSingleArray_generatesOutput() {

        // given
        setUpWithJson(SAMPLE_JSON);
        final Feature feature = features.get(1);
        page = new FeatureReportPage(reportResult, configuration, feature);

        // when
        page.generatePage();

        // then
        DocumentAssertion document = documentFrom(page.getWebPage());

        Output outputElement = features.get(1).getElements()[0].getSteps()[8].getOutput();
        OutputAssertion output = document.getFeature().getElements()[0].getStepsSection().getSteps()[8].getOutput();
        output.hasMessages(outputElement.getMessages());
    }

    @Test
    public void generatePage_generatesArguments() {

        // given
        setUpWithJson(SAMPLE_JSON);
        final Feature feature = features.get(0);
        page = new FeatureReportPage(reportResult, configuration, feature);

        // when
        page.generatePage();

        // then
        DocumentAssertion document = documentFrom(page.getWebPage());

        StepAssertion stepElement = document.getFeature().getElements()[0].getStepsSection().getSteps()[2];
        TableAssertion argTable = stepElement.getArgumentsTable();

        Step step = feature.getElements()[0].getSteps()[1];

        for (int r = 0; r < step.getRows().length; r++) {
            Row row = step.getRows()[r];
            TableRowAssertion rowElement = argTable.getBodyRows()[r];

            assertThat(rowElement.getCellsValues()).isEqualTo(row.getCells());
        }
    }

    @Test
    public void generatePage_generatesDocString() {

        // given
        setUpWithJson(SAMPLE_JSON);
        final Feature feature = features.get(0);
        page = new FeatureReportPage(reportResult, configuration, feature);

        // when
        page.generatePage();

        // then
        DocumentAssertion document = documentFrom(page.getWebPage());

        StepAssertion stepElement = document.getFeature().getElements()[0].getStepsSection().getSteps()[1];
        Step step = feature.getElements()[0].getSteps()[1];

        stepElement.getDocString().hasDocString(step.getDocString());
    }

    @Test
    public void generatePage_generatesEmbedding() {

        // given
        setUpWithJson(SAMPLE_JSON);
        final Feature feature = features.get(1);
        page = new FeatureReportPage(reportResult, configuration, feature);

        // when
        page.generatePage();

        // then
        DocumentAssertion document = documentFrom(page.getWebPage());

        StepAssertion stepElement = document.getFeature().getElements()[0].getStepsSection().getSteps()[5];
        EmbeddingAssertion[] embeddingsElement = stepElement.getEmbedding();

        Embedding[] embeddings = feature.getElements()[0].getSteps()[5].getEmbeddings();

        assertThat(embeddingsElement).hasSameSizeAs(embeddings);
        embeddingsElement[0].hasImageContent(embeddings[0]);
        asserEmbeddingFileExist(embeddings[0]);
        embeddingsElement[2].hasTextContent(embeddings[2].getData());
        asserEmbeddingFileExist(embeddings[2]);
        embeddingsElement[3].hasTextContent(embeddings[3].getData());
        asserEmbeddingFileExist(embeddings[3]);
    }

    @Test
    public void generatePage_OnRubyFormat_ForAfterHook_generatesEmbedding() {

        // given
        setUpWithJson(SAMPLE_JSON);
        final Feature feature = features.get(1);
        page = new FeatureReportPage(reportResult, configuration, feature);

        // when
        page.generatePage();

        // then
        DocumentAssertion document = documentFrom(page.getWebPage());

        EmbeddingAssertion[] embeddingsElement = document.getFeature().getElements()[0].getAfter().getHooks()[0].getEmbedding();

        Embedding[] embeddings = feature.getElements()[0].getAfter()[0].getEmbeddings();

        assertThat(embeddingsElement).hasSameSizeAs(embeddings);
        embeddingsElement[0].hasImageContent(embeddings[0]);
        asserEmbeddingFileExist(embeddings[0]);
    }

    private static void validateHook(HookAssertion[] elements, Hook[] hooks, String hookName) {
        for (int i = 0; i < elements.length; i++) {
            BriefAssertion brief = elements[i].getBrief();
            assertThat(brief.getKeyword()).isEqualTo(hookName);
            brief.hasStatus(hooks[i].getStatus());

            if (hooks[i].getMatch() != null) {
                assertThat(brief.getName()).isEqualTo(hooks[i].getMatch().getLocation());
            }
            if (hooks[i].getResult() != null) {
                brief.hasDuration(hooks[i].getResult().getDuration());
                if (hooks[i].getResult().getErrorMessage() != null) {
                    assertThat(elements[i].getErrorMessage()).contains(hooks[i].getResult().getErrorMessage());
                }
            }
        }
    }

    private void asserEmbeddingFileExist(Embedding embedding) {
        File file = new File(configuration.getEmbeddingDirectory(), embedding.getFileName());
        assertThat(file).exists();
    }
}
