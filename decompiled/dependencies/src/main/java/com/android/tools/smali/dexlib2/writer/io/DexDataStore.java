package com.android.tools.smali.dexlib2.writer.io;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import javax.annotation.Nonnull;

/* loaded from: classes.dex */
public interface DexDataStore {
    void close() throws IOException;

    @Nonnull
    OutputStream outputAt(int i);

    @Nonnull
    InputStream readAt(int i);
}
