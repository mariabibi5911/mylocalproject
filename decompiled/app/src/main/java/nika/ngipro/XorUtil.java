package nika.ngipro;

import android.util.Base64;
import java.nio.charset.StandardCharsets;

/* loaded from: classes4.dex */
public final class XorUtil {
    private static final byte[] KEY = "NGI_PRO_2026_SECURE_KEY".getBytes(StandardCharsets.UTF_8);

    private XorUtil() {
    }

    private static byte[] xor(byte[] input) {
        byte[] output = new byte[input.length];
        for (int i = 0; i < input.length; i++) {
            byte b = input[i];
            byte[] bArr = KEY;
            output[i] = (byte) (b ^ bArr[i % bArr.length]);
        }
        return output;
    }

    public static String obfuscate(String plainText) {
        if (plainText == null) {
            return null;
        }
        byte[] xored = xor(plainText.getBytes(StandardCharsets.UTF_8));
        return Base64.encodeToString(xored, 2);
    }

    public static String deobfuscate(String obfuscatedText) {
        if (obfuscatedText == null) {
            return null;
        }
        try {
            byte[] decoded = Base64.decode(obfuscatedText, 2);
            byte[] original = xor(decoded);
            return new String(original, StandardCharsets.UTF_8);
        } catch (Exception e) {
            return null;
        }
    }
}
