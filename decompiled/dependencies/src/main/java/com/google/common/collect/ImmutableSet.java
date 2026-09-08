package com.google.common.collect;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableCollection;
import com.google.errorprone.annotations.concurrent.LazyInit;
import java.io.Serializable;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.Objects;
import java.util.Set;
import java.util.SortedSet;
import javax.annotation.CheckForNull;

@ElementTypesAreNonnullByDefault
/* loaded from: classes.dex */
public abstract class ImmutableSet<E> extends ImmutableCollection<E> implements Set<E> {
    private static final int CUTOFF = 751619276;
    private static final double DESIRED_LOAD_FACTOR = 0.7d;
    static final int MAX_TABLE_SIZE = 1073741824;

    @CheckForNull
    @LazyInit
    private transient ImmutableList<E> asList;

    @Override // com.google.common.collect.ImmutableCollection, java.util.AbstractCollection, java.util.Collection, java.lang.Iterable, java.util.Set, java.util.NavigableSet, com.google.common.collect.SortedIterable
    public abstract UnmodifiableIterator<E> iterator();

    public static <E> ImmutableSet<E> of() {
        return RegularImmutableSet.EMPTY;
    }

    public static <E> ImmutableSet<E> of(E element) {
        return new SingletonImmutableSet(element);
    }

    public static <E> ImmutableSet<E> of(E e1, E e2) {
        return construct(2, e1, e2);
    }

    public static <E> ImmutableSet<E> of(E e1, E e2, E e3) {
        return construct(3, e1, e2, e3);
    }

    public static <E> ImmutableSet<E> of(E e1, E e2, E e3, E e4) {
        return construct(4, e1, e2, e3, e4);
    }

    public static <E> ImmutableSet<E> of(E e1, E e2, E e3, E e4, E e5) {
        return construct(5, e1, e2, e3, e4, e5);
    }

    @SafeVarargs
    public static <E> ImmutableSet<E> of(E e1, E e2, E e3, E e4, E e5, E e6, E... others) {
        Preconditions.checkArgument(others.length <= 2147483641, "the total number of elements must fit in an int");
        Object[] elements = new Object[others.length + 6];
        elements[0] = e1;
        elements[1] = e2;
        elements[2] = e3;
        elements[3] = e4;
        elements[4] = e5;
        elements[5] = e6;
        System.arraycopy(others, 0, elements, 6, others.length);
        return construct(elements.length, elements);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static <E> ImmutableSet<E> construct(int n, Object... elements) {
        switch (n) {
            case 0:
                return of();
            case 1:
                return of(Objects.requireNonNull(elements[0]));
            default:
                int tableSize = chooseTableSize(n);
                Object[] table = new Object[tableSize];
                int mask = tableSize - 1;
                int hashCode = 0;
                int uniques = 0;
                for (int i = 0; i < n; i++) {
                    Object element = ObjectArrays.checkElementNotNull(elements[i], i);
                    int hash = element.hashCode();
                    int j = Hashing.smear(hash);
                    while (true) {
                        int index = j & mask;
                        Object value = table[index];
                        if (value == null) {
                            elements[uniques] = element;
                            table[index] = element;
                            hashCode += hash;
                            uniques++;
                        } else {
                            if (value.equals(element)) {
                                break;
                            }
                            j++;
                        }
                    }
                }
                Arrays.fill(elements, uniques, n, (Object) null);
                if (uniques == 1) {
                    return new SingletonImmutableSet(Objects.requireNonNull(elements[0]));
                }
                if (chooseTableSize(uniques) < tableSize / 2) {
                    return construct(uniques, elements);
                }
                Object[] uniqueElements = shouldTrim(uniques, elements.length) ? Arrays.copyOf(elements, uniques) : elements;
                return new RegularImmutableSet(uniqueElements, hashCode, table, mask, uniques);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static boolean shouldTrim(int actualUnique, int expectedUnique) {
        return actualUnique < (expectedUnique >> 1) + (expectedUnique >> 2);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static int chooseTableSize(int setSize) {
        int setSize2 = Math.max(setSize, 2);
        if (setSize2 < CUTOFF) {
            int tableSize = Integer.highestOneBit(setSize2 - 1) << 1;
            while (tableSize * DESIRED_LOAD_FACTOR < setSize2) {
                tableSize <<= 1;
            }
            return tableSize;
        }
        Preconditions.checkArgument(setSize2 < 1073741824, "collection too large");
        return 1073741824;
    }

    public static <E> ImmutableSet<E> copyOf(Collection<? extends E> elements) {
        if ((elements instanceof ImmutableSet) && !(elements instanceof SortedSet)) {
            ImmutableSet<E> set = (ImmutableSet) elements;
            if (!set.isPartialView()) {
                return set;
            }
        }
        Object[] array = elements.toArray();
        return construct(array.length, array);
    }

    public static <E> ImmutableSet<E> copyOf(Iterable<? extends E> elements) {
        if (elements instanceof Collection) {
            return copyOf((Collection) elements);
        }
        return copyOf(elements.iterator());
    }

    public static <E> ImmutableSet<E> copyOf(Iterator<? extends E> elements) {
        if (!elements.hasNext()) {
            return of();
        }
        E first = elements.next();
        if (!elements.hasNext()) {
            return of((Object) first);
        }
        return new Builder().add((Builder) first).addAll((Iterator) elements).build();
    }

    public static <E> ImmutableSet<E> copyOf(E[] elements) {
        switch (elements.length) {
            case 0:
                return of();
            case 1:
                return of((Object) elements[0]);
            default:
                return construct(elements.length, (Object[]) elements.clone());
        }
    }

    boolean isHashCodeFast() {
        return false;
    }

    @Override // java.util.Collection, java.util.Set
    public boolean equals(@CheckForNull Object object) {
        if (object == this) {
            return true;
        }
        if ((object instanceof ImmutableSet) && isHashCodeFast() && ((ImmutableSet) object).isHashCodeFast() && hashCode() != object.hashCode()) {
            return false;
        }
        return Sets.equalsImpl(this, object);
    }

    @Override // java.util.Collection, java.util.Set
    public int hashCode() {
        return Sets.hashCodeImpl(this);
    }

    @Override // com.google.common.collect.ImmutableCollection
    public ImmutableList<E> asList() {
        ImmutableList<E> result = this.asList;
        if (result != null) {
            return result;
        }
        ImmutableList<E> createAsList = createAsList();
        this.asList = createAsList;
        return createAsList;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public ImmutableList<E> createAsList() {
        return ImmutableList.asImmutableList(toArray());
    }

    /* loaded from: classes.dex */
    private static class SerializedForm implements Serializable {
        private static final long serialVersionUID = 0;
        final Object[] elements;

        SerializedForm(Object[] elements) {
            this.elements = elements;
        }

        Object readResolve() {
            return ImmutableSet.copyOf(this.elements);
        }
    }

    @Override // com.google.common.collect.ImmutableCollection
    Object writeReplace() {
        return new SerializedForm(toArray());
    }

    public static <E> Builder<E> builder() {
        return new Builder<>();
    }

    public static <E> Builder<E> builderWithExpectedSize(int expectedSize) {
        CollectPreconditions.checkNonnegative(expectedSize, "expectedSize");
        return new Builder<>(expectedSize);
    }

    /* loaded from: classes.dex */
    public static class Builder<E> extends ImmutableCollection.ArrayBasedBuilder<E> {
        private int hashCode;

        @CheckForNull
        Object[] hashTable;

        /* JADX WARN: Multi-variable type inference failed */
        @Override // com.google.common.collect.ImmutableCollection.ArrayBasedBuilder, com.google.common.collect.ImmutableCollection.Builder
        public /* bridge */ /* synthetic */ ImmutableCollection.ArrayBasedBuilder add(Object obj) {
            return add((Builder<E>) obj);
        }

        /* JADX WARN: Multi-variable type inference failed */
        @Override // com.google.common.collect.ImmutableCollection.ArrayBasedBuilder, com.google.common.collect.ImmutableCollection.Builder
        public /* bridge */ /* synthetic */ ImmutableCollection.Builder add(Object obj) {
            return add((Builder<E>) obj);
        }

        public Builder() {
            super(4);
        }

        Builder(int capacity) {
            super(capacity);
            this.hashTable = new Object[ImmutableSet.chooseTableSize(capacity)];
        }

        @Override // com.google.common.collect.ImmutableCollection.ArrayBasedBuilder, com.google.common.collect.ImmutableCollection.Builder
        public Builder<E> add(E element) {
            Preconditions.checkNotNull(element);
            if (this.hashTable != null && ImmutableSet.chooseTableSize(this.size) <= this.hashTable.length) {
                addDeduping(element);
                return this;
            }
            this.hashTable = null;
            super.add((Builder<E>) element);
            return this;
        }

        @Override // com.google.common.collect.ImmutableCollection.ArrayBasedBuilder, com.google.common.collect.ImmutableCollection.Builder
        public Builder<E> add(E... elements) {
            if (this.hashTable != null) {
                for (E e : elements) {
                    add((Builder<E>) e);
                }
            } else {
                super.add((Object[]) elements);
            }
            return this;
        }

        private void addDeduping(E element) {
            Objects.requireNonNull(this.hashTable);
            int mask = this.hashTable.length - 1;
            int hash = element.hashCode();
            int i = Hashing.smear(hash);
            while (true) {
                int i2 = i & mask;
                Object[] objArr = this.hashTable;
                Object previous = objArr[i2];
                if (previous == null) {
                    objArr[i2] = element;
                    this.hashCode += hash;
                    super.add((Builder<E>) element);
                    return;
                } else if (!previous.equals(element)) {
                    i = i2 + 1;
                } else {
                    return;
                }
            }
        }

        @Override // com.google.common.collect.ImmutableCollection.ArrayBasedBuilder, com.google.common.collect.ImmutableCollection.Builder
        public Builder<E> addAll(Iterable<? extends E> elements) {
            Preconditions.checkNotNull(elements);
            if (this.hashTable != null) {
                for (E e : elements) {
                    add((Builder<E>) e);
                }
            } else {
                super.addAll((Iterable) elements);
            }
            return this;
        }

        @Override // com.google.common.collect.ImmutableCollection.Builder
        public Builder<E> addAll(Iterator<? extends E> elements) {
            Preconditions.checkNotNull(elements);
            while (elements.hasNext()) {
                add((Builder<E>) elements.next());
            }
            return this;
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        /* JADX WARN: Multi-variable type inference failed */
        public Builder<E> combine(Builder<E> other) {
            if (this.hashTable != null) {
                for (int i = 0; i < other.size; i++) {
                    add((Builder<E>) Objects.requireNonNull(other.contents[i]));
                }
            } else {
                addAll(other.contents, other.size);
            }
            return this;
        }

        @Override // com.google.common.collect.ImmutableCollection.Builder
        public ImmutableSet<E> build() {
            ImmutableSet<E> result;
            switch (this.size) {
                case 0:
                    return ImmutableSet.of();
                case 1:
                    return ImmutableSet.of(Objects.requireNonNull(this.contents[0]));
                default:
                    if (this.hashTable == null || ImmutableSet.chooseTableSize(this.size) != this.hashTable.length) {
                        result = ImmutableSet.construct(this.size, this.contents);
                        this.size = result.size();
                    } else {
                        Object[] uniqueElements = ImmutableSet.shouldTrim(this.size, this.contents.length) ? Arrays.copyOf(this.contents, this.size) : this.contents;
                        result = new RegularImmutableSet<>(uniqueElements, this.hashCode, this.hashTable, r5.length - 1, this.size);
                    }
                    this.forceCopy = true;
                    this.hashTable = null;
                    return result;
            }
        }
    }
}
