package com.android.tools.smali.util;

import androidx.exifinterface.media.ExifInterface;
import java.text.DecimalFormat;

/* loaded from: classes.dex */
public class NumberUtils {
    private static final int canonicalFloatNaN = Float.floatToRawIntBits(Float.NaN);
    private static final int maxFloat = Float.floatToRawIntBits(Float.MAX_VALUE);
    private static final int piFloat = Float.floatToRawIntBits(3.1415927f);
    private static final int eFloat = Float.floatToRawIntBits(2.7182817f);
    private static final long canonicalDoubleNaN = Double.doubleToRawLongBits(Double.NaN);
    private static final long maxDouble = Double.doubleToLongBits(Double.MAX_VALUE);
    private static final long piDouble = Double.doubleToLongBits(3.141592653589793d);
    private static final long eDouble = Double.doubleToLongBits(2.718281828459045d);
    private static final DecimalFormat format = new DecimalFormat("0.####################E0");

    public static boolean isLikelyFloat(int value) {
        if (value == canonicalFloatNaN || value == maxFloat || value == piFloat || value == eFloat) {
            return true;
        }
        if (value == Integer.MAX_VALUE || value == Integer.MIN_VALUE) {
            return false;
        }
        int packageId = value >> 24;
        int resourceType = (value >> 16) & 255;
        int resourceId = 65535 & value;
        if ((packageId == 127 || packageId == 1) && resourceType < 31 && resourceId < 4095) {
            return false;
        }
        float floatValue = Float.intBitsToFloat(value);
        if (Float.isNaN(floatValue)) {
            return false;
        }
        DecimalFormat decimalFormat = format;
        String asInt = decimalFormat.format(value);
        String asFloat = decimalFormat.format(floatValue);
        int decimalPoint = asFloat.indexOf(46);
        int exponent = asFloat.indexOf(ExifInterface.LONGITUDE_EAST);
        int zeros = asFloat.indexOf("000");
        if (zeros > decimalPoint && zeros < exponent) {
            asFloat = asFloat.substring(0, zeros) + asFloat.substring(exponent);
        } else {
            int nines = asFloat.indexOf("999");
            if (nines > decimalPoint && nines < exponent) {
                asFloat = asFloat.substring(0, nines) + asFloat.substring(exponent);
            }
        }
        return asFloat.length() < asInt.length();
    }

    public static boolean isLikelyDouble(long value) {
        if (value == canonicalDoubleNaN || value == maxDouble || value == piDouble || value == eDouble) {
            return true;
        }
        if (value == Long.MAX_VALUE || value == Long.MIN_VALUE) {
            return false;
        }
        double doubleValue = Double.longBitsToDouble(value);
        if (Double.isNaN(doubleValue)) {
            return false;
        }
        DecimalFormat decimalFormat = format;
        String asLong = decimalFormat.format(value);
        String asDouble = decimalFormat.format(doubleValue);
        int decimalPoint = asDouble.indexOf(46);
        int exponent = asDouble.indexOf(ExifInterface.LONGITUDE_EAST);
        int zeros = asDouble.indexOf("000");
        if (zeros > decimalPoint && zeros < exponent) {
            asDouble = asDouble.substring(0, zeros) + asDouble.substring(exponent);
        } else {
            int nines = asDouble.indexOf("999");
            if (nines > decimalPoint && nines < exponent) {
                asDouble = asDouble.substring(0, nines) + asDouble.substring(exponent);
            }
        }
        return asDouble.length() < asLong.length();
    }
}
