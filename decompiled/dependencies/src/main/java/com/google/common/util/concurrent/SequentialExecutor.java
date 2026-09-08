package com.google.common.util.concurrent;

import com.google.common.base.Preconditions;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.concurrent.Executor;
import java.util.concurrent.RejectedExecutionException;
import java.util.logging.Logger;
import javax.annotation.CheckForNull;

/* JADX INFO: Access modifiers changed from: package-private */
@ElementTypesAreNonnullByDefault
/* loaded from: classes7.dex */
public final class SequentialExecutor implements Executor {
    private static final Logger log = Logger.getLogger(SequentialExecutor.class.getName());
    private final Executor executor;
    private final Deque<Runnable> queue = new ArrayDeque();
    private WorkerRunningState workerRunningState = WorkerRunningState.IDLE;
    private long workerRunCount = 0;
    private final QueueWorker worker = new QueueWorker();

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes7.dex */
    public enum WorkerRunningState {
        IDLE,
        QUEUING,
        QUEUED,
        RUNNING
    }

    static /* synthetic */ long access$308(SequentialExecutor x0) {
        long j = x0.workerRunCount;
        x0.workerRunCount = 1 + j;
        return j;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public SequentialExecutor(Executor executor) {
        this.executor = (Executor) Preconditions.checkNotNull(executor);
    }

    @Override // java.util.concurrent.Executor
    public void execute(final Runnable task) {
        Preconditions.checkNotNull(task);
        synchronized (this.queue) {
            if (this.workerRunningState != WorkerRunningState.RUNNING && this.workerRunningState != WorkerRunningState.QUEUED) {
                long oldRunCount = this.workerRunCount;
                Runnable submittedTask = new Runnable(this) { // from class: com.google.common.util.concurrent.SequentialExecutor.1
                    @Override // java.lang.Runnable
                    public void run() {
                        task.run();
                    }

                    public String toString() {
                        return task.toString();
                    }
                };
                this.queue.add(submittedTask);
                this.workerRunningState = WorkerRunningState.QUEUING;
                try {
                    this.executor.execute(this.worker);
                    removed = this.workerRunningState != WorkerRunningState.QUEUING;
                    boolean alreadyMarkedQueued = removed;
                    if (alreadyMarkedQueued) {
                        return;
                    }
                    synchronized (this.queue) {
                        if (this.workerRunCount == oldRunCount && this.workerRunningState == WorkerRunningState.QUEUING) {
                            this.workerRunningState = WorkerRunningState.QUEUED;
                        }
                    }
                    return;
                } catch (Error | RuntimeException t) {
                    synchronized (this.queue) {
                        if ((this.workerRunningState != WorkerRunningState.IDLE && this.workerRunningState != WorkerRunningState.QUEUING) || !this.queue.removeLastOccurrence(submittedTask)) {
                            removed = false;
                        }
                        if (!(t instanceof RejectedExecutionException) || removed) {
                            throw t;
                        }
                    }
                    return;
                }
            }
            this.queue.add(task);
        }
    }

    /* loaded from: classes7.dex */
    private final class QueueWorker implements Runnable {

        @CheckForNull
        Runnable task;

        private QueueWorker() {
        }

        @Override // java.lang.Runnable
        public void run() {
            try {
                workOnQueue();
            } catch (Error e) {
                synchronized (SequentialExecutor.this.queue) {
                    SequentialExecutor.this.workerRunningState = WorkerRunningState.IDLE;
                    throw e;
                }
            }
        }

        /* JADX WARN: Code restructure failed: missing block: B:10:0x0054, code lost:

            r0 = r0 | java.lang.Thread.interrupted();
            r2 = null;
         */
        /* JADX WARN: Code restructure failed: missing block: B:12:0x0056, code lost:

            r9.task.run();
         */
        /* JADX WARN: Code restructure failed: missing block: B:18:0x0060, code lost:

            r3 = move-exception;
         */
        /* JADX WARN: Code restructure failed: missing block: B:19:0x0061, code lost:

            r4 = com.google.common.util.concurrent.SequentialExecutor.log;
            r5 = java.util.logging.Level.SEVERE;
            r6 = java.lang.String.valueOf(r9.task);
            r4.log(r5, new java.lang.StringBuilder(java.lang.String.valueOf(r6).length() + 35).append("Exception while executing runnable ").append(r6).toString(), (java.lang.Throwable) r3);
         */
        /* JADX WARN: Code restructure failed: missing block: B:20:0x008d, code lost:

            r9.task = null;
         */
        /* JADX WARN: Code restructure failed: missing block: B:22:0x005e, code lost:

            r3 = move-exception;
         */
        /* JADX WARN: Code restructure failed: missing block: B:23:0x0090, code lost:

            r9.task = null;
         */
        /* JADX WARN: Code restructure failed: missing block: B:24:0x0093, code lost:

            throw r3;
         */
        /* JADX WARN: Code restructure failed: missing block: B:31:0x004e, code lost:

            return;
         */
        /* JADX WARN: Code restructure failed: missing block: B:32:?, code lost:

            return;
         */
        /*
            Code decompiled incorrectly, please refer to instructions dump.
        */
        private void workOnQueue() {
            boolean interruptedDuringTask = false;
            boolean hasSetRunning = false;
            while (true) {
                try {
                    synchronized (SequentialExecutor.this.queue) {
                        if (!hasSetRunning) {
                            if (SequentialExecutor.this.workerRunningState != WorkerRunningState.RUNNING) {
                                SequentialExecutor.access$308(SequentialExecutor.this);
                                SequentialExecutor.this.workerRunningState = WorkerRunningState.RUNNING;
                                hasSetRunning = true;
                            }
                        }
                        Runnable runnable = (Runnable) SequentialExecutor.this.queue.poll();
                        this.task = runnable;
                        if (runnable == null) {
                            SequentialExecutor.this.workerRunningState = WorkerRunningState.IDLE;
                        }
                    }
                    if (!interruptedDuringTask) {
                        return;
                    }
                    Thread.currentThread().interrupt();
                    return;
                } finally {
                    if (interruptedDuringTask) {
                        Thread.currentThread().interrupt();
                    }
                }
            }
        }

        public String toString() {
            Runnable currentlyRunning = this.task;
            if (currentlyRunning == null) {
                String valueOf = String.valueOf(SequentialExecutor.this.workerRunningState);
                return new StringBuilder(String.valueOf(valueOf).length() + 32).append("SequentialExecutorWorker{state=").append(valueOf).append("}").toString();
            }
            String valueOf2 = String.valueOf(currentlyRunning);
            return new StringBuilder(String.valueOf(valueOf2).length() + 34).append("SequentialExecutorWorker{running=").append(valueOf2).append("}").toString();
        }
    }

    public String toString() {
        int identityHashCode = System.identityHashCode(this);
        String valueOf = String.valueOf(this.executor);
        return new StringBuilder(String.valueOf(valueOf).length() + 32).append("SequentialExecutor@").append(identityHashCode).append("{").append(valueOf).append("}").toString();
    }
}
