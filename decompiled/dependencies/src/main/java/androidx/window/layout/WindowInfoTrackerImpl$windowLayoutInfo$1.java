package androidx.window.layout;

import android.app.Activity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.util.Consumer;
import java.util.concurrent.Executor;
import kotlin.Metadata;
import kotlin.ResultKt;
import kotlin.Unit;
import kotlin.coroutines.Continuation;
import kotlin.coroutines.intrinsics.IntrinsicsKt;
import kotlin.coroutines.jvm.internal.DebugMetadata;
import kotlin.coroutines.jvm.internal.SuspendLambda;
import kotlin.jvm.functions.Function2;
import kotlin.jvm.internal.Intrinsics;
import kotlinx.coroutines.channels.BufferOverflow;
import kotlinx.coroutines.channels.Channel;
import kotlinx.coroutines.channels.ChannelIterator;
import kotlinx.coroutines.channels.ChannelKt;
import kotlinx.coroutines.flow.FlowCollector;

/* compiled from: WindowInfoTrackerImpl.kt */
@Metadata(d1 = {"\u0000\u000e\n\u0000\n\u0002\u0010\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\u0010\u0000\u001a\u00020\u0001*\b\u0012\u0004\u0012\u00020\u00030\u0002H\u008a@"}, d2 = {"<anonymous>", "", "Lkotlinx/coroutines/flow/FlowCollector;", "Landroidx/window/layout/WindowLayoutInfo;"}, k = 3, mv = {1, 6, 0}, xi = ConstraintLayout.LayoutParams.Table.LAYOUT_CONSTRAINT_VERTICAL_CHAINSTYLE)
@DebugMetadata(c = "androidx.window.layout.WindowInfoTrackerImpl$windowLayoutInfo$1", f = "WindowInfoTrackerImpl.kt", i = {0, 0, 1, 1}, l = {ConstraintLayout.LayoutParams.Table.LAYOUT_MARGIN_BASELINE, ConstraintLayout.LayoutParams.Table.LAYOUT_GONE_MARGIN_BASELINE}, m = "invokeSuspend", n = {"$this$flow", "listener", "$this$flow", "listener"}, s = {"L$0", "L$1", "L$0", "L$1"})
/* loaded from: classes.dex */
final class WindowInfoTrackerImpl$windowLayoutInfo$1 extends SuspendLambda implements Function2<FlowCollector<? super WindowLayoutInfo>, Continuation<? super Unit>, Object> {
    final /* synthetic */ Activity $activity;
    private /* synthetic */ Object L$0;
    Object L$1;
    Object L$2;
    int label;
    final /* synthetic */ WindowInfoTrackerImpl this$0;

    /* JADX INFO: Access modifiers changed from: package-private */
    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public WindowInfoTrackerImpl$windowLayoutInfo$1(WindowInfoTrackerImpl windowInfoTrackerImpl, Activity activity, Continuation<? super WindowInfoTrackerImpl$windowLayoutInfo$1> continuation) {
        super(2, continuation);
        this.this$0 = windowInfoTrackerImpl;
        this.$activity = activity;
    }

    @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
    public final Continuation<Unit> create(Object obj, Continuation<?> continuation) {
        WindowInfoTrackerImpl$windowLayoutInfo$1 windowInfoTrackerImpl$windowLayoutInfo$1 = new WindowInfoTrackerImpl$windowLayoutInfo$1(this.this$0, this.$activity, continuation);
        windowInfoTrackerImpl$windowLayoutInfo$1.L$0 = obj;
        return windowInfoTrackerImpl$windowLayoutInfo$1;
    }

    @Override // kotlin.jvm.functions.Function2
    public final Object invoke(FlowCollector<? super WindowLayoutInfo> flowCollector, Continuation<? super Unit> continuation) {
        return ((WindowInfoTrackerImpl$windowLayoutInfo$1) create(flowCollector, continuation)).invokeSuspend(Unit.INSTANCE);
    }

    /* JADX WARN: Failed to find 'out' block for switch in B:3:0x0006. Please report as an issue. */
    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Removed duplicated region for block: B:12:0x0079 A[RETURN] */
    /* JADX WARN: Removed duplicated region for block: B:13:0x007a  */
    /* JADX WARN: Removed duplicated region for block: B:17:0x0087 A[Catch: all -> 0x00b1, TRY_LEAVE, TryCatch #0 {all -> 0x00b1, blocks: (B:15:0x007f, B:17:0x0087), top: B:14:0x007f }] */
    /* JADX WARN: Removed duplicated region for block: B:21:0x00a4  */
    /* JADX WARN: Type inference failed for: r1v0, types: [int] */
    /* JADX WARN: Type inference failed for: r1v12 */
    /* JADX WARN: Type inference failed for: r1v13 */
    /* JADX WARN: Type inference failed for: r1v9, types: [androidx.window.layout.WindowInfoTrackerImpl$windowLayoutInfo$1] */
    /* JADX WARN: Unsupported multi-entry loop pattern (BACK_EDGE: B:20:0x00a0 -> B:10:0x0067). Please report as a decompilation issue!!! */
    @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public final Object invokeSuspend(Object obj) {
        Consumer<WindowLayoutInfo> consumer;
        WindowBackend windowBackend;
        FlowCollector flowCollector;
        WindowBackend windowBackend2;
        ChannelIterator it;
        WindowInfoTrackerImpl$windowLayoutInfo$1 windowInfoTrackerImpl$windowLayoutInfo$1;
        Object obj2;
        Object obj3;
        WindowBackend windowBackend3;
        Object hasNext;
        Object coroutine_suspended = IntrinsicsKt.getCOROUTINE_SUSPENDED();
        Continuation<? super Boolean> continuation = this.label;
        try {
            switch (continuation) {
                case 0:
                    ResultKt.throwOnFailure(obj);
                    WindowInfoTrackerImpl$windowLayoutInfo$1 windowInfoTrackerImpl$windowLayoutInfo$12 = this;
                    flowCollector = (FlowCollector) windowInfoTrackerImpl$windowLayoutInfo$12.L$0;
                    final Channel Channel$default = ChannelKt.Channel$default(10, BufferOverflow.DROP_OLDEST, null, 4, null);
                    consumer = new Consumer() { // from class: androidx.window.layout.WindowInfoTrackerImpl$windowLayoutInfo$1$$ExternalSyntheticLambda0
                        @Override // androidx.core.util.Consumer
                        public final void accept(Object obj4) {
                            WindowInfoTrackerImpl$windowLayoutInfo$1.m100invokeSuspend$lambda0(Channel.this, (WindowLayoutInfo) obj4);
                        }
                    };
                    windowBackend2 = windowInfoTrackerImpl$windowLayoutInfo$12.this$0.windowBackend;
                    windowBackend2.registerLayoutChangeCallback(windowInfoTrackerImpl$windowLayoutInfo$12.$activity, new Executor() { // from class: androidx.window.layout.WindowInfoTrackerImpl$windowLayoutInfo$1$$ExternalSyntheticLambda1
                        @Override // java.util.concurrent.Executor
                        public final void execute(Runnable runnable) {
                            runnable.run();
                        }
                    }, consumer);
                    it = Channel$default.iterator();
                    continuation = windowInfoTrackerImpl$windowLayoutInfo$12;
                    continuation.L$0 = flowCollector;
                    continuation.L$1 = consumer;
                    continuation.L$2 = it;
                    continuation.label = 1;
                    hasNext = it.hasNext(continuation);
                    if (hasNext == coroutine_suspended) {
                        return coroutine_suspended;
                    }
                    Object obj4 = coroutine_suspended;
                    obj3 = obj;
                    obj = hasNext;
                    windowInfoTrackerImpl$windowLayoutInfo$1 = continuation;
                    obj2 = obj4;
                    try {
                        if (((Boolean) obj).booleanValue()) {
                            windowBackend3 = windowInfoTrackerImpl$windowLayoutInfo$1.this$0.windowBackend;
                            windowBackend3.unregisterLayoutChangeCallback(consumer);
                            return Unit.INSTANCE;
                        }
                        windowInfoTrackerImpl$windowLayoutInfo$1.L$0 = flowCollector;
                        windowInfoTrackerImpl$windowLayoutInfo$1.L$1 = consumer;
                        windowInfoTrackerImpl$windowLayoutInfo$1.L$2 = it;
                        windowInfoTrackerImpl$windowLayoutInfo$1.label = 2;
                        if (flowCollector.emit((WindowLayoutInfo) it.next(), windowInfoTrackerImpl$windowLayoutInfo$1) == obj2) {
                            return obj2;
                        }
                        obj = obj3;
                        coroutine_suspended = obj2;
                        continuation = windowInfoTrackerImpl$windowLayoutInfo$1;
                        continuation.L$0 = flowCollector;
                        continuation.L$1 = consumer;
                        continuation.L$2 = it;
                        continuation.label = 1;
                        hasNext = it.hasNext(continuation);
                        if (hasNext == coroutine_suspended) {
                        }
                    } catch (Throwable th) {
                        continuation = windowInfoTrackerImpl$windowLayoutInfo$1;
                        th = th;
                        windowBackend = continuation.this$0.windowBackend;
                        windowBackend.unregisterLayoutChangeCallback(consumer);
                        throw th;
                    }
                case 1:
                    ChannelIterator channelIterator = (ChannelIterator) this.L$2;
                    consumer = (Consumer) this.L$1;
                    flowCollector = (FlowCollector) this.L$0;
                    ResultKt.throwOnFailure(obj);
                    it = channelIterator;
                    windowInfoTrackerImpl$windowLayoutInfo$1 = this;
                    obj2 = coroutine_suspended;
                    obj3 = obj;
                    if (((Boolean) obj).booleanValue()) {
                    }
                    break;
                case 2:
                    WindowInfoTrackerImpl$windowLayoutInfo$1 windowInfoTrackerImpl$windowLayoutInfo$13 = this;
                    ChannelIterator channelIterator2 = (ChannelIterator) windowInfoTrackerImpl$windowLayoutInfo$13.L$2;
                    consumer = (Consumer) windowInfoTrackerImpl$windowLayoutInfo$13.L$1;
                    flowCollector = (FlowCollector) windowInfoTrackerImpl$windowLayoutInfo$13.L$0;
                    ResultKt.throwOnFailure(obj);
                    it = channelIterator2;
                    continuation = windowInfoTrackerImpl$windowLayoutInfo$13;
                    continuation.L$0 = flowCollector;
                    continuation.L$1 = consumer;
                    continuation.L$2 = it;
                    continuation.label = 1;
                    hasNext = it.hasNext(continuation);
                    if (hasNext == coroutine_suspended) {
                    }
                    break;
                default:
                    throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
            }
        } catch (Throwable th2) {
            th = th2;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* renamed from: invokeSuspend$lambda-0, reason: not valid java name */
    public static final void m100invokeSuspend$lambda0(Channel $channel, WindowLayoutInfo info) {
        Intrinsics.checkNotNullExpressionValue(info, "info");
        $channel.mo1659trySendJP2dKIU(info);
    }
}
