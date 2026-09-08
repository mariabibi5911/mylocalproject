package com.android.tools.smali.dexlib2.writer.pool;

import com.android.tools.smali.dexlib2.iface.Field;
import com.android.tools.smali.dexlib2.iface.reference.FieldReference;
import com.android.tools.smali.dexlib2.writer.FieldSection;
import javax.annotation.Nonnull;

/* loaded from: classes.dex */
public class FieldPool extends BaseIndexPool<FieldReference> implements FieldSection<CharSequence, CharSequence, FieldReference, Field> {
    public FieldPool(@Nonnull DexPool dexPool) {
        super(dexPool);
    }

    public void intern(@Nonnull FieldReference field) {
        Integer prev = (Integer) this.internedItems.put(field, 0);
        if (prev == null) {
            ((TypePool) this.dexPool.typeSection).intern(field.getDefiningClass());
            ((StringPool) this.dexPool.stringSection).intern(field.getName());
            ((TypePool) this.dexPool.typeSection).intern(field.getType());
        }
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // com.android.tools.smali.dexlib2.writer.FieldSection
    @Nonnull
    public CharSequence getDefiningClass(@Nonnull FieldReference fieldReference) {
        return fieldReference.getDefiningClass();
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // com.android.tools.smali.dexlib2.writer.FieldSection
    @Nonnull
    public CharSequence getFieldType(@Nonnull FieldReference fieldReference) {
        return fieldReference.getType();
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // com.android.tools.smali.dexlib2.writer.FieldSection
    @Nonnull
    public CharSequence getName(@Nonnull FieldReference fieldReference) {
        return fieldReference.getName();
    }

    @Override // com.android.tools.smali.dexlib2.writer.FieldSection
    public int getFieldIndex(@Nonnull Field field) {
        return getItemIndex(field);
    }
}
