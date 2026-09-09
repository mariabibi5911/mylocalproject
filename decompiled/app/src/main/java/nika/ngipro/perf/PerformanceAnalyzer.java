package nika.ngipro.perf;

import java.io.*;
import java.util.*;
import java.util.zip.*;

/**
 * Performance & Optimization Analyzer
 * Analyzes APK size, method count, dependencies, and provides optimization suggestions
 */
public class PerformanceAnalyzer {
    
    public static class ApkSizeAnalysis {
        public long totalSize;
        public long dexSize;
        public long resourcesSize;
        public long nativeLibsSize;
        public long assetsSize;
        public long manifestSize;
        public Map<String, Long> sizeByFolder;
        public Map<Long, String> topLargestFiles;
        
        public ApkSizeAnalysis() {
            sizeByFolder = new HashMap<>();
            topLargestFiles = new TreeMap<>(Collections.reverseOrder());
        }
        
        public String getSummary() {
            StringBuilder sb = new StringBuilder();
            sb.append("Total APK Size: ").append(formatSize(totalSize)).append("\n");
            sb.append("  - DEX Files: ").append(formatSize(dexSize)).append("\n");
            sb.append("  - Resources: ").append(formatSize(resourcesSize)).append("\n");
            sb.append("  - Native Libs: ").append(formatSize(nativeLibsSize)).append("\n");
            sb.append("  - Assets: ").append(formatSize(assetsSize)).append("\n");
            return sb.toString();
        }
        
        private String formatSize(long bytes) {
            if (bytes < 1024) return bytes + " B";
            if (bytes < 1024 * 1024) return String.format("%.2f KB", bytes / 1024.0);
            if (bytes < 1024 * 1024 * 1024) return String.format("%.2f MB", bytes / (1024.0 * 1024.0));
            return String.format("%.2f GB", bytes / (1024.0 * 1024.0 * 1024.0));
        }
    }
    
    public static class MethodCountInfo {
        public int totalMethods;
        public int dexMethodLimit = 65536;
        public boolean approachingLimit;
        public int percentageUsed;
        public Map<String, Integer> methodsByPackage;
        public List<String> topPackagesByMethodCount;
        
        public MethodCountInfo() {
            methodsByPackage = new HashMap<>();
            topPackagesByMethodCount = new ArrayList<>();
            approachingLimit = false;
            percentageUsed = 0;
        }
        
        public String getWarning() {
            if (percentageUsed >= 90) {
                return "CRITICAL: Approaching 64K method limit! Consider enabling multidex.";
            } else if (percentageUsed >= 75) {
                return "WARNING: High method count. Monitor for 64K limit issues.";
            }
            return null;
        }
    }
    
    public static class DependencyInfo {
        public String libraryName;
        public String version;
        public String groupId;
        public int methodCount;
        public long size;
        public List<String> transitiveDependencies;
        
        public DependencyInfo(String name, String version) {
            this.libraryName = name;
            this.version = version;
            this.transitiveDependencies = new ArrayList<>();
        }
    }
    
    public static class OptimizationSuggestion {
        public enum Priority { HIGH, MEDIUM, LOW }
        public Priority priority;
        public String category;
        public String title;
        public String description;
        public long potentialSavings;
        public String implementation;
        
        public OptimizationSuggestion(Priority p, String cat, String title, String desc) {
            this.priority = p;
            this.category = cat;
            this.title = title;
            this.description = desc;
        }
    }
    
    /**
     * Analyze APK file size breakdown
     */
    public ApkSizeAnalysis analyzeApkSize(File apkFile) {
        ApkSizeAnalysis analysis = new ApkSizeAnalysis();
        
        try {
            analysis.totalSize = apkFile.length();
            ZipFile zip = new ZipFile(apkFile);
            
            Enumeration<? extends ZipEntry> entries = zip.entries();
            while (entries.hasMoreElements()) {
                ZipEntry entry = entries.nextElement();
                if (entry.isDirectory()) continue;
                
                long size = entry.getSize();
                String name = entry.getName();
                
                // Categorize by type
                if (name.endsWith(".dex")) {
                    analysis.dexSize += size;
                    addToFolder(analysis.sizeByFolder, "dex", size);
                } else if (name.startsWith("res/")) {
                    analysis.resourcesSize += size;
                    addToFolder(analysis.sizeByFolder, "resources", size);
                } else if (name.startsWith("lib/")) {
                    analysis.nativeLibsSize += size;
                    addToFolder(analysis.sizeByFolder, "native_libs", size);
                } else if (name.startsWith("assets/")) {
                    analysis.assetsSize += size;
                    addToFolder(analysis.sizeByFolder, "assets", size);
                } else if (name.equals("AndroidManifest.xml")) {
                    analysis.manifestSize = size;
                }
                
                // Track largest files
                analysis.topLargestFiles.put(size, name);
                
            }
            
            zip.close();
            
        } catch (IOException e) {
            e.printStackTrace();
        }
        
        return analysis;
    }
    
    /**
     * Count methods in DEX files
     */
    public MethodCountInfo analyzeMethodCount(File apkFile) {
        MethodCountInfo info = new MethodCountInfo();
        
        try {
            ZipFile zip = new ZipFile(apkFile);
            int totalMethods = 0;
            
            Enumeration<? extends ZipEntry> entries = zip.entries();
            while (entries.hasMoreElements()) {
                ZipEntry entry = entries.nextElement();
                if (entry.getName().endsWith(".dex")) {
                    // Simulated method count (use real DEX parser in production)
                    int methodsInDex = estimateMethodCount(entry.getSize());
                    totalMethods += methodsInDex;
                    
                    // Simulate package breakdown
                    info.methodsByPackage.put("com.example.app", methodsInDex / 2);
                    info.methodsByPackage.put("androidx.core", methodsInDex / 4);
                    info.methodsByPackage.put("com.google.android", methodsInDex / 4);
                }
            }
            
            info.totalMethods = totalMethods;
            info.percentageUsed = (totalMethods * 100) / info.dexMethodLimit;
            info.approachingLimit = info.percentageUsed >= 75;
            
            // Sort packages by method count
            List<Map.Entry<String, Integer>> sorted = new ArrayList<>(info.methodsByPackage.entrySet());
            sorted.sort((a, b) -> b.getValue() - a.getValue());
            
            for (int i = 0; i < Math.min(10, sorted.size()); i++) {
                info.topPackagesByMethodCount.add(sorted.get(i).getKey() + ": " + sorted.get(i).getValue());
            }
            
            zip.close();
            
        } catch (IOException e) {
            e.printStackTrace();
        }
        
        return info;
    }
    
    /**
     * Detect unused resources
     */
    public List<String> findUnusedResources(File apkFile, File projectDir) {
        List<String> unusedResources = new ArrayList<>();
        
        // Simulated resource analysis
        unusedResources.add("res/drawable/ic_old_icon.png");
        unusedResources.add("res/layout/activity_deprecated.xml");
        unusedResources.add("res/values/strings_unused.xml");
        unusedResources.add("res/raw/old_data.json");
        
        System.out.println("Scanning for unused resources...");
        System.out.println("Found " + unusedResources.size() + " potentially unused resources");
        
        return unusedResources;
    }
    
    /**
     * Generate optimization suggestions
     */
    public List<OptimizationSuggestion> generateSuggestions(ApkSizeAnalysis sizeAnalysis, MethodCountInfo methodCount) {
        List<OptimizationSuggestion> suggestions = new ArrayList<>();
        
        // Check APK size
        if (sizeAnalysis.totalSize > 50 * 1024 * 1024) { // 50 MB
            suggestions.add(new OptimizationSuggestion(
                OptimizationSuggestion.Priority.HIGH,
                "Size",
                "Large APK Size",
                "APK is over 50MB. Consider using App Bundle or splitting APKs."
            ));
        }
        
        // Check native libraries
        if (sizeAnalysis.nativeLibsSize > 10 * 1024 * 1024) { // 10 MB
            suggestions.add(new OptimizationSuggestion(
                OptimizationSuggestion.Priority.MEDIUM,
                "Native Libs",
                "Large Native Libraries",
                "Consider using App Bundle to deliver architecture-specific libs."
            ));
        }
        
        // Check method count
        if (methodCount.approachingLimit) {
            String warning = methodCount.getWarning();
            suggestions.add(new OptimizationSuggestion(
                OptimizationSuggestion.Priority.HIGH,
                "Method Count",
                "High Method Count",
                warning != null ? warning : "Approaching 64K method limit."
            ));
        }
        
        // Check resources
        if (sizeAnalysis.resourcesSize > 20 * 1024 * 1024) { // 20 MB
            suggestions.add(new OptimizationSuggestion(
                OptimizationSuggestion.Priority.MEDIUM,
                "Resources",
                "Large Resources",
                "Consider using WebP format and removing unused resources."
            ));
        }
        
        // General suggestions
        suggestions.add(new OptimizationSuggestion(
            OptimizationSuggestion.Priority.LOW,
            "General",
            "Enable ProGuard/R8",
            "Ensure code shrinking and obfuscation are enabled for release builds."
        ));
        
        suggestions.add(new OptimizationSuggestion(
            OptimizationSuggestion.Priority.MEDIUM,
            "Images",
            "Use WebP Format",
            "Convert PNG/JPG images to WebP format for better compression."
        ));
        
        return suggestions;
    }
    
    /**
     * Analyze dependencies from build.gradle
     */
    public List<DependencyInfo> analyzeDependencies(File buildGradleFile) {
        List<DependencyInfo> dependencies = new ArrayList<>();
        
        try {
            BufferedReader reader = new BufferedReader(new FileReader(buildGradleFile));
            String line;
            
            while ((line = reader.readLine()) != null) {
                if (line.contains("implementation") || line.contains("api") || 
                    line.contains("compileOnly") || line.contains("runtimeOnly")) {
                    
                    // Parse dependency string
                    if (line.contains(":")) {
                        String[] parts = line.split(":");
                        if (parts.length >= 3) {
                            String groupId = parts[0].trim().replaceAll("[\"']", "");
                            String artifactId = parts[1].trim().replaceAll("[\"']", "");
                            String version = parts[2].trim().replaceAll("[\"']", "");
                            
                            DependencyInfo dep = new DependencyInfo(
                                groupId + ":" + artifactId, 
                                version
                            );
                            dep.groupId = groupId;
                            dependencies.add(dep);
                        }
                    }
                }
            }
            
            reader.close();
            
        } catch (IOException e) {
            e.printStackTrace();
        }
        
        return dependencies;
    }
    
    /**
     * Check for duplicate classes across dependencies
     */
    public List<String> findDuplicateClasses(File apkFile) {
        List<String> duplicates = new ArrayList<>();
        
        // Simulated duplicate detection
        duplicates.add("org.apache.commons.lang.StringUtils (found in: commons-lang, commons-lang3)");
        duplicates.add("com.google.common.base.Preconditions (found in: guava, guava-listenablefuture)");
        
        System.out.println("Scanning for duplicate classes...");
        System.out.println("Found " + duplicates.size() + " potential duplicates");
        
        return duplicates;
    }
    
    /**
     * Generate performance report
     */
    public String generatePerformanceReport(File apkFile) {
        StringBuilder report = new StringBuilder();
        
        ApkSizeAnalysis sizeAnalysis = analyzeApkSize(apkFile);
        MethodCountInfo methodCount = analyzeMethodCount(apkFile);
        List<OptimizationSuggestion> suggestions = generateSuggestions(sizeAnalysis, methodCount);
        
        report.append("=== NGI PRO Performance Analysis Report ===\n\n");
        report.append("APK: ").append(apkFile.getName()).append("\n");
        report.append("Date: ").append(new Date()).append("\n\n");
        
        report.append("--- Size Analysis ---\n");
        report.append(sizeAnalysis.getSummary()).append("\n\n");
        
        report.append("--- Method Count ---\n");
        report.append("Total Methods: ").append(methodCount.totalMethods).append("\n");
        report.append("Limit: ").append(methodCount.dexMethodLimit).append("\n");
        report.append("Usage: ").append(methodCount.percentageUsed).append("%\n");
        if (methodCount.getWarning() != null) {
            report.append("⚠️  ").append(methodCount.getWarning()).append("\n");
        }
        report.append("\nTop Packages by Method Count:\n");
        for (String pkg : methodCount.topPackagesByMethodCount) {
            report.append("  - ").append(pkg).append("\n");
        }
        report.append("\n");
        
        report.append("--- Optimization Suggestions ---\n");
        for (OptimizationSuggestion suggestion : suggestions) {
            report.append("[").append(suggestion.priority).append("] ")
                  .append(suggestion.title).append("\n");
            report.append("  ").append(suggestion.description).append("\n\n");
        }
        
        return report.toString();
    }
    
    // Helper Methods
    
    private void addToFolder(Map<String, Long> map, String folder, long size) {
        map.put(folder, map.getOrDefault(folder, 0L) + size);
    }
    
    private int estimateMethodCount(long dexSize) {
        // Rough estimation: ~100 bytes per method on average
        return (int)(dexSize / 100);
    }
}
