package com.android.tools.smali.dexlib2.dexbacked.util;

import com.android.tools.smali.dexlib2.dexbacked.DexBackedDexFile;
import com.android.tools.smali.dexlib2.dexbacked.DexBuffer;
import com.android.tools.smali.dexlib2.dexbacked.DexReader;
import com.android.tools.smali.dexlib2.dexbacked.value.DexBackedEncodedValue;
import com.android.tools.smali.dexlib2.iface.value.EncodedValue;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/* loaded from: classes.dex */
public abstract class EncodedArrayItemIterator {
    public static final EncodedArrayItemIterator EMPTY = new EncodedArrayItemIterator() { // from class: com.android.tools.smali.dexlib2.dexbacked.util.EncodedArrayItemIterator.1
        @Override // com.android.tools.smali.dexlib2.dexbacked.util.EncodedArrayItemIterator
        @Nullable
        public EncodedValue getNextOrNull() {
            return null;
        }

        @Override // com.android.tools.smali.dexlib2.dexbacked.util.EncodedArrayItemIterator
        public void skipNext() {
        }

        @Override // com.android.tools.smali.dexlib2.dexbacked.util.EncodedArrayItemIterator
        public int getReaderOffset() {
            return 0;
        }

        @Override // com.android.tools.smali.dexlib2.dexbacked.util.EncodedArrayItemIterator
        public int getItemCount() {
            return 0;
        }
    };

    public abstract int getItemCount();

    @Nullable
    public abstract EncodedValue getNextOrNull();

    public abstract int getReaderOffset();

    public abstract void skipNext();

    @Nonnull
    public static EncodedArrayItemIterator newOrEmpty(@Nonnull DexBackedDexFile dexFile, int offset) {
        if (offset == 0) {
            return EMPTY;
        }
        return new EncodedArrayItemIteratorImpl(dexFile, offset);
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
    public static class EncodedArrayItemIteratorImpl extends EncodedArrayItemIterator {

        @Nonnull
        private final DexBackedDexFile dexFile;
        private int index = 0;

        @Nonnull
        private final DexReader reader;
        private final int size;

        public EncodedArrayItemIteratorImpl(@Nonnull DexBackedDexFile dexFile, int offset) {
            this.dexFile = dexFile;
            DexReader<? extends DexBuffer> readerAt = dexFile.getDataBuffer().readerAt(offset);
            this.reader = readerAt;
            this.size = readerAt.readSmallUleb128();
        }

        @Override // com.android.tools.smali.dexlib2.dexbacked.util.EncodedArrayItemIterator
        @Nullable
        public EncodedValue getNextOrNull() {
            int i = this.index;
            if (i < this.size) {
                this.index = i + 1;
                return DexBackedEncodedValue.readFrom(this.dexFile, this.reader);
            }
            return null;
        }

        @Override // com.android.tools.smali.dexlib2.dexbacked.util.EncodedArrayItemIterator
        public void skipNext() {
            int i = this.index;
            if (i < this.size) {
                this.index = i + 1;
                DexBackedEncodedValue.skipFrom(this.reader);
            }
        }

        @Override // com.android.tools.smali.dexlib2.dexbacked.util.EncodedArrayItemIterator
        public int getReaderOffset() {
            return this.reader.getOffset();
        }

        @Override // com.android.tools.smali.dexlib2.dexbacked.util.EncodedArrayItemIterator
        public int getItemCount() {
            return this.size;
        }
    }
}
