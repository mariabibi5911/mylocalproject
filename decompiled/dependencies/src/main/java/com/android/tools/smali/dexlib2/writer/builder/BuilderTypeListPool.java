package com.android.tools.smali.dexlib2.writer.builder;

import com.android.tools.smali.dexlib2.writer.TypeListSection;
import com.google.common.base.Function;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Iterables;
import com.google.common.collect.Maps;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentMap;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes.dex */
public class BuilderTypeListPool extends BaseBuilderPool implements TypeListSection<BuilderTypeReference, BuilderTypeList> {

    @Nonnull
    private final ConcurrentMap<List<? extends CharSequence>, BuilderTypeList> internedItems;

    public BuilderTypeListPool(@Nonnull DexBuilder dexBuilder) {
        super(dexBuilder);
        this.internedItems = Maps.newConcurrentMap();
    }

    @Nonnull
    public BuilderTypeList internTypeList(@Nullable List<? extends CharSequence> types) {
        if (types == null || types.size() == 0) {
            return BuilderTypeList.EMPTY;
        }
        BuilderTypeList ret = this.internedItems.get(types);
        if (ret != null) {
            return ret;
        }
        BuilderTypeList typeList = new BuilderTypeList(ImmutableList.copyOf(Iterables.transform(types, new Function<CharSequence, BuilderTypeReference>() { // from class: com.android.tools.smali.dexlib2.writer.builder.BuilderTypeListPool.1
            @Override // com.google.common.base.Function
            @Nonnull
            public BuilderTypeReference apply(CharSequence input) {
                return ((BuilderTypePool) BuilderTypeListPool.this.dexBuilder.typeSection).internType(input.toString());
            }
        })));
        BuilderTypeList ret2 = this.internedItems.putIfAbsent(typeList, typeList);
        return ret2 == null ? typeList : ret2;
    }

    @Override // com.android.tools.smali.dexlib2.writer.TypeListSection, com.android.tools.smali.dexlib2.writer.NullableOffsetSection
    public int getNullableItemOffset(@Nullable BuilderTypeList key) {
        if (key == null || key.size() == 0) {
            return 0;
        }
        return key.offset;
    }

    @Override // com.android.tools.smali.dexlib2.writer.TypeListSection
    @Nonnull
    public Collection<? extends BuilderTypeReference> getTypes(@Nullable BuilderTypeList key) {
        return key == null ? BuilderTypeList.EMPTY : key.types;
    }

    @Override // com.android.tools.smali.dexlib2.writer.OffsetSection
    public int getItemOffset(@Nonnull BuilderTypeList key) {
        return key.offset;
    }

    @Override // com.android.tools.smali.dexlib2.writer.OffsetSection
    @Nonnull
    public Collection<? extends Map.Entry<? extends BuilderTypeList, Integer>> getItems() {
        return new BuilderMapEntryCollection<BuilderTypeList>(this.internedItems.values()) { // from class: com.android.tools.smali.dexlib2.writer.builder.BuilderTypeListPool.2
            /* JADX INFO: Access modifiers changed from: protected */
            @Override // com.android.tools.smali.dexlib2.writer.builder.BuilderMapEntryCollection
            public int getValue(@Nonnull BuilderTypeList key) {
                return key.offset;
            }

            /* JADX INFO: Access modifiers changed from: protected */
            @Override // com.android.tools.smali.dexlib2.writer.builder.BuilderMapEntryCollection
            public int setValue(@Nonnull BuilderTypeList key, int value) {
                int prev = key.offset;
                key.offset = value;
                return prev;
            }
        };
    }
}
