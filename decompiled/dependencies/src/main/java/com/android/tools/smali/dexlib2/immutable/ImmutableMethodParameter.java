package com.android.tools.smali.dexlib2.immutable;

import com.android.tools.smali.dexlib2.base.BaseMethodParameter;
import com.android.tools.smali.dexlib2.iface.Annotation;
import com.android.tools.smali.dexlib2.iface.MethodParameter;
import com.android.tools.smali.util.ImmutableConverter;
import com.android.tools.smali.util.ImmutableUtils;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import java.util.Set;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/* loaded from: classes.dex */
public class ImmutableMethodParameter extends BaseMethodParameter {
    private static final ImmutableConverter<ImmutableMethodParameter, MethodParameter> CONVERTER = new ImmutableConverter<ImmutableMethodParameter, MethodParameter>() { // from class: com.android.tools.smali.dexlib2.immutable.ImmutableMethodParameter.1
        /* JADX INFO: Access modifiers changed from: protected */
        @Override // com.android.tools.smali.util.ImmutableConverter
        public boolean isImmutable(@Nonnull MethodParameter item) {
            return item instanceof ImmutableMethodParameter;
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // com.android.tools.smali.util.ImmutableConverter
        @Nonnull
        public ImmutableMethodParameter makeImmutable(@Nonnull MethodParameter item) {
            return ImmutableMethodParameter.of(item);
        }
    };

    @Nonnull
    protected final ImmutableSet<? extends ImmutableAnnotation> annotations;

    @Nullable
    protected final String name;

    @Nonnull
    protected final String type;

    public ImmutableMethodParameter(@Nonnull String type, @Nullable Set<? extends Annotation> annotations, @Nullable String name) {
        this.type = type;
        this.annotations = ImmutableAnnotation.immutableSetOf(annotations);
        this.name = name;
    }

    public ImmutableMethodParameter(@Nonnull String type, @Nullable ImmutableSet<? extends ImmutableAnnotation> annotations, @Nullable String name) {
        this.type = type;
        this.annotations = ImmutableUtils.nullToEmptySet(annotations);
        this.name = name;
    }

    public static ImmutableMethodParameter of(MethodParameter methodParameter) {
        if (methodParameter instanceof ImmutableMethodParameter) {
            return (ImmutableMethodParameter) methodParameter;
        }
        return new ImmutableMethodParameter(methodParameter.getType(), methodParameter.getAnnotations(), methodParameter.getName());
    }

    @Override // com.android.tools.smali.dexlib2.iface.reference.TypeReference, com.android.tools.smali.dexlib2.iface.ClassDef
    @Nonnull
    public String getType() {
        return this.type;
    }

    @Override // com.android.tools.smali.dexlib2.iface.MethodParameter
    @Nonnull
    public Set<? extends Annotation> getAnnotations() {
        return this.annotations;
    }

    @Override // com.android.tools.smali.dexlib2.iface.MethodParameter, com.android.tools.smali.dexlib2.iface.debug.LocalInfo
    @Nullable
    public String getName() {
        return this.name;
    }

    @Override // com.android.tools.smali.dexlib2.base.BaseMethodParameter, com.android.tools.smali.dexlib2.iface.MethodParameter, com.android.tools.smali.dexlib2.iface.debug.LocalInfo
    @Nullable
    public String getSignature() {
        return null;
    }

    @Nonnull
    public static ImmutableList<ImmutableMethodParameter> immutableListOf(@Nullable Iterable<? extends MethodParameter> list) {
        return CONVERTER.toList(list);
    }
}
