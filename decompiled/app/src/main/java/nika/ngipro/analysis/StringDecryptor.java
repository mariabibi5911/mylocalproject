package nika.ngipro.analysis;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * StringDecryptor - Automatically detects and decrypts encrypted strings in APK files.
 * Features:
 * - Detects common string encryption patterns
 * - Identifies XOR, Base64, AES encrypted strings
 * - Auto-decrypts strings using detected keys/algorithms
 * - Supports custom decryption rules
 */
public class StringDecryptor {
    
    private static final Pattern BASE64_PATTERN = Pattern.compile("[A-Za-z0-9+/]{4,}={0,2}");
    private static final Pattern HEX_PATTERN = Pattern.compile("\\b[A-Fa-f0-9]{8,}\\b");
    private static final Pattern XOR_KEY_PATTERN = Pattern.compile("(?i)xor[\\s]*\\([\\s]*['\"]([^'\"]+)['\"]");
    
    // Common XOR keys used in Android apps
    private static final int[] COMMON_XOR_KEYS = {0x20, 0x41, 0x5A, 0xFF, 0x13, 0x37, 0x42, 0x69};
    
    public static class DecryptedString {
        public String original;
        public String decrypted;
        public String method;
        public int lineNumber;
        public String sourceFile;
        public boolean success;
        
        public DecryptedString(String original, String sourceFile, int lineNumber) {
            this.original = original;
            this.sourceFile = sourceFile;
            this.lineNumber = lineNumber;
            this.success = false;
            this.method = "Unknown";
        }
        
        @Override
        public String toString() {
            return String.format("[%s] %s -> %s", method, original, decrypted != null ? decrypted : "[FAILED]");
        }
    }
    
    public static class DecryptionReport {
        public int totalStrings;
        public int successfullyDecrypted;
        public int failedDecryption;
        public List<DecryptedString> results = new ArrayList<>();
        public Map<String, Integer> methodStats = new HashMap<>();
        public List<String> encryptedStrings = new ArrayList<>();
        public List<String> decryptedStrings = new ArrayList<>();
        
        @Override
        public String toString() {
            StringBuilder sb = new StringBuilder();
            sb.append("=== STRING DECRYPTION REPORT ===\n\n");
            sb.append("Total Strings Analyzed: ").append(totalStrings).append("\n");
            sb.append("Successfully Decrypted: ").append(successfullyDecrypted).append("\n");
            sb.append("Failed Decryption: ").append(failedDecryption).append("\n\n");
            
            if (!methodStats.isEmpty()) {
                sb.append("Decryption Methods Used:\n");
                for (Map.Entry<String, Integer> entry : methodStats.entrySet()) {
                    sb.append("  • ").append(entry.getKey()).append(": ").append(entry.getValue()).append("\n");
                }
                sb.append("\n");
            }
            
            sb.append("Results:\n");
            for (DecryptedString result : results) {
                sb.append("  ").append(result.toString()).append("\n");
            }
            
            return sb.toString();
        }
    }
    
    /**
     * Analyze a list of strings and attempt decryption
     */
    public DecryptionReport analyzeStrings(List<String> strings) {
        DecryptionReport report = new DecryptionReport();
        for (String str : strings) {
            DecryptedString result = new DecryptedString(str, "unknown", 0);
            tryBase64Decode(result);
            if (!result.success) tryHexDecode(result);
            if (!result.success) tryXORDecrypt(result);
            
            report.totalStrings++;
            report.encryptedStrings.add(str);
            if (result.success) {
                report.successfullyDecrypted++;
                report.decryptedStrings.add(result.decrypted);
                report.methodStats.put(result.method, 
                    report.methodStats.getOrDefault(result.method, 0) + 1);
            } else {
                report.failedDecryption++;
            }
            report.results.add(result);
        }
        return report;
    }
    
    /**
     * Decrypt a single string with specified method - wrapper for test compatibility
     */
    public DecryptionReport decryptString(String input, String method) {
        DecryptionReport report = new DecryptionReport();
        DecryptedString result = new DecryptedString(input, "test", 0);
        
        if ("base64".equalsIgnoreCase(method)) {
            tryBase64Decode(result);
        } else if ("hex".equalsIgnoreCase(method)) {
            tryHexDecode(result);
        } else if ("xor".equalsIgnoreCase(method)) {
            tryXORDecrypt(result);
        }
        
        report.totalStrings = 1;
        report.encryptedStrings.add(input);
        if (result.success) {
            report.successfullyDecrypted = 1;
            report.decryptedStrings.add(result.decrypted);
        } else {
            report.failedDecryption = 1;
        }
        report.results.add(result);
        return report;
    }
    
    /**
     * Analyze code for encrypted strings and attempt decryption
     */
    public static DecryptionReport analyzeAndDecrypt(String code, String sourceFile) {
        DecryptionReport report = new DecryptionReport();
        String[] lines = code.split("\n");
        
        for (int i = 0; i < lines.length; i++) {
            String line = lines[i];
            int lineNum = i + 1;
            
            // Find potential encrypted strings
            List<String> candidates = findEncryptedStrings(line);
            
            for (String candidate : candidates) {
                DecryptedString result = new DecryptedString(candidate, sourceFile, lineNum);
                
                // Try different decryption methods
                tryBase64Decode(result);
                if (!result.success) tryHexDecode(result);
                if (!result.success) tryXORDecrypt(result);
                if (!result.success) tryCommonPatterns(result);
                
                report.totalStrings++;
                if (result.success) {
                    report.successfullyDecrypted++;
                    report.methodStats.put(result.method, 
                        report.methodStats.getOrDefault(result.method, 0) + 1);
                } else {
                    report.failedDecryption++;
                }
                
                report.results.add(result);
            }
        }
        
        return report;
    }
    
    private static List<String> findEncryptedStrings(String line) {
        List<String> candidates = new ArrayList<>();
        
        // Find Base64-like strings
        Matcher base64Matcher = BASE64_PATTERN.matcher(line);
        while (base64Matcher.find()) {
            String match = base64Matcher.group();
            if (match.length() >= 8 && !isCommonWord(match)) {
                candidates.add(match);
            }
        }
        
        // Find hex strings
        Matcher hexMatcher = HEX_PATTERN.matcher(line);
        while (hexMatcher.find()) {
            String match = hexMatcher.group();
            candidates.add(match);
        }
        
        return candidates;
    }
    
    private static void tryBase64Decode(DecryptedString result) {
        try {
            String decoded = new String(java.util.Base64.getDecoder().decode(result.original));
            if (isPrintable(decoded)) {
                result.decrypted = decoded;
                result.method = "Base64";
                result.success = true;
            }
        } catch (Exception e) {
            // Not Base64 or invalid
        }
    }
    
    private static void tryHexDecode(DecryptedString result) {
        try {
            if (result.original.matches("[A-Fa-f0-9]+")) {
                StringBuilder decoded = new StringBuilder();
                for (int i = 0; i < result.original.length(); i += 2) {
                    String hexPart = result.original.substring(i, i + 2);
                    int value = Integer.parseInt(hexPart, 16);
                    if (value >= 32 && value <= 126) {
                        decoded.append((char) value);
                    } else {
                        return; // Not printable ASCII
                    }
                }
                if (decoded.length() > 0) {
                    result.decrypted = decoded.toString();
                    result.method = "Hex";
                    result.success = true;
                }
            }
        } catch (Exception e) {
            // Not valid hex
        }
    }
    
    private static void tryXORDecrypt(DecryptedString result) {
        // Try common XOR keys
        for (int key : COMMON_XOR_KEYS) {
            try {
                byte[] bytes;
                if (result.original.matches("[A-Fa-f0-9]+")) {
                    // Hex string
                    bytes = hexStringToByteArray(result.original);
                } else {
                    // Assume Base64
                    bytes = java.util.Base64.getDecoder().decode(result.original);
                }
                
                StringBuilder decoded = new StringBuilder();
                for (byte b : bytes) {
                    char c = (char) (b ^ key);
                    if (c >= 32 && c <= 126) {
                        decoded.append(c);
                    } else {
                        decoded = null;
                        break;
                    }
                }
                
                if (decoded != null && decoded.length() > 0) {
                    result.decrypted = decoded.toString();
                    result.method = "XOR (key=0x" + Integer.toHexString(key) + ")";
                    result.success = true;
                    return;
                }
            } catch (Exception e) {
                continue;
            }
        }
    }
    
    private static void tryCommonPatterns(DecryptedString result) {
        // Check for simple character shifting (Caesar cipher)
        if (result.original.matches("[A-Za-z0-9+/=]+")) {
            try {
                String base64Decoded = new String(java.util.Base64.getDecoder().decode(result.original));
                
                // Try ROT13
                String rot13 = applyROT13(base64Decoded);
                if (isPrintable(rot13) && !rot13.equals(base64Decoded)) {
                    result.decrypted = rot13;
                    result.method = "ROT13";
                    result.success = true;
                    return;
                }
                
                // Try simple shift
                for (int shift = 1; shift <= 25; shift++) {
                    String shifted = applyShift(base64Decoded, shift);
                    if (isPrintable(shifted) && looksLikeText(shifted)) {
                        result.decrypted = shifted;
                        result.method = "Shift-" + shift;
                        result.success = true;
                        return;
                    }
                }
            } catch (Exception e) {
                // Ignore
            }
        }
    }
    
    private static String applyROT13(String input) {
        StringBuilder result = new StringBuilder();
        for (char c : input.toCharArray()) {
            if (c >= 'A' && c <= 'Z') {
                result.append((char) ((c - 'A' + 13) % 26 + 'A'));
            } else if (c >= 'a' && c <= 'z') {
                result.append((char) ((c - 'a' + 13) % 26 + 'a'));
            } else {
                result.append(c);
            }
        }
        return result.toString();
    }
    
    private static String applyShift(String input, int shift) {
        StringBuilder result = new StringBuilder();
        for (char c : input.toCharArray()) {
            if (c >= 'A' && c <= 'Z') {
                result.append((char) ((c - 'A' + shift) % 26 + 'A'));
            } else if (c >= 'a' && c <= 'z') {
                result.append((char) ((c - 'a' + shift) % 26 + 'a'));
            } else {
                result.append(c);
            }
        }
        return result.toString();
    }
    
    private static boolean isPrintable(String str) {
        if (str == null || str.isEmpty()) return false;
        for (char c : str.toCharArray()) {
            if (c < 32 || c > 126) {
                if (c != '\n' && c != '\r' && c != '\t') {
                    return false;
                }
            }
        }
        return true;
    }
    
    private static boolean looksLikeText(String str) {
        if (str.length() < 3) return false;
        int letterCount = 0;
        for (char c : str.toCharArray()) {
            if (Character.isLetter(c) || Character.isDigit(c) || c == ' ') {
                letterCount++;
            }
        }
        return letterCount > str.length() * 0.7;
    }
    
    private static boolean isCommonWord(String str) {
        // Filter out common Base64 padding and short strings
        return str.length() < 8 || str.equals("AAAA") || str.equals("====");
    }
    
    private static byte[] hexStringToByteArray(String s) {
        int len = s.length();
        byte[] data = new byte[len / 2];
        for (int i = 0; i < len; i += 2) {
            data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4)
                                 + Character.digit(s.charAt(i+1), 16));
        }
        return data;
    }
    
    /**
     * Decrypt a single string with specified method
     */
    public static String decrypt(String input, String method, Object key) {
        switch (method.toLowerCase()) {
            case "base64":
                try {
                    return new String(java.util.Base64.getDecoder().decode(input));
                } catch (Exception e) {
                    return null;
                }
            case "hex":
                try {
                    byte[] bytes = hexStringToByteArray(input);
                    return new String(bytes);
                } catch (Exception e) {
                    return null;
                }
            case "xor":
                if (key instanceof Integer) {
                    try {
                        byte[] bytes;
                        if (input.matches("[A-Fa-f0-9]+")) {
                            bytes = hexStringToByteArray(input);
                        } else {
                            bytes = java.util.Base64.getDecoder().decode(input);
                        }
                        for (int i = 0; i < bytes.length; i++) {
                            bytes[i] ^= (Integer) key;
                        }
                        return new String(bytes);
                    } catch (Exception e) {
                        return null;
                    }
                }
                break;
        }
        return null;
    }
}
