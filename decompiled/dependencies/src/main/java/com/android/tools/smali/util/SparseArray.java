package com.android.tools.smali.util;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/* loaded from: classes.dex */
public class SparseArray<E> {
    private static final Object DELETED = new Object();
    private boolean mGarbage;
    private int[] mKeys;
    private int mSize;
    private Object[] mValues;

    public SparseArray() {
        this(10);
    }

    public SparseArray(int initialCapacity) {
        this.mGarbage = false;
        this.mKeys = new int[initialCapacity];
        this.mValues = new Object[initialCapacity];
        this.mSize = 0;
    }

    public E get(int key) {
        return get(key, null);
    }

    public E get(int i, E e) {
        E e2;
        int binarySearch = binarySearch(this.mKeys, 0, this.mSize, i);
        if (binarySearch < 0 || (e2 = (E) this.mValues[binarySearch]) == DELETED) {
            return e;
        }
        return e2;
    }

    public void delete(int key) {
        int i = binarySearch(this.mKeys, 0, this.mSize, key);
        if (i >= 0) {
            Object[] objArr = this.mValues;
            Object obj = objArr[i];
            Object obj2 = DELETED;
            if (obj != obj2) {
                objArr[i] = obj2;
                this.mGarbage = true;
            }
        }
    }

    public void remove(int key) {
        delete(key);
    }

    private void gc() {
        int n = this.mSize;
        int o = 0;
        int[] keys = this.mKeys;
        Object[] values = this.mValues;
        for (int i = 0; i < n; i++) {
            Object val = values[i];
            if (val != DELETED) {
                if (i != o) {
                    keys[o] = keys[i];
                    values[o] = val;
                }
                o++;
            }
        }
        this.mGarbage = false;
        this.mSize = o;
    }

    public void put(int key, E value) {
        int i = binarySearch(this.mKeys, 0, this.mSize, key);
        if (i >= 0) {
            this.mValues[i] = value;
            return;
        }
        int i2 = ~i;
        int i3 = this.mSize;
        if (i2 < i3) {
            Object[] objArr = this.mValues;
            if (objArr[i2] == DELETED) {
                this.mKeys[i2] = key;
                objArr[i2] = value;
                return;
            }
        }
        if (this.mGarbage && i3 >= this.mKeys.length) {
            gc();
            i2 = ~binarySearch(this.mKeys, 0, this.mSize, key);
        }
        int i4 = this.mSize;
        int[] iArr = this.mKeys;
        if (i4 >= iArr.length) {
            int n = Math.max(i4 + 1, iArr.length * 2);
            int[] nkeys = new int[n];
            Object[] nvalues = new Object[n];
            int[] iArr2 = this.mKeys;
            System.arraycopy(iArr2, 0, nkeys, 0, iArr2.length);
            Object[] objArr2 = this.mValues;
            System.arraycopy(objArr2, 0, nvalues, 0, objArr2.length);
            this.mKeys = nkeys;
            this.mValues = nvalues;
        }
        int n2 = this.mSize;
        if (n2 - i2 != 0) {
            int[] iArr3 = this.mKeys;
            System.arraycopy(iArr3, i2, iArr3, i2 + 1, n2 - i2);
            Object[] objArr3 = this.mValues;
            System.arraycopy(objArr3, i2, objArr3, i2 + 1, this.mSize - i2);
        }
        this.mKeys[i2] = key;
        this.mValues[i2] = value;
        this.mSize++;
    }

    public int size() {
        if (this.mGarbage) {
            gc();
        }
        return this.mSize;
    }

    public int keyAt(int index) {
        if (this.mGarbage) {
            gc();
        }
        return this.mKeys[index];
    }

    public E valueAt(int i) {
        if (this.mGarbage) {
            gc();
        }
        return (E) this.mValues[i];
    }

    public void setValueAt(int index, E value) {
        if (this.mGarbage) {
            gc();
        }
        this.mValues[index] = value;
    }

    public int indexOfKey(int key) {
        if (this.mGarbage) {
            gc();
        }
        return binarySearch(this.mKeys, 0, this.mSize, key);
    }

    public int indexOfValue(E value) {
        if (this.mGarbage) {
            gc();
        }
        for (int i = 0; i < this.mSize; i++) {
            if (this.mValues[i] == value) {
                return i;
            }
        }
        return -1;
    }

    public void clear() {
        int n = this.mSize;
        Object[] values = this.mValues;
        for (int i = 0; i < n; i++) {
            values[i] = null;
        }
        this.mSize = 0;
        this.mGarbage = false;
    }

    public void append(int key, E value) {
        int i = this.mSize;
        if (i != 0 && key <= this.mKeys[i - 1]) {
            put(key, value);
            return;
        }
        if (this.mGarbage && i >= this.mKeys.length) {
            gc();
        }
        int pos = this.mSize;
        int[] iArr = this.mKeys;
        if (pos >= iArr.length) {
            int n = Math.max(pos + 1, iArr.length * 2);
            int[] nkeys = new int[n];
            Object[] nvalues = new Object[n];
            int[] iArr2 = this.mKeys;
            System.arraycopy(iArr2, 0, nkeys, 0, iArr2.length);
            Object[] objArr = this.mValues;
            System.arraycopy(objArr, 0, nvalues, 0, objArr.length);
            this.mKeys = nkeys;
            this.mValues = nvalues;
        }
        this.mKeys[pos] = key;
        this.mValues[pos] = value;
        this.mSize = pos + 1;
    }

    public void ensureCapacity(int capacity) {
        if (this.mGarbage && this.mSize >= this.mKeys.length) {
            gc();
        }
        int[] iArr = this.mKeys;
        if (iArr.length < capacity) {
            int[] nkeys = new int[capacity];
            Object[] nvalues = new Object[capacity];
            System.arraycopy(iArr, 0, nkeys, 0, iArr.length);
            Object[] objArr = this.mValues;
            System.arraycopy(objArr, 0, nvalues, 0, objArr.length);
            this.mKeys = nkeys;
            this.mValues = nvalues;
        }
    }

    private static int binarySearch(int[] a, int start, int len, int key) {
        int high = start + len;
        int low = start - 1;
        while (high - low > 1) {
            int guess = (high + low) / 2;
            if (a[guess] < key) {
                low = guess;
            } else {
                high = guess;
            }
        }
        if (high == start + len) {
            return ~(start + len);
        }
        if (a[high] == key) {
            return high;
        }
        return ~high;
    }

    public List<E> getValues() {
        return Collections.unmodifiableList(Arrays.asList(this.mValues));
    }
}
