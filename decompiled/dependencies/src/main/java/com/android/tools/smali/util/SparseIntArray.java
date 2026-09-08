package com.android.tools.smali.util;

/* loaded from: classes.dex */
public class SparseIntArray {
    private int[] mKeys;
    private int mSize;
    private int[] mValues;

    public SparseIntArray() {
        this(10);
    }

    public SparseIntArray(int initialCapacity) {
        this.mKeys = new int[initialCapacity];
        this.mValues = new int[initialCapacity];
        this.mSize = 0;
    }

    public int get(int key) {
        return get(key, 0);
    }

    public int get(int key, int valueIfKeyNotFound) {
        int i = binarySearch(this.mKeys, 0, this.mSize, key);
        if (i < 0) {
            return valueIfKeyNotFound;
        }
        return this.mValues[i];
    }

    public int getClosestSmaller(int key) {
        int i = binarySearch(this.mKeys, 0, this.mSize, key);
        if (i < 0) {
            int i2 = ~i;
            if (i2 > 0) {
                i2--;
            }
            return this.mValues[i2];
        }
        return this.mValues[i];
    }

    public void delete(int key) {
        int i = binarySearch(this.mKeys, 0, this.mSize, key);
        if (i >= 0) {
            removeAt(i);
        }
    }

    public void removeAt(int index) {
        int[] iArr = this.mKeys;
        System.arraycopy(iArr, index + 1, iArr, index, this.mSize - (index + 1));
        int[] iArr2 = this.mValues;
        System.arraycopy(iArr2, index + 1, iArr2, index, this.mSize - (index + 1));
        this.mSize--;
    }

    public void put(int key, int value) {
        int i = binarySearch(this.mKeys, 0, this.mSize, key);
        if (i >= 0) {
            this.mValues[i] = value;
            return;
        }
        int i2 = ~i;
        int i3 = this.mSize;
        int[] iArr = this.mKeys;
        if (i3 >= iArr.length) {
            int n = Math.max(i3 + 1, iArr.length * 2);
            int[] nkeys = new int[n];
            int[] nvalues = new int[n];
            int[] iArr2 = this.mKeys;
            System.arraycopy(iArr2, 0, nkeys, 0, iArr2.length);
            int[] iArr3 = this.mValues;
            System.arraycopy(iArr3, 0, nvalues, 0, iArr3.length);
            this.mKeys = nkeys;
            this.mValues = nvalues;
        }
        int n2 = this.mSize;
        if (n2 - i2 != 0) {
            int[] iArr4 = this.mKeys;
            System.arraycopy(iArr4, i2, iArr4, i2 + 1, n2 - i2);
            int[] iArr5 = this.mValues;
            System.arraycopy(iArr5, i2, iArr5, i2 + 1, this.mSize - i2);
        }
        this.mKeys[i2] = key;
        this.mValues[i2] = value;
        this.mSize++;
    }

    public int size() {
        return this.mSize;
    }

    public int keyAt(int index) {
        return this.mKeys[index];
    }

    public int valueAt(int index) {
        return this.mValues[index];
    }

    public int indexOfKey(int key) {
        return binarySearch(this.mKeys, 0, this.mSize, key);
    }

    public int indexOfValue(int value) {
        for (int i = 0; i < this.mSize; i++) {
            if (this.mValues[i] == value) {
                return i;
            }
        }
        return -1;
    }

    public void clear() {
        this.mSize = 0;
    }

    public void append(int key, int value) {
        int i = this.mSize;
        if (i != 0 && key <= this.mKeys[i - 1]) {
            put(key, value);
            return;
        }
        int pos = this.mSize;
        int[] iArr = this.mKeys;
        if (pos >= iArr.length) {
            int n = Math.max(pos + 1, iArr.length * 2);
            int[] nkeys = new int[n];
            int[] nvalues = new int[n];
            int[] iArr2 = this.mKeys;
            System.arraycopy(iArr2, 0, nkeys, 0, iArr2.length);
            int[] iArr3 = this.mValues;
            System.arraycopy(iArr3, 0, nvalues, 0, iArr3.length);
            this.mKeys = nkeys;
            this.mValues = nvalues;
        }
        this.mKeys[pos] = key;
        this.mValues[pos] = value;
        this.mSize = pos + 1;
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
}
