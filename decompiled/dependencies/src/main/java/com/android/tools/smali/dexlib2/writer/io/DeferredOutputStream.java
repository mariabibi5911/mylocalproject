package com.android.tools.smali.dexlib2.writer.io;

import java.io.IOException;
import java.io.OutputStream;

/* loaded from: classes.dex */
public abstract class DeferredOutputStream extends OutputStream {
    public abstract void writeTo(OutputStream outputStream) throws IOException;
}
