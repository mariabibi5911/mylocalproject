package com.android.tools.smali.util;

import com.google.common.base.Predicate;
import com.google.common.collect.ImmutableSortedSet;
import com.google.common.collect.Ordering;
import com.google.common.primitives.Ints;
import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;
import java.util.SortedSet;
import javax.annotation.Nonnull;

/* loaded from: classes.dex */
public class CollectionUtils {
    public static <T> int listHashCode(@Nonnull Iterable<T> iterable) {
        int hashCode = 1;
        for (T item : iterable) {
            hashCode = (hashCode * 31) + item.hashCode();
        }
        return hashCode;
    }

    public static <T> int lastIndexOf(@Nonnull Iterable<T> iterable, @Nonnull Predicate<? super T> predicate) {
        int index = 0;
        int lastMatchingIndex = -1;
        for (T item : iterable) {
            if (predicate.apply(item)) {
                lastMatchingIndex = index;
            }
            index++;
        }
        return lastMatchingIndex;
    }

    public static <T extends Comparable<? super T>> int compareAsList(@Nonnull Collection<? extends T> list1, @Nonnull Collection<? extends T> list2) {
        int res = Ints.compare(list1.size(), list2.size());
        if (res != 0) {
            return res;
        }
        Iterator<? extends T> elements2 = list2.iterator();
        for (T element1 : list1) {
            int res2 = element1.compareTo(elements2.next());
            if (res2 != 0) {
                return res2;
            }
        }
        return 0;
    }

    public static <T> int compareAsIterable(@Nonnull Comparator<? super T> comparator, @Nonnull Iterable<? extends T> iterable, @Nonnull Iterable<? extends T> iterable2) {
        Iterator<? extends T> it = iterable2.iterator();
        for (T t : iterable) {
            if (!it.hasNext()) {
                return 1;
            }
            int compare = comparator.compare(t, it.next());
            if (compare != 0) {
                return compare;
            }
        }
        if (it.hasNext()) {
            return -1;
        }
        return 0;
    }

    public static <T extends Comparable<? super T>> int compareAsIterable(@Nonnull Iterable<? extends T> it1, @Nonnull Iterable<? extends T> it2) {
        Iterator<? extends T> elements2 = it2.iterator();
        for (T element1 : it1) {
            if (!elements2.hasNext()) {
                return 1;
            }
            T element2 = elements2.next();
            int res = element1.compareTo(element2);
            if (res != 0) {
                return res;
            }
        }
        if (elements2.hasNext()) {
            return -1;
        }
        return 0;
    }

    public static <T> int compareAsList(@Nonnull Comparator<? super T> comparator, @Nonnull Collection<? extends T> collection, @Nonnull Collection<? extends T> collection2) {
        int compare = Ints.compare(collection.size(), collection2.size());
        if (compare != 0) {
            return compare;
        }
        Iterator<? extends T> it = collection2.iterator();
        Iterator<? extends T> it2 = collection.iterator();
        while (it2.hasNext()) {
            int compare2 = comparator.compare(it2.next(), it.next());
            if (compare2 != 0) {
                return compare2;
            }
        }
        return 0;
    }

    @Nonnull
    public static <T> Comparator<Collection<? extends T>> listComparator(@Nonnull final Comparator<? super T> elementComparator) {
        return new Comparator<Collection<? extends T>>() { // from class: com.android.tools.smali.util.CollectionUtils.1
            @Override // java.util.Comparator
            public int compare(Collection<? extends T> list1, Collection<? extends T> list2) {
                return CollectionUtils.compareAsList(elementComparator, list1, list2);
            }
        };
    }

    public static <T> boolean isNaturalSortedSet(@Nonnull Iterable<? extends T> it) {
        if (!(it instanceof SortedSet)) {
            return false;
        }
        SortedSet<? extends T> sortedSet = (SortedSet) it;
        Comparator<?> comparator = sortedSet.comparator();
        return comparator == null || comparator.equals(Ordering.natural());
    }

    public static <T> boolean isSortedSet(@Nonnull Comparator<? extends T> elementComparator, @Nonnull Iterable<? extends T> it) {
        if (it instanceof SortedSet) {
            SortedSet<? extends T> sortedSet = (SortedSet) it;
            Comparator<?> comparator = sortedSet.comparator();
            if (comparator == null) {
                return elementComparator.equals(Ordering.natural());
            }
            return elementComparator.equals(comparator);
        }
        return false;
    }

    @Nonnull
    private static <T> SortedSet<? extends T> toNaturalSortedSet(@Nonnull Collection<? extends T> collection) {
        if (isNaturalSortedSet(collection)) {
            return (SortedSet) collection;
        }
        return ImmutableSortedSet.copyOf((Collection) collection);
    }

    @Nonnull
    private static <T> SortedSet<? extends T> toSortedSet(@Nonnull Comparator<? super T> elementComparator, @Nonnull Collection<? extends T> collection) {
        SortedSet<? extends T> sortedSet;
        Comparator<?> comparator;
        if ((collection instanceof SortedSet) && (comparator = (sortedSet = (SortedSet) collection).comparator()) != null && comparator.equals(elementComparator)) {
            return sortedSet;
        }
        return ImmutableSortedSet.copyOf((Comparator) elementComparator, (Collection) collection);
    }

    @Nonnull
    public static <T> Comparator<Collection<? extends T>> setComparator(@Nonnull final Comparator<? super T> elementComparator) {
        return new Comparator<Collection<? extends T>>() { // from class: com.android.tools.smali.util.CollectionUtils.2
            @Override // java.util.Comparator
            public int compare(Collection<? extends T> list1, Collection<? extends T> list2) {
                return CollectionUtils.compareAsSet(elementComparator, list1, list2);
            }
        };
    }

    public static <T extends Comparable<T>> int compareAsSet(@Nonnull Collection<? extends T> set1, @Nonnull Collection<? extends T> set2) {
        int res = Ints.compare(set1.size(), set2.size());
        if (res != 0) {
            return res;
        }
        toNaturalSortedSet(set1);
        toNaturalSortedSet(set2);
        Iterator<? extends T> elements2 = set2.iterator();
        for (T element1 : set1) {
            int res2 = element1.compareTo(elements2.next());
            if (res2 != 0) {
                return res2;
            }
        }
        return 0;
    }

    public static <T> int compareAsSet(@Nonnull Comparator<? super T> comparator, @Nonnull Collection<? extends T> collection, @Nonnull Collection<? extends T> collection2) {
        int compare = Ints.compare(collection.size(), collection2.size());
        if (compare != 0) {
            return compare;
        }
        SortedSet sortedSet = toSortedSet(comparator, collection);
        Iterator it = toSortedSet(comparator, collection2).iterator();
        Iterator it2 = sortedSet.iterator();
        while (it2.hasNext()) {
            int compare2 = comparator.compare((T) it2.next(), (T) it.next());
            if (compare2 != 0) {
                return compare2;
            }
        }
        return 0;
    }
}
