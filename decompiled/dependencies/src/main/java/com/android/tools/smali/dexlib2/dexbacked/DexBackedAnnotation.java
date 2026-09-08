package com.android.tools.smali.dexlib2.dexbacked;

import com.android.tools.smali.dexlib2.base.BaseAnnotation;
import com.android.tools.smali.dexlib2.dexbacked.util.VariableSizeSet;
import java.util.Set;
import javax.annotation.Nonnull;

/* loaded from: classes.dex */
public class DexBackedAnnotation extends BaseAnnotation {

    @Nonnull
    public final DexBackedDexFile dexFile;
    private final int elementsOffset;
    public final int typeIndex;
    public final int visibility;

    public DexBackedAnnotation(@Nonnull DexBackedDexFile dexFile, int annotationOffset) {
        this.dexFile = dexFile;
        DexReader reader = dexFile.getDataBuffer().readerAt(annotationOffset);
        this.visibility = reader.readUbyte();
        this.typeIndex = reader.readSmallUleb128();
        this.elementsOffset = reader.getOffset();
    }

    @Override // com.android.tools.smali.dexlib2.iface.Annotation
    public int getVisibility() {
        return this.visibility;
    }

    @Override // com.android.tools.smali.dexlib2.iface.Annotation, com.android.tools.smali.dexlib2.iface.BasicAnnotation
    @Nonnull
    public String getType() {
        return (String) this.dexFile.getTypeSection().get(this.typeIndex);
    }

    @Override // com.android.tools.smali.dexlib2.iface.Annotation, com.android.tools.smali.dexlib2.iface.BasicAnnotation
    @Nonnull
    public Set<? extends DexBackedAnnotationElement> getElements() {
        DexReader reader = this.dexFile.getDataBuffer().readerAt(this.elementsOffset);
        int size = reader.readSmallUleb128();
        return new VariableSizeSet<DexBackedAnnotationElement>(this.dexFile.getDataBuffer(), reader.getOffset(), size) { // from class: com.android.tools.smali.dexlib2.dexbacked.DexBackedAnnotation.1
            /* JADX INFO: Access modifiers changed from: protected */
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // com.android.tools.smali.dexlib2.dexbacked.util.VariableSizeSet
            @Nonnull
            public DexBackedAnnotationElement readNextItem(@Nonnull DexReader reader2, int index) {
                return new DexBackedAnnotationElement(DexBackedAnnotation.this.dexFile, reader2);
            }
        };
    }
}
