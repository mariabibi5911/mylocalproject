package com.google.common.collect;

import java.util.NoSuchElementException;
import javax.annotation.CheckForNull;

@ElementTypesAreNonnullByDefault
/* loaded from: classes.dex */
public abstract class AbstractSequentialIterator<T> extends UnmodifiableIterator<T> {

    @CheckForNull
    private T nextOrNull;

    @CheckForNull
    protected abstract T computeNext(T t);

    /* JADX INFO: Access modifiers changed from: protected */
    public AbstractSequentialIterator(@CheckForNull T firstOrNull) {
        this.nextOrNull = firstOrNull;
    }

    @Override // java.util.Iterator
    public final boolean hasNext() {
        return this.nextOrNull != null;
    }

    @Override // java.util.Iterator
    public final T next() {
        if (this.nextOrNull == null) {
            throw new NoSuchElementException();
        }
        T oldNext = this.nextOrNull;
        this.nextOrNull = computeNext(oldNext);
        return oldNext;
    }
}
