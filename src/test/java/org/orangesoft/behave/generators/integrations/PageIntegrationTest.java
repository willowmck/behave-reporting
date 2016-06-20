package org.orangesoft.behave.generators.integrations;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;

import org.orangesoft.behave.generators.FeaturesOverviewPage;
import org.orangesoft.behave.generators.StepsOverviewPage;
import org.orangesoft.behave.generators.TagReportPage;
import org.orangesoft.behave.generators.integrations.helpers.BuildInfoAssertion;
import org.orangesoft.behave.generators.integrations.helpers.DocumentAssertion;
import org.orangesoft.behave.generators.integrations.helpers.LinkAssertion;
import org.orangesoft.behave.generators.integrations.helpers.NavigationAssertion;
import org.orangesoft.behave.generators.integrations.helpers.NavigationItemAssertion;
import org.orangesoft.behave.generators.integrations.helpers.TableRowAssertion;
import org.orangesoft.behave.generators.integrations.helpers.WebAssertion;
import org.junit.Before;
import java.util.Locale;

/**
 * @author Damian Szczepanik (damianszczepanik@github)
 */
public class PageIntegrationTest extends PageTest {

    @Before
    public void prepare() {
        Locale.setDefault(Locale.UK);
    }
    @Test
    public void generatePage_onDefaultConfiguration_generatesDefaultItemsInNaviBarfor()  throws Exception{

        // given
        setUpWithJson(SAMPLE_JSON);
        page = new FeaturesOverviewPage(reportResult, configuration);

        // when
        page.generatePage();

        // then
        DocumentAssertion document = documentFrom(page.getWebPage());
        NavigationAssertion navigation = document.getNavigation();
        NavigationItemAssertion[] menuItems = navigation.getNaviBarLinks();

        navigation.hasPluginName();
        assertThat(menuItems).hasSize(4);

        menuItems[0].hasLinkToFeatures();
        menuItems[1].hasLinkToTags();
        menuItems[2].hasLinkToSteps();
        menuItems[3].hasLinkToFailures();
    }

    @Test
    public void generatePage_onJenkinsConfiguration_generatesAllItemsInNaviBarfor() throws Exception {

        // given
        setUpWithJson(SAMPLE_JSON);
        configuration.setRunWithJenkins(true);
        configuration.setBuildNumber("123");

        page = new TagReportPage(reportResult, configuration, reportResult.getAllTags().get(0));

        // when
        page.generatePage();

        // then
        DocumentAssertion document = documentFrom(page.getWebPage());
        NavigationAssertion navigation = document.getNavigation();
        NavigationItemAssertion[] menuItems = navigation.getNaviBarLinks();

        navigation.hasPluginName();
        assertThat(navigation.getNaviBarLinks()).hasSize(7);

        menuItems[0].hasLinkToJenkins(configuration);
        menuItems[1].hasLinkToPreviousResult(configuration, page.getWebPage());
        menuItems[2].hasLinkToLastResult(configuration, page.getWebPage());

        menuItems[3].hasLinkToFeatures();
        menuItems[4].hasLinkToTags();
        menuItems[5].hasLinkToSteps();
        menuItems[6].hasLinkToFailures();
    }

    @Test
    public void generatePage_onDefaultConfiguration_generatesSummaryTable() throws Exception {

        // given
        setUpWithJson(SAMPLE_JSON);
        page = new StepsOverviewPage(reportResult, configuration);

        // when
        page.generatePage();

        // then
        DocumentAssertion document = documentFrom(page.getWebPage());
        BuildInfoAssertion buildInfo = document.getBuildInfo();

        TableRowAssertion headValues = buildInfo.getHeaderRow();
        headValues.hasExactValues("Project", "Date");

        assertThat(buildInfo.getProjectName()).isEqualTo(configuration.getProjectName());
        buildInfo.hasBuildDate(false);
    }

    @Test
    public void generatePage_onJenkinsConfiguration_generatesSummaryTableWithBuildNumber() throws Exception {

        // given
        setUpWithJson(SAMPLE_JSON);
        configuration.setRunWithJenkins(true);
        configuration.setBuildNumber("123");

        page = new StepsOverviewPage(reportResult, configuration);

        // when
        page.generatePage();

        // then
        DocumentAssertion document = documentFrom(page.getWebPage());
        BuildInfoAssertion buildInfo = document.getBuildInfo();

        TableRowAssertion headValues = buildInfo.getHeaderRow();
        headValues.hasExactValues("Project", "Number", "Date");

        assertThat(buildInfo.getProjectName()).isEqualTo(configuration.getProjectName());
        assertThat(buildInfo.getBuildNumber()).isEqualTo(configuration.getBuildNumber());
        buildInfo.hasBuildDate(true);
    }

    @Test
    public void generatePage_generatesFooter()  throws Exception{

        // given
        setUpWithJson(SAMPLE_JSON);
        page = new TagReportPage(reportResult, configuration, reportResult.getAllTags().get(0));

        // when
        page.generatePage();

        // then
        DocumentAssertion document = documentFrom(page.getWebPage());
        WebAssertion footer = extractFooter(document);
        LinkAssertion[] footerLinks = extractFooterLinks(footer);

        assertThat(footerLinks).hasSize(2);
        footerLinks[0].hasLabelAndAddress("Jenkins Plugin", "https://github.com/jenkinsci/cucumber-reports-plugin");
        footerLinks[1].hasLabelAndAddress("Cucumber-JVM Reports", "https://github.com/damianszczepanik/cucumber-reporting");
    }

    private WebAssertion extractFooter(WebAssertion document) {
        return document.byId("footer", WebAssertion.class);
    }

    private LinkAssertion[] extractFooterLinks(WebAssertion footer) {
        return footer.allBySelector("a", LinkAssertion.class);
    }

}
