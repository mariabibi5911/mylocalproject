package com.android.tools.smali.util;

import androidx.recyclerview.widget.ItemTouchHelper;
import java.io.PrintStream;
import java.io.PrintWriter;

/* loaded from: classes.dex */
public class ExceptionWithContext extends RuntimeException {
    private StringBuffer context;

    public static ExceptionWithContext withContext(Throwable ex, String str, Object... formatArgs) {
        ExceptionWithContext ewc;
        if (ex instanceof ExceptionWithContext) {
            ewc = (ExceptionWithContext) ex;
        } else {
            ewc = new ExceptionWithContext(ex);
        }
        ewc.addContext(String.format(str, formatArgs));
        return ewc;
    }

    public ExceptionWithContext(String message, Object... formatArgs) {
        this(null, message, formatArgs);
    }

    public ExceptionWithContext(Throwable cause) {
        this(cause, null, new Object[0]);
    }

    /* JADX WARN: Illegal instructions before constructor call */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public ExceptionWithContext(Throwable cause, String message, Object... formatArgs) {
        super(r0, cause);
        String message2;
        if (message != null) {
            message2 = formatMessage(message, formatArgs);
        } else {
            message2 = cause != null ? cause.getMessage() : null;
        }
        if (cause instanceof ExceptionWithContext) {
            String ctx = ((ExceptionWithContext) cause).context.toString();
            StringBuffer stringBuffer = new StringBuffer(ctx.length() + ItemTouchHelper.Callback.DEFAULT_DRAG_ANIMATION_DURATION);
            this.context = stringBuffer;
            stringBuffer.append(ctx);
            return;
        }
        this.context = new StringBuffer(ItemTouchHelper.Callback.DEFAULT_DRAG_ANIMATION_DURATION);
    }

    private static String formatMessage(String message, Object... formatArgs) {
        if (message == null) {
            return null;
        }
        return String.format(message, formatArgs);
    }

    @Override // java.lang.Throwable
    public void printStackTrace(PrintStream out) {
        super.printStackTrace(out);
        out.println(this.context);
    }

    @Override // java.lang.Throwable
    public void printStackTrace(PrintWriter out) {
        super.printStackTrace(out);
        out.println(this.context);
    }

    public void addContext(String str) {
        if (str == null) {
            throw new NullPointerException("str == null");
        }
        this.context.append(str);
        if (!str.endsWith("\n")) {
            this.context.append('\n');
        }
    }

    public String getContext() {
        return this.context.toString();
    }

    public void printContext(PrintStream out) {
        out.println(getMessage());
        out.print(this.context);
    }

    public void printContext(PrintWriter out) {
        out.println(getMessage());
        out.print(this.context);
    }
}
