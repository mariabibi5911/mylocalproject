package com.android.tools.smali.dexlib2.writer.pool;

import com.google.common.collect.Maps;
import java.util.Iterator;
import java.util.Map;
import javax.annotation.Nonnull;

/* loaded from: classes.dex */
public class BasePool<Key, Value> implements Markable {

    @Nonnull
    protected final DexPool dexPool;

    @Nonnull
    protected final Map<Key, Value> internedItems = Maps.newLinkedHashMap();
    private int markedItemCount = -1;

    public BasePool(@Nonnull DexPool dexPool) {
        this.dexPool = dexPool;
    }

    @Override // com.android.tools.smali.dexlib2.writer.pool.Markable
    public void mark() {
        this.markedItemCount = this.internedItems.size();
    }

    @Override // com.android.tools.smali.dexlib2.writer.pool.Markable
    public void reset() {
        int i = this.markedItemCount;
        if (i < 0) {
            throw new IllegalStateException("mark() must be called before calling reset()");
        }
        if (i == this.internedItems.size()) {
            return;
        }
        Iterator<Key> keys = this.internedItems.keySet().iterator();
        for (int i2 = 0; i2 < this.markedItemCount; i2++) {
            keys.next();
        }
        while (keys.hasNext()) {
            keys.next();
            keys.remove();
        }
    }

    public int getItemCount() {
        return this.internedItems.size();
    }
}
