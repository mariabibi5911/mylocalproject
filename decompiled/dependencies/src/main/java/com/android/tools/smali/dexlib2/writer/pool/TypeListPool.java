package com.android.tools.smali.dexlib2.writer.pool;

import com.android.tools.smali.dexlib2.writer.TypeListSection;
import com.google.common.collect.ImmutableList;
import java.util.Collection;
import java.util.Iterator;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/* loaded from: classes.dex */
public class TypeListPool extends BaseNullableOffsetPool<Key<? extends Collection<? extends CharSequence>>> implements TypeListSection<CharSequence, Key<? extends Collection<? extends CharSequence>>> {
    public TypeListPool(@Nonnull DexPool dexPool) {
        super(dexPool);
    }

    public void intern(@Nonnull Collection<? extends CharSequence> types) {
        if (types.size() > 0) {
            Key<? extends Collection<? extends CharSequence>> key = new Key<>(types);
            Integer prev = (Integer) this.internedItems.put(key, 0);
            if (prev == null) {
                for (CharSequence type : types) {
                    ((TypePool) this.dexPool.typeSection).intern(type);
                }
            }
        }
    }

    @Override // com.android.tools.smali.dexlib2.writer.TypeListSection
    @Nonnull
    public Collection<? extends CharSequence> getTypes(Key<? extends Collection<? extends CharSequence>> typesKey) {
        if (typesKey == null) {
            return ImmutableList.of();
        }
        return typesKey.types;
    }

    @Override // com.android.tools.smali.dexlib2.writer.pool.BaseNullableOffsetPool, com.android.tools.smali.dexlib2.writer.NullableOffsetSection
    public int getNullableItemOffset(@Nullable Key<? extends Collection<? extends CharSequence>> key) {
        if (key == null || key.types.size() == 0) {
            return 0;
        }
        return super.getNullableItemOffset((TypeListPool) key);
    }

    /* loaded from: classes.dex */
    public static class Key<TypeCollection extends Collection<? extends CharSequence>> implements Comparable<Key<? extends Collection<? extends CharSequence>>> {

        @Nonnull
        TypeCollection types;

        public Key(@Nonnull TypeCollection types) {
            this.types = types;
        }

        public int hashCode() {
            int hashCode = 1;
            for (CharSequence type : this.types) {
                hashCode = (hashCode * 31) + type.toString().hashCode();
            }
            return hashCode;
        }

        public boolean equals(Object o) {
            if (!(o instanceof Key)) {
                return false;
            }
            Key<? extends Collection<? extends CharSequence>> other = (Key) o;
            if (this.types.size() != other.types.size()) {
                return false;
            }
            Iterator<? extends CharSequence> otherTypes = other.types.iterator();
            for (CharSequence type : this.types) {
                if (!type.toString().equals(((CharSequence) otherTypes.next()).toString())) {
                    return false;
                }
            }
            return true;
        }

        public String toString() {
            StringBuilder sb = new StringBuilder();
            for (CharSequence type : this.types) {
                sb.append(type.toString());
            }
            return sb.toString();
        }

        @Override // java.lang.Comparable
        public int compareTo(Key<? extends Collection<? extends CharSequence>> o) {
            Iterator<? extends CharSequence> other = o.types.iterator();
            for (CharSequence type : this.types) {
                if (!other.hasNext()) {
                    return 1;
                }
                int comparison = type.toString().compareTo(((CharSequence) other.next()).toString());
                if (comparison != 0) {
                    return comparison;
                }
            }
            if (other.hasNext()) {
                return -1;
            }
            return 0;
        }
    }
}
