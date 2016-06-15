package org.orangesoft.behave.generators.integrations.helpers;

import static org.assertj.core.api.Assertions.assertThat;

import java.nio.charset.StandardCharsets;

import org.codehaus.plexus.util.Base64;

import org.orangesoft.behave.json.Embedding;

/**
 * @author Damian Szczepanik (damianszczepanik@github)
 */
public class EmbeddingAssertion extends ReportAssertion {

    public void hasImageContent(Embedding embedding) {
        String src = getBox().oneBySelector("img", WebAssertion.class).attr("src");
        assertThat(src).endsWith(embedding.getFileName());
    }

    public void hasTextContent(String content) {
        assertThat(getBox().oneBySelector("pre", WebAssertion.class).html()).isEqualTo(getDecodedData(content));
    }

    private WebAssertion getBox() {
        return oneByClass("embedding-content", WebAssertion.class);
    }

    private String getDecodedData(String data) {
        return new String(Base64.decodeBase64(data.getBytes(StandardCharsets.UTF_8)), StandardCharsets.UTF_8);
    }
}
