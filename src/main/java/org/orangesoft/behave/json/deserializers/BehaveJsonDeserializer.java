package org.orangesoft.behave.json.deserializers;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

import org.orangesoft.behave.Configuration;

/**
 * Abstract deserializer that extracts {@link Configuration} and passes to
 * {@link #deserialize(JsonParser, Configuration)}.
 * 
 * @author Damian Szczepanik (damianszczepanik@github)
 */
abstract class BehaveJsonDeserializer<T> extends JsonDeserializer<T> {

    @Override
    public T deserialize(JsonParser parser, DeserializationContext context)
            throws IOException, JsonProcessingException {
        Configuration configuration = (Configuration) context.findInjectableValue(Configuration.class.getName(), null,
                null);
        return deserialize(parser, configuration);
    }

    protected abstract T deserialize(JsonParser parser, Configuration configuration)
            throws IOException, JsonProcessingException;
}
