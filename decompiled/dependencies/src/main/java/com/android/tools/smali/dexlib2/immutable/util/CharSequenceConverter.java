package com.android.tools.smali.dexlib2.immutable.util;

import com.android.tools.smali.util.ImmutableConverter;
import com.google.common.collect.ImmutableList;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/* loaded from: classes.dex */
public final class CharSequenceConverter {
    private static final ImmutableConverter<String, CharSequence> CONVERTER = new ImmutableConverter<String, CharSequence>() { // from class: com.android.tools.smali.dexlib2.immutable.util.CharSequenceConverter.1
        /* JADX INFO: Access modifiers changed from: protected */
        @Override // com.android.tools.smali.util.ImmutableConverter
        public boolean isImmutable(@Nonnull CharSequence item) {
            return item instanceof String;
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // com.android.tools.smali.util.ImmutableConverter
        @Nonnull
        public String makeImmutable(@Nonnull CharSequence item) {
            return item.toString();
        }
    };

    private CharSequenceConverter() {
    }

    @Nonnull
    public static ImmutableList<String> immutableStringList(@Nullable Iterable<? extends CharSequence> iterable) {
        return CONVERTER.toList(iterable);
    }
}
