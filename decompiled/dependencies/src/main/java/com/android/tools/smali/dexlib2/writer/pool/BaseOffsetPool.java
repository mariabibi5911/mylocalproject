package com.android.tools.smali.dexlib2.writer.pool;

import com.android.tools.smali.dexlib2.writer.OffsetSection;
import com.android.tools.smali.util.ExceptionWithContext;
import java.util.Collection;
import java.util.Map;
import javax.annotation.Nonnull;

/* loaded from: classes.dex */
public abstract class BaseOffsetPool<Key> extends BasePool<Key, Integer> implements OffsetSection<Key> {
    public BaseOffsetPool(@Nonnull DexPool dexPool) {
        super(dexPool);
    }

    @Override // com.android.tools.smali.dexlib2.writer.OffsetSection
    @Nonnull
    public Collection<? extends Map.Entry<? extends Key, Integer>> getItems() {
        return this.internedItems.entrySet();
    }

    @Override // com.android.tools.smali.dexlib2.writer.OffsetSection
    public int getItemOffset(@Nonnull Key key) {
        Integer offset = (Integer) this.internedItems.get(key);
        if (offset == null) {
            throw new ExceptionWithContext("Item not found.: %s", getItemString(key));
        }
        return offset.intValue();
    }

    @Nonnull
    protected String getItemString(@Nonnull Key key) {
        return key.toString();
    }
}
