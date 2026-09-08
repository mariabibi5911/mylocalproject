package com.android.tools.smali.dexlib2.dexbacked.raw;

import com.android.tools.smali.dexlib2.AccessFlags;
import com.android.tools.smali.dexlib2.dexbacked.DexBackedDexFile;
import com.android.tools.smali.dexlib2.dexbacked.DexReader;
import com.android.tools.smali.dexlib2.dexbacked.raw.util.DexAnnotator;
import com.android.tools.smali.dexlib2.util.AnnotatedBytes;
import com.google.common.base.Joiner;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/* loaded from: classes.dex */
public class ClassDataItem {
    @Nonnull
    public static SectionAnnotator makeAnnotator(@Nonnull DexAnnotator annotator, @Nonnull MapItem mapItem) {
        return new SectionAnnotator(annotator, mapItem) { // from class: com.android.tools.smali.dexlib2.dexbacked.raw.ClassDataItem.1
            private SectionAnnotator codeItemAnnotator = null;

            @Override // com.android.tools.smali.dexlib2.dexbacked.raw.SectionAnnotator
            public void annotateSection(@Nonnull AnnotatedBytes out) {
                this.codeItemAnnotator = this.annotator.getAnnotator(ItemType.CODE_ITEM);
                super.annotateSection(out);
            }

            @Override // com.android.tools.smali.dexlib2.dexbacked.raw.SectionAnnotator
            @Nonnull
            public String getItemName() {
                return "class_data_item";
            }

            @Override // com.android.tools.smali.dexlib2.dexbacked.raw.SectionAnnotator
            protected void annotateItem(@Nonnull AnnotatedBytes out, int itemIndex, @Nullable String itemIdentity) {
                DexReader reader = this.dexFile.getBuffer().readerAt(out.getCursor());
                int staticFieldsSize = reader.readSmallUleb128();
                out.annotateTo(reader.getOffset(), "static_fields_size = %d", Integer.valueOf(staticFieldsSize));
                int instanceFieldsSize = reader.readSmallUleb128();
                out.annotateTo(reader.getOffset(), "instance_fields_size = %d", Integer.valueOf(instanceFieldsSize));
                int directMethodsSize = reader.readSmallUleb128();
                out.annotateTo(reader.getOffset(), "direct_methods_size = %d", Integer.valueOf(directMethodsSize));
                int virtualMethodsSize = reader.readSmallUleb128();
                out.annotateTo(reader.getOffset(), "virtual_methods_size = %d", Integer.valueOf(virtualMethodsSize));
                int previousIndex = 0;
                if (staticFieldsSize > 0) {
                    out.annotate(0, "static_fields:", new Object[0]);
                    out.indent();
                    for (int i = 0; i < staticFieldsSize; i++) {
                        out.annotate(0, "static_field[%d]", Integer.valueOf(i));
                        out.indent();
                        previousIndex = annotateEncodedField(out, this.dexFile, reader, previousIndex);
                        out.deindent();
                    }
                    out.deindent();
                }
                if (instanceFieldsSize > 0) {
                    out.annotate(0, "instance_fields:", new Object[0]);
                    out.indent();
                    int previousIndex2 = 0;
                    for (int i2 = 0; i2 < instanceFieldsSize; i2++) {
                        out.annotate(0, "instance_field[%d]", Integer.valueOf(i2));
                        out.indent();
                        previousIndex2 = annotateEncodedField(out, this.dexFile, reader, previousIndex2);
                        out.deindent();
                    }
                    out.deindent();
                }
                if (directMethodsSize > 0) {
                    out.annotate(0, "direct_methods:", new Object[0]);
                    out.indent();
                    int previousIndex3 = 0;
                    for (int i3 = 0; i3 < directMethodsSize; i3++) {
                        out.annotate(0, "direct_method[%d]", Integer.valueOf(i3));
                        out.indent();
                        previousIndex3 = annotateEncodedMethod(out, this.dexFile, reader, previousIndex3);
                        out.deindent();
                    }
                    out.deindent();
                }
                if (virtualMethodsSize > 0) {
                    out.annotate(0, "virtual_methods:", new Object[0]);
                    out.indent();
                    int previousIndex4 = 0;
                    for (int i4 = 0; i4 < virtualMethodsSize; i4++) {
                        out.annotate(0, "virtual_method[%d]", Integer.valueOf(i4));
                        out.indent();
                        previousIndex4 = annotateEncodedMethod(out, this.dexFile, reader, previousIndex4);
                        out.deindent();
                    }
                    out.deindent();
                }
            }

            private int annotateEncodedField(@Nonnull AnnotatedBytes out, @Nonnull DexBackedDexFile dexFile, @Nonnull DexReader reader, int previousIndex) {
                int indexDelta = reader.readLargeUleb128();
                int fieldIndex = previousIndex + indexDelta;
                out.annotateTo(reader.getOffset(), "field_idx_diff = %d: %s", Integer.valueOf(indexDelta), FieldIdItem.getReferenceAnnotation(dexFile, fieldIndex));
                int accessFlags = reader.readSmallUleb128();
                out.annotateTo(reader.getOffset(), "access_flags = 0x%x: %s", Integer.valueOf(accessFlags), Joiner.on('|').join(AccessFlags.getAccessFlagsForField(accessFlags)));
                return fieldIndex;
            }

            private int annotateEncodedMethod(@Nonnull AnnotatedBytes out, @Nonnull DexBackedDexFile dexFile, @Nonnull DexReader reader, int previousIndex) {
                int indexDelta = reader.readLargeUleb128();
                int methodIndex = previousIndex + indexDelta;
                out.annotateTo(reader.getOffset(), "method_idx_diff = %d: %s", Integer.valueOf(indexDelta), MethodIdItem.getReferenceAnnotation(dexFile, methodIndex));
                int accessFlags = reader.readSmallUleb128();
                out.annotateTo(reader.getOffset(), "access_flags = 0x%x: %s", Integer.valueOf(accessFlags), Joiner.on('|').join(AccessFlags.getAccessFlagsForMethod(accessFlags)));
                int codeOffset = reader.readSmallUleb128();
                if (codeOffset == 0) {
                    out.annotateTo(reader.getOffset(), "code_off = code_item[NO_OFFSET]", new Object[0]);
                } else {
                    out.annotateTo(reader.getOffset(), "code_off = code_item[0x%x]", Integer.valueOf(codeOffset));
                    addCodeItemIdentity(codeOffset, MethodIdItem.asString(dexFile, methodIndex));
                }
                return methodIndex;
            }

            private void addCodeItemIdentity(int codeItemOffset, String methodString) {
                SectionAnnotator sectionAnnotator = this.codeItemAnnotator;
                if (sectionAnnotator != null) {
                    sectionAnnotator.setItemIdentity(codeItemOffset, methodString);
                }
            }
        };
    }
}
