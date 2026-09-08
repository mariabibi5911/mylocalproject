package com.google.common.util.concurrent;

import com.google.common.collect.ImmutableCollection;
import com.google.common.collect.Lists;
import com.google.common.util.concurrent.AggregateFuture;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import javax.annotation.CheckForNull;

@ElementTypesAreNonnullByDefault
/* loaded from: classes7.dex */
abstract class CollectionFuture<V, C> extends AggregateFuture<V, C> {

    @CheckForNull
    private List<Present<V>> values;

    abstract C combine(List<Present<V>> list);

    CollectionFuture(ImmutableCollection<? extends ListenableFuture<? extends V>> futures, boolean allMustSucceed) {
        super(futures, allMustSucceed, true);
        List<Present<V>> values;
        if (futures.isEmpty()) {
            values = Collections.emptyList();
        } else {
            values = Lists.newArrayListWithCapacity(futures.size());
        }
        for (int i = 0; i < futures.size(); i++) {
            values.add(null);
        }
        this.values = values;
    }

    @Override // com.google.common.util.concurrent.AggregateFuture
    final void collectOneValue(int index, @ParametricNullness V returnValue) {
        List<Present<V>> localValues = this.values;
        if (localValues != null) {
            localValues.set(index, new Present<>(returnValue));
        }
    }

    @Override // com.google.common.util.concurrent.AggregateFuture
    final void handleAllCompleted() {
        List<Present<V>> localValues = this.values;
        if (localValues != null) {
            set(combine(localValues));
        }
    }

    @Override // com.google.common.util.concurrent.AggregateFuture
    void releaseResources(AggregateFuture.ReleaseResourcesReason reason) {
        super.releaseResources(reason);
        this.values = null;
    }

    /* loaded from: classes7.dex */
    static final class ListFuture<V> extends CollectionFuture<V, List<V>> {
        /* JADX INFO: Access modifiers changed from: package-private */
        public ListFuture(ImmutableCollection<? extends ListenableFuture<? extends V>> futures, boolean allMustSucceed) {
            super(futures, allMustSucceed);
            init();
        }

        @Override // com.google.common.util.concurrent.CollectionFuture
        public List<V> combine(List<Present<V>> values) {
            List<V> result = Lists.newArrayListWithCapacity(values.size());
            Iterator<Present<V>> it = values.iterator();
            while (it.hasNext()) {
                Present<V> element = it.next();
                result.add(element != null ? element.value : null);
            }
            return Collections.unmodifiableList(result);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes7.dex */
    public static final class Present<V> {
        V value;

        Present(V value) {
            this.value = value;
        }
    }
}
