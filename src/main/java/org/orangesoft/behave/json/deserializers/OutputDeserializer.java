package org.orangesoft.behave.json.deserializers;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;

import org.orangesoft.behave.Configuration;
import org.orangesoft.behave.json.Output;

/**
 * Deserialize output messages depends supporting single and bi-dimensional arrays (JVM and Ruby implementations).
 * 
 * @author Damian Szczepanik (damianszczepanik@github)
 */
public class OutputDeserializer extends BehaveJsonDeserializer<Output> {

    @Override
    public Output deserialize(JsonParser parser, Configuration configuration)
            throws IOException, JsonProcessingException {
        JsonNode rootNode = parser.getCodec().readTree(parser);
        List<String> list = parseOutput(rootNode);

        return new Output(list.toArray(new String[list.size()]));
    }

    /**
     * Converts passed node to list of strings. It supports single node and an array working as recursive method.
     * 
     * @param node
     *            node that should be parsed
     * @return extracted strings
     */
    private List<String> parseOutput(JsonNode node) {
        List<String> list = new ArrayList<>();

        if (node.isArray()) {
            for (JsonNode innerNode : node) {
                list.addAll(parseOutput(innerNode));
            }
        } else {
            list.add(node.asText());
        }

        return list;
    }
}
