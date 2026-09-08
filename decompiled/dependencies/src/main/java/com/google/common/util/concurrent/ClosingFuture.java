package com.google.common.util.concurrent;

import androidx.lifecycle.LifecycleKt$$ExternalSyntheticBackportWithForwarding0;
import com.google.common.base.Function;
import com.google.common.base.Functions;
import com.google.common.base.MoreObjects;
import com.google.common.base.Preconditions;
import com.google.common.collect.FluentIterable;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.google.common.util.concurrent.Futures;
import com.google.errorprone.annotations.DoNotMock;
import java.io.Closeable;
import java.io.IOException;
import java.util.IdentityHashMap;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.atomic.AtomicReference;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.CheckForNull;

@DoNotMock("Use ClosingFuture.from(Futures.immediate*Future)")
@ElementTypesAreNonnullByDefault
/* loaded from: classes7.dex */
public final class ClosingFuture<V> {
    private static final Logger logger = Logger.getLogger(ClosingFuture.class.getName());
    private final CloseableList closeables;
    private final FluentFuture<V> future;
    private final AtomicReference<State> state;

    /* loaded from: classes7.dex */
    public interface AsyncClosingCallable<V> {
        ClosingFuture<V> call(DeferredCloser deferredCloser) throws Exception;
    }

    /* loaded from: classes7.dex */
    public interface AsyncClosingFunction<T, U> {
        ClosingFuture<U> apply(DeferredCloser deferredCloser, @ParametricNullness T t) throws Exception;
    }

    /* loaded from: classes7.dex */
    public interface ClosingCallable<V> {
        @ParametricNullness
        V call(DeferredCloser deferredCloser) throws Exception;
    }

    /* loaded from: classes7.dex */
    public interface ClosingFunction<T, U> {
        @ParametricNullness
        U apply(DeferredCloser deferredCloser, @ParametricNullness T t) throws Exception;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes7.dex */
    public enum State {
        OPEN,
        SUBSUMED,
        WILL_CLOSE,
        CLOSING,
        CLOSED,
        WILL_CREATE_VALUE_AND_CLOSER
    }

    /* loaded from: classes7.dex */
    public interface ValueAndCloserConsumer<V> {
        void accept(ValueAndCloser<V> valueAndCloser);
    }

    /* loaded from: classes7.dex */
    public static final class DeferredCloser {
        private final CloseableList list;

        DeferredCloser(CloseableList list) {
            this.list = list;
        }

        @ParametricNullness
        public <C extends Closeable> C eventuallyClose(@ParametricNullness C closeable, Executor closingExecutor) {
            Preconditions.checkNotNull(closingExecutor);
            if (closeable != null) {
                this.list.add(closeable, closingExecutor);
            }
            return closeable;
        }
    }

    /* loaded from: classes7.dex */
    public static final class ValueAndCloser<V> {
        private final ClosingFuture<? extends V> closingFuture;

        ValueAndCloser(ClosingFuture<? extends V> closingFuture) {
            this.closingFuture = (ClosingFuture) Preconditions.checkNotNull(closingFuture);
        }

        @ParametricNullness
        public V get() throws ExecutionException {
            return (V) Futures.getDone(((ClosingFuture) this.closingFuture).future);
        }

        public void closeAsync() {
            this.closingFuture.close();
        }
    }

    public static <V> ClosingFuture<V> submit(ClosingCallable<V> callable, Executor executor) {
        return new ClosingFuture<>(callable, executor);
    }

    public static <V> ClosingFuture<V> submitAsync(AsyncClosingCallable<V> callable, Executor executor) {
        return new ClosingFuture<>(callable, executor);
    }

    public static <V> ClosingFuture<V> from(ListenableFuture<V> future) {
        return new ClosingFuture<>(future);
    }

    @Deprecated
    public static <C extends Closeable> ClosingFuture<C> eventuallyClosing(ListenableFuture<C> future, final Executor closingExecutor) {
        Preconditions.checkNotNull(closingExecutor);
        ClosingFuture<C> closingFuture = new ClosingFuture<>(Futures.nonCancellationPropagating(future));
        Futures.addCallback(future, new FutureCallback<Closeable>() { // from class: com.google.common.util.concurrent.ClosingFuture.1
            @Override // com.google.common.util.concurrent.FutureCallback
            public void onSuccess(@CheckForNull Closeable result) {
                ClosingFuture.this.closeables.closer.eventuallyClose(result, closingExecutor);
            }

            @Override // com.google.common.util.concurrent.FutureCallback
            public void onFailure(Throwable t) {
            }
        }, MoreExecutors.directExecutor());
        return closingFuture;
    }

    public static Combiner whenAllComplete(Iterable<? extends ClosingFuture<?>> futures) {
        return new Combiner(false, futures);
    }

    public static Combiner whenAllComplete(ClosingFuture<?> future1, ClosingFuture<?>... moreFutures) {
        return whenAllComplete(Lists.asList(future1, moreFutures));
    }

    public static Combiner whenAllSucceed(Iterable<? extends ClosingFuture<?>> futures) {
        return new Combiner(true, futures);
    }

    public static <V1, V2> Combiner2<V1, V2> whenAllSucceed(ClosingFuture<V1> future1, ClosingFuture<V2> future2) {
        return new Combiner2<>(future2);
    }

    public static <V1, V2, V3> Combiner3<V1, V2, V3> whenAllSucceed(ClosingFuture<V1> future1, ClosingFuture<V2> future2, ClosingFuture<V3> future3) {
        return new Combiner3<>(future2, future3);
    }

    public static <V1, V2, V3, V4> Combiner4<V1, V2, V3, V4> whenAllSucceed(ClosingFuture<V1> future1, ClosingFuture<V2> future2, ClosingFuture<V3> future3, ClosingFuture<V4> future4) {
        return new Combiner4<>(future2, future3, future4);
    }

    public static <V1, V2, V3, V4, V5> Combiner5<V1, V2, V3, V4, V5> whenAllSucceed(ClosingFuture<V1> future1, ClosingFuture<V2> future2, ClosingFuture<V3> future3, ClosingFuture<V4> future4, ClosingFuture<V5> future5) {
        return new Combiner5<>(future2, future3, future4, future5);
    }

    public static Combiner whenAllSucceed(ClosingFuture<?> future1, ClosingFuture<?> future2, ClosingFuture<?> future3, ClosingFuture<?> future4, ClosingFuture<?> future5, ClosingFuture<?> future6, ClosingFuture<?>... moreFutures) {
        return whenAllSucceed(FluentIterable.of(future1, future2, future3, future4, future5, future6).append(moreFutures));
    }

    private ClosingFuture(ListenableFuture<V> future) {
        this.state = new AtomicReference<>(State.OPEN);
        this.closeables = new CloseableList();
        this.future = FluentFuture.from(future);
    }

    private ClosingFuture(final ClosingCallable<V> callable, Executor executor) {
        this.state = new AtomicReference<>(State.OPEN);
        this.closeables = new CloseableList();
        Preconditions.checkNotNull(callable);
        TrustedListenableFutureTask<V> task = TrustedListenableFutureTask.create(new Callable<V>() { // from class: com.google.common.util.concurrent.ClosingFuture.2
            @Override // java.util.concurrent.Callable
            @ParametricNullness
            public V call() throws Exception {
                return (V) callable.call(ClosingFuture.this.closeables.closer);
            }

            public String toString() {
                return callable.toString();
            }
        });
        executor.execute(task);
        this.future = task;
    }

    private ClosingFuture(final AsyncClosingCallable<V> callable, Executor executor) {
        this.state = new AtomicReference<>(State.OPEN);
        this.closeables = new CloseableList();
        Preconditions.checkNotNull(callable);
        TrustedListenableFutureTask<V> task = TrustedListenableFutureTask.create(new AsyncCallable<V>() { // from class: com.google.common.util.concurrent.ClosingFuture.3
            @Override // com.google.common.util.concurrent.AsyncCallable
            public ListenableFuture<V> call() throws Exception {
                CloseableList newCloseables = new CloseableList();
                try {
                    ClosingFuture<V> closingFuture = callable.call(newCloseables.closer);
                    closingFuture.becomeSubsumedInto(ClosingFuture.this.closeables);
                    return ((ClosingFuture) closingFuture).future;
                } finally {
                    ClosingFuture.this.closeables.add(newCloseables, MoreExecutors.directExecutor());
                }
            }

            public String toString() {
                return callable.toString();
            }
        });
        executor.execute(task);
        this.future = task;
    }

    public ListenableFuture<?> statusFuture() {
        return Futures.nonCancellationPropagating(this.future.transform(Functions.constant(null), MoreExecutors.directExecutor()));
    }

    public <U> ClosingFuture<U> transform(final ClosingFunction<? super V, U> function, Executor executor) {
        Preconditions.checkNotNull(function);
        AsyncFunction<V, U> applyFunction = new AsyncFunction<V, U>() { // from class: com.google.common.util.concurrent.ClosingFuture.4
            @Override // com.google.common.util.concurrent.AsyncFunction
            public ListenableFuture<U> apply(V input) throws Exception {
                return ClosingFuture.this.closeables.applyClosingFunction(function, input);
            }

            public String toString() {
                return function.toString();
            }
        };
        return derive(this.future.transformAsync(applyFunction, executor));
    }

    public <U> ClosingFuture<U> transformAsync(final AsyncClosingFunction<? super V, U> function, Executor executor) {
        Preconditions.checkNotNull(function);
        AsyncFunction<V, U> applyFunction = new AsyncFunction<V, U>() { // from class: com.google.common.util.concurrent.ClosingFuture.5
            @Override // com.google.common.util.concurrent.AsyncFunction
            public ListenableFuture<U> apply(V input) throws Exception {
                return ClosingFuture.this.closeables.applyAsyncClosingFunction(function, input);
            }

            public String toString() {
                return function.toString();
            }
        };
        return derive(this.future.transformAsync(applyFunction, executor));
    }

    public static <V, U> AsyncClosingFunction<V, U> withoutCloser(final AsyncFunction<V, U> function) {
        Preconditions.checkNotNull(function);
        return new AsyncClosingFunction<V, U>() { // from class: com.google.common.util.concurrent.ClosingFuture.6
            @Override // com.google.common.util.concurrent.ClosingFuture.AsyncClosingFunction
            public ClosingFuture<U> apply(DeferredCloser closer, V input) throws Exception {
                return ClosingFuture.from(AsyncFunction.this.apply(input));
            }
        };
    }

    /* JADX WARN: Multi-variable type inference failed */
    public <X extends Throwable> ClosingFuture<V> catching(Class<X> exceptionType, ClosingFunction<? super X, ? extends V> closingFunction, Executor executor) {
        return catchingMoreGeneric(exceptionType, closingFunction, executor);
    }

    /* JADX WARN: Multi-variable type inference failed */
    private <X extends Throwable, W extends V> ClosingFuture<V> catchingMoreGeneric(Class<X> cls, final ClosingFunction<? super X, W> closingFunction, Executor executor) {
        Preconditions.checkNotNull(closingFunction);
        return (ClosingFuture<V>) derive(this.future.catchingAsync(cls, new AsyncFunction<X, W>() { // from class: com.google.common.util.concurrent.ClosingFuture.7
            /* JADX WARN: Incorrect types in method signature: (TX;)Lcom/google/common/util/concurrent/ListenableFuture<TW;>; */
            @Override // com.google.common.util.concurrent.AsyncFunction
            public ListenableFuture apply(Throwable th) throws Exception {
                return ClosingFuture.this.closeables.applyClosingFunction(closingFunction, th);
            }

            public String toString() {
                return closingFunction.toString();
            }
        }, executor));
    }

    /* JADX WARN: Multi-variable type inference failed */
    public <X extends Throwable> ClosingFuture<V> catchingAsync(Class<X> exceptionType, AsyncClosingFunction<? super X, ? extends V> asyncClosingFunction, Executor executor) {
        return catchingAsyncMoreGeneric(exceptionType, asyncClosingFunction, executor);
    }

    /* JADX WARN: Multi-variable type inference failed */
    private <X extends Throwable, W extends V> ClosingFuture<V> catchingAsyncMoreGeneric(Class<X> cls, final AsyncClosingFunction<? super X, W> asyncClosingFunction, Executor executor) {
        Preconditions.checkNotNull(asyncClosingFunction);
        return (ClosingFuture<V>) derive(this.future.catchingAsync(cls, new AsyncFunction<X, W>() { // from class: com.google.common.util.concurrent.ClosingFuture.8
            /* JADX WARN: Incorrect types in method signature: (TX;)Lcom/google/common/util/concurrent/ListenableFuture<TW;>; */
            @Override // com.google.common.util.concurrent.AsyncFunction
            public ListenableFuture apply(Throwable th) throws Exception {
                return ClosingFuture.this.closeables.applyAsyncClosingFunction(asyncClosingFunction, th);
            }

            public String toString() {
                return asyncClosingFunction.toString();
            }
        }, executor));
    }

    public FluentFuture<V> finishToFuture() {
        if (compareAndUpdateState(State.OPEN, State.WILL_CLOSE)) {
            logger.log(Level.FINER, "will close {0}", this);
            this.future.addListener(new Runnable() { // from class: com.google.common.util.concurrent.ClosingFuture.9
                @Override // java.lang.Runnable
                public void run() {
                    ClosingFuture.this.checkAndUpdateState(State.WILL_CLOSE, State.CLOSING);
                    ClosingFuture.this.close();
                    ClosingFuture.this.checkAndUpdateState(State.CLOSING, State.CLOSED);
                }
            }, MoreExecutors.directExecutor());
        } else {
            switch (AnonymousClass12.$SwitchMap$com$google$common$util$concurrent$ClosingFuture$State[this.state.get().ordinal()]) {
                case 1:
                    throw new IllegalStateException("Cannot call finishToFuture() after deriving another step");
                case 2:
                    throw new IllegalStateException("Cannot call finishToFuture() after calling finishToValueAndCloser()");
                case 3:
                case 4:
                case 5:
                    throw new IllegalStateException("Cannot call finishToFuture() twice");
                case 6:
                    throw new AssertionError();
            }
        }
        return this.future;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: com.google.common.util.concurrent.ClosingFuture$12, reason: invalid class name */
    /* loaded from: classes7.dex */
    public static /* synthetic */ class AnonymousClass12 {
        static final /* synthetic */ int[] $SwitchMap$com$google$common$util$concurrent$ClosingFuture$State;

        static {
            int[] iArr = new int[State.values().length];
            $SwitchMap$com$google$common$util$concurrent$ClosingFuture$State = iArr;
            try {
                iArr[State.SUBSUMED.ordinal()] = 1;
            } catch (NoSuchFieldError e) {
            }
            try {
                $SwitchMap$com$google$common$util$concurrent$ClosingFuture$State[State.WILL_CREATE_VALUE_AND_CLOSER.ordinal()] = 2;
            } catch (NoSuchFieldError e2) {
            }
            try {
                $SwitchMap$com$google$common$util$concurrent$ClosingFuture$State[State.WILL_CLOSE.ordinal()] = 3;
            } catch (NoSuchFieldError e3) {
            }
            try {
                $SwitchMap$com$google$common$util$concurrent$ClosingFuture$State[State.CLOSING.ordinal()] = 4;
            } catch (NoSuchFieldError e4) {
            }
            try {
                $SwitchMap$com$google$common$util$concurrent$ClosingFuture$State[State.CLOSED.ordinal()] = 5;
            } catch (NoSuchFieldError e5) {
            }
            try {
                $SwitchMap$com$google$common$util$concurrent$ClosingFuture$State[State.OPEN.ordinal()] = 6;
            } catch (NoSuchFieldError e6) {
            }
        }
    }

    public void finishToValueAndCloser(final ValueAndCloserConsumer<? super V> consumer, Executor executor) {
        Preconditions.checkNotNull(consumer);
        if (!compareAndUpdateState(State.OPEN, State.WILL_CREATE_VALUE_AND_CLOSER)) {
            switch (AnonymousClass12.$SwitchMap$com$google$common$util$concurrent$ClosingFuture$State[this.state.get().ordinal()]) {
                case 1:
                    throw new IllegalStateException("Cannot call finishToValueAndCloser() after deriving another step");
                case 2:
                    throw new IllegalStateException("Cannot call finishToValueAndCloser() twice");
                case 3:
                case 4:
                case 5:
                    throw new IllegalStateException("Cannot call finishToValueAndCloser() after calling finishToFuture()");
                default:
                    throw new AssertionError(this.state);
            }
        }
        this.future.addListener(new Runnable() { // from class: com.google.common.util.concurrent.ClosingFuture.10
            @Override // java.lang.Runnable
            public void run() {
                ClosingFuture.provideValueAndCloser(consumer, ClosingFuture.this);
            }
        }, executor);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static <C, V extends C> void provideValueAndCloser(ValueAndCloserConsumer<C> consumer, ClosingFuture<V> closingFuture) {
        consumer.accept(new ValueAndCloser<>(closingFuture));
    }

    public boolean cancel(boolean mayInterruptIfRunning) {
        logger.log(Level.FINER, "cancelling {0}", this);
        boolean cancelled = this.future.cancel(mayInterruptIfRunning);
        if (cancelled) {
            close();
        }
        return cancelled;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void close() {
        logger.log(Level.FINER, "closing {0}", this);
        this.closeables.close();
    }

    private <U> ClosingFuture<U> derive(FluentFuture<U> future) {
        ClosingFuture<U> derived = new ClosingFuture<>(future);
        becomeSubsumedInto(derived.closeables);
        return derived;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void becomeSubsumedInto(CloseableList otherCloseables) {
        checkAndUpdateState(State.OPEN, State.SUBSUMED);
        otherCloseables.add(this.closeables, MoreExecutors.directExecutor());
    }

    /* loaded from: classes7.dex */
    public static final class Peeker {
        private volatile boolean beingCalled;
        private final ImmutableList<ClosingFuture<?>> futures;

        private Peeker(ImmutableList<ClosingFuture<?>> futures) {
            this.futures = (ImmutableList) Preconditions.checkNotNull(futures);
        }

        @ParametricNullness
        public final <D> D getDone(ClosingFuture<D> closingFuture) throws ExecutionException {
            Preconditions.checkState(this.beingCalled);
            Preconditions.checkArgument(this.futures.contains(closingFuture));
            return (D) Futures.getDone(((ClosingFuture) closingFuture).future);
        }

        /* JADX INFO: Access modifiers changed from: private */
        @ParametricNullness
        public <V> V call(Combiner.CombiningCallable<V> combiner, CloseableList closeables) throws Exception {
            this.beingCalled = true;
            CloseableList newCloseables = new CloseableList();
            try {
                return combiner.call(newCloseables.closer, this);
            } finally {
                closeables.add(newCloseables, MoreExecutors.directExecutor());
                this.beingCalled = false;
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public <V> FluentFuture<V> callAsync(Combiner.AsyncCombiningCallable<V> combiner, CloseableList closeables) throws Exception {
            this.beingCalled = true;
            CloseableList newCloseables = new CloseableList();
            try {
                ClosingFuture<V> closingFuture = combiner.call(newCloseables.closer, this);
                closingFuture.becomeSubsumedInto(closeables);
                return ((ClosingFuture) closingFuture).future;
            } finally {
                closeables.add(newCloseables, MoreExecutors.directExecutor());
                this.beingCalled = false;
            }
        }
    }

    @DoNotMock("Use ClosingFuture.whenAllSucceed() or .whenAllComplete() instead.")
    /* loaded from: classes7.dex */
    public static class Combiner {
        private static final Function<ClosingFuture<?>, FluentFuture<?>> INNER_FUTURE = new Function<ClosingFuture<?>, FluentFuture<?>>() { // from class: com.google.common.util.concurrent.ClosingFuture.Combiner.3
            @Override // com.google.common.base.Function
            public FluentFuture<?> apply(ClosingFuture<?> future) {
                return ((ClosingFuture) future).future;
            }
        };
        private final boolean allMustSucceed;
        private final CloseableList closeables;
        protected final ImmutableList<ClosingFuture<?>> inputs;

        /* loaded from: classes7.dex */
        public interface AsyncCombiningCallable<V> {
            ClosingFuture<V> call(DeferredCloser deferredCloser, Peeker peeker) throws Exception;
        }

        /* loaded from: classes7.dex */
        public interface CombiningCallable<V> {
            @ParametricNullness
            V call(DeferredCloser deferredCloser, Peeker peeker) throws Exception;
        }

        private Combiner(boolean allMustSucceed, Iterable<? extends ClosingFuture<?>> inputs) {
            this.closeables = new CloseableList();
            this.allMustSucceed = allMustSucceed;
            this.inputs = ImmutableList.copyOf(inputs);
            for (ClosingFuture<?> input : inputs) {
                input.becomeSubsumedInto(this.closeables);
            }
        }

        public <V> ClosingFuture<V> call(final CombiningCallable<V> combiningCallable, Executor executor) {
            ClosingFuture<V> closingFuture = new ClosingFuture<>(futureCombiner().call(new Callable<V>() { // from class: com.google.common.util.concurrent.ClosingFuture.Combiner.1
                @Override // java.util.concurrent.Callable
                @ParametricNullness
                public V call() throws Exception {
                    return (V) new Peeker(Combiner.this.inputs).call(combiningCallable, Combiner.this.closeables);
                }

                public String toString() {
                    return combiningCallable.toString();
                }
            }, executor));
            ((ClosingFuture) closingFuture).closeables.add(this.closeables, MoreExecutors.directExecutor());
            return closingFuture;
        }

        public <V> ClosingFuture<V> callAsync(final AsyncCombiningCallable<V> asyncCombiningCallable, Executor executor) {
            ClosingFuture<V> closingFuture = new ClosingFuture<>(futureCombiner().callAsync(new AsyncCallable<V>() { // from class: com.google.common.util.concurrent.ClosingFuture.Combiner.2
                @Override // com.google.common.util.concurrent.AsyncCallable
                public ListenableFuture<V> call() throws Exception {
                    return new Peeker(Combiner.this.inputs).callAsync(asyncCombiningCallable, Combiner.this.closeables);
                }

                public String toString() {
                    return asyncCombiningCallable.toString();
                }
            }, executor));
            ((ClosingFuture) closingFuture).closeables.add(this.closeables, MoreExecutors.directExecutor());
            return closingFuture;
        }

        private Futures.FutureCombiner<Object> futureCombiner() {
            if (this.allMustSucceed) {
                return Futures.whenAllSucceed(inputFutures());
            }
            return Futures.whenAllComplete(inputFutures());
        }

        private ImmutableList<FluentFuture<?>> inputFutures() {
            return FluentIterable.from(this.inputs).transform(INNER_FUTURE).toList();
        }
    }

    /* loaded from: classes7.dex */
    public static final class Combiner2<V1, V2> extends Combiner {
        private final ClosingFuture<V1> future1;
        private final ClosingFuture<V2> future2;

        /* loaded from: classes7.dex */
        public interface AsyncClosingFunction2<V1, V2, U> {
            ClosingFuture<U> apply(DeferredCloser deferredCloser, @ParametricNullness V1 v1, @ParametricNullness V2 v2) throws Exception;
        }

        /* loaded from: classes7.dex */
        public interface ClosingFunction2<V1, V2, U> {
            @ParametricNullness
            U apply(DeferredCloser deferredCloser, @ParametricNullness V1 v1, @ParametricNullness V2 v2) throws Exception;
        }

        private Combiner2(ClosingFuture<V1> future1, ClosingFuture<V2> future2) {
            super(true, ImmutableList.of((ClosingFuture<V2>) future1, future2));
            this.future1 = future1;
            this.future2 = future2;
        }

        public <U> ClosingFuture<U> call(final ClosingFunction2<V1, V2, U> function, Executor executor) {
            return call(new Combiner.CombiningCallable<U>() { // from class: com.google.common.util.concurrent.ClosingFuture.Combiner2.1
                /* JADX WARN: Multi-variable type inference failed */
                @Override // com.google.common.util.concurrent.ClosingFuture.Combiner.CombiningCallable
                @ParametricNullness
                public U call(DeferredCloser deferredCloser, Peeker peeker) throws Exception {
                    return (U) function.apply(deferredCloser, peeker.getDone(Combiner2.this.future1), peeker.getDone(Combiner2.this.future2));
                }

                public String toString() {
                    return function.toString();
                }
            }, executor);
        }

        public <U> ClosingFuture<U> callAsync(final AsyncClosingFunction2<V1, V2, U> function, Executor executor) {
            return callAsync(new Combiner.AsyncCombiningCallable<U>() { // from class: com.google.common.util.concurrent.ClosingFuture.Combiner2.2
                /* JADX WARN: Multi-variable type inference failed */
                @Override // com.google.common.util.concurrent.ClosingFuture.Combiner.AsyncCombiningCallable
                public ClosingFuture<U> call(DeferredCloser closer, Peeker peeker) throws Exception {
                    return function.apply(closer, peeker.getDone(Combiner2.this.future1), peeker.getDone(Combiner2.this.future2));
                }

                public String toString() {
                    return function.toString();
                }
            }, executor);
        }
    }

    /* loaded from: classes7.dex */
    public static final class Combiner3<V1, V2, V3> extends Combiner {
        private final ClosingFuture<V1> future1;
        private final ClosingFuture<V2> future2;
        private final ClosingFuture<V3> future3;

        /* loaded from: classes7.dex */
        public interface AsyncClosingFunction3<V1, V2, V3, U> {
            ClosingFuture<U> apply(DeferredCloser deferredCloser, @ParametricNullness V1 v1, @ParametricNullness V2 v2, @ParametricNullness V3 v3) throws Exception;
        }

        /* loaded from: classes7.dex */
        public interface ClosingFunction3<V1, V2, V3, U> {
            @ParametricNullness
            U apply(DeferredCloser deferredCloser, @ParametricNullness V1 v1, @ParametricNullness V2 v2, @ParametricNullness V3 v3) throws Exception;
        }

        private Combiner3(ClosingFuture<V1> future1, ClosingFuture<V2> future2, ClosingFuture<V3> future3) {
            super(true, ImmutableList.of((ClosingFuture<V3>) future1, (ClosingFuture<V3>) future2, future3));
            this.future1 = future1;
            this.future2 = future2;
            this.future3 = future3;
        }

        public <U> ClosingFuture<U> call(final ClosingFunction3<V1, V2, V3, U> function, Executor executor) {
            return call(new Combiner.CombiningCallable<U>() { // from class: com.google.common.util.concurrent.ClosingFuture.Combiner3.1
                /* JADX WARN: Multi-variable type inference failed */
                @Override // com.google.common.util.concurrent.ClosingFuture.Combiner.CombiningCallable
                @ParametricNullness
                public U call(DeferredCloser deferredCloser, Peeker peeker) throws Exception {
                    return (U) function.apply(deferredCloser, peeker.getDone(Combiner3.this.future1), peeker.getDone(Combiner3.this.future2), peeker.getDone(Combiner3.this.future3));
                }

                public String toString() {
                    return function.toString();
                }
            }, executor);
        }

        public <U> ClosingFuture<U> callAsync(final AsyncClosingFunction3<V1, V2, V3, U> function, Executor executor) {
            return callAsync(new Combiner.AsyncCombiningCallable<U>() { // from class: com.google.common.util.concurrent.ClosingFuture.Combiner3.2
                /* JADX WARN: Multi-variable type inference failed */
                @Override // com.google.common.util.concurrent.ClosingFuture.Combiner.AsyncCombiningCallable
                public ClosingFuture<U> call(DeferredCloser closer, Peeker peeker) throws Exception {
                    return function.apply(closer, peeker.getDone(Combiner3.this.future1), peeker.getDone(Combiner3.this.future2), peeker.getDone(Combiner3.this.future3));
                }

                public String toString() {
                    return function.toString();
                }
            }, executor);
        }
    }

    /* loaded from: classes7.dex */
    public static final class Combiner4<V1, V2, V3, V4> extends Combiner {
        private final ClosingFuture<V1> future1;
        private final ClosingFuture<V2> future2;
        private final ClosingFuture<V3> future3;
        private final ClosingFuture<V4> future4;

        /* loaded from: classes7.dex */
        public interface AsyncClosingFunction4<V1, V2, V3, V4, U> {
            ClosingFuture<U> apply(DeferredCloser deferredCloser, @ParametricNullness V1 v1, @ParametricNullness V2 v2, @ParametricNullness V3 v3, @ParametricNullness V4 v4) throws Exception;
        }

        /* loaded from: classes7.dex */
        public interface ClosingFunction4<V1, V2, V3, V4, U> {
            @ParametricNullness
            U apply(DeferredCloser deferredCloser, @ParametricNullness V1 v1, @ParametricNullness V2 v2, @ParametricNullness V3 v3, @ParametricNullness V4 v4) throws Exception;
        }

        private Combiner4(ClosingFuture<V1> future1, ClosingFuture<V2> future2, ClosingFuture<V3> future3, ClosingFuture<V4> future4) {
            super(true, ImmutableList.of((ClosingFuture<V4>) future1, (ClosingFuture<V4>) future2, (ClosingFuture<V4>) future3, future4));
            this.future1 = future1;
            this.future2 = future2;
            this.future3 = future3;
            this.future4 = future4;
        }

        public <U> ClosingFuture<U> call(final ClosingFunction4<V1, V2, V3, V4, U> function, Executor executor) {
            return call(new Combiner.CombiningCallable<U>() { // from class: com.google.common.util.concurrent.ClosingFuture.Combiner4.1
                /* JADX WARN: Multi-variable type inference failed */
                @Override // com.google.common.util.concurrent.ClosingFuture.Combiner.CombiningCallable
                @ParametricNullness
                public U call(DeferredCloser deferredCloser, Peeker peeker) throws Exception {
                    return (U) function.apply(deferredCloser, peeker.getDone(Combiner4.this.future1), peeker.getDone(Combiner4.this.future2), peeker.getDone(Combiner4.this.future3), peeker.getDone(Combiner4.this.future4));
                }

                public String toString() {
                    return function.toString();
                }
            }, executor);
        }

        public <U> ClosingFuture<U> callAsync(final AsyncClosingFunction4<V1, V2, V3, V4, U> function, Executor executor) {
            return callAsync(new Combiner.AsyncCombiningCallable<U>() { // from class: com.google.common.util.concurrent.ClosingFuture.Combiner4.2
                /* JADX WARN: Multi-variable type inference failed */
                @Override // com.google.common.util.concurrent.ClosingFuture.Combiner.AsyncCombiningCallable
                public ClosingFuture<U> call(DeferredCloser closer, Peeker peeker) throws Exception {
                    return function.apply(closer, peeker.getDone(Combiner4.this.future1), peeker.getDone(Combiner4.this.future2), peeker.getDone(Combiner4.this.future3), peeker.getDone(Combiner4.this.future4));
                }

                public String toString() {
                    return function.toString();
                }
            }, executor);
        }
    }

    /* loaded from: classes7.dex */
    public static final class Combiner5<V1, V2, V3, V4, V5> extends Combiner {
        private final ClosingFuture<V1> future1;
        private final ClosingFuture<V2> future2;
        private final ClosingFuture<V3> future3;
        private final ClosingFuture<V4> future4;
        private final ClosingFuture<V5> future5;

        /* loaded from: classes7.dex */
        public interface AsyncClosingFunction5<V1, V2, V3, V4, V5, U> {
            ClosingFuture<U> apply(DeferredCloser deferredCloser, @ParametricNullness V1 v1, @ParametricNullness V2 v2, @ParametricNullness V3 v3, @ParametricNullness V4 v4, @ParametricNullness V5 v5) throws Exception;
        }

        /* loaded from: classes7.dex */
        public interface ClosingFunction5<V1, V2, V3, V4, V5, U> {
            @ParametricNullness
            U apply(DeferredCloser deferredCloser, @ParametricNullness V1 v1, @ParametricNullness V2 v2, @ParametricNullness V3 v3, @ParametricNullness V4 v4, @ParametricNullness V5 v5) throws Exception;
        }

        private Combiner5(ClosingFuture<V1> future1, ClosingFuture<V2> future2, ClosingFuture<V3> future3, ClosingFuture<V4> future4, ClosingFuture<V5> future5) {
            super(true, ImmutableList.of((ClosingFuture<V5>) future1, (ClosingFuture<V5>) future2, (ClosingFuture<V5>) future3, (ClosingFuture<V5>) future4, future5));
            this.future1 = future1;
            this.future2 = future2;
            this.future3 = future3;
            this.future4 = future4;
            this.future5 = future5;
        }

        public <U> ClosingFuture<U> call(final ClosingFunction5<V1, V2, V3, V4, V5, U> function, Executor executor) {
            return call(new Combiner.CombiningCallable<U>() { // from class: com.google.common.util.concurrent.ClosingFuture.Combiner5.1
                /* JADX WARN: Multi-variable type inference failed */
                @Override // com.google.common.util.concurrent.ClosingFuture.Combiner.CombiningCallable
                @ParametricNullness
                public U call(DeferredCloser deferredCloser, Peeker peeker) throws Exception {
                    return (U) function.apply(deferredCloser, peeker.getDone(Combiner5.this.future1), peeker.getDone(Combiner5.this.future2), peeker.getDone(Combiner5.this.future3), peeker.getDone(Combiner5.this.future4), peeker.getDone(Combiner5.this.future5));
                }

                public String toString() {
                    return function.toString();
                }
            }, executor);
        }

        public <U> ClosingFuture<U> callAsync(final AsyncClosingFunction5<V1, V2, V3, V4, V5, U> function, Executor executor) {
            return callAsync(new Combiner.AsyncCombiningCallable<U>() { // from class: com.google.common.util.concurrent.ClosingFuture.Combiner5.2
                /* JADX WARN: Multi-variable type inference failed */
                @Override // com.google.common.util.concurrent.ClosingFuture.Combiner.AsyncCombiningCallable
                public ClosingFuture<U> call(DeferredCloser closer, Peeker peeker) throws Exception {
                    return function.apply(closer, peeker.getDone(Combiner5.this.future1), peeker.getDone(Combiner5.this.future2), peeker.getDone(Combiner5.this.future3), peeker.getDone(Combiner5.this.future4), peeker.getDone(Combiner5.this.future5));
                }

                public String toString() {
                    return function.toString();
                }
            }, executor);
        }
    }

    public String toString() {
        return MoreObjects.toStringHelper(this).add("state", this.state.get()).addValue(this.future).toString();
    }

    protected void finalize() {
        if (this.state.get().equals(State.OPEN)) {
            logger.log(Level.SEVERE, "Uh oh! An open ClosingFuture has leaked and will close: {0}", this);
            finishToFuture();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void closeQuietly(@CheckForNull final Closeable closeable, Executor executor) {
        if (closeable == null) {
            return;
        }
        try {
            executor.execute(new Runnable() { // from class: com.google.common.util.concurrent.ClosingFuture.11
                @Override // java.lang.Runnable
                public void run() {
                    try {
                        closeable.close();
                    } catch (IOException | RuntimeException e) {
                        ClosingFuture.logger.log(Level.WARNING, "thrown by close()", (Throwable) e);
                    }
                }
            });
        } catch (RejectedExecutionException e) {
            Logger logger2 = logger;
            if (logger2.isLoggable(Level.WARNING)) {
                logger2.log(Level.WARNING, String.format("while submitting close to %s; will close inline", executor), (Throwable) e);
            }
            closeQuietly(closeable, MoreExecutors.directExecutor());
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void checkAndUpdateState(State oldState, State newState) {
        Preconditions.checkState(compareAndUpdateState(oldState, newState), "Expected state to be %s, but it was %s", oldState, newState);
    }

    private boolean compareAndUpdateState(State oldState, State newState) {
        return LifecycleKt$$ExternalSyntheticBackportWithForwarding0.m(this.state, oldState, newState);
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes7.dex */
    public static final class CloseableList extends IdentityHashMap<Closeable, Executor> implements Closeable {
        private volatile boolean closed;
        private final DeferredCloser closer;

        @CheckForNull
        private volatile CountDownLatch whenClosed;

        private CloseableList() {
            this.closer = new DeferredCloser(this);
        }

        /* JADX WARN: Multi-variable type inference failed */
        <V, U> ListenableFuture<U> applyClosingFunction(ClosingFunction<? super V, U> transformation, @ParametricNullness V v) throws Exception {
            CloseableList newCloseables = new CloseableList();
            try {
                return Futures.immediateFuture(transformation.apply(newCloseables.closer, v));
            } finally {
                add(newCloseables, MoreExecutors.directExecutor());
            }
        }

        <V, U> FluentFuture<U> applyAsyncClosingFunction(AsyncClosingFunction<V, U> transformation, @ParametricNullness V input) throws Exception {
            CloseableList newCloseables = new CloseableList();
            try {
                ClosingFuture<U> closingFuture = transformation.apply(newCloseables.closer, input);
                closingFuture.becomeSubsumedInto(newCloseables);
                return ((ClosingFuture) closingFuture).future;
            } finally {
                add(newCloseables, MoreExecutors.directExecutor());
            }
        }

        @Override // java.io.Closeable, java.lang.AutoCloseable
        public void close() {
            if (this.closed) {
                return;
            }
            synchronized (this) {
                if (this.closed) {
                    return;
                }
                this.closed = true;
                for (Map.Entry<Closeable, Executor> entry : entrySet()) {
                    ClosingFuture.closeQuietly(entry.getKey(), entry.getValue());
                }
                clear();
                if (this.whenClosed != null) {
                    this.whenClosed.countDown();
                }
            }
        }

        void add(@CheckForNull Closeable closeable, Executor executor) {
            Preconditions.checkNotNull(executor);
            if (closeable == null) {
                return;
            }
            synchronized (this) {
                if (this.closed) {
                    ClosingFuture.closeQuietly(closeable, executor);
                } else {
                    put(closeable, executor);
                }
            }
        }

        CountDownLatch whenClosedCountDown() {
            if (this.closed) {
                return new CountDownLatch(0);
            }
            synchronized (this) {
                if (this.closed) {
                    return new CountDownLatch(0);
                }
                Preconditions.checkState(this.whenClosed == null);
                CountDownLatch countDownLatch = new CountDownLatch(1);
                this.whenClosed = countDownLatch;
                return countDownLatch;
            }
        }
    }

    CountDownLatch whenClosedCountDown() {
        return this.closeables.whenClosedCountDown();
    }
}
