package com.android.tools.smali.dexlib2.dexbacked.raw;

import com.android.tools.smali.dexlib2.dexbacked.DexBackedDexFile;
import com.android.tools.smali.dexlib2.dexbacked.raw.util.DexAnnotator;
import com.android.tools.smali.dexlib2.util.AnnotatedBytes;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/* loaded from: classes.dex */
public class FieldIdItem {
    public static final int CLASS_OFFSET = 0;
    public static final int ITEM_SIZE = 8;
    public static final int NAME_OFFSET = 4;
    public static final int TYPE_OFFSET = 2;

    @Nonnull
    public static SectionAnnotator makeAnnotator(@Nonnull DexAnnotator annotator, @Nonnull MapItem mapItem) {
        return new SectionAnnotator(annotator, mapItem) { // from class: com.android.tools.smali.dexlib2.dexbacked.raw.FieldIdItem.1
            @Override // com.android.tools.smali.dexlib2.dexbacked.raw.SectionAnnotator
            @Nonnull
            public String getItemName() {
                return "field_id_item";
            }

            @Override // com.android.tools.smali.dexlib2.dexbacked.raw.SectionAnnotator
            protected void annotateItem(@Nonnull AnnotatedBytes out, int itemIndex, @Nullable String itemIdentity) {
                int classIndex = this.dexFile.getBuffer().readUshort(out.getCursor());
                out.annotate(2, "class_idx = %s", TypeIdItem.getReferenceAnnotation(this.dexFile, classIndex));
                int typeIndex = this.dexFile.getBuffer().readUshort(out.getCursor());
                out.annotate(2, "return_type_idx = %s", TypeIdItem.getReferenceAnnotation(this.dexFile, typeIndex));
                int nameIndex = this.dexFile.getBuffer().readSmallUint(out.getCursor());
                out.annotate(4, "name_idx = %s", StringIdItem.getReferenceAnnotation(this.dexFile, nameIndex));
            }
        };
    }

    @Nonnull
    public static String asString(@Nonnull DexBackedDexFile dexFile, int fieldIndex) {
        int fieldOffset = dexFile.getFieldSection().getOffset(fieldIndex);
        int classIndex = dexFile.getBuffer().readUshort(fieldOffset + 0);
        String classType = (String) dexFile.getTypeSection().get(classIndex);
        int typeIndex = dexFile.getBuffer().readUshort(fieldOffset + 2);
        String fieldType = (String) dexFile.getTypeSection().get(typeIndex);
        int nameIndex = dexFile.getBuffer().readSmallUint(fieldOffset + 4);
        String fieldName = (String) dexFile.getStringSection().get(nameIndex);
        return String.format("%s->%s:%s", classType, fieldName, fieldType);
    }

    @Nonnull
    public static String getReferenceAnnotation(@Nonnull DexBackedDexFile dexFile, int fieldIndex) {
        try {
            String fieldString = asString(dexFile, fieldIndex);
            return String.format("field_id_item[%d]: %s", Integer.valueOf(fieldIndex), fieldString);
        } catch (Exception ex) {
            ex.printStackTrace(System.err);
            return String.format("field_id_item[%d]", Integer.valueOf(fieldIndex));
        }
    }

    public static String[] getFields(@Nonnull DexBackedDexFile dexFile) {
        MapItem mapItem = dexFile.getMapItemForSection(4);
        if (mapItem == null) {
            return new String[0];
        }
        int fieldCount = mapItem.getItemCount();
        String[] ret = new String[fieldCount];
        for (int i = 0; i < fieldCount; i++) {
            ret[i] = asString(dexFile, i);
        }
        return ret;
    }
}
