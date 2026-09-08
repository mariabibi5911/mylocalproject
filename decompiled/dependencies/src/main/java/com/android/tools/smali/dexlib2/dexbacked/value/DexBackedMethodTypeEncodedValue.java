package com.android.tools.smali.dexlib2.dexbacked.value;

import com.android.tools.smali.dexlib2.base.value.BaseMethodTypeEncodedValue;
import com.android.tools.smali.dexlib2.dexbacked.DexBackedDexFile;
import com.android.tools.smali.dexlib2.dexbacked.DexReader;
import com.android.tools.smali.dexlib2.dexbacked.reference.DexBackedMethodProtoReference;
import com.android.tools.smali.dexlib2.iface.reference.MethodProtoReference;
import javax.annotation.Nonnull;

/* loaded from: classes.dex */
public class DexBackedMethodTypeEncodedValue extends BaseMethodTypeEncodedValue {

    @Nonnull
    public final DexBackedDexFile dexFile;
    private final int methodProtoIndex;

    public DexBackedMethodTypeEncodedValue(@Nonnull DexBackedDexFile dexFile, @Nonnull DexReader reader, int valueArg) {
        this.dexFile = dexFile;
        this.methodProtoIndex = reader.readSizedSmallUint(valueArg + 1);
    }

    @Override // com.android.tools.smali.dexlib2.iface.value.MethodTypeEncodedValue
    @Nonnull
    public MethodProtoReference getValue() {
        return new DexBackedMethodProtoReference(this.dexFile, this.methodProtoIndex);
    }
}
