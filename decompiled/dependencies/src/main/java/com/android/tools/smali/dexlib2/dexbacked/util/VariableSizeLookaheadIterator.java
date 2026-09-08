package com.android.tools.smali.dexlib2.dexbacked.util;

import com.android.tools.smali.dexlib2.dexbacked.DexBuffer;
import com.android.tools.smali.dexlib2.dexbacked.DexReader;
import com.google.common.collect.AbstractIterator;
import java.util.Iterator;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/* loaded from: classes.dex */
public abstract class VariableSizeLookaheadIterator<T> extends AbstractIterator<T> implements Iterator<T> {

    @Nonnull
    private final DexReader reader;

    @Nullable
    protected abstract T readNextItem(@Nonnull DexReader dexReader);

    /* JADX INFO: Access modifiers changed from: protected */
    public VariableSizeLookaheadIterator(@Nonnull DexBuffer buffer, int offset) {
        this.reader = buffer.readerAt(offset);
    }

    @Override // com.google.common.collect.AbstractIterator
    protected T computeNext() {
        return readNextItem(this.reader);
    }

    public final int getReaderOffset() {
        return this.reader.getOffset();
    }
}
