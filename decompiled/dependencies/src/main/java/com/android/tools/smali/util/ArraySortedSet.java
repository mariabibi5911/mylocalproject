package com.android.tools.smali.util;

import com.google.common.collect.Iterators;
import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.SortedSet;
import javax.annotation.Nonnull;

/* loaded from: classes.dex */
public class ArraySortedSet<T> implements SortedSet<T> {

    @Nonnull
    private final Object[] arr;

    @Nonnull
    private final Comparator<? super T> comparator;

    private ArraySortedSet(@Nonnull Comparator<? super T> comparator, @Nonnull T[] arr) {
        this.comparator = comparator;
        this.arr = arr;
    }

    public static <T> ArraySortedSet<T> of(@Nonnull Comparator<? super T> comparator, @Nonnull T[] arr) {
        return new ArraySortedSet<>(comparator, arr);
    }

    @Override // java.util.Set, java.util.Collection
    public int size() {
        return this.arr.length;
    }

    @Override // java.util.Set, java.util.Collection
    public boolean isEmpty() {
        return this.arr.length > 0;
    }

    @Override // java.util.Set, java.util.Collection
    public boolean contains(Object o) {
        return Arrays.binarySearch(this.arr, o, this.comparator) >= 0;
    }

    @Override // java.util.Set, java.util.Collection, java.lang.Iterable
    public Iterator<T> iterator() {
        return Iterators.forArray(this.arr);
    }

    @Override // java.util.Set, java.util.Collection
    public Object[] toArray() {
        return (Object[]) this.arr.clone();
    }

    @Override // java.util.Set, java.util.Collection
    public <T> T[] toArray(T[] tArr) {
        int length = tArr.length;
        Object[] objArr = this.arr;
        if (length <= objArr.length) {
            System.arraycopy(objArr, 0, tArr, 0, objArr.length);
            return tArr;
        }
        return (T[]) Arrays.copyOf(objArr, objArr.length);
    }

    @Override // java.util.Set, java.util.Collection
    public boolean add(T t) {
        throw new UnsupportedOperationException();
    }

    @Override // java.util.Set, java.util.Collection
    public boolean remove(Object o) {
        throw new UnsupportedOperationException();
    }

    @Override // java.util.Set, java.util.Collection
    public boolean containsAll(Collection<?> c) {
        for (Object o : c) {
            if (!contains(o)) {
                return false;
            }
        }
        return true;
    }

    @Override // java.util.Set, java.util.Collection
    public boolean addAll(Collection<? extends T> c) {
        throw new UnsupportedOperationException();
    }

    @Override // java.util.Set, java.util.Collection
    public boolean retainAll(Collection<?> c) {
        throw new UnsupportedOperationException();
    }

    @Override // java.util.Set, java.util.Collection
    public boolean removeAll(Collection<?> c) {
        throw new UnsupportedOperationException();
    }

    @Override // java.util.Set, java.util.Collection
    public void clear() {
        throw new UnsupportedOperationException();
    }

    @Override // java.util.SortedSet
    public Comparator<? super T> comparator() {
        return this.comparator;
    }

    @Override // java.util.SortedSet
    public SortedSet<T> subSet(T fromElement, T toElement) {
        throw new UnsupportedOperationException();
    }

    @Override // java.util.SortedSet
    public SortedSet<T> headSet(T toElement) {
        throw new UnsupportedOperationException();
    }

    @Override // java.util.SortedSet
    public SortedSet<T> tailSet(T fromElement) {
        throw new UnsupportedOperationException();
    }

    @Override // java.util.SortedSet
    public T first() {
        Object[] objArr = this.arr;
        if (objArr.length == 0) {
            throw new NoSuchElementException();
        }
        return (T) objArr[0];
    }

    @Override // java.util.SortedSet
    public T last() {
        Object[] objArr = this.arr;
        if (objArr.length == 0) {
            throw new NoSuchElementException();
        }
        return (T) objArr[objArr.length - 1];
    }

    @Override // java.util.Set, java.util.Collection
    public int hashCode() {
        int result = 0;
        for (Object o : this.arr) {
            result += o.hashCode();
        }
        return result;
    }

    @Override // java.util.Set, java.util.Collection
    public boolean equals(Object o) {
        if (o == null) {
            return false;
        }
        if (o instanceof SortedSet) {
            SortedSet other = (SortedSet) o;
            if (this.arr.length != other.size()) {
                return false;
            }
            return Iterators.elementsEqual(iterator(), other.iterator());
        }
        if (!(o instanceof Set)) {
            return false;
        }
        Set other2 = (Set) o;
        if (this.arr.length != other2.size()) {
            return false;
        }
        return containsAll(other2);
    }
}
