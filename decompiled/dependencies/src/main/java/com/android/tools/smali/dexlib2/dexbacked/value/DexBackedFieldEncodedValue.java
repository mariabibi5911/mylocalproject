package com.android.tools.smali.dexlib2.dexbacked.value;

import com.android.tools.smali.dexlib2.base.value.BaseFieldEncodedValue;
import com.android.tools.smali.dexlib2.dexbacked.DexBackedDexFile;
import com.android.tools.smali.dexlib2.dexbacked.DexReader;
import com.android.tools.smali.dexlib2.dexbacked.reference.DexBackedFieldReference;
import com.android.tools.smali.dexlib2.iface.reference.FieldReference;
import javax.annotation.Nonnull;

/* loaded from: classes.dex */
public class DexBackedFieldEncodedValue extends BaseFieldEncodedValue {

    @Nonnull
    public final DexBackedDexFile dexFile;
    private final int fieldIndex;

    public DexBackedFieldEncodedValue(@Nonnull DexBackedDexFile dexFile, @Nonnull DexReader reader, int valueArg) {
        this.dexFile = dexFile;
        this.fieldIndex = reader.readSizedSmallUint(valueArg + 1);
    }

    @Override // com.android.tools.smali.dexlib2.iface.value.FieldEncodedValue
    @Nonnull
    public FieldReference getValue() {
        return new DexBackedFieldReference(this.dexFile, this.fieldIndex);
    }
}
