package com.android.tools.smali.dexlib2.analysis;

import com.android.tools.smali.dexlib2.iface.Method;
import com.android.tools.smali.dexlib2.iface.reference.FieldReference;
import com.android.tools.smali.dexlib2.iface.reference.MethodReference;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/* loaded from: classes.dex */
public class UnknownClassProto implements TypeProto {

    @Nonnull
    protected final ClassPath classPath;

    public UnknownClassProto(@Nonnull ClassPath classPath) {
        this.classPath = classPath;
    }

    public String toString() {
        return "Ujava/lang/Object;";
    }

    @Override // com.android.tools.smali.dexlib2.analysis.TypeProto
    @Nonnull
    public ClassPath getClassPath() {
        return this.classPath;
    }

    @Override // com.android.tools.smali.dexlib2.analysis.TypeProto
    @Nullable
    public String getSuperclass() {
        return null;
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
    @Nonnull
    public TypeProto getCommonSuperclass(@Nonnull TypeProto other) {
        if (other.getType().equals("Ljava/lang/Object;")) {
            return other;
        }
        if (other instanceof ArrayProto) {
            return this.classPath.getClass("Ljava/lang/Object;");
        }
        return this;
    }

    @Override // com.android.tools.smali.dexlib2.analysis.TypeProto
    @Nonnull
    public String getType() {
        return "Ujava/lang/Object;";
    }

    @Override // com.android.tools.smali.dexlib2.analysis.TypeProto
    @Nullable
    public FieldReference getFieldByOffset(int fieldOffset) {
        return this.classPath.getClass("Ljava/lang/Object;").getFieldByOffset(fieldOffset);
    }

    @Override // com.android.tools.smali.dexlib2.analysis.TypeProto
    @Nullable
    public Method getMethodByVtableIndex(int vtableIndex) {
        return this.classPath.getClass("Ljava/lang/Object;").getMethodByVtableIndex(vtableIndex);
    }

    @Override // com.android.tools.smali.dexlib2.analysis.TypeProto
    public int findMethodIndexInVtable(@Nonnull MethodReference method) {
        return this.classPath.getClass("Ljava/lang/Object;").findMethodIndexInVtable(method);
    }
}
