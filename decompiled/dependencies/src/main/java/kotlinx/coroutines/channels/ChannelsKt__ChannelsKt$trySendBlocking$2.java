package kotlinx.coroutines.channels;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.exifinterface.media.ExifInterface;
import kotlin.Metadata;
import kotlin.Result;
import kotlin.ResultKt;
import kotlin.Unit;
import kotlin.coroutines.Continuation;
import kotlin.coroutines.intrinsics.IntrinsicsKt;
import kotlin.coroutines.jvm.internal.DebugMetadata;
import kotlin.coroutines.jvm.internal.SuspendLambda;
import kotlin.jvm.functions.Function2;
import kotlinx.coroutines.CoroutineScope;

/* JADX INFO: Access modifiers changed from: package-private */
/* compiled from: Channels.kt */
@Metadata(d1 = {"\u0000\u0010\n\u0000\n\u0002\u0018\u0002\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\u0010\u0000\u001a\b\u0012\u0004\u0012\u00020\u00020\u0001\"\u0004\b\u0000\u0010\u0003*\u00020\u0004H\u008a@"}, d2 = {"<anonymous>", "Lkotlinx/coroutines/channels/ChannelResult;", "", ExifInterface.LONGITUDE_EAST, "Lkotlinx/coroutines/CoroutineScope;"}, k = 3, mv = {1, 6, 0}, xi = ConstraintLayout.LayoutParams.Table.LAYOUT_CONSTRAINT_VERTICAL_CHAINSTYLE)
@DebugMetadata(c = "kotlinx.coroutines.channels.ChannelsKt__ChannelsKt$trySendBlocking$2", f = "Channels.kt", i = {}, l = {39}, m = "invokeSuspend", n = {}, s = {})
/* loaded from: classes7.dex */
public final class ChannelsKt__ChannelsKt$trySendBlocking$2 extends SuspendLambda implements Function2<CoroutineScope, Continuation<? super ChannelResult<? extends Unit>>, Object> {
    final /* synthetic */ E $element;
    final /* synthetic */ SendChannel<E> $this_trySendBlocking;
    private /* synthetic */ Object L$0;
    int label;

    /* JADX INFO: Access modifiers changed from: package-private */
    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    /* JADX WARN: Multi-variable type inference failed */
    public ChannelsKt__ChannelsKt$trySendBlocking$2(SendChannel<? super E> sendChannel, E e, Continuation<? super ChannelsKt__ChannelsKt$trySendBlocking$2> continuation) {
        super(2, continuation);
        this.$this_trySendBlocking = sendChannel;
        this.$element = e;
    }

    @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
    public final Continuation<Unit> create(Object obj, Continuation<?> continuation) {
        ChannelsKt__ChannelsKt$trySendBlocking$2 channelsKt__ChannelsKt$trySendBlocking$2 = new ChannelsKt__ChannelsKt$trySendBlocking$2(this.$this_trySendBlocking, this.$element, continuation);
        channelsKt__ChannelsKt$trySendBlocking$2.L$0 = obj;
        return channelsKt__ChannelsKt$trySendBlocking$2;
    }

    @Override // kotlin.jvm.functions.Function2
    public /* bridge */ /* synthetic */ Object invoke(CoroutineScope coroutineScope, Continuation<? super ChannelResult<? extends Unit>> continuation) {
        return invoke2(coroutineScope, (Continuation<? super ChannelResult<Unit>>) continuation);
    }

    /* renamed from: invoke, reason: avoid collision after fix types in other method */
    public final Object invoke2(CoroutineScope coroutineScope, Continuation<? super ChannelResult<Unit>> continuation) {
        return ((ChannelsKt__ChannelsKt$trySendBlocking$2) create(coroutineScope, continuation)).invokeSuspend(Unit.INSTANCE);
    }

    /* JADX WARN: Failed to find 'out' block for switch in B:2:0x0006. Please report as an issue. */
    /* JADX WARN: Removed duplicated region for block: B:12:0x004f  */
    /* JADX WARN: Removed duplicated region for block: B:15:0x0058  */
    @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public final Object invokeSuspend(Object $result) {
        Throwable th;
        Object r;
        Object coroutine_suspended = IntrinsicsKt.getCOROUTINE_SUSPENDED();
        switch (this.label) {
            case 0:
                ResultKt.throwOnFailure($result);
                SendChannel<E> sendChannel = this.$this_trySendBlocking;
                E e = this.$element;
                try {
                    Result.Companion companion = Result.INSTANCE;
                    this.label = 1;
                } catch (Throwable th2) {
                    th = th2;
                    Result.Companion companion2 = Result.INSTANCE;
                    r = Result.m168constructorimpl(ResultKt.createFailure(th));
                    return ChannelResult.m1664boximpl(!Result.m175isSuccessimpl(r) ? ChannelResult.INSTANCE.m1679successJP2dKIU(Unit.INSTANCE) : ChannelResult.INSTANCE.m1677closedJP2dKIU(Result.m171exceptionOrNullimpl(r)));
                }
                if (sendChannel.send(e, this) == coroutine_suspended) {
                    return coroutine_suspended;
                }
                r = Result.m168constructorimpl(Unit.INSTANCE);
                return ChannelResult.m1664boximpl(!Result.m175isSuccessimpl(r) ? ChannelResult.INSTANCE.m1679successJP2dKIU(Unit.INSTANCE) : ChannelResult.INSTANCE.m1677closedJP2dKIU(Result.m171exceptionOrNullimpl(r)));
            case 1:
                try {
                    ResultKt.throwOnFailure($result);
                    r = Result.m168constructorimpl(Unit.INSTANCE);
                } catch (Throwable th3) {
                    th = th3;
                    Result.Companion companion22 = Result.INSTANCE;
                    r = Result.m168constructorimpl(ResultKt.createFailure(th));
                    return ChannelResult.m1664boximpl(!Result.m175isSuccessimpl(r) ? ChannelResult.INSTANCE.m1679successJP2dKIU(Unit.INSTANCE) : ChannelResult.INSTANCE.m1677closedJP2dKIU(Result.m171exceptionOrNullimpl(r)));
                }
                return ChannelResult.m1664boximpl(!Result.m175isSuccessimpl(r) ? ChannelResult.INSTANCE.m1679successJP2dKIU(Unit.INSTANCE) : ChannelResult.INSTANCE.m1677closedJP2dKIU(Result.m171exceptionOrNullimpl(r)));
            default:
                throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
        }
    }
}
