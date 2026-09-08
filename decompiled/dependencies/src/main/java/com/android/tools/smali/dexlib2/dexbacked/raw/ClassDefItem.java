package com.android.tools.smali.dexlib2.dexbacked.raw;

import com.android.tools.smali.dexlib2.AccessFlags;
import com.android.tools.smali.dexlib2.dexbacked.DexBackedDexFile;
import com.android.tools.smali.dexlib2.dexbacked.raw.util.DexAnnotator;
import com.android.tools.smali.dexlib2.util.AnnotatedBytes;
import com.google.common.base.Joiner;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/* loaded from: classes.dex */
public class ClassDefItem {
    public static final int ACCESS_FLAGS_OFFSET = 4;
    public static final int ANNOTATIONS_OFFSET = 20;
    public static final int CLASS_DATA_OFFSET = 24;
    public static final int CLASS_OFFSET = 0;
    public static final int INTERFACES_OFFSET = 12;
    public static final int ITEM_SIZE = 32;
    public static final int SOURCE_FILE_OFFSET = 16;
    public static final int STATIC_VALUES_OFFSET = 28;
    public static final int SUPERCLASS_OFFSET = 8;

    @Nonnull
    public static SectionAnnotator makeAnnotator(@Nonnull DexAnnotator annotator, @Nonnull MapItem mapItem) {
        return new SectionAnnotator(annotator, mapItem) { // from class: com.android.tools.smali.dexlib2.dexbacked.raw.ClassDefItem.1
            private SectionAnnotator classDataAnnotator = null;

            @Override // com.android.tools.smali.dexlib2.dexbacked.raw.SectionAnnotator
            public void annotateSection(@Nonnull AnnotatedBytes out) {
                this.classDataAnnotator = this.annotator.getAnnotator(8192);
                super.annotateSection(out);
            }

            @Override // com.android.tools.smali.dexlib2.dexbacked.raw.SectionAnnotator
            @Nonnull
            public String getItemName() {
                return "class_def_item";
            }

            @Override // com.android.tools.smali.dexlib2.dexbacked.raw.SectionAnnotator
            protected void annotateItem(@Nonnull AnnotatedBytes out, int itemIndex, @Nullable String itemIdentity) {
                int classIndex = this.dexFile.getBuffer().readSmallUint(out.getCursor());
                out.annotate(4, "class_idx = %s", TypeIdItem.getReferenceAnnotation(this.dexFile, classIndex));
                int accessFlags = this.dexFile.getBuffer().readInt(out.getCursor());
                out.annotate(4, "access_flags = 0x%x: %s", Integer.valueOf(accessFlags), Joiner.on('|').join(AccessFlags.getAccessFlagsForClass(accessFlags)));
                int superclassIndex = this.dexFile.getBuffer().readOptionalUint(out.getCursor());
                out.annotate(4, "superclass_idx = %s", TypeIdItem.getOptionalReferenceAnnotation(this.dexFile, superclassIndex));
                int interfacesOffset = this.dexFile.getBuffer().readSmallUint(out.getCursor());
                out.annotate(4, "interfaces_off = %s", TypeListItem.getReferenceAnnotation(this.dexFile, interfacesOffset));
                int sourceFileIdx = this.dexFile.getBuffer().readOptionalUint(out.getCursor());
                out.annotate(4, "source_file_idx = %s", StringIdItem.getOptionalReferenceAnnotation(this.dexFile, sourceFileIdx));
                int annotationsOffset = this.dexFile.getBuffer().readSmallUint(out.getCursor());
                if (annotationsOffset == 0) {
                    out.annotate(4, "annotations_off = annotations_directory_item[NO_OFFSET]", new Object[0]);
                } else {
                    out.annotate(4, "annotations_off = annotations_directory_item[0x%x]", Integer.valueOf(annotationsOffset));
                }
                int classDataOffset = this.dexFile.getBuffer().readSmallUint(out.getCursor());
                if (classDataOffset == 0) {
                    out.annotate(4, "class_data_off = class_data_item[NO_OFFSET]", new Object[0]);
                } else {
                    out.annotate(4, "class_data_off = class_data_item[0x%x]", Integer.valueOf(classDataOffset));
                    addClassDataIdentity(classDataOffset, (String) this.dexFile.getTypeSection().get(classIndex));
                }
                int staticValuesOffset = this.dexFile.getBuffer().readSmallUint(out.getCursor());
                if (staticValuesOffset == 0) {
                    out.annotate(4, "static_values_off = encoded_array_item[NO_OFFSET]", new Object[0]);
                } else {
                    out.annotate(4, "static_values_off = encoded_array_item[0x%x]", Integer.valueOf(staticValuesOffset));
                }
            }

            private void addClassDataIdentity(int classDataOffset, String classType) {
                SectionAnnotator sectionAnnotator = this.classDataAnnotator;
                if (sectionAnnotator != null) {
                    sectionAnnotator.setItemIdentity(classDataOffset, classType);
                }
            }
        };
    }

    @Nonnull
    public static String asString(@Nonnull DexBackedDexFile dexFile, int classIndex) {
        int offset = dexFile.getClassSection().getOffset(classIndex);
        int typeIndex = dexFile.getBuffer().readSmallUint(offset + 0);
        return (String) dexFile.getTypeSection().get(typeIndex);
    }

    public static String[] getClasses(@Nonnull DexBackedDexFile dexFile) {
        MapItem mapItem = dexFile.getMapItemForSection(6);
        if (mapItem == null) {
            return new String[0];
        }
        int classCount = mapItem.getItemCount();
        String[] ret = new String[classCount];
        for (int i = 0; i < classCount; i++) {
            ret[i] = asString(dexFile, i);
        }
        return ret;
    }
}
