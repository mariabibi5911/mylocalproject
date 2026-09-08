package com.android.tools.smali.dexlib2.writer.builder;

import com.android.tools.smali.dexlib2.iface.reference.FieldReference;
import com.android.tools.smali.dexlib2.immutable.reference.ImmutableFieldReference;
import com.android.tools.smali.dexlib2.writer.FieldSection;
import com.google.common.collect.Maps;
import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentMap;
import javax.annotation.Nonnull;

/* loaded from: classes.dex */
public class BuilderFieldPool extends BaseBuilderPool implements FieldSection<BuilderStringReference, BuilderTypeReference, BuilderFieldReference, BuilderField> {

    @Nonnull
    private final ConcurrentMap<FieldReference, BuilderFieldReference> internedItems;

    public BuilderFieldPool(@Nonnull DexBuilder dexBuilder) {
        super(dexBuilder);
        this.internedItems = Maps.newConcurrentMap();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Nonnull
    public BuilderFieldReference internField(@Nonnull String definingClass, String name, String type) {
        ImmutableFieldReference fieldReference = new ImmutableFieldReference(definingClass, name, type);
        return internField(fieldReference);
    }

    @Nonnull
    public BuilderFieldReference internField(@Nonnull FieldReference fieldReference) {
        BuilderFieldReference ret = this.internedItems.get(fieldReference);
        if (ret != null) {
            return ret;
        }
        BuilderFieldReference dexPoolFieldReference = new BuilderFieldReference(((BuilderTypePool) this.dexBuilder.typeSection).internType(fieldReference.getDefiningClass()), ((BuilderStringPool) this.dexBuilder.stringSection).internString(fieldReference.getName()), ((BuilderTypePool) this.dexBuilder.typeSection).internType(fieldReference.getType()));
        BuilderFieldReference ret2 = this.internedItems.putIfAbsent(dexPoolFieldReference, dexPoolFieldReference);
        return ret2 == null ? dexPoolFieldReference : ret2;
    }

    @Override // com.android.tools.smali.dexlib2.writer.FieldSection
    @Nonnull
    public BuilderTypeReference getDefiningClass(@Nonnull BuilderFieldReference key) {
        return key.definingClass;
    }

    @Override // com.android.tools.smali.dexlib2.writer.FieldSection
    @Nonnull
    public BuilderTypeReference getFieldType(@Nonnull BuilderFieldReference key) {
        return key.fieldType;
    }

    @Override // com.android.tools.smali.dexlib2.writer.FieldSection
    @Nonnull
    public BuilderStringReference getName(@Nonnull BuilderFieldReference key) {
        return key.name;
    }

    @Override // com.android.tools.smali.dexlib2.writer.FieldSection
    public int getFieldIndex(@Nonnull BuilderField builderField) {
        return builderField.fieldReference.getIndex();
    }

    @Override // com.android.tools.smali.dexlib2.writer.IndexSection
    public int getItemIndex(@Nonnull BuilderFieldReference key) {
        return key.index;
    }

    @Override // com.android.tools.smali.dexlib2.writer.IndexSection
    @Nonnull
    public Collection<? extends Map.Entry<? extends BuilderFieldReference, Integer>> getItems() {
        return new BuilderMapEntryCollection<BuilderFieldReference>(this.internedItems.values()) { // from class: com.android.tools.smali.dexlib2.writer.builder.BuilderFieldPool.1
            /* JADX INFO: Access modifiers changed from: protected */
            @Override // com.android.tools.smali.dexlib2.writer.builder.BuilderMapEntryCollection
            public int getValue(@Nonnull BuilderFieldReference key) {
                return key.index;
            }

            /* JADX INFO: Access modifiers changed from: protected */
            @Override // com.android.tools.smali.dexlib2.writer.builder.BuilderMapEntryCollection
            public int setValue(@Nonnull BuilderFieldReference key, int value) {
                int prev = key.index;
                key.index = value;
                return prev;
            }
        };
    }

    @Override // com.android.tools.smali.dexlib2.writer.IndexSection
    public int getItemCount() {
        return this.internedItems.size();
    }
}
