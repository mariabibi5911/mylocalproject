package com.google.common.collect;

import java.util.Comparator;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.SortedSet;
import javax.annotation.CheckForNull;

@ElementTypesAreNonnullByDefault
/* loaded from: classes.dex */
public abstract class ForwardingSortedSet<E> extends ForwardingSet<E> implements SortedSet<E> {
    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.google.common.collect.ForwardingSet, com.google.common.collect.ForwardingCollection, com.google.common.collect.ForwardingObject
    public abstract SortedSet<E> delegate();

    @Override // java.util.SortedSet
    @CheckForNull
    public Comparator<? super E> comparator() {
        return delegate().comparator();
    }

    @Override // java.util.SortedSet
    @ParametricNullness
    public E first() {
        return delegate().first();
    }

    @Override // java.util.SortedSet
    public SortedSet<E> headSet(@ParametricNullness E toElement) {
        return delegate().headSet(toElement);
    }

    @Override // java.util.SortedSet
    @ParametricNullness
    public E last() {
        return delegate().last();
    }

    @Override // java.util.SortedSet
    public SortedSet<E> subSet(@ParametricNullness E fromElement, @ParametricNullness E toElement) {
        return delegate().subSet(fromElement, toElement);
    }

    @Override // java.util.SortedSet
    public SortedSet<E> tailSet(@ParametricNullness E fromElement) {
        return delegate().tailSet(fromElement);
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // com.google.common.collect.ForwardingCollection
    protected boolean standardContains(@CheckForNull Object obj) {
        try {
            Object ceiling = tailSet(obj).first();
            return ForwardingSortedMap.unsafeCompare(comparator(), ceiling, obj) == 0;
        } catch (ClassCastException | NullPointerException | NoSuchElementException e) {
            return false;
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // com.google.common.collect.ForwardingCollection
    protected boolean standardRemove(@CheckForNull Object obj) {
        try {
            Iterator<E> it = tailSet(obj).iterator();
            if (it.hasNext()) {
                Object ceiling = it.next();
                if (ForwardingSortedMap.unsafeCompare(comparator(), ceiling, obj) == 0) {
                    it.remove();
                    return true;
                }
            }
            return false;
        } catch (ClassCastException | NullPointerException e) {
            return false;
        }
    }

    protected SortedSet<E> standardSubSet(@ParametricNullness E fromElement, @ParametricNullness E toElement) {
        return tailSet(fromElement).headSet(toElement);
    }
}
