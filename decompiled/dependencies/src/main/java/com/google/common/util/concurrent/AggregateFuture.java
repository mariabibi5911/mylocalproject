package com.google.common.util.concurrent;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableCollection;
import com.google.common.collect.UnmodifiableIterator;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.CheckForNull;

/* JADX INFO: Access modifiers changed from: package-private */
@ElementTypesAreNonnullByDefault
/* loaded from: classes7.dex */
public abstract class AggregateFuture<InputT, OutputT> extends AggregateFutureState<OutputT> {
    private static final Logger logger = Logger.getLogger(AggregateFuture.class.getName());
    private final boolean allMustSucceed;
    private final boolean collectsValues;

    @CheckForNull
    private ImmutableCollection<? extends ListenableFuture<? extends InputT>> futures;

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes7.dex */
    public enum ReleaseResourcesReason {
        OUTPUT_FUTURE_DONE,
        ALL_INPUT_FUTURES_PROCESSED
    }

    abstract void collectOneValue(int i, @ParametricNullness InputT inputt);

    abstract void handleAllCompleted();

    /* JADX INFO: Access modifiers changed from: package-private */
    public AggregateFuture(ImmutableCollection<? extends ListenableFuture<? extends InputT>> futures, boolean allMustSucceed, boolean collectsValues) {
        super(futures.size());
        this.futures = (ImmutableCollection) Preconditions.checkNotNull(futures);
        this.allMustSucceed = allMustSucceed;
        this.collectsValues = collectsValues;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.google.common.util.concurrent.AbstractFuture
    public final void afterDone() {
        super.afterDone();
        ImmutableCollection<? extends Future<?>> localFutures = this.futures;
        releaseResources(ReleaseResourcesReason.OUTPUT_FUTURE_DONE);
        if (isCancelled() & (localFutures != null)) {
            boolean wasInterrupted = wasInterrupted();
            UnmodifiableIterator<? extends Future<?>> it = localFutures.iterator();
            while (it.hasNext()) {
                Future<?> future = it.next();
                future.cancel(wasInterrupted);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.google.common.util.concurrent.AbstractFuture
    @CheckForNull
    public final String pendingToString() {
        ImmutableCollection<? extends Future<?>> localFutures = this.futures;
        if (localFutures != null) {
            String valueOf = String.valueOf(localFutures);
            return new StringBuilder(String.valueOf(valueOf).length() + 8).append("futures=").append(valueOf).toString();
        }
        return super.pendingToString();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public final void init() {
        Objects.requireNonNull(this.futures);
        if (this.futures.isEmpty()) {
            handleAllCompleted();
            return;
        }
        if (this.allMustSucceed) {
            final int index = 0;
            UnmodifiableIterator<? extends ListenableFuture<? extends InputT>> it = this.futures.iterator();
            while (it.hasNext()) {
                final ListenableFuture<? extends InputT> future = it.next();
                int i = index + 1;
                future.addListener(new Runnable() { // from class: com.google.common.util.concurrent.AggregateFuture$$ExternalSyntheticLambda0
                    @Override // java.lang.Runnable
                    public final void run() {
                        AggregateFuture.this.m161lambda$init$0$comgooglecommonutilconcurrentAggregateFuture(future, index);
                    }
                }, MoreExecutors.directExecutor());
                index = i;
            }
            return;
        }
        final ImmutableCollection<? extends Future<? extends InputT>> localFutures = this.collectsValues ? this.futures : null;
        Runnable listener = new Runnable() { // from class: com.google.common.util.concurrent.AggregateFuture$$ExternalSyntheticLambda1
            @Override // java.lang.Runnable
            public final void run() {
                AggregateFuture.this.m162lambda$init$1$comgooglecommonutilconcurrentAggregateFuture(localFutures);
            }
        };
        UnmodifiableIterator<? extends ListenableFuture<? extends InputT>> it2 = this.futures.iterator();
        while (it2.hasNext()) {
            it2.next().addListener(listener, MoreExecutors.directExecutor());
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: lambda$init$0$com-google-common-util-concurrent-AggregateFuture, reason: not valid java name */
    public /* synthetic */ void m161lambda$init$0$comgooglecommonutilconcurrentAggregateFuture(ListenableFuture future, int index) {
        try {
            if (future.isCancelled()) {
                this.futures = null;
                cancel(false);
            } else {
                collectValueFromNonCancelledFuture(index, future);
            }
        } finally {
            m162lambda$init$1$comgooglecommonutilconcurrentAggregateFuture(null);
        }
    }

    private void handleException(Throwable throwable) {
        Preconditions.checkNotNull(throwable);
        if (this.allMustSucceed) {
            boolean completedWithFailure = setException(throwable);
            if (!completedWithFailure) {
                boolean firstTimeSeeingThisException = addCausalChain(getOrInitSeenExceptions(), throwable);
                if (firstTimeSeeingThisException) {
                    log(throwable);
                    return;
                }
            }
        }
        boolean completedWithFailure2 = throwable instanceof Error;
        if (completedWithFailure2) {
            log(throwable);
        }
    }

    private static void log(Throwable throwable) {
        String message;
        if (throwable instanceof Error) {
            message = "Input Future failed with Error";
        } else {
            message = "Got more than one input Future failure. Logging failures after the first";
        }
        logger.log(Level.SEVERE, message, throwable);
    }

    @Override // com.google.common.util.concurrent.AggregateFutureState
    final void addInitialException(Set<Throwable> seen) {
        Preconditions.checkNotNull(seen);
        if (!isCancelled()) {
            addCausalChain(seen, (Throwable) Objects.requireNonNull(tryInternalFastPathGetFailure()));
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    private void collectValueFromNonCancelledFuture(int index, Future<? extends InputT> future) {
        try {
            collectOneValue(index, Futures.getDone(future));
        } catch (ExecutionException e) {
            handleException(e.getCause());
        } catch (Throwable t) {
            handleException(t);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* renamed from: decrementCountAndMaybeComplete, reason: merged with bridge method [inline-methods] */
    public void m162lambda$init$1$comgooglecommonutilconcurrentAggregateFuture(@CheckForNull ImmutableCollection<? extends Future<? extends InputT>> futuresIfNeedToCollectAtCompletion) {
        int newRemaining = decrementRemainingAndGet();
        Preconditions.checkState(newRemaining >= 0, "Less than 0 remaining futures");
        if (newRemaining == 0) {
            processCompleted(futuresIfNeedToCollectAtCompletion);
        }
    }

    private void processCompleted(@CheckForNull ImmutableCollection<? extends Future<? extends InputT>> futuresIfNeedToCollectAtCompletion) {
        if (futuresIfNeedToCollectAtCompletion != null) {
            int i = 0;
            UnmodifiableIterator<? extends Future<? extends InputT>> it = futuresIfNeedToCollectAtCompletion.iterator();
            while (it.hasNext()) {
                Future<? extends InputT> future = it.next();
                if (!future.isCancelled()) {
                    collectValueFromNonCancelledFuture(i, future);
                }
                i++;
            }
        }
        clearSeenExceptions();
        handleAllCompleted();
        releaseResources(ReleaseResourcesReason.ALL_INPUT_FUTURES_PROCESSED);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void releaseResources(ReleaseResourcesReason reason) {
        Preconditions.checkNotNull(reason);
        this.futures = null;
    }

    private static boolean addCausalChain(Set<Throwable> seen, Throwable param) {
        for (Throwable t = param; t != null; t = t.getCause()) {
            boolean firstTimeSeen = seen.add(t);
            if (!firstTimeSeen) {
                return false;
            }
        }
        return true;
    }
}
