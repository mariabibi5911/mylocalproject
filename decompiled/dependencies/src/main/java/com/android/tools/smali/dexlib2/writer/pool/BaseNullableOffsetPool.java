package com.android.tools.smali.dexlib2.writer.pool;

import com.android.tools.smali.dexlib2.writer.NullableOffsetSection;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/* loaded from: classes.dex */
public abstract class BaseNullableOffsetPool<Key> extends BaseOffsetPool<Key> implements NullableOffsetSection<Key> {
    public BaseNullableOffsetPool(@Nonnull DexPool dexPool) {
        super(dexPool);
    }

    @Override // com.android.tools.smali.dexlib2.writer.NullableOffsetSection
    public int getNullableItemOffset(@Nullable Key key) {
        if (key == null) {
            return 0;
        }
        return getItemOffset(key);
    }
}
