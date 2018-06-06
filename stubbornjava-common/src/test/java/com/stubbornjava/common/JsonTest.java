package com.stubbornjava.common;

import static org.junit.Assert.assertEquals;

import java.time.LocalDate;

import org.junit.Ignore;
import org.junit.Test;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.stubbornjava.common.Json.JsonException;

public class JsonTest {
    /*
     * No need to test every method in here since we just delegate.
     * Only test that the customizations made are working properly.
     *
     * When testing writing json we already tested reading so abuse
     * that to compare objects so we don;t have to deal with whitespace issues.
     */

    @Test
    public void jsonSerializingShouldPass() {
        // {{start:toJson}}
        Message message = new Message(LocalDate.of(2017, 1, 1), "Happy New Year!");
        String json = Json.serializer().toString(message);
        // {{end:toJson}}

        // {{start:fromJson}}
        String expectedJson = Resources.asString("json-test/full-message.json");
        Message expectedMessage = Json.serializer().fromJson(expectedJson, new TypeReference<Message>() {});
        // {{end:fromJson}}

        assertEquals(expectedMessage, Json.serializer().fromJson(json, new TypeReference<Message>() {}));
    }

    @Test
    public void parseShouldNotFailOnFullMessage() {
        String rawJson = Resources.asString("json-test/full-message.json");
        Message message = Json.serializer().fromJson(rawJson, new TypeReference<Message>() {});

        assertEquals("Happy New Year!", message.getMessage());
        assertEquals(LocalDate.of(2017, 1, 1), message.getDate());

        String actualJson = Json.serializer().toString(message);
        assertEquals(message, Json.serializer().fromJson(actualJson, new TypeReference<Message>() {}));
    }

    @Test
    public void parseShouldNotFailOnPartialMessage() {
        String rawJson = Resources.asString("json-test/partial-message.json");
        Message message = Json.serializer().fromJson(rawJson, new TypeReference<Message>() {});

        assertEquals("Partial", message.getMessage());
        assertEquals(null, message.getDate());

        String actualJson = Json.serializer().toString(message);
        assertEquals(message, Json.serializer().fromJson(actualJson, new TypeReference<Message>() {}));
    }

    @Test
    public void parseShouldNotFailOnExtraFields() {
        // {{start:fromJsonExtraFields}}
        String rawJson = Resources.asString("json-test/extra-fields.json");
        Message message = Json.serializer().fromJson(rawJson, new TypeReference<Message>() {});
        // {{end:fromJsonExtraFields}}

        assertEquals("Happy New Year!", message.getMessage());
        assertEquals(LocalDate.of(2017, 1, 1), message.getDate());

        String actualJson = Json.serializer().toString(message);
        assertEquals(message, Json.serializer().fromJson(actualJson, new TypeReference<Message>() {}));
    }

    @Ignore // apparently this is expected now
    @Test(expected=JsonException.class)
    public void parseShouldFailOnInvalidType() {
        String rawJson = Resources.asString("json-test/invalid-message.json");
        Json.serializer().fromJson(rawJson, new TypeReference<Message>() {});
    }

    /*
     * This show cases used Jackson's JsonNode.path() for traversing deep object graphs.
     */
    @Test
    public void parseShouldNotFailOnJsonNode() {
        // {{start:fromJsonNode}}
        String rawJson = Resources.asString("json-test/nested.json");
        JsonNode node = Json.serializer()
                            .nodeFromJson(rawJson)
                            .path("nested1")
                            .path("nested2")
                            .path("nested3");
        Message message = Json.serializer().fromNode(node, new TypeReference<Message>() {});
        // {{end:fromJsonNode}}

        assertEquals("Nested!", message.getMessage());
        assertEquals(null, message.getDate());

        String actualJson = Json.serializer().toString(message);
        assertEquals(message, Json.serializer().fromJson(actualJson, new TypeReference<Message>() {}));
    }

    // {{start:pojo}}
    private static class Message {
        private final LocalDate date;
        private final String message;

        public Message(@JsonProperty("date") LocalDate date, @JsonProperty("message") String message) {
            super();
            this.date = date;
            this.message = message;
        }
        public LocalDate getDate() {
            return date;
        }
        public String getMessage() {
            return message;
        }
        // {{end:pojo}}
        @Override
        public int hashCode() {
            final int prime = 31;
            int result = 1;
            result = prime * result + ((date == null) ? 0 : date.hashCode());
            result = prime * result + ((message == null) ? 0 : message.hashCode());
            return result;
        }
        @Override
        public boolean equals(Object obj) {
            if (this == obj)
                return true;
            if (obj == null)
                return false;
            if (getClass() != obj.getClass())
                return false;
            Message other = (Message) obj;
            if (date == null) {
                if (other.date != null)
                    return false;
            } else if (!date.equals(other.date))
                return false;
            if (message == null) {
                if (other.message != null)
                    return false;
            } else if (!message.equals(other.message))
                return false;
            return true;
        }
    }
}
