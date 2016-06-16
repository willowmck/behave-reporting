package org.orangesoft.behave.json;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;

import org.orangesoft.behave.generators.integrations.PageTest;

/**
 * @author Damian Szczepanik (damianszczepanik@github)
 */
public class OutputTest extends PageTest {

    @Test
    public void getMessages_ReturnsMessages() {

        // give
        String[] messages = { "a", "b", "c", "a" };

        // when
        Output output = new Output(messages);

        // then
        assertThat(output.getMessages()).containsExactly(messages);
    }

}
