package com.stubbornjava.common.concurrent;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.Executor;
import java.util.function.Supplier;

public class Futures {

    public <U> CompletionStage<U> supplyAsync(Supplier<U> supplier, Executor executor) {
        return new StickyCompletableFuture<>(CompletableFuture.supplyAsync(supplier, executor), executor);
    }

    public CompletionStage<Void> runAsync(Runnable runnable, Executor executor) {
        return new StickyCompletableFuture<>(CompletableFuture.runAsync(runnable, executor), executor);
    }
}
