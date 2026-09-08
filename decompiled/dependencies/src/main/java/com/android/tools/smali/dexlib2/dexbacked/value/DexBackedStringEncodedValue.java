package com.android.tools.smali.dexlib2.dexbacked.value;

import com.android.tools.smali.dexlib2.base.value.BaseStringEncodedValue;
import com.android.tools.smali.dexlib2.dexbacked.DexBackedDexFile;
import com.android.tools.smali.dexlib2.dexbacked.DexReader;
import javax.annotation.Nonnull;

/* loaded from: classes.dex */
public class DexBackedStringEncodedValue extends BaseStringEncodedValue {

    @Nonnull
    public final DexBackedDexFile dexFile;
    private final int stringIndex;

    public DexBackedStringEncodedValue(@Nonnull DexBackedDexFile dexFile, @Nonnull DexReader reader, int valueArg) {
        this.dexFile = dexFile;
        this.stringIndex = reader.readSizedSmallUint(valueArg + 1);
    }

    @Override // com.android.tools.smali.dexlib2.iface.value.StringEncodedValue
    @Nonnull
    public String getValue() {
        return (String) this.dexFile.getStringSection().get(this.stringIndex);
    }
}
