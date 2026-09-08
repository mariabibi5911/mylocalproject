package com.android.tools.smali.dexlib2.analysis;

import com.android.tools.smali.dexlib2.iface.Method;
import com.android.tools.smali.dexlib2.iface.reference.FieldReference;
import com.android.tools.smali.dexlib2.iface.reference.MethodReference;
import com.android.tools.smali.util.ExceptionWithContext;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/* loaded from: classes.dex */
public class PrimitiveProto implements TypeProto {
    protected final ClassPath classPath;
    protected final String type;

    public PrimitiveProto(@Nonnull ClassPath classPath, @Nonnull String type) {
        this.classPath = classPath;
        this.type = type;
    }

    public String toString() {
        return this.type;
    }

    @Override // com.android.tools.smali.dexlib2.analysis.TypeProto
    @Nonnull
    public ClassPath getClassPath() {
        return this.classPath;
    }

    @Override // com.android.tools.smali.dexlib2.analysis.TypeProto
    @Nonnull
    public String getType() {
        return this.type;
    }

    @Override // com.android.tools.smali.dexlib2.analysis.TypeProto
    public boolean isInterface() {
        return false;
    }

    @Override // com.android.tools.smali.dexlib2.analysis.TypeProto
    public boolean implementsInterface(@Nonnull String iface) {
        return false;
    }

    @Override // com.android.tools.smali.dexlib2.analysis.TypeProto
    @Nullable
    public String getSuperclass() {
        return null;
    }

    @Override // com.android.tools.smali.dexlib2.analysis.TypeProto
    @Nonnull
    public TypeProto getCommonSuperclass(@Nonnull TypeProto other) {
        throw new ExceptionWithContext("Cannot call getCommonSuperclass on PrimitiveProto", new Object[0]);
    }

    @Override // com.android.tools.smali.dexlib2.analysis.TypeProto
    @Nullable
    public FieldReference getFieldByOffset(int fieldOffset) {
        return null;
    }

    @Override // com.android.tools.smali.dexlib2.analysis.TypeProto
    @Nullable
    public Method getMethodByVtableIndex(int vtableIndex) {
        return null;
    }

    @Override // com.android.tools.smali.dexlib2.analysis.TypeProto
    public int findMethodIndexInVtable(@Nonnull MethodReference method) {
        return -1;
    }
}
