package org.orangesoft.behave.json;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import org.orangesoft.behave.json.deserializers.OutputDeserializer;

/**
 * @author Damian Szczepanik (damianszczepanik@github)
 */
@JsonDeserialize(using = OutputDeserializer.class)
public class Output {

    private final String[] messages;

    public Output(String[] messages) {
        this.messages = messages;
    }

    public String[] getMessages() {
        return messages;
    }
}
