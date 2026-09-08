package com.google.common.collect;

import com.google.common.base.Objects;
import com.google.common.base.Preconditions;
import com.google.common.primitives.Ints;
import java.io.IOException;
import java.io.InvalidObjectException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.AbstractSet;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.NoSuchElementException;
import java.util.Set;
import javax.annotation.CheckForNull;
import kotlinx.coroutines.internal.LockFreeTaskQueueCore;

/* JADX INFO: Access modifiers changed from: package-private */
@ElementTypesAreNonnullByDefault
/* loaded from: classes.dex */
public class CompactHashSet<E> extends AbstractSet<E> implements Serializable {
    static final double HASH_FLOODING_FPP = 0.001d;
    private static final int MAX_HASH_BUCKET_LENGTH = 9;

    @CheckForNull
    transient Object[] elements;

    @CheckForNull
    private transient int[] entries;
    private transient int metadata;
    private transient int size;

    @CheckForNull
    private transient Object table;

    public static <E> CompactHashSet<E> create() {
        return new CompactHashSet<>();
    }

    public static <E> CompactHashSet<E> create(Collection<? extends E> collection) {
        CompactHashSet<E> set = createWithExpectedSize(collection.size());
        set.addAll(collection);
        return set;
    }

    @SafeVarargs
    public static <E> CompactHashSet<E> create(E... elements) {
        CompactHashSet<E> set = createWithExpectedSize(elements.length);
        Collections.addAll(set, elements);
        return set;
    }

    public static <E> CompactHashSet<E> createWithExpectedSize(int expectedSize) {
        return new CompactHashSet<>(expectedSize);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public CompactHashSet() {
        init(3);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public CompactHashSet(int expectedSize) {
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
        this.elements = new Object[expectedSize];
        return expectedSize;
    }

    @CheckForNull
    Set<E> delegateOrNull() {
        Object obj = this.table;
        if (obj instanceof Set) {
            return (Set) obj;
        }
        return null;
    }

    private Set<E> createHashFloodingResistantDelegate(int tableSize) {
        return new LinkedHashSet(tableSize, 1.0f);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public Set<E> convertToHashFloodingResistantImplementation() {
        Set<E> newDelegate = createHashFloodingResistantDelegate(hashTableMask() + 1);
        int i = firstEntryIndex();
        while (i >= 0) {
            newDelegate.add(element(i));
            i = getSuccessor(i);
        }
        this.table = newDelegate;
        this.entries = null;
        this.elements = null;
        incrementModCount();
        return newDelegate;
    }

    boolean isUsingHashFloodingResistance() {
        return delegateOrNull() != null;
    }

    private void setHashTableMask(int mask) {
        int hashTableBits = 32 - Integer.numberOfLeadingZeros(mask);
        this.metadata = CompactHashing.maskCombine(this.metadata, hashTableBits, 31);
    }

    private int hashTableMask() {
        return (1 << (this.metadata & 31)) - 1;
    }

    void incrementModCount() {
        this.metadata += 32;
    }

    @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
    public boolean add(@ParametricNullness E object) {
        int entryIndex;
        int entry;
        if (needsAllocArrays()) {
            allocArrays();
        }
        Set<E> delegate = delegateOrNull();
        if (delegate != null) {
            return delegate.add(object);
        }
        int[] entries = requireEntries();
        Object[] elements = requireElements();
        int newEntryIndex = this.size;
        int newSize = newEntryIndex + 1;
        int hash = Hashing.smearedHash(object);
        int mask = hashTableMask();
        int tableIndex = hash & mask;
        int next = CompactHashing.tableGet(requireTable(), tableIndex);
        if (next == 0) {
            if (newSize > mask) {
                mask = resizeTable(mask, CompactHashing.newCapacity(mask), hash, newEntryIndex);
            } else {
                CompactHashing.tableSet(requireTable(), tableIndex, newEntryIndex + 1);
            }
        } else {
            int hashPrefix = CompactHashing.getHashPrefix(hash, mask);
            int bucketLength = 0;
            do {
                entryIndex = next - 1;
                entry = entries[entryIndex];
                if (CompactHashing.getHashPrefix(entry, mask) == hashPrefix && Objects.equal(object, elements[entryIndex])) {
                    return false;
                }
                next = CompactHashing.getNext(entry, mask);
                bucketLength++;
            } while (next != 0);
            if (bucketLength >= 9) {
                return convertToHashFloodingResistantImplementation().add(object);
            }
            if (newSize > mask) {
                mask = resizeTable(mask, CompactHashing.newCapacity(mask), hash, newEntryIndex);
            } else {
                entries[entryIndex] = CompactHashing.maskCombine(entry, newEntryIndex + 1, mask);
            }
        }
        resizeMeMaybe(newSize);
        insertEntry(newEntryIndex, object, hash, mask);
        this.size = newSize;
        incrementModCount();
        return true;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void insertEntry(int entryIndex, @ParametricNullness E object, int hash, int mask) {
        setEntry(entryIndex, CompactHashing.maskCombine(hash, 0, mask));
        setElement(entryIndex, object);
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
        this.elements = Arrays.copyOf(requireElements(), newCapacity);
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

    @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
    public boolean contains(@CheckForNull Object object) {
        if (needsAllocArrays()) {
            return false;
        }
        Set<E> delegate = delegateOrNull();
        if (delegate != null) {
            return delegate.contains(object);
        }
        int hash = Hashing.smearedHash(object);
        int mask = hashTableMask();
        int next = CompactHashing.tableGet(requireTable(), hash & mask);
        if (next == 0) {
            return false;
        }
        int hashPrefix = CompactHashing.getHashPrefix(hash, mask);
        do {
            int entryIndex = next - 1;
            int entry = entry(entryIndex);
            if (CompactHashing.getHashPrefix(entry, mask) == hashPrefix && Objects.equal(object, element(entryIndex))) {
                return true;
            }
            next = CompactHashing.getNext(entry, mask);
        } while (next != 0);
        return false;
    }

    @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
    public boolean remove(@CheckForNull Object object) {
        if (needsAllocArrays()) {
            return false;
        }
        Set<E> delegate = delegateOrNull();
        if (delegate != null) {
            return delegate.remove(object);
        }
        int mask = hashTableMask();
        int index = CompactHashing.remove(object, null, mask, requireTable(), requireEntries(), requireElements(), null);
        if (index == -1) {
            return false;
        }
        moveLastEntry(index, mask);
        this.size--;
        incrementModCount();
        return true;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void moveLastEntry(int dstIndex, int mask) {
        int entryIndex;
        int entry;
        Object table = requireTable();
        int[] entries = requireEntries();
        Object[] elements = requireElements();
        int srcIndex = size() - 1;
        if (dstIndex < srcIndex) {
            Object object = elements[srcIndex];
            elements[dstIndex] = object;
            elements[srcIndex] = null;
            entries[dstIndex] = entries[srcIndex];
            entries[srcIndex] = 0;
            int tableIndex = Hashing.smearedHash(object) & mask;
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
        elements[dstIndex] = null;
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

    @Override // java.util.AbstractCollection, java.util.Collection, java.lang.Iterable, java.util.Set
    public Iterator<E> iterator() {
        Set<E> delegate = delegateOrNull();
        if (delegate != null) {
            return delegate.iterator();
        }
        return new Iterator<E>() { // from class: com.google.common.collect.CompactHashSet.1
            int currentIndex;
            int expectedMetadata;
            int indexToRemove = -1;

            {
                this.expectedMetadata = CompactHashSet.this.metadata;
                this.currentIndex = CompactHashSet.this.firstEntryIndex();
            }

            @Override // java.util.Iterator
            public boolean hasNext() {
                return this.currentIndex >= 0;
            }

            @Override // java.util.Iterator
            @ParametricNullness
            public E next() {
                checkForConcurrentModification();
                if (!hasNext()) {
                    throw new NoSuchElementException();
                }
                int i = this.currentIndex;
                this.indexToRemove = i;
                E e = (E) CompactHashSet.this.element(i);
                this.currentIndex = CompactHashSet.this.getSuccessor(this.currentIndex);
                return e;
            }

            @Override // java.util.Iterator
            public void remove() {
                checkForConcurrentModification();
                CollectPreconditions.checkRemove(this.indexToRemove >= 0);
                incrementExpectedModCount();
                CompactHashSet compactHashSet = CompactHashSet.this;
                compactHashSet.remove(compactHashSet.element(this.indexToRemove));
                this.currentIndex = CompactHashSet.this.adjustAfterRemove(this.currentIndex, this.indexToRemove);
                this.indexToRemove = -1;
            }

            void incrementExpectedModCount() {
                this.expectedMetadata += 32;
            }

            private void checkForConcurrentModification() {
                if (CompactHashSet.this.metadata != this.expectedMetadata) {
                    throw new ConcurrentModificationException();
                }
            }
        };
    }

    @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
    public int size() {
        Set<E> delegate = delegateOrNull();
        return delegate != null ? delegate.size() : this.size;
    }

    @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
    public boolean isEmpty() {
        return size() == 0;
    }

    @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
    public Object[] toArray() {
        if (needsAllocArrays()) {
            return new Object[0];
        }
        Set<E> delegate = delegateOrNull();
        return delegate != null ? delegate.toArray() : Arrays.copyOf(requireElements(), this.size);
    }

    @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
    public <T> T[] toArray(T[] tArr) {
        if (needsAllocArrays()) {
            if (tArr.length > 0) {
                tArr[0] = null;
            }
            return tArr;
        }
        Set<E> delegateOrNull = delegateOrNull();
        if (delegateOrNull != null) {
            return (T[]) delegateOrNull.toArray(tArr);
        }
        return (T[]) ObjectArrays.toArrayImpl(requireElements(), 0, this.size, tArr);
    }

    public void trimToSize() {
        if (needsAllocArrays()) {
            return;
        }
        Set<E> delegate = delegateOrNull();
        if (delegate != null) {
            Set<E> newDelegate = createHashFloodingResistantDelegate(size());
            newDelegate.addAll(delegate);
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

    @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
    public void clear() {
        if (needsAllocArrays()) {
            return;
        }
        incrementModCount();
        Set<E> delegate = delegateOrNull();
        if (delegate == null) {
            Arrays.fill(requireElements(), 0, this.size, (Object) null);
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
        Iterator<E> it = iterator();
        while (it.hasNext()) {
            E e = it.next();
            stream.writeObject(e);
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
            add(stream.readObject());
        }
    }

    private Object requireTable() {
        return java.util.Objects.requireNonNull(this.table);
    }

    private int[] requireEntries() {
        return (int[]) java.util.Objects.requireNonNull(this.entries);
    }

    private Object[] requireElements() {
        return (Object[]) java.util.Objects.requireNonNull(this.elements);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public E element(int i) {
        return (E) requireElements()[i];
    }

    private int entry(int i) {
        return requireEntries()[i];
    }

    private void setElement(int i, E value) {
        requireElements()[i] = value;
    }

    private void setEntry(int i, int value) {
        requireEntries()[i] = value;
    }
}
