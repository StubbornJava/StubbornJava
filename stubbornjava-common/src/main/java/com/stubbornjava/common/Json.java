package com.stubbornjava.common;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.codahale.metrics.json.MetricsModule;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

public class Json {
    private static final Logger logger = LoggerFactory.getLogger(Json.class);

    // {{start:setup}}
    private static final Json DEFAULT_SERIALIZER;
    static {
        ObjectMapper mapper = new ObjectMapper();

        // Don't throw an exception when json has extra fields you are
        // not serializing on. This is useful when you want to use a pojo
        // for deserialization and only care about a portion of the json
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        // Ignore null values when writing json.
        mapper.configure(SerializationFeature.WRITE_NULL_MAP_VALUES, false);
        mapper.setSerializationInclusion(Include.NON_NULL);

        // Write times as a String instead of a Long so its human readable.
        mapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
        mapper.registerModule(new JavaTimeModule());

        // Custom serializer for coda hale metrics.
        mapper.registerModule(new MetricsModule(TimeUnit.MINUTES, TimeUnit.MILLISECONDS, false));

        DEFAULT_SERIALIZER = new Json(mapper);
    }
    // {{end:setup}}

    public static Json serializer() {
        return DEFAULT_SERIALIZER;
    }

    private final ObjectMapper mapper;
    private final ObjectWriter writer;
    private final ObjectWriter prettyWriter;

    // Only let this be called statically. Hide the constructor
    private Json(ObjectMapper mapper) {
        this.mapper = mapper;
        this.writer = mapper.writer();
        this.prettyWriter = mapper.writerWithDefaultPrettyPrinter();
    }

    public ObjectMapper mapper() {
        return mapper;
    }

    public ObjectWriter writer() {
        return writer;
    }

    public ObjectWriter prettyWriter() {
        return prettyWriter;
    }

    // {{start:fromBytes}}
    public <T> T fromJson(byte[] bytes, TypeReference<T> typeRef) {
        try {
            return mapper.readValue(bytes, typeRef);
        } catch (IOException e) {
            throw new JsonException(e);
        }
    }
    // {{end:fromBytes}}

    // {{start:readJson}}
    public <T> T fromJson(String json, TypeReference<T> typeRef) {
        try {
            return mapper.readValue(json, typeRef);
        } catch (IOException e) {
            throw new JsonException(e);
        }
    }
    // {{end:readJson}}

    // {{start:fromNode}}
    public <T> T fromNode(JsonNode node, TypeReference<T> typeRef) {
        try {
            return mapper.readValue(node.toString(), typeRef);
        } catch (IOException e) {
            throw new JsonException(e);
        }
    }
    // {{end:fromNode}}

    public <T> T fromObject(Object obj, TypeReference<T> typeRef) {
        try {
            return mapper.readValue(toString(obj), typeRef);
        } catch (IOException e) {
            throw new JsonException(e);
        }
    }

    // {{start:fromInputStream}}
    public <T> T fromInputStream(InputStream is, TypeReference<T> typeRef) {
        try {
            return mapper.readValue(is, typeRef);
        } catch (IOException e) {
            throw new JsonException(e);
        }
    }
    // {{end:fromInputStream}}

    // {{start:writeJson}}
    public String toString(Object obj) {
        try {
            return writer.writeValueAsString(obj);
        } catch (IOException e) {
            throw new JsonException(e);
        }
    }
    // {{end:writeJson}}

    // {{start:toPrettyString}}
    public String toPrettyString(Object obj) {
        try {
            return prettyWriter.writeValueAsString(obj);
        } catch (IOException e) {
            throw new JsonException(e);
        }
    }
    // {{end:toPrettyString}}

    // {{start:toByteArray}}
    public byte[] toByteArray(Object obj) {
        try {
            return prettyWriter.writeValueAsBytes(obj);
        } catch (IOException e) {
            throw new JsonException(e);
        }
    }
    // {{end:toByteArray}}

    public Map<String, Object> mapFromJson(byte[] bytes) {
        try {
            return mapper.readValue(bytes, new TypeReference<Map<String, Object>>() {
            });
        } catch (IOException e) {
            throw new JsonException(e);
        }
    }

    public Map<String, Object> mapFromJson(String json) {
        try {
            return mapper.readValue(json, new TypeReference<Map<String, Object>>() {
            });
        } catch (IOException e) {
            throw new JsonException(e);
        }
    }

    // {{start:jsonNode}}
    public JsonNode nodeFromJson(String json) {
        try {
            return mapper.readTree(json);
        } catch (IOException e) {
            throw new JsonException(e);
        }
    }
    // {{end:jsonNode}}

    public JsonNode nodeFromObject(Object obj) {
        try {
            return mapper.readTree(toString(obj));
        } catch (IOException e) {
            throw new JsonException(e);
        }
    }

    public static class JsonException extends RuntimeException {
        private JsonException(Exception ex) {
            super(ex);
        }
    }
}
