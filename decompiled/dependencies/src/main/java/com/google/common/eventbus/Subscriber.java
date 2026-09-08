package com.google.common.eventbus;

import com.google.common.base.Preconditions;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.concurrent.Executor;
import javax.annotation.CheckForNull;

/* JADX INFO: Access modifiers changed from: package-private */
@ElementTypesAreNonnullByDefault
/* loaded from: classes.dex */
public class Subscriber {
    private EventBus bus;
    private final Executor executor;
    private final Method method;
    final Object target;

    /* JADX INFO: Access modifiers changed from: package-private */
    public static Subscriber create(EventBus bus, Object listener, Method method) {
        if (isDeclaredThreadSafe(method)) {
            return new Subscriber(bus, listener, method);
        }
        return new SynchronizedSubscriber(bus, listener, method);
    }

    private Subscriber(EventBus bus, Object target, Method method) {
        this.bus = bus;
        this.target = Preconditions.checkNotNull(target);
        this.method = method;
        method.setAccessible(true);
        this.executor = bus.executor();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public final void dispatchEvent(final Object event) {
        this.executor.execute(new Runnable() { // from class: com.google.common.eventbus.Subscriber$$ExternalSyntheticLambda0
            @Override // java.lang.Runnable
            public final void run() {
                Subscriber.this.m157lambda$dispatchEvent$0$comgooglecommoneventbusSubscriber(event);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: lambda$dispatchEvent$0$com-google-common-eventbus-Subscriber, reason: not valid java name */
    public /* synthetic */ void m157lambda$dispatchEvent$0$comgooglecommoneventbusSubscriber(Object event) {
        try {
            invokeSubscriberMethod(event);
        } catch (InvocationTargetException e) {
            this.bus.handleSubscriberException(e.getCause(), context(event));
        }
    }

    void invokeSubscriberMethod(Object event) throws InvocationTargetException {
        try {
            this.method.invoke(this.target, Preconditions.checkNotNull(event));
        } catch (IllegalAccessException e) {
            String valueOf = String.valueOf(event);
            throw new Error(new StringBuilder(String.valueOf(valueOf).length() + 28).append("Method became inaccessible: ").append(valueOf).toString(), e);
        } catch (IllegalArgumentException e2) {
            String valueOf2 = String.valueOf(event);
            throw new Error(new StringBuilder(String.valueOf(valueOf2).length() + 33).append("Method rejected target/argument: ").append(valueOf2).toString(), e2);
        } catch (InvocationTargetException e3) {
            if (e3.getCause() instanceof Error) {
                throw ((Error) e3.getCause());
            }
            throw e3;
        }
    }

    private SubscriberExceptionContext context(Object event) {
        return new SubscriberExceptionContext(this.bus, event, this.target, this.method);
    }

    public final int hashCode() {
        return ((this.method.hashCode() + 31) * 31) + System.identityHashCode(this.target);
    }

    public final boolean equals(@CheckForNull Object obj) {
        if (!(obj instanceof Subscriber)) {
            return false;
        }
        Subscriber that = (Subscriber) obj;
        return this.target == that.target && this.method.equals(that.method);
    }

    private static boolean isDeclaredThreadSafe(Method method) {
        return method.getAnnotation(AllowConcurrentEvents.class) != null;
    }

    /* loaded from: classes.dex */
    static final class SynchronizedSubscriber extends Subscriber {
        private SynchronizedSubscriber(EventBus bus, Object target, Method method) {
            super(bus, target, method);
        }

        @Override // com.google.common.eventbus.Subscriber
        void invokeSubscriberMethod(Object event) throws InvocationTargetException {
            synchronized (this) {
                super.invokeSubscriberMethod(event);
            }
        }
    }
}
