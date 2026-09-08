package com.android.tools.smali.dexlib2.dexbacked.value;

import com.android.tools.smali.dexlib2.base.value.BaseMethodHandleEncodedValue;
import com.android.tools.smali.dexlib2.dexbacked.DexBackedDexFile;
import com.android.tools.smali.dexlib2.dexbacked.DexReader;
import com.android.tools.smali.dexlib2.dexbacked.reference.DexBackedMethodHandleReference;
import com.android.tools.smali.dexlib2.iface.reference.MethodHandleReference;
import javax.annotation.Nonnull;

/* loaded from: classes.dex */
public class DexBackedMethodHandleEncodedValue extends BaseMethodHandleEncodedValue {

    @Nonnull
    public final DexBackedDexFile dexFile;
    private final int methodHandleIndex;

    public DexBackedMethodHandleEncodedValue(@Nonnull DexBackedDexFile dexFile, @Nonnull DexReader reader, int valueArg) {
        this.dexFile = dexFile;
        this.methodHandleIndex = reader.readSizedSmallUint(valueArg + 1);
    }

    @Override // com.android.tools.smali.dexlib2.iface.value.MethodHandleEncodedValue
    @Nonnull
    public MethodHandleReference getValue() {
        return new DexBackedMethodHandleReference(this.dexFile, this.methodHandleIndex);
    }
}
