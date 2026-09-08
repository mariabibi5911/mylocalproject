package com.google.common.collect;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableCollection;
import com.google.errorprone.annotations.DoNotMock;
import com.google.errorprone.annotations.concurrent.LazyInit;
import java.io.Serializable;
import java.util.AbstractMap;
import java.util.Arrays;
import java.util.BitSet;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.SortedMap;
import javax.annotation.CheckForNull;

@DoNotMock("Use ImmutableMap.of or another implementation")
@ElementTypesAreNonnullByDefault
/* loaded from: classes.dex */
public abstract class ImmutableMap<K, V> implements Map<K, V>, Serializable {
    static final Map.Entry<?, ?>[] EMPTY_ENTRY_ARRAY = new Map.Entry[0];

    @CheckForNull
    @LazyInit
    private transient ImmutableSet<Map.Entry<K, V>> entrySet;

    @CheckForNull
    @LazyInit
    private transient ImmutableSet<K> keySet;

    @CheckForNull
    @LazyInit
    private transient ImmutableSetMultimap<K, V> multimapView;

    @CheckForNull
    @LazyInit
    private transient ImmutableCollection<V> values;

    abstract ImmutableSet<Map.Entry<K, V>> createEntrySet();

    abstract ImmutableSet<K> createKeySet();

    abstract ImmutableCollection<V> createValues();

    @Override // java.util.Map
    @CheckForNull
    public abstract V get(@CheckForNull Object obj);

    /* JADX INFO: Access modifiers changed from: package-private */
    public abstract boolean isPartialView();

    public static <K, V> ImmutableMap<K, V> of() {
        return (ImmutableMap<K, V>) RegularImmutableMap.EMPTY;
    }

    public static <K, V> ImmutableMap<K, V> of(K k1, V v1) {
        CollectPreconditions.checkEntryNotNull(k1, v1);
        return RegularImmutableMap.create(1, new Object[]{k1, v1});
    }

    public static <K, V> ImmutableMap<K, V> of(K k1, V v1, K k2, V v2) {
        CollectPreconditions.checkEntryNotNull(k1, v1);
        CollectPreconditions.checkEntryNotNull(k2, v2);
        return RegularImmutableMap.create(2, new Object[]{k1, v1, k2, v2});
    }

    public static <K, V> ImmutableMap<K, V> of(K k1, V v1, K k2, V v2, K k3, V v3) {
        CollectPreconditions.checkEntryNotNull(k1, v1);
        CollectPreconditions.checkEntryNotNull(k2, v2);
        CollectPreconditions.checkEntryNotNull(k3, v3);
        return RegularImmutableMap.create(3, new Object[]{k1, v1, k2, v2, k3, v3});
    }

    public static <K, V> ImmutableMap<K, V> of(K k1, V v1, K k2, V v2, K k3, V v3, K k4, V v4) {
        CollectPreconditions.checkEntryNotNull(k1, v1);
        CollectPreconditions.checkEntryNotNull(k2, v2);
        CollectPreconditions.checkEntryNotNull(k3, v3);
        CollectPreconditions.checkEntryNotNull(k4, v4);
        return RegularImmutableMap.create(4, new Object[]{k1, v1, k2, v2, k3, v3, k4, v4});
    }

    public static <K, V> ImmutableMap<K, V> of(K k1, V v1, K k2, V v2, K k3, V v3, K k4, V v4, K k5, V v5) {
        CollectPreconditions.checkEntryNotNull(k1, v1);
        CollectPreconditions.checkEntryNotNull(k2, v2);
        CollectPreconditions.checkEntryNotNull(k3, v3);
        CollectPreconditions.checkEntryNotNull(k4, v4);
        CollectPreconditions.checkEntryNotNull(k5, v5);
        return RegularImmutableMap.create(5, new Object[]{k1, v1, k2, v2, k3, v3, k4, v4, k5, v5});
    }

    public static <K, V> ImmutableMap<K, V> of(K k1, V v1, K k2, V v2, K k3, V v3, K k4, V v4, K k5, V v5, K k6, V v6) {
        CollectPreconditions.checkEntryNotNull(k1, v1);
        CollectPreconditions.checkEntryNotNull(k2, v2);
        CollectPreconditions.checkEntryNotNull(k3, v3);
        CollectPreconditions.checkEntryNotNull(k4, v4);
        CollectPreconditions.checkEntryNotNull(k5, v5);
        CollectPreconditions.checkEntryNotNull(k6, v6);
        return RegularImmutableMap.create(6, new Object[]{k1, v1, k2, v2, k3, v3, k4, v4, k5, v5, k6, v6});
    }

    public static <K, V> ImmutableMap<K, V> of(K k1, V v1, K k2, V v2, K k3, V v3, K k4, V v4, K k5, V v5, K k6, V v6, K k7, V v7) {
        CollectPreconditions.checkEntryNotNull(k1, v1);
        CollectPreconditions.checkEntryNotNull(k2, v2);
        CollectPreconditions.checkEntryNotNull(k3, v3);
        CollectPreconditions.checkEntryNotNull(k4, v4);
        CollectPreconditions.checkEntryNotNull(k5, v5);
        CollectPreconditions.checkEntryNotNull(k6, v6);
        CollectPreconditions.checkEntryNotNull(k7, v7);
        return RegularImmutableMap.create(7, new Object[]{k1, v1, k2, v2, k3, v3, k4, v4, k5, v5, k6, v6, k7, v7});
    }

    public static <K, V> ImmutableMap<K, V> of(K k1, V v1, K k2, V v2, K k3, V v3, K k4, V v4, K k5, V v5, K k6, V v6, K k7, V v7, K k8, V v8) {
        CollectPreconditions.checkEntryNotNull(k1, v1);
        CollectPreconditions.checkEntryNotNull(k2, v2);
        CollectPreconditions.checkEntryNotNull(k3, v3);
        CollectPreconditions.checkEntryNotNull(k4, v4);
        CollectPreconditions.checkEntryNotNull(k5, v5);
        CollectPreconditions.checkEntryNotNull(k6, v6);
        CollectPreconditions.checkEntryNotNull(k7, v7);
        CollectPreconditions.checkEntryNotNull(k8, v8);
        return RegularImmutableMap.create(8, new Object[]{k1, v1, k2, v2, k3, v3, k4, v4, k5, v5, k6, v6, k7, v7, k8, v8});
    }

    public static <K, V> ImmutableMap<K, V> of(K k1, V v1, K k2, V v2, K k3, V v3, K k4, V v4, K k5, V v5, K k6, V v6, K k7, V v7, K k8, V v8, K k9, V v9) {
        CollectPreconditions.checkEntryNotNull(k1, v1);
        CollectPreconditions.checkEntryNotNull(k2, v2);
        CollectPreconditions.checkEntryNotNull(k3, v3);
        CollectPreconditions.checkEntryNotNull(k4, v4);
        CollectPreconditions.checkEntryNotNull(k5, v5);
        CollectPreconditions.checkEntryNotNull(k6, v6);
        CollectPreconditions.checkEntryNotNull(k7, v7);
        CollectPreconditions.checkEntryNotNull(k8, v8);
        CollectPreconditions.checkEntryNotNull(k9, v9);
        return RegularImmutableMap.create(9, new Object[]{k1, v1, k2, v2, k3, v3, k4, v4, k5, v5, k6, v6, k7, v7, k8, v8, k9, v9});
    }

    public static <K, V> ImmutableMap<K, V> of(K k1, V v1, K k2, V v2, K k3, V v3, K k4, V v4, K k5, V v5, K k6, V v6, K k7, V v7, K k8, V v8, K k9, V v9, K k10, V v10) {
        CollectPreconditions.checkEntryNotNull(k1, v1);
        CollectPreconditions.checkEntryNotNull(k2, v2);
        CollectPreconditions.checkEntryNotNull(k3, v3);
        CollectPreconditions.checkEntryNotNull(k4, v4);
        CollectPreconditions.checkEntryNotNull(k5, v5);
        CollectPreconditions.checkEntryNotNull(k6, v6);
        CollectPreconditions.checkEntryNotNull(k7, v7);
        CollectPreconditions.checkEntryNotNull(k8, v8);
        CollectPreconditions.checkEntryNotNull(k9, v9);
        CollectPreconditions.checkEntryNotNull(k10, v10);
        return RegularImmutableMap.create(10, new Object[]{k1, v1, k2, v2, k3, v3, k4, v4, k5, v5, k6, v6, k7, v7, k8, v8, k9, v9, k10, v10});
    }

    @SafeVarargs
    public static <K, V> ImmutableMap<K, V> ofEntries(Map.Entry<? extends K, ? extends V>... entries) {
        return copyOf(Arrays.asList(entries));
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static <K, V> Map.Entry<K, V> entryOf(K key, V value) {
        CollectPreconditions.checkEntryNotNull(key, value);
        return new AbstractMap.SimpleImmutableEntry(key, value);
    }

    public static <K, V> Builder<K, V> builder() {
        return new Builder<>();
    }

    public static <K, V> Builder<K, V> builderWithExpectedSize(int expectedSize) {
        CollectPreconditions.checkNonnegative(expectedSize, "expectedSize");
        return new Builder<>(expectedSize);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static void checkNoConflict(boolean safe, String conflictDescription, Object entry1, Object entry2) {
        if (!safe) {
            throw conflictException(conflictDescription, entry1, entry2);
        }
    }

    static IllegalArgumentException conflictException(String conflictDescription, Object entry1, Object entry2) {
        String valueOf = String.valueOf(entry1);
        String valueOf2 = String.valueOf(entry2);
        return new IllegalArgumentException(new StringBuilder(String.valueOf(conflictDescription).length() + 34 + String.valueOf(valueOf).length() + String.valueOf(valueOf2).length()).append("Multiple entries with same ").append(conflictDescription).append(": ").append(valueOf).append(" and ").append(valueOf2).toString());
    }

    @DoNotMock
    /* loaded from: classes.dex */
    public static class Builder<K, V> {
        Object[] alternatingKeysAndValues;
        DuplicateKey duplicateKey;
        boolean entriesUsed;
        int size;

        @CheckForNull
        Comparator<? super V> valueComparator;

        public Builder() {
            this(4);
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        public Builder(int initialCapacity) {
            this.alternatingKeysAndValues = new Object[initialCapacity * 2];
            this.size = 0;
            this.entriesUsed = false;
        }

        private void ensureCapacity(int minCapacity) {
            int i = minCapacity * 2;
            Object[] objArr = this.alternatingKeysAndValues;
            if (i > objArr.length) {
                this.alternatingKeysAndValues = Arrays.copyOf(objArr, ImmutableCollection.Builder.expandedCapacity(objArr.length, minCapacity * 2));
                this.entriesUsed = false;
            }
        }

        public Builder<K, V> put(K key, V value) {
            ensureCapacity(this.size + 1);
            CollectPreconditions.checkEntryNotNull(key, value);
            Object[] objArr = this.alternatingKeysAndValues;
            int i = this.size;
            objArr[i * 2] = key;
            objArr[(i * 2) + 1] = value;
            this.size = i + 1;
            return this;
        }

        public Builder<K, V> put(Map.Entry<? extends K, ? extends V> entry) {
            return put(entry.getKey(), entry.getValue());
        }

        public Builder<K, V> putAll(Map<? extends K, ? extends V> map) {
            return putAll(map.entrySet());
        }

        public Builder<K, V> putAll(Iterable<? extends Map.Entry<? extends K, ? extends V>> entries) {
            if (entries instanceof Collection) {
                ensureCapacity(this.size + ((Collection) entries).size());
            }
            for (Map.Entry<? extends K, ? extends V> entry : entries) {
                put(entry);
            }
            return this;
        }

        public Builder<K, V> orderEntriesByValue(Comparator<? super V> valueComparator) {
            Preconditions.checkState(this.valueComparator == null, "valueComparator was already set");
            this.valueComparator = (Comparator) Preconditions.checkNotNull(valueComparator, "valueComparator");
            return this;
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        public Builder<K, V> combine(Builder<K, V> other) {
            Preconditions.checkNotNull(other);
            ensureCapacity(this.size + other.size);
            System.arraycopy(other.alternatingKeysAndValues, 0, this.alternatingKeysAndValues, this.size * 2, other.size * 2);
            this.size += other.size;
            return this;
        }

        private ImmutableMap<K, V> build(boolean throwIfDuplicateKeys) {
            Object[] localAlternatingKeysAndValues;
            DuplicateKey duplicateKey;
            DuplicateKey duplicateKey2;
            if (throwIfDuplicateKeys && (duplicateKey2 = this.duplicateKey) != null) {
                throw duplicateKey2.exception();
            }
            int localSize = this.size;
            if (this.valueComparator == null) {
                localAlternatingKeysAndValues = this.alternatingKeysAndValues;
            } else {
                if (this.entriesUsed) {
                    this.alternatingKeysAndValues = Arrays.copyOf(this.alternatingKeysAndValues, this.size * 2);
                }
                localAlternatingKeysAndValues = this.alternatingKeysAndValues;
                if (!throwIfDuplicateKeys) {
                    localAlternatingKeysAndValues = lastEntryForEachKey(localAlternatingKeysAndValues, this.size);
                    if (localAlternatingKeysAndValues.length < this.alternatingKeysAndValues.length) {
                        localSize = localAlternatingKeysAndValues.length >>> 1;
                    }
                }
                sortEntries(localAlternatingKeysAndValues, localSize, this.valueComparator);
            }
            this.entriesUsed = true;
            ImmutableMap<K, V> map = RegularImmutableMap.create(localSize, localAlternatingKeysAndValues, this);
            if (throwIfDuplicateKeys && (duplicateKey = this.duplicateKey) != null) {
                throw duplicateKey.exception();
            }
            return map;
        }

        public ImmutableMap<K, V> build() {
            return buildOrThrow();
        }

        public ImmutableMap<K, V> buildOrThrow() {
            return build(true);
        }

        public ImmutableMap<K, V> buildKeepingLast() {
            return build(false);
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        public static <V> void sortEntries(Object[] alternatingKeysAndValues, int size, Comparator<? super V> valueComparator) {
            Map.Entry<Object, V>[] entries = new Map.Entry[size];
            for (int i = 0; i < size; i++) {
                Object key = Objects.requireNonNull(alternatingKeysAndValues[i * 2]);
                entries[i] = new AbstractMap.SimpleImmutableEntry<>(key, Objects.requireNonNull(alternatingKeysAndValues[(i * 2) + 1]));
            }
            Arrays.sort(entries, 0, size, Ordering.from(valueComparator).onResultOf(Maps.valueFunction()));
            for (int i2 = 0; i2 < size; i2++) {
                alternatingKeysAndValues[i2 * 2] = entries[i2].getKey();
                alternatingKeysAndValues[(i2 * 2) + 1] = entries[i2].getValue();
            }
        }

        private Object[] lastEntryForEachKey(Object[] localAlternatingKeysAndValues, int size) {
            Set<Object> seenKeys = new HashSet<>();
            BitSet dups = new BitSet();
            for (int i = size - 1; i >= 0; i--) {
                Object key = Objects.requireNonNull(localAlternatingKeysAndValues[i * 2]);
                if (!seenKeys.add(key)) {
                    dups.set(i);
                }
            }
            if (dups.isEmpty()) {
                return localAlternatingKeysAndValues;
            }
            Object[] newAlternatingKeysAndValues = new Object[(size - dups.cardinality()) * 2];
            int inI = 0;
            int outI = 0;
            while (inI < size * 2) {
                if (dups.get(inI >>> 1)) {
                    inI += 2;
                } else {
                    int outI2 = outI + 1;
                    int inI2 = inI + 1;
                    newAlternatingKeysAndValues[outI] = Objects.requireNonNull(localAlternatingKeysAndValues[inI]);
                    outI = outI2 + 1;
                    inI = inI2 + 1;
                    newAlternatingKeysAndValues[outI2] = Objects.requireNonNull(localAlternatingKeysAndValues[inI2]);
                }
            }
            return newAlternatingKeysAndValues;
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        /* loaded from: classes.dex */
        public static final class DuplicateKey {
            private final Object key;
            private final Object value1;
            private final Object value2;

            /* JADX INFO: Access modifiers changed from: package-private */
            public DuplicateKey(Object key, Object value1, Object value2) {
                this.key = key;
                this.value1 = value1;
                this.value2 = value2;
            }

            /* JADX INFO: Access modifiers changed from: package-private */
            public IllegalArgumentException exception() {
                String valueOf = String.valueOf(this.key);
                String valueOf2 = String.valueOf(this.value1);
                String valueOf3 = String.valueOf(this.key);
                String valueOf4 = String.valueOf(this.value2);
                return new IllegalArgumentException(new StringBuilder(String.valueOf(valueOf).length() + 39 + String.valueOf(valueOf2).length() + String.valueOf(valueOf3).length() + String.valueOf(valueOf4).length()).append("Multiple entries with same key: ").append(valueOf).append("=").append(valueOf2).append(" and ").append(valueOf3).append("=").append(valueOf4).toString());
            }
        }
    }

    public static <K, V> ImmutableMap<K, V> copyOf(Map<? extends K, ? extends V> map) {
        if ((map instanceof ImmutableMap) && !(map instanceof SortedMap)) {
            ImmutableMap<K, V> kvMap = (ImmutableMap) map;
            if (!kvMap.isPartialView()) {
                return kvMap;
            }
        }
        return copyOf(map.entrySet());
    }

    public static <K, V> ImmutableMap<K, V> copyOf(Iterable<? extends Map.Entry<? extends K, ? extends V>> entries) {
        int initialCapacity;
        if (entries instanceof Collection) {
            initialCapacity = ((Collection) entries).size();
        } else {
            initialCapacity = 4;
        }
        Builder<K, V> builder = new Builder<>(initialCapacity);
        builder.putAll(entries);
        return builder.build();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes.dex */
    public static abstract class IteratorBasedImmutableMap<K, V> extends ImmutableMap<K, V> {
        abstract UnmodifiableIterator<Map.Entry<K, V>> entryIterator();

        @Override // com.google.common.collect.ImmutableMap, java.util.Map
        public /* bridge */ /* synthetic */ Set entrySet() {
            return super.entrySet();
        }

        @Override // com.google.common.collect.ImmutableMap, java.util.Map
        public /* bridge */ /* synthetic */ Set keySet() {
            return super.keySet();
        }

        @Override // com.google.common.collect.ImmutableMap, java.util.Map
        public /* bridge */ /* synthetic */ Collection values() {
            return super.values();
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        @Override // com.google.common.collect.ImmutableMap
        public ImmutableSet<K> createKeySet() {
            return new ImmutableMapKeySet(this);
        }

        @Override // com.google.common.collect.ImmutableMap
        ImmutableSet<Map.Entry<K, V>> createEntrySet() {
            return new ImmutableMapEntrySet<K, V>() { // from class: com.google.common.collect.ImmutableMap.IteratorBasedImmutableMap.1EntrySetImpl
                @Override // com.google.common.collect.ImmutableMapEntrySet
                ImmutableMap<K, V> map() {
                    return IteratorBasedImmutableMap.this;
                }

                @Override // com.google.common.collect.ImmutableSet, com.google.common.collect.ImmutableCollection, java.util.AbstractCollection, java.util.Collection, java.lang.Iterable, java.util.Set, java.util.NavigableSet, com.google.common.collect.SortedIterable
                public UnmodifiableIterator<Map.Entry<K, V>> iterator() {
                    return IteratorBasedImmutableMap.this.entryIterator();
                }
            };
        }

        @Override // com.google.common.collect.ImmutableMap
        ImmutableCollection<V> createValues() {
            return new ImmutableMapValues(this);
        }
    }

    @Override // java.util.Map
    @CheckForNull
    @Deprecated
    public final V put(K k, V v) {
        throw new UnsupportedOperationException();
    }

    @Override // java.util.Map
    @CheckForNull
    @Deprecated
    public final V remove(@CheckForNull Object o) {
        throw new UnsupportedOperationException();
    }

    @Override // java.util.Map
    @Deprecated
    public final void putAll(Map<? extends K, ? extends V> map) {
        throw new UnsupportedOperationException();
    }

    @Override // java.util.Map
    @Deprecated
    public final void clear() {
        throw new UnsupportedOperationException();
    }

    @Override // java.util.Map
    public boolean isEmpty() {
        return size() == 0;
    }

    @Override // java.util.Map
    public boolean containsKey(@CheckForNull Object key) {
        return get(key) != null;
    }

    @Override // java.util.Map
    public boolean containsValue(@CheckForNull Object value) {
        return values().contains(value);
    }

    @Override // java.util.Map
    @CheckForNull
    public final V getOrDefault(@CheckForNull Object key, @CheckForNull V defaultValue) {
        V result = get(key);
        if (result != null) {
            return result;
        }
        return defaultValue;
    }

    @Override // java.util.Map
    public ImmutableSet<Map.Entry<K, V>> entrySet() {
        ImmutableSet<Map.Entry<K, V>> result = this.entrySet;
        if (result != null) {
            return result;
        }
        ImmutableSet<Map.Entry<K, V>> createEntrySet = createEntrySet();
        this.entrySet = createEntrySet;
        return createEntrySet;
    }

    @Override // java.util.Map
    public ImmutableSet<K> keySet() {
        ImmutableSet<K> result = this.keySet;
        if (result != null) {
            return result;
        }
        ImmutableSet<K> createKeySet = createKeySet();
        this.keySet = createKeySet;
        return createKeySet;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public UnmodifiableIterator<K> keyIterator() {
        final UnmodifiableIterator<Map.Entry<K, V>> entryIterator = entrySet().iterator();
        return new UnmodifiableIterator<K>(this) { // from class: com.google.common.collect.ImmutableMap.1
            @Override // java.util.Iterator
            public boolean hasNext() {
                return entryIterator.hasNext();
            }

            @Override // java.util.Iterator
            public K next() {
                return (K) ((Map.Entry) entryIterator.next()).getKey();
            }
        };
    }

    @Override // java.util.Map
    public ImmutableCollection<V> values() {
        ImmutableCollection<V> result = this.values;
        if (result != null) {
            return result;
        }
        ImmutableCollection<V> createValues = createValues();
        this.values = createValues;
        return createValues;
    }

    public ImmutableSetMultimap<K, V> asMultimap() {
        if (isEmpty()) {
            return ImmutableSetMultimap.of();
        }
        ImmutableSetMultimap<K, V> result = this.multimapView;
        if (result == null) {
            ImmutableSetMultimap<K, V> immutableSetMultimap = new ImmutableSetMultimap<>(new MapViewOfValuesAsSingletonSets(), size(), null);
            this.multimapView = immutableSetMultimap;
            return immutableSetMultimap;
        }
        return result;
    }

    /* loaded from: classes.dex */
    private final class MapViewOfValuesAsSingletonSets extends IteratorBasedImmutableMap<K, ImmutableSet<V>> {
        private MapViewOfValuesAsSingletonSets() {
        }

        @Override // java.util.Map
        public int size() {
            return ImmutableMap.this.size();
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        @Override // com.google.common.collect.ImmutableMap.IteratorBasedImmutableMap, com.google.common.collect.ImmutableMap
        public ImmutableSet<K> createKeySet() {
            return ImmutableMap.this.keySet();
        }

        @Override // com.google.common.collect.ImmutableMap, java.util.Map
        public boolean containsKey(@CheckForNull Object key) {
            return ImmutableMap.this.containsKey(key);
        }

        @Override // com.google.common.collect.ImmutableMap, java.util.Map
        @CheckForNull
        public ImmutableSet<V> get(@CheckForNull Object key) {
            Object obj = ImmutableMap.this.get(key);
            if (obj == null) {
                return null;
            }
            return ImmutableSet.of(obj);
        }

        @Override // com.google.common.collect.ImmutableMap
        boolean isPartialView() {
            return ImmutableMap.this.isPartialView();
        }

        @Override // com.google.common.collect.ImmutableMap, java.util.Map
        public int hashCode() {
            return ImmutableMap.this.hashCode();
        }

        @Override // com.google.common.collect.ImmutableMap
        boolean isHashCodeFast() {
            return ImmutableMap.this.isHashCodeFast();
        }

        @Override // com.google.common.collect.ImmutableMap.IteratorBasedImmutableMap
        UnmodifiableIterator<Map.Entry<K, ImmutableSet<V>>> entryIterator() {
            final Iterator<Map.Entry<K, V>> backingIterator = ImmutableMap.this.entrySet().iterator();
            return new UnmodifiableIterator<Map.Entry<K, ImmutableSet<V>>>(this) { // from class: com.google.common.collect.ImmutableMap.MapViewOfValuesAsSingletonSets.1
                @Override // java.util.Iterator
                public boolean hasNext() {
                    return backingIterator.hasNext();
                }

                @Override // java.util.Iterator
                public Map.Entry<K, ImmutableSet<V>> next() {
                    final Map.Entry<K, V> backingEntry = (Map.Entry) backingIterator.next();
                    return new AbstractMapEntry<K, ImmutableSet<V>>(this) { // from class: com.google.common.collect.ImmutableMap.MapViewOfValuesAsSingletonSets.1.1
                        @Override // com.google.common.collect.AbstractMapEntry, java.util.Map.Entry
                        public K getKey() {
                            return (K) backingEntry.getKey();
                        }

                        @Override // com.google.common.collect.AbstractMapEntry, java.util.Map.Entry
                        public ImmutableSet<V> getValue() {
                            return ImmutableSet.of(backingEntry.getValue());
                        }
                    };
                }
            };
        }
    }

    @Override // java.util.Map
    public boolean equals(@CheckForNull Object object) {
        return Maps.equalsImpl(this, object);
    }

    @Override // java.util.Map
    public int hashCode() {
        return Sets.hashCodeImpl(entrySet());
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean isHashCodeFast() {
        return false;
    }

    public String toString() {
        return Maps.toStringImpl(this);
    }

    /* loaded from: classes.dex */
    static class SerializedForm<K, V> implements Serializable {
        private static final boolean USE_LEGACY_SERIALIZATION = true;
        private static final long serialVersionUID = 0;
        private final Object keys;
        private final Object values;

        /* JADX INFO: Access modifiers changed from: package-private */
        public SerializedForm(ImmutableMap<K, V> map) {
            Object[] keys = new Object[map.size()];
            Object[] values = new Object[map.size()];
            int i = 0;
            UnmodifiableIterator<Map.Entry<K, V>> it = map.entrySet().iterator();
            while (it.hasNext()) {
                Map.Entry<K, V> next = it.next();
                keys[i] = next.getKey();
                values[i] = next.getValue();
                i++;
            }
            this.keys = keys;
            this.values = values;
        }

        final Object readResolve() {
            Object obj = this.keys;
            if (!(obj instanceof ImmutableSet)) {
                return legacyReadResolve();
            }
            ImmutableSet<K> keySet = (ImmutableSet) obj;
            ImmutableCollection<V> values = (ImmutableCollection) this.values;
            Builder<K, V> builder = makeBuilder(keySet.size());
            UnmodifiableIterator<K> keyIter = keySet.iterator();
            UnmodifiableIterator<V> valueIter = values.iterator();
            while (keyIter.hasNext()) {
                builder.put(keyIter.next(), valueIter.next());
            }
            return builder.buildOrThrow();
        }

        /* JADX WARN: Multi-variable type inference failed */
        final Object legacyReadResolve() {
            Object[] objArr = (Object[]) this.keys;
            Object[] objArr2 = (Object[]) this.values;
            Builder<K, V> makeBuilder = makeBuilder(objArr.length);
            for (int i = 0; i < objArr.length; i++) {
                makeBuilder.put(objArr[i], objArr2[i]);
            }
            return makeBuilder.buildOrThrow();
        }

        Builder<K, V> makeBuilder(int size) {
            return new Builder<>(size);
        }
    }

    Object writeReplace() {
        return new SerializedForm(this);
    }
}
