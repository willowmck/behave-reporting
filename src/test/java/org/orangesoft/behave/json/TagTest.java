package org.orangesoft.behave.json;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Before;
import org.junit.Test;

import org.orangesoft.behave.generators.integrations.PageTest;

/**
 * @author Damian Szczepanik (damianszczepanik@github)
 */
public class TagTest extends PageTest {

    @Before
    public void setUp() throws Exception  {
        setUpWithJson(SAMPLE_JSON);
    }

   
    public void getName_ReturnsFeatureTagName() {

        // give
        String tag = features.get(0).getTags()[0];

        // when
        String tagName = tag;

        // then
        assertThat(tagName).isEqualTo("@featureTag");
    }

   
    public void getName_ReturnsElementTagName() {

        // give
        String tag = features.get(0).getElements()[1].getTags()[2];

        // when
        String tagName = tag;

        // then
        assertThat(tagName).isEqualTo("@checkout");
    }

   
    public void getFileName_ReturnsTagFileName() {

        // give
        String tag = features.get(1).getElements()[0].getTags()[0];

        // when
        String fileName = tag;

        // then
        assertThat(fileName).isEqualTo("checkout.html");
    }
}