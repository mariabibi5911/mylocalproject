package com.android.tools.smali.dexlib2.writer.io;

import com.google.common.collect.Lists;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;
import javax.annotation.Nonnull;

/* loaded from: classes.dex */
public class MemoryDeferredOutputStream extends DeferredOutputStream {
    private static final int DEFAULT_BUFFER_SIZE = 16384;
    private final List<byte[]> buffers;
    private byte[] currentBuffer;
    private int currentPosition;

    public MemoryDeferredOutputStream() {
        this(16384);
    }

    public MemoryDeferredOutputStream(int bufferSize) {
        this.buffers = Lists.newArrayList();
        this.currentBuffer = new byte[bufferSize];
    }

    @Override // com.android.tools.smali.dexlib2.writer.io.DeferredOutputStream
    public void writeTo(OutputStream output) throws IOException {
        for (byte[] buffer : this.buffers) {
            output.write(buffer);
        }
        int i = this.currentPosition;
        if (i > 0) {
            output.write(this.currentBuffer, 0, i);
        }
        this.buffers.clear();
        this.currentPosition = 0;
    }

    @Override // java.io.OutputStream
    public void write(int i) throws IOException {
        if (remaining() == 0) {
            this.buffers.add(this.currentBuffer);
            this.currentBuffer = new byte[this.currentBuffer.length];
            this.currentPosition = 0;
        }
        byte[] bArr = this.currentBuffer;
        int i2 = this.currentPosition;
        this.currentPosition = i2 + 1;
        bArr[i2] = (byte) i;
    }

    @Override // java.io.OutputStream
    public void write(byte[] bytes) throws IOException {
        write(bytes, 0, bytes.length);
    }

    @Override // java.io.OutputStream
    public void write(byte[] bytes, int offset, int length) throws IOException {
        int remaining = remaining();
        int written = 0;
        while (length - written > 0) {
            int toWrite = Math.min(remaining, length - written);
            System.arraycopy(bytes, offset + written, this.currentBuffer, this.currentPosition, toWrite);
            written += toWrite;
            this.currentPosition += toWrite;
            remaining = remaining();
            if (remaining == 0) {
                this.buffers.add(this.currentBuffer);
                byte[] bArr = new byte[this.currentBuffer.length];
                this.currentBuffer = bArr;
                this.currentPosition = 0;
                remaining = bArr.length;
            }
        }
    }

    private int remaining() {
        return this.currentBuffer.length - this.currentPosition;
    }

    @Nonnull
    public static DeferredOutputStreamFactory getFactory() {
        return getFactory(16384);
    }

    @Nonnull
    public static DeferredOutputStreamFactory getFactory(final int bufferSize) {
        return new DeferredOutputStreamFactory() { // from class: com.android.tools.smali.dexlib2.writer.io.MemoryDeferredOutputStream.1
            @Override // com.android.tools.smali.dexlib2.writer.io.DeferredOutputStreamFactory
            public DeferredOutputStream makeDeferredOutputStream() {
                return new MemoryDeferredOutputStream(bufferSize);
            }
        };
    }
}
