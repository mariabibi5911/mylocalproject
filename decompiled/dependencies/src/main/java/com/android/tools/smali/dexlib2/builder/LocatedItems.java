package com.android.tools.smali.dexlib2.builder;

import com.android.tools.smali.dexlib2.builder.ItemWithLocation;
import com.google.common.collect.ImmutableList;
import java.util.AbstractSet;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/* loaded from: classes.dex */
public abstract class LocatedItems<T extends ItemWithLocation> {

    @Nullable
    private List<T> items = null;

    protected abstract String getAddLocatedItemError();

    /* JADX INFO: Access modifiers changed from: private */
    @Nonnull
    public List<T> getItems() {
        List<T> list = this.items;
        if (list == null) {
            return ImmutableList.of();
        }
        return list;
    }

    public Set<T> getModifiableItems(final MethodLocation methodLocation) {
        return new AbstractSet<T>() { // from class: com.android.tools.smali.dexlib2.builder.LocatedItems.1
            @Override // java.util.AbstractCollection, java.util.Collection, java.lang.Iterable, java.util.Set
            @Nonnull
            public Iterator<T> iterator() {
                final Iterator it = LocatedItems.this.getItems().iterator();
                return (Iterator<T>) new Iterator<T>() { // from class: com.android.tools.smali.dexlib2.builder.LocatedItems.1.1

                    @Nullable
                    private T currentItem = null;

                    @Override // java.util.Iterator
                    public boolean hasNext() {
                        return it.hasNext();
                    }

                    @Override // java.util.Iterator
                    public T next() {
                        T t = (T) it.next();
                        this.currentItem = t;
                        return t;
                    }

                    @Override // java.util.Iterator
                    public void remove() {
                        T t = this.currentItem;
                        if (t != null) {
                            t.setLocation(null);
                        }
                        it.remove();
                    }
                };
            }

            @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
            public int size() {
                return LocatedItems.this.getItems().size();
            }

            @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
            public boolean add(@Nonnull T item) {
                if (item.isPlaced()) {
                    throw new IllegalArgumentException(LocatedItems.this.getAddLocatedItemError());
                }
                item.setLocation(methodLocation);
                LocatedItems.this.addItem(item);
                return true;
            }
        };
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void addItem(@Nonnull T item) {
        if (this.items == null) {
            this.items = new ArrayList(1);
        }
        this.items.add(item);
    }

    public void mergeItemsIntoNext(@Nonnull MethodLocation nextLocation, LocatedItems<T> otherLocatedItems) {
        List<T> list;
        if (otherLocatedItems != this && (list = this.items) != null) {
            for (T item : list) {
                item.setLocation(nextLocation);
            }
            List<T> mergedItems = this.items;
            mergedItems.addAll(otherLocatedItems.getItems());
            otherLocatedItems.items = mergedItems;
            this.items = null;
        }
    }
}
