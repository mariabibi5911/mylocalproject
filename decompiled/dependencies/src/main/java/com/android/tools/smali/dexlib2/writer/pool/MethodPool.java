package com.android.tools.smali.dexlib2.writer.pool;

import com.android.tools.smali.dexlib2.iface.reference.MethodProtoReference;
import com.android.tools.smali.dexlib2.iface.reference.MethodReference;
import com.android.tools.smali.dexlib2.writer.MethodSection;
import javax.annotation.Nonnull;

/* loaded from: classes.dex */
public class MethodPool extends BaseIndexPool<MethodReference> implements MethodSection<CharSequence, CharSequence, MethodProtoReference, MethodReference, PoolMethod> {
    public MethodPool(@Nonnull DexPool dexPool) {
        super(dexPool);
    }

    public void intern(@Nonnull MethodReference method) {
        Integer prev = (Integer) this.internedItems.put(method, 0);
        if (prev == null) {
            ((TypePool) this.dexPool.typeSection).intern(method.getDefiningClass());
            ((ProtoPool) this.dexPool.protoSection).intern(new PoolMethodProto(method));
            ((StringPool) this.dexPool.stringSection).intern(method.getName());
        }
    }

    @Override // com.android.tools.smali.dexlib2.writer.MethodSection
    @Nonnull
    public MethodReference getMethodReference(@Nonnull PoolMethod poolMethod) {
        return poolMethod;
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // com.android.tools.smali.dexlib2.writer.MethodSection
    @Nonnull
    public CharSequence getDefiningClass(@Nonnull MethodReference methodReference) {
        return methodReference.getDefiningClass();
    }

    @Override // com.android.tools.smali.dexlib2.writer.MethodSection
    @Nonnull
    public MethodProtoReference getPrototype(@Nonnull MethodReference methodReference) {
        return new PoolMethodProto(methodReference);
    }

    @Override // com.android.tools.smali.dexlib2.writer.MethodSection
    @Nonnull
    public MethodProtoReference getPrototype(@Nonnull PoolMethod poolMethod) {
        return new PoolMethodProto(poolMethod);
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // com.android.tools.smali.dexlib2.writer.MethodSection
    @Nonnull
    public CharSequence getName(@Nonnull MethodReference methodReference) {
        return methodReference.getName();
    }

    @Override // com.android.tools.smali.dexlib2.writer.MethodSection
    public int getMethodIndex(@Nonnull PoolMethod poolMethod) {
        return getItemIndex(poolMethod);
    }
}
