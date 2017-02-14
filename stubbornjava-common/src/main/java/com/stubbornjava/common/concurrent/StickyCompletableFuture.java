package com.stubbornjava.common.concurrent;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.Executor;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;

public class StickyCompletableFuture<T> implements CompletionStage<T> {
    private final CompletableFuture<T> future;
    private final Executor defaultExecutor;

    StickyCompletableFuture(CompletableFuture<T> future, Executor defaultExecutor) {
        this.future = future;
        this.defaultExecutor = defaultExecutor;
    }

    @Override
    public <U> CompletionStage<U> thenApply(Function<? super T, ? extends U> fn) {
        return new StickyCompletableFuture<>(future.thenApply(fn), defaultExecutor);
    }

    @Override
    public <U> CompletionStage<U> thenApplyAsync(Function<? super T, ? extends U> fn) {
        return new StickyCompletableFuture<>(future.thenApplyAsync(fn, defaultExecutor), defaultExecutor);
    }

    @Override
    public <U> CompletionStage<U> thenApplyAsync(Function<? super T, ? extends U> fn, Executor executor) {
        return new StickyCompletableFuture<>(future.thenApplyAsync(fn, executor), executor);
    }

    @Override
    public CompletionStage<Void> thenAccept(Consumer<? super T> action) {
        return new StickyCompletableFuture<>(future.thenAccept(action), defaultExecutor);
    }

    @Override
    public CompletionStage<Void> thenAcceptAsync(Consumer<? super T> action) {
        return new StickyCompletableFuture<>(future.thenAcceptAsync(action, defaultExecutor), defaultExecutor);
    }

    @Override
    public CompletionStage<Void> thenAcceptAsync(Consumer<? super T> action, Executor executor) {
        return new StickyCompletableFuture<>(future.thenAcceptAsync(action, executor), executor);
    }

    @Override
    public CompletionStage<Void> thenRun(Runnable action) {
        return new StickyCompletableFuture<>(future.thenRun(action), defaultExecutor);
    }

    @Override
    public CompletionStage<Void> thenRunAsync(Runnable action) {
        return new StickyCompletableFuture<>(future.thenRunAsync(action, defaultExecutor), defaultExecutor);
    }

    @Override
    public CompletionStage<Void> thenRunAsync(Runnable action, Executor executor) {
        return new StickyCompletableFuture<>(future.thenRunAsync(action, executor), executor);
    }

    @Override
    public <U, V> CompletionStage<V> thenCombine(CompletionStage<? extends U> other,
            BiFunction<? super T, ? super U, ? extends V> fn) {
        return new StickyCompletableFuture<>(future.thenCombine(other, fn), defaultExecutor);
    }

    @Override
    public <U, V> CompletionStage<V> thenCombineAsync(CompletionStage<? extends U> other,
            BiFunction<? super T, ? super U, ? extends V> fn) {
        return new StickyCompletableFuture<>(future.thenCombineAsync(other, fn, defaultExecutor), defaultExecutor);
    }

    @Override
    public <U, V> CompletionStage<V> thenCombineAsync(CompletionStage<? extends U> other,
            BiFunction<? super T, ? super U, ? extends V> fn, Executor executor) {
        return new StickyCompletableFuture<>(future.thenCombineAsync(other, fn, executor), executor);
    }

    @Override
    public <U> CompletionStage<Void> thenAcceptBoth(CompletionStage<? extends U> other,
            BiConsumer<? super T, ? super U> action) {
        return new StickyCompletableFuture<>(future.thenAcceptBoth(other, action), defaultExecutor);
    }

    @Override
    public <U> CompletionStage<Void> thenAcceptBothAsync(CompletionStage<? extends U> other,
            BiConsumer<? super T, ? super U> action) {
        return new StickyCompletableFuture<>(future.thenAcceptBothAsync(other, action, defaultExecutor), defaultExecutor);
    }

    @Override
    public <U> CompletionStage<Void> thenAcceptBothAsync(CompletionStage<? extends U> other,
            BiConsumer<? super T, ? super U> action, Executor executor) {
        return new StickyCompletableFuture<>(future.thenAcceptBothAsync(other, action, executor), executor);
    }

    @Override
    public CompletionStage<Void> runAfterBoth(CompletionStage<?> other, Runnable action) {
        return new StickyCompletableFuture<>(future.runAfterBoth(other, action), defaultExecutor);
    }

    @Override
    public CompletionStage<Void> runAfterBothAsync(CompletionStage<?> other, Runnable action) {
        return new StickyCompletableFuture<>(future.runAfterBothAsync(other, action, defaultExecutor), defaultExecutor);
    }

    @Override
    public CompletionStage<Void> runAfterBothAsync(CompletionStage<?> other, Runnable action, Executor executor) {
        return new StickyCompletableFuture<>(future.runAfterBothAsync(other, action, executor), executor);
    }

    @Override
    public <U> CompletionStage<U> applyToEither(CompletionStage<? extends T> other, Function<? super T, U> fn) {
        return new StickyCompletableFuture<>(future.applyToEither(other, fn), defaultExecutor);
    }

    @Override
    public <U> CompletionStage<U> applyToEitherAsync(CompletionStage<? extends T> other, Function<? super T, U> fn) {
        return new StickyCompletableFuture<>(future.applyToEitherAsync(other, fn, defaultExecutor), defaultExecutor);
    }

    @Override
    public <U> CompletionStage<U> applyToEitherAsync(CompletionStage<? extends T> other, Function<? super T, U> fn,
            Executor executor) {
        return new StickyCompletableFuture<>(future.applyToEitherAsync(other, fn, executor), executor);
    }

    @Override
    public CompletionStage<Void> acceptEither(CompletionStage<? extends T> other, Consumer<? super T> action) {
        return new StickyCompletableFuture<>(future.acceptEither(other, action), defaultExecutor);
    }

    @Override
    public CompletionStage<Void> acceptEitherAsync(CompletionStage<? extends T> other, Consumer<? super T> action) {
        return new StickyCompletableFuture<>(future.acceptEitherAsync(other, action, defaultExecutor), defaultExecutor);
    }

    @Override
    public CompletionStage<Void> acceptEitherAsync(CompletionStage<? extends T> other, Consumer<? super T> action,
            Executor executor) {
        return new StickyCompletableFuture<>(future.acceptEitherAsync(other, action, executor), executor);
    }

    @Override
    public CompletionStage<Void> runAfterEither(CompletionStage<?> other, Runnable action) {
        return new StickyCompletableFuture<>(future.runAfterEither(other, action), defaultExecutor);
    }

    @Override
    public CompletionStage<Void> runAfterEitherAsync(CompletionStage<?> other, Runnable action) {
        return new StickyCompletableFuture<>(future.runAfterEitherAsync(other, action, defaultExecutor), defaultExecutor);
    }

    @Override
    public CompletionStage<Void> runAfterEitherAsync(CompletionStage<?> other, Runnable action, Executor executor) {
        return new StickyCompletableFuture<>(future.runAfterEitherAsync(other, action, executor), executor);
    }

    @Override
    public <U> CompletionStage<U> thenCompose(Function<? super T, ? extends CompletionStage<U>> fn) {
        return new StickyCompletableFuture<>(future.thenCompose(fn), defaultExecutor);
    }

    @Override
    public <U> CompletionStage<U> thenComposeAsync(Function<? super T, ? extends CompletionStage<U>> fn) {
        return new StickyCompletableFuture<>(future.thenComposeAsync(fn, defaultExecutor), defaultExecutor);
    }

    @Override
    public <U> CompletionStage<U> thenComposeAsync(Function<? super T, ? extends CompletionStage<U>> fn,
            Executor executor) {
        return new StickyCompletableFuture<>(future.thenComposeAsync(fn, executor), executor);
    }

    @Override
    public CompletionStage<T> exceptionally(Function<Throwable, ? extends T> fn) {
        return new StickyCompletableFuture<>(future.exceptionally(fn), defaultExecutor);
    }

    @Override
    public CompletionStage<T> whenComplete(BiConsumer<? super T, ? super Throwable> action) {
        return new StickyCompletableFuture<>(future.whenComplete(action), defaultExecutor);
    }

    @Override
    public CompletionStage<T> whenCompleteAsync(BiConsumer<? super T, ? super Throwable> action) {
        return new StickyCompletableFuture<>(future.whenCompleteAsync(action, defaultExecutor), defaultExecutor);
    }

    @Override
    public CompletionStage<T> whenCompleteAsync(BiConsumer<? super T, ? super Throwable> action, Executor executor) {
        return new StickyCompletableFuture<>(future.whenCompleteAsync(action, executor), executor);
    }

    @Override
    public <U> CompletionStage<U> handle(BiFunction<? super T, Throwable, ? extends U> fn) {
        return new StickyCompletableFuture<>(future.handle(fn), defaultExecutor);
    }

    @Override
    public <U> CompletionStage<U> handleAsync(BiFunction<? super T, Throwable, ? extends U> fn) {
        return new StickyCompletableFuture<>(future.handleAsync(fn, defaultExecutor), defaultExecutor);
    }

    @Override
    public <U> CompletionStage<U> handleAsync(BiFunction<? super T, Throwable, ? extends U> fn, Executor executor) {
        return new StickyCompletableFuture<>(future.handleAsync(fn, executor), executor);
    }

    @Override
    public CompletableFuture<T> toCompletableFuture() {
        return future;
    }
}
