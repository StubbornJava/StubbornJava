package com.stubbornjava.common;

import static org.junit.Assert.assertEquals;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.jooq.lambda.Seq;
import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.stubbornjava.common.DeterministicObjectMapper.CustomComparators;

// {{start:test}}
public class DeterministicObjectMapperTest {

    private ObjectMapper mapper;

    @Before
    public void setup() {
        CustomComparators customComparators = new DeterministicObjectMapper.CustomComparators();
        mapper = DeterministicObjectMapper.create(Json.serializer().mapper(), customComparators);
    }

    @Test
    public void testDeterministicSetInts() throws JsonProcessingException
    {
        Set<Integer> ints = Sets.newLinkedHashSet(Lists.newArrayList(1, 3, 2));
        String actual = mapper.writer().writeValueAsString(ints);
        String expected = "[1,2,3]";
        assertEquals(expected, actual);
    }

    @Test
    public void testDeterministicSetStrings() throws JsonProcessingException
    {
        Set<String> strings = Sets.newLinkedHashSet(Lists.newArrayList("a", "c", "b", "aa", "cc", "bb"));
        String actual = mapper.writer().writeValueAsString(strings);
        String expected = "[\"a\",\"aa\",\"b\",\"bb\",\"c\",\"cc\"]";
        assertEquals(expected, actual);
    }

    @Test
    public void testHeterogeneousList() throws JsonProcessingException
    {
        List<Object> strings = Lists.newArrayList("a", 1, "b", "c", 2);
        String actual = mapper.writer().writeValueAsString(strings);
        String expected = "[1,2,\"a\",\"b\",\"c\"]";
        assertEquals(expected, actual);
    }

    @Test
    public void testDeterministicFieldOrder() throws JsonProcessingException
    {
        @SuppressWarnings("unused")
        Object data = new Object() {
            public String get1() { return "1"; }
            public String getC() { return "C"; }
            public String getA() { return "A"; }
        };

        String actual = mapper.writer().writeValueAsString(data);
        String expected = "{\"1\":\"1\",\"a\":\"A\",\"c\":\"C\"}";
        assertEquals(expected, actual);
    }

    @Test
    public void testDeterministicMapKeyOrder() throws JsonProcessingException
    {
        Map<String, String> data = Maps.newLinkedHashMap();
        data.put("1", "1");
        data.put("a", "A");
        data.put("c", "C");

        String actual = mapper.writer().writeValueAsString(data);
        String expected = "{\"1\":\"1\",\"a\":\"A\",\"c\":\"C\"}";
        assertEquals(expected, actual);
    }

    @Test(expected=JsonMappingException.class)
    public void testCustomComparatorFails() throws JsonProcessingException {
        Set<MyObject> objects = Seq.of(
            new MyObject(2),
            new MyObject(4),
            new MyObject(3),
            new MyObject(1)
        ).toSet(Sets::newHashSet);

        mapper.writer().writeValueAsString(objects);
    }

    @Test
    public void testCustomComparatorPasses() throws JsonProcessingException {
        CustomComparators comparators = new DeterministicObjectMapper.CustomComparators();
        comparators.addConverter(MyObject.class, Comparator.comparing(MyObject::getX));
        ObjectMapper customizedComparatorsMapper = DeterministicObjectMapper.create(Json.serializer().mapper(), comparators);
        Set<MyObject> objects = Seq.of(
            new MyObject(2),
            new MyObject(4),
            new MyObject(3),
            new MyObject(1)
        ).toSet();

        String actual = customizedComparatorsMapper.writer().writeValueAsString(objects);
        String expected = "[{\"x\":1},{\"x\":2},{\"x\":3},{\"x\":4}]";
        assertEquals(expected, actual);
    }

    @Test
    public void testDeterministicNesting() throws JsonProcessingException
    {
        @SuppressWarnings("unused")
        Object obj = new Object() {
            public String get1() { return "1"; }
            public String getC() { return "C"; }
            public String getA() { return "A"; }
        };

        Set<Integer> ints = Sets.newLinkedHashSet(Lists.newArrayList(1, 4, 2, 3, 5, 7, 8, 9, 6, 0, 50, 100, 99));

        Map<String, Object> data = Maps.newLinkedHashMap();
        data.put("obj", obj);
        data.put("c", "C");
        data.put("ints", ints);

        String actual = mapper.writer().writeValueAsString(data);
        String expected = "{" +
                            "\"c\":\"C\"," +
                            "\"ints\":[0,1,2,3,4,5,6,7,8,9,50,99,100]," +
                            "\"obj\":{\"1\":\"1\",\"a\":\"A\",\"c\":\"C\"}" +
                          "}";
        assertEquals(expected, actual);
    }

    private static class MyObject {
        private final int x;

        public MyObject(int x) {
            this.x = x;
        }

        public int getX() {
            return x;
        }
    }
}

// {{end:test}}

