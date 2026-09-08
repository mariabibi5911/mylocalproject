package com.android.tools.smali.dexlib2.writer.pool;

import com.android.tools.smali.dexlib2.iface.reference.StringReference;
import com.android.tools.smali.dexlib2.writer.StringSection;
import com.android.tools.smali.util.ExceptionWithContext;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/* loaded from: classes.dex */
public class StringPool extends StringTypeBasePool implements StringSection<CharSequence, StringReference> {
    public StringPool(@Nonnull DexPool dexPool) {
        super(dexPool);
    }

    public void intern(@Nonnull CharSequence string) {
        this.internedItems.put(string.toString(), 0);
    }

    public void internNullable(@Nullable CharSequence string) {
        if (string != null) {
            intern(string);
        }
    }

    @Override // com.android.tools.smali.dexlib2.writer.StringSection
    public int getItemIndex(@Nonnull StringReference key) {
        Integer index = (Integer) this.internedItems.get(key.toString());
        if (index == null) {
            throw new ExceptionWithContext("Item not found.: %s", key.toString());
        }
        return index.intValue();
    }

    @Override // com.android.tools.smali.dexlib2.writer.StringSection
    public boolean hasJumboIndexes() {
        return this.internedItems.size() > 65536;
    }
}
