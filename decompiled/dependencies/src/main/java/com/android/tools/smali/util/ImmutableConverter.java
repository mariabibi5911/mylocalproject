package com.android.tools.smali.util;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.ImmutableSortedSet;
import java.util.Comparator;
import java.util.Iterator;
import java.util.SortedSet;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/* loaded from: classes.dex */
public abstract class ImmutableConverter<ImmutableItem, Item> {
    protected abstract boolean isImmutable(@Nonnull Item item);

    @Nonnull
    protected abstract ImmutableItem makeImmutable(@Nonnull Item item);

    @Nonnull
    public ImmutableList<ImmutableItem> toList(@Nullable Iterable<? extends Item> iterable) {
        if (iterable == null) {
            return ImmutableList.of();
        }
        boolean needsCopy = false;
        if (iterable instanceof ImmutableList) {
            Iterator<? extends Item> it = iterable.iterator();
            while (true) {
                if (!it.hasNext()) {
                    break;
                }
                Item element = it.next();
                if (!isImmutable(element)) {
                    needsCopy = true;
                    break;
                }
            }
        } else {
            needsCopy = true;
        }
        if (!needsCopy) {
            return (ImmutableList) iterable;
        }
        final Iterator<? extends Item> iter = iterable.iterator();
        return ImmutableList.copyOf(new Iterator<ImmutableItem>() { // from class: com.android.tools.smali.util.ImmutableConverter.1
            @Override // java.util.Iterator
            public boolean hasNext() {
                return iter.hasNext();
            }

            /* JADX WARN: Multi-variable type inference failed */
            @Override // java.util.Iterator
            public ImmutableItem next() {
                return (ImmutableItem) ImmutableConverter.this.makeImmutable(iter.next());
            }

            @Override // java.util.Iterator
            public void remove() {
                iter.remove();
            }
        });
    }

    @Nonnull
    public ImmutableSet<ImmutableItem> toSet(@Nullable Iterable<? extends Item> iterable) {
        if (iterable == null) {
            return ImmutableSet.of();
        }
        boolean needsCopy = false;
        if (iterable instanceof ImmutableSet) {
            Iterator<? extends Item> it = iterable.iterator();
            while (true) {
                if (!it.hasNext()) {
                    break;
                }
                Item element = it.next();
                if (!isImmutable(element)) {
                    needsCopy = true;
                    break;
                }
            }
        } else {
            needsCopy = true;
        }
        if (!needsCopy) {
            return (ImmutableSet) iterable;
        }
        final Iterator<? extends Item> iter = iterable.iterator();
        return ImmutableSet.copyOf(new Iterator<ImmutableItem>() { // from class: com.android.tools.smali.util.ImmutableConverter.2
            @Override // java.util.Iterator
            public boolean hasNext() {
                return iter.hasNext();
            }

            /* JADX WARN: Multi-variable type inference failed */
            @Override // java.util.Iterator
            public ImmutableItem next() {
                return (ImmutableItem) ImmutableConverter.this.makeImmutable(iter.next());
            }

            @Override // java.util.Iterator
            public void remove() {
                iter.remove();
            }
        });
    }

    @Nonnull
    public ImmutableSortedSet<ImmutableItem> toSortedSet(@Nonnull Comparator<? super ImmutableItem> comparator, @Nullable Iterable<? extends Item> iterable) {
        if (iterable == null) {
            return ImmutableSortedSet.of();
        }
        boolean needsCopy = false;
        if ((iterable instanceof ImmutableSortedSet) && ((ImmutableSortedSet) iterable).comparator().equals(comparator)) {
            Iterator<? extends Item> it = iterable.iterator();
            while (true) {
                if (!it.hasNext()) {
                    break;
                }
                Item element = it.next();
                if (!isImmutable(element)) {
                    needsCopy = true;
                    break;
                }
            }
        } else {
            needsCopy = true;
        }
        if (!needsCopy) {
            return (ImmutableSortedSet) iterable;
        }
        final Iterator<? extends Item> iter = iterable.iterator();
        return ImmutableSortedSet.copyOf(comparator, new Iterator<ImmutableItem>() { // from class: com.android.tools.smali.util.ImmutableConverter.3
            @Override // java.util.Iterator
            public boolean hasNext() {
                return iter.hasNext();
            }

            /* JADX WARN: Multi-variable type inference failed */
            @Override // java.util.Iterator
            public ImmutableItem next() {
                return (ImmutableItem) ImmutableConverter.this.makeImmutable(iter.next());
            }

            @Override // java.util.Iterator
            public void remove() {
                iter.remove();
            }
        });
    }

    @Nonnull
    public SortedSet<ImmutableItem> toSortedSet(@Nonnull Comparator<? super ImmutableItem> comparator, @Nullable SortedSet<? extends Item> sortedSet) {
        if (sortedSet == null || sortedSet.size() == 0) {
            return ImmutableSortedSet.of();
        }
        Object[] objArr = new Object[sortedSet.size()];
        int index = 0;
        for (Item item : sortedSet) {
            objArr[index] = makeImmutable(item);
            index++;
        }
        return ArraySortedSet.of((Comparator) comparator, objArr);
    }
}
