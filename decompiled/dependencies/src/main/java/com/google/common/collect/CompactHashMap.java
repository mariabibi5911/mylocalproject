package com.google.common.collect;

import com.google.common.base.Objects;
import com.google.common.base.Preconditions;
import com.google.common.primitives.Ints;
import java.io.IOException;
import java.io.InvalidObjectException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.AbstractCollection;
import java.util.AbstractMap;
import java.util.AbstractSet;
import java.util.Arrays;
import java.util.Collection;
import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;
import javax.annotation.CheckForNull;
import kotlinx.coroutines.internal.LockFreeTaskQueueCore;

/* JADX INFO: Access modifiers changed from: package-private */
@ElementTypesAreNonnullByDefault
/* loaded from: classes.dex */
public class CompactHashMap<K, V> extends AbstractMap<K, V> implements Serializable {
    static final double HASH_FLOODING_FPP = 0.001d;
    private static final int MAX_HASH_BUCKET_LENGTH = 9;
    private static final Object NOT_FOUND = new Object();

    @CheckForNull
    transient int[] entries;

    @CheckForNull
    private transient Set<Map.Entry<K, V>> entrySetView;

    @CheckForNull
    private transient Set<K> keySetView;

    @CheckForNull
    transient Object[] keys;
    private transient int metadata;
    private transient int size;

    @CheckForNull
    private transient Object table;

    @CheckForNull
    transient Object[] values;

    @CheckForNull
    private transient Collection<V> valuesView;

    static /* synthetic */ int access$1210(CompactHashMap x0) {
        int i = x0.size;
        x0.size = i - 1;
        return i;
    }

    public static <K, V> CompactHashMap<K, V> create() {
        return new CompactHashMap<>();
    }

    public static <K, V> CompactHashMap<K, V> createWithExpectedSize(int expectedSize) {
        return new CompactHashMap<>(expectedSize);
    }

    CompactHashMap() {
        init(3);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public CompactHashMap(int expectedSize) {
        init(expectedSize);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void init(int expectedSize) {
        Preconditions.checkArgument(expectedSize >= 0, "Expected size must be >= 0");
        this.metadata = Ints.constrainToRange(expectedSize, 1, LockFreeTaskQueueCore.MAX_CAPACITY_MASK);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean needsAllocArrays() {
        return this.table == null;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public int allocArrays() {
        Preconditions.checkState(needsAllocArrays(), "Arrays already allocated");
        int expectedSize = this.metadata;
        int buckets = CompactHashing.tableSize(expectedSize);
        this.table = CompactHashing.createTable(buckets);
        setHashTableMask(buckets - 1);
        this.entries = new int[expectedSize];
        this.keys = new Object[expectedSize];
        this.values = new Object[expectedSize];
        return expectedSize;
    }

    @CheckForNull
    Map<K, V> delegateOrNull() {
        Object obj = this.table;
        if (obj instanceof Map) {
            return (Map) obj;
        }
        return null;
    }

    Map<K, V> createHashFloodingResistantDelegate(int tableSize) {
        return new LinkedHashMap(tableSize, 1.0f);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public Map<K, V> convertToHashFloodingResistantImplementation() {
        Map<K, V> newDelegate = createHashFloodingResistantDelegate(hashTableMask() + 1);
        int i = firstEntryIndex();
        while (i >= 0) {
            newDelegate.put(key(i), value(i));
            i = getSuccessor(i);
        }
        this.table = newDelegate;
        this.entries = null;
        this.keys = null;
        this.values = null;
        incrementModCount();
        return newDelegate;
    }

    private void setHashTableMask(int mask) {
        int hashTableBits = 32 - Integer.numberOfLeadingZeros(mask);
        this.metadata = CompactHashing.maskCombine(this.metadata, hashTableBits, 31);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public int hashTableMask() {
        return (1 << (this.metadata & 31)) - 1;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void incrementModCount() {
        this.metadata += 32;
    }

    void accessEntry(int index) {
    }

    @Override // java.util.AbstractMap, java.util.Map
    @CheckForNull
    public V put(@ParametricNullness K k, @ParametricNullness V v) {
        int i;
        if (needsAllocArrays()) {
            allocArrays();
        }
        Map<K, V> delegateOrNull = delegateOrNull();
        if (delegateOrNull != null) {
            return delegateOrNull.put(k, v);
        }
        int[] requireEntries = requireEntries();
        Object[] requireKeys = requireKeys();
        Object[] requireValues = requireValues();
        int i2 = this.size;
        int i3 = i2 + 1;
        int smearedHash = Hashing.smearedHash(k);
        int hashTableMask = hashTableMask();
        int i4 = smearedHash & hashTableMask;
        int tableGet = CompactHashing.tableGet(requireTable(), i4);
        if (tableGet == 0) {
            if (i3 > hashTableMask) {
                i = resizeTable(hashTableMask, CompactHashing.newCapacity(hashTableMask), smearedHash, i2);
            } else {
                CompactHashing.tableSet(requireTable(), i4, i2 + 1);
                i = hashTableMask;
            }
        } else {
            int hashPrefix = CompactHashing.getHashPrefix(smearedHash, hashTableMask);
            int i5 = 0;
            while (true) {
                int i6 = tableGet - 1;
                int i7 = requireEntries[i6];
                int i8 = i4;
                if (CompactHashing.getHashPrefix(i7, hashTableMask) == hashPrefix && Objects.equal(k, requireKeys[i6])) {
                    V v2 = (V) requireValues[i6];
                    requireValues[i6] = v;
                    accessEntry(i6);
                    return v2;
                }
                int next = CompactHashing.getNext(i7, hashTableMask);
                i5++;
                if (next != 0) {
                    tableGet = next;
                    i4 = i8;
                } else {
                    if (i5 >= 9) {
                        return convertToHashFloodingResistantImplementation().put(k, v);
                    }
                    if (i3 > hashTableMask) {
                        i = resizeTable(hashTableMask, CompactHashing.newCapacity(hashTableMask), smearedHash, i2);
                    } else {
                        requireEntries[i6] = CompactHashing.maskCombine(i7, i2 + 1, hashTableMask);
                        i = hashTableMask;
                    }
                }
            }
        }
        resizeMeMaybe(i3);
        insertEntry(i2, k, v, smearedHash, i);
        this.size = i3;
        incrementModCount();
        return null;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void insertEntry(int entryIndex, @ParametricNullness K key, @ParametricNullness V value, int hash, int mask) {
        setEntry(entryIndex, CompactHashing.maskCombine(hash, 0, mask));
        setKey(entryIndex, key);
        setValue(entryIndex, value);
    }

    private void resizeMeMaybe(int newSize) {
        int newCapacity;
        int entriesSize = requireEntries().length;
        if (newSize > entriesSize && (newCapacity = Math.min(LockFreeTaskQueueCore.MAX_CAPACITY_MASK, (Math.max(1, entriesSize >>> 1) + entriesSize) | 1)) != entriesSize) {
            resizeEntries(newCapacity);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void resizeEntries(int newCapacity) {
        this.entries = Arrays.copyOf(requireEntries(), newCapacity);
        this.keys = Arrays.copyOf(requireKeys(), newCapacity);
        this.values = Arrays.copyOf(requireValues(), newCapacity);
    }

    private int resizeTable(int oldMask, int newCapacity, int targetHash, int targetEntryIndex) {
        Object newTable = CompactHashing.createTable(newCapacity);
        int newMask = newCapacity - 1;
        if (targetEntryIndex != 0) {
            CompactHashing.tableSet(newTable, targetHash & newMask, targetEntryIndex + 1);
        }
        Object oldTable = requireTable();
        int[] entries = requireEntries();
        for (int oldTableIndex = 0; oldTableIndex <= oldMask; oldTableIndex++) {
            int oldNext = CompactHashing.tableGet(oldTable, oldTableIndex);
            while (oldNext != 0) {
                int entryIndex = oldNext - 1;
                int oldEntry = entries[entryIndex];
                int hash = CompactHashing.getHashPrefix(oldEntry, oldMask) | oldTableIndex;
                int newTableIndex = hash & newMask;
                int newNext = CompactHashing.tableGet(newTable, newTableIndex);
                CompactHashing.tableSet(newTable, newTableIndex, oldNext);
                entries[entryIndex] = CompactHashing.maskCombine(hash, newNext, newMask);
                oldNext = CompactHashing.getNext(oldEntry, oldMask);
            }
        }
        this.table = newTable;
        setHashTableMask(newMask);
        return newMask;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public int indexOf(@CheckForNull Object key) {
        if (needsAllocArrays()) {
            return -1;
        }
        int hash = Hashing.smearedHash(key);
        int mask = hashTableMask();
        int next = CompactHashing.tableGet(requireTable(), hash & mask);
        if (next == 0) {
            return -1;
        }
        int hashPrefix = CompactHashing.getHashPrefix(hash, mask);
        do {
            int entryIndex = next - 1;
            int entry = entry(entryIndex);
            if (CompactHashing.getHashPrefix(entry, mask) == hashPrefix && Objects.equal(key, key(entryIndex))) {
                return entryIndex;
            }
            next = CompactHashing.getNext(entry, mask);
        } while (next != 0);
        return -1;
    }

    @Override // java.util.AbstractMap, java.util.Map
    public boolean containsKey(@CheckForNull Object key) {
        Map<K, V> delegate = delegateOrNull();
        return delegate != null ? delegate.containsKey(key) : indexOf(key) != -1;
    }

    @Override // java.util.AbstractMap, java.util.Map
    @CheckForNull
    public V get(@CheckForNull Object key) {
        Map<K, V> delegate = delegateOrNull();
        if (delegate != null) {
            return delegate.get(key);
        }
        int index = indexOf(key);
        if (index == -1) {
            return null;
        }
        accessEntry(index);
        return value(index);
    }

    @Override // java.util.AbstractMap, java.util.Map
    @CheckForNull
    public V remove(@CheckForNull Object obj) {
        Map<K, V> delegateOrNull = delegateOrNull();
        if (delegateOrNull != null) {
            return delegateOrNull.remove(obj);
        }
        V v = (V) removeHelper(obj);
        if (v == NOT_FOUND) {
            return null;
        }
        return v;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public Object removeHelper(@CheckForNull Object key) {
        if (needsAllocArrays()) {
            return NOT_FOUND;
        }
        int mask = hashTableMask();
        int index = CompactHashing.remove(key, null, mask, requireTable(), requireEntries(), requireKeys(), null);
        if (index == -1) {
            return NOT_FOUND;
        }
        Object oldValue = value(index);
        moveLastEntry(index, mask);
        this.size--;
        incrementModCount();
        return oldValue;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void moveLastEntry(int dstIndex, int mask) {
        int entryIndex;
        int entry;
        Object table = requireTable();
        int[] entries = requireEntries();
        Object[] keys = requireKeys();
        Object[] values = requireValues();
        int srcIndex = size() - 1;
        if (dstIndex < srcIndex) {
            Object key = keys[srcIndex];
            keys[dstIndex] = key;
            values[dstIndex] = values[srcIndex];
            keys[srcIndex] = null;
            values[srcIndex] = null;
            entries[dstIndex] = entries[srcIndex];
            entries[srcIndex] = 0;
            int tableIndex = Hashing.smearedHash(key) & mask;
            int next = CompactHashing.tableGet(table, tableIndex);
            int srcNext = srcIndex + 1;
            if (next == srcNext) {
                CompactHashing.tableSet(table, tableIndex, dstIndex + 1);
                return;
            }
            do {
                entryIndex = next - 1;
                entry = entries[entryIndex];
                next = CompactHashing.getNext(entry, mask);
            } while (next != srcNext);
            entries[entryIndex] = CompactHashing.maskCombine(entry, dstIndex + 1, mask);
            return;
        }
        keys[dstIndex] = null;
        values[dstIndex] = null;
        entries[dstIndex] = 0;
    }

    int firstEntryIndex() {
        return isEmpty() ? -1 : 0;
    }

    int getSuccessor(int entryIndex) {
        if (entryIndex + 1 < this.size) {
            return entryIndex + 1;
        }
        return -1;
    }

    int adjustAfterRemove(int indexBeforeRemove, int indexRemoved) {
        return indexBeforeRemove - 1;
    }

    /* loaded from: classes.dex */
    private abstract class Itr<T> implements Iterator<T> {
        int currentIndex;
        int expectedMetadata;
        int indexToRemove;

        @ParametricNullness
        abstract T getOutput(int i);

        private Itr() {
            this.expectedMetadata = CompactHashMap.this.metadata;
            this.currentIndex = CompactHashMap.this.firstEntryIndex();
            this.indexToRemove = -1;
        }

        @Override // java.util.Iterator
        public boolean hasNext() {
            return this.currentIndex >= 0;
        }

        @Override // java.util.Iterator
        @ParametricNullness
        public T next() {
            checkForConcurrentModification();
            if (!hasNext()) {
                throw new NoSuchElementException();
            }
            int i = this.currentIndex;
            this.indexToRemove = i;
            T result = getOutput(i);
            this.currentIndex = CompactHashMap.this.getSuccessor(this.currentIndex);
            return result;
        }

        @Override // java.util.Iterator
        public void remove() {
            checkForConcurrentModification();
            CollectPreconditions.checkRemove(this.indexToRemove >= 0);
            incrementExpectedModCount();
            CompactHashMap compactHashMap = CompactHashMap.this;
            compactHashMap.remove(compactHashMap.key(this.indexToRemove));
            this.currentIndex = CompactHashMap.this.adjustAfterRemove(this.currentIndex, this.indexToRemove);
            this.indexToRemove = -1;
        }

        void incrementExpectedModCount() {
            this.expectedMetadata += 32;
        }

        private void checkForConcurrentModification() {
            if (CompactHashMap.this.metadata != this.expectedMetadata) {
                throw new ConcurrentModificationException();
            }
        }
    }

    @Override // java.util.AbstractMap, java.util.Map
    public Set<K> keySet() {
        Set<K> set = this.keySetView;
        if (set != null) {
            return set;
        }
        Set<K> createKeySet = createKeySet();
        this.keySetView = createKeySet;
        return createKeySet;
    }

    Set<K> createKeySet() {
        return new KeySetView();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes.dex */
    public class KeySetView extends AbstractSet<K> {
        KeySetView() {
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
        public int size() {
            return CompactHashMap.this.size();
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
        public boolean contains(@CheckForNull Object o) {
            return CompactHashMap.this.containsKey(o);
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
        public boolean remove(@CheckForNull Object o) {
            Map<K, V> delegate = CompactHashMap.this.delegateOrNull();
            if (delegate != null) {
                return delegate.keySet().remove(o);
            }
            return CompactHashMap.this.removeHelper(o) != CompactHashMap.NOT_FOUND;
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.lang.Iterable, java.util.Set
        public Iterator<K> iterator() {
            return CompactHashMap.this.keySetIterator();
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
        public void clear() {
            CompactHashMap.this.clear();
        }
    }

    Iterator<K> keySetIterator() {
        Map<K, V> delegate = delegateOrNull();
        if (delegate != null) {
            return delegate.keySet().iterator();
        }
        return new CompactHashMap<K, V>.Itr<K>() { // from class: com.google.common.collect.CompactHashMap.1
            @Override // com.google.common.collect.CompactHashMap.Itr
            @ParametricNullness
            K getOutput(int i) {
                return (K) CompactHashMap.this.key(i);
            }
        };
    }

    @Override // java.util.AbstractMap, java.util.Map
    public Set<Map.Entry<K, V>> entrySet() {
        Set<Map.Entry<K, V>> set = this.entrySetView;
        if (set != null) {
            return set;
        }
        Set<Map.Entry<K, V>> createEntrySet = createEntrySet();
        this.entrySetView = createEntrySet;
        return createEntrySet;
    }

    Set<Map.Entry<K, V>> createEntrySet() {
        return new EntrySetView();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes.dex */
    public class EntrySetView extends AbstractSet<Map.Entry<K, V>> {
        EntrySetView() {
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
        public int size() {
            return CompactHashMap.this.size();
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
        public void clear() {
            CompactHashMap.this.clear();
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.lang.Iterable, java.util.Set
        public Iterator<Map.Entry<K, V>> iterator() {
            return CompactHashMap.this.entrySetIterator();
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
        public boolean contains(@CheckForNull Object o) {
            Map<K, V> delegate = CompactHashMap.this.delegateOrNull();
            if (delegate != null) {
                return delegate.entrySet().contains(o);
            }
            if (!(o instanceof Map.Entry)) {
                return false;
            }
            Map.Entry<?, ?> entry = (Map.Entry) o;
            int index = CompactHashMap.this.indexOf(entry.getKey());
            return index != -1 && Objects.equal(CompactHashMap.this.value(index), entry.getValue());
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
        public boolean remove(@CheckForNull Object o) {
            Map<K, V> delegate = CompactHashMap.this.delegateOrNull();
            if (delegate != null) {
                return delegate.entrySet().remove(o);
            }
            if (!(o instanceof Map.Entry)) {
                return false;
            }
            Map.Entry<?, ?> entry = (Map.Entry) o;
            if (CompactHashMap.this.needsAllocArrays()) {
                return false;
            }
            int mask = CompactHashMap.this.hashTableMask();
            int index = CompactHashing.remove(entry.getKey(), entry.getValue(), mask, CompactHashMap.this.requireTable(), CompactHashMap.this.requireEntries(), CompactHashMap.this.requireKeys(), CompactHashMap.this.requireValues());
            if (index == -1) {
                return false;
            }
            CompactHashMap.this.moveLastEntry(index, mask);
            CompactHashMap.access$1210(CompactHashMap.this);
            CompactHashMap.this.incrementModCount();
            return true;
        }
    }

    Iterator<Map.Entry<K, V>> entrySetIterator() {
        Map<K, V> delegate = delegateOrNull();
        if (delegate != null) {
            return delegate.entrySet().iterator();
        }
        return new CompactHashMap<K, V>.Itr<Map.Entry<K, V>>() { // from class: com.google.common.collect.CompactHashMap.2
            /* JADX INFO: Access modifiers changed from: package-private */
            @Override // com.google.common.collect.CompactHashMap.Itr
            public Map.Entry<K, V> getOutput(int entry) {
                return new MapEntry(entry);
            }
        };
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes.dex */
    public final class MapEntry extends AbstractMapEntry<K, V> {

        @ParametricNullness
        private final K key;
        private int lastKnownIndex;

        MapEntry(int i) {
            this.key = (K) CompactHashMap.this.key(i);
            this.lastKnownIndex = i;
        }

        @Override // com.google.common.collect.AbstractMapEntry, java.util.Map.Entry
        @ParametricNullness
        public K getKey() {
            return this.key;
        }

        private void updateLastKnownIndex() {
            int i = this.lastKnownIndex;
            if (i == -1 || i >= CompactHashMap.this.size() || !Objects.equal(this.key, CompactHashMap.this.key(this.lastKnownIndex))) {
                this.lastKnownIndex = CompactHashMap.this.indexOf(this.key);
            }
        }

        @Override // com.google.common.collect.AbstractMapEntry, java.util.Map.Entry
        @ParametricNullness
        public V getValue() {
            Map<K, V> delegateOrNull = CompactHashMap.this.delegateOrNull();
            if (delegateOrNull != null) {
                return (V) NullnessCasts.uncheckedCastNullableTToT(delegateOrNull.get(this.key));
            }
            updateLastKnownIndex();
            int i = this.lastKnownIndex;
            return i == -1 ? (V) NullnessCasts.unsafeNull() : (V) CompactHashMap.this.value(i);
        }

        @Override // com.google.common.collect.AbstractMapEntry, java.util.Map.Entry
        @ParametricNullness
        public V setValue(@ParametricNullness V v) {
            Map<K, V> delegateOrNull = CompactHashMap.this.delegateOrNull();
            if (delegateOrNull != null) {
                return (V) NullnessCasts.uncheckedCastNullableTToT(delegateOrNull.put(this.key, v));
            }
            updateLastKnownIndex();
            int i = this.lastKnownIndex;
            if (i != -1) {
                V v2 = (V) CompactHashMap.this.value(i);
                CompactHashMap.this.setValue(this.lastKnownIndex, v);
                return v2;
            }
            CompactHashMap.this.put(this.key, v);
            return (V) NullnessCasts.unsafeNull();
        }
    }

    @Override // java.util.AbstractMap, java.util.Map
    public int size() {
        Map<K, V> delegate = delegateOrNull();
        return delegate != null ? delegate.size() : this.size;
    }

    @Override // java.util.AbstractMap, java.util.Map
    public boolean isEmpty() {
        return size() == 0;
    }

    @Override // java.util.AbstractMap, java.util.Map
    public boolean containsValue(@CheckForNull Object value) {
        Map<K, V> delegate = delegateOrNull();
        if (delegate != null) {
            return delegate.containsValue(value);
        }
        for (int i = 0; i < this.size; i++) {
            if (Objects.equal(value, value(i))) {
                return true;
            }
        }
        return false;
    }

    @Override // java.util.AbstractMap, java.util.Map
    public Collection<V> values() {
        Collection<V> collection = this.valuesView;
        if (collection != null) {
            return collection;
        }
        Collection<V> createValues = createValues();
        this.valuesView = createValues;
        return createValues;
    }

    Collection<V> createValues() {
        return new ValuesView();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes.dex */
    public class ValuesView extends AbstractCollection<V> {
        ValuesView() {
        }

        @Override // java.util.AbstractCollection, java.util.Collection
        public int size() {
            return CompactHashMap.this.size();
        }

        @Override // java.util.AbstractCollection, java.util.Collection
        public void clear() {
            CompactHashMap.this.clear();
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.lang.Iterable
        public Iterator<V> iterator() {
            return CompactHashMap.this.valuesIterator();
        }
    }

    Iterator<V> valuesIterator() {
        Map<K, V> delegate = delegateOrNull();
        if (delegate != null) {
            return delegate.values().iterator();
        }
        return new CompactHashMap<K, V>.Itr<V>() { // from class: com.google.common.collect.CompactHashMap.3
            @Override // com.google.common.collect.CompactHashMap.Itr
            @ParametricNullness
            V getOutput(int i) {
                return (V) CompactHashMap.this.value(i);
            }
        };
    }

    public void trimToSize() {
        if (needsAllocArrays()) {
            return;
        }
        Map<K, V> delegate = delegateOrNull();
        if (delegate != null) {
            Map<K, V> newDelegate = createHashFloodingResistantDelegate(size());
            newDelegate.putAll(delegate);
            this.table = newDelegate;
            return;
        }
        int size = this.size;
        if (size < requireEntries().length) {
            resizeEntries(size);
        }
        int minimumTableSize = CompactHashing.tableSize(size);
        int mask = hashTableMask();
        if (minimumTableSize < mask) {
            resizeTable(mask, minimumTableSize, 0, 0);
        }
    }

    @Override // java.util.AbstractMap, java.util.Map
    public void clear() {
        if (needsAllocArrays()) {
            return;
        }
        incrementModCount();
        Map<K, V> delegate = delegateOrNull();
        if (delegate == null) {
            Arrays.fill(requireKeys(), 0, this.size, (Object) null);
            Arrays.fill(requireValues(), 0, this.size, (Object) null);
            CompactHashing.tableClear(requireTable());
            Arrays.fill(requireEntries(), 0, this.size, 0);
            this.size = 0;
            return;
        }
        this.metadata = Ints.constrainToRange(size(), 3, LockFreeTaskQueueCore.MAX_CAPACITY_MASK);
        delegate.clear();
        this.table = null;
        this.size = 0;
    }

    private void writeObject(ObjectOutputStream stream) throws IOException {
        stream.defaultWriteObject();
        stream.writeInt(size());
        Iterator<Map.Entry<K, V>> entryIterator = entrySetIterator();
        while (entryIterator.hasNext()) {
            Map.Entry<K, V> e = entryIterator.next();
            stream.writeObject(e.getKey());
            stream.writeObject(e.getValue());
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    private void readObject(ObjectInputStream stream) throws IOException, ClassNotFoundException {
        stream.defaultReadObject();
        int elementCount = stream.readInt();
        if (elementCount < 0) {
            throw new InvalidObjectException(new StringBuilder(25).append("Invalid size: ").append(elementCount).toString());
        }
        init(elementCount);
        for (int i = 0; i < elementCount; i++) {
            put(stream.readObject(), stream.readObject());
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public Object requireTable() {
        return java.util.Objects.requireNonNull(this.table);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public int[] requireEntries() {
        return (int[]) java.util.Objects.requireNonNull(this.entries);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public Object[] requireKeys() {
        return (Object[]) java.util.Objects.requireNonNull(this.keys);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public Object[] requireValues() {
        return (Object[]) java.util.Objects.requireNonNull(this.values);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public K key(int i) {
        return (K) requireKeys()[i];
    }

    /* JADX INFO: Access modifiers changed from: private */
    public V value(int i) {
        return (V) requireValues()[i];
    }

    private int entry(int i) {
        return requireEntries()[i];
    }

    private void setKey(int i, K key) {
        requireKeys()[i] = key;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void setValue(int i, V value) {
        requireValues()[i] = value;
    }

    private void setEntry(int i, int value) {
        requireEntries()[i] = value;
    }
}
