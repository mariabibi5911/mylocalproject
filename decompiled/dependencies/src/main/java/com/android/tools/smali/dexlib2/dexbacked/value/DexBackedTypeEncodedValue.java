package com.android.tools.smali.dexlib2.dexbacked.value;

import com.android.tools.smali.dexlib2.base.value.BaseTypeEncodedValue;
import com.android.tools.smali.dexlib2.dexbacked.DexBackedDexFile;
import com.android.tools.smali.dexlib2.dexbacked.DexReader;
import javax.annotation.Nonnull;

/* loaded from: classes.dex */
public class DexBackedTypeEncodedValue extends BaseTypeEncodedValue {

    @Nonnull
    public final DexBackedDexFile dexFile;
    private final int typeIndex;

    public DexBackedTypeEncodedValue(@Nonnull DexBackedDexFile dexFile, @Nonnull DexReader reader, int valueArg) {
        this.dexFile = dexFile;
        this.typeIndex = reader.readSizedSmallUint(valueArg + 1);
    }

    @Override // com.android.tools.smali.dexlib2.iface.value.TypeEncodedValue
    @Nonnull
    public String getValue() {
        return (String) this.dexFile.getTypeSection().get(this.typeIndex);
    }
}
