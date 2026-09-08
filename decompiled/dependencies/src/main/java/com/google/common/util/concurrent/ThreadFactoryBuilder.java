package com.google.common.util.concurrent;

import com.google.common.base.Preconditions;
import com.google.errorprone.annotations.CheckReturnValue;
import java.lang.Thread;
import java.util.Locale;
import java.util.Objects;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicLong;
import javax.annotation.CheckForNull;

@ElementTypesAreNonnullByDefault
/* loaded from: classes7.dex */
public final class ThreadFactoryBuilder {

    @CheckForNull
    private String nameFormat = null;

    @CheckForNull
    private Boolean daemon = null;

    @CheckForNull
    private Integer priority = null;

    @CheckForNull
    private Thread.UncaughtExceptionHandler uncaughtExceptionHandler = null;

    @CheckForNull
    private ThreadFactory backingThreadFactory = null;

    public ThreadFactoryBuilder setNameFormat(String nameFormat) {
        format(nameFormat, 0);
        this.nameFormat = nameFormat;
        return this;
    }

    public ThreadFactoryBuilder setDaemon(boolean daemon) {
        this.daemon = Boolean.valueOf(daemon);
        return this;
    }

    public ThreadFactoryBuilder setPriority(int priority) {
        Preconditions.checkArgument(priority >= 1, "Thread priority (%s) must be >= %s", priority, 1);
        Preconditions.checkArgument(priority <= 10, "Thread priority (%s) must be <= %s", priority, 10);
        this.priority = Integer.valueOf(priority);
        return this;
    }

    public ThreadFactoryBuilder setUncaughtExceptionHandler(Thread.UncaughtExceptionHandler uncaughtExceptionHandler) {
        this.uncaughtExceptionHandler = (Thread.UncaughtExceptionHandler) Preconditions.checkNotNull(uncaughtExceptionHandler);
        return this;
    }

    public ThreadFactoryBuilder setThreadFactory(ThreadFactory backingThreadFactory) {
        this.backingThreadFactory = (ThreadFactory) Preconditions.checkNotNull(backingThreadFactory);
        return this;
    }

    @CheckReturnValue
    public ThreadFactory build() {
        return doBuild(this);
    }

    private static ThreadFactory doBuild(ThreadFactoryBuilder builder) {
        final ThreadFactory backingThreadFactory;
        final String nameFormat = builder.nameFormat;
        final Boolean daemon = builder.daemon;
        final Integer priority = builder.priority;
        final Thread.UncaughtExceptionHandler uncaughtExceptionHandler = builder.uncaughtExceptionHandler;
        ThreadFactory threadFactory = builder.backingThreadFactory;
        if (threadFactory != null) {
            backingThreadFactory = threadFactory;
        } else {
            backingThreadFactory = Executors.defaultThreadFactory();
        }
        final AtomicLong count = nameFormat != null ? new AtomicLong(0L) : null;
        return new ThreadFactory() { // from class: com.google.common.util.concurrent.ThreadFactoryBuilder.1
            @Override // java.util.concurrent.ThreadFactory
            public Thread newThread(Runnable runnable) {
                Thread thread = backingThreadFactory.newThread(runnable);
                String str = nameFormat;
                if (str != null) {
                    thread.setName(ThreadFactoryBuilder.format(str, Long.valueOf(((AtomicLong) Objects.requireNonNull(count)).getAndIncrement())));
                }
                Boolean bool = daemon;
                if (bool != null) {
                    thread.setDaemon(bool.booleanValue());
                }
                Integer num = priority;
                if (num != null) {
                    thread.setPriority(num.intValue());
                }
                Thread.UncaughtExceptionHandler uncaughtExceptionHandler2 = uncaughtExceptionHandler;
                if (uncaughtExceptionHandler2 != null) {
                    thread.setUncaughtExceptionHandler(uncaughtExceptionHandler2);
                }
                return thread;
            }
        };
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static String format(String format, Object... args) {
        return String.format(Locale.ROOT, format, args);
    }
}
