package com.google.common.hash;

import com.google.common.base.Preconditions;

@ElementTypesAreNonnullByDefault
/* loaded from: classes7.dex */
final class Fingerprint2011 extends AbstractNonStreamingHashFunction {
    static final HashFunction FINGERPRINT_2011 = new Fingerprint2011();
    private static final long K0 = -6505348102511208375L;
    private static final long K1 = -8261664234251669945L;
    private static final long K2 = -4288712594273399085L;
    private static final long K3 = -4132994306676758123L;

    Fingerprint2011() {
    }

    @Override // com.google.common.hash.AbstractNonStreamingHashFunction, com.google.common.hash.AbstractHashFunction, com.google.common.hash.HashFunction
    public HashCode hashBytes(byte[] input, int off, int len) {
        Preconditions.checkPositionIndexes(off, off + len, input.length);
        return HashCode.fromLong(fingerprint(input, off, len));
    }

    @Override // com.google.common.hash.HashFunction
    public int bits() {
        return 64;
    }

    public String toString() {
        return "Hashing.fingerprint2011()";
    }

    static long fingerprint(byte[] bytes, int offset, int length) {
        long result;
        if (length <= 32) {
            result = murmurHash64WithSeed(bytes, offset, length, -1397348546323613475L);
        } else if (length <= 64) {
            result = hashLength33To64(bytes, offset, length);
        } else {
            result = fullFingerprint(bytes, offset, length);
        }
        long v = K0;
        long u = length >= 8 ? LittleEndianByteArray.load64(bytes, offset) : -6505348102511208375L;
        if (length >= 9) {
            v = LittleEndianByteArray.load64(bytes, (offset + length) - 8);
        }
        long result2 = hash128to64(result + v, u);
        return (result2 == 0 || result2 == 1) ? (-2) + result2 : result2;
    }

    private static long shiftMix(long val) {
        return (val >>> 47) ^ val;
    }

    static long hash128to64(long high, long low) {
        long a = (low ^ high) * K3;
        long b = (high ^ (a ^ (a >>> 47))) * K3;
        return (b ^ (b >>> 47)) * K3;
    }

    private static void weakHashLength32WithSeeds(byte[] bytes, int offset, long seedA, long seedB, long[] output) {
        long part1 = LittleEndianByteArray.load64(bytes, offset);
        long part2 = LittleEndianByteArray.load64(bytes, offset + 8);
        long part3 = LittleEndianByteArray.load64(bytes, offset + 16);
        long part4 = LittleEndianByteArray.load64(bytes, offset + 24);
        long seedA2 = seedA + part1;
        long seedB2 = Long.rotateRight(seedB + seedA2 + part4, 51);
        long seedA3 = seedA2 + part2 + part3;
        long seedB3 = seedB2 + Long.rotateRight(seedA3, 23);
        output[0] = seedA3 + part4;
        output[1] = seedB3 + seedA2;
    }

    private static long fullFingerprint(byte[] bytes, int offset, int length) {
        long x = LittleEndianByteArray.load64(bytes, offset);
        long y = LittleEndianByteArray.load64(bytes, (offset + length) - 16) ^ K1;
        long z = LittleEndianByteArray.load64(bytes, (offset + length) - 56) ^ K0;
        long[] v = new long[2];
        long[] w = new long[2];
        weakHashLength32WithSeeds(bytes, (offset + length) - 64, length, y, v);
        weakHashLength32WithSeeds(bytes, (offset + length) - 32, length * K1, K0, w);
        long z2 = z + (shiftMix(v[1]) * K1);
        long x2 = Long.rotateRight(z2 + x, 39) * K1;
        long x3 = Long.rotateRight(y, 33) * K1;
        int offset2 = offset;
        int length2 = (length - 1) & (-64);
        while (true) {
            long x4 = Long.rotateRight(x2 + x3 + v[0] + LittleEndianByteArray.load64(bytes, offset2 + 16), 37) * K1;
            long x5 = v[1];
            long y2 = Long.rotateRight(x5 + x3 + LittleEndianByteArray.load64(bytes, offset2 + 48), 42) * K1;
            long x6 = x4 ^ w[1];
            long y3 = y2 ^ v[0];
            long z3 = Long.rotateRight(w[0] ^ z2, 33);
            weakHashLength32WithSeeds(bytes, offset2, v[1] * K1, x6 + w[0], v);
            weakHashLength32WithSeeds(bytes, offset2 + 32, w[1] + z3, y3, w);
            z2 = x6;
            offset2 += 64;
            length2 -= 64;
            if (length2 == 0) {
                return hash128to64(hash128to64(v[0], w[0]) + (shiftMix(y3) * K1) + z2, hash128to64(v[1], w[1]) + z3);
            }
            x2 = z3;
            x3 = y3;
        }
    }

    private static long hashLength33To64(byte[] bytes, int offset, int length) {
        long z = LittleEndianByteArray.load64(bytes, offset + 24);
        long a = LittleEndianByteArray.load64(bytes, offset) + ((length + LittleEndianByteArray.load64(bytes, (offset + length) - 16)) * K0);
        long b = Long.rotateRight(a + z, 52);
        long c = Long.rotateRight(a, 37);
        long a2 = a + LittleEndianByteArray.load64(bytes, offset + 8);
        long c2 = c + Long.rotateRight(a2, 7);
        long a3 = a2 + LittleEndianByteArray.load64(bytes, offset + 16);
        long vf = a3 + z;
        long vs = b + Long.rotateRight(a3, 31) + c2;
        long a4 = LittleEndianByteArray.load64(bytes, offset + 16) + LittleEndianByteArray.load64(bytes, (offset + length) - 32);
        long z2 = LittleEndianByteArray.load64(bytes, (offset + length) - 8);
        long b2 = Long.rotateRight(a4 + z2, 52);
        long c3 = Long.rotateRight(a4, 37);
        long a5 = a4 + LittleEndianByteArray.load64(bytes, (offset + length) - 24);
        long c4 = c3 + Long.rotateRight(a5, 7);
        long a6 = a5 + LittleEndianByteArray.load64(bytes, (offset + length) - 16);
        long wf = a6 + z2;
        long ws = Long.rotateRight(a6, 31) + b2 + c4;
        long r = shiftMix(((vf + ws) * K2) + ((wf + vs) * K0));
        return shiftMix((K0 * r) + vs) * K2;
    }

    static long murmurHash64WithSeed(byte[] bytes, int offset, int length, long seed) {
        int lengthAligned = (~7) & length;
        int lengthRemainder = length & 7;
        long hash = seed ^ (length * K3);
        for (int i = 0; i < lengthAligned; i += 8) {
            long loaded = LittleEndianByteArray.load64(bytes, offset + i);
            long data = shiftMix(loaded * K3) * K3;
            hash = (hash ^ data) * K3;
        }
        if (lengthRemainder != 0) {
            long data2 = LittleEndianByteArray.load64Safely(bytes, offset + lengthAligned, lengthRemainder);
            hash = (hash ^ data2) * K3;
        }
        long data3 = shiftMix(hash);
        long hash2 = data3 * K3;
        return shiftMix(hash2);
    }
}
