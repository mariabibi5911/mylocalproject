package com.android.tools.smali.dexlib2.dexbacked.util;

import com.android.tools.smali.dexlib2.dexbacked.DexBuffer;
import com.android.tools.smali.dexlib2.dexbacked.DexReader;
import java.util.AbstractSet;
import javax.annotation.Nonnull;

/* loaded from: classes.dex */
public abstract class VariableSizeSet<T> extends AbstractSet<T> {

    @Nonnull
    private final DexBuffer buffer;
    private final int offset;
    private final int size;

    protected abstract T readNextItem(@Nonnull DexReader dexReader, int i);

    public VariableSizeSet(@Nonnull DexBuffer buffer, int offset, int size) {
        this.buffer = buffer;
        this.offset = offset;
        this.size = size;
    }

    @Override // java.util.AbstractCollection, java.util.Collection, java.lang.Iterable, java.util.Set
    @Nonnull
    public VariableSizeIterator<T> iterator() {
        return new VariableSizeIterator<T>(this.buffer, this.offset, this.size) { // from class: com.android.tools.smali.dexlib2.dexbacked.util.VariableSizeSet.1
            @Override // com.android.tools.smali.dexlib2.dexbacked.util.VariableSizeIterator
            protected T readNextItem(@Nonnull DexReader dexReader, int i) {
                return (T) VariableSizeSet.this.readNextItem(dexReader, i);
            }
        };
    }

    @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
    public int size() {
        return this.size;
    }
}
