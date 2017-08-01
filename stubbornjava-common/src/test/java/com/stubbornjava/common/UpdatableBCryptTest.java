package com.stubbornjava.common;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class UpdatableBCryptTest {

    @Test
    public void testHash() {
        UpdatableBCrypt bcrypt = new UpdatableBCrypt(11);
        String hash = bcrypt.hash("password");

        assertTrue(bcrypt.verifyHash("password", hash));
    }

    @Test
    public void testVerifyAndUpdateDoesNotUpdate() {
        UpdatableBCrypt bcrypt11 = new UpdatableBCrypt(10);
        String hash11 = bcrypt11.hash("password");
        assertTrue(bcrypt11.verifyHash("password", hash11));

        UpdatableBCrypt bcrypt12 = new UpdatableBCrypt(10);

        String[] hash12 = new String[1];
        bcrypt12.verifyAndUpdateHash("password", hash11, newHash -> {
            hash12[0] = newHash;
            return true;
        });
        assertTrue(null == hash12[0]);
    }

    @Test
    public void testVerifyAndUpdateChangesHash() {
        UpdatableBCrypt bcrypt11 = new UpdatableBCrypt(10);
        String hash11 = bcrypt11.hash("password");
        assertTrue(bcrypt11.verifyHash("password", hash11));

        UpdatableBCrypt bcrypt12 = new UpdatableBCrypt(11);

        String[] hash12 = new String[1];
        boolean result = bcrypt12.verifyAndUpdateHash("password", hash11, newHash -> {
            hash12[0] = newHash;
            return true;
        });
        assertTrue(result);
        assertTrue(null != hash12[0]);
        assertTrue(bcrypt12.verifyHash("password", hash12[0]));

        // Hash shouldn't change again
        String finalHash = hash12[0];
        result = bcrypt12.verifyAndUpdateHash("password", finalHash, newHash -> {
            hash12[0] = newHash;
            return true;
        });
        assertTrue(result);
        assertTrue(null != finalHash);
        assertEquals(finalHash, hash12[0]);
        assertTrue(bcrypt12.verifyHash("password", finalHash));
    }

    @Test
    public void testVerifyAndUpdateChangesHashSkippingRounds() {
        UpdatableBCrypt bcrypt11 = new UpdatableBCrypt(5);
        String hash11 = bcrypt11.hash("password");
        assertTrue(bcrypt11.verifyHash("password", hash11));

        UpdatableBCrypt bcrypt12 = new UpdatableBCrypt(13);

        String[] hash12 = new String[1];
        boolean result = bcrypt12.verifyAndUpdateHash("password", hash11, newHash -> {
            hash12[0] = newHash;
            return true;
        });
        assertTrue(result);
        assertTrue(null != hash12[0]);
        assertTrue(bcrypt12.verifyHash("password", hash12[0]));
    }

    @Test
    public void testVerifyAndUpdateFailsOnBadPW() {
        UpdatableBCrypt bcrypt11 = new UpdatableBCrypt(10);
        String hash11 = bcrypt11.hash("password");
        assertTrue(bcrypt11.verifyHash("password", hash11));

        UpdatableBCrypt bcrypt12 = new UpdatableBCrypt(11);

        String[] hash12 = new String[1];
        boolean result = bcrypt12.verifyAndUpdateHash("password1", hash11, newHash -> {
            hash12[0] = newHash;
            return true;
        });
        assertFalse(result);
        assertTrue(null == hash12[0]);
    }
}
