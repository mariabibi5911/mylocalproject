package com.android.tools.smali.dexlib2.dexbacked.util;

import com.android.tools.smali.dexlib2.dexbacked.DexBuffer;
import com.android.tools.smali.dexlib2.dexbacked.DexReader;
import java.util.AbstractSequentialList;
import javax.annotation.Nonnull;

/* loaded from: classes.dex */
public abstract class VariableSizeList<T> extends AbstractSequentialList<T> {

    @Nonnull
    private final DexBuffer buffer;
    private final int offset;
    private final int size;

    protected abstract T readNextItem(@Nonnull DexReader dexReader, int i);

    public VariableSizeList(@Nonnull DexBuffer buffer, int offset, int size) {
        this.buffer = buffer;
        this.offset = offset;
        this.size = size;
    }

    @Override // java.util.AbstractList, java.util.List
    @Nonnull
    public VariableSizeListIterator<T> listIterator() {
        return listIterator(0);
    }

    @Override // java.util.AbstractCollection, java.util.Collection, java.util.List
    public int size() {
        return this.size;
    }

    @Override // java.util.AbstractSequentialList, java.util.AbstractList, java.util.List
    @Nonnull
    public VariableSizeListIterator<T> listIterator(int index) {
        VariableSizeListIterator<T> iterator = new VariableSizeListIterator<T>(this.buffer, this.offset, this.size) { // from class: com.android.tools.smali.dexlib2.dexbacked.util.VariableSizeList.1
            @Override // com.android.tools.smali.dexlib2.dexbacked.util.VariableSizeListIterator
            protected T readNextItem(@Nonnull DexReader dexReader, int i) {
                return (T) VariableSizeList.this.readNextItem(dexReader, i);
            }
        };
        for (int i = 0; i < index; i++) {
            iterator.next();
        }
        return iterator;
    }
}
