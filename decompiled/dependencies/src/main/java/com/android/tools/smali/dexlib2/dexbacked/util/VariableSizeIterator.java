package com.android.tools.smali.dexlib2.dexbacked.util;

import com.android.tools.smali.dexlib2.dexbacked.DexBuffer;
import com.android.tools.smali.dexlib2.dexbacked.DexReader;
import java.util.Iterator;
import java.util.NoSuchElementException;
import javax.annotation.Nonnull;

/* loaded from: classes.dex */
public abstract class VariableSizeIterator<T> implements Iterator<T> {
    private int index;

    @Nonnull
    private final DexReader reader;
    protected final int size;

    protected abstract T readNextItem(@Nonnull DexReader dexReader, int i);

    /* JADX INFO: Access modifiers changed from: protected */
    public VariableSizeIterator(@Nonnull DexBuffer buffer, int offset, int size) {
        this.reader = buffer.readerAt(offset);
        this.size = size;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public VariableSizeIterator(@Nonnull DexReader reader, int size) {
        this.reader = reader;
        this.size = size;
    }

    public int getReaderOffset() {
        return this.reader.getOffset();
    }

    @Override // java.util.Iterator
    public boolean hasNext() {
        return this.index < this.size;
    }

    @Override // java.util.Iterator
    public T next() {
        int i = this.index;
        if (i >= this.size) {
            throw new NoSuchElementException();
        }
        DexReader dexReader = this.reader;
        this.index = i + 1;
        return readNextItem(dexReader, i);
    }

    @Override // java.util.Iterator
    public void remove() {
        throw new UnsupportedOperationException();
    }
}
