package com.android.tools.smali.dexlib2.dexbacked.value;

import com.android.tools.smali.dexlib2.base.value.BaseAnnotationEncodedValue;
import com.android.tools.smali.dexlib2.dexbacked.DexBackedAnnotationElement;
import com.android.tools.smali.dexlib2.dexbacked.DexBackedDexFile;
import com.android.tools.smali.dexlib2.dexbacked.DexReader;
import com.android.tools.smali.dexlib2.dexbacked.util.VariableSizeSet;
import com.android.tools.smali.dexlib2.iface.value.AnnotationEncodedValue;
import java.util.Set;
import javax.annotation.Nonnull;

/* loaded from: classes.dex */
public class DexBackedAnnotationEncodedValue extends BaseAnnotationEncodedValue implements AnnotationEncodedValue {

    @Nonnull
    public final DexBackedDexFile dexFile;
    private final int elementCount;
    private final int elementsOffset;

    @Nonnull
    public final String type;

    public DexBackedAnnotationEncodedValue(@Nonnull DexBackedDexFile dexFile, @Nonnull DexReader reader) {
        this.dexFile = dexFile;
        this.type = (String) dexFile.getTypeSection().get(reader.readSmallUleb128());
        int readSmallUleb128 = reader.readSmallUleb128();
        this.elementCount = readSmallUleb128;
        this.elementsOffset = reader.getOffset();
        skipElements(reader, readSmallUleb128);
    }

    public static void skipFrom(@Nonnull DexReader reader) {
        reader.skipUleb128();
        int elementCount = reader.readSmallUleb128();
        skipElements(reader, elementCount);
    }

    private static void skipElements(@Nonnull DexReader reader, int elementCount) {
        for (int i = 0; i < elementCount; i++) {
            reader.skipUleb128();
            DexBackedEncodedValue.skipFrom(reader);
        }
    }

    @Override // com.android.tools.smali.dexlib2.iface.value.AnnotationEncodedValue, com.android.tools.smali.dexlib2.iface.BasicAnnotation
    @Nonnull
    public String getType() {
        return this.type;
    }

    @Override // com.android.tools.smali.dexlib2.iface.value.AnnotationEncodedValue, com.android.tools.smali.dexlib2.iface.BasicAnnotation
    @Nonnull
    public Set<? extends DexBackedAnnotationElement> getElements() {
        return new VariableSizeSet<DexBackedAnnotationElement>(this.dexFile.getDataBuffer(), this.elementsOffset, this.elementCount) { // from class: com.android.tools.smali.dexlib2.dexbacked.value.DexBackedAnnotationEncodedValue.1
            /* JADX INFO: Access modifiers changed from: protected */
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // com.android.tools.smali.dexlib2.dexbacked.util.VariableSizeSet
            @Nonnull
            public DexBackedAnnotationElement readNextItem(@Nonnull DexReader dexReader, int index) {
                return new DexBackedAnnotationElement(DexBackedAnnotationEncodedValue.this.dexFile, dexReader);
            }
        };
    }
}
