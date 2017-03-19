package com.stubbornjava.examples.common;

import org.hashids.Hashids;

// {{start:hashids}}
public class HashIds {
    /*
     *  The salt is important so that your ids cannot be guessed.
     *  If you used a default hash an attacker could generate all possible ids
     *  which defeats the purpose of obfuscating the ids and making them non sequential.
     */
    private static final Hashids hashids = new Hashids("Your Salt Here", 3);

    public static String encode(long... ids) {
        return hashids.encode(ids);
    }

    public static long[] decodeArray(String hash) {
        return hashids.decode(hash);
    }

    public static long decode(String hash) {
        return hashids.decode(hash)[0];
    }
}
// {{end:hashids}}
