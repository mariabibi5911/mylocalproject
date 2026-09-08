package com.android.tools.smali.dexlib2.analysis;

import com.android.tools.smali.dexlib2.iface.Method;
import com.android.tools.smali.dexlib2.iface.reference.FieldReference;
import com.android.tools.smali.dexlib2.iface.reference.MethodReference;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/* loaded from: classes.dex */
public interface TypeProto {
    int findMethodIndexInVtable(@Nonnull MethodReference methodReference);

    @Nonnull
    ClassPath getClassPath();

    @Nonnull
    TypeProto getCommonSuperclass(@Nonnull TypeProto typeProto);

    @Nullable
    FieldReference getFieldByOffset(int i);

    @Nullable
    Method getMethodByVtableIndex(int i);

    @Nullable
    String getSuperclass();

    @Nonnull
    String getType();

    boolean implementsInterface(@Nonnull String str);

    boolean isInterface();
}
