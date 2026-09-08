package com.android.tools.smali.dexlib2.dexbacked;

import com.android.tools.smali.dexlib2.base.BaseTryBlock;
import com.android.tools.smali.dexlib2.dexbacked.util.VariableSizeList;
import java.util.List;
import javax.annotation.Nonnull;

/* loaded from: classes.dex */
public class DexBackedTryBlock extends BaseTryBlock<DexBackedExceptionHandler> {

    @Nonnull
    public final DexBackedDexFile dexFile;
    private final int handlersStartOffset;
    private final int tryItemOffset;

    public DexBackedTryBlock(@Nonnull DexBackedDexFile dexFile, int tryItemOffset, int handlersStartOffset) {
        this.dexFile = dexFile;
        this.tryItemOffset = tryItemOffset;
        this.handlersStartOffset = handlersStartOffset;
    }

    @Override // com.android.tools.smali.dexlib2.iface.TryBlock
    public int getStartCodeAddress() {
        return this.dexFile.getDataBuffer().readSmallUint(this.tryItemOffset + 0);
    }

    @Override // com.android.tools.smali.dexlib2.iface.TryBlock
    public int getCodeUnitCount() {
        return this.dexFile.getDataBuffer().readUshort(this.tryItemOffset + 4);
    }

    @Override // com.android.tools.smali.dexlib2.iface.TryBlock
    @Nonnull
    public List<? extends DexBackedExceptionHandler> getExceptionHandlers() {
        DexReader reader = this.dexFile.getDataBuffer().readerAt(this.handlersStartOffset + this.dexFile.getDataBuffer().readUshort(this.tryItemOffset + 6));
        int encodedSize = reader.readSleb128();
        if (encodedSize > 0) {
            return new VariableSizeList<DexBackedTypedExceptionHandler>(this.dexFile.getDataBuffer(), reader.getOffset(), encodedSize) { // from class: com.android.tools.smali.dexlib2.dexbacked.DexBackedTryBlock.1
                /* JADX INFO: Access modifiers changed from: protected */
                /* JADX WARN: Can't rename method to resolve collision */
                @Override // com.android.tools.smali.dexlib2.dexbacked.util.VariableSizeList
                @Nonnull
                public DexBackedTypedExceptionHandler readNextItem(@Nonnull DexReader reader2, int index) {
                    return new DexBackedTypedExceptionHandler(DexBackedTryBlock.this.dexFile, reader2);
                }
            };
        }
        final int sizeWithCatchAll = (encodedSize * (-1)) + 1;
        return new VariableSizeList<DexBackedExceptionHandler>(this.dexFile.getDataBuffer(), reader.getOffset(), sizeWithCatchAll) { // from class: com.android.tools.smali.dexlib2.dexbacked.DexBackedTryBlock.2
            /* JADX INFO: Access modifiers changed from: protected */
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // com.android.tools.smali.dexlib2.dexbacked.util.VariableSizeList
            @Nonnull
            public DexBackedExceptionHandler readNextItem(@Nonnull DexReader dexReader, int index) {
                if (index == sizeWithCatchAll - 1) {
                    return new DexBackedCatchAllExceptionHandler(dexReader);
                }
                return new DexBackedTypedExceptionHandler(DexBackedTryBlock.this.dexFile, dexReader);
            }
        };
    }
}
