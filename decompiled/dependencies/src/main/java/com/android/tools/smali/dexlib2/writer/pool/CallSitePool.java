package com.android.tools.smali.dexlib2.writer.pool;

import com.android.tools.smali.dexlib2.iface.reference.CallSiteReference;
import com.android.tools.smali.dexlib2.iface.value.ArrayEncodedValue;
import com.android.tools.smali.dexlib2.writer.CallSiteSection;
import com.android.tools.smali.dexlib2.writer.util.CallSiteUtil;
import javax.annotation.Nonnull;

/* loaded from: classes.dex */
public class CallSitePool extends BaseIndexPool<CallSiteReference> implements CallSiteSection<CallSiteReference, ArrayEncodedValue> {
    public CallSitePool(@Nonnull DexPool dexPool) {
        super(dexPool);
    }

    public void intern(CallSiteReference callSiteReference) {
        Integer prev = (Integer) this.internedItems.put(callSiteReference, 0);
        if (prev == null) {
            ((EncodedArrayPool) this.dexPool.encodedArraySection).intern(getEncodedCallSite(callSiteReference));
        }
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // com.android.tools.smali.dexlib2.writer.CallSiteSection
    public ArrayEncodedValue getEncodedCallSite(CallSiteReference callSiteReference) {
        return CallSiteUtil.getEncodedCallSite(callSiteReference);
    }
}
