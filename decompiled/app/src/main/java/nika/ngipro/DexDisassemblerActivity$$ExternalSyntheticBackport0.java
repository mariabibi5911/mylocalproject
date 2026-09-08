package nika.ngipro;

import java.util.Iterator;

/* compiled from: D8$$SyntheticClass */
/* loaded from: classes4.dex */
public final /* synthetic */ class DexDisassemblerActivity$$ExternalSyntheticBackport0 {
    public static /* synthetic */ String m(CharSequence charSequence, Iterable iterable) {
        if (charSequence == null) {
            throw new NullPointerException("delimiter");
        }
        StringBuilder sb = new StringBuilder();
        Iterator it = iterable.iterator();
        if (it.hasNext()) {
            while (true) {
                sb.append((CharSequence) it.next());
                if (!it.hasNext()) {
                    break;
                }
                sb.append(charSequence);
            }
        }
        return sb.toString();
    }
}
