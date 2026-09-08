package com.android.tools.smali.dexlib2.dexbacked.raw;

import com.android.tools.smali.dexlib2.MethodHandleType;
import com.android.tools.smali.dexlib2.dexbacked.raw.util.DexAnnotator;
import com.android.tools.smali.dexlib2.util.AnnotatedBytes;
import com.android.tools.smali.util.ExceptionWithContext;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/* loaded from: classes.dex */
public class MethodHandleItem {
    public static final int ITEM_SIZE = 8;
    public static final int MEMBER_ID_OFFSET = 4;
    public static final int METHOD_HANDLE_TYPE_OFFSET = 0;

    @Nonnull
    public static SectionAnnotator makeAnnotator(@Nonnull DexAnnotator annotator, @Nonnull MapItem mapItem) {
        return new SectionAnnotator(annotator, mapItem) { // from class: com.android.tools.smali.dexlib2.dexbacked.raw.MethodHandleItem.1
            @Override // com.android.tools.smali.dexlib2.dexbacked.raw.SectionAnnotator
            @Nonnull
            public String getItemName() {
                return "method_handle_item";
            }

            @Override // com.android.tools.smali.dexlib2.dexbacked.raw.SectionAnnotator
            protected void annotateItem(@Nonnull AnnotatedBytes out, int itemIndex, @Nullable String itemIdentity) {
                String fieldOrMethodDescriptor;
                int methodHandleType = this.dexFile.getBuffer().readUshort(out.getCursor());
                out.annotate(2, "type = %s", MethodHandleType.toString(methodHandleType));
                out.annotate(2, "unused", new Object[0]);
                int fieldOrMethodId = this.dexFile.getBuffer().readUshort(out.getCursor());
                switch (methodHandleType) {
                    case 0:
                    case 1:
                    case 2:
                    case 3:
                        fieldOrMethodDescriptor = FieldIdItem.getReferenceAnnotation(this.dexFile, fieldOrMethodId);
                        break;
                    case 4:
                    case 5:
                    case 6:
                    case 7:
                    case 8:
                        fieldOrMethodDescriptor = MethodIdItem.getReferenceAnnotation(this.dexFile, fieldOrMethodId);
                        break;
                    default:
                        throw new ExceptionWithContext("Invalid method handle type: %d", Integer.valueOf(methodHandleType));
                }
                out.annotate(2, "field_or_method_id = %s", fieldOrMethodDescriptor);
                out.annotate(2, "unused", new Object[0]);
            }
        };
    }
}
