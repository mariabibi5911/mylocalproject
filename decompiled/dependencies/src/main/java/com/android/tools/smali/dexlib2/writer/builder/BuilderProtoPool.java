package com.android.tools.smali.dexlib2.writer.builder;

import com.android.tools.smali.dexlib2.iface.reference.MethodProtoReference;
import com.android.tools.smali.dexlib2.iface.reference.MethodReference;
import com.android.tools.smali.dexlib2.immutable.reference.ImmutableMethodProtoReference;
import com.android.tools.smali.dexlib2.util.MethodUtil;
import com.android.tools.smali.dexlib2.writer.ProtoSection;
import com.google.common.collect.Maps;
import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentMap;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes.dex */
public class BuilderProtoPool extends BaseBuilderPool implements ProtoSection<BuilderStringReference, BuilderTypeReference, BuilderMethodProtoReference, BuilderTypeList> {

    @Nonnull
    private final ConcurrentMap<MethodProtoReference, BuilderMethodProtoReference> internedItems;

    public BuilderProtoPool(@Nonnull DexBuilder dexBuilder) {
        super(dexBuilder);
        this.internedItems = Maps.newConcurrentMap();
    }

    @Nonnull
    public BuilderMethodProtoReference internMethodProto(@Nonnull MethodProtoReference methodProto) {
        BuilderMethodProtoReference ret = this.internedItems.get(methodProto);
        if (ret != null) {
            return ret;
        }
        BuilderMethodProtoReference protoReference = new BuilderMethodProtoReference(((BuilderStringPool) this.dexBuilder.stringSection).internString(MethodUtil.getShorty(methodProto.getParameterTypes(), methodProto.getReturnType())), ((BuilderTypeListPool) this.dexBuilder.typeListSection).internTypeList(methodProto.getParameterTypes()), ((BuilderTypePool) this.dexBuilder.typeSection).internType(methodProto.getReturnType()));
        BuilderMethodProtoReference ret2 = this.internedItems.putIfAbsent(protoReference, protoReference);
        return ret2 == null ? protoReference : ret2;
    }

    @Nonnull
    public BuilderMethodProtoReference internMethodProto(@Nonnull MethodReference methodReference) {
        return internMethodProto(new ImmutableMethodProtoReference(methodReference.getParameterTypes(), methodReference.getReturnType()));
    }

    @Override // com.android.tools.smali.dexlib2.writer.ProtoSection
    @Nonnull
    public BuilderStringReference getShorty(@Nonnull BuilderMethodProtoReference proto) {
        return proto.shorty;
    }

    @Override // com.android.tools.smali.dexlib2.writer.ProtoSection
    @Nonnull
    public BuilderTypeReference getReturnType(@Nonnull BuilderMethodProtoReference proto) {
        return proto.returnType;
    }

    @Override // com.android.tools.smali.dexlib2.writer.ProtoSection
    @Nullable
    public BuilderTypeList getParameters(@Nonnull BuilderMethodProtoReference proto) {
        return proto.parameterTypes;
    }

    @Override // com.android.tools.smali.dexlib2.writer.IndexSection
    public int getItemIndex(@Nonnull BuilderMethodProtoReference proto) {
        return proto.getIndex();
    }

    @Override // com.android.tools.smali.dexlib2.writer.IndexSection
    @Nonnull
    public Collection<? extends Map.Entry<? extends BuilderMethodProtoReference, Integer>> getItems() {
        return new BuilderMapEntryCollection<BuilderMethodProtoReference>(this.internedItems.values()) { // from class: com.android.tools.smali.dexlib2.writer.builder.BuilderProtoPool.1
            /* JADX INFO: Access modifiers changed from: protected */
            @Override // com.android.tools.smali.dexlib2.writer.builder.BuilderMapEntryCollection
            public int getValue(@Nonnull BuilderMethodProtoReference key) {
                return key.index;
            }

            /* JADX INFO: Access modifiers changed from: protected */
            @Override // com.android.tools.smali.dexlib2.writer.builder.BuilderMapEntryCollection
            public int setValue(@Nonnull BuilderMethodProtoReference key, int value) {
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
