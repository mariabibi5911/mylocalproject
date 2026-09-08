package com.google.common.util.concurrent;

import com.google.common.base.Preconditions;
import com.google.common.util.concurrent.FluentFuture;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import javax.annotation.CheckForNull;

/* JADX INFO: Access modifiers changed from: package-private */
@ElementTypesAreNonnullByDefault
/* loaded from: classes7.dex */
public final class TimeoutFuture<V> extends FluentFuture.TrustedFuture<V> {

    @CheckForNull
    private ListenableFuture<V> delegateRef;

    @CheckForNull
    private ScheduledFuture<?> timer;

    /* JADX INFO: Access modifiers changed from: package-private */
    public static <V> ListenableFuture<V> create(ListenableFuture<V> delegate, long time, TimeUnit unit, ScheduledExecutorService scheduledExecutor) {
        TimeoutFuture<V> result = new TimeoutFuture<>(delegate);
        Fire<V> fire = new Fire<>(result);
        ((TimeoutFuture) result).timer = scheduledExecutor.schedule(fire, time, unit);
        delegate.addListener(fire, MoreExecutors.directExecutor());
        return result;
    }

    private TimeoutFuture(ListenableFuture<V> delegate) {
        this.delegateRef = (ListenableFuture) Preconditions.checkNotNull(delegate);
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes7.dex */
    public static final class Fire<V> implements Runnable {

        @CheckForNull
        TimeoutFuture<V> timeoutFutureRef;

        Fire(TimeoutFuture<V> timeoutFuture) {
            this.timeoutFutureRef = timeoutFuture;
        }

        @Override // java.lang.Runnable
        public void run() {
            ListenableFuture<? extends V> listenableFuture;
            TimeoutFuture<V> timeoutFuture = this.timeoutFutureRef;
            if (timeoutFuture == null || (listenableFuture = ((TimeoutFuture) timeoutFuture).delegateRef) == null) {
                return;
            }
            this.timeoutFutureRef = null;
            if (listenableFuture.isDone()) {
                timeoutFuture.setFuture(listenableFuture);
                return;
            }
            try {
                ScheduledFuture<?> timer = ((TimeoutFuture) timeoutFuture).timer;
                ((TimeoutFuture) timeoutFuture).timer = null;
                String message = "Timed out";
                if (timer != null) {
                    try {
                        long overDelayMs = Math.abs(timer.getDelay(TimeUnit.MILLISECONDS));
                        if (overDelayMs > 10) {
                            String valueOf = String.valueOf("Timed out");
                            message = new StringBuilder(String.valueOf(valueOf).length() + 66).append(valueOf).append(" (timeout delayed by ").append(overDelayMs).append(" ms after scheduled time)").toString();
                        }
                    } finally {
                        timeoutFuture.setException(new TimeoutFutureException(message));
                    }
                }
                String valueOf2 = String.valueOf(message);
                String valueOf3 = String.valueOf(listenableFuture);
                message = new StringBuilder(String.valueOf(valueOf2).length() + 2 + String.valueOf(valueOf3).length()).append(valueOf2).append(": ").append(valueOf3).toString();
            } finally {
                listenableFuture.cancel(true);
            }
        }
    }

    /* loaded from: classes7.dex */
    private static final class TimeoutFutureException extends TimeoutException {
        private TimeoutFutureException(String message) {
            super(message);
        }

        @Override // java.lang.Throwable
        public synchronized Throwable fillInStackTrace() {
            setStackTrace(new StackTraceElement[0]);
            return this;
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.google.common.util.concurrent.AbstractFuture
    @CheckForNull
    public String pendingToString() {
        ListenableFuture<V> listenableFuture = this.delegateRef;
        ScheduledFuture<?> localTimer = this.timer;
        if (listenableFuture != null) {
            String valueOf = String.valueOf(listenableFuture);
            String message = new StringBuilder(String.valueOf(valueOf).length() + 14).append("inputFuture=[").append(valueOf).append("]").toString();
            if (localTimer != null) {
                long delay = localTimer.getDelay(TimeUnit.MILLISECONDS);
                if (delay > 0) {
                    String valueOf2 = String.valueOf(message);
                    return new StringBuilder(String.valueOf(valueOf2).length() + 43).append(valueOf2).append(", remaining delay=[").append(delay).append(" ms]").toString();
                }
                return message;
            }
            return message;
        }
        return null;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.google.common.util.concurrent.AbstractFuture
    public void afterDone() {
        maybePropagateCancellationTo(this.delegateRef);
        Future<?> localTimer = this.timer;
        if (localTimer != null) {
            localTimer.cancel(false);
        }
        this.delegateRef = null;
        this.timer = null;
    }
}
