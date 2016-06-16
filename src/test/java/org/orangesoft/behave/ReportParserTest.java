package org.orangesoft.behave;

import static org.hamcrest.core.StringContains.containsString;

import java.io.IOException;
import java.util.List;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import org.orangesoft.behave.json.Feature;
import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Damian Szczepanik (damianszczepanik@github)
 */
public class ReportParserTest extends ReportGenerator {

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Test
    public void parseJsonResults_ReturnsParsedFeatureFiles() throws IOException {

        // given
        setUpWithJson(SAMPLE_JSON, SIMPLE_JSON);

        // when
        List<Feature> testFeatures = new ReportParser(configuration).parseJsonResults(jsonReports);

        // then
        assertThat(testFeatures).hasSize(3);
    }

    @Test
    public void parseJsonResults_OnInvalidJSON_SkipFiles() throws IOException {

        // given
        setUpWithJson(INVALID_JSON, SIMPLE_JSON);

        // when
        List<Feature> testFeatures = new ReportParser(configuration).parseJsonResults(jsonReports);

        // then
        assertThat(testFeatures).hasSize(1);
    }

    @Test
    public void parseJsonResults_OnEmptyJSON_SkipFiles() throws IOException {

        // given
        setUpWithJson(EMPTY_JSON);

        // when
        List<Feature> testFeatures = new ReportParser(configuration).parseJsonResults(jsonReports);

        // then
        assertThat(testFeatures).isEmpty();
    }

    @Test
    public void parseJsonResults_OnInvalidReport_SkipsFiles() throws IOException {

        // given
        setUpWithJson(INVALID_REPORT_JSON, SIMPLE_JSON);

        // when
        List<Feature> testFeatures = new ReportParser(configuration).parseJsonResults(jsonReports);

        // then
        assertThat(testFeatures).hasSize(1);
    }

    @Test
    public void parseJsonResultsFails_OnNoExistingFile_ThrowsException() throws IOException {

        // given
        final String invalidFile = "?no-existing%file.json";
        setUpWithJson(EMPTY_JSON);
        jsonReports.add(invalidFile);

        // then
        thrown.expect(ValidationException.class);
        thrown.expectMessage(containsString(invalidFile));
        new ReportParser(configuration).parseJsonResults(jsonReports);
    }
}
