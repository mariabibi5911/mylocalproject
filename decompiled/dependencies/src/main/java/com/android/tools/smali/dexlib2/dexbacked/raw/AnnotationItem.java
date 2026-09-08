package com.android.tools.smali.dexlib2.dexbacked.raw;

import com.android.tools.smali.dexlib2.dexbacked.DexBackedDexFile;
import com.android.tools.smali.dexlib2.dexbacked.DexReader;
import com.android.tools.smali.dexlib2.dexbacked.raw.util.DexAnnotator;
import com.android.tools.smali.dexlib2.util.AnnotatedBytes;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/* loaded from: classes.dex */
public class AnnotationItem {
    public static final int ANNOTATION_OFFSET = 1;
    public static final int VISIBILITY_OFFSET = 0;

    @Nonnull
    public static SectionAnnotator makeAnnotator(@Nonnull DexAnnotator annotator, @Nonnull MapItem mapItem) {
        return new SectionAnnotator(annotator, mapItem) { // from class: com.android.tools.smali.dexlib2.dexbacked.raw.AnnotationItem.1
            @Override // com.android.tools.smali.dexlib2.dexbacked.raw.SectionAnnotator
            @Nonnull
            public String getItemName() {
                return "annotation_item";
            }

            @Override // com.android.tools.smali.dexlib2.dexbacked.raw.SectionAnnotator
            protected void annotateItem(@Nonnull AnnotatedBytes out, int itemIndex, @Nullable String itemIdentity) {
                int visibility = this.dexFile.getBuffer().readUbyte(out.getCursor());
                out.annotate(1, "visibility = %d: %s", Integer.valueOf(visibility), AnnotationItem.getAnnotationVisibility(visibility));
                DexReader reader = this.dexFile.getBuffer().readerAt(out.getCursor());
                EncodedValue.annotateEncodedAnnotation(this.dexFile, out, reader);
            }
        };
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static String getAnnotationVisibility(int visibility) {
        switch (visibility) {
            case 0:
                return "build";
            case 1:
                return "runtime";
            case 2:
                return "system";
            default:
                return "invalid visibility";
        }
    }

    public static String getReferenceAnnotation(@Nonnull DexBackedDexFile dexFile, int annotationItemOffset) {
        try {
            DexReader reader = dexFile.getDataBuffer().readerAt(annotationItemOffset);
            reader.readUbyte();
            int typeIndex = reader.readSmallUleb128();
            String annotationType = (String) dexFile.getTypeSection().get(typeIndex);
            return String.format("annotation_item[0x%x]: %s", Integer.valueOf(annotationItemOffset), annotationType);
        } catch (Exception ex) {
            ex.printStackTrace(System.err);
            return String.format("annotation_item[0x%x]", Integer.valueOf(annotationItemOffset));
        }
    }
}
