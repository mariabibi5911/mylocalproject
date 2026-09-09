package nika.ngipro.batch;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

import nika.ngipro.crypto.CryptoAPIAnalyzer;
import nika.ngipro.analysis.StringDecryptor;
import nika.ngipro.privacy.TrackerDetector;
import nika.ngipro.hooks.HookGenerator;

/**
 * Batch APK Processor - Process multiple APKs simultaneously
 * Supports parallel processing with configurable thread count
 */
public class BatchProcessor {
    
    public interface BatchCallback {
        void onProgress(int current, int total, String fileName, String status);
        void onComplete(List<BatchResult> results);
        void onError(String fileName, String error);
    }
    
    public static class BatchResult {
        public String fileName;
        public String filePath;
        public boolean success;
        public CryptoAPIAnalyzer.CryptoReport cryptoReport;
        public StringDecryptor.DecryptionReport decryptionReport;
        public TrackerDetector.PrivacyReport trackerReport;
        public List<HookGenerator.HookTemplate> hookTemplates;
        public long processingTimeMs;
        public String errorMessage;
        
        @Override
        public String toString() {
            StringBuilder sb = new StringBuilder();
            sb.append("=== Batch Result: ").append(fileName).append(" ===\n");
            sb.append("Status: ").append(success ? "SUCCESS" : "FAILED").append("\n");
            sb.append("Processing Time: ").append(processingTimeMs).append("ms\n");
            
            if (cryptoReport != null) {
                sb.append("Crypto API Calls: ").append(cryptoReport.totalCalls).append("\n");
                sb.append("Hardcoded Keys: ").append(cryptoReport.hardcodedKeysFound).append("\n");
                sb.append("High Risk: ").append(cryptoReport.highRiskCalls).append("\n");
            }
            
            if (trackerReport != null) {
                sb.append("Trackers Found: ").append(trackerReport.totalTrackersFound).append("\n");
                sb.append("Privacy Risk: ").append(trackerReport.overallPrivacyRisk).append("/5\n");
            }
            
            if (decryptionReport != null) {
                sb.append("Encrypted Strings: ").append(decryptionReport.encryptedStrings.size()).append("\n");
                sb.append("Successfully Decrypted: ").append(decryptionReport.decryptedStrings.size()).append("\n");
            }
            
            if (!success && errorMessage != null) {
                sb.append("Error: ").append(errorMessage).append("\n");
            }
            
            return sb.toString();
        }
    }
    
    private int threadCount = 4;
    private boolean includeCryptoAnalysis = true;
    private boolean includeStringDecryption = true;
    private boolean includeTrackerDetection = true;
    private boolean generateHooks = false;
    
    public BatchProcessor setThreadCount(int count) {
        this.threadCount = Math.max(1, Math.min(count, 8));
        return this;
    }
    
    public BatchProcessor enableCryptoAnalysis(boolean enable) {
        this.includeCryptoAnalysis = enable;
        return this;
    }
    
    public BatchProcessor enableStringDecryption(boolean enable) {
        this.includeStringDecryption = enable;
        return this;
    }
    
    public BatchProcessor enableTrackerDetection(boolean enable) {
        this.includeTrackerDetection = enable;
        return this;
    }
    
    public BatchProcessor enableHookGeneration(boolean enable) {
        this.generateHooks = enable;
        return this;
    }
    
    /**
     * Process multiple APK files in batch
     */
    public void processBatch(List<File> apkFiles, BatchCallback callback) {
        if (apkFiles == null || apkFiles.isEmpty()) {
            callback.onComplete(new ArrayList<>());
            return;
        }
        
        ExecutorService executor = Executors.newFixedThreadPool(threadCount);
        List<BatchResult> results = new ArrayList<>();
        
        for (int i = 0; i < apkFiles.size(); i++) {
            final int index = i;
            final File apkFile = apkFiles.get(i);
            
            executor.submit(() -> {
                try {
                    callback.onProgress(index + 1, apkFiles.size(), apkFile.getName(), "Processing...");
                    
                    long startTime = System.currentTimeMillis();
                    BatchResult result = processSingleApk(apkFile);
                    result.processingTimeMs = System.currentTimeMillis() - startTime;
                    
                    synchronized (results) {
                        results.add(result);
                    }
                    
                    callback.onProgress(index + 1, apkFiles.size(), apkFile.getName(), 
                        result.success ? "Completed" : "Failed");
                        
                } catch (Exception e) {
                    BatchResult errorResult = new BatchResult();
                    errorResult.fileName = apkFile.getName();
                    errorResult.filePath = apkFile.getAbsolutePath();
                    errorResult.success = false;
                    errorResult.errorMessage = e.getMessage();
                    
                    synchronized (results) {
                        results.add(errorResult);
                    }
                    
                    callback.onError(apkFile.getName(), e.getMessage());
                }
            });
        }
        
        executor.shutdown();
        try {
            executor.awaitTermination(30, TimeUnit.MINUTES);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        
        callback.onComplete(results);
    }
    
    /**
     * Process a single APK file with all enabled analyses
     */
    public BatchResult processSingleApk(File apkFile) {
        BatchResult result = new BatchResult();
        result.fileName = apkFile.getName();
        result.filePath = apkFile.getAbsolutePath();
        
        try {
            // Validate APK file
            if (!apkFile.exists() || !apkFile.isFile()) {
                throw new Exception("APK file not found: " + apkFile.getName());
            }
            
            if (!apkFile.getName().toLowerCase().endsWith(".apk")) {
                throw new Exception("Invalid file type. Expected .apk file");
            }
            
            // Extract APK to temporary directory for analysis
            File tempDir = new File(System.getProperty("java.io.tmpdir"), 
                "ngipro_batch_" + System.currentTimeMillis());
            tempDir.mkdirs();
            
            try {
                // TODO: Implement APK extraction logic
                // For now, we'll simulate the analysis
                // In real implementation, use Apktool or similar to extract
                
                // Perform Crypto Analysis
                if (includeCryptoAnalysis) {
                    // Simulate DEX file analysis
                    List<String> dexPaths = new ArrayList<>();
                    dexPaths.add(apkFile.getAbsolutePath()); // Placeholder
                    
                    CryptoAPIAnalyzer cryptoAnalyzer = new CryptoAPIAnalyzer();
                    result.cryptoReport = cryptoAnalyzer.analyzeDexFiles(dexPaths);
                }
                
                // Perform String Decryption
                if (includeStringDecryption) {
                    StringDecryptor decryptor = new StringDecryptor();
                    // In real implementation, extract strings from DEX first
                    List<String> sampleStrings = new ArrayList<>();
                    result.decryptionReport = decryptor.analyzeStrings(sampleStrings);
                }
                
                // Perform Tracker Detection
                if (includeTrackerDetection) {
                    TrackerDetector trackerDetector = new TrackerDetector();
                    result.trackerReport = trackerDetector.detectTrackers(apkFile.getAbsolutePath());
                }
                
                // Generate Hook Templates
                if (generateHooks && result.cryptoReport != null) {
                    HookGenerator hookGen = new HookGenerator();
                    result.hookTemplates = new ArrayList<>();
                    
                    for (CryptoAPIAnalyzer.CryptoUsage usage : result.cryptoReport.usages) {
                        HookGenerator.HookTemplate fridaHook = hookGen.generateFridaHook(
                            usage.className,
                            usage.methodName,
                            "V", // Default signature placeholder
                            HookGenerator.HookPurpose.TRACE
                        );
                        result.hookTemplates.add(fridaHook);
                    }
                }
                
                result.success = true;
                
            } finally {
                // Cleanup temp directory
                deleteDirectory(tempDir);
            }
            
        } catch (Exception e) {
            result.success = false;
            result.errorMessage = e.getMessage();
            e.printStackTrace();
        }
        
        return result;
    }
    
    /**
     * Generate batch report summary
     */
    public String generateBatchReport(List<BatchResult> results) {
        StringBuilder report = new StringBuilder();
        report.append("===========================================\n");
        report.append("     NGI PRO 2.0 - BATCH ANALYSIS REPORT   \n");
        report.append("===========================================\n\n");
        report.append("Total Files Processed: ").append(results.size()).append("\n");
        
        long successCount = results.stream().filter(r -> r.success).count();
        long failedCount = results.size() - successCount;
        
        report.append("Successful: ").append(successCount).append("\n");
        report.append("Failed: ").append(failedCount).append("\n\n");
        
        long totalTime = results.stream().mapToLong(r -> r.processingTimeMs).sum();
        report.append("Total Processing Time: ").append(totalTime / 1000.0).append("s\n");
        report.append("Average per File: ").append(results.isEmpty() ? 0 : totalTime / results.size()).append("ms\n\n");
        
        // Aggregate statistics
        int totalCryptoAPIs = 0;
        int totalTrackers = 0;
        int totalHardcodedKeys = 0;
        
        for (BatchResult result : results) {
            if (result.success) {
                if (result.cryptoReport != null) {
                    totalCryptoAPIs += result.cryptoReport.totalCalls;
                    totalHardcodedKeys += result.cryptoReport.hardcodedKeysFound;
                }
                if (result.trackerReport != null) {
                    totalTrackers += result.trackerReport.totalTrackersFound;
                }
            }
        }
        
        report.append("--- Aggregate Statistics ---\n");
        report.append("Total Crypto API Calls: ").append(totalCryptoAPIs).append("\n");
        report.append("Total Hardcoded Keys: ").append(totalHardcodedKeys).append("\n");
        report.append("Total Trackers Found: ").append(totalTrackers).append("\n\n");
        
        // Detailed results
        report.append("--- Detailed Results ---\n\n");
        for (BatchResult result : results) {
            report.append(result.toString());
            report.append("\n-------------------------------------------\n\n");
        }
        
        return report.toString();
    }
    
    /**
     * Export batch results to CSV
     */
    public String exportToCSV(List<BatchResult> results) {
        StringBuilder csv = new StringBuilder();
        csv.append("FileName,Status,ProcessingTime,CryptoAPICalls,HardcodedKeys,Trackers,PrivacyRisk,Error\n");
        
        for (BatchResult result : results) {
            csv.append("\"").append(result.fileName).append("\",");
            csv.append(result.success ? "SUCCESS" : "FAILED").append(",");
            csv.append(result.processingTimeMs).append(",");
            csv.append(result.cryptoReport != null ? result.cryptoReport.totalCalls : 0).append(",");
            csv.append(result.cryptoReport != null ? result.cryptoReport.hardcodedKeysFound : 0).append(",");
            csv.append(result.trackerReport != null ? result.trackerReport.totalTrackersFound : 0).append(",");
            csv.append(result.trackerReport != null ? result.trackerReport.overallPrivacyRisk : 0).append(",");
            csv.append("\"").append(result.errorMessage != null ? result.errorMessage.replace("\"", "\"\"") : "").append("\"\n");
        }
        
        return csv.toString();
    }
    
    private void deleteDirectory(File dir) {
        if (dir != null && dir.exists()) {
            File[] files = dir.listFiles();
            if (files != null) {
                for (File file : files) {
                    if (file.isDirectory()) {
                        deleteDirectory(file);
                    } else {
                        file.delete();
                    }
                }
            }
            dir.delete();
        }
    }
}
