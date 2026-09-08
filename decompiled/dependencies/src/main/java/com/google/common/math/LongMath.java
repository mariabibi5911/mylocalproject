package com.google.common.math;

import androidx.constraintlayout.core.motion.utils.TypedValues;
import com.google.common.base.Preconditions;
import com.google.common.primitives.Longs;
import com.google.common.primitives.UnsignedLongs;
import java.math.RoundingMode;

@ElementTypesAreNonnullByDefault
/* loaded from: classes7.dex */
public final class LongMath {
    static final long FLOOR_SQRT_MAX_LONG = 3037000499L;
    static final long MAX_POWER_OF_SQRT2_UNSIGNED = -5402926248376769404L;
    static final long MAX_SIGNED_POWER_OF_TWO = 4611686018427387904L;
    private static final int SIEVE_30 = -545925251;
    static final byte[] maxLog10ForLeadingZeros = {19, 18, 18, 18, 18, 17, 17, 17, 16, 16, 16, 15, 15, 15, 15, 14, 14, 14, 13, 13, 13, 12, 12, 12, 12, 11, 11, 11, 10, 10, 10, 9, 9, 9, 9, 8, 8, 8, 7, 7, 7, 6, 6, 6, 6, 5, 5, 5, 4, 4, 4, 3, 3, 3, 3, 2, 2, 2, 1, 1, 1, 0, 0, 0};
    static final long[] powersOf10 = {1, 10, 100, 1000, 10000, 100000, 1000000, 10000000, 100000000, 1000000000, 10000000000L, 100000000000L, 1000000000000L, 10000000000000L, 100000000000000L, 1000000000000000L, 10000000000000000L, 100000000000000000L, 1000000000000000000L};
    static final long[] halfPowersOf10 = {3, 31, 316, 3162, 31622, 316227, 3162277, 31622776, 316227766, 3162277660L, 31622776601L, 316227766016L, 3162277660168L, 31622776601683L, 316227766016837L, 3162277660168379L, 31622776601683793L, 316227766016837933L, 3162277660168379331L};
    static final long[] factorials = {1, 1, 2, 6, 24, 120, 720, 5040, 40320, 362880, 3628800, 39916800, 479001600, 6227020800L, 87178291200L, 1307674368000L, 20922789888000L, 355687428096000L, 6402373705728000L, 121645100408832000L, 2432902008176640000L};
    static final int[] biggestBinomials = {Integer.MAX_VALUE, Integer.MAX_VALUE, Integer.MAX_VALUE, 3810779, 121977, 16175, 4337, 1733, 887, 534, 361, 265, 206, 169, 143, 125, 111, TypedValues.TYPE_TARGET, 94, 88, 83, 79, 76, 74, 72, 70, 69, 68, 67, 67, 66, 66, 66, 66};
    static final int[] biggestSimpleBinomials = {Integer.MAX_VALUE, Integer.MAX_VALUE, Integer.MAX_VALUE, 2642246, 86251, 11724, 3218, 1313, 684, 419, 287, 214, 169, 139, 119, 105, 95, 87, 81, 76, 73, 70, 68, 66, 64, 63, 62, 62, 61, 61, 61};
    private static final long[][] millerRabinBaseSets = {new long[]{291830, 126401071349994536L}, new long[]{885594168, 725270293939359937L, 3569819667048198375L}, new long[]{273919523040L, 15, 7363882082L, 992620450144556L}, new long[]{47636622961200L, 2, 2570940, 211991001, 3749873356L}, new long[]{7999252175582850L, 2, 4130806001517L, 149795463772692060L, 186635894390467037L, 3967304179347715805L}, new long[]{585226005592931976L, 2, 123635709730000L, 9233062284813009L, 43835965440333360L, 761179012939631437L, 1263739024124850375L}, new long[]{Long.MAX_VALUE, 2, 325, 9375, 28178, 450775, 9780504, 1795265022}};

    public static long ceilingPowerOfTwo(long x) {
        MathPreconditions.checkPositive("x", x);
        if (x > 4611686018427387904L) {
            throw new ArithmeticException(new StringBuilder(70).append("ceilingPowerOfTwo(").append(x).append(") is not representable as a long").toString());
        }
        return 1 << (-Long.numberOfLeadingZeros(x - 1));
    }

    public static long floorPowerOfTwo(long x) {
        MathPreconditions.checkPositive("x", x);
        return 1 << (63 - Long.numberOfLeadingZeros(x));
    }

    public static boolean isPowerOfTwo(long x) {
        return (((x - 1) & x) == 0) & (x > 0);
    }

    static int lessThanBranchFree(long x, long y) {
        return (int) ((~(~(x - y))) >>> 63);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: com.google.common.math.LongMath$1, reason: invalid class name */
    /* loaded from: classes7.dex */
    public static /* synthetic */ class AnonymousClass1 {
        static final /* synthetic */ int[] $SwitchMap$java$math$RoundingMode;

        static {
            int[] iArr = new int[RoundingMode.values().length];
            $SwitchMap$java$math$RoundingMode = iArr;
            try {
                iArr[RoundingMode.UNNECESSARY.ordinal()] = 1;
            } catch (NoSuchFieldError e) {
            }
            try {
                $SwitchMap$java$math$RoundingMode[RoundingMode.DOWN.ordinal()] = 2;
            } catch (NoSuchFieldError e2) {
            }
            try {
                $SwitchMap$java$math$RoundingMode[RoundingMode.FLOOR.ordinal()] = 3;
            } catch (NoSuchFieldError e3) {
            }
            try {
                $SwitchMap$java$math$RoundingMode[RoundingMode.UP.ordinal()] = 4;
            } catch (NoSuchFieldError e4) {
            }
            try {
                $SwitchMap$java$math$RoundingMode[RoundingMode.CEILING.ordinal()] = 5;
            } catch (NoSuchFieldError e5) {
            }
            try {
                $SwitchMap$java$math$RoundingMode[RoundingMode.HALF_DOWN.ordinal()] = 6;
            } catch (NoSuchFieldError e6) {
            }
            try {
                $SwitchMap$java$math$RoundingMode[RoundingMode.HALF_UP.ordinal()] = 7;
            } catch (NoSuchFieldError e7) {
            }
            try {
                $SwitchMap$java$math$RoundingMode[RoundingMode.HALF_EVEN.ordinal()] = 8;
            } catch (NoSuchFieldError e8) {
            }
        }
    }

    public static int log2(long x, RoundingMode mode) {
        MathPreconditions.checkPositive("x", x);
        switch (AnonymousClass1.$SwitchMap$java$math$RoundingMode[mode.ordinal()]) {
            case 1:
                MathPreconditions.checkRoundingUnnecessary(isPowerOfTwo(x));
                break;
            case 2:
            case 3:
                break;
            case 4:
            case 5:
                return 64 - Long.numberOfLeadingZeros(x - 1);
            case 6:
            case 7:
            case 8:
                int leadingZeros = Long.numberOfLeadingZeros(x);
                long cmp = MAX_POWER_OF_SQRT2_UNSIGNED >>> leadingZeros;
                int logFloor = 63 - leadingZeros;
                return lessThanBranchFree(cmp, x) + logFloor;
            default:
                throw new AssertionError("impossible");
        }
        return 63 - Long.numberOfLeadingZeros(x);
    }

    public static int log10(long x, RoundingMode mode) {
        MathPreconditions.checkPositive("x", x);
        int logFloor = log10Floor(x);
        long floorPow = powersOf10[logFloor];
        switch (AnonymousClass1.$SwitchMap$java$math$RoundingMode[mode.ordinal()]) {
            case 1:
                MathPreconditions.checkRoundingUnnecessary(x == floorPow);
            case 2:
            case 3:
                return logFloor;
            case 4:
            case 5:
                return lessThanBranchFree(floorPow, x) + logFloor;
            case 6:
            case 7:
            case 8:
                return lessThanBranchFree(halfPowersOf10[logFloor], x) + logFloor;
            default:
                throw new AssertionError();
        }
    }

    static int log10Floor(long x) {
        int y = maxLog10ForLeadingZeros[Long.numberOfLeadingZeros(x)];
        return y - lessThanBranchFree(x, powersOf10[y]);
    }

    public static long pow(long b, int k) {
        MathPreconditions.checkNonNegative("exponent", k);
        if (-2 <= b && b <= 2) {
            switch ((int) b) {
                case -2:
                    if (k < 64) {
                        return (k & 1) == 0 ? 1 << k : -(1 << k);
                    }
                    return 0L;
                case -1:
                    return (k & 1) == 0 ? 1L : -1L;
                case 0:
                    return k == 0 ? 1L : 0L;
                case 1:
                    return 1L;
                case 2:
                    if (k < 64) {
                        return 1 << k;
                    }
                    return 0L;
                default:
                    throw new AssertionError();
            }
        }
        long accum = 1;
        while (true) {
            switch (k) {
                case 0:
                    return accum;
                case 1:
                    return accum * b;
                default:
                    accum *= (k & 1) == 0 ? 1L : b;
                    b *= b;
                    k >>= 1;
            }
        }
    }

    public static long sqrt(long j, RoundingMode roundingMode) {
        MathPreconditions.checkNonNegative("x", j);
        if (fitsInInt(j)) {
            return IntMath.sqrt((int) j, roundingMode);
        }
        long sqrt = (long) Math.sqrt(j);
        long j2 = sqrt * sqrt;
        switch (AnonymousClass1.$SwitchMap$java$math$RoundingMode[roundingMode.ordinal()]) {
            case 1:
                MathPreconditions.checkRoundingUnnecessary(j2 == j);
                return sqrt;
            case 2:
            case 3:
                if (j < j2) {
                    return sqrt - 1;
                }
                return sqrt;
            case 4:
            case 5:
                if (j > j2) {
                    return 1 + sqrt;
                }
                return sqrt;
            case 6:
            case 7:
            case 8:
                return lessThanBranchFree((r4 * r4) + r4, j) + (sqrt - (j >= j2 ? 0 : 1));
            default:
                throw new AssertionError();
        }
    }

    /* JADX WARN: Failed to find 'out' block for switch in B:6:0x0023. Please report as an issue. */
    /* JADX WARN: Removed duplicated region for block: B:20:0x0071  */
    /* JADX WARN: Removed duplicated region for block: B:22:0x0074  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public static long divide(long p, long q, RoundingMode mode) {
        boolean increment;
        Preconditions.checkNotNull(mode);
        long div = p / q;
        long rem = p - (q * div);
        if (rem == 0) {
            return div;
        }
        int signum = ((int) ((p ^ q) >> 63)) | 1;
        switch (AnonymousClass1.$SwitchMap$java$math$RoundingMode[mode.ordinal()]) {
            case 1:
                MathPreconditions.checkRoundingUnnecessary(rem == 0);
            case 2:
                increment = false;
                return increment ? signum + div : div;
            case 3:
                increment = signum < 0;
                if (increment) {
                }
                break;
            case 4:
                increment = true;
                if (increment) {
                }
                break;
            case 5:
                increment = signum > 0;
                if (increment) {
                }
                break;
            case 6:
            case 7:
            case 8:
                long absRem = Math.abs(rem);
                long cmpRemToHalfDivisor = absRem - (Math.abs(q) - absRem);
                if (cmpRemToHalfDivisor == 0) {
                    if (mode != RoundingMode.HALF_UP && (mode != RoundingMode.HALF_EVEN || (1 & div) == 0)) {
                        r7 = false;
                    }
                    increment = r7;
                } else {
                    increment = cmpRemToHalfDivisor > 0;
                }
                if (increment) {
                }
                break;
            default:
                throw new AssertionError();
        }
    }

    public static int mod(long x, int m) {
        return (int) mod(x, m);
    }

    public static long mod(long x, long m) {
        if (m <= 0) {
            throw new ArithmeticException("Modulus must be positive");
        }
        long result = x % m;
        return result >= 0 ? result : result + m;
    }

    public static long gcd(long a, long b) {
        MathPreconditions.checkNonNegative("a", a);
        MathPreconditions.checkNonNegative("b", b);
        if (a == 0) {
            return b;
        }
        if (b == 0) {
            return a;
        }
        int aTwos = Long.numberOfTrailingZeros(a);
        long a2 = a >> aTwos;
        int bTwos = Long.numberOfTrailingZeros(b);
        long b2 = b >> bTwos;
        while (a2 != b2) {
            long delta = a2 - b2;
            long minDeltaOrZero = (delta >> 63) & delta;
            long a3 = (delta - minDeltaOrZero) - minDeltaOrZero;
            b2 += minDeltaOrZero;
            a2 = a3 >> Long.numberOfTrailingZeros(a3);
        }
        return a2 << Math.min(aTwos, bTwos);
    }

    public static long checkedAdd(long a, long b) {
        long result = a + b;
        MathPreconditions.checkNoOverflow(((a ^ b) < 0) | ((a ^ result) >= 0), "checkedAdd", a, b);
        return result;
    }

    public static long checkedSubtract(long a, long b) {
        long result = a - b;
        MathPreconditions.checkNoOverflow(((a ^ b) >= 0) | ((a ^ result) >= 0), "checkedSubtract", a, b);
        return result;
    }

    public static long checkedMultiply(long a, long b) {
        int leadingZeros = Long.numberOfLeadingZeros(a) + Long.numberOfLeadingZeros(~a) + Long.numberOfLeadingZeros(b) + Long.numberOfLeadingZeros(~b);
        if (leadingZeros > 65) {
            return a * b;
        }
        MathPreconditions.checkNoOverflow(leadingZeros >= 64, "checkedMultiply", a, b);
        MathPreconditions.checkNoOverflow((a >= 0) | (b != Long.MIN_VALUE), "checkedMultiply", a, b);
        long result = a * b;
        MathPreconditions.checkNoOverflow(a == 0 || result / a == b, "checkedMultiply", a, b);
        return result;
    }

    public static long checkedPow(long b, int k) {
        long accum;
        MathPreconditions.checkNonNegative("exponent", k);
        if ((b >= -2) & (b <= 2)) {
            switch ((int) b) {
                case -2:
                    MathPreconditions.checkNoOverflow(k < 64, "checkedPow", b, k);
                    return (k & 1) == 0 ? 1 << k : (-1) << k;
                case -1:
                    return (k & 1) == 0 ? 1L : -1L;
                case 0:
                    return k == 0 ? 1L : 0L;
                case 1:
                    return 1L;
                case 2:
                    MathPreconditions.checkNoOverflow(k < 63, "checkedPow", b, k);
                    return 1 << k;
                default:
                    throw new AssertionError();
            }
        }
        int k2 = k;
        long accum2 = 1;
        long b2 = b;
        while (true) {
            switch (k2) {
                case 0:
                    return accum2;
                case 1:
                    return checkedMultiply(accum2, b2);
                default:
                    if ((k2 & 1) == 0) {
                        accum = accum2;
                    } else {
                        accum = checkedMultiply(accum2, b2);
                    }
                    int k3 = k2 >> 1;
                    if (k3 <= 0) {
                        accum2 = accum;
                        k2 = k3;
                    } else {
                        MathPreconditions.checkNoOverflow(-3037000499L <= b2 && b2 <= FLOOR_SQRT_MAX_LONG, "checkedPow", b2, k3);
                        b2 *= b2;
                        accum2 = accum;
                        k2 = k3;
                    }
                    break;
            }
        }
    }

    public static long saturatedAdd(long a, long b) {
        long naiveSum = a + b;
        if (((a ^ b) < 0) | ((a ^ naiveSum) >= 0)) {
            return naiveSum;
        }
        return ((naiveSum >>> 63) ^ 1) + Long.MAX_VALUE;
    }

    public static long saturatedSubtract(long a, long b) {
        long naiveDifference = a - b;
        if (((a ^ b) >= 0) | ((a ^ naiveDifference) >= 0)) {
            return naiveDifference;
        }
        return ((naiveDifference >>> 63) ^ 1) + Long.MAX_VALUE;
    }

    public static long saturatedMultiply(long a, long b) {
        int leadingZeros = Long.numberOfLeadingZeros(a) + Long.numberOfLeadingZeros(~a) + Long.numberOfLeadingZeros(b) + Long.numberOfLeadingZeros(~b);
        if (leadingZeros > 65) {
            return a * b;
        }
        long limit = ((a ^ b) >>> 63) + Long.MAX_VALUE;
        if ((leadingZeros < 64) | ((b == Long.MIN_VALUE) & (a < 0))) {
            return limit;
        }
        long result = a * b;
        if (a == 0 || result / a == b) {
            return result;
        }
        return limit;
    }

    public static long saturatedPow(long b, int k) {
        MathPreconditions.checkNonNegative("exponent", k);
        if ((b >= -2) & (b <= 2)) {
            switch ((int) b) {
                case -2:
                    if (k >= 64) {
                        return (k & 1) + Long.MAX_VALUE;
                    }
                    return (k & 1) == 0 ? 1 << k : (-1) << k;
                case -1:
                    return (k & 1) == 0 ? 1L : -1L;
                case 0:
                    return k == 0 ? 1L : 0L;
                case 1:
                    return 1L;
                case 2:
                    if (k >= 63) {
                        return Long.MAX_VALUE;
                    }
                    return 1 << k;
                default:
                    throw new AssertionError();
            }
        }
        long accum = 1;
        long limit = ((b >>> 63) & k & 1) + Long.MAX_VALUE;
        while (true) {
            switch (k) {
                case 0:
                    return accum;
                case 1:
                    return saturatedMultiply(accum, b);
                default:
                    if ((k & 1) != 0) {
                        accum = saturatedMultiply(accum, b);
                    }
                    k >>= 1;
                    if (k > 0) {
                        if ((-3037000499L > b) | (b > FLOOR_SQRT_MAX_LONG)) {
                            return limit;
                        }
                        b *= b;
                    }
            }
        }
    }

    public static long factorial(int n) {
        MathPreconditions.checkNonNegative("n", n);
        long[] jArr = factorials;
        if (n < jArr.length) {
            return jArr[n];
        }
        return Long.MAX_VALUE;
    }

    public static long binomial(int n, int k) {
        int numeratorBits;
        long result;
        int numeratorBits2;
        int k2 = k;
        MathPreconditions.checkNonNegative("n", n);
        MathPreconditions.checkNonNegative("k", k2);
        Preconditions.checkArgument(k2 <= n, "k (%s) > n (%s)", k2, n);
        if (k2 > (n >> 1)) {
            k2 = n - k2;
        }
        switch (k2) {
            case 0:
                return 1L;
            case 1:
                return n;
            default:
                long[] jArr = factorials;
                if (n < jArr.length) {
                    return jArr[n] / (jArr[k2] * jArr[n - k2]);
                }
                int[] iArr = biggestBinomials;
                if (k2 >= iArr.length || n > iArr[k2]) {
                    return Long.MAX_VALUE;
                }
                int[] iArr2 = biggestSimpleBinomials;
                if (k2 < iArr2.length && n <= iArr2[k2]) {
                    int n2 = n - 1;
                    long result2 = n;
                    for (int i = 2; i <= k2; i++) {
                        result2 = (result2 * n2) / i;
                        n2--;
                    }
                    return result2;
                }
                int nBits = log2(n, RoundingMode.CEILING);
                long numerator = n;
                int numeratorBits3 = nBits;
                int n3 = n - 1;
                long numerator2 = numerator;
                long denominator = 1;
                int i2 = 2;
                long result3 = 1;
                while (i2 <= k2) {
                    if (numeratorBits3 + nBits < 63) {
                        numerator2 *= n3;
                        denominator *= i2;
                        result = result3;
                        numeratorBits2 = numeratorBits3 + nBits;
                        numeratorBits = i2;
                    } else {
                        numeratorBits = i2;
                        result = multiplyFraction(result3, numerator2, denominator);
                        long numerator3 = n3;
                        long denominator2 = numeratorBits;
                        numeratorBits2 = nBits;
                        numerator2 = numerator3;
                        denominator = denominator2;
                    }
                    i2 = numeratorBits + 1;
                    n3--;
                    numeratorBits3 = numeratorBits2;
                    result3 = result;
                }
                return multiplyFraction(result3, numerator2, denominator);
        }
    }

    static long multiplyFraction(long x, long numerator, long denominator) {
        if (x == 1) {
            return numerator / denominator;
        }
        long commonDivisor = gcd(x, denominator);
        return (numerator / (denominator / commonDivisor)) * (x / commonDivisor);
    }

    static boolean fitsInInt(long x) {
        return ((long) ((int) x)) == x;
    }

    public static long mean(long x, long y) {
        return (x & y) + ((x ^ y) >> 1);
    }

    public static boolean isPrime(long n) {
        if (n < 2) {
            MathPreconditions.checkNonNegative("n", n);
            return false;
        }
        if (n < 66) {
            return ((722865708377213483 >> (((int) n) + (-2))) & 1) != 0;
        }
        if (((1 << ((int) (n % 30))) & SIEVE_30) != 0 || n % 7 == 0 || n % 11 == 0 || n % 13 == 0) {
            return false;
        }
        if (n < 289) {
            return true;
        }
        for (long[] baseSet : millerRabinBaseSets) {
            if (n <= baseSet[0]) {
                for (int i = 1; i < baseSet.length; i++) {
                    if (!MillerRabinTester.test(baseSet[i], n)) {
                        return false;
                    }
                }
                return true;
            }
        }
        throw new AssertionError();
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes7.dex */
    public enum MillerRabinTester {
        SMALL { // from class: com.google.common.math.LongMath.MillerRabinTester.1
            @Override // com.google.common.math.LongMath.MillerRabinTester
            long mulMod(long a, long b, long m) {
                return (a * b) % m;
            }

            @Override // com.google.common.math.LongMath.MillerRabinTester
            long squareMod(long a, long m) {
                return (a * a) % m;
            }
        },
        LARGE { // from class: com.google.common.math.LongMath.MillerRabinTester.2
            private long plusMod(long a, long b, long m) {
                long j = a + b;
                return a >= m - b ? j - m : j;
            }

            private long times2ToThe32Mod(long a, long m) {
                int remainingPowersOf2 = 32;
                do {
                    int shift = Math.min(remainingPowersOf2, Long.numberOfLeadingZeros(a));
                    a = UnsignedLongs.remainder(a << shift, m);
                    remainingPowersOf2 -= shift;
                } while (remainingPowersOf2 > 0);
                return a;
            }

            @Override // com.google.common.math.LongMath.MillerRabinTester
            long mulMod(long a, long b, long m) {
                long aHi = a >>> 32;
                long bHi = b >>> 32;
                long aLo = a & 4294967295L;
                long bLo = b & 4294967295L;
                long result = times2ToThe32Mod(aHi * bHi, m) + (aHi * bLo);
                if (result < 0) {
                    result = UnsignedLongs.remainder(result, m);
                }
                Long.signum(aLo);
                return plusMod(times2ToThe32Mod(result + (aLo * bHi), m), UnsignedLongs.remainder(aLo * bLo, m), m);
            }

            @Override // com.google.common.math.LongMath.MillerRabinTester
            long squareMod(long a, long m) {
                long hiLo;
                long aHi = a >>> 32;
                long aLo = a & 4294967295L;
                long result = times2ToThe32Mod(aHi * aHi, m);
                long hiLo2 = aHi * aLo * 2;
                if (hiLo2 >= 0) {
                    hiLo = hiLo2;
                } else {
                    hiLo = UnsignedLongs.remainder(hiLo2, m);
                }
                long result2 = times2ToThe32Mod(result + hiLo, m);
                long result3 = aLo * aLo;
                return plusMod(result2, UnsignedLongs.remainder(result3, m), m);
            }
        };

        abstract long mulMod(long j, long j2, long j3);

        abstract long squareMod(long j, long j2);

        /* synthetic */ MillerRabinTester(AnonymousClass1 x2) {
            this();
        }

        static boolean test(long base, long n) {
            return (n <= LongMath.FLOOR_SQRT_MAX_LONG ? SMALL : LARGE).testWitness(base, n);
        }

        private long powMod(long a, long p, long m) {
            long res = 1;
            while (p != 0) {
                if ((1 & p) != 0) {
                    res = mulMod(res, a, m);
                }
                a = squareMod(a, m);
                p >>= 1;
            }
            return res;
        }

        private boolean testWitness(long base, long n) {
            int r = Long.numberOfTrailingZeros(n - 1);
            long d = (n - 1) >> r;
            long base2 = base % n;
            if (base2 == 0) {
                return true;
            }
            long a = powMod(base2, d, n);
            if (a == 1) {
                return true;
            }
            int j = 0;
            while (a != n - 1) {
                j++;
                if (j != r) {
                    a = squareMod(a, n);
                } else {
                    return false;
                }
            }
            return true;
        }
    }

    public static double roundToDouble(long x, RoundingMode mode) {
        int cmpXToRoundArbitrarily;
        double roundCeilingAsDouble;
        long roundCeiling;
        double roundFloorAsDouble;
        long roundFloor;
        double roundArbitrarily = x;
        long roundArbitrarilyAsLong = (long) roundArbitrarily;
        if (roundArbitrarilyAsLong == Long.MAX_VALUE) {
            cmpXToRoundArbitrarily = -1;
        } else {
            cmpXToRoundArbitrarily = Longs.compare(x, roundArbitrarilyAsLong);
        }
        switch (AnonymousClass1.$SwitchMap$java$math$RoundingMode[mode.ordinal()]) {
            case 1:
                MathPreconditions.checkRoundingUnnecessary(cmpXToRoundArbitrarily == 0);
                return roundArbitrarily;
            case 2:
                if (x < 0) {
                    return cmpXToRoundArbitrarily <= 0 ? roundArbitrarily : Math.nextUp(roundArbitrarily);
                }
                if (cmpXToRoundArbitrarily >= 0) {
                    return roundArbitrarily;
                }
                return DoubleUtils.nextDown(roundArbitrarily);
            case 3:
                if (cmpXToRoundArbitrarily >= 0) {
                    return roundArbitrarily;
                }
                return DoubleUtils.nextDown(roundArbitrarily);
            case 4:
                if (x >= 0) {
                    return cmpXToRoundArbitrarily <= 0 ? roundArbitrarily : Math.nextUp(roundArbitrarily);
                }
                if (cmpXToRoundArbitrarily >= 0) {
                    return roundArbitrarily;
                }
                return DoubleUtils.nextDown(roundArbitrarily);
            case 5:
                return cmpXToRoundArbitrarily <= 0 ? roundArbitrarily : Math.nextUp(roundArbitrarily);
            case 6:
            case 7:
            case 8:
                if (cmpXToRoundArbitrarily >= 0) {
                    roundFloorAsDouble = roundArbitrarily;
                    roundCeilingAsDouble = Math.nextUp(roundArbitrarily);
                    roundCeiling = (long) Math.ceil(roundCeilingAsDouble);
                    roundFloor = roundArbitrarilyAsLong;
                } else {
                    roundCeilingAsDouble = roundArbitrarily;
                    roundCeiling = roundArbitrarilyAsLong;
                    roundFloorAsDouble = DoubleUtils.nextDown(roundArbitrarily);
                    roundFloor = (long) Math.floor(roundFloorAsDouble);
                }
                long roundArbitrarilyAsLong2 = x - roundFloor;
                long deltaToCeiling = roundCeiling - x;
                long roundFloor2 = roundCeiling == Long.MAX_VALUE ? deltaToCeiling + 1 : deltaToCeiling;
                int diff = Longs.compare(roundArbitrarilyAsLong2, roundFloor2);
                if (diff < 0) {
                    return roundFloorAsDouble;
                }
                if (diff > 0) {
                    return roundCeilingAsDouble;
                }
                switch (AnonymousClass1.$SwitchMap$java$math$RoundingMode[mode.ordinal()]) {
                    case 6:
                        return x >= 0 ? roundFloorAsDouble : roundCeilingAsDouble;
                    case 7:
                        return x >= 0 ? roundCeilingAsDouble : roundFloorAsDouble;
                    case 8:
                        if ((DoubleUtils.getSignificand(roundFloorAsDouble) & 1) == 0) {
                            return roundFloorAsDouble;
                        }
                        return roundCeilingAsDouble;
                    default:
                        throw new AssertionError("impossible");
                }
            default:
                throw new AssertionError("impossible");
        }
    }

    private LongMath() {
    }
}
