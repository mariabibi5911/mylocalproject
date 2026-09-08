package com.android.tools.smali.dexlib2.writer.pool;

import com.android.tools.smali.dexlib2.writer.NullableIndexSection;
import com.android.tools.smali.util.ExceptionWithContext;
import java.util.Collection;
import java.util.Map;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/* loaded from: classes.dex */
public abstract class StringTypeBasePool extends BasePool<String, Integer> implements NullableIndexSection<CharSequence>, Markable {
    public StringTypeBasePool(@Nonnull DexPool dexPool) {
        super(dexPool);
    }

    @Override // com.android.tools.smali.dexlib2.writer.IndexSection
    @Nonnull
    public Collection<Map.Entry<String, Integer>> getItems() {
        return this.internedItems.entrySet();
    }

    @Override // com.android.tools.smali.dexlib2.writer.IndexSection
    public int getItemIndex(@Nonnull CharSequence key) {
        Integer index = (Integer) this.internedItems.get(key.toString());
        if (index == null) {
            throw new ExceptionWithContext("Item not found.: %s", key.toString());
        }
        return index.intValue();
    }

    @Override // com.android.tools.smali.dexlib2.writer.NullableIndexSection
    public int getNullableItemIndex(@Nullable CharSequence key) {
        if (key == null) {
            return -1;
        }
        return getItemIndex(key);
    }
}
