package com.android.tools.smali.dexlib2.rewriter;

import javax.annotation.Nonnull;

/* loaded from: classes.dex */
public class TypeRewriter implements Rewriter<String> {
    @Override // com.android.tools.smali.dexlib2.rewriter.Rewriter
    @Nonnull
    public String rewrite(@Nonnull String value) {
        if (value.length() > 0 && value.charAt(0) == '[') {
            int dimensions = 0;
            while (value.charAt(dimensions) == '[') {
                dimensions++;
            }
            String unwrappedType = value.substring(dimensions);
            String rewrittenType = rewriteUnwrappedType(unwrappedType);
            if (unwrappedType != rewrittenType) {
                return new StringBuilder(rewrittenType.length() + dimensions).append((CharSequence) value, 0, dimensions).append(rewrittenType).toString();
            }
            return value;
        }
        return rewriteUnwrappedType(value);
    }

    @Nonnull
    protected String rewriteUnwrappedType(@Nonnull String value) {
        return value;
    }
}
