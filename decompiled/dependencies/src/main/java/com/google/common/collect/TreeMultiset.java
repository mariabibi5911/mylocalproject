package com.google.common.collect;

import com.google.common.base.MoreObjects;
import com.google.common.base.Preconditions;
import com.google.common.collect.Multiset;
import com.google.common.collect.Multisets;
import com.google.common.collect.Serialization;
import com.google.common.primitives.Ints;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Comparator;
import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.NavigableSet;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Set;
import javax.annotation.CheckForNull;

@ElementTypesAreNonnullByDefault
/* loaded from: classes.dex */
public final class TreeMultiset<E> extends AbstractSortedMultiset<E> implements Serializable {
    private static final long serialVersionUID = 1;
    private final transient AvlNode<E> header;
    private final transient GeneralRange<E> range;
    private final transient Reference<AvlNode<E>> rootReference;

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
    public enum Aggregate {
        SIZE { // from class: com.google.common.collect.TreeMultiset.Aggregate.1
            @Override // com.google.common.collect.TreeMultiset.Aggregate
            int nodeAggregate(AvlNode<?> node) {
                return ((AvlNode) node).elemCount;
            }

            @Override // com.google.common.collect.TreeMultiset.Aggregate
            long treeAggregate(@CheckForNull AvlNode<?> root) {
                if (root == null) {
                    return 0L;
                }
                return ((AvlNode) root).totalCount;
            }
        },
        DISTINCT { // from class: com.google.common.collect.TreeMultiset.Aggregate.2
            @Override // com.google.common.collect.TreeMultiset.Aggregate
            int nodeAggregate(AvlNode<?> node) {
                return 1;
            }

            @Override // com.google.common.collect.TreeMultiset.Aggregate
            long treeAggregate(@CheckForNull AvlNode<?> root) {
                if (root == null) {
                    return 0L;
                }
                return ((AvlNode) root).distinctElements;
            }
        };

        abstract int nodeAggregate(AvlNode<?> avlNode);

        abstract long treeAggregate(@CheckForNull AvlNode<?> avlNode);
    }

    @Override // com.google.common.collect.AbstractSortedMultiset, com.google.common.collect.SortedMultiset, com.google.common.collect.SortedIterable
    public /* bridge */ /* synthetic */ Comparator comparator() {
        return super.comparator();
    }

    @Override // com.google.common.collect.AbstractMultiset, java.util.AbstractCollection, java.util.Collection, com.google.common.collect.Multiset
    public /* bridge */ /* synthetic */ boolean contains(@CheckForNull Object obj) {
        return super.contains(obj);
    }

    @Override // com.google.common.collect.AbstractSortedMultiset, com.google.common.collect.SortedMultiset
    public /* bridge */ /* synthetic */ SortedMultiset descendingMultiset() {
        return super.descendingMultiset();
    }

    @Override // com.google.common.collect.AbstractSortedMultiset, com.google.common.collect.AbstractMultiset, com.google.common.collect.Multiset
    public /* bridge */ /* synthetic */ NavigableSet elementSet() {
        return super.elementSet();
    }

    @Override // com.google.common.collect.AbstractMultiset, com.google.common.collect.Multiset
    public /* bridge */ /* synthetic */ Set entrySet() {
        return super.entrySet();
    }

    @Override // com.google.common.collect.AbstractSortedMultiset, com.google.common.collect.SortedMultiset
    @CheckForNull
    public /* bridge */ /* synthetic */ Multiset.Entry firstEntry() {
        return super.firstEntry();
    }

    @Override // com.google.common.collect.AbstractMultiset, java.util.AbstractCollection, java.util.Collection
    public /* bridge */ /* synthetic */ boolean isEmpty() {
        return super.isEmpty();
    }

    @Override // com.google.common.collect.AbstractSortedMultiset, com.google.common.collect.SortedMultiset
    @CheckForNull
    public /* bridge */ /* synthetic */ Multiset.Entry lastEntry() {
        return super.lastEntry();
    }

    @Override // com.google.common.collect.AbstractSortedMultiset, com.google.common.collect.SortedMultiset
    @CheckForNull
    public /* bridge */ /* synthetic */ Multiset.Entry pollFirstEntry() {
        return super.pollFirstEntry();
    }

    @Override // com.google.common.collect.AbstractSortedMultiset, com.google.common.collect.SortedMultiset
    @CheckForNull
    public /* bridge */ /* synthetic */ Multiset.Entry pollLastEntry() {
        return super.pollLastEntry();
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // com.google.common.collect.AbstractSortedMultiset, com.google.common.collect.SortedMultiset
    public /* bridge */ /* synthetic */ SortedMultiset subMultiset(@ParametricNullness Object obj, BoundType boundType, @ParametricNullness Object obj2, BoundType boundType2) {
        return super.subMultiset(obj, boundType, obj2, boundType2);
    }

    public static <E extends Comparable> TreeMultiset<E> create() {
        return new TreeMultiset<>(Ordering.natural());
    }

    public static <E> TreeMultiset<E> create(@CheckForNull Comparator<? super E> comparator) {
        if (comparator == null) {
            return new TreeMultiset<>(Ordering.natural());
        }
        return new TreeMultiset<>(comparator);
    }

    public static <E extends Comparable> TreeMultiset<E> create(Iterable<? extends E> elements) {
        TreeMultiset<E> multiset = create();
        Iterables.addAll(multiset, elements);
        return multiset;
    }

    TreeMultiset(Reference<AvlNode<E>> rootReference, GeneralRange<E> range, AvlNode<E> endLink) {
        super(range.comparator());
        this.rootReference = rootReference;
        this.range = range;
        this.header = endLink;
    }

    TreeMultiset(Comparator<? super E> comparator) {
        super(comparator);
        this.range = GeneralRange.all(comparator);
        AvlNode<E> avlNode = new AvlNode<>();
        this.header = avlNode;
        successor(avlNode, avlNode);
        this.rootReference = new Reference<>();
    }

    private long aggregateForEntries(Aggregate aggr) {
        AvlNode<E> root = this.rootReference.get();
        long total = aggr.treeAggregate(root);
        if (this.range.hasLowerBound()) {
            total -= aggregateBelowRange(aggr, root);
        }
        if (this.range.hasUpperBound()) {
            return total - aggregateAboveRange(aggr, root);
        }
        return total;
    }

    private long aggregateBelowRange(Aggregate aggr, @CheckForNull AvlNode<E> node) {
        if (node == null) {
            return 0L;
        }
        int cmp = comparator().compare(NullnessCasts.uncheckedCastNullableTToT(this.range.getLowerEndpoint()), node.getElement());
        if (cmp >= 0) {
            if (cmp == 0) {
                switch (AnonymousClass4.$SwitchMap$com$google$common$collect$BoundType[this.range.getLowerBoundType().ordinal()]) {
                    case 1:
                        return aggr.nodeAggregate(node) + aggr.treeAggregate(((AvlNode) node).left);
                    case 2:
                        return aggr.treeAggregate(((AvlNode) node).left);
                    default:
                        throw new AssertionError();
                }
            }
            return aggr.treeAggregate(((AvlNode) node).left) + aggr.nodeAggregate(node) + aggregateBelowRange(aggr, ((AvlNode) node).right);
        }
        return aggregateBelowRange(aggr, ((AvlNode) node).left);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: com.google.common.collect.TreeMultiset$4, reason: invalid class name */
    /* loaded from: classes.dex */
    public static /* synthetic */ class AnonymousClass4 {
        static final /* synthetic */ int[] $SwitchMap$com$google$common$collect$BoundType;

        static {
            int[] iArr = new int[BoundType.values().length];
            $SwitchMap$com$google$common$collect$BoundType = iArr;
            try {
                iArr[BoundType.OPEN.ordinal()] = 1;
            } catch (NoSuchFieldError e) {
            }
            try {
                $SwitchMap$com$google$common$collect$BoundType[BoundType.CLOSED.ordinal()] = 2;
            } catch (NoSuchFieldError e2) {
            }
        }
    }

    private long aggregateAboveRange(Aggregate aggr, @CheckForNull AvlNode<E> node) {
        if (node == null) {
            return 0L;
        }
        int cmp = comparator().compare(NullnessCasts.uncheckedCastNullableTToT(this.range.getUpperEndpoint()), node.getElement());
        if (cmp <= 0) {
            if (cmp == 0) {
                switch (AnonymousClass4.$SwitchMap$com$google$common$collect$BoundType[this.range.getUpperBoundType().ordinal()]) {
                    case 1:
                        return aggr.nodeAggregate(node) + aggr.treeAggregate(((AvlNode) node).right);
                    case 2:
                        return aggr.treeAggregate(((AvlNode) node).right);
                    default:
                        throw new AssertionError();
                }
            }
            return aggr.treeAggregate(((AvlNode) node).right) + aggr.nodeAggregate(node) + aggregateAboveRange(aggr, ((AvlNode) node).left);
        }
        return aggregateAboveRange(aggr, ((AvlNode) node).right);
    }

    @Override // java.util.AbstractCollection, java.util.Collection, com.google.common.collect.Multiset
    public int size() {
        return Ints.saturatedCast(aggregateForEntries(Aggregate.SIZE));
    }

    @Override // com.google.common.collect.AbstractMultiset
    int distinctElements() {
        return Ints.saturatedCast(aggregateForEntries(Aggregate.DISTINCT));
    }

    static int distinctElements(@CheckForNull AvlNode<?> node) {
        if (node == null) {
            return 0;
        }
        return ((AvlNode) node).distinctElements;
    }

    @Override // com.google.common.collect.Multiset
    public int count(@CheckForNull Object element) {
        try {
            AvlNode<E> root = this.rootReference.get();
            if (this.range.contains(element) && root != null) {
                return root.count(comparator(), element);
            }
            return 0;
        } catch (ClassCastException | NullPointerException e) {
            return 0;
        }
    }

    @Override // com.google.common.collect.AbstractMultiset, com.google.common.collect.Multiset
    public int add(@ParametricNullness E element, int occurrences) {
        CollectPreconditions.checkNonnegative(occurrences, "occurrences");
        if (occurrences == 0) {
            return count(element);
        }
        Preconditions.checkArgument(this.range.contains(element));
        AvlNode<E> root = this.rootReference.get();
        if (root == null) {
            comparator().compare(element, element);
            AvlNode<E> newRoot = new AvlNode<>(element, occurrences);
            AvlNode<E> avlNode = this.header;
            successor(avlNode, newRoot, avlNode);
            this.rootReference.checkAndSet(root, newRoot);
            return 0;
        }
        int[] result = new int[1];
        this.rootReference.checkAndSet(root, root.add(comparator(), element, occurrences, result));
        return result[0];
    }

    @Override // com.google.common.collect.AbstractMultiset, com.google.common.collect.Multiset
    public int remove(@CheckForNull Object element, int occurrences) {
        CollectPreconditions.checkNonnegative(occurrences, "occurrences");
        if (occurrences == 0) {
            return count(element);
        }
        AvlNode<E> root = this.rootReference.get();
        int[] result = new int[1];
        try {
            if (this.range.contains(element) && root != null) {
                AvlNode<E> newRoot = root.remove(comparator(), element, occurrences, result);
                this.rootReference.checkAndSet(root, newRoot);
                return result[0];
            }
            return 0;
        } catch (ClassCastException | NullPointerException e) {
            return 0;
        }
    }

    @Override // com.google.common.collect.AbstractMultiset, com.google.common.collect.Multiset
    public int setCount(@ParametricNullness E element, int count) {
        CollectPreconditions.checkNonnegative(count, "count");
        if (!this.range.contains(element)) {
            Preconditions.checkArgument(count == 0);
            return 0;
        }
        AvlNode<E> root = this.rootReference.get();
        if (root == null) {
            if (count > 0) {
                add(element, count);
            }
            return 0;
        }
        int[] result = new int[1];
        AvlNode<E> newRoot = root.setCount(comparator(), element, count, result);
        this.rootReference.checkAndSet(root, newRoot);
        return result[0];
    }

    @Override // com.google.common.collect.AbstractMultiset, com.google.common.collect.Multiset
    public boolean setCount(@ParametricNullness E element, int oldCount, int newCount) {
        CollectPreconditions.checkNonnegative(newCount, "newCount");
        CollectPreconditions.checkNonnegative(oldCount, "oldCount");
        Preconditions.checkArgument(this.range.contains(element));
        AvlNode<E> root = this.rootReference.get();
        if (root == null) {
            if (oldCount != 0) {
                return false;
            }
            if (newCount > 0) {
                add(element, newCount);
            }
            return true;
        }
        int[] result = new int[1];
        AvlNode<E> newRoot = root.setCount(comparator(), element, oldCount, newCount, result);
        this.rootReference.checkAndSet(root, newRoot);
        return result[0] == oldCount;
    }

    @Override // com.google.common.collect.AbstractMultiset, java.util.AbstractCollection, java.util.Collection
    public void clear() {
        if (this.range.hasLowerBound() || this.range.hasUpperBound()) {
            Iterators.clear(entryIterator());
            return;
        }
        AvlNode<E> current = this.header.succ();
        while (true) {
            AvlNode<E> avlNode = this.header;
            if (current == avlNode) {
                successor(avlNode, avlNode);
                this.rootReference.clear();
                return;
            }
            AvlNode<E> next = current.succ();
            ((AvlNode) current).elemCount = 0;
            ((AvlNode) current).left = null;
            ((AvlNode) current).right = null;
            ((AvlNode) current).pred = null;
            ((AvlNode) current).succ = null;
            current = next;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public Multiset.Entry<E> wrapEntry(final AvlNode<E> baseEntry) {
        return new Multisets.AbstractEntry<E>() { // from class: com.google.common.collect.TreeMultiset.1
            @Override // com.google.common.collect.Multiset.Entry
            @ParametricNullness
            public E getElement() {
                return (E) baseEntry.getElement();
            }

            @Override // com.google.common.collect.Multiset.Entry
            public int getCount() {
                int result = baseEntry.getCount();
                if (result == 0) {
                    return TreeMultiset.this.count(getElement());
                }
                return result;
            }
        };
    }

    /* JADX INFO: Access modifiers changed from: private */
    @CheckForNull
    public AvlNode<E> firstNode() {
        AvlNode<E> node;
        AvlNode<E> root = this.rootReference.get();
        if (root == null) {
            return null;
        }
        if (this.range.hasLowerBound()) {
            Object uncheckedCastNullableTToT = NullnessCasts.uncheckedCastNullableTToT(this.range.getLowerEndpoint());
            node = root.ceiling(comparator(), uncheckedCastNullableTToT);
            if (node == null) {
                return null;
            }
            if (this.range.getLowerBoundType() == BoundType.OPEN && comparator().compare(uncheckedCastNullableTToT, node.getElement()) == 0) {
                node = node.succ();
            }
        } else {
            node = this.header.succ();
        }
        if (node == this.header || !this.range.contains(node.getElement())) {
            return null;
        }
        return node;
    }

    /* JADX INFO: Access modifiers changed from: private */
    @CheckForNull
    public AvlNode<E> lastNode() {
        AvlNode<E> node;
        AvlNode<E> root = this.rootReference.get();
        if (root == null) {
            return null;
        }
        if (this.range.hasUpperBound()) {
            Object uncheckedCastNullableTToT = NullnessCasts.uncheckedCastNullableTToT(this.range.getUpperEndpoint());
            node = root.floor(comparator(), uncheckedCastNullableTToT);
            if (node == null) {
                return null;
            }
            if (this.range.getUpperBoundType() == BoundType.OPEN && comparator().compare(uncheckedCastNullableTToT, node.getElement()) == 0) {
                node = node.pred();
            }
        } else {
            node = this.header.pred();
        }
        if (node == this.header || !this.range.contains(node.getElement())) {
            return null;
        }
        return node;
    }

    @Override // com.google.common.collect.AbstractMultiset
    Iterator<E> elementIterator() {
        return Multisets.elementIterator(entryIterator());
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.google.common.collect.AbstractMultiset
    public Iterator<Multiset.Entry<E>> entryIterator() {
        return new Iterator<Multiset.Entry<E>>() { // from class: com.google.common.collect.TreeMultiset.2

            @CheckForNull
            AvlNode<E> current;

            @CheckForNull
            Multiset.Entry<E> prevEntry;

            {
                this.current = TreeMultiset.this.firstNode();
            }

            @Override // java.util.Iterator
            public boolean hasNext() {
                if (this.current == null) {
                    return false;
                }
                if (TreeMultiset.this.range.tooHigh(this.current.getElement())) {
                    this.current = null;
                    return false;
                }
                return true;
            }

            @Override // java.util.Iterator
            public Multiset.Entry<E> next() {
                if (hasNext()) {
                    Multiset.Entry<E> result = TreeMultiset.this.wrapEntry((AvlNode) Objects.requireNonNull(this.current));
                    this.prevEntry = result;
                    if (this.current.succ() == TreeMultiset.this.header) {
                        this.current = null;
                    } else {
                        this.current = this.current.succ();
                    }
                    return result;
                }
                throw new NoSuchElementException();
            }

            @Override // java.util.Iterator
            public void remove() {
                Preconditions.checkState(this.prevEntry != null, "no calls to next() since the last call to remove()");
                TreeMultiset.this.setCount(this.prevEntry.getElement(), 0);
                this.prevEntry = null;
            }
        };
    }

    @Override // com.google.common.collect.AbstractSortedMultiset
    Iterator<Multiset.Entry<E>> descendingEntryIterator() {
        return new Iterator<Multiset.Entry<E>>() { // from class: com.google.common.collect.TreeMultiset.3

            @CheckForNull
            AvlNode<E> current;

            @CheckForNull
            Multiset.Entry<E> prevEntry = null;

            {
                this.current = TreeMultiset.this.lastNode();
            }

            @Override // java.util.Iterator
            public boolean hasNext() {
                if (this.current == null) {
                    return false;
                }
                if (TreeMultiset.this.range.tooLow(this.current.getElement())) {
                    this.current = null;
                    return false;
                }
                return true;
            }

            @Override // java.util.Iterator
            public Multiset.Entry<E> next() {
                if (!hasNext()) {
                    throw new NoSuchElementException();
                }
                Objects.requireNonNull(this.current);
                Multiset.Entry<E> result = TreeMultiset.this.wrapEntry(this.current);
                this.prevEntry = result;
                if (this.current.pred() == TreeMultiset.this.header) {
                    this.current = null;
                } else {
                    this.current = this.current.pred();
                }
                return result;
            }

            @Override // java.util.Iterator
            public void remove() {
                Preconditions.checkState(this.prevEntry != null, "no calls to next() since the last call to remove()");
                TreeMultiset.this.setCount(this.prevEntry.getElement(), 0);
                this.prevEntry = null;
            }
        };
    }

    @Override // java.util.AbstractCollection, java.util.Collection, java.lang.Iterable, com.google.common.collect.Multiset
    public Iterator<E> iterator() {
        return Multisets.iteratorImpl(this);
    }

    @Override // com.google.common.collect.SortedMultiset
    public SortedMultiset<E> headMultiset(@ParametricNullness E upperBound, BoundType boundType) {
        return new TreeMultiset(this.rootReference, this.range.intersect(GeneralRange.upTo(comparator(), upperBound, boundType)), this.header);
    }

    @Override // com.google.common.collect.SortedMultiset
    public SortedMultiset<E> tailMultiset(@ParametricNullness E lowerBound, BoundType boundType) {
        return new TreeMultiset(this.rootReference, this.range.intersect(GeneralRange.downTo(comparator(), lowerBound, boundType)), this.header);
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
    public static final class Reference<T> {

        @CheckForNull
        private T value;

        private Reference() {
        }

        @CheckForNull
        public T get() {
            return this.value;
        }

        public void checkAndSet(@CheckForNull T expected, @CheckForNull T newValue) {
            if (this.value != expected) {
                throw new ConcurrentModificationException();
            }
            this.value = newValue;
        }

        void clear() {
            this.value = null;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
    public static final class AvlNode<E> {
        private int distinctElements;

        @CheckForNull
        private final E elem;
        private int elemCount;
        private int height;

        @CheckForNull
        private AvlNode<E> left;

        @CheckForNull
        private AvlNode<E> pred;

        @CheckForNull
        private AvlNode<E> right;

        @CheckForNull
        private AvlNode<E> succ;
        private long totalCount;

        AvlNode(@ParametricNullness E elem, int elemCount) {
            Preconditions.checkArgument(elemCount > 0);
            this.elem = elem;
            this.elemCount = elemCount;
            this.totalCount = elemCount;
            this.distinctElements = 1;
            this.height = 1;
            this.left = null;
            this.right = null;
        }

        AvlNode() {
            this.elem = null;
            this.elemCount = 1;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public AvlNode<E> pred() {
            return (AvlNode) Objects.requireNonNull(this.pred);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public AvlNode<E> succ() {
            return (AvlNode) Objects.requireNonNull(this.succ);
        }

        /* JADX WARN: Multi-variable type inference failed */
        int count(Comparator<? super E> comparator, @ParametricNullness E e) {
            int cmp = comparator.compare(e, getElement());
            if (cmp < 0) {
                AvlNode<E> avlNode = this.left;
                if (avlNode == null) {
                    return 0;
                }
                return avlNode.count(comparator, e);
            }
            if (cmp > 0) {
                AvlNode<E> avlNode2 = this.right;
                if (avlNode2 == null) {
                    return 0;
                }
                return avlNode2.count(comparator, e);
            }
            return this.elemCount;
        }

        private AvlNode<E> addRightChild(@ParametricNullness E e, int count) {
            AvlNode<E> avlNode = new AvlNode<>(e, count);
            this.right = avlNode;
            TreeMultiset.successor(this, avlNode, succ());
            this.height = Math.max(2, this.height);
            this.distinctElements++;
            this.totalCount += count;
            return this;
        }

        private AvlNode<E> addLeftChild(@ParametricNullness E e, int count) {
            this.left = new AvlNode<>(e, count);
            TreeMultiset.successor(pred(), this.left, this);
            this.height = Math.max(2, this.height);
            this.distinctElements++;
            this.totalCount += count;
            return this;
        }

        /* JADX WARN: Multi-variable type inference failed */
        AvlNode<E> add(Comparator<? super E> comparator, @ParametricNullness E e, int count, int[] result) {
            int cmp = comparator.compare(e, getElement());
            if (cmp < 0) {
                AvlNode<E> initLeft = this.left;
                if (initLeft == null) {
                    result[0] = 0;
                    return addLeftChild(e, count);
                }
                int initHeight = initLeft.height;
                AvlNode<E> add = initLeft.add(comparator, e, count, result);
                this.left = add;
                if (result[0] == 0) {
                    this.distinctElements++;
                }
                this.totalCount += count;
                return add.height == initHeight ? this : rebalance();
            }
            if (cmp > 0) {
                AvlNode<E> initRight = this.right;
                if (initRight == null) {
                    result[0] = 0;
                    return addRightChild(e, count);
                }
                int initHeight2 = initRight.height;
                AvlNode<E> add2 = initRight.add(comparator, e, count, result);
                this.right = add2;
                if (result[0] == 0) {
                    this.distinctElements++;
                }
                this.totalCount += count;
                return add2.height == initHeight2 ? this : rebalance();
            }
            int i = this.elemCount;
            result[0] = i;
            long resultCount = i + count;
            Preconditions.checkArgument(resultCount <= 2147483647L);
            this.elemCount += count;
            this.totalCount += count;
            return this;
        }

        /* JADX WARN: Multi-variable type inference failed */
        @CheckForNull
        AvlNode<E> remove(Comparator<? super E> comparator, @ParametricNullness E e, int count, int[] result) {
            int cmp = comparator.compare(e, getElement());
            if (cmp < 0) {
                AvlNode<E> initLeft = this.left;
                if (initLeft == null) {
                    result[0] = 0;
                    return this;
                }
                this.left = initLeft.remove(comparator, e, count, result);
                if (result[0] > 0) {
                    if (count >= result[0]) {
                        this.distinctElements--;
                        this.totalCount -= result[0];
                    } else {
                        this.totalCount -= count;
                    }
                }
                return result[0] == 0 ? this : rebalance();
            }
            if (cmp > 0) {
                AvlNode<E> initRight = this.right;
                if (initRight == null) {
                    result[0] = 0;
                    return this;
                }
                this.right = initRight.remove(comparator, e, count, result);
                if (result[0] > 0) {
                    if (count >= result[0]) {
                        this.distinctElements--;
                        this.totalCount -= result[0];
                    } else {
                        this.totalCount -= count;
                    }
                }
                return rebalance();
            }
            int i = this.elemCount;
            result[0] = i;
            if (count >= i) {
                return deleteMe();
            }
            this.elemCount = i - count;
            this.totalCount -= count;
            return this;
        }

        /* JADX WARN: Multi-variable type inference failed */
        @CheckForNull
        AvlNode<E> setCount(Comparator<? super E> comparator, @ParametricNullness E e, int count, int[] result) {
            int cmp = comparator.compare(e, getElement());
            if (cmp < 0) {
                AvlNode<E> initLeft = this.left;
                if (initLeft == null) {
                    result[0] = 0;
                    return count > 0 ? addLeftChild(e, count) : this;
                }
                this.left = initLeft.setCount(comparator, e, count, result);
                if (count == 0 && result[0] != 0) {
                    this.distinctElements--;
                } else if (count > 0 && result[0] == 0) {
                    this.distinctElements++;
                }
                this.totalCount += count - result[0];
                return rebalance();
            }
            if (cmp > 0) {
                AvlNode<E> initRight = this.right;
                if (initRight == null) {
                    result[0] = 0;
                    return count > 0 ? addRightChild(e, count) : this;
                }
                this.right = initRight.setCount(comparator, e, count, result);
                if (count == 0 && result[0] != 0) {
                    this.distinctElements--;
                } else if (count > 0 && result[0] == 0) {
                    this.distinctElements++;
                }
                this.totalCount += count - result[0];
                return rebalance();
            }
            result[0] = this.elemCount;
            if (count == 0) {
                return deleteMe();
            }
            this.totalCount += count - r2;
            this.elemCount = count;
            return this;
        }

        /* JADX WARN: Multi-variable type inference failed */
        @CheckForNull
        AvlNode<E> setCount(Comparator<? super E> comparator, @ParametricNullness E e, int expectedCount, int newCount, int[] result) {
            int cmp = comparator.compare(e, getElement());
            if (cmp < 0) {
                AvlNode<E> initLeft = this.left;
                if (initLeft == null) {
                    result[0] = 0;
                    if (expectedCount == 0 && newCount > 0) {
                        return addLeftChild(e, newCount);
                    }
                    return this;
                }
                this.left = initLeft.setCount(comparator, e, expectedCount, newCount, result);
                if (result[0] == expectedCount) {
                    if (newCount == 0 && result[0] != 0) {
                        this.distinctElements--;
                    } else if (newCount > 0 && result[0] == 0) {
                        this.distinctElements++;
                    }
                    this.totalCount += newCount - result[0];
                }
                return rebalance();
            }
            if (cmp > 0) {
                AvlNode<E> initRight = this.right;
                if (initRight == null) {
                    result[0] = 0;
                    if (expectedCount == 0 && newCount > 0) {
                        return addRightChild(e, newCount);
                    }
                    return this;
                }
                this.right = initRight.setCount(comparator, e, expectedCount, newCount, result);
                if (result[0] == expectedCount) {
                    if (newCount == 0 && result[0] != 0) {
                        this.distinctElements--;
                    } else if (newCount > 0 && result[0] == 0) {
                        this.distinctElements++;
                    }
                    this.totalCount += newCount - result[0];
                }
                return rebalance();
            }
            int i = this.elemCount;
            result[0] = i;
            if (expectedCount == i) {
                if (newCount == 0) {
                    return deleteMe();
                }
                this.totalCount += newCount - i;
                this.elemCount = newCount;
            }
            return this;
        }

        @CheckForNull
        private AvlNode<E> deleteMe() {
            int oldElemCount = this.elemCount;
            this.elemCount = 0;
            TreeMultiset.successor(pred(), succ());
            AvlNode<E> avlNode = this.left;
            if (avlNode == null) {
                return this.right;
            }
            AvlNode<E> avlNode2 = this.right;
            if (avlNode2 == null) {
                return avlNode;
            }
            if (avlNode.height >= avlNode2.height) {
                AvlNode<E> newTop = pred();
                newTop.left = this.left.removeMax(newTop);
                newTop.right = this.right;
                newTop.distinctElements = this.distinctElements - 1;
                newTop.totalCount = this.totalCount - oldElemCount;
                return newTop.rebalance();
            }
            AvlNode<E> newTop2 = succ();
            newTop2.right = this.right.removeMin(newTop2);
            newTop2.left = this.left;
            newTop2.distinctElements = this.distinctElements - 1;
            newTop2.totalCount = this.totalCount - oldElemCount;
            return newTop2.rebalance();
        }

        @CheckForNull
        private AvlNode<E> removeMin(AvlNode<E> node) {
            AvlNode<E> avlNode = this.left;
            if (avlNode == null) {
                return this.right;
            }
            this.left = avlNode.removeMin(node);
            this.distinctElements--;
            this.totalCount -= node.elemCount;
            return rebalance();
        }

        @CheckForNull
        private AvlNode<E> removeMax(AvlNode<E> node) {
            AvlNode<E> avlNode = this.right;
            if (avlNode == null) {
                return this.left;
            }
            this.right = avlNode.removeMax(node);
            this.distinctElements--;
            this.totalCount -= node.elemCount;
            return rebalance();
        }

        private void recomputeMultiset() {
            this.distinctElements = TreeMultiset.distinctElements(this.left) + 1 + TreeMultiset.distinctElements(this.right);
            this.totalCount = this.elemCount + totalCount(this.left) + totalCount(this.right);
        }

        private void recomputeHeight() {
            this.height = Math.max(height(this.left), height(this.right)) + 1;
        }

        private void recompute() {
            recomputeMultiset();
            recomputeHeight();
        }

        private AvlNode<E> rebalance() {
            switch (balanceFactor()) {
                case -2:
                    Objects.requireNonNull(this.right);
                    if (this.right.balanceFactor() > 0) {
                        this.right = this.right.rotateRight();
                    }
                    return rotateLeft();
                case 2:
                    Objects.requireNonNull(this.left);
                    if (this.left.balanceFactor() < 0) {
                        this.left = this.left.rotateLeft();
                    }
                    return rotateRight();
                default:
                    recomputeHeight();
                    return this;
            }
        }

        private int balanceFactor() {
            return height(this.left) - height(this.right);
        }

        private AvlNode<E> rotateLeft() {
            Preconditions.checkState(this.right != null);
            AvlNode<E> newTop = this.right;
            this.right = newTop.left;
            newTop.left = this;
            newTop.totalCount = this.totalCount;
            newTop.distinctElements = this.distinctElements;
            recompute();
            newTop.recomputeHeight();
            return newTop;
        }

        private AvlNode<E> rotateRight() {
            Preconditions.checkState(this.left != null);
            AvlNode<E> newTop = this.left;
            this.left = newTop.right;
            newTop.right = this;
            newTop.totalCount = this.totalCount;
            newTop.distinctElements = this.distinctElements;
            recompute();
            newTop.recomputeHeight();
            return newTop;
        }

        private static long totalCount(@CheckForNull AvlNode<?> node) {
            if (node == null) {
                return 0L;
            }
            return ((AvlNode) node).totalCount;
        }

        private static int height(@CheckForNull AvlNode<?> node) {
            if (node == null) {
                return 0;
            }
            return ((AvlNode) node).height;
        }

        /* JADX INFO: Access modifiers changed from: private */
        /* JADX WARN: Multi-variable type inference failed */
        @CheckForNull
        public AvlNode<E> ceiling(Comparator<? super E> comparator, @ParametricNullness E e) {
            int cmp = comparator.compare(e, getElement());
            if (cmp < 0) {
                AvlNode<E> avlNode = this.left;
                return avlNode == null ? this : (AvlNode) MoreObjects.firstNonNull(avlNode.ceiling(comparator, e), this);
            }
            if (cmp == 0) {
                return this;
            }
            AvlNode<E> avlNode2 = this.right;
            if (avlNode2 == null) {
                return null;
            }
            return avlNode2.ceiling(comparator, e);
        }

        /* JADX INFO: Access modifiers changed from: private */
        /* JADX WARN: Multi-variable type inference failed */
        @CheckForNull
        public AvlNode<E> floor(Comparator<? super E> comparator, @ParametricNullness E e) {
            int cmp = comparator.compare(e, getElement());
            if (cmp > 0) {
                AvlNode<E> avlNode = this.right;
                return avlNode == null ? this : (AvlNode) MoreObjects.firstNonNull(avlNode.floor(comparator, e), this);
            }
            if (cmp == 0) {
                return this;
            }
            AvlNode<E> avlNode2 = this.left;
            if (avlNode2 == null) {
                return null;
            }
            return avlNode2.floor(comparator, e);
        }

        @ParametricNullness
        E getElement() {
            return (E) NullnessCasts.uncheckedCastNullableTToT(this.elem);
        }

        int getCount() {
            return this.elemCount;
        }

        public String toString() {
            return Multisets.immutableEntry(getElement(), getCount()).toString();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static <T> void successor(AvlNode<T> a, AvlNode<T> b) {
        ((AvlNode) a).succ = b;
        ((AvlNode) b).pred = a;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static <T> void successor(AvlNode<T> a, AvlNode<T> b, AvlNode<T> c) {
        successor(a, b);
        successor(b, c);
    }

    private void writeObject(ObjectOutputStream stream) throws IOException {
        stream.defaultWriteObject();
        stream.writeObject(elementSet().comparator());
        Serialization.writeMultiset(this, stream);
    }

    private void readObject(ObjectInputStream stream) throws IOException, ClassNotFoundException {
        stream.defaultReadObject();
        Comparator<? super E> comparator = (Comparator) stream.readObject();
        Serialization.getFieldSetter(AbstractSortedMultiset.class, "comparator").set((Serialization.FieldSetter) this, (Object) comparator);
        Serialization.getFieldSetter(TreeMultiset.class, "range").set((Serialization.FieldSetter) this, (Object) GeneralRange.all(comparator));
        Serialization.getFieldSetter(TreeMultiset.class, "rootReference").set((Serialization.FieldSetter) this, (Object) new Reference());
        AvlNode<E> header = new AvlNode<>();
        Serialization.getFieldSetter(TreeMultiset.class, "header").set((Serialization.FieldSetter) this, (Object) header);
        successor(header, header);
        Serialization.populateMultiset(this, stream);
    }
}
