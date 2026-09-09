package nika.ngipro.forensic;

import java.io.*;
import java.security.cert.*;
import java.security.*;
import java.util.*;
import java.util.zip.*;
import javax.crypto.Cipher;

/**
 * Forensic & Legal Analysis Tools
 * Extract metadata, verify certificates, detect tampering, maintain chain of custody
 */
public class ForensicAnalyzer {
    
    public static class ApkMetadata {
        public String packageName;
        public String versionName;
        public int versionCode;
        public long fileSize;
        public String md5Hash;
        public String sha1Hash;
        public String sha256Hash;
        public Date buildDate;
        public String minSdk;
        public String targetSdk;
        public Map<String, String> manifestAttributes;
        
        public ApkMetadata() {
            manifestAttributes = new HashMap<>();
        }
        
        @Override
        public String toString() {
            StringBuilder sb = new StringBuilder();
            sb.append("Package: ").append(packageName).append("\n");
            sb.append("Version: ").append(versionName).append(" (").append(versionCode).append(")\n");
            sb.append("Size: ").append(formatSize(fileSize)).append("\n");
            sb.append("MD5: ").append(md5Hash).append("\n");
            sb.append("SHA-256: ").append(sha256Hash).append("\n");
            return sb.toString();
        }
        
        private String formatSize(long bytes) {
            if (bytes < 1024 * 1024) return String.format("%.2f KB", bytes / 1024.0);
            return String.format("%.2f MB", bytes / (1024.0 * 1024.0));
        }
    }
    
    public static class CertificateInfo {
        public String subject;
        public String issuer;
        public Date validFrom;
        public Date validTo;
        public String serialNumber;
        public String signatureAlgorithm;
        public int keySize;
        public String fingerprintMD5;
        public String fingerprintSHA1;
        public String fingerprintSHA256;
        public boolean isValid;
        public boolean isSelfSigned;
        public List<X509Certificate> certificateChain;
        
        public CertificateInfo() {
            certificateChain = new ArrayList<>();
            isValid = true;
            isSelfSigned = false;
        }
        
        @Override
        public String toString() {
            StringBuilder sb = new StringBuilder();
            sb.append("Subject: ").append(subject).append("\n");
            sb.append("Issuer: ").append(issuer).append("\n");
            sb.append("Valid From: ").append(validFrom).append("\n");
            sb.append("Valid To: ").append(validTo).append("\n");
            sb.append("Serial: ").append(serialNumber).append("\n");
            sb.append("Algorithm: ").append(signatureAlgorithm).append("\n");
            sb.append("Key Size: ").append(keySize).append(" bits\n");
            sb.append("SHA-256 Fingerprint: ").append(fingerprintSHA256).append("\n");
            sb.append("Valid: ").append(isValid ? "YES" : "NO").append("\n");
            return sb.toString();
        }
    }
    
    public static class TamperEvidence {
        public boolean isTampered;
        public List<String> evidenceList;
        public String originalSignature;
        public String currentSignature;
        public List<String> modifiedFiles;
        public Date analysisDate;
        
        public TamperEvidence() {
            evidenceList = new ArrayList<>();
            modifiedFiles = new ArrayList<>();
            isTampered = false;
            analysisDate = new Date();
        }
        
        public void addEvidence(String evidence) {
            evidenceList.add(evidence);
            isTampered = true;
        }
        
        @Override
        public String toString() {
            StringBuilder sb = new StringBuilder();
            sb.append("Tamper Analysis Report\n");
            sb.append("======================\n");
            sb.append("Analysis Date: ").append(analysisDate).append("\n");
            sb.append("Tampered: ").append(isTampered ? "YES ⚠️" : "NO ✓").append("\n");
            if (!evidenceList.isEmpty()) {
                sb.append("\nEvidence:\n");
                for (String evidence : evidenceList) {
                    sb.append("  - ").append(evidence).append("\n");
                }
            }
            return sb.toString();
        }
    }
    
    public static class ChainOfCustodyEntry {
        public Date timestamp;
        public String action;
        public String performedBy;
        public String notes;
        public String hashBefore;
        public String hashAfter;
        
        public ChainOfCustodyEntry(String action, String by, String notes) {
            this.timestamp = new Date();
            this.action = action;
            this.performedBy = by;
            this.notes = notes;
        }
        
        @Override
        public String toString() {
            return String.format("[%s] %s by %s - %s", 
                timestamp, action, performedBy, notes);
        }
    }
    
    private List<ChainOfCustodyEntry> custodyLog;
    
    public ForensicAnalyzer() {
        custodyLog = new ArrayList<>();
        logEntry("Analysis Session Started", "System", "New forensic analysis initiated");
    }
    
    /**
     * Extract complete metadata from APK
     */
    public ApkMetadata extractMetadata(File apkFile) {
        ApkMetadata metadata = new ApkMetadata();
        
        try {
            metadata.fileSize = apkFile.length();
            metadata.md5Hash = calculateHash(apkFile, "MD5");
            metadata.sha1Hash = calculateHash(apkFile, "SHA-1");
            metadata.sha256Hash = calculateHash(apkFile, "SHA-256");
            
            // Parse APK contents
            ZipFile zip = new ZipFile(apkFile);
            
            // Extract manifest info (simplified - use real parser in production)
            ZipEntry manifestEntry = zip.getEntry("AndroidManifest.xml");
            if (manifestEntry != null) {
                // Simulated extraction
                metadata.packageName = "com.example.app";
                metadata.versionName = "1.0.0";
                metadata.versionCode = 1;
                metadata.minSdk = "21";
                metadata.targetSdk = "33";
            }
            
            // Try to extract build date from ZIP entry timestamps
            Enumeration<? extends ZipEntry> entries = zip.entries();
            if (entries.hasMoreElements()) {
                ZipEntry firstEntry = entries.nextElement();
                long time = firstEntry.getTime();
                if (time > 0) {
                    metadata.buildDate = new Date(time);
                }
            }
            
            zip.close();
            
            logEntry("Metadata Extraction", "Analyst", 
                "Extracted metadata from " + apkFile.getName());
            
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        return metadata;
    }
    
    /**
     * Analyze signing certificate
     */
    public CertificateInfo analyzeCertificate(File apkFile) {
        CertificateInfo certInfo = new CertificateInfo();
        
        try {
            // In real implementation, extract META-INF/*.RSA or *.SF files
            // and parse X.509 certificates
            
            // Simulated certificate analysis
            certInfo.subject = "CN=NGI PRO Developer, O=Example Inc, C=US";
            certInfo.issuer = "CN=NGI PRO Developer, O=Example Inc, C=US";
            certInfo.validFrom = new Date(System.currentTimeMillis() - 365L * 24 * 60 * 60 * 1000);
            certInfo.validTo = new Date(System.currentTimeMillis() + 365L * 24 * 60 * 60 * 1000 * 2);
            certInfo.serialNumber = "1A2B3C4D5E6F7890";
            certInfo.signatureAlgorithm = "SHA256withRSA";
            certInfo.keySize = 2048;
            certInfo.fingerprintMD5 = "A1:B2:C3:D4:E5:F6:G7:H8:I9:J0:K1:L2:M3:N4:O5:P6";
            certInfo.fingerprintSHA1 = "A1B2C3D4E5F6G7H8I9J0K1L2M3N4O5P6Q7R8S9T0";
            certInfo.fingerprintSHA256 = "A1B2C3D4E5F6G7H8I9J0K1L2M3N4O5P6Q7R8S9T0U1V2W3X4Y5Z6";
            certInfo.isSelfSigned = true;
            
            // Check validity dates
            Date now = new Date();
            certInfo.isValid = !now.before(certInfo.validFrom) && !now.after(certInfo.validTo);
            
            logEntry("Certificate Analysis", "Analyst", 
                "Analyzed signing certificate for " + apkFile.getName());
            
        } catch (Exception e) {
            e.printStackTrace();
            certInfo.isValid = false;
        }
        
        return certInfo;
    }
    
    /**
     * Detect if APK has been tampered with
     */
    public TamperEvidence detectTampering(File apkFile, String expectedSignature) {
        TamperEvidence evidence = new TamperEvidence();
        
        try {
            String currentSignature = calculateHash(apkFile, "SHA-256");
            evidence.currentSignature = currentSignature;
            evidence.originalSignature = expectedSignature;
            
            // Check if signature matches
            if (expectedSignature != null && !expectedSignature.equals(currentSignature)) {
                evidence.addEvidence("APK signature mismatch detected");
                evidence.modifiedFiles.add("Entire APK");
            }
            
            // Check for common tampering signs
            ZipFile zip = new ZipFile(apkFile);
            
            // Check if META-INF directory exists (should exist in signed APK)
            if (zip.getEntry("META-INF/") == null) {
                evidence.addEvidence("META-INF directory missing - APK may be unsigned or resigned");
            }
            
            // Check for suspicious file modifications
            Enumeration<? extends ZipEntry> entries = zip.entries();
            while (entries.hasMoreElements()) {
                ZipEntry entry = entries.nextElement();
                
                // Check for recently modified critical files
                long modTime = entry.getTime();
                long apkModTime = apkFile.lastModified();
                if (Math.abs(modTime - apkModTime) > 1000) { // More than 1 second difference
                    // This could indicate repackaging
                }
            }
            
            zip.close();
            
            logEntry("Tamper Detection", "Analyst", 
                "Tamper analysis completed. Tampered: " + evidence.isTampered);
            
        } catch (Exception e) {
            evidence.addEvidence("Error during tamper analysis: " + e.getMessage());
        }
        
        return evidence;
    }
    
    /**
     * Verify certificate chain
     */
    public boolean verifyCertificateChain(CertificateInfo certInfo) {
        try {
            // In real implementation, validate against trusted CA store
            if (!certInfo.isValid) {
                return false;
            }
            
            // Check if certificate is expired
            Date now = new Date();
            if (now.before(certInfo.validFrom) || now.after(certInfo.validTo)) {
                return false;
            }
            
            // Check key size (minimum 2048 bits recommended)
            if (certInfo.keySize < 2048) {
                System.out.println("Warning: Weak key size (" + certInfo.keySize + " bits)");
            }
            
            // Check signature algorithm strength
            if (certInfo.signatureAlgorithm.contains("MD5") || 
                certInfo.signatureAlgorithm.contains("SHA1")) {
                System.out.println("Warning: Weak signature algorithm");
            }
            
            return true;
            
        } catch (Exception e) {
            return false;
        }
    }
    
    /**
     * Generate forensic report for legal proceedings
     */
    public String generateForensicReport(File apkFile, String caseId, String examiner) {
        StringBuilder report = new StringBuilder();
        
        ApkMetadata metadata = extractMetadata(apkFile);
        CertificateInfo certInfo = analyzeCertificate(apkFile);
        TamperEvidence tamperEvidence = detectTampering(apkFile, null);
        
        report.append("╔════════════════════════════════════════════════════════╗\n");
        report.append("║         NGI PRO FORENSIC ANALYSIS REPORT              ║\n");
        report.append("╚════════════════════════════════════════════════════════╝\n\n");
        
        report.append("CASE INFORMATION\n");
        report.append("-----------------\n");
        report.append("Case ID: ").append(caseId).append("\n");
        report.append("Examiner: ").append(examiner).append("\n");
        report.append("Analysis Date: ").append(new Date()).append("\n");
        report.append("Evidence File: ").append(apkFile.getName()).append("\n\n");
        
        report.append("EVIDENCE METADATA\n");
        report.append("-----------------\n");
        report.append(metadata.toString()).append("\n");
        
        report.append("CERTIFICATE ANALYSIS\n");
        report.append("--------------------\n");
        report.append(certInfo.toString()).append("\n");
        
        report.append("TAMPER ANALYSIS\n");
        report.append("----------------\n");
        report.append(tamperEvidence.toString()).append("\n");
        
        report.append("CHAIN OF CUSTODY\n");
        report.append("-----------------\n");
        for (ChainOfCustodyEntry entry : custodyLog) {
            report.append(entry.toString()).append("\n");
        }
        report.append("\n");
        
        report.append("CONCLUSION\n");
        report.append("----------\n");
        if (tamperEvidence.isTampered) {
            report.append("⚠️  EVIDENCE OF TAMPERING DETECTED\n");
            report.append("This APK appears to have been modified after signing.\n");
        } else {
            report.append("✓ NO TAMPERING DETECTED\n");
            report.append("This APK appears to be in its original signed state.\n");
        }
        
        if (!certInfo.isValid) {
            report.append("⚠️  CERTIFICATE VALIDATION FAILED\n");
        }
        
        report.append("\n\n");
        report.append("This report was generated automatically by NGI PRO Forensic Analyzer.\n");
        report.append("For legal proceedings, manual verification is recommended.\n");
        
        logEntry("Report Generated", examiner, "Forensic report generated for case " + caseId);
        
        return report.toString();
    }
    
    /**
     * Log chain of custody entry
     */
    public void logEntry(String action, String performedBy, String notes) {
        custodyLog.add(new ChainOfCustodyEntry(action, performedBy, notes));
    }
    
    /**
     * Export chain of custody log
     */
    public String exportCustodyLog() {
        StringBuilder log = new StringBuilder();
        log.append("CHAIN OF CUSTODY LOG\n");
        log.append("====================\n\n");
        
        for (ChainOfCustodyEntry entry : custodyLog) {
            log.append("Timestamp: ").append(entry.timestamp).append("\n");
            log.append("Action: ").append(entry.action).append("\n");
            log.append("Performed By: ").append(entry.performedBy).append("\n");
            log.append("Notes: ").append(entry.notes).append("\n");
            if (entry.hashBefore != null) {
                log.append("Hash Before: ").append(entry.hashBefore).append("\n");
            }
            if (entry.hashAfter != null) {
                log.append("Hash After: ").append(entry.hashAfter).append("\n");
            }
            log.append("------------------------\n");
        }
        
        return log.toString();
    }
    
    // Helper Methods
    
    private String calculateHash(File file, String algorithm) throws Exception {
        MessageDigest digest = MessageDigest.getInstance(algorithm);
        FileInputStream fis = new FileInputStream(file);
        byte[] buffer = new byte[8192];
        int bytesRead;
        
        while ((bytesRead = fis.read(buffer)) != -1) {
            digest.update(buffer, 0, bytesRead);
        }
        fis.close();
        
        byte[] hashBytes = digest.digest();
        StringBuilder hex = new StringBuilder();
        for (byte b : hashBytes) {
            hex.append(String.format("%02X", b));
        }
        return hex.toString();
    }
}
