package com.android.tools.smali.dexlib2.dexbacked.util;

import java.util.AbstractSet;
import java.util.Iterator;
import java.util.NoSuchElementException;
import javax.annotation.Nonnull;

/* loaded from: classes.dex */
public abstract class FixedSizeSet<T> extends AbstractSet<T> {
    @Nonnull
    public abstract T readItem(int i);

    @Override // java.util.AbstractCollection, java.util.Collection, java.lang.Iterable, java.util.Set
    public Iterator<T> iterator() {
        return new Iterator<T>() { // from class: com.android.tools.smali.dexlib2.dexbacked.util.FixedSizeSet.1
            int index = 0;

            @Override // java.util.Iterator
            public boolean hasNext() {
                return this.index < FixedSizeSet.this.size();
            }

            @Override // java.util.Iterator
            public void remove() {
                throw new UnsupportedOperationException();
            }

            @Override // java.util.Iterator
            public T next() {
                if (!hasNext()) {
                    throw new NoSuchElementException();
                }
                FixedSizeSet fixedSizeSet = FixedSizeSet.this;
                int i = this.index;
                this.index = i + 1;
                return (T) fixedSizeSet.readItem(i);
            }
        };
    }
}
