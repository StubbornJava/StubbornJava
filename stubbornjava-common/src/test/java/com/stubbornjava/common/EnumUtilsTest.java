package com.stubbornjava.common;

import static org.junit.Assert.assertEquals;

import java.util.function.Function;

import org.junit.Test;

public class EnumUtilsTest {

    @Test
    public void testFromClass() {
        Function<String, EnumTest> nameLookup = EnumUtils.lookupMap(EnumTest.class, e -> e.getName());
        Function<Integer, EnumTest> numLookup = EnumUtils.lookupMap(EnumTest.class, e -> e.getNum());

        for (EnumTest cur : EnumTest.values()) {
            assertEquals(cur, nameLookup.apply(cur.getName()));
            assertEquals(cur, numLookup.apply(cur.getNum()));
        }

        // Extra sanity
        assertEquals(EnumTest.ZERO, nameLookup.apply("zero"));
        assertEquals(EnumTest.ZERO, numLookup.apply(0));
    }

    @Test
    public void testFromValues() {
        Function<String, EnumTest> nameLookup = EnumUtils.lookupMap(EnumTest.values(), e -> e.getName());
        Function<Integer, EnumTest> numLookup = EnumUtils.lookupMap(EnumTest.values(), e -> e.getNum());

        for (EnumTest cur : EnumTest.values()) {
            assertEquals(cur, nameLookup.apply(cur.getName()));
            assertEquals(cur, numLookup.apply(cur.getNum()));
        }

        // Extra sanity
        assertEquals(EnumTest.ZERO, nameLookup.apply("zero"));
        assertEquals(EnumTest.ZERO, numLookup.apply(0));
    }

    private enum EnumTest {
        ZERO("zero", 0),
        ONE("one", 1),
        TWO("two", 2),
        ;
        private final String name;
        private final int num;
        private EnumTest(String name, int num) {
            this.name = name;
            this.num = num;
        }
        public String getName() {
            return name;
        }
        public int getNum() {
            return num;
        }
    }
}
