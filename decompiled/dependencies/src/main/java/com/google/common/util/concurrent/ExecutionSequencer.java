package com.google.common.util.concurrent;

import com.google.common.base.Preconditions;
import java.util.Objects;
import java.util.concurrent.Callable;
import java.util.concurrent.Executor;
import java.util.concurrent.atomic.AtomicReference;
import javax.annotation.CheckForNull;

@ElementTypesAreNonnullByDefault
/* loaded from: classes7.dex */
public final class ExecutionSequencer {
    private final AtomicReference<ListenableFuture<Void>> ref = new AtomicReference<>(Futures.immediateVoidFuture());
    private ThreadConfinedTaskQueue latestTaskQueue = new ThreadConfinedTaskQueue();

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes7.dex */
    public enum RunningState {
        NOT_RUN,
        CANCELLED,
        STARTED
    }

    private ExecutionSequencer() {
    }

    public static ExecutionSequencer create() {
        return new ExecutionSequencer();
    }

    /* loaded from: classes7.dex */
    private static final class ThreadConfinedTaskQueue {

        @CheckForNull
        Executor nextExecutor;

        @CheckForNull
        Runnable nextTask;

        @CheckForNull
        Thread thread;

        private ThreadConfinedTaskQueue() {
        }
    }

    public <T> ListenableFuture<T> submit(final Callable<T> callable, Executor executor) {
        Preconditions.checkNotNull(callable);
        Preconditions.checkNotNull(executor);
        return submitAsync(new AsyncCallable<T>(this) { // from class: com.google.common.util.concurrent.ExecutionSequencer.1
            @Override // com.google.common.util.concurrent.AsyncCallable
            public ListenableFuture<T> call() throws Exception {
                return Futures.immediateFuture(callable.call());
            }

            public String toString() {
                return callable.toString();
            }
        }, executor);
    }

    public <T> ListenableFuture<T> submitAsync(final AsyncCallable<T> callable, Executor executor) {
        Preconditions.checkNotNull(callable);
        Preconditions.checkNotNull(executor);
        final TaskNonReentrantExecutor taskExecutor = new TaskNonReentrantExecutor(executor, this);
        AsyncCallable<T> task = new AsyncCallable<T>(this) { // from class: com.google.common.util.concurrent.ExecutionSequencer.2
            @Override // com.google.common.util.concurrent.AsyncCallable
            public ListenableFuture<T> call() throws Exception {
                if (!taskExecutor.trySetStarted()) {
                    return Futures.immediateCancelledFuture();
                }
                return callable.call();
            }

            public String toString() {
                return callable.toString();
            }
        };
        final SettableFuture<Void> newFuture = SettableFuture.create();
        final ListenableFuture<Void> oldFuture = this.ref.getAndSet(newFuture);
        final TrustedListenableFutureTask<T> taskFuture = TrustedListenableFutureTask.create(task);
        oldFuture.addListener(taskFuture, taskExecutor);
        final ListenableFuture<T> outputFuture = Futures.nonCancellationPropagating(taskFuture);
        Runnable listener = new Runnable() { // from class: com.google.common.util.concurrent.ExecutionSequencer$$ExternalSyntheticLambda0
            @Override // java.lang.Runnable
            public final void run() {
                ExecutionSequencer.lambda$submitAsync$0(TrustedListenableFutureTask.this, newFuture, oldFuture, outputFuture, taskExecutor);
            }
        };
        outputFuture.addListener(listener, MoreExecutors.directExecutor());
        taskFuture.addListener(listener, MoreExecutors.directExecutor());
        return outputFuture;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static /* synthetic */ void lambda$submitAsync$0(TrustedListenableFutureTask taskFuture, SettableFuture newFuture, ListenableFuture oldFuture, ListenableFuture outputFuture, TaskNonReentrantExecutor taskExecutor) {
        if (taskFuture.isDone()) {
            newFuture.setFuture(oldFuture);
        } else if (outputFuture.isCancelled() && taskExecutor.trySetCancelled()) {
            taskFuture.cancel(false);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes7.dex */
    public static final class TaskNonReentrantExecutor extends AtomicReference<RunningState> implements Executor, Runnable {

        @CheckForNull
        Executor delegate;

        @CheckForNull
        ExecutionSequencer sequencer;

        @CheckForNull
        Thread submitting;

        @CheckForNull
        Runnable task;

        private TaskNonReentrantExecutor(Executor delegate, ExecutionSequencer sequencer) {
            super(RunningState.NOT_RUN);
            this.delegate = delegate;
            this.sequencer = sequencer;
        }

        @Override // java.util.concurrent.Executor
        public void execute(Runnable task) {
            if (get() == RunningState.CANCELLED) {
                this.delegate = null;
                this.sequencer = null;
                return;
            }
            this.submitting = Thread.currentThread();
            try {
                ThreadConfinedTaskQueue submittingTaskQueue = ((ExecutionSequencer) Objects.requireNonNull(this.sequencer)).latestTaskQueue;
                if (submittingTaskQueue.thread == this.submitting) {
                    this.sequencer = null;
                    Preconditions.checkState(submittingTaskQueue.nextTask == null);
                    submittingTaskQueue.nextTask = task;
                    submittingTaskQueue.nextExecutor = (Executor) Objects.requireNonNull(this.delegate);
                    this.delegate = null;
                } else {
                    Executor localDelegate = (Executor) Objects.requireNonNull(this.delegate);
                    this.delegate = null;
                    this.task = task;
                    localDelegate.execute(this);
                }
            } finally {
                this.submitting = null;
            }
        }

        /* JADX WARN: Multi-variable type inference failed */
        @Override // java.lang.Runnable
        public void run() {
            Executor executor;
            Thread currentThread = Thread.currentThread();
            Thread thread = null;
            Object[] objArr = 0;
            if (currentThread != this.submitting) {
                Runnable runnable = (Runnable) Objects.requireNonNull(this.task);
                this.task = null;
                runnable.run();
                return;
            }
            ThreadConfinedTaskQueue threadConfinedTaskQueue = new ThreadConfinedTaskQueue();
            threadConfinedTaskQueue.thread = currentThread;
            ((ExecutionSequencer) Objects.requireNonNull(this.sequencer)).latestTaskQueue = threadConfinedTaskQueue;
            this.sequencer = null;
            try {
                Runnable runnable2 = (Runnable) Objects.requireNonNull(this.task);
                this.task = null;
                runnable2.run();
                while (true) {
                    Runnable runnable3 = threadConfinedTaskQueue.nextTask;
                    if (runnable3 == null || (executor = threadConfinedTaskQueue.nextExecutor) == null) {
                        break;
                    }
                    threadConfinedTaskQueue.nextTask = null;
                    threadConfinedTaskQueue.nextExecutor = null;
                    executor.execute(runnable3);
                }
            } finally {
                threadConfinedTaskQueue.thread = null;
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public boolean trySetStarted() {
            return compareAndSet(RunningState.NOT_RUN, RunningState.STARTED);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public boolean trySetCancelled() {
            return compareAndSet(RunningState.NOT_RUN, RunningState.CANCELLED);
        }
    }
}
