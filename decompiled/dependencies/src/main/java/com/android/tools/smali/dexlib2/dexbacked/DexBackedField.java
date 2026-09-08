package com.android.tools.smali.dexlib2.dexbacked;

import com.android.tools.smali.dexlib2.HiddenApiRestriction;
import com.android.tools.smali.dexlib2.base.reference.BaseFieldReference;
import com.android.tools.smali.dexlib2.dexbacked.reference.DexBackedFieldReference;
import com.android.tools.smali.dexlib2.dexbacked.util.AnnotationsDirectory;
import com.android.tools.smali.dexlib2.dexbacked.util.EncodedArrayItemIterator;
import com.android.tools.smali.dexlib2.dexbacked.value.DexBackedEncodedValue;
import com.android.tools.smali.dexlib2.iface.ClassDef;
import com.android.tools.smali.dexlib2.iface.Field;
import com.android.tools.smali.dexlib2.iface.value.EncodedValue;
import com.google.common.collect.ImmutableSet;
import java.util.Collection;
import java.util.EnumSet;
import java.util.Set;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/* loaded from: classes.dex */
public class DexBackedField extends BaseFieldReference implements Field {
    public final int accessFlags;
    public final int annotationSetOffset;

    @Nonnull
    public final ClassDef classDef;

    @Nonnull
    public final DexBackedDexFile dexFile;
    private int fieldIdItemOffset;
    public final int fieldIndex;
    private final int hiddenApiRestrictions;

    @Nullable
    public final EncodedValue initialValue;
    private final int initialValueOffset;
    private final int startOffset;

    public DexBackedField(@Nonnull DexBackedDexFile dexFile, @Nonnull DexReader reader, @Nonnull DexBackedClassDef classDef, int previousFieldIndex, @Nonnull EncodedArrayItemIterator staticInitialValueIterator, @Nonnull AnnotationsDirectory.AnnotationIterator annotationIterator, int hiddenApiRestrictions) {
        this.dexFile = dexFile;
        this.classDef = classDef;
        this.startOffset = reader.getOffset();
        int fieldIndexDiff = reader.readLargeUleb128();
        int i = fieldIndexDiff + previousFieldIndex;
        this.fieldIndex = i;
        this.accessFlags = reader.readSmallUleb128();
        this.annotationSetOffset = annotationIterator.seekTo(i);
        this.initialValueOffset = staticInitialValueIterator.getReaderOffset();
        this.initialValue = staticInitialValueIterator.getNextOrNull();
        this.hiddenApiRestrictions = hiddenApiRestrictions;
    }

    public DexBackedField(@Nonnull DexBackedDexFile dexFile, @Nonnull DexReader reader, @Nonnull DexBackedClassDef classDef, int previousFieldIndex, @Nonnull AnnotationsDirectory.AnnotationIterator annotationIterator, int hiddenApiRestrictions) {
        this.dexFile = dexFile;
        this.classDef = classDef;
        this.startOffset = reader.getOffset();
        int fieldIndexDiff = reader.readLargeUleb128();
        int i = fieldIndexDiff + previousFieldIndex;
        this.fieldIndex = i;
        this.accessFlags = reader.readSmallUleb128();
        this.annotationSetOffset = annotationIterator.seekTo(i);
        this.initialValueOffset = 0;
        this.initialValue = null;
        this.hiddenApiRestrictions = hiddenApiRestrictions;
    }

    @Override // com.android.tools.smali.dexlib2.iface.reference.FieldReference, com.android.tools.smali.dexlib2.iface.Field, com.android.tools.smali.dexlib2.iface.Member
    @Nonnull
    public String getName() {
        return (String) this.dexFile.getStringSection().get(this.dexFile.getBuffer().readSmallUint(getFieldIdItemOffset() + 4));
    }

    @Override // com.android.tools.smali.dexlib2.iface.reference.FieldReference, com.android.tools.smali.dexlib2.iface.Field
    @Nonnull
    public String getType() {
        return (String) this.dexFile.getTypeSection().get(this.dexFile.getBuffer().readUshort(getFieldIdItemOffset() + 2));
    }

    @Override // com.android.tools.smali.dexlib2.iface.reference.FieldReference, com.android.tools.smali.dexlib2.iface.Field, com.android.tools.smali.dexlib2.iface.Member
    @Nonnull
    public String getDefiningClass() {
        return this.classDef.getType();
    }

    @Override // com.android.tools.smali.dexlib2.iface.Field, com.android.tools.smali.dexlib2.iface.Member
    public int getAccessFlags() {
        return this.accessFlags;
    }

    @Override // com.android.tools.smali.dexlib2.iface.Field
    @Nullable
    public EncodedValue getInitialValue() {
        return this.initialValue;
    }

    @Override // com.android.tools.smali.dexlib2.iface.Field, com.android.tools.smali.dexlib2.iface.Annotatable
    @Nonnull
    public Set<? extends DexBackedAnnotation> getAnnotations() {
        return AnnotationsDirectory.getAnnotations(this.dexFile, this.annotationSetOffset);
    }

    @Override // com.android.tools.smali.dexlib2.iface.Field, com.android.tools.smali.dexlib2.iface.Member
    @Nonnull
    public Set<HiddenApiRestriction> getHiddenApiRestrictions() {
        int i = this.hiddenApiRestrictions;
        if (i == 7) {
            return ImmutableSet.of();
        }
        return EnumSet.copyOf((Collection) HiddenApiRestriction.getAllFlags(i));
    }

    public static void skipFields(@Nonnull DexReader reader, int count) {
        for (int i = 0; i < count; i++) {
            reader.skipUleb128();
            reader.skipUleb128();
        }
    }

    private int getFieldIdItemOffset() {
        if (this.fieldIdItemOffset == 0) {
            this.fieldIdItemOffset = this.dexFile.getFieldSection().getOffset(this.fieldIndex);
        }
        return this.fieldIdItemOffset;
    }

    public int getSize() {
        DexReader reader = this.dexFile.getBuffer().readerAt(this.startOffset);
        reader.readLargeUleb128();
        reader.readSmallUleb128();
        int size = 0 + (reader.getOffset() - this.startOffset);
        Set<? extends DexBackedAnnotation> annotations = getAnnotations();
        if (!annotations.isEmpty()) {
            size += 8;
        }
        int i = this.initialValueOffset;
        if (i > 0) {
            reader.setOffset(i);
            if (this.initialValue != null) {
                DexBackedEncodedValue.skipFrom(reader);
                size += reader.getOffset() - this.initialValueOffset;
            }
        }
        DexBackedFieldReference fieldRef = new DexBackedFieldReference(this.dexFile, this.fieldIndex);
        return size + fieldRef.getSize();
    }
}
