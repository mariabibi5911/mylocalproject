package com.android.tools.smali.util;

import java.util.AbstractSequentialList;
import java.util.Iterator;
import java.util.ListIterator;
import java.util.NoSuchElementException;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/* loaded from: classes.dex */
public abstract class AbstractForwardSequentialList<T> extends AbstractSequentialList<T> {
    @Override // java.util.AbstractSequentialList, java.util.AbstractList, java.util.AbstractCollection, java.util.Collection, java.lang.Iterable, java.util.List
    @Nonnull
    public abstract Iterator<T> iterator();

    /* JADX INFO: Access modifiers changed from: private */
    @Nonnull
    public Iterator<T> iterator(int index) {
        if (index < 0) {
            throw new NoSuchElementException();
        }
        Iterator<T> it = iterator();
        for (int i = 0; i < index; i++) {
            it.next();
        }
        return it;
    }

    @Override // java.util.AbstractSequentialList, java.util.AbstractList, java.util.List
    @Nonnull
    public ListIterator<T> listIterator(int initialIndex) {
        try {
            Iterator<T> initialIterator = iterator(initialIndex);
            return new AbstractListIterator<T>(initialIndex, initialIterator) { // from class: com.android.tools.smali.util.AbstractForwardSequentialList.1

                @Nullable
                private Iterator<T> forwardIterator;
                private int index;
                final /* synthetic */ int val$initialIndex;
                final /* synthetic */ Iterator val$initialIterator;

                {
                    this.val$initialIndex = initialIndex;
                    this.val$initialIterator = initialIterator;
                    this.index = initialIndex - 1;
                    this.forwardIterator = initialIterator;
                }

                @Nonnull
                private Iterator<T> getForwardIterator() {
                    if (this.forwardIterator == null) {
                        try {
                            this.forwardIterator = AbstractForwardSequentialList.this.iterator(this.index + 1);
                        } catch (IndexOutOfBoundsException e) {
                            throw new NoSuchElementException();
                        }
                    }
                    return this.forwardIterator;
                }

                @Override // com.android.tools.smali.util.AbstractListIterator, java.util.ListIterator, java.util.Iterator
                public boolean hasNext() {
                    return getForwardIterator().hasNext();
                }

                @Override // com.android.tools.smali.util.AbstractListIterator, java.util.ListIterator
                public boolean hasPrevious() {
                    return this.index >= 0;
                }

                @Override // com.android.tools.smali.util.AbstractListIterator, java.util.ListIterator, java.util.Iterator
                public T next() {
                    T ret = getForwardIterator().next();
                    this.index++;
                    return ret;
                }

                @Override // com.android.tools.smali.util.AbstractListIterator, java.util.ListIterator
                public int nextIndex() {
                    return this.index + 1;
                }

                @Override // com.android.tools.smali.util.AbstractListIterator, java.util.ListIterator
                public T previous() {
                    this.forwardIterator = null;
                    try {
                        AbstractForwardSequentialList abstractForwardSequentialList = AbstractForwardSequentialList.this;
                        int i = this.index;
                        this.index = i - 1;
                        return (T) abstractForwardSequentialList.iterator(i).next();
                    } catch (IndexOutOfBoundsException e) {
                        throw new NoSuchElementException();
                    }
                }

                @Override // com.android.tools.smali.util.AbstractListIterator, java.util.ListIterator
                public int previousIndex() {
                    return this.index;
                }
            };
        } catch (NoSuchElementException e) {
            throw new IndexOutOfBoundsException();
        }
    }

    @Override // java.util.AbstractList, java.util.List
    @Nonnull
    public ListIterator<T> listIterator() {
        return listIterator(0);
    }
}
