package com.stubbornjava.common.db;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.List;

import org.jooq.Configuration;
import org.junit.Test;

public class DSLsTest {

    @Test(expected=NullPointerException.class)
    public void testAnyThrowsWhenNotInitialized() {
        DSLs.any();
    }

    @Test
    public void testThreadLocalConfigsAreIdentical() {
        DSLs.processing().newTransaction(() -> {
            Configuration ctx1 = DSLs.any().configuration();
            Configuration ctx2 = DSLs.processing().get().configuration();
            Configuration ctx3 = DSLs.processing().get().configuration();
            assertTrue("any() config and processing() config should be identical", ctx1 == ctx2);
            assertTrue("processing() config should be identical when called twice", ctx2 == ctx3);
        });
    }

    @Test(expected=IllegalStateException.class)
    public void testNamedTransactionFailsOnWrongName() {
        DSLs.processing().newTransaction(() -> {
            Configuration ctx1 = DSLs.any().configuration();
            Configuration ctx2 = DSLs.transactional().get().configuration();
            fail("We should not have a transactional Configuration in our thread local scope.");
        });
    }

    @Test
    public void testDSLWorks() {
        DSLs.transactional().newTransaction(() -> {
            DSLs.any().execute("DROP TABLE IF EXISTS numbers;");
            DSLs.any().execute("CREATE TABLE numbers (number int NOT NULL);");
            int count = DSLs.any().fetchOne("select count(*) from numbers").into(Integer.class);
            assertEquals(0, count);
            DSLs.any().execute("INSERT INTO numbers VALUES (1),(2),(3);");
            count = DSLs.any().fetchOne("select count(*) from numbers").into(Integer.class);
            assertEquals(3, count);
        });
    }

    @Test
    public void testRollbackWorks() {
        DSLs.transactional().newTransaction(() -> {
            DSLs.any().execute("DROP TABLE IF EXISTS numbers;");
            DSLs.any().execute("CREATE TABLE numbers (number int NOT NULL);");
            int count = DSLs.any().fetchOne("select count(*) from numbers").into(Integer.class);
            assertEquals(0, count);
            DSLs.any().execute("INSERT INTO numbers VALUES (1);");
            count = DSLs.any().fetchOne("select count(*) from numbers").into(Integer.class);
            assertEquals(1, count);
        });

        try {
            DSLs.transactional().newTransaction(() -> {
                int count = DSLs.any().fetchOne("select count(*) from numbers").into(Integer.class);
                assertEquals(1, count);
                DSLs.any().execute("INSERT INTO numbers VALUES (2),(3);");
                count = DSLs.any().fetchOne("select count(*) from numbers").into(Integer.class);
                assertEquals(3, count);
                throw new RollbackTestException("boom");
            });
        } catch (RollbackTestException ex) {
            // Do nothing
        }

        DSLs.transactional().newTransaction(() -> {
            int count = DSLs.any().fetchOne("select count(*) from numbers").into(Integer.class);
            assertEquals(1, count);
        });
    }

    @Test
    public void testNestedRollbackWorks() {
        DSLs.transactional().newTransaction(() -> {
            DSLs.any().execute("DROP TABLE IF EXISTS numbers;");
            DSLs.any().execute("CREATE TABLE numbers (number int NOT NULL);");
            int count = DSLs.any().fetchOne("select count(*) from numbers").into(Integer.class);
            assertEquals(0, count);
            DSLs.any().execute("INSERT INTO numbers VALUES (1);");
            count = DSLs.any().fetchOne("select count(*) from numbers").into(Integer.class);
            assertEquals(1, count);

            // This whole block should be rolled back
            try {
                DSLs.any().transaction((ctx) -> {
                    DSLs.any().execute("INSERT INTO numbers VALUES (2);");
                    int newCount = DSLs.any().fetchOne("select count(*) from numbers").into(Integer.class);
                    assertEquals(2, newCount);
                    DSLs.any().transaction((ctx2) -> {
                        DSLs.any().execute("INSERT INTO numbers VALUES (3);");
                        int nestedCount = DSLs.any().fetchOne("select count(*) from numbers").into(Integer.class);
                        assertEquals(3, nestedCount);
                    });
                    throw new RollbackTestException("boom");
                });
            } catch (RollbackTestException ex) {
                // Do nothing
            }

            DSLs.any().transaction((ctx) -> {
                DSLs.any().execute("INSERT INTO numbers VALUES (4);");
                int newCount = DSLs.any().fetchOne("select count(*) from numbers").into(Integer.class);
                assertEquals(2, newCount);
            });

            count = DSLs.any().fetchOne("select count(*) from numbers").into(Integer.class);
            assertEquals(2, count);
        });

        DSLs.transactional().newTransaction(() -> {
            int count = DSLs.any().fetchOne("select count(*) from numbers").into(Integer.class);
            assertEquals(2, count);
            List<Integer> nums = DSLs.any().fetch("select * from numbers order by number").into(Integer.class);
            assertTrue(nums.get(0).equals(1));
            assertTrue(nums.get(1).equals(4));
        });
    }

    private static class RollbackTestException extends RuntimeException {
        public RollbackTestException(String message) {
            super(message);
        }
    }
}
