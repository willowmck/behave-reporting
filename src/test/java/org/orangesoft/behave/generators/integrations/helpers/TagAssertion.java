package org.orangesoft.behave.generators.integrations.helpers;

/**
 * @author Damian Szczepanik (damianszczepanik@github)
 */
public class TagAssertion extends ReportAssertion {

    public static TagAssertion[] getTags(WebAssertion report) {
        return report.childByClass("tags", WebAssertion.class).allBySelector("a", TagAssertion.class);
    }
}
