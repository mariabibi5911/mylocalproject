package com.android.tools.smali.dexlib2.dexbacked.util;

import com.android.tools.smali.dexlib2.dexbacked.DexBuffer;
import com.android.tools.smali.dexlib2.dexbacked.DexReader;
import java.util.ListIterator;
import java.util.NoSuchElementException;
import javax.annotation.Nonnull;

/* loaded from: classes.dex */
public abstract class VariableSizeListIterator<T> implements ListIterator<T> {
    private int index;

    @Nonnull
    private DexReader<? extends DexBuffer> reader;
    protected final int size;
    private final int startOffset;

    protected abstract T readNextItem(@Nonnull DexReader<? extends DexBuffer> dexReader, int i);

    /* JADX INFO: Access modifiers changed from: protected */
    public VariableSizeListIterator(@Nonnull DexBuffer buffer, int offset, int size) {
        this.reader = buffer.readerAt(offset);
        this.startOffset = offset;
        this.size = size;
    }

    public int getReaderOffset() {
        return this.reader.getOffset();
    }

    @Override // java.util.ListIterator, java.util.Iterator
    public boolean hasNext() {
        return this.index < this.size;
    }

    @Override // java.util.ListIterator, java.util.Iterator
    public T next() {
        int i = this.index;
        if (i >= this.size) {
            throw new NoSuchElementException();
        }
        DexReader<? extends DexBuffer> dexReader = this.reader;
        this.index = i + 1;
        return readNextItem(dexReader, i);
    }

    @Override // java.util.ListIterator
    public boolean hasPrevious() {
        return this.index > 0;
    }

    @Override // java.util.ListIterator
    public T previous() {
        int targetIndex = this.index - 1;
        this.reader.setOffset(this.startOffset);
        this.index = 0;
        while (true) {
            int i = this.index;
            if (i < targetIndex) {
                DexReader<? extends DexBuffer> dexReader = this.reader;
                this.index = i + 1;
                readNextItem(dexReader, i);
            } else {
                DexReader<? extends DexBuffer> dexReader2 = this.reader;
                this.index = i + 1;
                return readNextItem(dexReader2, i);
            }
        }
    }

    @Override // java.util.ListIterator
    public int nextIndex() {
        return this.index;
    }

    @Override // java.util.ListIterator
    public int previousIndex() {
        return this.index - 1;
    }

    @Override // java.util.ListIterator, java.util.Iterator
    public void remove() {
        throw new UnsupportedOperationException();
    }

    @Override // java.util.ListIterator
    public void set(T t) {
        throw new UnsupportedOperationException();
    }

    @Override // java.util.ListIterator
    public void add(T t) {
        throw new UnsupportedOperationException();
    }
}
