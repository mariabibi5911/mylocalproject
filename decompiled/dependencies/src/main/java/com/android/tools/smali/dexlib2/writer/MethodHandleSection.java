package com.android.tools.smali.dexlib2.writer;

import com.android.tools.smali.dexlib2.iface.reference.FieldReference;
import com.android.tools.smali.dexlib2.iface.reference.MethodHandleReference;
import com.android.tools.smali.dexlib2.iface.reference.MethodReference;

/* loaded from: classes.dex */
public interface MethodHandleSection<MethodHandleKey extends MethodHandleReference, FieldRefKey extends FieldReference, MethodRefKey extends MethodReference> extends IndexSection<MethodHandleKey> {
    FieldRefKey getFieldReference(MethodHandleKey methodhandlekey);

    MethodRefKey getMethodReference(MethodHandleKey methodhandlekey);
}
