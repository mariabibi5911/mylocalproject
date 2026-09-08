package com.android.tools.smali.dexlib2.dexbacked.raw;

import com.android.tools.smali.dexlib2.dexbacked.DexBackedDexFile;
import com.android.tools.smali.dexlib2.dexbacked.raw.util.DexAnnotator;
import com.android.tools.smali.dexlib2.util.AnnotatedBytes;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/* loaded from: classes.dex */
public class AnnotationSetRefList {
    public static final int LIST_OFFSET = 4;
    public static final int SIZE_OFFSET = 0;

    @Nonnull
    public static SectionAnnotator makeAnnotator(@Nonnull DexAnnotator annotator, @Nonnull MapItem mapItem) {
        return new SectionAnnotator(annotator, mapItem) { // from class: com.android.tools.smali.dexlib2.dexbacked.raw.AnnotationSetRefList.1
            @Override // com.android.tools.smali.dexlib2.dexbacked.raw.SectionAnnotator
            @Nonnull
            public String getItemName() {
                return "annotation_set_ref_list";
            }

            @Override // com.android.tools.smali.dexlib2.dexbacked.raw.SectionAnnotator
            protected void annotateItem(@Nonnull AnnotatedBytes out, int itemIndex, @Nullable String itemIdentity) {
                int size = this.dexFile.getBuffer().readSmallUint(out.getCursor());
                out.annotate(4, "size = %d", Integer.valueOf(size));
                for (int i = 0; i < size; i++) {
                    int annotationSetOffset = this.dexFile.getBuffer().readSmallUint(out.getCursor());
                    out.annotate(4, "annotation_set_item[0x%x]", Integer.valueOf(annotationSetOffset));
                }
            }

            @Override // com.android.tools.smali.dexlib2.dexbacked.raw.SectionAnnotator
            public int getItemAlignment() {
                return 4;
            }
        };
    }

    @Nonnull
    public static String getReferenceAnnotation(@Nonnull DexBackedDexFile dexFile, int annotationSetRefListOffset) {
        return annotationSetRefListOffset == 0 ? "annotation_set_ref_list[NO_OFFSET]" : String.format("annotation_set_ref_list[0x%x]", Integer.valueOf(annotationSetRefListOffset));
    }
}
