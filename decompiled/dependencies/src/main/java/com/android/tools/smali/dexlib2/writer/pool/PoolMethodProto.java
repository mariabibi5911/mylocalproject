package com.android.tools.smali.dexlib2.writer.pool;

import com.android.tools.smali.dexlib2.base.reference.BaseMethodProtoReference;
import com.android.tools.smali.dexlib2.iface.reference.MethodProtoReference;
import com.android.tools.smali.dexlib2.iface.reference.MethodReference;
import java.util.List;

/* loaded from: classes.dex */
public class PoolMethodProto extends BaseMethodProtoReference implements MethodProtoReference {
    private final MethodReference methodReference;

    public PoolMethodProto(MethodReference methodReference) {
        this.methodReference = methodReference;
    }

    @Override // com.android.tools.smali.dexlib2.iface.reference.MethodProtoReference
    public List<? extends CharSequence> getParameterTypes() {
        return this.methodReference.getParameterTypes();
    }

    @Override // com.android.tools.smali.dexlib2.iface.reference.MethodProtoReference
    public String getReturnType() {
        return this.methodReference.getReturnType();
    }
}
