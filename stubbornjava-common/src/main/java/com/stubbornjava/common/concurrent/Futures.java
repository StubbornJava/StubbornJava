package com.stubbornjava.common.concurrent;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.Executor;
import java.util.function.Supplier;

public class Futures {
    private Futures() {

    }

    public static <U> CompletionStage<U> supplyAsync(Supplier<U> supplier, Executor executor) {
        return new StickyCompletableFuture<>(CompletableFuture.supplyAsync(supplier, executor), executor);
    }

    public static CompletionStage<Void> runAsync(Runnable runnable, Executor executor) {
        return new StickyCompletableFuture<>(CompletableFuture.runAsync(runnable, executor), executor);
    }
}
