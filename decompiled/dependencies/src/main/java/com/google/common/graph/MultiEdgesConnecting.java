package com.google.common.graph;

import com.google.common.base.Preconditions;
import com.google.common.collect.AbstractIterator;
import com.google.common.collect.UnmodifiableIterator;
import java.util.AbstractSet;
import java.util.Iterator;
import java.util.Map;
import javax.annotation.CheckForNull;

@ElementTypesAreNonnullByDefault
/* loaded from: classes7.dex */
abstract class MultiEdgesConnecting<E> extends AbstractSet<E> {
    private final Map<E, ?> outEdgeToNode;
    private final Object targetNode;

    /* JADX INFO: Access modifiers changed from: package-private */
    public MultiEdgesConnecting(Map<E, ?> outEdgeToNode, Object targetNode) {
        this.outEdgeToNode = (Map) Preconditions.checkNotNull(outEdgeToNode);
        this.targetNode = Preconditions.checkNotNull(targetNode);
    }

    @Override // java.util.AbstractCollection, java.util.Collection, java.lang.Iterable, java.util.Set
    public UnmodifiableIterator<E> iterator() {
        final Iterator<? extends Map.Entry<E, ?>> entries = this.outEdgeToNode.entrySet().iterator();
        return new AbstractIterator<E>() { // from class: com.google.common.graph.MultiEdgesConnecting.1
            @Override // com.google.common.collect.AbstractIterator
            @CheckForNull
            protected E computeNext() {
                while (entries.hasNext()) {
                    Map.Entry<E, ?> entry = (Map.Entry) entries.next();
                    if (MultiEdgesConnecting.this.targetNode.equals(entry.getValue())) {
                        return entry.getKey();
                    }
                }
                return endOfData();
            }
        };
    }

    @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
    public boolean contains(@CheckForNull Object edge) {
        return this.targetNode.equals(this.outEdgeToNode.get(edge));
    }
}
