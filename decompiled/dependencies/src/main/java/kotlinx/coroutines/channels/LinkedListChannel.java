package kotlinx.coroutines.channels;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.exifinterface.media.ExifInterface;
import java.util.ArrayList;
import kotlin.Metadata;
import kotlin.Unit;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Intrinsics;
import kotlinx.coroutines.channels.AbstractSendChannel;
import kotlinx.coroutines.internal.AtomicKt;
import kotlinx.coroutines.internal.OnUndeliveredElementKt;
import kotlinx.coroutines.internal.UndeliveredElementException;
import kotlinx.coroutines.selects.SelectInstance;
import kotlinx.coroutines.selects.SelectKt;

/* compiled from: LinkedListChannel.kt */
@Metadata(d1 = {"\u0000F\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0010\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0002\b\u0005\n\u0002\u0010\u0000\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\b\u0010\u0018\u0000*\u0004\b\u0000\u0010\u00012\b\u0012\u0004\u0012\u0002H\u00010\u0002B'\u0012 \u0010\u0003\u001a\u001c\u0012\u0004\u0012\u00028\u0000\u0012\u0004\u0012\u00020\u0005\u0018\u00010\u0004j\n\u0012\u0004\u0012\u00028\u0000\u0018\u0001`\u0006¢\u0006\u0002\u0010\u0007J\u0015\u0010\u000e\u001a\u00020\u000f2\u0006\u0010\u0010\u001a\u00028\u0000H\u0014¢\u0006\u0002\u0010\u0011J!\u0010\u0012\u001a\u00020\u000f2\u0006\u0010\u0010\u001a\u00028\u00002\n\u0010\u0013\u001a\u0006\u0012\u0002\b\u00030\u0014H\u0014¢\u0006\u0002\u0010\u0015J/\u0010\u0016\u001a\u00020\u00052\f\u0010\u0017\u001a\b\u0012\u0004\u0012\u00020\u00190\u00182\n\u0010\u001a\u001a\u0006\u0012\u0002\b\u00030\u001bH\u0014ø\u0001\u0000ø\u0001\u0001¢\u0006\u0004\b\u001c\u0010\u001dR\u0014\u0010\b\u001a\u00020\t8DX\u0084\u0004¢\u0006\u0006\u001a\u0004\b\b\u0010\nR\u0014\u0010\u000b\u001a\u00020\t8DX\u0084\u0004¢\u0006\u0006\u001a\u0004\b\u000b\u0010\nR\u0014\u0010\f\u001a\u00020\t8DX\u0084\u0004¢\u0006\u0006\u001a\u0004\b\f\u0010\nR\u0014\u0010\r\u001a\u00020\t8DX\u0084\u0004¢\u0006\u0006\u001a\u0004\b\r\u0010\n\u0082\u0002\u000b\n\u0002\b\u0019\n\u0005\b¡\u001e0\u0001¨\u0006\u001e"}, d2 = {"Lkotlinx/coroutines/channels/LinkedListChannel;", ExifInterface.LONGITUDE_EAST, "Lkotlinx/coroutines/channels/AbstractChannel;", "onUndeliveredElement", "Lkotlin/Function1;", "", "Lkotlinx/coroutines/internal/OnUndeliveredElement;", "(Lkotlin/jvm/functions/Function1;)V", "isBufferAlwaysEmpty", "", "()Z", "isBufferAlwaysFull", "isBufferEmpty", "isBufferFull", "offerInternal", "", "element", "(Ljava/lang/Object;)Ljava/lang/Object;", "offerSelectInternal", "select", "Lkotlinx/coroutines/selects/SelectInstance;", "(Ljava/lang/Object;Lkotlinx/coroutines/selects/SelectInstance;)Ljava/lang/Object;", "onCancelIdempotentList", "list", "Lkotlinx/coroutines/internal/InlineList;", "Lkotlinx/coroutines/channels/Send;", "closed", "Lkotlinx/coroutines/channels/Closed;", "onCancelIdempotentList-w-w6eGU", "(Ljava/lang/Object;Lkotlinx/coroutines/channels/Closed;)V", "kotlinx-coroutines-core"}, k = 1, mv = {1, 6, 0}, xi = ConstraintLayout.LayoutParams.Table.LAYOUT_CONSTRAINT_VERTICAL_CHAINSTYLE)
/* loaded from: classes7.dex */
public class LinkedListChannel<E> extends AbstractChannel<E> {
    public LinkedListChannel(Function1<? super E, Unit> function1) {
        super(function1);
    }

    @Override // kotlinx.coroutines.channels.AbstractChannel
    protected final boolean isBufferAlwaysEmpty() {
        return true;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // kotlinx.coroutines.channels.AbstractChannel
    public final boolean isBufferEmpty() {
        return true;
    }

    @Override // kotlinx.coroutines.channels.AbstractSendChannel
    protected final boolean isBufferAlwaysFull() {
        return false;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // kotlinx.coroutines.channels.AbstractSendChannel
    public final boolean isBufferFull() {
        return false;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // kotlinx.coroutines.channels.AbstractSendChannel
    public Object offerInternal(E element) {
        ReceiveOrClosed sendResult;
        do {
            Object result = super.offerInternal(element);
            if (result == AbstractChannelKt.OFFER_SUCCESS) {
                return AbstractChannelKt.OFFER_SUCCESS;
            }
            if (result == AbstractChannelKt.OFFER_FAILED) {
                sendResult = sendBuffered(element);
                if (sendResult == null) {
                    return AbstractChannelKt.OFFER_SUCCESS;
                }
            } else {
                if (result instanceof Closed) {
                    return result;
                }
                throw new IllegalStateException(Intrinsics.stringPlus("Invalid offerInternal result ", result).toString());
            }
        } while (!(sendResult instanceof Closed));
        return sendResult;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // kotlinx.coroutines.channels.AbstractSendChannel
    public Object offerSelectInternal(E element, SelectInstance<?> select) {
        Object result;
        while (true) {
            if (getHasReceiveOrClosed()) {
                result = super.offerSelectInternal(element, select);
            } else {
                result = select.performAtomicTrySelect(describeSendBuffered(element));
                if (result == null) {
                    result = AbstractChannelKt.OFFER_SUCCESS;
                }
            }
            if (result == SelectKt.getALREADY_SELECTED()) {
                return SelectKt.getALREADY_SELECTED();
            }
            if (result == AbstractChannelKt.OFFER_SUCCESS) {
                return AbstractChannelKt.OFFER_SUCCESS;
            }
            if (result != AbstractChannelKt.OFFER_FAILED && result != AtomicKt.RETRY_ATOMIC) {
                if (result instanceof Closed) {
                    return result;
                }
                throw new IllegalStateException(Intrinsics.stringPlus("Invalid result ", result).toString());
            }
        }
    }

    /* JADX WARN: Code restructure failed: missing block: B:14:0x0037, code lost:

        if (r4 >= 0) goto L19;
     */
    /* JADX WARN: Code restructure failed: missing block: B:15:0x0039, code lost:

        r5 = r4;
        r4 = r4 - 1;
        r6 = (kotlinx.coroutines.channels.Send) r2.get(r5);
     */
    /* JADX WARN: Code restructure failed: missing block: B:16:0x0046, code lost:

        if ((r6 instanceof kotlinx.coroutines.channels.AbstractSendChannel.SendBuffered) == false) goto L26;
     */
    /* JADX WARN: Code restructure failed: missing block: B:17:0x0048, code lost:

        r8 = r10.onUndeliveredElement;
     */
    /* JADX WARN: Code restructure failed: missing block: B:18:0x004a, code lost:

        if (r8 != null) goto L24;
     */
    /* JADX WARN: Code restructure failed: missing block: B:19:0x004c, code lost:

        r8 = null;
     */
    /* JADX WARN: Code restructure failed: missing block: B:20:0x0057, code lost:

        r0 = r8;
     */
    /* JADX WARN: Code restructure failed: missing block: B:22:0x005d, code lost:

        if (r4 >= 0) goto L39;
     */
    /* JADX WARN: Code restructure failed: missing block: B:25:0x004e, code lost:

        r8 = kotlinx.coroutines.internal.OnUndeliveredElementKt.callUndeliveredElementCatchingException(r8, ((kotlinx.coroutines.channels.AbstractSendChannel.SendBuffered) r6).element, r0);
     */
    /* JADX WARN: Code restructure failed: missing block: B:26:0x0059, code lost:

        r6.resumeSendClosed(r12);
     */
    @Override // kotlinx.coroutines.channels.AbstractChannel
    /* renamed from: onCancelIdempotentList-w-w6eGU */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    protected void mo1656onCancelIdempotentListww6eGU(Object list, Closed<?> closed) {
        UndeliveredElementException it = null;
        if (list != null) {
            if (!(list instanceof ArrayList)) {
                Send it2 = (Send) list;
                if (it2 instanceof AbstractSendChannel.SendBuffered) {
                    Function1<E, Unit> function1 = this.onUndeliveredElement;
                    it = function1 != null ? OnUndeliveredElementKt.callUndeliveredElementCatchingException(function1, ((AbstractSendChannel.SendBuffered) it2).element, (UndeliveredElementException) null) : null;
                } else {
                    it2.resumeSendClosed(closed);
                }
            } else {
                if (list == null) {
                    throw new NullPointerException("null cannot be cast to non-null type java.util.ArrayList<E of kotlinx.coroutines.internal.InlineList>{ kotlin.collections.TypeAliasesKt.ArrayList<E of kotlinx.coroutines.internal.InlineList> }");
                }
                ArrayList list$iv = (ArrayList) list;
                int size = list$iv.size() - 1;
            }
        }
        if (it != null) {
            throw it;
        }
    }
}
