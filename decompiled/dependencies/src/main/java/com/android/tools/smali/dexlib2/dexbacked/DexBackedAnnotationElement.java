package com.android.tools.smali.dexlib2.dexbacked;

import com.android.tools.smali.dexlib2.base.BaseAnnotationElement;
import com.android.tools.smali.dexlib2.dexbacked.value.DexBackedEncodedValue;
import com.android.tools.smali.dexlib2.iface.value.EncodedValue;
import javax.annotation.Nonnull;

/* loaded from: classes.dex */
public class DexBackedAnnotationElement extends BaseAnnotationElement {

    @Nonnull
    private final DexBackedDexFile dexFile;
    public final int nameIndex;

    @Nonnull
    public final EncodedValue value;

    public DexBackedAnnotationElement(@Nonnull DexBackedDexFile dexFile, @Nonnull DexReader reader) {
        this.dexFile = dexFile;
        this.nameIndex = reader.readSmallUleb128();
        this.value = DexBackedEncodedValue.readFrom(dexFile, reader);
    }

    @Override // com.android.tools.smali.dexlib2.iface.AnnotationElement
    @Nonnull
    public String getName() {
        return (String) this.dexFile.getStringSection().get(this.nameIndex);
    }

    @Override // com.android.tools.smali.dexlib2.iface.AnnotationElement
    @Nonnull
    public EncodedValue getValue() {
        return this.value;
    }
}
