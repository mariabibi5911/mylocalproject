package com.android.tools.smali.dexlib2.writer;

import com.android.tools.smali.dexlib2.iface.reference.CallSiteReference;

/* loaded from: classes.dex */
public interface CallSiteSection<CallSiteKey extends CallSiteReference, EncodedArrayKey> extends IndexSection<CallSiteKey> {
    EncodedArrayKey getEncodedCallSite(CallSiteKey callsitekey);
}
