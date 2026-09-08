package com.android.tools.smali.dexlib2.writer.io;

import java.io.IOException;

/* loaded from: classes.dex */
public interface DeferredOutputStreamFactory {
    DeferredOutputStream makeDeferredOutputStream() throws IOException;
}
