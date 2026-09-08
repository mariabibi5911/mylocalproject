package com.android.tools.smali.dexlib2.dexbacked.raw;

import com.android.tools.smali.dexlib2.dexbacked.raw.util.DexAnnotator;
import com.android.tools.smali.dexlib2.util.AnnotatedBytes;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/* loaded from: classes.dex */
public class AnnotationDirectoryItem {
    public static final int ANNOTATED_METHOD_SIZE_OFFSET = 8;
    public static final int ANNOTATED_PARAMETERS_SIZE = 12;
    public static final int CLASS_ANNOTATIONS_OFFSET = 0;
    public static final int FIELD_SIZE_OFFSET = 4;

    @Nonnull
    public static SectionAnnotator makeAnnotator(@Nonnull DexAnnotator annotator, @Nonnull MapItem mapItem) {
        return new SectionAnnotator(annotator, mapItem) { // from class: com.android.tools.smali.dexlib2.dexbacked.raw.AnnotationDirectoryItem.1
            @Override // com.android.tools.smali.dexlib2.dexbacked.raw.SectionAnnotator
            @Nonnull
            public String getItemName() {
                return "annotation_directory_item";
            }

            @Override // com.android.tools.smali.dexlib2.dexbacked.raw.SectionAnnotator
            public int getItemAlignment() {
                return 4;
            }

            @Override // com.android.tools.smali.dexlib2.dexbacked.raw.SectionAnnotator
            protected void annotateItem(@Nonnull AnnotatedBytes out, int itemIndex, @Nullable String itemIdentity) {
                int classAnnotationsOffset = this.dexFile.getBuffer().readSmallUint(out.getCursor());
                out.annotate(4, "class_annotations_off = %s", AnnotationSetItem.getReferenceAnnotation(this.dexFile, classAnnotationsOffset));
                int fieldsSize = this.dexFile.getBuffer().readSmallUint(out.getCursor());
                out.annotate(4, "fields_size = %d", Integer.valueOf(fieldsSize));
                int annotatedMethodsSize = this.dexFile.getBuffer().readSmallUint(out.getCursor());
                out.annotate(4, "annotated_methods_size = %d", Integer.valueOf(annotatedMethodsSize));
                int annotatedParameterSize = this.dexFile.getBuffer().readSmallUint(out.getCursor());
                out.annotate(4, "annotated_parameters_size = %d", Integer.valueOf(annotatedParameterSize));
                if (fieldsSize > 0) {
                    out.annotate(0, "field_annotations:", new Object[0]);
                    out.indent();
                    for (int i = 0; i < fieldsSize; i++) {
                        out.annotate(0, "field_annotation[%d]", Integer.valueOf(i));
                        out.indent();
                        int fieldIndex = this.dexFile.getBuffer().readSmallUint(out.getCursor());
                        out.annotate(4, "%s", FieldIdItem.getReferenceAnnotation(this.dexFile, fieldIndex));
                        int annotationOffset = this.dexFile.getBuffer().readSmallUint(out.getCursor());
                        out.annotate(4, "%s", AnnotationSetItem.getReferenceAnnotation(this.dexFile, annotationOffset));
                        out.deindent();
                    }
                    out.deindent();
                }
                if (annotatedMethodsSize > 0) {
                    out.annotate(0, "method_annotations:", new Object[0]);
                    out.indent();
                    for (int i2 = 0; i2 < annotatedMethodsSize; i2++) {
                        out.annotate(0, "method_annotation[%d]", Integer.valueOf(i2));
                        out.indent();
                        int methodIndex = this.dexFile.getBuffer().readSmallUint(out.getCursor());
                        out.annotate(4, "%s", MethodIdItem.getReferenceAnnotation(this.dexFile, methodIndex));
                        int annotationOffset2 = this.dexFile.getBuffer().readSmallUint(out.getCursor());
                        out.annotate(4, "%s", AnnotationSetItem.getReferenceAnnotation(this.dexFile, annotationOffset2));
                        out.deindent();
                    }
                    out.deindent();
                }
                if (annotatedParameterSize > 0) {
                    out.annotate(0, "parameter_annotations:", new Object[0]);
                    out.indent();
                    for (int i3 = 0; i3 < annotatedParameterSize; i3++) {
                        out.annotate(0, "parameter_annotation[%d]", Integer.valueOf(i3));
                        out.indent();
                        int methodIndex2 = this.dexFile.getBuffer().readSmallUint(out.getCursor());
                        out.annotate(4, "%s", MethodIdItem.getReferenceAnnotation(this.dexFile, methodIndex2));
                        int annotationOffset3 = this.dexFile.getBuffer().readSmallUint(out.getCursor());
                        out.annotate(4, "%s", AnnotationSetRefList.getReferenceAnnotation(this.dexFile, annotationOffset3));
                        out.deindent();
                    }
                    out.deindent();
                }
            }
        };
    }
}
