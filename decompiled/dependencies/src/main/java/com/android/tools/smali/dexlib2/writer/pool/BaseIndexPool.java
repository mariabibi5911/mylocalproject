package com.android.tools.smali.dexlib2.writer.pool;

import com.android.tools.smali.dexlib2.writer.IndexSection;
import com.android.tools.smali.util.ExceptionWithContext;
import java.util.Collection;
import java.util.Map;
import javax.annotation.Nonnull;

/* loaded from: classes.dex */
public abstract class BaseIndexPool<Key> extends BasePool<Key, Integer> implements IndexSection<Key> {
    public BaseIndexPool(@Nonnull DexPool dexPool) {
        super(dexPool);
    }

    @Override // com.android.tools.smali.dexlib2.writer.IndexSection
    @Nonnull
    public Collection<? extends Map.Entry<? extends Key, Integer>> getItems() {
        return this.internedItems.entrySet();
    }

    @Override // com.android.tools.smali.dexlib2.writer.IndexSection
    public int getItemIndex(@Nonnull Key key) {
        Integer index = (Integer) this.internedItems.get(key);
        if (index == null) {
            throw new ExceptionWithContext("Item not found.: %s", getItemString(key));
        }
        return index.intValue();
    }

    @Nonnull
    protected String getItemString(@Nonnull Key key) {
        return key.toString();
    }
}
