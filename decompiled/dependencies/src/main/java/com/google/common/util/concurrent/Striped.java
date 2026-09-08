package com.google.common.util.concurrent;

import com.google.common.base.MoreObjects;
import com.google.common.base.Preconditions;
import com.google.common.base.Supplier;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.google.common.collect.MapMaker;
import com.google.common.math.IntMath;
import com.google.common.util.concurrent.Striped;
import java.lang.ref.Reference;
import java.lang.ref.ReferenceQueue;
import java.lang.ref.WeakReference;
import java.math.RoundingMode;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.Semaphore;
import java.util.concurrent.atomic.AtomicReferenceArray;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

@ElementTypesAreNonnullByDefault
/* loaded from: classes7.dex */
public abstract class Striped<L> {
    private static final int ALL_SET = -1;
    private static final int LARGE_LAZY_CUTOFF = 1024;

    public abstract L get(Object obj);

    public abstract L getAt(int i);

    abstract int indexFor(Object obj);

    public abstract int size();

    private Striped() {
    }

    public Iterable<L> bulkGet(Iterable<? extends Object> keys) {
        List<Object> result = Lists.newArrayList(keys);
        if (result.isEmpty()) {
            return ImmutableList.of();
        }
        int[] stripes = new int[result.size()];
        for (int i = 0; i < result.size(); i++) {
            stripes[i] = indexFor(result.get(i));
        }
        Arrays.sort(stripes);
        int previousStripe = stripes[0];
        result.set(0, getAt(previousStripe));
        for (int i2 = 1; i2 < result.size(); i2++) {
            int currentStripe = stripes[i2];
            if (currentStripe == previousStripe) {
                result.set(i2, result.get(i2 - 1));
            } else {
                result.set(i2, getAt(currentStripe));
                previousStripe = currentStripe;
            }
        }
        return Collections.unmodifiableList(result);
    }

    static <L> Striped<L> custom(int stripes, Supplier<L> supplier) {
        return new CompactStriped(stripes, supplier);
    }

    public static Striped<Lock> lock(int stripes) {
        return custom(stripes, new Supplier() { // from class: com.google.common.util.concurrent.Striped$$ExternalSyntheticLambda5
            @Override // com.google.common.base.Supplier
            public final Object get() {
                return new Striped.PaddedLock();
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static /* synthetic */ Lock lambda$lazyWeakLock$0() {
        return new ReentrantLock(false);
    }

    public static Striped<Lock> lazyWeakLock(int stripes) {
        return lazy(stripes, new Supplier() { // from class: com.google.common.util.concurrent.Striped$$ExternalSyntheticLambda4
            @Override // com.google.common.base.Supplier
            public final Object get() {
                return Striped.lambda$lazyWeakLock$0();
            }
        });
    }

    private static <L> Striped<L> lazy(int stripes, Supplier<L> supplier) {
        if (stripes < 1024) {
            return new SmallLazyStriped(stripes, supplier);
        }
        return new LargeLazyStriped(stripes, supplier);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static /* synthetic */ Semaphore lambda$semaphore$1(int permits) {
        return new PaddedSemaphore(permits);
    }

    public static Striped<Semaphore> semaphore(int stripes, final int permits) {
        return custom(stripes, new Supplier() { // from class: com.google.common.util.concurrent.Striped$$ExternalSyntheticLambda0
            @Override // com.google.common.base.Supplier
            public final Object get() {
                return Striped.lambda$semaphore$1(permits);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static /* synthetic */ Semaphore lambda$lazyWeakSemaphore$2(int permits) {
        return new Semaphore(permits, false);
    }

    public static Striped<Semaphore> lazyWeakSemaphore(int stripes, final int permits) {
        return lazy(stripes, new Supplier() { // from class: com.google.common.util.concurrent.Striped$$ExternalSyntheticLambda1
            @Override // com.google.common.base.Supplier
            public final Object get() {
                return Striped.lambda$lazyWeakSemaphore$2(permits);
            }
        });
    }

    public static Striped<ReadWriteLock> readWriteLock(int stripes) {
        return custom(stripes, new Supplier() { // from class: com.google.common.util.concurrent.Striped$$ExternalSyntheticLambda3
            @Override // com.google.common.base.Supplier
            public final Object get() {
                return new ReentrantReadWriteLock();
            }
        });
    }

    public static Striped<ReadWriteLock> lazyWeakReadWriteLock(int stripes) {
        return lazy(stripes, new Supplier() { // from class: com.google.common.util.concurrent.Striped$$ExternalSyntheticLambda2
            @Override // com.google.common.base.Supplier
            public final Object get() {
                return new Striped.WeakSafeReadWriteLock();
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes7.dex */
    public static final class WeakSafeReadWriteLock implements ReadWriteLock {
        private final ReadWriteLock delegate = new ReentrantReadWriteLock();

        @Override // java.util.concurrent.locks.ReadWriteLock
        public Lock readLock() {
            return new WeakSafeLock(this.delegate.readLock(), this);
        }

        @Override // java.util.concurrent.locks.ReadWriteLock
        public Lock writeLock() {
            return new WeakSafeLock(this.delegate.writeLock(), this);
        }
    }

    /* loaded from: classes7.dex */
    private static final class WeakSafeLock extends ForwardingLock {
        private final Lock delegate;
        private final WeakSafeReadWriteLock strongReference;

        WeakSafeLock(Lock delegate, WeakSafeReadWriteLock strongReference) {
            this.delegate = delegate;
            this.strongReference = strongReference;
        }

        @Override // com.google.common.util.concurrent.ForwardingLock
        Lock delegate() {
            return this.delegate;
        }

        @Override // com.google.common.util.concurrent.ForwardingLock, java.util.concurrent.locks.Lock
        public Condition newCondition() {
            return new WeakSafeCondition(this.delegate.newCondition(), this.strongReference);
        }
    }

    /* loaded from: classes7.dex */
    private static final class WeakSafeCondition extends ForwardingCondition {
        private final Condition delegate;
        private final WeakSafeReadWriteLock strongReference;

        WeakSafeCondition(Condition delegate, WeakSafeReadWriteLock strongReference) {
            this.delegate = delegate;
            this.strongReference = strongReference;
        }

        @Override // com.google.common.util.concurrent.ForwardingCondition
        Condition delegate() {
            return this.delegate;
        }
    }

    /* loaded from: classes7.dex */
    private static abstract class PowerOfTwoStriped<L> extends Striped<L> {
        final int mask;

        PowerOfTwoStriped(int stripes) {
            super();
            Preconditions.checkArgument(stripes > 0, "Stripes must be positive");
            this.mask = stripes > 1073741824 ? -1 : Striped.ceilToPowerOfTwo(stripes) - 1;
        }

        @Override // com.google.common.util.concurrent.Striped
        final int indexFor(Object key) {
            int hash = Striped.smear(key.hashCode());
            return this.mask & hash;
        }

        @Override // com.google.common.util.concurrent.Striped
        public final L get(Object key) {
            return getAt(indexFor(key));
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes7.dex */
    public static class CompactStriped<L> extends PowerOfTwoStriped<L> {
        private final Object[] array;

        private CompactStriped(int stripes, Supplier<L> supplier) {
            super(stripes);
            Preconditions.checkArgument(stripes <= 1073741824, "Stripes must be <= 2^30)");
            this.array = new Object[this.mask + 1];
            int i = 0;
            while (true) {
                Object[] objArr = this.array;
                if (i < objArr.length) {
                    objArr[i] = supplier.get();
                    i++;
                } else {
                    return;
                }
            }
        }

        @Override // com.google.common.util.concurrent.Striped
        public L getAt(int i) {
            return (L) this.array[i];
        }

        @Override // com.google.common.util.concurrent.Striped
        public int size() {
            return this.array.length;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes7.dex */
    public static class SmallLazyStriped<L> extends PowerOfTwoStriped<L> {
        final AtomicReferenceArray<ArrayReference<? extends L>> locks;
        final ReferenceQueue<L> queue;
        final int size;
        final Supplier<L> supplier;

        SmallLazyStriped(int stripes, Supplier<L> supplier) {
            super(stripes);
            this.queue = new ReferenceQueue<>();
            int i = this.mask == -1 ? Integer.MAX_VALUE : this.mask + 1;
            this.size = i;
            this.locks = new AtomicReferenceArray<>(i);
            this.supplier = supplier;
        }

        @Override // com.google.common.util.concurrent.Striped
        public L getAt(int i) {
            if (this.size != Integer.MAX_VALUE) {
                Preconditions.checkElementIndex(i, size());
            }
            ArrayReference<? extends L> arrayReference = this.locks.get(i);
            L l = arrayReference == null ? null : (L) arrayReference.get();
            if (l != null) {
                return l;
            }
            L l2 = this.supplier.get();
            ArrayReference arrayReference2 = new ArrayReference(l2, i, this.queue);
            while (!Striped$SmallLazyStriped$$ExternalSyntheticBackportWithForwarding0.m(this.locks, i, arrayReference, arrayReference2)) {
                arrayReference = this.locks.get(i);
                L l3 = (L) (arrayReference == null ? null : arrayReference.get());
                if (l3 != null) {
                    return l3;
                }
            }
            drainQueue();
            return l2;
        }

        private void drainQueue() {
            while (true) {
                Reference<? extends L> ref = this.queue.poll();
                if (ref != null) {
                    ArrayReference<? extends L> arrayRef = (ArrayReference) ref;
                    Striped$SmallLazyStriped$$ExternalSyntheticBackportWithForwarding0.m(this.locks, arrayRef.index, arrayRef, null);
                } else {
                    return;
                }
            }
        }

        @Override // com.google.common.util.concurrent.Striped
        public int size() {
            return this.size;
        }

        /* JADX INFO: Access modifiers changed from: private */
        /* loaded from: classes7.dex */
        public static final class ArrayReference<L> extends WeakReference<L> {
            final int index;

            ArrayReference(L referent, int index, ReferenceQueue<L> queue) {
                super(referent, queue);
                this.index = index;
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes7.dex */
    public static class LargeLazyStriped<L> extends PowerOfTwoStriped<L> {
        final ConcurrentMap<Integer, L> locks;
        final int size;
        final Supplier<L> supplier;

        LargeLazyStriped(int stripes, Supplier<L> supplier) {
            super(stripes);
            this.size = this.mask == -1 ? Integer.MAX_VALUE : this.mask + 1;
            this.supplier = supplier;
            this.locks = new MapMaker().weakValues().makeMap();
        }

        @Override // com.google.common.util.concurrent.Striped
        public L getAt(int i) {
            if (this.size != Integer.MAX_VALUE) {
                Preconditions.checkElementIndex(i, size());
            }
            L l = this.locks.get(Integer.valueOf(i));
            if (l != null) {
                return l;
            }
            L l2 = this.supplier.get();
            return (L) MoreObjects.firstNonNull(this.locks.putIfAbsent(Integer.valueOf(i), l2), l2);
        }

        @Override // com.google.common.util.concurrent.Striped
        public int size() {
            return this.size;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static int ceilToPowerOfTwo(int x) {
        return 1 << IntMath.log2(x, RoundingMode.CEILING);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static int smear(int hashCode) {
        int hashCode2 = hashCode ^ ((hashCode >>> 20) ^ (hashCode >>> 12));
        return ((hashCode2 >>> 7) ^ hashCode2) ^ (hashCode2 >>> 4);
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes7.dex */
    public static class PaddedLock extends ReentrantLock {
        long unused1;
        long unused2;
        long unused3;

        /* JADX INFO: Access modifiers changed from: package-private */
        public PaddedLock() {
            super(false);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes7.dex */
    public static class PaddedSemaphore extends Semaphore {
        long unused1;
        long unused2;
        long unused3;

        PaddedSemaphore(int permits) {
            super(permits, false);
        }
    }
}
