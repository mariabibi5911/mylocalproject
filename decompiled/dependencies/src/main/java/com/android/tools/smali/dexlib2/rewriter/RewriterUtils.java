package com.android.tools.smali.dexlib2.rewriter;

import com.android.tools.smali.dexlib2.base.reference.BaseMethodHandleReference;
import com.android.tools.smali.dexlib2.base.reference.BaseMethodProtoReference;
import com.android.tools.smali.dexlib2.base.reference.BaseTypeReference;
import com.android.tools.smali.dexlib2.base.value.BaseFieldEncodedValue;
import com.android.tools.smali.dexlib2.base.value.BaseMethodEncodedValue;
import com.android.tools.smali.dexlib2.base.value.BaseMethodHandleEncodedValue;
import com.android.tools.smali.dexlib2.base.value.BaseMethodTypeEncodedValue;
import com.android.tools.smali.dexlib2.base.value.BaseTypeEncodedValue;
import com.android.tools.smali.dexlib2.iface.reference.FieldReference;
import com.android.tools.smali.dexlib2.iface.reference.MethodHandleReference;
import com.android.tools.smali.dexlib2.iface.reference.MethodProtoReference;
import com.android.tools.smali.dexlib2.iface.reference.MethodReference;
import com.android.tools.smali.dexlib2.iface.reference.Reference;
import com.android.tools.smali.dexlib2.iface.reference.TypeReference;
import com.android.tools.smali.dexlib2.iface.value.EncodedValue;
import com.android.tools.smali.dexlib2.iface.value.FieldEncodedValue;
import com.android.tools.smali.dexlib2.iface.value.MethodEncodedValue;
import com.android.tools.smali.dexlib2.iface.value.MethodHandleEncodedValue;
import com.android.tools.smali.dexlib2.iface.value.MethodTypeEncodedValue;
import com.android.tools.smali.dexlib2.iface.value.TypeEncodedValue;
import com.android.tools.smali.util.ExceptionWithContext;
import com.google.common.base.Function;
import com.google.common.collect.Lists;
import java.util.AbstractList;
import java.util.AbstractSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/* loaded from: classes.dex */
public class RewriterUtils {
    @Nullable
    public static <T> T rewriteNullable(@Nonnull Rewriter<T> rewriter, @Nullable T value) {
        if (value == null) {
            return null;
        }
        return rewriter.rewrite(value);
    }

    public static <T> Set<T> rewriteSet(@Nonnull final Rewriter<T> rewriter, @Nonnull final Set<? extends T> set) {
        return new AbstractSet<T>() { // from class: com.android.tools.smali.dexlib2.rewriter.RewriterUtils.1
            @Override // java.util.AbstractCollection, java.util.Collection, java.lang.Iterable, java.util.Set
            @Nonnull
            public Iterator<T> iterator() {
                final Iterator<? extends T> iterator = set.iterator();
                return new Iterator<T>() { // from class: com.android.tools.smali.dexlib2.rewriter.RewriterUtils.1.1
                    @Override // java.util.Iterator
                    public boolean hasNext() {
                        return iterator.hasNext();
                    }

                    @Override // java.util.Iterator
                    public T next() {
                        return (T) RewriterUtils.rewriteNullable(rewriter, iterator.next());
                    }

                    @Override // java.util.Iterator
                    public void remove() {
                        iterator.remove();
                    }
                };
            }

            @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
            public int size() {
                return set.size();
            }
        };
    }

    public static <T> List<T> rewriteList(@Nonnull final Rewriter<T> rewriter, @Nonnull final List<? extends T> list) {
        return new AbstractList<T>() { // from class: com.android.tools.smali.dexlib2.rewriter.RewriterUtils.2
            @Override // java.util.AbstractList, java.util.List
            public T get(int i) {
                return (T) RewriterUtils.rewriteNullable(Rewriter.this, list.get(i));
            }

            @Override // java.util.AbstractCollection, java.util.Collection, java.util.List
            public int size() {
                return list.size();
            }
        };
    }

    public static <T> Iterable<T> rewriteIterable(@Nonnull final Rewriter<T> rewriter, @Nonnull final Iterable<? extends T> iterable) {
        return new Iterable<T>() { // from class: com.android.tools.smali.dexlib2.rewriter.RewriterUtils.3
            @Override // java.lang.Iterable
            public Iterator<T> iterator() {
                final Iterator<T> it = iterable.iterator();
                return new Iterator<T>() { // from class: com.android.tools.smali.dexlib2.rewriter.RewriterUtils.3.1
                    @Override // java.util.Iterator
                    public boolean hasNext() {
                        return it.hasNext();
                    }

                    @Override // java.util.Iterator
                    public T next() {
                        return (T) RewriterUtils.rewriteNullable(rewriter, it.next());
                    }

                    @Override // java.util.Iterator
                    public void remove() {
                        it.remove();
                    }
                };
            }
        };
    }

    public static TypeReference rewriteTypeReference(@Nonnull final Rewriter<String> typeRewriter, @Nonnull final TypeReference typeReference) {
        return new BaseTypeReference() { // from class: com.android.tools.smali.dexlib2.rewriter.RewriterUtils.4
            @Override // com.android.tools.smali.dexlib2.iface.reference.TypeReference, com.android.tools.smali.dexlib2.iface.ClassDef
            @Nonnull
            public String getType() {
                return (String) Rewriter.this.rewrite(typeReference.getType());
            }
        };
    }

    @Nonnull
    public static MethodHandleReference rewriteMethodHandleReference(@Nonnull final Rewriters rewriters, @Nonnull final MethodHandleReference methodHandleReference) {
        switch (methodHandleReference.getMethodHandleType()) {
            case 0:
            case 1:
            case 2:
            case 3:
                return new BaseMethodHandleReference() { // from class: com.android.tools.smali.dexlib2.rewriter.RewriterUtils.5
                    @Override // com.android.tools.smali.dexlib2.iface.reference.MethodHandleReference
                    public int getMethodHandleType() {
                        return MethodHandleReference.this.getMethodHandleType();
                    }

                    @Override // com.android.tools.smali.dexlib2.iface.reference.MethodHandleReference
                    @Nonnull
                    public Reference getMemberReference() {
                        return rewriters.getFieldReferenceRewriter().rewrite((FieldReference) MethodHandleReference.this.getMemberReference());
                    }
                };
            case 4:
            case 5:
            case 6:
            case 7:
            case 8:
                return new BaseMethodHandleReference() { // from class: com.android.tools.smali.dexlib2.rewriter.RewriterUtils.6
                    @Override // com.android.tools.smali.dexlib2.iface.reference.MethodHandleReference
                    public int getMethodHandleType() {
                        return MethodHandleReference.this.getMethodHandleType();
                    }

                    @Override // com.android.tools.smali.dexlib2.iface.reference.MethodHandleReference
                    @Nonnull
                    public Reference getMemberReference() {
                        return rewriters.getMethodReferenceRewriter().rewrite((MethodReference) MethodHandleReference.this.getMemberReference());
                    }
                };
            default:
                throw new ExceptionWithContext("Invalid method handle type: %d", Integer.valueOf(methodHandleReference.getMethodHandleType()));
        }
    }

    @Nonnull
    public static MethodProtoReference rewriteMethodProtoReference(@Nonnull final Rewriter<String> typeRewriter, @Nonnull final MethodProtoReference methodProtoReference) {
        return new BaseMethodProtoReference() { // from class: com.android.tools.smali.dexlib2.rewriter.RewriterUtils.7
            @Override // com.android.tools.smali.dexlib2.iface.reference.MethodProtoReference
            @Nonnull
            public List<? extends CharSequence> getParameterTypes() {
                return RewriterUtils.rewriteList(Rewriter.this, Lists.transform(methodProtoReference.getParameterTypes(), new Function<CharSequence, String>() { // from class: com.android.tools.smali.dexlib2.rewriter.RewriterUtils.7.1
                    @Override // com.google.common.base.Function
                    @Nonnull
                    public String apply(CharSequence input) {
                        return input.toString();
                    }
                }));
            }

            @Override // com.android.tools.smali.dexlib2.iface.reference.MethodProtoReference
            @Nonnull
            public String getReturnType() {
                return (String) Rewriter.this.rewrite(methodProtoReference.getReturnType());
            }
        };
    }

    @Nonnull
    public static EncodedValue rewriteValue(@Nonnull final Rewriters rewriters, @Nonnull final EncodedValue encodedValue) {
        switch (encodedValue.getValueType()) {
            case 4:
            case 6:
            case 16:
            case 17:
            case 23:
                return encodedValue;
            case 21:
                return new BaseMethodTypeEncodedValue() { // from class: com.android.tools.smali.dexlib2.rewriter.RewriterUtils.8
                    @Override // com.android.tools.smali.dexlib2.iface.value.MethodTypeEncodedValue
                    @Nonnull
                    public MethodProtoReference getValue() {
                        return RewriterUtils.rewriteMethodProtoReference(Rewriters.this.getTypeRewriter(), ((MethodTypeEncodedValue) encodedValue).getValue());
                    }
                };
            case 22:
                return new BaseMethodHandleEncodedValue() { // from class: com.android.tools.smali.dexlib2.rewriter.RewriterUtils.9
                    @Override // com.android.tools.smali.dexlib2.iface.value.MethodHandleEncodedValue
                    @Nonnull
                    public MethodHandleReference getValue() {
                        return RewriterUtils.rewriteMethodHandleReference(Rewriters.this, ((MethodHandleEncodedValue) encodedValue).getValue());
                    }
                };
            case 24:
                return new BaseTypeEncodedValue() { // from class: com.android.tools.smali.dexlib2.rewriter.RewriterUtils.10
                    @Override // com.android.tools.smali.dexlib2.iface.value.TypeEncodedValue
                    @Nonnull
                    public String getValue() {
                        return Rewriters.this.getTypeRewriter().rewrite(((TypeEncodedValue) encodedValue).getValue());
                    }
                };
            case 25:
                return new BaseFieldEncodedValue() { // from class: com.android.tools.smali.dexlib2.rewriter.RewriterUtils.11
                    @Override // com.android.tools.smali.dexlib2.iface.value.FieldEncodedValue
                    @Nonnull
                    public FieldReference getValue() {
                        return Rewriters.this.getFieldReferenceRewriter().rewrite(((FieldEncodedValue) encodedValue).getValue());
                    }
                };
            case 26:
                return new BaseMethodEncodedValue() { // from class: com.android.tools.smali.dexlib2.rewriter.RewriterUtils.12
                    @Override // com.android.tools.smali.dexlib2.iface.value.MethodEncodedValue
                    @Nonnull
                    public MethodReference getValue() {
                        return Rewriters.this.getMethodReferenceRewriter().rewrite(((MethodEncodedValue) encodedValue).getValue());
                    }
                };
            default:
                throw new ExceptionWithContext("Unsupported encoded value type: %d", Integer.valueOf(encodedValue.getValueType()));
        }
    }
}
