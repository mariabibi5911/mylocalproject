package nika.ngipro.security;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Hardcoded Secret Scanner - Detects API keys, tokens, passwords in code
 * Scans for common secret patterns and hardcoded credentials
 */
public class SecretScanner {
    
    public static class SecretFinding {
        public enum SecretType {
            API_KEY,
            AWS_KEY,
            FIREBASE_KEY,
            GOOGLE_API_KEY,
            FACEBOOK_APP_ID,
            TWITTER_API_KEY,
            GITHUB_TOKEN,
            STRIPE_KEY,
            SENDGRID_KEY,
            MAILGUN_KEY,
            SLACK_TOKEN,
            DATABASE_PASSWORD,
            JWT_SECRET,
            ENCRYPTION_KEY,
            OAUTH_TOKEN,
            PRIVATE_KEY,
            CUSTOM_SECRET
        }
        
        public SecretType type;
        public String value;
        public String location; // File path or class name
        public int lineNumber;
        public String context; // Surrounding code
        public RiskLevel riskLevel;
        public String recommendation;
        
        public enum RiskLevel {
            CRITICAL, HIGH, MEDIUM, LOW, INFO
        }
        
        @Override
        public String toString() {
            return String.format("[%s] %s found at %s:%d\nValue: %s\nRisk: %s\nRecommendation: %s",
                type, type.name(), location, lineNumber, maskSecret(value), riskLevel, recommendation);
        }
        
        private String maskSecret(String secret) {
            if (secret == null || secret.length() < 8) return "****";
            return secret.substring(0, 4) + "..." + secret.substring(secret.length() - 4);
        }
    }
    
    public static class ScanReport {
        public List<SecretFinding> findings = new ArrayList<>();
        public int totalSecretsFound;
        public Map<SecretFinding.SecretType, Integer> secretsByType = new HashMap<>();
        public Map<SecretFinding.RiskLevel, Integer> secretsByRisk = new HashMap<>();
        public long scanDurationMs;
        public boolean completedSuccessfully;
        
        public String generateReport() {
            StringBuilder report = new StringBuilder();
            report.append("===========================================\n");
            report.append("   NGI PRO 2.0 - HARDCODED SECRET SCAN    \n");
            report.append("===========================================\n\n");
            
            report.append("Scan Duration: ").append(scanDurationMs).append("ms\n");
            report.append("Status: ").append(completedSuccessfully ? "COMPLETED" : "FAILED").append("\n");
            report.append("Total Secrets Found: ").append(totalSecretsFound).append("\n\n");
            
            if (!secretsByType.isEmpty()) {
                report.append("--- Secrets by Type ---\n");
                for (Map.Entry<SecretFinding.SecretType, Integer> entry : secretsByType.entrySet()) {
                    report.append(entry.getKey().name()).append(": ").append(entry.getValue()).append("\n");
                }
                report.append("\n");
            }
            
            if (!secretsByRisk.isEmpty()) {
                report.append("--- Secrets by Risk Level ---\n");
                for (Map.Entry<SecretFinding.RiskLevel, Integer> entry : secretsByRisk.entrySet()) {
                    report.append(entry.getKey().name()).append(": ").append(entry.getValue()).append("\n");
                }
                report.append("\n");
            }
            
            if (!findings.isEmpty()) {
                report.append("--- Detailed Findings ---\n\n");
                for (int i = 0; i < findings.size(); i++) {
                    report.append("[").append(i + 1).append("] ").append(findings.get(i).toString()).append("\n\n");
                }
            } else {
                report.append("✅ No hardcoded secrets detected!\n");
            }
            
            return report.toString();
        }
    }
    
    // Pattern definitions for various secret types
    private static final Map<SecretFinding.SecretType, Pattern> SECRET_PATTERNS = new HashMap<>();
    
    static {
        // AWS Keys
        SECRET_PATTERNS.put(SecretFinding.SecretType.AWS_KEY, 
            Pattern.compile("(?i)(AKIA[0-9A-Z]{16}|aws[_-]?access[_-]?key[_-]?id[\\s]*[=:][\\s]*['\"]?([A-Z0-9]{20})['\"]?)"));
        
        // Google API Keys
        SECRET_PATTERNS.put(SecretFinding.SecretType.GOOGLE_API_KEY,
            Pattern.compile("(?i)(AIza[0-9A-Za-z\\-_]{35}|google[_-]?api[_-]?key[\\s]*[=:][\\s]*['\"]?([A-Za-z0-9\\-_]{39})['\"]?)"));
        
        // Firebase Keys
        SECRET_PATTERNS.put(SecretFinding.SecretType.FIREBASE_KEY,
            Pattern.compile("(?i)(firebase[_-]?(database[_-]?)?url|AIza[0-9A-Za-z\\-_]{35})"));
        
        // Facebook App ID/Secret
        SECRET_PATTERNS.put(SecretFinding.SecretType.FACEBOOK_APP_ID,
            Pattern.compile("(?i)(facebook[_-]?app[_-]?id[\\s]*[=:][\\s]*['\"]?([0-9]{10,})['\"]?|fb[\\s]*[=:][\\s]*['\"]?([0-9]{10,})['\"]?)"));
        
        // Twitter API Keys
        SECRET_PATTERNS.put(SecretFinding.SecretType.TWITTER_API_KEY,
            Pattern.compile("(?i)(twitter[_-]?api[_-]?key|twitter[_-]?consumer[_-]?key)[\\s]*[=:][\\s]*['\"]?([A-Za-z0-9]{25,})['\"]?"));
        
        // GitHub Tokens
        SECRET_PATTERNS.put(SecretFinding.SecretType.GITHUB_TOKEN,
            Pattern.compile("(?i)(ghp_[A-Za-z0-9]{36}|github[_-]?token[\\s]*[=:][\\s]*['\"]?([A-Za-z0-9]{35,})['\"]?)"));
        
        // Stripe Keys
        SECRET_PATTERNS.put(SecretFinding.SecretType.STRIPE_KEY,
            Pattern.compile("(?i)(sk_live_[0-9a-zA-Z]{24}|rk_live_[0-9a-zA-Z]{24}|stripe[_-]?key[\\s]*[=:][\\s]*['\"]?([A-Za-z0-9]{30,})['\"]?)"));
        
        // SendGrid Keys
        SECRET_PATTERNS.put(SecretFinding.SecretType.SENDGRID_KEY,
            Pattern.compile("(?i)(SG\\.[A-Za-z0-9\\-_]{22}\\.[A-Za-z0-9\\-_]{43}|sendgrid[_-]?key[\\s]*[=:][\\s]*['\"]?([A-Za-z0-9\\-_]{60,})['\"]?)"));
        
        // Mailgun Keys
        SECRET_PATTERNS.put(SecretFinding.SecretType.MAILGUN_KEY,
            Pattern.compile("(?i)(key-[0-9a-zA-Z]{32}|mailgun[_-]?api[_-]?key[\\s]*[=:][\\s]*['\"]?([A-Za-z0-9\\-_]{30,})['\"]?)"));
        
        // Slack Tokens
        SECRET_PATTERNS.put(SecretFinding.SecretType.SLACK_TOKEN,
            Pattern.compile("(?i)(xox[baprs]-[0-9A-Za-z\\-]{10,48}|slack[_-]?token[\\s]*[=:][\\s]*['\"]?([A-Za-z0-9\\-]{30,})['\"]?)"));
        
        // Generic API Keys
        SECRET_PATTERNS.put(SecretFinding.SecretType.API_KEY,
            Pattern.compile("(?i)(api[_-]?key|apikey)[\\s]*[=:][\\s]*['\"]?([A-Za-z0-9\\-_\\./+]{16,})['\"]?"));
        
        // Database Passwords
        SECRET_PATTERNS.put(SecretFinding.SecretType.DATABASE_PASSWORD,
            Pattern.compile("(?i)(db[_-]?pass(word)?|database[_-]?password|mysql[_-]?pass|postgres[_-]?pass)[\\s]*[=:][\\s]*['\"]?([^\\s'\"]{4,})['\"]?"));
        
        // JWT Secrets
        SECRET_PATTERNS.put(SecretFinding.SecretType.JWT_SECRET,
            Pattern.compile("(?i)(jwt[_-]?secret|jwt[_-]?key)[\\s]*[=:][\\s]*['\"]?([A-Za-z0-9\\-_\\./+]{16,})['\"]?"));
        
        // Encryption Keys
        SECRET_PATTERNS.put(SecretFinding.SecretType.ENCRYPTION_KEY,
            Pattern.compile("(?i)(encrypt[_-]?key|encryption[_-]?key|aes[_-]?key|des[_-]?key)[\\s]*[=:][\\s]*['\"]?([A-Fa-f0-9]{16,})['\"]?"));
        
        // OAuth Tokens
        SECRET_PATTERNS.put(SecretFinding.SecretType.OAUTH_TOKEN,
            Pattern.compile("(?i)(oauth[_-]?token|access[_-]?token|bearer[_-]?token)[\\s]*[=:][\\s]*['\"]?([A-Za-z0-9\\-_\\./+]{20,})['\"]?"));
        
        // Private Keys (PEM format)
        SECRET_PATTERNS.put(SecretFinding.SecretType.PRIVATE_KEY,
            Pattern.compile("-----BEGIN (RSA |EC |DSA )?PRIVATE KEY-----[\\s\\S]{50,}-----END (RSA |EC |DSA )?PRIVATE KEY-----"));
        
        // Generic Secrets
        SECRET_PATTERNS.put(SecretFinding.SecretType.CUSTOM_SECRET,
            Pattern.compile("(?i)(secret|password|passwd|pwd)[\\s]*[=:][\\s]*['\"]?([^\\s'\"]{8,})['\"]?"));
    }
    
    /**
     * Scan source code for hardcoded secrets
     */
    public ScanReport scanSourceCode(String sourceCode, String fileName) {
        long startTime = System.currentTimeMillis();
        ScanReport report = new ScanReport();
        
        try {
            String[] lines = sourceCode.split("\\r?\\n");
            
            for (int lineNum = 0; lineNum < lines.length; lineNum++) {
                String line = lines[lineNum];
                
                for (Map.Entry<SecretFinding.SecretType, Pattern> entry : SECRET_PATTERNS.entrySet()) {
                    Matcher matcher = entry.getValue().matcher(line);
                    
                    while (matcher.find()) {
                        SecretFinding finding = new SecretFinding();
                        finding.type = entry.getKey();
                        finding.value = matcher.groupCount() > 0 ? matcher.group(matcher.groupCount()) : matcher.group();
                        finding.location = fileName;
                        finding.lineNumber = lineNum + 1;
                        finding.context = extractContext(lines, lineNum);
                        finding.riskLevel = assessRisk(finding.type, finding.value);
                        finding.recommendation = getRecommendation(finding.type);
                        
                        report.findings.add(finding);
                        
                        // Update statistics
                        report.secretsByType.merge(finding.type, 1, Integer::sum);
                        report.secretsByRisk.merge(finding.riskLevel, 1, Integer::sum);
                    }
                }
            }
            
            report.totalSecretsFound = report.findings.size();
            report.completedSuccessfully = true;
            
        } catch (Exception e) {
            report.completedSuccessfully = false;
            e.printStackTrace();
        } finally {
            report.scanDurationMs = System.currentTimeMillis() - startTime;
        }
        
        return report;
    }
    
    /**
     * Scan multiple files
     */
    public ScanReport scanMultipleFiles(Map<String, String> fileContents) {
        long startTime = System.currentTimeMillis();
        ScanReport aggregateReport = new ScanReport();
        
        for (Map.Entry<String, String> entry : fileContents.entrySet()) {
            ScanReport fileReport = scanSourceCode(entry.getValue(), entry.getKey());
            aggregateReport.findings.addAll(fileReport.findings);
            
            // Merge statistics
            for (Map.Entry<SecretFinding.SecretType, Integer> stat : fileReport.secretsByType.entrySet()) {
                aggregateReport.secretsByType.merge(stat.getKey(), stat.getValue(), Integer::sum);
            }
            for (Map.Entry<SecretFinding.RiskLevel, Integer> stat : fileReport.secretsByRisk.entrySet()) {
                aggregateReport.secretsByRisk.merge(stat.getKey(), stat.getValue(), Integer::sum);
            }
        }
        
        aggregateReport.totalSecretsFound = aggregateReport.findings.size();
        aggregateReport.scanDurationMs = System.currentTimeMillis() - startTime;
        aggregateReport.completedSuccessfully = true;
        
        return aggregateReport;
    }
    
    private String extractContext(String[] lines, int lineNum) {
        int start = Math.max(0, lineNum - 2);
        int end = Math.min(lines.length - 1, lineNum + 2);
        
        StringBuilder context = new StringBuilder();
        for (int i = start; i <= end; i++) {
            if (i == lineNum) {
                context.append(">>> ");
            }
            context.append(lines[i].trim()).append("\n");
        }
        
        return context.toString().trim();
    }
    
    private SecretFinding.RiskLevel assessRisk(SecretFinding.SecretType type, String value) {
        switch (type) {
            case AWS_KEY:
            case PRIVATE_KEY:
            case DATABASE_PASSWORD:
            case STRIPE_KEY:
                return SecretFinding.RiskLevel.CRITICAL;
                
            case API_KEY:
            case FIREBASE_KEY:
            case GITHUB_TOKEN:
            case JWT_SECRET:
            case ENCRYPTION_KEY:
                return SecretFinding.RiskLevel.HIGH;
                
            case GOOGLE_API_KEY:
            case FACEBOOK_APP_ID:
            case TWITTER_API_KEY:
            case SENDGRID_KEY:
            case MAILGUN_KEY:
            case OAUTH_TOKEN:
                return SecretFinding.RiskLevel.MEDIUM;
                
            case SLACK_TOKEN:
            case CUSTOM_SECRET:
                return SecretFinding.RiskLevel.LOW;
                
            default:
                return SecretFinding.RiskLevel.INFO;
        }
    }
    
    private String getRecommendation(SecretFinding.SecretType type) {
        switch (type) {
            case AWS_KEY:
                return "Rotate AWS keys immediately. Use IAM roles or environment variables.";
            case PRIVATE_KEY:
                return "Remove private key from code. Use secure key management service.";
            case DATABASE_PASSWORD:
                return "Use environment variables or secure configuration management.";
            case STRIPE_KEY:
            case API_KEY:
            case FIREBASE_KEY:
            case GITHUB_TOKEN:
                return "Revoke exposed key and generate new one. Use secret management tools.";
            case JWT_SECRET:
            case ENCRYPTION_KEY:
                return "Rotate keys and implement proper key rotation policy.";
            default:
                return "Review and remove hardcoded credentials. Use secure storage mechanisms.";
        }
    }
    
    /**
     * Quick scan for common secrets in APK resources
     */
    public ScanReport quickScan(String content) {
        return scanSourceCode(content, "QuickScan");
    }
}
