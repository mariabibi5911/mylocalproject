package com.google.common.math;

import java.math.BigInteger;
import java.math.RoundingMode;

@ElementTypesAreNonnullByDefault
/* loaded from: classes7.dex */
final class MathPreconditions {
    /* JADX INFO: Access modifiers changed from: package-private */
    public static int checkPositive(String role, int x) {
        if (x <= 0) {
            throw new IllegalArgumentException(new StringBuilder(String.valueOf(role).length() + 26).append(role).append(" (").append(x).append(") must be > 0").toString());
        }
        return x;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static long checkPositive(String role, long x) {
        if (x <= 0) {
            throw new IllegalArgumentException(new StringBuilder(String.valueOf(role).length() + 35).append(role).append(" (").append(x).append(") must be > 0").toString());
        }
        return x;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static BigInteger checkPositive(String role, BigInteger x) {
        if (x.signum() <= 0) {
            String valueOf = String.valueOf(x);
            throw new IllegalArgumentException(new StringBuilder(String.valueOf(role).length() + 15 + String.valueOf(valueOf).length()).append(role).append(" (").append(valueOf).append(") must be > 0").toString());
        }
        return x;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static int checkNonNegative(String role, int x) {
        if (x < 0) {
            throw new IllegalArgumentException(new StringBuilder(String.valueOf(role).length() + 27).append(role).append(" (").append(x).append(") must be >= 0").toString());
        }
        return x;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static long checkNonNegative(String role, long x) {
        if (x < 0) {
            throw new IllegalArgumentException(new StringBuilder(String.valueOf(role).length() + 36).append(role).append(" (").append(x).append(") must be >= 0").toString());
        }
        return x;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static BigInteger checkNonNegative(String role, BigInteger x) {
        if (x.signum() < 0) {
            String valueOf = String.valueOf(x);
            throw new IllegalArgumentException(new StringBuilder(String.valueOf(role).length() + 16 + String.valueOf(valueOf).length()).append(role).append(" (").append(valueOf).append(") must be >= 0").toString());
        }
        return x;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static double checkNonNegative(String role, double x) {
        if (x < 0.0d) {
            throw new IllegalArgumentException(new StringBuilder(String.valueOf(role).length() + 40).append(role).append(" (").append(x).append(") must be >= 0").toString());
        }
        return x;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static void checkRoundingUnnecessary(boolean condition) {
        if (!condition) {
            throw new ArithmeticException("mode was UNNECESSARY, but rounding was necessary");
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static void checkInRangeForRoundingInputs(boolean condition, double input, RoundingMode mode) {
        if (!condition) {
            String valueOf = String.valueOf(mode);
            throw new ArithmeticException(new StringBuilder(String.valueOf(valueOf).length() + 83).append("rounded value is out of range for input ").append(input).append(" and rounding mode ").append(valueOf).toString());
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static void checkNoOverflow(boolean condition, String methodName, int a, int b) {
        if (!condition) {
            throw new ArithmeticException(new StringBuilder(String.valueOf(methodName).length() + 36).append("overflow: ").append(methodName).append("(").append(a).append(", ").append(b).append(")").toString());
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static void checkNoOverflow(boolean condition, String methodName, long a, long b) {
        if (!condition) {
            throw new ArithmeticException(new StringBuilder(String.valueOf(methodName).length() + 54).append("overflow: ").append(methodName).append("(").append(a).append(", ").append(b).append(")").toString());
        }
    }

    private MathPreconditions() {
    }
}
