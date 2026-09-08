package com.android.tools.smali.dexlib2.writer.pool;

import com.android.tools.smali.dexlib2.iface.reference.MethodProtoReference;
import com.android.tools.smali.dexlib2.util.MethodUtil;
import com.android.tools.smali.dexlib2.writer.ProtoSection;
import com.android.tools.smali.dexlib2.writer.pool.TypeListPool;
import java.util.Collection;
import java.util.List;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/* loaded from: classes.dex */
public class ProtoPool extends BaseIndexPool<MethodProtoReference> implements ProtoSection<CharSequence, CharSequence, MethodProtoReference, TypeListPool.Key<? extends Collection<? extends CharSequence>>> {
    public ProtoPool(@Nonnull DexPool dexPool) {
        super(dexPool);
    }

    public void intern(@Nonnull MethodProtoReference reference) {
        Integer prev = (Integer) this.internedItems.put(reference, 0);
        if (prev == null) {
            ((StringPool) this.dexPool.stringSection).intern(getShorty(reference));
            ((TypePool) this.dexPool.typeSection).intern(reference.getReturnType());
            ((TypeListPool) this.dexPool.typeListSection).intern(reference.getParameterTypes());
        }
    }

    @Override // com.android.tools.smali.dexlib2.writer.ProtoSection
    @Nonnull
    public CharSequence getShorty(@Nonnull MethodProtoReference reference) {
        return MethodUtil.getShorty(reference.getParameterTypes(), reference.getReturnType());
    }

    @Override // com.android.tools.smali.dexlib2.writer.ProtoSection
    @Nonnull
    public CharSequence getReturnType(@Nonnull MethodProtoReference protoReference) {
        return protoReference.getReturnType();
    }

    @Override // com.android.tools.smali.dexlib2.writer.ProtoSection
    @Nullable
    public TypeListPool.Key<List<? extends CharSequence>> getParameters(@Nonnull MethodProtoReference methodProto) {
        return new TypeListPool.Key<>(methodProto.getParameterTypes());
    }
}
