package com.google.common.collect;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableMap;
import java.util.AbstractMap;
import java.util.Arrays;
import java.util.Map;
import java.util.Objects;
import javax.annotation.CheckForNull;

/* JADX INFO: Access modifiers changed from: package-private */
@ElementTypesAreNonnullByDefault
/* loaded from: classes.dex */
public final class RegularImmutableMap<K, V> extends ImmutableMap<K, V> {
    private static final byte ABSENT = -1;
    private static final int BYTE_MASK = 255;
    private static final int BYTE_MAX_SIZE = 128;
    static final ImmutableMap<Object, Object> EMPTY = new RegularImmutableMap(null, new Object[0], 0);
    private static final int SHORT_MASK = 65535;
    private static final int SHORT_MAX_SIZE = 32768;
    private static final long serialVersionUID = 0;
    final transient Object[] alternatingKeysAndValues;

    @CheckForNull
    private final transient Object hashTable;
    private final transient int size;

    /* JADX INFO: Access modifiers changed from: package-private */
    public static <K, V> RegularImmutableMap<K, V> create(int n, Object[] alternatingKeysAndValues) {
        return create(n, alternatingKeysAndValues, null);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static <K, V> RegularImmutableMap<K, V> create(int n, Object[] alternatingKeysAndValues, ImmutableMap.Builder<K, V> builder) {
        Object hashTable;
        if (n == 0) {
            RegularImmutableMap<K, V> empty = (RegularImmutableMap) EMPTY;
            return empty;
        }
        if (n == 1) {
            CollectPreconditions.checkEntryNotNull(Objects.requireNonNull(alternatingKeysAndValues[0]), Objects.requireNonNull(alternatingKeysAndValues[1]));
            return new RegularImmutableMap<>(null, alternatingKeysAndValues, 1);
        }
        Preconditions.checkPositionIndex(n, alternatingKeysAndValues.length >> 1);
        int tableSize = ImmutableSet.chooseTableSize(n);
        Object hashTablePlus = createHashTable(alternatingKeysAndValues, n, tableSize, 0);
        if (hashTablePlus instanceof Object[]) {
            Object[] hashTableAndSizeAndDuplicate = (Object[]) hashTablePlus;
            ImmutableMap.Builder.DuplicateKey duplicateKey = (ImmutableMap.Builder.DuplicateKey) hashTableAndSizeAndDuplicate[2];
            if (builder == null) {
                throw duplicateKey.exception();
            }
            builder.duplicateKey = duplicateKey;
            hashTable = hashTableAndSizeAndDuplicate[0];
            n = ((Integer) hashTableAndSizeAndDuplicate[1]).intValue();
            alternatingKeysAndValues = Arrays.copyOf(alternatingKeysAndValues, n * 2);
        } else {
            hashTable = hashTablePlus;
        }
        return new RegularImmutableMap<>(hashTable, alternatingKeysAndValues, n);
    }

    @CheckForNull
    private static Object createHashTable(Object[] alternatingKeysAndValues, int n, int tableSize, int keyOffset) {
        if (n == 1) {
            CollectPreconditions.checkEntryNotNull(Objects.requireNonNull(alternatingKeysAndValues[keyOffset]), Objects.requireNonNull(alternatingKeysAndValues[keyOffset ^ 1]));
            return null;
        }
        int mask = tableSize - 1;
        ImmutableMap.Builder.DuplicateKey duplicateKey = null;
        int i = -1;
        if (tableSize <= 128) {
            byte[] hashTable = new byte[tableSize];
            Arrays.fill(hashTable, (byte) -1);
            int outI = 0;
            for (int i2 = 0; i2 < n; i2++) {
                int keyIndex = (i2 * 2) + keyOffset;
                int outKeyIndex = (outI * 2) + keyOffset;
                Object key = Objects.requireNonNull(alternatingKeysAndValues[keyIndex]);
                Object value = Objects.requireNonNull(alternatingKeysAndValues[keyIndex ^ 1]);
                CollectPreconditions.checkEntryNotNull(key, value);
                int h = Hashing.smear(key.hashCode());
                while (true) {
                    int h2 = h & mask;
                    int previousKeyIndex = hashTable[h2] & 255;
                    if (previousKeyIndex == 255) {
                        hashTable[h2] = (byte) outKeyIndex;
                        if (outI < i2) {
                            alternatingKeysAndValues[outKeyIndex] = key;
                            alternatingKeysAndValues[outKeyIndex ^ 1] = value;
                        }
                        outI++;
                    } else if (!key.equals(alternatingKeysAndValues[previousKeyIndex])) {
                        h = h2 + 1;
                    } else {
                        ImmutableMap.Builder.DuplicateKey duplicateKey2 = new ImmutableMap.Builder.DuplicateKey(key, value, Objects.requireNonNull(alternatingKeysAndValues[previousKeyIndex ^ 1]));
                        alternatingKeysAndValues[previousKeyIndex ^ 1] = value;
                        duplicateKey = duplicateKey2;
                        break;
                    }
                }
            }
            return outI == n ? hashTable : new Object[]{hashTable, Integer.valueOf(outI), duplicateKey};
        }
        if (tableSize <= 32768) {
            short[] hashTable2 = new short[tableSize];
            Arrays.fill(hashTable2, (short) -1);
            int outI2 = 0;
            for (int i3 = 0; i3 < n; i3++) {
                int keyIndex2 = (i3 * 2) + keyOffset;
                int outKeyIndex2 = (outI2 * 2) + keyOffset;
                Object key2 = Objects.requireNonNull(alternatingKeysAndValues[keyIndex2]);
                Object value2 = Objects.requireNonNull(alternatingKeysAndValues[keyIndex2 ^ 1]);
                CollectPreconditions.checkEntryNotNull(key2, value2);
                int h3 = Hashing.smear(key2.hashCode());
                while (true) {
                    int h4 = h3 & mask;
                    int previousKeyIndex2 = hashTable2[h4] & 65535;
                    if (previousKeyIndex2 == 65535) {
                        hashTable2[h4] = (short) outKeyIndex2;
                        if (outI2 < i3) {
                            alternatingKeysAndValues[outKeyIndex2] = key2;
                            alternatingKeysAndValues[outKeyIndex2 ^ 1] = value2;
                        }
                        outI2++;
                    } else if (!key2.equals(alternatingKeysAndValues[previousKeyIndex2])) {
                        h3 = h4 + 1;
                    } else {
                        duplicateKey = new ImmutableMap.Builder.DuplicateKey(key2, value2, Objects.requireNonNull(alternatingKeysAndValues[previousKeyIndex2 ^ 1]));
                        alternatingKeysAndValues[previousKeyIndex2 ^ 1] = value2;
                        break;
                    }
                }
            }
            return outI2 == n ? hashTable2 : new Object[]{hashTable2, Integer.valueOf(outI2), duplicateKey};
        }
        int[] hashTable3 = new int[tableSize];
        Arrays.fill(hashTable3, -1);
        int outI3 = 0;
        int i4 = 0;
        while (i4 < n) {
            int keyIndex3 = (i4 * 2) + keyOffset;
            int outKeyIndex3 = (outI3 * 2) + keyOffset;
            Object key3 = Objects.requireNonNull(alternatingKeysAndValues[keyIndex3]);
            Object value3 = Objects.requireNonNull(alternatingKeysAndValues[keyIndex3 ^ 1]);
            CollectPreconditions.checkEntryNotNull(key3, value3);
            int h5 = Hashing.smear(key3.hashCode());
            while (true) {
                int h6 = h5 & mask;
                int previousKeyIndex3 = hashTable3[h6];
                if (previousKeyIndex3 == i) {
                    hashTable3[h6] = outKeyIndex3;
                    if (outI3 < i4) {
                        alternatingKeysAndValues[outKeyIndex3] = key3;
                        alternatingKeysAndValues[outKeyIndex3 ^ 1] = value3;
                    }
                    outI3++;
                } else if (!key3.equals(alternatingKeysAndValues[previousKeyIndex3])) {
                    h5 = h6 + 1;
                    i = -1;
                } else {
                    duplicateKey = new ImmutableMap.Builder.DuplicateKey(key3, value3, Objects.requireNonNull(alternatingKeysAndValues[previousKeyIndex3 ^ 1]));
                    alternatingKeysAndValues[previousKeyIndex3 ^ 1] = value3;
                    break;
                }
            }
            i4++;
            i = -1;
        }
        return outI3 == n ? hashTable3 : new Object[]{hashTable3, Integer.valueOf(outI3), duplicateKey};
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @CheckForNull
    public static Object createHashTableOrThrow(Object[] alternatingKeysAndValues, int n, int tableSize, int keyOffset) {
        Object hashTablePlus = createHashTable(alternatingKeysAndValues, n, tableSize, keyOffset);
        if (hashTablePlus instanceof Object[]) {
            Object[] hashTableAndSizeAndDuplicate = (Object[]) hashTablePlus;
            ImmutableMap.Builder.DuplicateKey duplicateKey = (ImmutableMap.Builder.DuplicateKey) hashTableAndSizeAndDuplicate[2];
            throw duplicateKey.exception();
        }
        return hashTablePlus;
    }

    private RegularImmutableMap(@CheckForNull Object hashTable, Object[] alternatingKeysAndValues, int size) {
        this.hashTable = hashTable;
        this.alternatingKeysAndValues = alternatingKeysAndValues;
        this.size = size;
    }

    @Override // java.util.Map
    public int size() {
        return this.size;
    }

    @Override // com.google.common.collect.ImmutableMap, java.util.Map
    @CheckForNull
    public V get(@CheckForNull Object obj) {
        V v = (V) get(this.hashTable, this.alternatingKeysAndValues, this.size, 0, obj);
        if (v == null) {
            return null;
        }
        return v;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @CheckForNull
    public static Object get(@CheckForNull Object hashTableObject, Object[] alternatingKeysAndValues, int size, int keyOffset, @CheckForNull Object key) {
        if (key == null) {
            return null;
        }
        if (size == 1) {
            if (!Objects.requireNonNull(alternatingKeysAndValues[keyOffset]).equals(key)) {
                return null;
            }
            return Objects.requireNonNull(alternatingKeysAndValues[keyOffset ^ 1]);
        }
        if (hashTableObject == null) {
            return null;
        }
        if (hashTableObject instanceof byte[]) {
            byte[] hashTable = (byte[]) hashTableObject;
            int mask = hashTable.length - 1;
            int h = Hashing.smear(key.hashCode());
            while (true) {
                int h2 = h & mask;
                int keyIndex = hashTable[h2] & 255;
                if (keyIndex == 255) {
                    return null;
                }
                if (!key.equals(alternatingKeysAndValues[keyIndex])) {
                    h = h2 + 1;
                } else {
                    return alternatingKeysAndValues[keyIndex ^ 1];
                }
            }
        } else if (hashTableObject instanceof short[]) {
            short[] hashTable2 = (short[]) hashTableObject;
            int mask2 = hashTable2.length - 1;
            int h3 = Hashing.smear(key.hashCode());
            while (true) {
                int h4 = h3 & mask2;
                int keyIndex2 = hashTable2[h4] & 65535;
                if (keyIndex2 == 65535) {
                    return null;
                }
                if (!key.equals(alternatingKeysAndValues[keyIndex2])) {
                    h3 = h4 + 1;
                } else {
                    return alternatingKeysAndValues[keyIndex2 ^ 1];
                }
            }
        } else {
            int[] hashTable3 = (int[]) hashTableObject;
            int mask3 = hashTable3.length - 1;
            int h5 = Hashing.smear(key.hashCode());
            while (true) {
                int h6 = h5 & mask3;
                int keyIndex3 = hashTable3[h6];
                if (keyIndex3 == -1) {
                    return null;
                }
                if (!key.equals(alternatingKeysAndValues[keyIndex3])) {
                    h5 = h6 + 1;
                } else {
                    return alternatingKeysAndValues[keyIndex3 ^ 1];
                }
            }
        }
    }

    @Override // com.google.common.collect.ImmutableMap
    ImmutableSet<Map.Entry<K, V>> createEntrySet() {
        return new EntrySet(this, this.alternatingKeysAndValues, 0, this.size);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes.dex */
    public static class EntrySet<K, V> extends ImmutableSet<Map.Entry<K, V>> {
        private final transient Object[] alternatingKeysAndValues;
        private final transient int keyOffset;
        private final transient ImmutableMap<K, V> map;
        private final transient int size;

        /* JADX INFO: Access modifiers changed from: package-private */
        public EntrySet(ImmutableMap<K, V> map, Object[] alternatingKeysAndValues, int keyOffset, int size) {
            this.map = map;
            this.alternatingKeysAndValues = alternatingKeysAndValues;
            this.keyOffset = keyOffset;
            this.size = size;
        }

        @Override // com.google.common.collect.ImmutableSet, com.google.common.collect.ImmutableCollection, java.util.AbstractCollection, java.util.Collection, java.lang.Iterable, java.util.Set, java.util.NavigableSet, com.google.common.collect.SortedIterable
        public UnmodifiableIterator<Map.Entry<K, V>> iterator() {
            return asList().iterator();
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        @Override // com.google.common.collect.ImmutableCollection
        public int copyIntoArray(Object[] dst, int offset) {
            return asList().copyIntoArray(dst, offset);
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        @Override // com.google.common.collect.ImmutableSet
        public ImmutableList<Map.Entry<K, V>> createAsList() {
            return new ImmutableList<Map.Entry<K, V>>() { // from class: com.google.common.collect.RegularImmutableMap.EntrySet.1
                @Override // java.util.List
                public Map.Entry<K, V> get(int index) {
                    Preconditions.checkElementIndex(index, EntrySet.this.size);
                    return new AbstractMap.SimpleImmutableEntry(Objects.requireNonNull(EntrySet.this.alternatingKeysAndValues[(index * 2) + EntrySet.this.keyOffset]), Objects.requireNonNull(EntrySet.this.alternatingKeysAndValues[(index * 2) + (EntrySet.this.keyOffset ^ 1)]));
                }

                @Override // java.util.AbstractCollection, java.util.Collection, java.util.List
                public int size() {
                    return EntrySet.this.size;
                }

                @Override // com.google.common.collect.ImmutableCollection
                public boolean isPartialView() {
                    return true;
                }
            };
        }

        @Override // com.google.common.collect.ImmutableCollection, java.util.AbstractCollection, java.util.Collection, java.util.Set
        public boolean contains(@CheckForNull Object object) {
            if (!(object instanceof Map.Entry)) {
                return false;
            }
            Map.Entry<?, ?> entry = (Map.Entry) object;
            Object k = entry.getKey();
            Object v = entry.getValue();
            return v != null && v.equals(this.map.get(k));
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        @Override // com.google.common.collect.ImmutableCollection
        public boolean isPartialView() {
            return true;
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
        public int size() {
            return this.size;
        }
    }

    @Override // com.google.common.collect.ImmutableMap
    ImmutableSet<K> createKeySet() {
        ImmutableList<K> keyList = new KeysOrValuesAsList(this.alternatingKeysAndValues, 0, this.size);
        return new KeySet(this, keyList);
    }

    /* loaded from: classes.dex */
    static final class KeysOrValuesAsList extends ImmutableList<Object> {
        private final transient Object[] alternatingKeysAndValues;
        private final transient int offset;
        private final transient int size;

        /* JADX INFO: Access modifiers changed from: package-private */
        public KeysOrValuesAsList(Object[] alternatingKeysAndValues, int offset, int size) {
            this.alternatingKeysAndValues = alternatingKeysAndValues;
            this.offset = offset;
            this.size = size;
        }

        @Override // java.util.List
        public Object get(int index) {
            Preconditions.checkElementIndex(index, this.size);
            return Objects.requireNonNull(this.alternatingKeysAndValues[(index * 2) + this.offset]);
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        @Override // com.google.common.collect.ImmutableCollection
        public boolean isPartialView() {
            return true;
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.util.List
        public int size() {
            return this.size;
        }
    }

    /* loaded from: classes.dex */
    static final class KeySet<K> extends ImmutableSet<K> {
        private final transient ImmutableList<K> list;
        private final transient ImmutableMap<K, ?> map;

        /* JADX INFO: Access modifiers changed from: package-private */
        public KeySet(ImmutableMap<K, ?> map, ImmutableList<K> list) {
            this.map = map;
            this.list = list;
        }

        @Override // com.google.common.collect.ImmutableSet, com.google.common.collect.ImmutableCollection, java.util.AbstractCollection, java.util.Collection, java.lang.Iterable, java.util.Set, java.util.NavigableSet, com.google.common.collect.SortedIterable
        public UnmodifiableIterator<K> iterator() {
            return asList().iterator();
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        @Override // com.google.common.collect.ImmutableCollection
        public int copyIntoArray(Object[] dst, int offset) {
            return asList().copyIntoArray(dst, offset);
        }

        @Override // com.google.common.collect.ImmutableSet, com.google.common.collect.ImmutableCollection
        public ImmutableList<K> asList() {
            return this.list;
        }

        @Override // com.google.common.collect.ImmutableCollection, java.util.AbstractCollection, java.util.Collection, java.util.Set
        public boolean contains(@CheckForNull Object object) {
            return this.map.get(object) != null;
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        @Override // com.google.common.collect.ImmutableCollection
        public boolean isPartialView() {
            return true;
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
        public int size() {
            return this.map.size();
        }
    }

    @Override // com.google.common.collect.ImmutableMap
    ImmutableCollection<V> createValues() {
        return new KeysOrValuesAsList(this.alternatingKeysAndValues, 1, this.size);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.google.common.collect.ImmutableMap
    public boolean isPartialView() {
        return false;
    }
}
