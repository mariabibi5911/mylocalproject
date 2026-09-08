package com.android.tools.smali.dexlib2.dexbacked.raw;

import com.android.tools.smali.dexlib2.dexbacked.DexBackedDexFile;
import com.android.tools.smali.dexlib2.dexbacked.raw.util.DexAnnotator;
import com.android.tools.smali.dexlib2.util.AnnotatedBytes;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/* loaded from: classes.dex */
public class ProtoIdItem {
    public static final int ITEM_SIZE = 12;
    public static final int PARAMETERS_OFFSET = 8;
    public static final int RETURN_TYPE_OFFSET = 4;
    public static final int SHORTY_OFFSET = 0;

    @Nonnull
    public static SectionAnnotator makeAnnotator(@Nonnull DexAnnotator annotator, @Nonnull MapItem mapItem) {
        return new SectionAnnotator(annotator, mapItem) { // from class: com.android.tools.smali.dexlib2.dexbacked.raw.ProtoIdItem.1
            @Override // com.android.tools.smali.dexlib2.dexbacked.raw.SectionAnnotator
            @Nonnull
            public String getItemName() {
                return "proto_id_item";
            }

            @Override // com.android.tools.smali.dexlib2.dexbacked.raw.SectionAnnotator
            protected void annotateItem(@Nonnull AnnotatedBytes out, int itemIndex, @Nullable String itemIdentity) {
                int shortyIndex = this.dexFile.getBuffer().readSmallUint(out.getCursor());
                out.annotate(4, "shorty_idx = %s", StringIdItem.getReferenceAnnotation(this.dexFile, shortyIndex));
                int returnTypeIndex = this.dexFile.getBuffer().readSmallUint(out.getCursor());
                out.annotate(4, "return_type_idx = %s", TypeIdItem.getReferenceAnnotation(this.dexFile, returnTypeIndex));
                int parametersOffset = this.dexFile.getBuffer().readSmallUint(out.getCursor());
                out.annotate(4, "parameters_off = %s", TypeListItem.getReferenceAnnotation(this.dexFile, parametersOffset));
            }
        };
    }

    @Nonnull
    public static String getReferenceAnnotation(@Nonnull DexBackedDexFile dexFile, int protoIndex) {
        try {
            String protoString = asString(dexFile, protoIndex);
            return String.format("proto_id_item[%d]: %s", Integer.valueOf(protoIndex), protoString);
        } catch (Exception ex) {
            ex.printStackTrace(System.err);
            return String.format("proto_id_item[%d]", Integer.valueOf(protoIndex));
        }
    }

    @Nonnull
    public static String asString(@Nonnull DexBackedDexFile dexFile, int protoIndex) {
        int offset = dexFile.getProtoSection().getOffset(protoIndex);
        StringBuilder sb = new StringBuilder();
        sb.append("(");
        int parametersOffset = dexFile.getBuffer().readSmallUint(offset + 8);
        sb.append(TypeListItem.asString(dexFile, parametersOffset));
        sb.append(")");
        int returnTypeIndex = dexFile.getBuffer().readSmallUint(offset + 4);
        String returnType = (String) dexFile.getTypeSection().get(returnTypeIndex);
        sb.append(returnType);
        return sb.toString();
    }

    public static String[] getProtos(@Nonnull DexBackedDexFile dexFile) {
        MapItem mapItem = dexFile.getMapItemForSection(3);
        if (mapItem == null) {
            return new String[0];
        }
        int protoCount = mapItem.getItemCount();
        String[] ret = new String[protoCount];
        for (int i = 0; i < protoCount; i++) {
            ret[i] = asString(dexFile, i);
        }
        return ret;
    }
}
