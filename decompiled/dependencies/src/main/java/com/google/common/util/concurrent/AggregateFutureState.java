package com.google.common.util.concurrent;

import androidx.concurrent.futures.AbstractResolvableFuture$SafeAtomicHelper$$ExternalSyntheticBackportWithForwarding0;
import com.google.common.collect.Sets;
import com.google.common.util.concurrent.AbstractFuture;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.atomic.AtomicIntegerFieldUpdater;
import java.util.concurrent.atomic.AtomicReferenceFieldUpdater;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.CheckForNull;

@ElementTypesAreNonnullByDefault
/* loaded from: classes7.dex */
abstract class AggregateFutureState<OutputT> extends AbstractFuture.TrustedFuture<OutputT> {
    private static final AtomicHelper ATOMIC_HELPER;
    private static final Logger log = Logger.getLogger(AggregateFutureState.class.getName());
    private volatile int remaining;

    @CheckForNull
    private volatile Set<Throwable> seenExceptions = null;

    abstract void addInitialException(Set<Throwable> set);

    static /* synthetic */ int access$306(AggregateFutureState x0) {
        int i = x0.remaining - 1;
        x0.remaining = i;
        return i;
    }

    static {
        AtomicHelper helper;
        Throwable thrownReflectionFailure = null;
        try {
            helper = new SafeAtomicHelper(AtomicReferenceFieldUpdater.newUpdater(AggregateFutureState.class, Set.class, "seenExceptions"), AtomicIntegerFieldUpdater.newUpdater(AggregateFutureState.class, "remaining"));
        } catch (Throwable reflectionFailure) {
            thrownReflectionFailure = reflectionFailure;
            helper = new SynchronizedAtomicHelper();
        }
        ATOMIC_HELPER = helper;
        if (thrownReflectionFailure != null) {
            log.log(Level.SEVERE, "SafeAtomicHelper is broken!", thrownReflectionFailure);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public AggregateFutureState(int remainingFutures) {
        this.remaining = remainingFutures;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public final Set<Throwable> getOrInitSeenExceptions() {
        Set<Throwable> seenExceptionsLocal = this.seenExceptions;
        if (seenExceptionsLocal == null) {
            Set<Throwable> seenExceptionsLocal2 = Sets.newConcurrentHashSet();
            addInitialException(seenExceptionsLocal2);
            ATOMIC_HELPER.compareAndSetSeenExceptions(this, null, seenExceptionsLocal2);
            return (Set) Objects.requireNonNull(this.seenExceptions);
        }
        return seenExceptionsLocal;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public final int decrementRemainingAndGet() {
        return ATOMIC_HELPER.decrementAndGetRemainingCount(this);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public final void clearSeenExceptions() {
        this.seenExceptions = null;
    }

    /* loaded from: classes7.dex */
    private static abstract class AtomicHelper {
        abstract void compareAndSetSeenExceptions(AggregateFutureState<?> aggregateFutureState, @CheckForNull Set<Throwable> set, Set<Throwable> set2);

        abstract int decrementAndGetRemainingCount(AggregateFutureState<?> aggregateFutureState);

        private AtomicHelper() {
        }
    }

    /* loaded from: classes7.dex */
    private static final class SafeAtomicHelper extends AtomicHelper {
        final AtomicIntegerFieldUpdater<AggregateFutureState<?>> remainingCountUpdater;
        final AtomicReferenceFieldUpdater<AggregateFutureState<?>, Set<Throwable>> seenExceptionsUpdater;

        SafeAtomicHelper(AtomicReferenceFieldUpdater seenExceptionsUpdater, AtomicIntegerFieldUpdater remainingCountUpdater) {
            super();
            this.seenExceptionsUpdater = seenExceptionsUpdater;
            this.remainingCountUpdater = remainingCountUpdater;
        }

        @Override // com.google.common.util.concurrent.AggregateFutureState.AtomicHelper
        void compareAndSetSeenExceptions(AggregateFutureState<?> state, @CheckForNull Set<Throwable> expect, Set<Throwable> update) {
            AbstractResolvableFuture$SafeAtomicHelper$$ExternalSyntheticBackportWithForwarding0.m(this.seenExceptionsUpdater, state, expect, update);
        }

        @Override // com.google.common.util.concurrent.AggregateFutureState.AtomicHelper
        int decrementAndGetRemainingCount(AggregateFutureState<?> state) {
            return this.remainingCountUpdater.decrementAndGet(state);
        }
    }

    /* loaded from: classes7.dex */
    private static final class SynchronizedAtomicHelper extends AtomicHelper {
        private SynchronizedAtomicHelper() {
            super();
        }

        @Override // com.google.common.util.concurrent.AggregateFutureState.AtomicHelper
        void compareAndSetSeenExceptions(AggregateFutureState<?> state, @CheckForNull Set<Throwable> expect, Set<Throwable> update) {
            synchronized (state) {
                if (((AggregateFutureState) state).seenExceptions == expect) {
                    ((AggregateFutureState) state).seenExceptions = update;
                }
            }
        }

        @Override // com.google.common.util.concurrent.AggregateFutureState.AtomicHelper
        int decrementAndGetRemainingCount(AggregateFutureState<?> state) {
            int access$306;
            synchronized (state) {
                access$306 = AggregateFutureState.access$306(state);
            }
            return access$306;
        }
    }
}
