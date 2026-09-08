package com.google.common.util.concurrent;

import androidx.concurrent.futures.AbstractResolvableFuture$SafeAtomicHelper$$ExternalSyntheticBackportWithForwarding0;
import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import com.google.common.base.Throwables;
import com.google.common.util.concurrent.internal.InternalFutureFailureAccess;
import com.google.common.util.concurrent.internal.InternalFutures;
import java.lang.reflect.Field;
import java.security.AccessController;
import java.security.PrivilegedActionException;
import java.security.PrivilegedExceptionAction;
import java.util.Locale;
import java.util.Objects;
import java.util.concurrent.CancellationException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.atomic.AtomicReferenceFieldUpdater;
import java.util.concurrent.locks.LockSupport;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.CheckForNull;
import sun.misc.Unsafe;

@ElementTypesAreNonnullByDefault
/* loaded from: classes7.dex */
public abstract class AbstractFuture<V> extends InternalFutureFailureAccess implements ListenableFuture<V> {
    private static final AtomicHelper ATOMIC_HELPER;
    static final boolean GENERATE_CANCELLATION_CAUSES;
    private static final Object NULL;
    private static final long SPIN_THRESHOLD_NANOS = 1000;
    private static final Logger log;

    @CheckForNull
    private volatile Listener listeners;

    @CheckForNull
    private volatile Object value;

    @CheckForNull
    private volatile Waiter waiters;

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes7.dex */
    public interface Trusted<V> extends ListenableFuture<V> {
    }

    static {
        boolean generateCancellationCauses;
        AtomicHelper helper;
        try {
            generateCancellationCauses = Boolean.parseBoolean(System.getProperty("guava.concurrent.generate_cancellation_cause", "false"));
        } catch (SecurityException e) {
            generateCancellationCauses = false;
        }
        GENERATE_CANCELLATION_CAUSES = generateCancellationCauses;
        log = Logger.getLogger(AbstractFuture.class.getName());
        Throwable thrownUnsafeFailure = null;
        Throwable thrownAtomicReferenceFieldUpdaterFailure = null;
        try {
            helper = new UnsafeAtomicHelper();
        } catch (Throwable unsafeFailure) {
            thrownUnsafeFailure = unsafeFailure;
            try {
                helper = new SafeAtomicHelper(AtomicReferenceFieldUpdater.newUpdater(Waiter.class, Thread.class, "thread"), AtomicReferenceFieldUpdater.newUpdater(Waiter.class, Waiter.class, "next"), AtomicReferenceFieldUpdater.newUpdater(AbstractFuture.class, Waiter.class, "waiters"), AtomicReferenceFieldUpdater.newUpdater(AbstractFuture.class, Listener.class, "listeners"), AtomicReferenceFieldUpdater.newUpdater(AbstractFuture.class, Object.class, "value"));
            } catch (Throwable atomicReferenceFieldUpdaterFailure) {
                thrownAtomicReferenceFieldUpdaterFailure = atomicReferenceFieldUpdaterFailure;
                helper = new SynchronizedHelper();
            }
        }
        ATOMIC_HELPER = helper;
        if (thrownAtomicReferenceFieldUpdaterFailure != null) {
            Logger logger = log;
            logger.log(Level.SEVERE, "UnsafeAtomicHelper is broken!", thrownUnsafeFailure);
            logger.log(Level.SEVERE, "SafeAtomicHelper is broken!", thrownAtomicReferenceFieldUpdaterFailure);
        }
        NULL = new Object();
    }

    /* loaded from: classes7.dex */
    static abstract class TrustedFuture<V> extends AbstractFuture<V> implements Trusted<V> {
        @Override // com.google.common.util.concurrent.AbstractFuture, java.util.concurrent.Future
        @ParametricNullness
        public final V get() throws InterruptedException, ExecutionException {
            return (V) super.get();
        }

        @Override // com.google.common.util.concurrent.AbstractFuture, java.util.concurrent.Future
        @ParametricNullness
        public final V get(long j, TimeUnit timeUnit) throws InterruptedException, ExecutionException, TimeoutException {
            return (V) super.get(j, timeUnit);
        }

        @Override // com.google.common.util.concurrent.AbstractFuture, java.util.concurrent.Future
        public final boolean isDone() {
            return super.isDone();
        }

        @Override // com.google.common.util.concurrent.AbstractFuture, java.util.concurrent.Future
        public final boolean isCancelled() {
            return super.isCancelled();
        }

        @Override // com.google.common.util.concurrent.AbstractFuture, com.google.common.util.concurrent.ListenableFuture
        public final void addListener(Runnable listener, Executor executor) {
            super.addListener(listener, executor);
        }

        @Override // com.google.common.util.concurrent.AbstractFuture, java.util.concurrent.Future
        public final boolean cancel(boolean mayInterruptIfRunning) {
            return super.cancel(mayInterruptIfRunning);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes7.dex */
    public static final class Waiter {
        static final Waiter TOMBSTONE = new Waiter(false);

        @CheckForNull
        volatile Waiter next;

        @CheckForNull
        volatile Thread thread;

        Waiter(boolean unused) {
        }

        Waiter() {
            AbstractFuture.ATOMIC_HELPER.putThread(this, Thread.currentThread());
        }

        void setNext(@CheckForNull Waiter next) {
            AbstractFuture.ATOMIC_HELPER.putNext(this, next);
        }

        void unpark() {
            Thread w = this.thread;
            if (w != null) {
                this.thread = null;
                LockSupport.unpark(w);
            }
        }
    }

    private void removeWaiter(Waiter node) {
        node.thread = null;
        while (true) {
            Waiter pred = null;
            Waiter curr = this.waiters;
            if (curr == Waiter.TOMBSTONE) {
                return;
            }
            while (curr != null) {
                Waiter succ = curr.next;
                if (curr.thread != null) {
                    pred = curr;
                } else if (pred != null) {
                    pred.next = succ;
                    if (pred.thread == null) {
                        break;
                    }
                } else if (!ATOMIC_HELPER.casWaiters(this, curr, succ)) {
                    break;
                }
                curr = succ;
            }
            return;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes7.dex */
    public static final class Listener {
        static final Listener TOMBSTONE = new Listener();

        @CheckForNull
        final Executor executor;

        @CheckForNull
        Listener next;

        @CheckForNull
        final Runnable task;

        Listener(Runnable task, Executor executor) {
            this.task = task;
            this.executor = executor;
        }

        Listener() {
            this.task = null;
            this.executor = null;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes7.dex */
    public static final class Failure {
        static final Failure FALLBACK_INSTANCE = new Failure(new Throwable("Failure occurred while trying to finish a future.") { // from class: com.google.common.util.concurrent.AbstractFuture.Failure.1
            @Override // java.lang.Throwable
            public synchronized Throwable fillInStackTrace() {
                return this;
            }
        });
        final Throwable exception;

        Failure(Throwable exception) {
            this.exception = (Throwable) Preconditions.checkNotNull(exception);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes7.dex */
    public static final class Cancellation {

        @CheckForNull
        static final Cancellation CAUSELESS_CANCELLED;

        @CheckForNull
        static final Cancellation CAUSELESS_INTERRUPTED;

        @CheckForNull
        final Throwable cause;
        final boolean wasInterrupted;

        static {
            if (AbstractFuture.GENERATE_CANCELLATION_CAUSES) {
                CAUSELESS_CANCELLED = null;
                CAUSELESS_INTERRUPTED = null;
            } else {
                CAUSELESS_CANCELLED = new Cancellation(false, null);
                CAUSELESS_INTERRUPTED = new Cancellation(true, null);
            }
        }

        Cancellation(boolean wasInterrupted, @CheckForNull Throwable cause) {
            this.wasInterrupted = wasInterrupted;
            this.cause = cause;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes7.dex */
    public static final class SetFuture<V> implements Runnable {
        final ListenableFuture<? extends V> future;
        final AbstractFuture<V> owner;

        SetFuture(AbstractFuture<V> owner, ListenableFuture<? extends V> future) {
            this.owner = owner;
            this.future = future;
        }

        @Override // java.lang.Runnable
        public void run() {
            if (((AbstractFuture) this.owner).value == this) {
                Object valueToSet = AbstractFuture.getFutureValue(this.future);
                if (AbstractFuture.ATOMIC_HELPER.casValue(this.owner, this, valueToSet)) {
                    AbstractFuture.complete(this.owner);
                }
            }
        }
    }

    @Override // java.util.concurrent.Future
    @ParametricNullness
    public V get(long timeout, TimeUnit unit) throws InterruptedException, TimeoutException, ExecutionException {
        long timeoutNanos = unit.toNanos(timeout);
        long remainingNanos = timeoutNanos;
        if (Thread.interrupted()) {
            throw new InterruptedException();
        }
        Object localValue = this.value;
        if ((localValue != null) & (!(localValue instanceof SetFuture))) {
            return getDoneValue(localValue);
        }
        long endNanos = remainingNanos > 0 ? System.nanoTime() + remainingNanos : 0L;
        if (remainingNanos >= SPIN_THRESHOLD_NANOS) {
            Waiter oldHead = this.waiters;
            if (oldHead != Waiter.TOMBSTONE) {
                Waiter node = new Waiter();
                do {
                    node.setNext(oldHead);
                    if (ATOMIC_HELPER.casWaiters(this, oldHead, node)) {
                        do {
                            OverflowAvoidingLockSupport.parkNanos(this, remainingNanos);
                            if (Thread.interrupted()) {
                                removeWaiter(node);
                                throw new InterruptedException();
                            }
                            Object localValue2 = this.value;
                            if ((localValue2 != null) & (!(localValue2 instanceof SetFuture))) {
                                return getDoneValue(localValue2);
                            }
                            remainingNanos = endNanos - System.nanoTime();
                        } while (remainingNanos >= SPIN_THRESHOLD_NANOS);
                        removeWaiter(node);
                    } else {
                        oldHead = this.waiters;
                    }
                } while (oldHead != Waiter.TOMBSTONE);
            }
            return getDoneValue(Objects.requireNonNull(this.value));
        }
        while (remainingNanos > 0) {
            Object localValue3 = this.value;
            if ((localValue3 != null) & (!(localValue3 instanceof SetFuture))) {
                return getDoneValue(localValue3);
            }
            if (Thread.interrupted()) {
                throw new InterruptedException();
            }
            remainingNanos = endNanos - System.nanoTime();
        }
        String futureToString = toString();
        String unitString = unit.toString().toLowerCase(Locale.ROOT);
        String lowerCase = unit.toString().toLowerCase(Locale.ROOT);
        String message = new StringBuilder(String.valueOf(lowerCase).length() + 28).append("Waited ").append(timeout).append(" ").append(lowerCase).toString();
        if (remainingNanos + SPIN_THRESHOLD_NANOS < 0) {
            String message2 = String.valueOf(message).concat(" (plus ");
            long overWaitNanos = -remainingNanos;
            long overWaitUnits = unit.convert(overWaitNanos, TimeUnit.NANOSECONDS);
            long remainingNanos2 = overWaitNanos - unit.toNanos(overWaitUnits);
            boolean shouldShowExtraNanos = overWaitUnits == 0 || remainingNanos2 > SPIN_THRESHOLD_NANOS;
            if (overWaitUnits > 0) {
                String valueOf = String.valueOf(message2);
                String message3 = new StringBuilder(String.valueOf(valueOf).length() + 21 + String.valueOf(unitString).length()).append(valueOf).append(overWaitUnits).append(" ").append(unitString).toString();
                if (shouldShowExtraNanos) {
                    message3 = String.valueOf(message3).concat(",");
                }
                message2 = String.valueOf(message3).concat(" ");
            }
            if (shouldShowExtraNanos) {
                String valueOf2 = String.valueOf(message2);
                message2 = new StringBuilder(String.valueOf(valueOf2).length() + 33).append(valueOf2).append(remainingNanos2).append(" nanoseconds ").toString();
            }
            message = String.valueOf(message2).concat("delay)");
        }
        if (isDone()) {
            throw new TimeoutException(String.valueOf(message).concat(" but future completed as timeout expired"));
        }
        throw new TimeoutException(new StringBuilder(String.valueOf(message).length() + 5 + String.valueOf(futureToString).length()).append(message).append(" for ").append(futureToString).toString());
    }

    @Override // java.util.concurrent.Future
    @ParametricNullness
    public V get() throws InterruptedException, ExecutionException {
        Object localValue;
        if (Thread.interrupted()) {
            throw new InterruptedException();
        }
        Object localValue2 = this.value;
        if ((localValue2 != null) & (!(localValue2 instanceof SetFuture))) {
            return getDoneValue(localValue2);
        }
        Waiter oldHead = this.waiters;
        if (oldHead != Waiter.TOMBSTONE) {
            Waiter node = new Waiter();
            do {
                node.setNext(oldHead);
                if (ATOMIC_HELPER.casWaiters(this, oldHead, node)) {
                    do {
                        LockSupport.park(this);
                        if (Thread.interrupted()) {
                            removeWaiter(node);
                            throw new InterruptedException();
                        }
                        localValue = this.value;
                    } while (!((localValue != null) & (!(localValue instanceof SetFuture))));
                    return getDoneValue(localValue);
                }
                oldHead = this.waiters;
            } while (oldHead != Waiter.TOMBSTONE);
        }
        return getDoneValue(Objects.requireNonNull(this.value));
    }

    /* JADX WARN: Multi-variable type inference failed */
    @ParametricNullness
    private V getDoneValue(Object obj) throws ExecutionException {
        if (obj instanceof Cancellation) {
            throw cancellationExceptionWithCause("Task was cancelled.", ((Cancellation) obj).cause);
        }
        if (obj instanceof Failure) {
            throw new ExecutionException(((Failure) obj).exception);
        }
        if (obj == NULL) {
            return (V) NullnessCasts.uncheckedNull();
        }
        return obj;
    }

    @Override // java.util.concurrent.Future
    public boolean isDone() {
        Object localValue = this.value;
        return (true ^ (localValue instanceof SetFuture)) & (localValue != null);
    }

    @Override // java.util.concurrent.Future
    public boolean isCancelled() {
        Object localValue = this.value;
        return localValue instanceof Cancellation;
    }

    @Override // java.util.concurrent.Future
    public boolean cancel(boolean mayInterruptIfRunning) {
        Cancellation cancellation;
        Object valueToSet;
        Object localValue = this.value;
        boolean rValue = false;
        if ((localValue == null) | (localValue instanceof SetFuture)) {
            if (GENERATE_CANCELLATION_CAUSES) {
                valueToSet = new Cancellation(mayInterruptIfRunning, new CancellationException("Future.cancel() was called."));
            } else {
                if (mayInterruptIfRunning) {
                    cancellation = Cancellation.CAUSELESS_INTERRUPTED;
                } else {
                    cancellation = Cancellation.CAUSELESS_CANCELLED;
                }
                valueToSet = Objects.requireNonNull(cancellation);
            }
            AbstractFuture<V> abstractFuture = this;
            while (true) {
                if (ATOMIC_HELPER.casValue(abstractFuture, localValue, valueToSet)) {
                    rValue = true;
                    if (mayInterruptIfRunning) {
                        abstractFuture.interruptTask();
                    }
                    complete(abstractFuture);
                    if (!(localValue instanceof SetFuture)) {
                        break;
                    }
                    ListenableFuture<?> futureToPropagateTo = ((SetFuture) localValue).future;
                    if (futureToPropagateTo instanceof Trusted) {
                        AbstractFuture<V> abstractFuture2 = (AbstractFuture) futureToPropagateTo;
                        localValue = abstractFuture2.value;
                        if (!(localValue == null) && !(localValue instanceof SetFuture)) {
                            break;
                        }
                        abstractFuture = abstractFuture2;
                    } else {
                        futureToPropagateTo.cancel(mayInterruptIfRunning);
                        break;
                    }
                } else {
                    localValue = abstractFuture.value;
                    if (!(localValue instanceof SetFuture)) {
                        break;
                    }
                }
            }
        }
        return rValue;
    }

    protected void interruptTask() {
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public final boolean wasInterrupted() {
        Object localValue = this.value;
        return (localValue instanceof Cancellation) && ((Cancellation) localValue).wasInterrupted;
    }

    @Override // com.google.common.util.concurrent.ListenableFuture
    public void addListener(Runnable listener, Executor executor) {
        Listener oldHead;
        Preconditions.checkNotNull(listener, "Runnable was null.");
        Preconditions.checkNotNull(executor, "Executor was null.");
        if (!isDone() && (oldHead = this.listeners) != Listener.TOMBSTONE) {
            Listener newNode = new Listener(listener, executor);
            do {
                newNode.next = oldHead;
                if (ATOMIC_HELPER.casListeners(this, oldHead, newNode)) {
                    return;
                } else {
                    oldHead = this.listeners;
                }
            } while (oldHead != Listener.TOMBSTONE);
        }
        executeListener(listener, executor);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public boolean set(@ParametricNullness V value) {
        Object valueToSet = value == null ? NULL : value;
        if (ATOMIC_HELPER.casValue(this, null, valueToSet)) {
            complete(this);
            return true;
        }
        return false;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public boolean setException(Throwable throwable) {
        Object valueToSet = new Failure((Throwable) Preconditions.checkNotNull(throwable));
        if (ATOMIC_HELPER.casValue(this, null, valueToSet)) {
            complete(this);
            return true;
        }
        return false;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public boolean setFuture(ListenableFuture<? extends V> future) {
        Failure failure;
        Preconditions.checkNotNull(future);
        Object localValue = this.value;
        if (localValue == null) {
            if (future.isDone()) {
                Object value = getFutureValue(future);
                if (!ATOMIC_HELPER.casValue(this, null, value)) {
                    return false;
                }
                complete(this);
                return true;
            }
            SetFuture<V> valueToSet = new SetFuture<>(this, future);
            if (ATOMIC_HELPER.casValue(this, null, valueToSet)) {
                try {
                    future.addListener(valueToSet, DirectExecutor.INSTANCE);
                } catch (Throwable t) {
                    try {
                        failure = new Failure(t);
                    } catch (Throwable th) {
                        failure = Failure.FALLBACK_INSTANCE;
                    }
                    ATOMIC_HELPER.casValue(this, valueToSet, failure);
                }
                return true;
            }
            localValue = this.value;
        }
        if (localValue instanceof Cancellation) {
            future.cancel(((Cancellation) localValue).wasInterrupted);
        }
        return false;
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX WARN: Multi-variable type inference failed */
    public static Object getFutureValue(ListenableFuture<?> listenableFuture) {
        Throwable throwable;
        Cancellation cancellation;
        if (listenableFuture instanceof Trusted) {
            Object v = ((AbstractFuture) listenableFuture).value;
            if (v instanceof Cancellation) {
                Cancellation c = (Cancellation) v;
                if (c.wasInterrupted) {
                    if (c.cause != null) {
                        cancellation = new Cancellation(false, c.cause);
                    } else {
                        cancellation = Cancellation.CAUSELESS_CANCELLED;
                    }
                    v = cancellation;
                }
            }
            return Objects.requireNonNull(v);
        }
        if ((listenableFuture instanceof InternalFutureFailureAccess) && (throwable = InternalFutures.tryInternalFastPathGetFailure((InternalFutureFailureAccess) listenableFuture)) != null) {
            return new Failure(throwable);
        }
        boolean wasCancelled = listenableFuture.isCancelled();
        if ((!GENERATE_CANCELLATION_CAUSES) & wasCancelled) {
            return Objects.requireNonNull(Cancellation.CAUSELESS_CANCELLED);
        }
        try {
            Object v2 = getUninterruptibly(listenableFuture);
            if (!wasCancelled) {
                return v2 == null ? NULL : v2;
            }
            String valueOf = String.valueOf(listenableFuture);
            return new Cancellation(false, new IllegalArgumentException(new StringBuilder(String.valueOf(valueOf).length() + 84).append("get() did not throw CancellationException, despite reporting isCancelled() == true: ").append(valueOf).toString()));
        } catch (CancellationException cancellation2) {
            if (!wasCancelled) {
                String valueOf2 = String.valueOf(listenableFuture);
                return new Failure(new IllegalArgumentException(new StringBuilder(String.valueOf(valueOf2).length() + 77).append("get() threw CancellationException, despite reporting isCancelled() == false: ").append(valueOf2).toString(), cancellation2));
            }
            return new Cancellation(false, cancellation2);
        } catch (ExecutionException exception) {
            if (wasCancelled) {
                String valueOf3 = String.valueOf(listenableFuture);
                return new Cancellation(false, new IllegalArgumentException(new StringBuilder(String.valueOf(valueOf3).length() + 84).append("get() did not throw CancellationException, despite reporting isCancelled() == true: ").append(valueOf3).toString(), exception));
            }
            return new Failure(exception.getCause());
        } catch (Throwable t) {
            return new Failure(t);
        }
    }

    @ParametricNullness
    private static <V> V getUninterruptibly(Future<V> future) throws ExecutionException {
        V v;
        boolean interrupted = false;
        while (true) {
            try {
                v = future.get();
                break;
            } catch (InterruptedException e) {
                interrupted = true;
            } catch (Throwable th) {
                if (interrupted) {
                    Thread.currentThread().interrupt();
                }
                throw th;
            }
        }
        if (interrupted) {
            Thread.currentThread().interrupt();
        }
        return v;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void complete(AbstractFuture<?> param) {
        AbstractFuture<?> future = param;
        Listener next = null;
        while (true) {
            future.releaseWaiters();
            future.afterDone();
            next = future.clearListeners(next);
            while (next != null) {
                Listener curr = next;
                next = next.next;
                Runnable task = (Runnable) Objects.requireNonNull(curr.task);
                if (task instanceof SetFuture) {
                    SetFuture<?> setFuture = (SetFuture) task;
                    future = setFuture.owner;
                    if (((AbstractFuture) future).value == setFuture) {
                        Object valueToSet = getFutureValue(setFuture.future);
                        if (ATOMIC_HELPER.casValue(future, setFuture, valueToSet)) {
                            break;
                        }
                    } else {
                        continue;
                    }
                } else {
                    executeListener(task, (Executor) Objects.requireNonNull(curr.executor));
                }
            }
            return;
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void afterDone() {
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.google.common.util.concurrent.internal.InternalFutureFailureAccess
    @CheckForNull
    public final Throwable tryInternalFastPathGetFailure() {
        if (this instanceof Trusted) {
            Object obj = this.value;
            if (obj instanceof Failure) {
                return ((Failure) obj).exception;
            }
            return null;
        }
        return null;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public final void maybePropagateCancellationTo(@CheckForNull Future<?> related) {
        if ((related != null) & isCancelled()) {
            related.cancel(wasInterrupted());
        }
    }

    private void releaseWaiters() {
        Waiter head = ATOMIC_HELPER.gasWaiters(this, Waiter.TOMBSTONE);
        for (Waiter currentWaiter = head; currentWaiter != null; currentWaiter = currentWaiter.next) {
            currentWaiter.unpark();
        }
    }

    @CheckForNull
    private Listener clearListeners(@CheckForNull Listener onto) {
        Listener head = ATOMIC_HELPER.gasListeners(this, Listener.TOMBSTONE);
        Listener reversedList = onto;
        while (head != null) {
            Listener tmp = head;
            head = head.next;
            tmp.next = reversedList;
            reversedList = tmp;
        }
        return reversedList;
    }

    public String toString() {
        StringBuilder builder = new StringBuilder();
        if (getClass().getName().startsWith("com.google.common.util.concurrent.")) {
            builder.append(getClass().getSimpleName());
        } else {
            builder.append(getClass().getName());
        }
        builder.append('@').append(Integer.toHexString(System.identityHashCode(this))).append("[status=");
        if (isCancelled()) {
            builder.append("CANCELLED");
        } else if (isDone()) {
            addDoneString(builder);
        } else {
            addPendingString(builder);
        }
        return builder.append("]").toString();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    /* JADX WARN: Multi-variable type inference failed */
    @CheckForNull
    public String pendingToString() {
        if (this instanceof ScheduledFuture) {
            return new StringBuilder(41).append("remaining delay=[").append(((ScheduledFuture) this).getDelay(TimeUnit.MILLISECONDS)).append(" ms]").toString();
        }
        return null;
    }

    private void addPendingString(StringBuilder builder) {
        String pendingDescription;
        int truncateLength = builder.length();
        builder.append("PENDING");
        Object localValue = this.value;
        if (localValue instanceof SetFuture) {
            builder.append(", setFuture=[");
            appendUserObject(builder, ((SetFuture) localValue).future);
            builder.append("]");
        } else {
            try {
                pendingDescription = Strings.emptyToNull(pendingToString());
            } catch (RuntimeException | StackOverflowError e) {
                String valueOf = String.valueOf(e.getClass());
                pendingDescription = new StringBuilder(String.valueOf(valueOf).length() + 38).append("Exception thrown from implementation: ").append(valueOf).toString();
            }
            if (pendingDescription != null) {
                builder.append(", info=[").append(pendingDescription).append("]");
            }
        }
        if (isDone()) {
            builder.delete(truncateLength, builder.length());
            addDoneString(builder);
        }
    }

    private void addDoneString(StringBuilder builder) {
        try {
            Object uninterruptibly = getUninterruptibly(this);
            builder.append("SUCCESS, result=[");
            appendResultObject(builder, uninterruptibly);
            builder.append("]");
        } catch (CancellationException e) {
            builder.append("CANCELLED");
        } catch (RuntimeException e2) {
            builder.append("UNKNOWN, cause=[").append(e2.getClass()).append(" thrown from get()]");
        } catch (ExecutionException e3) {
            builder.append("FAILURE, cause=[").append(e3.getCause()).append("]");
        }
    }

    private void appendResultObject(StringBuilder builder, @CheckForNull Object o) {
        if (o == null) {
            builder.append("null");
        } else if (o == this) {
            builder.append("this future");
        } else {
            builder.append(o.getClass().getName()).append("@").append(Integer.toHexString(System.identityHashCode(o)));
        }
    }

    private void appendUserObject(StringBuilder builder, @CheckForNull Object o) {
        try {
            if (o == this) {
                builder.append("this future");
            } else {
                builder.append(o);
            }
        } catch (RuntimeException | StackOverflowError e) {
            builder.append("Exception thrown from implementation: ").append(e.getClass());
        }
    }

    private static void executeListener(Runnable runnable, Executor executor) {
        try {
            executor.execute(runnable);
        } catch (RuntimeException e) {
            Logger logger = log;
            Level level = Level.SEVERE;
            String valueOf = String.valueOf(runnable);
            String valueOf2 = String.valueOf(executor);
            logger.log(level, new StringBuilder(String.valueOf(valueOf).length() + 57 + String.valueOf(valueOf2).length()).append("RuntimeException while executing runnable ").append(valueOf).append(" with executor ").append(valueOf2).toString(), (Throwable) e);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes7.dex */
    public static abstract class AtomicHelper {
        abstract boolean casListeners(AbstractFuture<?> abstractFuture, @CheckForNull Listener listener, Listener listener2);

        abstract boolean casValue(AbstractFuture<?> abstractFuture, @CheckForNull Object obj, Object obj2);

        abstract boolean casWaiters(AbstractFuture<?> abstractFuture, @CheckForNull Waiter waiter, @CheckForNull Waiter waiter2);

        abstract Listener gasListeners(AbstractFuture<?> abstractFuture, Listener listener);

        abstract Waiter gasWaiters(AbstractFuture<?> abstractFuture, Waiter waiter);

        abstract void putNext(Waiter waiter, @CheckForNull Waiter waiter2);

        abstract void putThread(Waiter waiter, Thread thread);

        private AtomicHelper() {
        }
    }

    /* loaded from: classes7.dex */
    private static final class UnsafeAtomicHelper extends AtomicHelper {
        static final long LISTENERS_OFFSET;
        static final Unsafe UNSAFE;
        static final long VALUE_OFFSET;
        static final long WAITERS_OFFSET;
        static final long WAITER_NEXT_OFFSET;
        static final long WAITER_THREAD_OFFSET;

        private UnsafeAtomicHelper() {
            super();
        }

        static {
            Unsafe unsafe;
            try {
                unsafe = Unsafe.getUnsafe();
            } catch (SecurityException e) {
                try {
                    unsafe = (Unsafe) AccessController.doPrivileged(new PrivilegedExceptionAction<Unsafe>() { // from class: com.google.common.util.concurrent.AbstractFuture.UnsafeAtomicHelper.1
                        @Override // java.security.PrivilegedExceptionAction
                        public Unsafe run() throws Exception {
                            for (Field f : Unsafe.class.getDeclaredFields()) {
                                f.setAccessible(true);
                                Object x = f.get(null);
                                if (Unsafe.class.isInstance(x)) {
                                    return (Unsafe) Unsafe.class.cast(x);
                                }
                            }
                            throw new NoSuchFieldError("the Unsafe");
                        }
                    });
                } catch (PrivilegedActionException e2) {
                    throw new RuntimeException("Could not initialize intrinsics", e2.getCause());
                }
            }
            try {
                WAITERS_OFFSET = unsafe.objectFieldOffset(AbstractFuture.class.getDeclaredField("waiters"));
                LISTENERS_OFFSET = unsafe.objectFieldOffset(AbstractFuture.class.getDeclaredField("listeners"));
                VALUE_OFFSET = unsafe.objectFieldOffset(AbstractFuture.class.getDeclaredField("value"));
                WAITER_THREAD_OFFSET = unsafe.objectFieldOffset(Waiter.class.getDeclaredField("thread"));
                WAITER_NEXT_OFFSET = unsafe.objectFieldOffset(Waiter.class.getDeclaredField("next"));
                UNSAFE = unsafe;
            } catch (Exception e3) {
                Throwables.throwIfUnchecked(e3);
                throw new RuntimeException(e3);
            }
        }

        @Override // com.google.common.util.concurrent.AbstractFuture.AtomicHelper
        void putThread(Waiter waiter, Thread newValue) {
            UNSAFE.putObject(waiter, WAITER_THREAD_OFFSET, newValue);
        }

        @Override // com.google.common.util.concurrent.AbstractFuture.AtomicHelper
        void putNext(Waiter waiter, @CheckForNull Waiter newValue) {
            UNSAFE.putObject(waiter, WAITER_NEXT_OFFSET, newValue);
        }

        @Override // com.google.common.util.concurrent.AbstractFuture.AtomicHelper
        boolean casWaiters(AbstractFuture<?> future, @CheckForNull Waiter expect, @CheckForNull Waiter update) {
            return AbstractFuture$UnsafeAtomicHelper$$ExternalSyntheticBackportWithForwarding0.m(UNSAFE, future, WAITERS_OFFSET, expect, update);
        }

        @Override // com.google.common.util.concurrent.AbstractFuture.AtomicHelper
        boolean casListeners(AbstractFuture<?> future, @CheckForNull Listener expect, Listener update) {
            return AbstractFuture$UnsafeAtomicHelper$$ExternalSyntheticBackportWithForwarding0.m(UNSAFE, future, LISTENERS_OFFSET, expect, update);
        }

        @Override // com.google.common.util.concurrent.AbstractFuture.AtomicHelper
        Listener gasListeners(AbstractFuture<?> future, Listener update) {
            Listener listener;
            do {
                listener = ((AbstractFuture) future).listeners;
                if (update == listener) {
                    return listener;
                }
            } while (!casListeners(future, listener, update));
            return listener;
        }

        @Override // com.google.common.util.concurrent.AbstractFuture.AtomicHelper
        Waiter gasWaiters(AbstractFuture<?> future, Waiter update) {
            Waiter waiter;
            do {
                waiter = ((AbstractFuture) future).waiters;
                if (update == waiter) {
                    return waiter;
                }
            } while (!casWaiters(future, waiter, update));
            return waiter;
        }

        @Override // com.google.common.util.concurrent.AbstractFuture.AtomicHelper
        boolean casValue(AbstractFuture<?> future, @CheckForNull Object expect, Object update) {
            return AbstractFuture$UnsafeAtomicHelper$$ExternalSyntheticBackportWithForwarding0.m(UNSAFE, future, VALUE_OFFSET, expect, update);
        }
    }

    /* loaded from: classes7.dex */
    private static final class SafeAtomicHelper extends AtomicHelper {
        final AtomicReferenceFieldUpdater<AbstractFuture, Listener> listenersUpdater;
        final AtomicReferenceFieldUpdater<AbstractFuture, Object> valueUpdater;
        final AtomicReferenceFieldUpdater<Waiter, Waiter> waiterNextUpdater;
        final AtomicReferenceFieldUpdater<Waiter, Thread> waiterThreadUpdater;
        final AtomicReferenceFieldUpdater<AbstractFuture, Waiter> waitersUpdater;

        SafeAtomicHelper(AtomicReferenceFieldUpdater<Waiter, Thread> waiterThreadUpdater, AtomicReferenceFieldUpdater<Waiter, Waiter> waiterNextUpdater, AtomicReferenceFieldUpdater<AbstractFuture, Waiter> waitersUpdater, AtomicReferenceFieldUpdater<AbstractFuture, Listener> listenersUpdater, AtomicReferenceFieldUpdater<AbstractFuture, Object> valueUpdater) {
            super();
            this.waiterThreadUpdater = waiterThreadUpdater;
            this.waiterNextUpdater = waiterNextUpdater;
            this.waitersUpdater = waitersUpdater;
            this.listenersUpdater = listenersUpdater;
            this.valueUpdater = valueUpdater;
        }

        @Override // com.google.common.util.concurrent.AbstractFuture.AtomicHelper
        void putThread(Waiter waiter, Thread newValue) {
            this.waiterThreadUpdater.lazySet(waiter, newValue);
        }

        @Override // com.google.common.util.concurrent.AbstractFuture.AtomicHelper
        void putNext(Waiter waiter, @CheckForNull Waiter newValue) {
            this.waiterNextUpdater.lazySet(waiter, newValue);
        }

        @Override // com.google.common.util.concurrent.AbstractFuture.AtomicHelper
        boolean casWaiters(AbstractFuture<?> future, @CheckForNull Waiter expect, @CheckForNull Waiter update) {
            return AbstractResolvableFuture$SafeAtomicHelper$$ExternalSyntheticBackportWithForwarding0.m(this.waitersUpdater, future, expect, update);
        }

        @Override // com.google.common.util.concurrent.AbstractFuture.AtomicHelper
        boolean casListeners(AbstractFuture<?> future, @CheckForNull Listener expect, Listener update) {
            return AbstractResolvableFuture$SafeAtomicHelper$$ExternalSyntheticBackportWithForwarding0.m(this.listenersUpdater, future, expect, update);
        }

        @Override // com.google.common.util.concurrent.AbstractFuture.AtomicHelper
        Listener gasListeners(AbstractFuture<?> future, Listener update) {
            return this.listenersUpdater.getAndSet(future, update);
        }

        @Override // com.google.common.util.concurrent.AbstractFuture.AtomicHelper
        Waiter gasWaiters(AbstractFuture<?> future, Waiter update) {
            return this.waitersUpdater.getAndSet(future, update);
        }

        @Override // com.google.common.util.concurrent.AbstractFuture.AtomicHelper
        boolean casValue(AbstractFuture<?> future, @CheckForNull Object expect, Object update) {
            return AbstractResolvableFuture$SafeAtomicHelper$$ExternalSyntheticBackportWithForwarding0.m(this.valueUpdater, future, expect, update);
        }
    }

    /* loaded from: classes7.dex */
    private static final class SynchronizedHelper extends AtomicHelper {
        private SynchronizedHelper() {
            super();
        }

        @Override // com.google.common.util.concurrent.AbstractFuture.AtomicHelper
        void putThread(Waiter waiter, Thread newValue) {
            waiter.thread = newValue;
        }

        @Override // com.google.common.util.concurrent.AbstractFuture.AtomicHelper
        void putNext(Waiter waiter, @CheckForNull Waiter newValue) {
            waiter.next = newValue;
        }

        @Override // com.google.common.util.concurrent.AbstractFuture.AtomicHelper
        boolean casWaiters(AbstractFuture<?> future, @CheckForNull Waiter expect, @CheckForNull Waiter update) {
            synchronized (future) {
                if (((AbstractFuture) future).waiters != expect) {
                    return false;
                }
                ((AbstractFuture) future).waiters = update;
                return true;
            }
        }

        @Override // com.google.common.util.concurrent.AbstractFuture.AtomicHelper
        boolean casListeners(AbstractFuture<?> future, @CheckForNull Listener expect, Listener update) {
            synchronized (future) {
                if (((AbstractFuture) future).listeners != expect) {
                    return false;
                }
                ((AbstractFuture) future).listeners = update;
                return true;
            }
        }

        @Override // com.google.common.util.concurrent.AbstractFuture.AtomicHelper
        Listener gasListeners(AbstractFuture<?> future, Listener update) {
            Listener old;
            synchronized (future) {
                old = ((AbstractFuture) future).listeners;
                if (old != update) {
                    ((AbstractFuture) future).listeners = update;
                }
            }
            return old;
        }

        @Override // com.google.common.util.concurrent.AbstractFuture.AtomicHelper
        Waiter gasWaiters(AbstractFuture<?> future, Waiter update) {
            Waiter old;
            synchronized (future) {
                old = ((AbstractFuture) future).waiters;
                if (old != update) {
                    ((AbstractFuture) future).waiters = update;
                }
            }
            return old;
        }

        @Override // com.google.common.util.concurrent.AbstractFuture.AtomicHelper
        boolean casValue(AbstractFuture<?> future, @CheckForNull Object expect, Object update) {
            synchronized (future) {
                if (((AbstractFuture) future).value != expect) {
                    return false;
                }
                ((AbstractFuture) future).value = update;
                return true;
            }
        }
    }

    private static CancellationException cancellationExceptionWithCause(String message, @CheckForNull Throwable cause) {
        CancellationException exception = new CancellationException(message);
        exception.initCause(cause);
        return exception;
    }
}
