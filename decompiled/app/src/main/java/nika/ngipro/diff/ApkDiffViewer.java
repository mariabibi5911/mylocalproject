package nika.ngipro.diff;

import java.io.*;
import java.util.*;

/**
 * APK/Smali Diff Viewer - Compare two APKs or smali files
 * Highlights differences and generates detailed reports
 */
public class ApkDiffViewer {
    
    public enum ChangeType {
        ADDED,
        REMOVED,
        MODIFIED,
        RENAMED,
        UNCHANGED
    }
    
    public static class DiffResult {
        public String filePath;
        public ChangeType changeType;
        public String oldContent;
        public String newContent;
        public List<String> addedLines;
        public List<String> removedLines;
        public int similarityScore;
        
        public DiffResult(String path, ChangeType type) {
            this.filePath = path;
            this.changeType = type;
            this.addedLines = new ArrayList<>();
            this.removedLines = new ArrayList<>();
            this.similarityScore = 100;
        }
        
        public void addAddedLine(String line) {
            addedLines.add(line);
        }
        
        public void addRemovedLine(String line) {
            removedLines.add(line);
        }
        
        @Override
        public String toString() {
            StringBuilder sb = new StringBuilder();
            sb.append("File: ").append(filePath).append("\n");
            sb.append("Change: ").append(changeType).append("\n");
            if (!removedLines.isEmpty()) {
                sb.append("Removed lines:\n");
                for (String line : removedLines) {
                    sb.append("- ").append(line).append("\n");
                }
            }
            if (!addedLines.isEmpty()) {
                sb.append("Added lines:\n");
                for (String line : addedLines) {
                    sb.append("+ ").append(line).append("\n");
                }
            }
            return sb.toString();
        }
    }
    
    public static class ApkInfo {
        public String packageName;
        public String versionName;
        public int versionCode;
        public int minSdk;
        public int targetSdk;
        public List<String> permissions;
        public List<String> activities;
        public List<String> services;
        public List<String> receivers;
        public Map<String, String> metaData;
        
        public ApkInfo() {
            permissions = new ArrayList<>();
            activities = new ArrayList<>();
            services = new ArrayList<>();
            receivers = new ArrayList<>();
            metaData = new HashMap<>();
        }
    }
    
    /**
     * Compare two APK files and generate diff report
     */
    public List<DiffResult> compareApks(File apk1, File apk2) {
        List<DiffResult> results = new ArrayList<>();
        
        System.out.println("Comparing APKs...");
        System.out.println("APK 1: " + apk1.getAbsolutePath());
        System.out.println("APK 2: " + apk2.getAbsolutePath());
        
        // Extract and compare components
        ApkInfo info1 = extractApkInfo(apk1);
        ApkInfo info2 = extractApkInfo(apk2);
        
        // Compare package name
        if (!info1.packageName.equals(info2.packageName)) {
            DiffResult diff = new DiffResult("AndroidManifest.xml:package", ChangeType.MODIFIED);
            diff.oldContent = info1.packageName;
            diff.newContent = info2.packageName;
            results.add(diff);
        }
        
        // Compare version
        if (info1.versionCode != info2.versionCode) {
            DiffResult diff = new DiffResult("AndroidManifest.xml:versionCode", ChangeType.MODIFIED);
            diff.oldContent = String.valueOf(info1.versionCode);
            diff.newContent = String.valueOf(info2.versionCode);
            results.add(diff);
        }
        
        // Compare permissions
        compareLists("Permissions", info1.permissions, info2.permissions, results);
        
        // Compare activities
        compareLists("Activities", info1.activities, info2.activities, results);
        
        // Compare services
        compareLists("Services", info1.services, info2.services, results);
        
        // Compare receivers
        compareLists("Receivers", info1.receivers, info2.receivers, results);
        
        return results;
    }
    
    /**
     * Compare two smali files
     */
    public List<DiffResult> compareSmaliFiles(File file1, File file2) {
        List<DiffResult> results = new ArrayList<>();
        
        try {
            List<String> lines1 = readLines(file1);
            List<String> lines2 = readLines(file2);
            
            // Simple line-by-line comparison (use proper diff algorithm in production)
            int maxLines = Math.max(lines1.size(), lines2.size());
            
            for (int i = 0; i < maxLines; i++) {
                String line1 = i < lines1.size() ? lines1.get(i) : null;
                String line2 = i < lines2.size() ? lines2.get(i) : null;
                
                if (line1 == null && line2 != null) {
                    DiffResult diff = new DiffResult(file2.getName(), ChangeType.ADDED);
                    diff.addAddedLine(line2);
                    results.add(diff);
                } else if (line1 != null && line2 == null) {
                    DiffResult diff = new DiffResult(file1.getName(), ChangeType.REMOVED);
                    diff.addRemovedLine(line1);
                    results.add(diff);
                } else if (line1 != null && !line1.equals(line2)) {
                    DiffResult diff = new DiffResult(file1.getName(), ChangeType.MODIFIED);
                    diff.addRemovedLine(line1);
                    diff.addAddedLine(line2);
                    results.add(diff);
                }
            }
            
        } catch (IOException e) {
            e.printStackTrace();
        }
        
        return results;
    }
    
    /**
     * Compare two directories recursively
     */
    public List<DiffResult> compareDirectories(File dir1, File dir2) {
        List<DiffResult> results = new ArrayList<>();
        
        Map<String, File> files1 = getAllFiles(dir1);
        Map<String, File> files2 = getAllFiles(dir2);
        
        Set<String> allPaths = new HashSet<>();
        allPaths.addAll(files1.keySet());
        allPaths.addAll(files2.keySet());
        
        for (String path : allPaths) {
            File f1 = files1.get(path);
            File f2 = files2.get(path);
            
            if (f1 != null && f2 == null) {
                DiffResult diff = new DiffResult(path, ChangeType.REMOVED);
                results.add(diff);
            } else if (f1 == null && f2 != null) {
                DiffResult diff = new DiffResult(path, ChangeType.ADDED);
                results.add(diff);
            } else if (f1 != null && f2 != null) {
                // Both exist, compare content
                if (path.endsWith(".smali")) {
                    results.addAll(compareSmaliFiles(f1, f2));
                } else if (path.endsWith(".xml")) {
                    results.addAll(compareXmlFiles(f1, f2));
                } else {
                    // Binary comparison
                    if (f1.length() != f2.length()) {
                        DiffResult diff = new DiffResult(path, ChangeType.MODIFIED);
                        diff.oldContent = "Size: " + f1.length();
                        diff.newContent = "Size: " + f2.length();
                        results.add(diff);
                    }
                }
            }
        }
        
        return results;
    }
    
    /**
     * Generate HTML diff report
     */
    public String generateHtmlReport(List<DiffResult> diffs, String title) {
        StringBuilder html = new StringBuilder();
        html.append("<!DOCTYPE html>\n<html>\n<head>\n");
        html.append("<title>").append(title).append("</title>\n");
        html.append("<style>\n");
        html.append("body { font-family: monospace; margin: 20px; }\n");
        html.append(".added { background-color: #d4edda; color: #155724; }\n");
        html.append(".removed { background-color: #f8d7da; color: #721c24; }\n");
        html.append(".modified { background-color: #fff3cd; color: #856404; }\n");
        html.append(".unchanged { background-color: #f8f9fa; color: #6c757d; }\n");
        html.append("h2 { border-bottom: 2px solid #333; padding-bottom: 10px; }\n");
        html.append(".file-path { font-weight: bold; color: #0066cc; }\n");
        html.append("</style>\n");
        html.append("</head>\n<body>\n");
        html.append("<h1>").append(title).append("</h1>\n");
        html.append("<p>Total changes: ").append(diffs.size()).append("</p>\n\n");
        
        int addedCount = 0, removedCount = 0, modifiedCount = 0;
        for (DiffResult diff : diffs) {
            if (diff.changeType == ChangeType.ADDED) addedCount++;
            else if (diff.changeType == ChangeType.REMOVED) removedCount++;
            else if (diff.changeType == ChangeType.MODIFIED) modifiedCount++;
        }
        
        html.append("<div><strong>Summary:</strong> ");
        html.append("Added: ").append(addedCount).append(", ");
        html.append("Removed: ").append(removedCount).append(", ");
        html.append("Modified: ").append(modifiedCount).append("</div><br>\n");
        
        for (DiffResult diff : diffs) {
            html.append("<div class=\"").append(diff.changeType.name().toLowerCase()).append("\">\n");
            html.append("<span class=\"file-path\">").append(diff.filePath).append("</span><br>\n");
            html.append("<strong>Change Type:</strong> ").append(diff.changeType).append("<br>\n");
            
            for (String line : diff.removedLines) {
                html.append("<pre class=\"removed\">- ").append(escapeHtml(line)).append("</pre>\n");
            }
            for (String line : diff.addedLines) {
                html.append("<pre class=\"added\">+ ").append(escapeHtml(line)).append("</pre>\n");
            }
            
            html.append("</div><hr>\n");
        }
        
        html.append("</body>\n</html>");
        return html.toString();
    }
    
    /**
     * Generate unified diff format
     */
    public String generateUnifiedDiff(List<DiffResult> diffs) {
        StringBuilder diff = new StringBuilder();
        
        for (DiffResult result : diffs) {
            diff.append("diff --git a/").append(result.filePath).append(" b/").append(result.filePath).append("\n");
            
            if (result.changeType == ChangeType.ADDED) {
                diff.append("new file mode 100644\n");
                diff.append("--- /dev/null\n");
                diff.append("+++ b/").append(result.filePath).append("\n");
                for (String line : result.addedLines) {
                    diff.append("+").append(line).append("\n");
                }
            } else if (result.changeType == ChangeType.REMOVED) {
                diff.append("deleted file mode 100644\n");
                diff.append("--- a/").append(result.filePath).append("\n");
                diff.append("+++ /dev/null\n");
                for (String line : result.removedLines) {
                    diff.append("-").append(line).append("\n");
                }
            } else if (result.changeType == ChangeType.MODIFIED) {
                diff.append("--- a/").append(result.filePath).append("\n");
                diff.append("+++ b/").append(result.filePath).append("\n");
                for (String line : result.removedLines) {
                    diff.append("-").append(line).append("\n");
                }
                for (String line : result.addedLines) {
                    diff.append("+").append(line).append("\n");
                }
            }
            diff.append("\n");
        }
        
        return diff.toString();
    }
    
    // Helper Methods
    
    private ApkInfo extractApkInfo(File apk) {
        ApkInfo info = new ApkInfo();
        // Simulated extraction (use APK parser in real implementation)
        info.packageName = "com.example.app";
        info.versionName = "1.0.0";
        info.versionCode = 1;
        info.minSdk = 21;
        info.targetSdk = 33;
        info.permissions.add("android.permission.INTERNET");
        info.activities.add("MainActivity");
        return info;
    }
    
    private void compareLists(String label, List<String> list1, List<String> list2, List<DiffResult> results) {
        Set<String> set1 = new HashSet<>(list1);
        Set<String> set2 = new HashSet<>(list2);
        
        // Removed items
        for (String item : set1) {
            if (!set2.contains(item)) {
                DiffResult diff = new DiffResult(label, ChangeType.REMOVED);
                diff.addRemovedLine(item);
                results.add(diff);
            }
        }
        
        // Added items
        for (String item : set2) {
            if (!set1.contains(item)) {
                DiffResult diff = new DiffResult(label, ChangeType.ADDED);
                diff.addAddedLine(item);
                results.add(diff);
            }
        }
    }
    
    private List<String> readLines(File file) throws IOException {
        List<String> lines = new ArrayList<>();
        BufferedReader reader = new BufferedReader(new FileReader(file));
        String line;
        while ((line = reader.readLine()) != null) {
            lines.add(line);
        }
        reader.close();
        return lines;
    }
    
    private Map<String, File> getAllFiles(File dir) {
        Map<String, File> files = new HashMap<>();
        if (dir.isDirectory()) {
            collectFiles(dir, "", files);
        }
        return files;
    }
    
    private void collectFiles(File dir, String prefix, Map<String, File> files) {
        File[] fileList = dir.listFiles();
        if (fileList != null) {
            for (File file : fileList) {
                String relativePath = prefix.isEmpty() ? file.getName() : prefix + "/" + file.getName();
                if (file.isDirectory()) {
                    collectFiles(file, relativePath, files);
                } else {
                    files.put(relativePath, file);
                }
            }
        }
    }
    
    private List<DiffResult> compareXmlFiles(File file1, File file2) {
        // Simplified XML comparison (use proper XML parser in production)
        return compareSmaliFiles(file1, file2); // Reuse line comparison for now
    }
    
    private String escapeHtml(String text) {
        return text.replace("&", "&amp;")
                   .replace("<", "&lt;")
                   .replace(">", "&gt;")
                   .replace("\"", "&quot;");
    }
}
