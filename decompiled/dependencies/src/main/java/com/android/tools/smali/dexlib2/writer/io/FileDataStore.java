package com.android.tools.smali.dexlib2.writer.io;

import com.android.tools.smali.util.RandomAccessFileInputStream;
import com.android.tools.smali.util.RandomAccessFileOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.RandomAccessFile;
import javax.annotation.Nonnull;

/* loaded from: classes.dex */
public class FileDataStore implements DexDataStore {
    private final RandomAccessFile raf;

    public FileDataStore(@Nonnull File file) throws FileNotFoundException, IOException {
        RandomAccessFile randomAccessFile = new RandomAccessFile(file, "rw");
        this.raf = randomAccessFile;
        randomAccessFile.setLength(0L);
    }

    @Override // com.android.tools.smali.dexlib2.writer.io.DexDataStore
    @Nonnull
    public OutputStream outputAt(int offset) {
        return new RandomAccessFileOutputStream(this.raf, offset);
    }

    @Override // com.android.tools.smali.dexlib2.writer.io.DexDataStore
    @Nonnull
    public InputStream readAt(int offset) {
        return new RandomAccessFileInputStream(this.raf, offset);
    }

    @Override // com.android.tools.smali.dexlib2.writer.io.DexDataStore
    public void close() throws IOException {
        this.raf.close();
    }
}
