package kotlinx.coroutines.channels;

import androidx.constraintlayout.widget.ConstraintLayout;
import kotlin.Metadata;
import kotlin.ResultKt;
import kotlin.Unit;
import kotlin.coroutines.Continuation;
import kotlin.coroutines.CoroutineContext;
import kotlin.coroutines.EmptyCoroutineContext;
import kotlin.coroutines.intrinsics.IntrinsicsKt;
import kotlin.coroutines.jvm.internal.Boxing;
import kotlin.ranges.RangesKt;
import kotlinx.coroutines.AbstractTimeSource;
import kotlinx.coroutines.AbstractTimeSourceKt;
import kotlinx.coroutines.DelayKt;
import kotlinx.coroutines.Dispatchers;
import kotlinx.coroutines.EventLoop_commonKt;
import kotlinx.coroutines.GlobalScope;

/* compiled from: TickerChannels.kt */
@Metadata(d1 = {"\u0000*\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010\t\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\u001a/\u0010\u0000\u001a\u00020\u00012\u0006\u0010\u0002\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u00032\f\u0010\u0005\u001a\b\u0012\u0004\u0012\u00020\u00010\u0006H\u0082@ø\u0001\u0000¢\u0006\u0002\u0010\u0007\u001a/\u0010\b\u001a\u00020\u00012\u0006\u0010\u0002\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u00032\f\u0010\u0005\u001a\b\u0012\u0004\u0012\u00020\u00010\u0006H\u0082@ø\u0001\u0000¢\u0006\u0002\u0010\u0007\u001a4\u0010\t\u001a\b\u0012\u0004\u0012\u00020\u00010\n2\u0006\u0010\u0002\u001a\u00020\u00032\b\b\u0002\u0010\u0004\u001a\u00020\u00032\b\b\u0002\u0010\u000b\u001a\u00020\f2\b\b\u0002\u0010\r\u001a\u00020\u000eH\u0007\u0082\u0002\u0004\n\u0002\b\u0019¨\u0006\u000f"}, d2 = {"fixedDelayTicker", "", "delayMillis", "", "initialDelayMillis", "channel", "Lkotlinx/coroutines/channels/SendChannel;", "(JJLkotlinx/coroutines/channels/SendChannel;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "fixedPeriodTicker", "ticker", "Lkotlinx/coroutines/channels/ReceiveChannel;", "context", "Lkotlin/coroutines/CoroutineContext;", "mode", "Lkotlinx/coroutines/channels/TickerMode;", "kotlinx-coroutines-core"}, k = 2, mv = {1, 6, 0}, xi = ConstraintLayout.LayoutParams.Table.LAYOUT_CONSTRAINT_VERTICAL_CHAINSTYLE)
/* loaded from: classes7.dex */
public final class TickerChannelsKt {
    public static /* synthetic */ ReceiveChannel ticker$default(long j, long j2, CoroutineContext coroutineContext, TickerMode tickerMode, int i, Object obj) {
        if ((i & 2) != 0) {
            j2 = j;
        }
        if ((i & 4) != 0) {
            coroutineContext = EmptyCoroutineContext.INSTANCE;
        }
        if ((i & 8) != 0) {
            tickerMode = TickerMode.FIXED_PERIOD;
        }
        return ticker(j, j2, coroutineContext, tickerMode);
    }

    public static final ReceiveChannel<Unit> ticker(long delayMillis, long initialDelayMillis, CoroutineContext context, TickerMode mode) {
        if (!(delayMillis >= 0)) {
            throw new IllegalArgumentException(("Expected non-negative delay, but has " + delayMillis + " ms").toString());
        }
        if (!(initialDelayMillis >= 0)) {
            throw new IllegalArgumentException(("Expected non-negative initial delay, but has " + initialDelayMillis + " ms").toString());
        }
        return ProduceKt.produce(GlobalScope.INSTANCE, Dispatchers.getUnconfined().plus(context), 0, new TickerChannelsKt$ticker$3(mode, delayMillis, initialDelayMillis, null));
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX WARN: Failed to find 'out' block for switch in B:7:0x0025. Please report as an issue. */
    /* JADX WARN: Removed duplicated region for block: B:11:0x0030  */
    /* JADX WARN: Removed duplicated region for block: B:14:0x00b7 A[RETURN] */
    /* JADX WARN: Removed duplicated region for block: B:15:0x00b8  */
    /* JADX WARN: Removed duplicated region for block: B:18:0x00c0  */
    /* JADX WARN: Removed duplicated region for block: B:20:0x00cc  */
    /* JADX WARN: Removed duplicated region for block: B:31:0x0118 A[RETURN] */
    /* JADX WARN: Removed duplicated region for block: B:32:0x0119  */
    /* JADX WARN: Removed duplicated region for block: B:33:0x00d1  */
    /* JADX WARN: Removed duplicated region for block: B:34:0x00c2  */
    /* JADX WARN: Removed duplicated region for block: B:35:0x003f  */
    /* JADX WARN: Removed duplicated region for block: B:36:0x004e  */
    /* JADX WARN: Removed duplicated region for block: B:37:0x005a  */
    /* JADX WARN: Removed duplicated region for block: B:39:0x0066  */
    /* JADX WARN: Removed duplicated region for block: B:8:0x0028  */
    /* JADX WARN: Unsupported multi-entry loop pattern (BACK_EDGE: B:28:0x00ff -> B:12:0x00a4). Please report as a decompilation issue!!! */
    /* JADX WARN: Unsupported multi-entry loop pattern (BACK_EDGE: B:32:0x0119 -> B:12:0x00a4). Please report as a decompilation issue!!! */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public static final Object fixedPeriodTicker(long j, long initialDelayMillis, SendChannel<? super Unit> sendChannel, Continuation<? super Unit> continuation) {
        TickerChannelsKt$fixedPeriodTicker$1 tickerChannelsKt$fixedPeriodTicker$1;
        TickerChannelsKt$fixedPeriodTicker$1 tickerChannelsKt$fixedPeriodTicker$12;
        long delayMillis;
        SendChannel channel;
        long delayNs;
        long delayNs2;
        long deadline;
        long deadline2;
        long nextDelay;
        long delayNs3;
        Unit unit;
        if (continuation instanceof TickerChannelsKt$fixedPeriodTicker$1) {
            tickerChannelsKt$fixedPeriodTicker$1 = (TickerChannelsKt$fixedPeriodTicker$1) continuation;
            if ((tickerChannelsKt$fixedPeriodTicker$1.label & Integer.MIN_VALUE) != 0) {
                tickerChannelsKt$fixedPeriodTicker$1.label -= Integer.MIN_VALUE;
                tickerChannelsKt$fixedPeriodTicker$12 = tickerChannelsKt$fixedPeriodTicker$1;
                Object $result = tickerChannelsKt$fixedPeriodTicker$12.result;
                Object coroutine_suspended = IntrinsicsKt.getCOROUTINE_SUSPENDED();
                switch (tickerChannelsKt$fixedPeriodTicker$12.label) {
                    case 0:
                        ResultKt.throwOnFailure($result);
                        delayMillis = j;
                        channel = sendChannel;
                        AbstractTimeSource timeSource = AbstractTimeSourceKt.getTimeSource();
                        Long boxLong = timeSource == null ? null : Boxing.boxLong(timeSource.nanoTime());
                        long deadline3 = (boxLong == null ? System.nanoTime() : boxLong.longValue()) + EventLoop_commonKt.delayToNanos(initialDelayMillis);
                        tickerChannelsKt$fixedPeriodTicker$12.L$0 = channel;
                        tickerChannelsKt$fixedPeriodTicker$12.J$0 = delayMillis;
                        tickerChannelsKt$fixedPeriodTicker$12.J$1 = deadline3;
                        tickerChannelsKt$fixedPeriodTicker$12.label = 1;
                        if (DelayKt.delay(initialDelayMillis, tickerChannelsKt$fixedPeriodTicker$12) == coroutine_suspended) {
                            return coroutine_suspended;
                        }
                        delayNs = deadline3;
                        delayNs2 = EventLoop_commonKt.delayToNanos(delayMillis);
                        long deadline4 = delayNs + delayNs2;
                        unit = Unit.INSTANCE;
                        tickerChannelsKt$fixedPeriodTicker$12.L$0 = channel;
                        tickerChannelsKt$fixedPeriodTicker$12.J$0 = deadline4;
                        tickerChannelsKt$fixedPeriodTicker$12.J$1 = delayNs2;
                        tickerChannelsKt$fixedPeriodTicker$12.label = 2;
                        if (channel.send(unit, tickerChannelsKt$fixedPeriodTicker$12) == coroutine_suspended) {
                            return coroutine_suspended;
                        }
                        deadline2 = deadline4;
                        deadline = delayNs2;
                        AbstractTimeSource timeSource2 = AbstractTimeSourceKt.getTimeSource();
                        Long boxLong2 = timeSource2 != null ? null : Boxing.boxLong(timeSource2.nanoTime());
                        long now = boxLong2 != null ? System.nanoTime() : boxLong2.longValue();
                        nextDelay = RangesKt.coerceAtLeast(deadline2 - now, 0L);
                        if (nextDelay == 0 || deadline == 0) {
                            delayNs3 = EventLoop_commonKt.delayNanosToMillis(nextDelay);
                            tickerChannelsKt$fixedPeriodTicker$12.L$0 = channel;
                            tickerChannelsKt$fixedPeriodTicker$12.J$0 = deadline2;
                            tickerChannelsKt$fixedPeriodTicker$12.J$1 = deadline;
                            tickerChannelsKt$fixedPeriodTicker$12.label = 4;
                            if (DelayKt.delay(delayNs3, tickerChannelsKt$fixedPeriodTicker$12) != coroutine_suspended) {
                                return coroutine_suspended;
                            }
                            delayNs2 = deadline;
                            delayNs = deadline2;
                        } else {
                            long deadline5 = deadline - ((now - deadline2) % deadline);
                            long deadline6 = now + deadline5;
                            long delayNanosToMillis = EventLoop_commonKt.delayNanosToMillis(deadline5);
                            tickerChannelsKt$fixedPeriodTicker$12.L$0 = channel;
                            tickerChannelsKt$fixedPeriodTicker$12.J$0 = deadline6;
                            tickerChannelsKt$fixedPeriodTicker$12.J$1 = deadline;
                            tickerChannelsKt$fixedPeriodTicker$12.label = 3;
                            if (DelayKt.delay(delayNanosToMillis, tickerChannelsKt$fixedPeriodTicker$12) == coroutine_suspended) {
                                return coroutine_suspended;
                            }
                            long j2 = deadline;
                            delayNs = deadline6;
                            delayNs2 = j2;
                        }
                        long deadline42 = delayNs + delayNs2;
                        unit = Unit.INSTANCE;
                        tickerChannelsKt$fixedPeriodTicker$12.L$0 = channel;
                        tickerChannelsKt$fixedPeriodTicker$12.J$0 = deadline42;
                        tickerChannelsKt$fixedPeriodTicker$12.J$1 = delayNs2;
                        tickerChannelsKt$fixedPeriodTicker$12.label = 2;
                        if (channel.send(unit, tickerChannelsKt$fixedPeriodTicker$12) == coroutine_suspended) {
                        }
                        break;
                    case 1:
                        delayNs = tickerChannelsKt$fixedPeriodTicker$12.J$1;
                        delayMillis = tickerChannelsKt$fixedPeriodTicker$12.J$0;
                        channel = (SendChannel) tickerChannelsKt$fixedPeriodTicker$12.L$0;
                        ResultKt.throwOnFailure($result);
                        delayNs2 = EventLoop_commonKt.delayToNanos(delayMillis);
                        long deadline422 = delayNs + delayNs2;
                        unit = Unit.INSTANCE;
                        tickerChannelsKt$fixedPeriodTicker$12.L$0 = channel;
                        tickerChannelsKt$fixedPeriodTicker$12.J$0 = deadline422;
                        tickerChannelsKt$fixedPeriodTicker$12.J$1 = delayNs2;
                        tickerChannelsKt$fixedPeriodTicker$12.label = 2;
                        if (channel.send(unit, tickerChannelsKt$fixedPeriodTicker$12) == coroutine_suspended) {
                        }
                        break;
                    case 2:
                        deadline = tickerChannelsKt$fixedPeriodTicker$12.J$1;
                        deadline2 = tickerChannelsKt$fixedPeriodTicker$12.J$0;
                        channel = (SendChannel) tickerChannelsKt$fixedPeriodTicker$12.L$0;
                        ResultKt.throwOnFailure($result);
                        AbstractTimeSource timeSource22 = AbstractTimeSourceKt.getTimeSource();
                        if (timeSource22 != null) {
                        }
                        if (boxLong2 != null) {
                        }
                        nextDelay = RangesKt.coerceAtLeast(deadline2 - now, 0L);
                        if (nextDelay == 0) {
                            break;
                        }
                        delayNs3 = EventLoop_commonKt.delayNanosToMillis(nextDelay);
                        tickerChannelsKt$fixedPeriodTicker$12.L$0 = channel;
                        tickerChannelsKt$fixedPeriodTicker$12.J$0 = deadline2;
                        tickerChannelsKt$fixedPeriodTicker$12.J$1 = deadline;
                        tickerChannelsKt$fixedPeriodTicker$12.label = 4;
                        if (DelayKt.delay(delayNs3, tickerChannelsKt$fixedPeriodTicker$12) != coroutine_suspended) {
                        }
                        break;
                    case 3:
                        long delayNs4 = tickerChannelsKt$fixedPeriodTicker$12.J$1;
                        long deadline7 = tickerChannelsKt$fixedPeriodTicker$12.J$0;
                        channel = (SendChannel) tickerChannelsKt$fixedPeriodTicker$12.L$0;
                        ResultKt.throwOnFailure($result);
                        delayNs2 = delayNs4;
                        delayNs = deadline7;
                        long deadline4222 = delayNs + delayNs2;
                        unit = Unit.INSTANCE;
                        tickerChannelsKt$fixedPeriodTicker$12.L$0 = channel;
                        tickerChannelsKt$fixedPeriodTicker$12.J$0 = deadline4222;
                        tickerChannelsKt$fixedPeriodTicker$12.J$1 = delayNs2;
                        tickerChannelsKt$fixedPeriodTicker$12.label = 2;
                        if (channel.send(unit, tickerChannelsKt$fixedPeriodTicker$12) == coroutine_suspended) {
                        }
                        break;
                    case 4:
                        long delayNs5 = tickerChannelsKt$fixedPeriodTicker$12.J$1;
                        long deadline8 = tickerChannelsKt$fixedPeriodTicker$12.J$0;
                        channel = (SendChannel) tickerChannelsKt$fixedPeriodTicker$12.L$0;
                        ResultKt.throwOnFailure($result);
                        delayNs2 = delayNs5;
                        delayNs = deadline8;
                        long deadline42222 = delayNs + delayNs2;
                        unit = Unit.INSTANCE;
                        tickerChannelsKt$fixedPeriodTicker$12.L$0 = channel;
                        tickerChannelsKt$fixedPeriodTicker$12.J$0 = deadline42222;
                        tickerChannelsKt$fixedPeriodTicker$12.J$1 = delayNs2;
                        tickerChannelsKt$fixedPeriodTicker$12.label = 2;
                        if (channel.send(unit, tickerChannelsKt$fixedPeriodTicker$12) == coroutine_suspended) {
                        }
                        break;
                    default:
                        throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
                }
            }
        }
        tickerChannelsKt$fixedPeriodTicker$1 = new TickerChannelsKt$fixedPeriodTicker$1(continuation);
        tickerChannelsKt$fixedPeriodTicker$12 = tickerChannelsKt$fixedPeriodTicker$1;
        Object $result2 = tickerChannelsKt$fixedPeriodTicker$12.result;
        Object coroutine_suspended2 = IntrinsicsKt.getCOROUTINE_SUSPENDED();
        switch (tickerChannelsKt$fixedPeriodTicker$12.label) {
        }
    }

    /*  JADX ERROR: JadxOverflowException in pass: RegionMakerVisitor
        jadx.core.utils.exceptions.JadxOverflowException: Regions count limit reached
            at jadx.core.utils.ErrorsCounter.addError(ErrorsCounter.java:59)
            at jadx.core.utils.ErrorsCounter.error(ErrorsCounter.java:31)
            at jadx.core.dex.attributes.nodes.NotificationAttrNode.addError(NotificationAttrNode.java:19)
        */
    /* JADX INFO: Access modifiers changed from: private */
    /* JADX WARN: Failed to find 'out' block for switch in B:7:0x0022. Please report as an issue. */
    /* JADX WARN: Removed duplicated region for block: B:14:0x006d A[RETURN] */
    /* JADX WARN: Removed duplicated region for block: B:17:0x007b A[RETURN] */
    /* JADX WARN: Unsupported multi-entry loop pattern (BACK_EDGE: B:16:0x0079 -> B:12:0x005e). Please report as a decompilation issue!!! */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public static final java.lang.Object fixedDelayTicker(long r3, long r5, kotlinx.coroutines.channels.SendChannel<? super kotlin.Unit> r7, kotlin.coroutines.Continuation<? super kotlin.Unit> r8) {
        /*
            boolean r0 = r8 instanceof kotlinx.coroutines.channels.TickerChannelsKt$fixedDelayTicker$1
            if (r0 == 0) goto L14
            r0 = r8
            kotlinx.coroutines.channels.TickerChannelsKt$fixedDelayTicker$1 r0 = (kotlinx.coroutines.channels.TickerChannelsKt$fixedDelayTicker$1) r0
            int r1 = r0.label
            r2 = -2147483648(0xffffffff80000000, float:-0.0)
            r1 = r1 & r2
            if (r1 == 0) goto L14
            int r8 = r0.label
            int r8 = r8 - r2
            r0.label = r8
            goto L19
        L14:
            kotlinx.coroutines.channels.TickerChannelsKt$fixedDelayTicker$1 r0 = new kotlinx.coroutines.channels.TickerChannelsKt$fixedDelayTicker$1
            r0.<init>(r8)
        L19:
            r8 = r0
            java.lang.Object r0 = r8.result
            java.lang.Object r1 = kotlin.coroutines.intrinsics.IntrinsicsKt.getCOROUTINE_SUSPENDED()
            int r2 = r8.label
            switch(r2) {
                case 0: goto L4b;
                case 1: goto L41;
                case 2: goto L37;
                case 3: goto L2d;
                default: goto L25;
            }
        L25:
            java.lang.IllegalStateException r3 = new java.lang.IllegalStateException
            java.lang.String r4 = "call to 'resume' before 'invoke' with coroutine"
            r3.<init>(r4)
            throw r3
        L2d:
            long r3 = r8.J$0
            java.lang.Object r5 = r8.L$0
            kotlinx.coroutines.channels.SendChannel r5 = (kotlinx.coroutines.channels.SendChannel) r5
            kotlin.ResultKt.throwOnFailure(r0)
            goto L7c
        L37:
            long r3 = r8.J$0
            java.lang.Object r5 = r8.L$0
            kotlinx.coroutines.channels.SendChannel r5 = (kotlinx.coroutines.channels.SendChannel) r5
            kotlin.ResultKt.throwOnFailure(r0)
            goto L6e
        L41:
            long r3 = r8.J$0
            java.lang.Object r5 = r8.L$0
            kotlinx.coroutines.channels.SendChannel r5 = (kotlinx.coroutines.channels.SendChannel) r5
            kotlin.ResultKt.throwOnFailure(r0)
            goto L5d
        L4b:
            kotlin.ResultKt.throwOnFailure(r0)
            r8.L$0 = r7
            r8.J$0 = r3
            r2 = 1
            r8.label = r2
            java.lang.Object r5 = kotlinx.coroutines.DelayKt.delay(r5, r8)
            if (r5 != r1) goto L5c
            return r1
        L5c:
            r5 = r7
        L5d:
        L5e:
            kotlin.Unit r6 = kotlin.Unit.INSTANCE
            r8.L$0 = r5
            r8.J$0 = r3
            r7 = 2
            r8.label = r7
            java.lang.Object r6 = r5.send(r6, r8)
            if (r6 != r1) goto L6e
            return r1
        L6e:
            r8.L$0 = r5
            r8.J$0 = r3
            r6 = 3
            r8.label = r6
            java.lang.Object r6 = kotlinx.coroutines.DelayKt.delay(r3, r8)
            if (r6 != r1) goto L7c
            return r1
        L7c:
            goto L5e
        */
        throw new UnsupportedOperationException("Method not decompiled: kotlinx.coroutines.channels.TickerChannelsKt.fixedDelayTicker(long, long, kotlinx.coroutines.channels.SendChannel, kotlin.coroutines.Continuation):java.lang.Object");
    }
}
