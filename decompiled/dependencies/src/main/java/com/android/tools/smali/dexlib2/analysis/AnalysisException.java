package com.android.tools.smali.dexlib2.analysis;

import com.android.tools.smali.util.ExceptionWithContext;

/* loaded from: classes.dex */
public class AnalysisException extends ExceptionWithContext {
    public int codeAddress;

    public AnalysisException(Throwable cause) {
        super(cause);
    }

    public AnalysisException(Throwable cause, String message, Object... formatArgs) {
        super(cause, message, formatArgs);
    }

    public AnalysisException(String message, Object... formatArgs) {
        super(message, formatArgs);
    }
}
