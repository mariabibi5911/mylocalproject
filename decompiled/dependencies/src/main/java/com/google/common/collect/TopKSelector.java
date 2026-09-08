package com.google.common.collect;

import com.google.common.base.Preconditions;
import com.google.common.math.IntMath;
import java.math.RoundingMode;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import javax.annotation.CheckForNull;

/* JADX INFO: Access modifiers changed from: package-private */
@ElementTypesAreNonnullByDefault
/* loaded from: classes.dex */
public final class TopKSelector<T> {
    private final T[] buffer;
    private int bufferSize;
    private final Comparator<? super T> comparator;
    private final int k;

    @CheckForNull
    private T threshold;

    public static <T extends Comparable<? super T>> TopKSelector<T> least(int k) {
        return least(k, Ordering.natural());
    }

    public static <T> TopKSelector<T> least(int k, Comparator<? super T> comparator) {
        return new TopKSelector<>(comparator, k);
    }

    public static <T extends Comparable<? super T>> TopKSelector<T> greatest(int k) {
        return greatest(k, Ordering.natural());
    }

    public static <T> TopKSelector<T> greatest(int k, Comparator<? super T> comparator) {
        return new TopKSelector<>(Ordering.from(comparator).reverse(), k);
    }

    private TopKSelector(Comparator<? super T> comparator, int i) {
        this.comparator = (Comparator) Preconditions.checkNotNull(comparator, "comparator");
        this.k = i;
        Preconditions.checkArgument(i >= 0, "k (%s) must be >= 0", i);
        Preconditions.checkArgument(i <= 1073741823, "k (%s) must be <= Integer.MAX_VALUE / 2", i);
        this.buffer = (T[]) new Object[IntMath.checkedMultiply(i, 2)];
        this.bufferSize = 0;
        this.threshold = null;
    }

    public void offer(@ParametricNullness T t) {
        int i = this.k;
        if (i == 0) {
            return;
        }
        int i2 = this.bufferSize;
        if (i2 == 0) {
            this.buffer[0] = t;
            this.threshold = t;
            this.bufferSize = 1;
            return;
        }
        if (i2 < i) {
            T[] tArr = this.buffer;
            this.bufferSize = i2 + 1;
            tArr[i2] = t;
            if (this.comparator.compare(t, (Object) NullnessCasts.uncheckedCastNullableTToT(this.threshold)) > 0) {
                this.threshold = t;
                return;
            }
            return;
        }
        if (this.comparator.compare(t, (Object) NullnessCasts.uncheckedCastNullableTToT(this.threshold)) < 0) {
            T[] tArr2 = this.buffer;
            int i3 = this.bufferSize;
            int i4 = i3 + 1;
            this.bufferSize = i4;
            tArr2[i3] = t;
            if (i4 == this.k * 2) {
                trim();
            }
        }
    }

    private void trim() {
        int i = 0;
        int i2 = (this.k * 2) - 1;
        int i3 = 0;
        int i4 = 0;
        int log2 = IntMath.log2(i2 - 0, RoundingMode.CEILING) * 3;
        while (true) {
            if (i >= i2) {
                break;
            }
            int partition = partition(i, i2, ((i + i2) + 1) >>> 1);
            int i5 = this.k;
            if (partition > i5) {
                i2 = partition - 1;
            } else {
                if (partition >= i5) {
                    break;
                }
                i = Math.max(partition, i + 1);
                i3 = partition;
            }
            i4++;
            if (i4 >= log2) {
                Arrays.sort(this.buffer, i, i2 + 1, this.comparator);
                break;
            }
        }
        this.bufferSize = this.k;
        this.threshold = (T) NullnessCasts.uncheckedCastNullableTToT(this.buffer[i3]);
        for (int i6 = i3 + 1; i6 < this.k; i6++) {
            if (this.comparator.compare((Object) NullnessCasts.uncheckedCastNullableTToT(this.buffer[i6]), (Object) NullnessCasts.uncheckedCastNullableTToT(this.threshold)) > 0) {
                this.threshold = this.buffer[i6];
            }
        }
    }

    private int partition(int i, int i2, int i3) {
        Object uncheckedCastNullableTToT = NullnessCasts.uncheckedCastNullableTToT(this.buffer[i3]);
        T[] tArr = this.buffer;
        tArr[i3] = tArr[i2];
        int i4 = i;
        for (int i5 = i; i5 < i2; i5++) {
            if (this.comparator.compare((Object) NullnessCasts.uncheckedCastNullableTToT(this.buffer[i5]), uncheckedCastNullableTToT) < 0) {
                swap(i4, i5);
                i4++;
            }
        }
        T[] tArr2 = this.buffer;
        tArr2[i2] = tArr2[i4];
        tArr2[i4] = uncheckedCastNullableTToT;
        return i4;
    }

    private void swap(int i, int j) {
        T[] tArr = this.buffer;
        T tmp = tArr[i];
        tArr[i] = tArr[j];
        tArr[j] = tmp;
    }

    public void offerAll(Iterable<? extends T> elements) {
        offerAll(elements.iterator());
    }

    public void offerAll(Iterator<? extends T> elements) {
        while (elements.hasNext()) {
            offer(elements.next());
        }
    }

    public List<T> topK() {
        T[] castBuffer = this.buffer;
        Arrays.sort(castBuffer, 0, this.bufferSize, this.comparator);
        int i = this.bufferSize;
        int i2 = this.k;
        if (i > i2) {
            T[] tArr = this.buffer;
            Arrays.fill(tArr, i2, tArr.length, (Object) null);
            int i3 = this.k;
            this.bufferSize = i3;
            this.threshold = this.buffer[i3 - 1];
        }
        return Collections.unmodifiableList(Arrays.asList(Arrays.copyOf(this.buffer, this.bufferSize)));
    }
}
