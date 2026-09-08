package com.android.tools.smali.dexlib2.dexbacked.value;

import com.android.tools.smali.dexlib2.base.value.BaseArrayEncodedValue;
import com.android.tools.smali.dexlib2.dexbacked.DexBackedDexFile;
import com.android.tools.smali.dexlib2.dexbacked.DexReader;
import com.android.tools.smali.dexlib2.dexbacked.util.VariableSizeList;
import com.android.tools.smali.dexlib2.iface.value.ArrayEncodedValue;
import com.android.tools.smali.dexlib2.iface.value.EncodedValue;
import java.util.List;
import javax.annotation.Nonnull;

/* loaded from: classes.dex */
public class DexBackedArrayEncodedValue extends BaseArrayEncodedValue implements ArrayEncodedValue {

    @Nonnull
    public final DexBackedDexFile dexFile;
    private final int elementCount;
    private final int encodedArrayOffset;

    public DexBackedArrayEncodedValue(@Nonnull DexBackedDexFile dexFile, @Nonnull DexReader reader) {
        this.dexFile = dexFile;
        int readSmallUleb128 = reader.readSmallUleb128();
        this.elementCount = readSmallUleb128;
        this.encodedArrayOffset = reader.getOffset();
        skipElementsFrom(reader, readSmallUleb128);
    }

    public static void skipFrom(@Nonnull DexReader reader) {
        int elementCount = reader.readSmallUleb128();
        skipElementsFrom(reader, elementCount);
    }

    private static void skipElementsFrom(@Nonnull DexReader reader, int elementCount) {
        for (int i = 0; i < elementCount; i++) {
            DexBackedEncodedValue.skipFrom(reader);
        }
    }

    @Override // com.android.tools.smali.dexlib2.iface.value.ArrayEncodedValue
    @Nonnull
    public List<? extends EncodedValue> getValue() {
        return new VariableSizeList<EncodedValue>(this.dexFile.getDataBuffer(), this.encodedArrayOffset, this.elementCount) { // from class: com.android.tools.smali.dexlib2.dexbacked.value.DexBackedArrayEncodedValue.1
            /* JADX INFO: Access modifiers changed from: protected */
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // com.android.tools.smali.dexlib2.dexbacked.util.VariableSizeList
            @Nonnull
            public EncodedValue readNextItem(@Nonnull DexReader dexReader, int index) {
                return DexBackedEncodedValue.readFrom(DexBackedArrayEncodedValue.this.dexFile, dexReader);
            }
        };
    }
}
