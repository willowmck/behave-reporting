package org.orangesoft.behave.generators.integrations.helpers;

import org.orangesoft.behave.json.DocString;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Fang Yuan (fayndee@github)
 */
public class DocStringAssertion extends ReportAssertion {

    public void hasDocString(DocString docString) {
        assertThat(docString).isNotNull();
        assertThat(oneBySelector("pre", WebAssertion.class).text()).isEqualTo(docString.getValue());
    }
}
