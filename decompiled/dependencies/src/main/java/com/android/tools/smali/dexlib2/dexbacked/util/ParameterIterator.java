package com.android.tools.smali.dexlib2.dexbacked.util;

import com.android.tools.smali.dexlib2.base.BaseMethodParameter;
import com.android.tools.smali.dexlib2.iface.Annotation;
import com.android.tools.smali.dexlib2.iface.MethodParameter;
import com.google.common.collect.ImmutableSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/* loaded from: classes.dex */
public class ParameterIterator implements Iterator<MethodParameter> {
    private final Iterator<? extends Set<? extends Annotation>> parameterAnnotations;
    private final Iterator<String> parameterNames;
    private final Iterator<? extends CharSequence> parameterTypes;

    public ParameterIterator(@Nonnull List<? extends CharSequence> parameterTypes, @Nonnull List<? extends Set<? extends Annotation>> parameterAnnotations, @Nonnull Iterator<String> parameterNames) {
        this.parameterTypes = parameterTypes.iterator();
        this.parameterAnnotations = parameterAnnotations.iterator();
        this.parameterNames = parameterNames;
    }

    @Override // java.util.Iterator
    public boolean hasNext() {
        return this.parameterTypes.hasNext();
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // java.util.Iterator
    public MethodParameter next() {
        final Set<? extends Annotation> annotations;
        final String name;
        final String type = this.parameterTypes.next().toString();
        if (this.parameterAnnotations.hasNext()) {
            annotations = this.parameterAnnotations.next();
        } else {
            annotations = ImmutableSet.of();
        }
        if (this.parameterNames.hasNext()) {
            name = this.parameterNames.next();
        } else {
            name = null;
        }
        return new BaseMethodParameter() { // from class: com.android.tools.smali.dexlib2.dexbacked.util.ParameterIterator.1
            @Override // com.android.tools.smali.dexlib2.iface.MethodParameter
            @Nonnull
            public Set<? extends Annotation> getAnnotations() {
                return annotations;
            }

            @Override // com.android.tools.smali.dexlib2.iface.MethodParameter, com.android.tools.smali.dexlib2.iface.debug.LocalInfo
            @Nullable
            public String getName() {
                return name;
            }

            @Override // com.android.tools.smali.dexlib2.iface.reference.TypeReference, com.android.tools.smali.dexlib2.iface.ClassDef
            @Nonnull
            public String getType() {
                return type;
            }
        };
    }

    @Override // java.util.Iterator
    public void remove() {
        throw new UnsupportedOperationException();
    }
}
