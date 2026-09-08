package com.google.common.math;

import com.google.common.base.Preconditions;
import java.lang.Comparable;
import java.lang.Number;
import java.math.RoundingMode;

@ElementTypesAreNonnullByDefault
/* loaded from: classes7.dex */
abstract class ToDoubleRounder<X extends Number & Comparable<X>> {
    abstract X minus(X x, X x2);

    abstract double roundToDoubleArbitrarily(X x);

    abstract int sign(X x);

    abstract X toX(double d, RoundingMode roundingMode);

    /* JADX INFO: Access modifiers changed from: package-private */
    public final double roundToDouble(X x, RoundingMode mode) {
        double roundCeilingAsDouble;
        X x2;
        X x3;
        double roundFloorAsDouble;
        Preconditions.checkNotNull(x, "x");
        Preconditions.checkNotNull(mode, "mode");
        double roundArbitrarily = roundToDoubleArbitrarily(x);
        if (Double.isInfinite(roundArbitrarily)) {
            switch (AnonymousClass1.$SwitchMap$java$math$RoundingMode[mode.ordinal()]) {
                case 1:
                case 2:
                case 3:
                case 4:
                    return sign(x) * Double.MAX_VALUE;
                case 5:
                    return roundArbitrarily == Double.POSITIVE_INFINITY ? Double.MAX_VALUE : Double.NEGATIVE_INFINITY;
                case 6:
                    return roundArbitrarily == Double.POSITIVE_INFINITY ? Double.POSITIVE_INFINITY : -1.7976931348623157E308d;
                case 7:
                    return roundArbitrarily;
                case 8:
                    String valueOf = String.valueOf(x);
                    throw new ArithmeticException(new StringBuilder(String.valueOf(valueOf).length() + 44).append(valueOf).append(" cannot be represented precisely as a double").toString());
            }
        }
        X x4 = toX(roundArbitrarily, RoundingMode.UNNECESSARY);
        int cmpXToRoundArbitrarily = ((Comparable) x).compareTo(x4);
        switch (AnonymousClass1.$SwitchMap$java$math$RoundingMode[mode.ordinal()]) {
            case 1:
                if (sign(x) < 0) {
                    return cmpXToRoundArbitrarily <= 0 ? roundArbitrarily : Math.nextUp(roundArbitrarily);
                }
                if (cmpXToRoundArbitrarily >= 0) {
                    return roundArbitrarily;
                }
                return DoubleUtils.nextDown(roundArbitrarily);
            case 2:
            case 3:
            case 4:
                if (cmpXToRoundArbitrarily >= 0) {
                    roundFloorAsDouble = roundArbitrarily;
                    x3 = x4;
                    roundCeilingAsDouble = Math.nextUp(roundArbitrarily);
                    if (roundCeilingAsDouble != Double.POSITIVE_INFINITY) {
                        x2 = toX(roundCeilingAsDouble, RoundingMode.CEILING);
                    } else {
                        return roundFloorAsDouble;
                    }
                } else {
                    roundCeilingAsDouble = roundArbitrarily;
                    x2 = x4;
                    double roundFloorAsDouble2 = DoubleUtils.nextDown(roundArbitrarily);
                    if (roundFloorAsDouble2 != Double.NEGATIVE_INFINITY) {
                        x3 = toX(roundFloorAsDouble2, RoundingMode.FLOOR);
                        roundFloorAsDouble = roundFloorAsDouble2;
                    } else {
                        return roundCeilingAsDouble;
                    }
                }
                int diff = ((Comparable) minus(x, x3)).compareTo(minus(x2, x));
                if (diff < 0) {
                    return roundFloorAsDouble;
                }
                if (diff > 0) {
                    return roundCeilingAsDouble;
                }
                switch (AnonymousClass1.$SwitchMap$java$math$RoundingMode[mode.ordinal()]) {
                    case 2:
                        if ((Double.doubleToRawLongBits(roundFloorAsDouble) & 1) == 0) {
                            return roundFloorAsDouble;
                        }
                        return roundCeilingAsDouble;
                    case 3:
                        return sign(x) >= 0 ? roundFloorAsDouble : roundCeilingAsDouble;
                    case 4:
                        return sign(x) >= 0 ? roundCeilingAsDouble : roundFloorAsDouble;
                    default:
                        throw new AssertionError("impossible");
                }
            case 5:
                if (cmpXToRoundArbitrarily >= 0) {
                    return roundArbitrarily;
                }
                return DoubleUtils.nextDown(roundArbitrarily);
            case 6:
                return cmpXToRoundArbitrarily <= 0 ? roundArbitrarily : Math.nextUp(roundArbitrarily);
            case 7:
                if (sign(x) >= 0) {
                    return cmpXToRoundArbitrarily <= 0 ? roundArbitrarily : Math.nextUp(roundArbitrarily);
                }
                if (cmpXToRoundArbitrarily >= 0) {
                    return roundArbitrarily;
                }
                return DoubleUtils.nextDown(roundArbitrarily);
            case 8:
                MathPreconditions.checkRoundingUnnecessary(cmpXToRoundArbitrarily == 0);
                return roundArbitrarily;
            default:
                throw new AssertionError("impossible");
        }
    }

    /* renamed from: com.google.common.math.ToDoubleRounder$1, reason: invalid class name */
    /* loaded from: classes7.dex */
    static /* synthetic */ class AnonymousClass1 {
        static final /* synthetic */ int[] $SwitchMap$java$math$RoundingMode;

        static {
            int[] iArr = new int[RoundingMode.values().length];
            $SwitchMap$java$math$RoundingMode = iArr;
            try {
                iArr[RoundingMode.DOWN.ordinal()] = 1;
            } catch (NoSuchFieldError e) {
            }
            try {
                $SwitchMap$java$math$RoundingMode[RoundingMode.HALF_EVEN.ordinal()] = 2;
            } catch (NoSuchFieldError e2) {
            }
            try {
                $SwitchMap$java$math$RoundingMode[RoundingMode.HALF_DOWN.ordinal()] = 3;
            } catch (NoSuchFieldError e3) {
            }
            try {
                $SwitchMap$java$math$RoundingMode[RoundingMode.HALF_UP.ordinal()] = 4;
            } catch (NoSuchFieldError e4) {
            }
            try {
                $SwitchMap$java$math$RoundingMode[RoundingMode.FLOOR.ordinal()] = 5;
            } catch (NoSuchFieldError e5) {
            }
            try {
                $SwitchMap$java$math$RoundingMode[RoundingMode.CEILING.ordinal()] = 6;
            } catch (NoSuchFieldError e6) {
            }
            try {
                $SwitchMap$java$math$RoundingMode[RoundingMode.UP.ordinal()] = 7;
            } catch (NoSuchFieldError e7) {
            }
            try {
                $SwitchMap$java$math$RoundingMode[RoundingMode.UNNECESSARY.ordinal()] = 8;
            } catch (NoSuchFieldError e8) {
            }
        }
    }
}
