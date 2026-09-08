package com.android.tools.smali.dexlib2.analysis.util;

import com.android.tools.smali.dexlib2.analysis.TypeProto;
import com.android.tools.smali.dexlib2.analysis.UnresolvedClassException;
import java.util.Iterator;
import java.util.NoSuchElementException;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/* loaded from: classes.dex */
public class TypeProtoUtils {
    @Nonnull
    public static Iterable<TypeProto> getSuperclassChain(@Nonnull final TypeProto typeProto) {
        return new Iterable<TypeProto>() { // from class: com.android.tools.smali.dexlib2.analysis.util.TypeProtoUtils.1
            @Override // java.lang.Iterable
            public Iterator<TypeProto> iterator() {
                return new Iterator<TypeProto>() { // from class: com.android.tools.smali.dexlib2.analysis.util.TypeProtoUtils.1.1

                    @Nullable
                    private TypeProto type;

                    {
                        this.type = TypeProtoUtils.getSuperclassAsTypeProto(TypeProto.this);
                    }

                    @Override // java.util.Iterator
                    public boolean hasNext() {
                        return this.type != null;
                    }

                    /* JADX WARN: Can't rename method to resolve collision */
                    @Override // java.util.Iterator
                    public TypeProto next() {
                        TypeProto type = this.type;
                        if (type == null) {
                            throw new NoSuchElementException();
                        }
                        this.type = TypeProtoUtils.getSuperclassAsTypeProto(type);
                        return type;
                    }

                    @Override // java.util.Iterator
                    public void remove() {
                        throw new UnsupportedOperationException();
                    }
                };
            }
        };
    }

    @Nullable
    public static TypeProto getSuperclassAsTypeProto(@Nonnull TypeProto type) {
        try {
            String next = type.getSuperclass();
            if (next != null) {
                return type.getClassPath().getClass(next);
            }
            return null;
        } catch (UnresolvedClassException e) {
            return type.getClassPath().getUnknownClass();
        }
    }

    public static boolean extendsFrom(@Nonnull TypeProto candidate, @Nonnull String possibleSuper) {
        if (candidate.getType().equals(possibleSuper)) {
            return true;
        }
        for (TypeProto superProto : getSuperclassChain(candidate)) {
            if (superProto.getType().equals(possibleSuper)) {
                return true;
            }
        }
        return false;
    }
}
