package com.android.tools.smali.dexlib2.writer.builder;

import com.google.common.collect.ImmutableList;
import java.util.AbstractList;
import java.util.List;
import javax.annotation.Nonnull;

/* loaded from: classes.dex */
public class BuilderTypeList extends AbstractList<BuilderTypeReference> {
    static final BuilderTypeList EMPTY = new BuilderTypeList(ImmutableList.of());
    int offset = 0;

    @Nonnull
    final List<? extends BuilderTypeReference> types;

    public BuilderTypeList(@Nonnull List<? extends BuilderTypeReference> types) {
        this.types = types;
    }

    @Override // java.util.AbstractList, java.util.List
    public BuilderTypeReference get(int index) {
        return this.types.get(index);
    }

    @Override // java.util.AbstractCollection, java.util.Collection, java.util.List
    public int size() {
        return this.types.size();
    }

    public int getOffset() {
        return this.offset;
    }

    public void setOffset(int offset) {
        this.offset = offset;
    }
}
