package kotlinx.coroutines.flow;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.exifinterface.media.ExifInterface;
import kotlin.Metadata;
import kotlin.ResultKt;
import kotlin.Unit;
import kotlin.coroutines.Continuation;
import kotlin.coroutines.intrinsics.IntrinsicsKt;
import kotlin.coroutines.jvm.internal.DebugMetadata;
import kotlin.coroutines.jvm.internal.DebugProbesKt;
import kotlin.coroutines.jvm.internal.SuspendLambda;
import kotlin.jvm.functions.Function3;
import kotlin.jvm.internal.Ref;
import kotlinx.coroutines.CoroutineScope;
import kotlinx.coroutines.channels.ProduceKt;
import kotlinx.coroutines.channels.ReceiveChannel;
import kotlinx.coroutines.flow.internal.NullSurrogateKt;
import kotlinx.coroutines.selects.SelectInstance;

/* JADX INFO: Access modifiers changed from: package-private */
/* JADX INFO: Add missing generic type declarations: [T] */
/* compiled from: Delay.kt */
@Metadata(d1 = {"\u0000\u0012\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\u0010\u0000\u001a\u00020\u0001\"\u0004\b\u0000\u0010\u0002*\u00020\u00032\f\u0010\u0004\u001a\b\u0012\u0004\u0012\u0002H\u00020\u0005H\u008a@"}, d2 = {"<anonymous>", "", ExifInterface.GPS_DIRECTION_TRUE, "Lkotlinx/coroutines/CoroutineScope;", "downstream", "Lkotlinx/coroutines/flow/FlowCollector;"}, k = 3, mv = {1, 6, 0}, xi = ConstraintLayout.LayoutParams.Table.LAYOUT_CONSTRAINT_VERTICAL_CHAINSTYLE)
@DebugMetadata(c = "kotlinx.coroutines.flow.FlowKt__DelayKt$sample$2", f = "Delay.kt", i = {0, 0, 0, 0}, l = {352}, m = "invokeSuspend", n = {"downstream", "values", "lastValue", "ticker"}, s = {"L$0", "L$1", "L$2", "L$3"})
/* loaded from: classes7.dex */
public final class FlowKt__DelayKt$sample$2<T> extends SuspendLambda implements Function3<CoroutineScope, FlowCollector<? super T>, Continuation<? super Unit>, Object> {
    final /* synthetic */ long $periodMillis;
    final /* synthetic */ Flow<T> $this_sample;
    private /* synthetic */ Object L$0;
    /* synthetic */ Object L$1;
    Object L$2;
    Object L$3;
    int label;

    /* JADX INFO: Access modifiers changed from: package-private */
    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    /* JADX WARN: Multi-variable type inference failed */
    public FlowKt__DelayKt$sample$2(long j, Flow<? extends T> flow, Continuation<? super FlowKt__DelayKt$sample$2> continuation) {
        super(3, continuation);
        this.$periodMillis = j;
        this.$this_sample = flow;
    }

    @Override // kotlin.jvm.functions.Function3
    public final Object invoke(CoroutineScope coroutineScope, FlowCollector<? super T> flowCollector, Continuation<? super Unit> continuation) {
        FlowKt__DelayKt$sample$2 flowKt__DelayKt$sample$2 = new FlowKt__DelayKt$sample$2(this.$periodMillis, this.$this_sample, continuation);
        flowKt__DelayKt$sample$2.L$0 = coroutineScope;
        flowKt__DelayKt$sample$2.L$1 = flowCollector;
        return flowKt__DelayKt$sample$2.invokeSuspend(Unit.INSTANCE);
    }

    /* JADX WARN: Can't wrap try/catch for region: R(15:10|(1:11)|12|13|14|15|16|17|18|19|20|21|22|(1:24)|(1:26)(4:27|7|8|(2:40|41)(0))) */
    /* JADX WARN: Code restructure failed: missing block: B:29:0x00b1, code lost:

        r0 = th;
     */
    /* JADX WARN: Code restructure failed: missing block: B:30:0x00bb, code lost:

        r0.handleBuilderException(r0);
     */
    /* JADX WARN: Code restructure failed: missing block: B:32:0x00b3, code lost:

        r0 = th;
     */
    /* JADX WARN: Code restructure failed: missing block: B:33:0x00b4, code lost:

        r15 = null;
     */
    /* JADX WARN: Code restructure failed: missing block: B:35:0x00b6, code lost:

        r0 = th;
     */
    /* JADX WARN: Code restructure failed: missing block: B:36:0x00b7, code lost:

        r15 = null;
     */
    /* JADX WARN: Failed to find 'out' block for switch in B:2:0x0009. Please report as an issue. */
    /* JADX WARN: Removed duplicated region for block: B:10:0x0075  */
    /* JADX WARN: Removed duplicated region for block: B:24:0x00c8  */
    /* JADX WARN: Removed duplicated region for block: B:26:0x00d0 A[RETURN] */
    /* JADX WARN: Removed duplicated region for block: B:27:0x00d1  */
    /* JADX WARN: Removed duplicated region for block: B:40:0x00d8  */
    /* JADX WARN: Unsupported multi-entry loop pattern (BACK_EDGE: B:27:0x00d1 -> B:7:0x00d3). Please report as a decompilation issue!!! */
    @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public final Object invokeSuspend(Object $result) {
        ReceiveChannel ticker;
        Object $result2;
        FlowCollector downstream;
        ReceiveChannel values;
        Ref.ObjectRef lastValue;
        FlowKt__DelayKt$sample$2 flowKt__DelayKt$sample$2;
        Object obj;
        Object obj2;
        Object $result3;
        Object coroutine_suspended = IntrinsicsKt.getCOROUTINE_SUSPENDED();
        Object obj3 = null;
        switch (this.label) {
            case 0:
                ResultKt.throwOnFailure($result);
                CoroutineScope $this$scopedFlow = (CoroutineScope) this.L$0;
                FlowCollector downstream2 = (FlowCollector) this.L$1;
                ReceiveChannel values2 = ProduceKt.produce$default($this$scopedFlow, null, -1, new FlowKt__DelayKt$sample$2$values$1(this.$this_sample, null), 1, null);
                Ref.ObjectRef lastValue2 = new Ref.ObjectRef();
                ticker = FlowKt__DelayKt.fixedPeriodTicker$default($this$scopedFlow, this.$periodMillis, 0L, 2, null);
                $result2 = $result;
                downstream = downstream2;
                values = values2;
                lastValue = lastValue2;
                flowKt__DelayKt$sample$2 = this;
                obj = coroutine_suspended;
                if (lastValue.element != NullSurrogateKt.DONE) {
                    flowKt__DelayKt$sample$2.L$0 = downstream;
                    flowKt__DelayKt$sample$2.L$1 = values;
                    flowKt__DelayKt$sample$2.L$2 = lastValue;
                    flowKt__DelayKt$sample$2.L$3 = ticker;
                    flowKt__DelayKt$sample$2.label = 1;
                    Continuation uCont$iv = flowKt__DelayKt$sample$2;
                    SelectInstance scope$iv = new SelectInstance(uCont$iv);
                    try {
                    } catch (Throwable th) {
                        e$iv = th;
                        obj2 = obj3;
                        break;
                    }
                    SelectInstance $this$invokeSuspend_u24lambda_u2d0 = scope$iv;
                    $this$invokeSuspend_u24lambda_u2d0.invoke(values.getOnReceiveCatching(), new FlowKt__DelayKt$sample$2$1$1(lastValue, ticker, null));
                    obj2 = null;
                    $this$invokeSuspend_u24lambda_u2d0.invoke(ticker.getOnReceive(), new FlowKt__DelayKt$sample$2$1$2(lastValue, downstream, null));
                    Object result = scope$iv.getResult();
                    if (result == IntrinsicsKt.getCOROUTINE_SUSPENDED()) {
                        DebugProbesKt.probeCoroutineSuspended(flowKt__DelayKt$sample$2);
                    }
                    if (result != obj) {
                        return obj;
                    }
                    $result3 = $result2;
                    $result2 = $result3;
                    obj3 = obj2;
                    if (lastValue.element != NullSurrogateKt.DONE) {
                        return Unit.INSTANCE;
                    }
                }
            case 1:
                ticker = (ReceiveChannel) this.L$3;
                lastValue = (Ref.ObjectRef) this.L$2;
                values = (ReceiveChannel) this.L$1;
                downstream = (FlowCollector) this.L$0;
                ResultKt.throwOnFailure($result);
                obj2 = null;
                obj = coroutine_suspended;
                $result3 = $result;
                flowKt__DelayKt$sample$2 = this;
                $result2 = $result3;
                obj3 = obj2;
                if (lastValue.element != NullSurrogateKt.DONE) {
                }
                break;
            default:
                throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
        }
    }
}
