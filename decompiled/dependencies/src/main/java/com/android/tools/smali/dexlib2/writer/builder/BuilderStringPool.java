package com.android.tools.smali.dexlib2.writer.builder;

import com.android.tools.smali.dexlib2.writer.StringSection;
import com.google.common.collect.Maps;
import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentMap;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/* loaded from: classes.dex */
class BuilderStringPool implements StringSection<BuilderStringReference, BuilderStringReference> {

    @Nonnull
    private final ConcurrentMap<String, BuilderStringReference> internedItems = Maps.newConcurrentMap();

    /* JADX INFO: Access modifiers changed from: package-private */
    @Nonnull
    public BuilderStringReference internString(@Nonnull String string) {
        BuilderStringReference ret = this.internedItems.get(string);
        if (ret != null) {
            return ret;
        }
        BuilderStringReference stringReference = new BuilderStringReference(string);
        BuilderStringReference ret2 = this.internedItems.putIfAbsent(string, stringReference);
        return ret2 == null ? stringReference : ret2;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Nullable
    public BuilderStringReference internNullableString(@Nullable String string) {
        if (string == null) {
            return null;
        }
        return internString(string);
    }

    @Override // com.android.tools.smali.dexlib2.writer.NullableIndexSection
    public int getNullableItemIndex(@Nullable BuilderStringReference key) {
        if (key == null) {
            return -1;
        }
        return key.index;
    }

    @Override // com.android.tools.smali.dexlib2.writer.IndexSection
    public int getItemIndex(@Nonnull BuilderStringReference key) {
        return key.index;
    }

    @Override // com.android.tools.smali.dexlib2.writer.StringSection
    public boolean hasJumboIndexes() {
        return this.internedItems.size() > 65536;
    }

    @Override // com.android.tools.smali.dexlib2.writer.IndexSection
    @Nonnull
    public Collection<? extends Map.Entry<? extends BuilderStringReference, Integer>> getItems() {
        return new BuilderMapEntryCollection<BuilderStringReference>(this.internedItems.values()) { // from class: com.android.tools.smali.dexlib2.writer.builder.BuilderStringPool.1
            /* JADX INFO: Access modifiers changed from: protected */
            @Override // com.android.tools.smali.dexlib2.writer.builder.BuilderMapEntryCollection
            public int getValue(@Nonnull BuilderStringReference key) {
                return key.index;
            }

            /* JADX INFO: Access modifiers changed from: protected */
            @Override // com.android.tools.smali.dexlib2.writer.builder.BuilderMapEntryCollection
            public int setValue(@Nonnull BuilderStringReference key, int value) {
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
