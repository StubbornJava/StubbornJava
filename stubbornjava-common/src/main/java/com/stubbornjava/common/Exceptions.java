package com.stubbornjava.common;

public class Exceptions {

    /**
     * Sneaky throw any type of Throwable.
     */
    public static RuntimeException sneaky(Throwable throwable) {
        return Exceptions.<RuntimeException>sneaky0(throwable);
    }

    /**
     * Sneaky throw any type of Throwable.
     */
    @SuppressWarnings("unchecked")
    static <E extends Throwable> E sneaky0(Throwable throwable) throws E {
        return (E) throwable;
    }

    /**
     * Sneaky throw any type of Throwable.
     */
    public static void sneakyThrow(Throwable throwable) {
        Exceptions.<RuntimeException>sneakyThrow0(throwable);
    }

    /**
     * Sneaky throw any type of Throwable.
     */
    @SuppressWarnings("unchecked")
    static <E extends Throwable> void sneakyThrow0(Throwable throwable) throws E {
        throw (E) throwable;
    }
}
