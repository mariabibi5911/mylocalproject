package com.android.tools.smali.dexlib2.iface;

import com.android.tools.smali.dexlib2.iface.reference.TypeReference;
import java.util.List;
import java.util.Set;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/* loaded from: classes.dex */
public interface ClassDef extends TypeReference, Annotatable {
    int getAccessFlags();

    @Override // com.android.tools.smali.dexlib2.iface.Annotatable
    @Nonnull
    Set<? extends Annotation> getAnnotations();

    @Nonnull
    Iterable<? extends Method> getDirectMethods();

    @Nonnull
    Iterable<? extends Field> getFields();

    @Nonnull
    Iterable<? extends Field> getInstanceFields();

    @Nonnull
    List<String> getInterfaces();

    @Nonnull
    Iterable<? extends Method> getMethods();

    @Nullable
    String getSourceFile();

    @Nonnull
    Iterable<? extends Field> getStaticFields();

    @Nullable
    String getSuperclass();

    @Nonnull
    String getType();

    @Nonnull
    Iterable<? extends Method> getVirtualMethods();
}
