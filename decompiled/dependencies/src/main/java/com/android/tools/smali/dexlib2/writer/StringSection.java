package com.android.tools.smali.dexlib2.writer;

import com.android.tools.smali.dexlib2.iface.reference.StringReference;
import javax.annotation.Nonnull;

/* loaded from: classes.dex */
public interface StringSection<StringKey, StringRef extends StringReference> extends NullableIndexSection<StringKey> {
    int getItemIndex(@Nonnull StringRef stringref);

    boolean hasJumboIndexes();
}
