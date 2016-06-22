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
    public void generatePage_generatesTitle() throws Exception {

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
    public void generatePage_generatesStatsTableBody()  throws Exception {

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

        odyRow.hasExactValues(feature.getName(), "1", "1", "0", "7", "7", "0", "0", "0", "0", "0", "460ms", "Passed");
        odyRow.hasExactCSSClasses("tagname", "", "", "", "", "", "", "", "", "", "", "duration", "passed");
    }

    @Test
    public void generatePage_generatesFeatureDetails()  throws Exception {

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
        testTags[0].getLink().hasLabelAndAddress("$tag.getName()", "$tag.getFileName()");

        assertThat(featureDetails.getDescription()).isEqualTo(feature.getDescription());
    }

    public void generatePage_generatesScenarioDetails() throws Exception {

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
            testTags[i].getLink().hasLabelAndAddress(scenario.getTags()[i], scenario.getTags()[i]);
        }

        BriefAssertion brief = firstElement.getBrief();
        assertThat(brief.getKeyword()).isEqualTo(scenario.getKeyword());
        assertThat(brief.getName()).isEqualTo(scenario.getName());
        brief.hasStatus(scenario.getElementStatus());

        assertThat(firstElement.getDescription()).isEqualTo(scenario.getDescription());
    }

   
    public void generatePage_generatesHooks() throws Exception {

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

    
    public void generatePage_generatesSteps()  throws Exception {

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

    public void generatePage_OnBiDimentionalArray_generatesOutput() throws Exception {

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

    public void generatePage_OnSingleArray_generatesOutput() throws Exception {

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

    public void generatePage_generatesArguments() throws Exception {

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

    public void generatePage_generatesDocString() throws Exception {

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

   
    public void generatePage_generatesEmbedding() throws Exception {

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

   
    public void generatePage_OnRubyFormat_ForAfterHook_generatesEmbedding()  throws Exception {

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
