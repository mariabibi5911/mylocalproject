package com.android.tools.smali.dexlib2.analysis;

import com.android.tools.smali.util.ExceptionWithContext;

/* loaded from: classes.dex */
public class UnresolvedClassException extends ExceptionWithContext {
    public UnresolvedClassException(Throwable cause) {
        super(cause);
    }

    public UnresolvedClassException(Throwable cause, String message, Object... formatArgs) {
        super(cause, message, formatArgs);
    }

    public UnresolvedClassException(String message, Object... formatArgs) {
        super(message, formatArgs);
    }
}
