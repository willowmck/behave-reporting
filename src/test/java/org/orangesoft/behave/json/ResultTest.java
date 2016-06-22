package org.orangesoft.behave.json;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Before;
import org.junit.Test;

import org.orangesoft.behave.generators.integrations.PageTest;
import org.orangesoft.behave.json.support.Status;

/**
 * @author Damian Szczepanik (damianszczepanik@github)
 */
public class ResultTest extends PageTest {

    @Before
    public void setUp() throws Exception{
        setUpWithJson(SAMPLE_JSON);
    }

    @Test
    public void getStatus_ReturnsStatus() {

        // give
        Result result = features.get(0).getElements()[0].getSteps()[1].getResult();

        // when
        String status = result.getStatus();

        // then
        assertThat(status).isEqualTo(Status.PASSED.getRawName());
    }

    @Test
    public void getDuration_ReturnsDuration() {

        // give
        Result result = features.get(0).getElements()[0].getSteps()[3].getResult();

        // when
        long duration = result.getDuration();

        // then
        assertThat(duration).isEqualTo(0L);
    }

    @Test
    public void getFormattedDuration_ReturnsDurationAsString() {

        // give
        Result result = features.get(0).getElements()[0].getSteps()[2].getResult();

        // when
        String duration = result.getFormatedDuration();

        // then
        assertThat(duration).isEqualTo("300ms");
    }

    
    public void getErrorMessage_ReturnsMessage() {

        // give
        Result result = features.get(0).getElements()[1].getAfter()[0].getResult();

        // when
        String message = result.getErrorMessage();

        // then
        assertThat(message).isEqualTo("Completed");
    }

}