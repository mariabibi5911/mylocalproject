package com.android.tools.smali.dexlib2.dexbacked;

import com.android.tools.smali.util.ExceptionWithContext;
import com.google.common.base.Ascii;
import java.util.Arrays;
import javax.annotation.Nonnull;

/* loaded from: classes.dex */
public class DexBuffer {
    final int baseOffset;

    @Nonnull
    final byte[] buf;

    public DexBuffer(@Nonnull byte[] buf) {
        this(buf, 0);
    }

    public DexBuffer(@Nonnull byte[] buf, int offset) {
        this.buf = buf;
        this.baseOffset = offset;
    }

    public int readSmallUint(int offset) {
        byte[] buf = this.buf;
        int offset2 = offset + this.baseOffset;
        int result = (buf[offset2] & 255) | ((buf[offset2 + 1] & 255) << 8) | ((buf[offset2 + 2] & 255) << 16) | (buf[offset2 + 3] << Ascii.CAN);
        if (result < 0) {
            throw new ExceptionWithContext("Encountered small uint that is out of range at offset 0x%x", Integer.valueOf(offset2));
        }
        return result;
    }

    public int readOptionalUint(int offset) {
        byte[] buf = this.buf;
        int offset2 = offset + this.baseOffset;
        int result = (buf[offset2] & 255) | ((buf[offset2 + 1] & 255) << 8) | ((buf[offset2 + 2] & 255) << 16) | (buf[offset2 + 3] << Ascii.CAN);
        if (result < -1) {
            throw new ExceptionWithContext("Encountered optional uint that is out of range at offset 0x%x", Integer.valueOf(offset2));
        }
        return result;
    }

    public int readUshort(int offset) {
        byte[] buf = this.buf;
        int offset2 = offset + this.baseOffset;
        return (buf[offset2] & 255) | ((buf[offset2 + 1] & 255) << 8);
    }

    public int readUbyte(int offset) {
        return this.buf[this.baseOffset + offset] & 255;
    }

    public long readLong(int offset) {
        byte[] buf = this.buf;
        int offset2 = offset + this.baseOffset;
        return (buf[offset2] & 255) | ((buf[offset2 + 1] & 255) << 8) | ((buf[offset2 + 2] & 255) << 16) | ((buf[offset2 + 3] & 255) << 24) | ((buf[offset2 + 4] & 255) << 32) | ((buf[offset2 + 5] & 255) << 40) | ((buf[offset2 + 6] & 255) << 48) | (buf[offset2 + 7] << 56);
    }

    public int readLongAsSmallUint(int offset) {
        byte[] buf = this.buf;
        int offset2 = offset + this.baseOffset;
        long result = (buf[offset2] & 255) | ((buf[offset2 + 1] & 255) << 8) | ((buf[offset2 + 2] & 255) << 16) | ((buf[offset2 + 3] & 255) << 24) | ((buf[offset2 + 4] & 255) << 32) | ((buf[offset2 + 5] & 255) << 40) | ((buf[offset2 + 6] & 255) << 48) | (buf[offset2 + 7] << 56);
        if (result < 0 || result > 2147483647L) {
            throw new ExceptionWithContext("Encountered out-of-range ulong at offset 0x%x", Integer.valueOf(offset2));
        }
        return (int) result;
    }

    public int readInt(int offset) {
        byte[] buf = this.buf;
        int offset2 = offset + this.baseOffset;
        return (buf[offset2] & 255) | ((buf[offset2 + 1] & 255) << 8) | ((buf[offset2 + 2] & 255) << 16) | (buf[offset2 + 3] << Ascii.CAN);
    }

    public int readShort(int offset) {
        byte[] buf = this.buf;
        int offset2 = offset + this.baseOffset;
        return (buf[offset2] & 255) | (buf[offset2 + 1] << 8);
    }

    public int readByte(int offset) {
        return this.buf[this.baseOffset + offset];
    }

    @Nonnull
    public byte[] readByteRange(int start, int length) {
        byte[] bArr = this.buf;
        int i = this.baseOffset;
        return Arrays.copyOfRange(bArr, i + start, i + start + length);
    }

    @Nonnull
    public DexReader<? extends DexBuffer> readerAt(int offset) {
        return new DexReader<>(this, offset);
    }

    @Nonnull
    public byte[] getBuf() {
        return this.buf;
    }

    public int getBaseOffset() {
        return this.baseOffset;
    }
}
