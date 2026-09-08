package com.android.tools.smali.dexlib2.writer;

import java.util.List;

/* loaded from: classes.dex */
public interface EncodedArraySection<EncodedArrayKey, EncodedValue> extends OffsetSection<EncodedArrayKey> {
    List<? extends EncodedValue> getEncodedValueList(EncodedArrayKey encodedarraykey);
}
