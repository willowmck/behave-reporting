package org.orangesoft.behave.generators;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.apache.velocity.VelocityContext;
import org.junit.Before;
import org.junit.Test;

import mockit.Deencapsulation;
import org.orangesoft.behave.generators.integrations.PageTest;
import org.orangesoft.behave.json.support.TagObject;

/**
 * @author Damian Szczepanik (damianszczepanik@github)
 */
public class TagsOverviewPageTest extends PageTest {

    @Before
    public void setUp()  throws Exception{
        setUpWithJson(SAMPLE_JSON);
    }

    @Test
    public void getWebPage_ReturnsFeatureFileName() {

        // give
        page = new TagsOverviewPage(reportResult, configuration);

        // when
        String fileName = page.getWebPage();

        // then
        assertThat(fileName).isEqualTo("tag-overview.html");
    }

    @Test
    public void prepareReportAddsCustomProperties() {

        // give
        page = new TagsOverviewPage(reportResult, configuration);

        // when
        page.prepareReport();

        // then
        VelocityContext context = Deencapsulation.getField(page, "context");
        assertThat(context.getKeys()).hasSize(9);

        assertThat(context.get("all_tags")).isEqualTo(tags);
        assertThat(context.get("report_summary")).isEqualTo(reportResult.getTagReport());
        assertThat(context.get("chart_categories")).isEqualTo(TagsOverviewPage.generateTagLabels(tags));
        assertThat(context.get("chart_data")).isEqualTo(TagsOverviewPage.generateTagValues(tags));
    }

    @Test
    public void generateTagLabels_ReturnsTags() {

        // give
        List<TagObject> allTags = this.tags;

        // when
        String labels = TagsOverviewPage.generateTagLabels(allTags);

        // then
        assertThat(labels).isEqualTo("[\"@checkout\",\"@slow\"]");
    }
}
