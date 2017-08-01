package com.stubbornjava.common;

import java.util.function.Function;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Hashing {
    private static final Logger log = LoggerFactory.getLogger(Hashing.class);

    // {{start:bcrypt}}
    // This should be updated every year or two.
    private static final UpdatableBCrypt bcrypt = new UpdatableBCrypt(11);

    public static String hash(String password) {
        return bcrypt.hash(password);
    }

    public static boolean verifyAndUpdateHash(String password, String hash, Function<String, Boolean> updateFunc) {
        return bcrypt.verifyAndUpdateHash(password, hash, updateFunc);
    }
    // {{end:bcrypt}}

    public static void main(String[] args) {
        // {{start:bcryptMain}}
        // Mini function to test updates.
        String[] mutableHash = new String[1];
        Function<String, Boolean> update = hash -> { mutableHash[0] = hash; return true; };

        String hashPw1 = Hashing.hash("password");
        log.debug("hash of pw1: {}", hashPw1);
        log.debug("verifying pw1: {}", Hashing.verifyAndUpdateHash("password", hashPw1, update));
        log.debug("verifying pw1 fails: {}", Hashing.verifyAndUpdateHash("password1", hashPw1, update));
        String hashPw2 = Hashing.hash("password");
        log.debug("hash of pw2: {}", hashPw2);
        log.debug("verifying pw2: {}", Hashing.verifyAndUpdateHash("password", hashPw2, update));
        log.debug("verifying pw2 fails: {}", Hashing.verifyAndUpdateHash("password2", hashPw2, update));

        UpdatableBCrypt oldHasher = new UpdatableBCrypt(7);
        String oldHash = oldHasher.hash("password");
        log.debug("hash of oldHash: {}", oldHash);
        log.debug("verifying oldHash: {}, hash upgraded to: {}",
                  Hashing.verifyAndUpdateHash("password", oldHash, update),
                  mutableHash[0]);
        // {{end:bcryptMain}}

    }
}
