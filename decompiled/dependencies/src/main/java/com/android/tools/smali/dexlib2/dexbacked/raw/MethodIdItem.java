package com.android.tools.smali.dexlib2.dexbacked.raw;

import com.android.tools.smali.dexlib2.dexbacked.DexBackedDexFile;
import com.android.tools.smali.dexlib2.dexbacked.raw.util.DexAnnotator;
import com.android.tools.smali.dexlib2.util.AnnotatedBytes;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/* loaded from: classes.dex */
public class MethodIdItem {
    public static final int CLASS_OFFSET = 0;
    public static final int ITEM_SIZE = 8;
    public static final int NAME_OFFSET = 4;
    public static final int PROTO_OFFSET = 2;

    @Nonnull
    public static SectionAnnotator makeAnnotator(@Nonnull DexAnnotator annotator, @Nonnull MapItem mapItem) {
        return new SectionAnnotator(annotator, mapItem) { // from class: com.android.tools.smali.dexlib2.dexbacked.raw.MethodIdItem.1
            @Override // com.android.tools.smali.dexlib2.dexbacked.raw.SectionAnnotator
            @Nonnull
            public String getItemName() {
                return "method_id_item";
            }

            @Override // com.android.tools.smali.dexlib2.dexbacked.raw.SectionAnnotator
            public void annotateItem(@Nonnull AnnotatedBytes out, int itemIndex, @Nullable String itemIdentity) {
                int classIndex = this.dexFile.getBuffer().readUshort(out.getCursor());
                out.annotate(2, "class_idx = %s", TypeIdItem.getReferenceAnnotation(this.dexFile, classIndex));
                int protoIndex = this.dexFile.getBuffer().readUshort(out.getCursor());
                out.annotate(2, "proto_idx = %s", ProtoIdItem.getReferenceAnnotation(this.dexFile, protoIndex));
                int nameIndex = this.dexFile.getBuffer().readSmallUint(out.getCursor());
                out.annotate(4, "name_idx = %s", StringIdItem.getReferenceAnnotation(this.dexFile, nameIndex));
            }
        };
    }

    @Nonnull
    public static String asString(@Nonnull DexBackedDexFile dexFile, int methodIndex) {
        int methodOffset = dexFile.getMethodSection().getOffset(methodIndex);
        int classIndex = dexFile.getBuffer().readUshort(methodOffset + 0);
        String classType = (String) dexFile.getTypeSection().get(classIndex);
        int protoIndex = dexFile.getBuffer().readUshort(methodOffset + 2);
        String protoString = ProtoIdItem.asString(dexFile, protoIndex);
        int nameIndex = dexFile.getBuffer().readSmallUint(methodOffset + 4);
        String methodName = (String) dexFile.getStringSection().get(nameIndex);
        return String.format("%s->%s%s", classType, methodName, protoString);
    }

    @Nonnull
    public static String getReferenceAnnotation(@Nonnull DexBackedDexFile dexFile, int methodIndex) {
        try {
            String methodString = asString(dexFile, methodIndex);
            return String.format("method_id_item[%d]: %s", Integer.valueOf(methodIndex), methodString);
        } catch (Exception ex) {
            ex.printStackTrace(System.err);
            return String.format("method_id_item[%d]", Integer.valueOf(methodIndex));
        }
    }

    public static String[] getMethods(@Nonnull DexBackedDexFile dexFile) {
        MapItem mapItem = dexFile.getMapItemForSection(5);
        if (mapItem == null) {
            return new String[0];
        }
        int methodCount = mapItem.getItemCount();
        String[] ret = new String[methodCount];
        for (int i = 0; i < methodCount; i++) {
            ret[i] = asString(dexFile, i);
        }
        return ret;
    }
}
