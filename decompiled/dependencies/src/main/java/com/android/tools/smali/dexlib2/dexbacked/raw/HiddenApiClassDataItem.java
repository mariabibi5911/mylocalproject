package com.android.tools.smali.dexlib2.dexbacked.raw;

import com.android.tools.smali.dexlib2.HiddenApiRestriction;
import com.android.tools.smali.dexlib2.dexbacked.DexBuffer;
import com.android.tools.smali.dexlib2.dexbacked.DexReader;
import com.android.tools.smali.dexlib2.dexbacked.raw.util.DexAnnotator;
import com.android.tools.smali.dexlib2.iface.ClassDef;
import com.android.tools.smali.dexlib2.iface.Field;
import com.android.tools.smali.dexlib2.iface.Method;
import com.android.tools.smali.dexlib2.util.AnnotatedBytes;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/* loaded from: classes.dex */
public class HiddenApiClassDataItem {
    public static final int OFFSETS_LIST_OFFSET = 4;
    public static final int OFFSET_ITEM_SIZE = 4;
    public static final int SIZE_OFFSET = 0;

    @Nonnull
    public static SectionAnnotator makeAnnotator(@Nonnull DexAnnotator annotator, @Nonnull MapItem mapItem) {
        return new SectionAnnotator(annotator, mapItem) { // from class: com.android.tools.smali.dexlib2.dexbacked.raw.HiddenApiClassDataItem.1
            @Override // com.android.tools.smali.dexlib2.dexbacked.raw.SectionAnnotator
            @Nonnull
            public String getItemName() {
                return "hiddenapi_class_data_item";
            }

            @Override // com.android.tools.smali.dexlib2.dexbacked.raw.SectionAnnotator
            protected void annotateItem(@Nonnull AnnotatedBytes out, int itemIndex, @Nullable String itemIdentity) {
                AnonymousClass1 anonymousClass1 = this;
                int startOffset = out.getCursor();
                int i = 1;
                int i2 = 0;
                int i3 = 4;
                out.annotate(4, "size = 0x%x", Integer.valueOf(anonymousClass1.dexFile.getDataBuffer().readSmallUint(out.getCursor())));
                int index = 0;
                for (ClassDef classDef : anonymousClass1.dexFile.getClasses()) {
                    Object[] objArr = new Object[2];
                    objArr[i2] = Integer.valueOf(index);
                    objArr[i] = classDef;
                    out.annotate(i2, "[%d] %s", objArr);
                    out.indent();
                    int offset = anonymousClass1.dexFile.getDataBuffer().readSmallUint(out.getCursor());
                    if (offset == 0) {
                        Object[] objArr2 = new Object[i];
                        objArr2[i2] = Integer.valueOf(offset);
                        out.annotate(i3, "offset = 0x%x", objArr2);
                    } else {
                        Object[] objArr3 = new Object[2];
                        objArr3[i2] = Integer.valueOf(offset);
                        objArr3[i] = Integer.valueOf(startOffset + offset);
                        out.annotate(i3, "offset = 0x%x (absolute offset: 0x%x)", objArr3);
                    }
                    int nextOffset = out.getCursor();
                    if (offset > 0) {
                        out.deindent();
                        out.moveTo(startOffset + offset);
                        DexReader<? extends DexBuffer> reader = anonymousClass1.dexFile.getBuffer().readerAt(out.getCursor());
                        for (Field field : classDef.getStaticFields()) {
                            Object[] objArr4 = new Object[i];
                            objArr4[i2] = field;
                            out.annotate(i2, "%s:", objArr4);
                            out.indent();
                            int restrictions = reader.readSmallUleb128();
                            out.annotateTo(reader.getOffset(), "restriction = 0x%x: %s", Integer.valueOf(restrictions), HiddenApiRestriction.formatHiddenRestrictions(restrictions));
                            out.deindent();
                            i = 1;
                            i2 = 0;
                        }
                        for (Field field2 : classDef.getInstanceFields()) {
                            out.annotate(0, "%s:", field2);
                            out.indent();
                            int restrictions2 = reader.readSmallUleb128();
                            out.annotateTo(reader.getOffset(), "restriction = 0x%x: %s", Integer.valueOf(restrictions2), HiddenApiRestriction.formatHiddenRestrictions(restrictions2));
                            out.deindent();
                        }
                        for (Method method : classDef.getDirectMethods()) {
                            out.annotate(0, "%s:", method);
                            out.indent();
                            int restrictions3 = reader.readSmallUleb128();
                            out.annotateTo(reader.getOffset(), "restriction = 0x%x: %s", Integer.valueOf(restrictions3), HiddenApiRestriction.formatHiddenRestrictions(restrictions3));
                            out.deindent();
                        }
                        for (Method method2 : classDef.getVirtualMethods()) {
                            out.annotate(0, "%s:", method2);
                            out.indent();
                            int restrictions4 = reader.readSmallUleb128();
                            out.annotateTo(reader.getOffset(), "restriction = 0x%x: %s", Integer.valueOf(restrictions4), HiddenApiRestriction.formatHiddenRestrictions(restrictions4));
                            out.deindent();
                        }
                        out.indent();
                    }
                    out.moveTo(nextOffset);
                    out.deindent();
                    index++;
                    i = 1;
                    i3 = 4;
                    i2 = 0;
                    anonymousClass1 = this;
                }
            }
        };
    }
}
