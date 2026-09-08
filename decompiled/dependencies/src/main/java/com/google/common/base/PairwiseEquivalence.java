package com.google.common.base;

import java.io.Serializable;
import java.util.Iterator;
import javax.annotation.CheckForNull;

@ElementTypesAreNonnullByDefault
/* loaded from: classes.dex */
final class PairwiseEquivalence<E, T extends E> extends Equivalence<Iterable<T>> implements Serializable {
    private static final long serialVersionUID = 1;
    final Equivalence<E> elementEquivalence;

    /* JADX INFO: Access modifiers changed from: package-private */
    public PairwiseEquivalence(Equivalence<E> elementEquivalence) {
        this.elementEquivalence = (Equivalence) Preconditions.checkNotNull(elementEquivalence);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.google.common.base.Equivalence
    public boolean doEquivalent(Iterable<T> iterableA, Iterable<T> iterableB) {
        Iterator<T> iteratorA = iterableA.iterator();
        Iterator<T> iteratorB = iterableB.iterator();
        while (iteratorA.hasNext() && iteratorB.hasNext()) {
            if (!this.elementEquivalence.equivalent(iteratorA.next(), iteratorB.next())) {
                return false;
            }
        }
        return (iteratorA.hasNext() || iteratorB.hasNext()) ? false : true;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.google.common.base.Equivalence
    public int doHash(Iterable<T> iterable) {
        int hash = 78721;
        for (T element : iterable) {
            hash = (hash * 24943) + this.elementEquivalence.hash(element);
        }
        return hash;
    }

    public boolean equals(@CheckForNull Object object) {
        if (object instanceof PairwiseEquivalence) {
            PairwiseEquivalence<?, ?> that = (PairwiseEquivalence) object;
            return this.elementEquivalence.equals(that.elementEquivalence);
        }
        return false;
    }

    public int hashCode() {
        return this.elementEquivalence.hashCode() ^ 1185147655;
    }

    public String toString() {
        String valueOf = String.valueOf(this.elementEquivalence);
        return new StringBuilder(String.valueOf(valueOf).length() + 11).append(valueOf).append(".pairwise()").toString();
    }
}
