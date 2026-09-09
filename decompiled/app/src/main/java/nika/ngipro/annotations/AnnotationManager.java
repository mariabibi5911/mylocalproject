package nika.ngipro.annotations;

import java.util.*;
import java.io.*;

/**
 * Annotation System for NGI PRO 2.0
 * Allows users to add notes, tags, and markers to code locations
 */
public class AnnotationManager {
    
    public enum AnnotationType {
        NOTE,       // General note
        WARNING,    // Security warning
        TODO,       // Something to investigate
        IMPORTANT,  // Critical finding
        HOOK,       // Hook point identified
        DECRYPT,    // Decryption routine
        NATIVE,     // Native call
        API_KEY,    // Potential API key
        CRYPTO,     // Cryptographic operation
        TRACKER,    // Tracking SDK
        OBFUSCATED, // Obfuscated code
        CUSTOM      // User-defined type
    }
    
    public static class Annotation {
        public String id;
        public String targetClass;
        public String targetMethod;
        public int lineNumber;
        public int address; // Smali address if applicable
        public AnnotationType type;
        public String title;
        public String content;
        public List<String> tags = new ArrayList<>();
        public long createdAt;
        public long modifiedAt;
        public String color; // Custom color for UI
        
        public Annotation() {
            this.id = UUID.randomUUID().toString();
            this.createdAt = System.currentTimeMillis();
            this.modifiedAt = this.createdAt;
        }
        
        public Annotation(String targetClass, String targetMethod, int address, 
                         AnnotationType type, String title, String content) {
            this();
            this.targetClass = targetClass;
            this.targetMethod = targetMethod;
            this.address = address;
            this.type = type;
            this.title = title;
            this.content = content;
        }
        
        public void addTag(String tag) {
            if (!tags.contains(tag)) {
                tags.add(tag);
            }
        }
        
        public void removeTag(String tag) {
            tags.remove(tag);
        }
        
        public String getSummary() {
            return String.format("[%s] %s: %s", type.name(), title, 
                    content.length() > 50 ? content.substring(0, 47) + "..." : content);
        }
        
        public Map<String, Object> toMap() {
            Map<String, Object> map = new LinkedHashMap<>();
            map.put("id", id);
            map.put("targetClass", targetClass);
            map.put("targetMethod", targetMethod);
            map.put("lineNumber", lineNumber);
            map.put("address", address);
            map.put("type", type.name());
            map.put("title", title);
            map.put("content", content);
            map.put("tags", tags);
            map.put("createdAt", createdAt);
            map.put("modifiedAt", modifiedAt);
            map.put("color", color);
            return map;
        }
        
        @Override
        public String toString() {
            return String.format("%s | %s.%s @ %04X | %s - %s",
                    id, targetClass, targetMethod, address, type.name(), title);
        }
    }
    
    public static class AnnotationCollection {
        public String projectName;
        public String apkPath;
        public Map<String, List<Annotation>> annotationsByClass = new HashMap<>();
        public Map<String, List<Annotation>> annotationsByMethod = new HashMap<>();
        public Map<AnnotationType, List<Annotation>> annotationsByType = new HashMap<>();
        public long lastModified;
        
        public AnnotationCollection() {
            this.lastModified = System.currentTimeMillis();
        }
        
        public void addAnnotation(Annotation annotation) {
            // By class
            annotationsByClass.computeIfAbsent(annotation.targetClass, k -> new ArrayList<>())
                             .add(annotation);
            
            // By method
            String methodKey = annotation.targetClass + "." + annotation.targetMethod;
            annotationsByMethod.computeIfAbsent(methodKey, k -> new ArrayList<>())
                              .add(annotation);
            
            // By type
            annotationsByType.computeIfAbsent(annotation.type, k -> new ArrayList<>())
                            .add(annotation);
            
            lastModified = System.currentTimeMillis();
        }
        
        public void removeAnnotation(String annotationId) {
            // Remove from all indices
            for (List<Annotation> list : annotationsByClass.values()) {
                list.removeIf(a -> a.id.equals(annotationId));
            }
            for (List<Annotation> list : annotationsByMethod.values()) {
                list.removeIf(a -> a.id.equals(annotationId));
            }
            for (List<Annotation> list : annotationsByType.values()) {
                list.removeIf(a -> a.id.equals(annotationId));
            }
            
            lastModified = System.currentTimeMillis();
        }
        
        public List<Annotation> getAnnotationsForClass(String className) {
            return annotationsByClass.getOrDefault(className, new ArrayList<>());
        }
        
        public List<Annotation> getAnnotationsForMethod(String className, String methodName) {
            String key = className + "." + methodName;
            return annotationsByMethod.getOrDefault(key, new ArrayList<>());
        }
        
        public List<Annotation> getAnnotationsByType(AnnotationType type) {
            return annotationsByType.getOrDefault(type, new ArrayList<>());
        }
        
        public List<Annotation> getAllAnnotations() {
            List<Annotation> all = new ArrayList<>();
            for (List<Annotation> list : annotationsByClass.values()) {
                all.addAll(list);
            }
            Collections.sort(all, (a, b) -> Long.compare(b.createdAt, a.createdAt));
            return all;
        }
        
        public int getTotalCount() {
            return getAllAnnotations().size();
        }
        
        public Map<String, Integer> getTypeCounts() {
            Map<String, Integer> counts = new HashMap<>();
            for (AnnotationType type : AnnotationType.values()) {
                counts.put(type.name(), getAnnotationsByType(type).size());
            }
            return counts;
        }
        
        public List<Annotation> searchAnnotations(String query) {
            List<Annotation> results = new ArrayList<>();
            String lowerQuery = query.toLowerCase();
            
            for (Annotation annotation : getAllAnnotations()) {
                if (annotation.title.toLowerCase().contains(lowerQuery) ||
                    annotation.content.toLowerCase().contains(lowerQuery) ||
                    annotation.targetClass.toLowerCase().contains(lowerQuery) ||
                    annotation.targetMethod.toLowerCase().contains(lowerQuery) ||
                    annotation.tags.stream().anyMatch(t -> t.toLowerCase().contains(lowerQuery))) {
                    results.add(annotation);
                }
            }
            
            return results;
        }
    }
    
    /**
     * Create a new annotation
     */
    public static Annotation createAnnotation(String targetClass, String targetMethod, 
                                              int address, AnnotationType type,
                                              String title, String content, 
                                              List<String> tags) {
        Annotation annotation = new Annotation(targetClass, targetMethod, address, type, title, content);
        if (tags != null) {
            annotation.tags.addAll(tags);
        }
        return annotation;
    }
    
    /**
     * Export annotations to JSON format
     */
    public static String exportToJson(AnnotationCollection collection) {
        StringBuilder sb = new StringBuilder();
        sb.append("{\n");
        sb.append("  \"projectName\": \"").append(collection.projectName).append("\",\n");
        sb.append("  \"apkPath\": \"").append(collection.apkPath).append("\",\n");
        sb.append("  \"lastModified\": ").append(collection.lastModified).append(",\n");
        sb.append("  \"totalAnnotations\": ").append(collection.getTotalCount()).append(",\n");
        
        sb.append("  \"annotations\": [\n");
        List<Annotation> all = collection.getAllAnnotations();
        for (int i = 0; i < all.size(); i++) {
            Annotation ann = all.get(i);
            sb.append("    {\n");
            sb.append("      \"id\": \"").append(ann.id).append("\",\n");
            sb.append("      \"targetClass\": \"").append(ann.targetClass).append("\",\n");
            sb.append("      \"targetMethod\": \"").append(ann.targetMethod).append("\",\n");
            sb.append("      \"address\": ").append(ann.address).append(",\n");
            sb.append("      \"type\": \"").append(ann.type.name()).append("\",\n");
            sb.append("      \"title\": \"").append(escapeJson(ann.title)).append("\",\n");
            sb.append("      \"content\": \"").append(escapeJson(ann.content)).append("\",\n");
            sb.append("      \"tags\": ").append(ann.tags).append(",\n");
            sb.append("      \"createdAt\": ").append(ann.createdAt).append("\n");
            sb.append("    }");
            if (i < all.size() - 1) sb.append(",");
            sb.append("\n");
        }
        sb.append("  ]\n");
        sb.append("}\n");
        
        return sb.toString();
    }
    
    /**
     * Import annotations from JSON format
     */
    public static AnnotationCollection importFromJson(String json) throws Exception {
        // Simple JSON parser (in production, use a proper library like Gson)
        AnnotationCollection collection = new AnnotationCollection();
        
        // This is a simplified parser - in production use Gson or Jackson
        // For now, return empty collection as placeholder
        System.out.println("JSON import requires external library (Gson/Jackson)");
        return collection;
    }
    
    /**
     * Export annotations to CSV format
     */
    public static String exportToCsv(AnnotationCollection collection) {
        StringBuilder sb = new StringBuilder();
        sb.append("ID,Target Class,Target Method,Address,Type,Title,Content,Tags,Created At\n");
        
        for (Annotation ann : collection.getAllAnnotations()) {
            sb.append(ann.id).append(",");
            sb.append("\"").append(ann.targetClass).append("\",");
            sb.append("\"").append(ann.targetMethod).append("\",");
            sb.append(ann.address).append(",");
            sb.append(ann.type.name()).append(",");
            sb.append("\"").append(escapeCsv(ann.title)).append("\",");
            sb.append("\"").append(escapeCsv(ann.content)).append("\",");
            sb.append("\"").append(String.join(";", ann.tags)).append("\",");
            sb.append(new Date(ann.createdAt)).append("\n");
        }
        
        return sb.toString();
    }
    
    /**
     * Save annotations to file
     */
    public static void saveToFile(AnnotationCollection collection, String filePath) throws IOException {
        String json = exportToJson(collection);
        try (FileWriter writer = new FileWriter(filePath)) {
            writer.write(json);
        }
    }
    
    /**
     * Load annotations from file
     */
    public static AnnotationCollection loadFromFile(String filePath) throws Exception {
        StringBuilder sb = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }
        }
        return importFromJson(sb.toString());
    }
    
    /**
     * Generate summary report
     */
    public static String generateReport(AnnotationCollection collection) {
        StringBuilder sb = new StringBuilder();
        sb.append("=== NGI PRO 2.0 Annotation Report ===\n\n");
        sb.append("Project: ").append(collection.projectName).append("\n");
        sb.append("APK: ").append(collection.apkPath).append("\n");
        sb.append("Total Annotations: ").append(collection.getTotalCount()).append("\n\n");
        
        sb.append("Annotations by Type:\n");
        sb.append("--------------------\n");
        for (Map.Entry<String, Integer> entry : collection.getTypeCounts().entrySet()) {
            if (entry.getValue() > 0) {
                sb.append(String.format("  %-15s: %d\n", entry.getKey(), entry.getValue()));
            }
        }
        
        sb.append("\nRecent Annotations:\n");
        sb.append("-------------------\n");
        List<Annotation> recent = collection.getAllAnnotations();
        int show = Math.min(10, recent.size());
        for (int i = 0; i < show; i++) {
            Annotation ann = recent.get(i);
            sb.append(String.format("  [%s] %s.%s @ %04X - %s\n",
                    ann.type.name(), ann.targetClass, ann.targetMethod, 
                    ann.address, ann.title));
        }
        
        return sb.toString();
    }
    
    private static String escapeJson(String text) {
        if (text == null) return "";
        return text.replace("\\", "\\\\")
                   .replace("\"", "\\\"")
                   .replace("\n", "\\n")
                   .replace("\r", "\\r")
                   .replace("\t", "\\t");
    }
    
    private static String escapeCsv(String text) {
        if (text == null) return "";
        return text.replace("\"", "\"\"");
    }
    
    /**
     * Get default color for annotation type
     */
    public static String getDefaultColor(AnnotationType type) {
        switch (type) {
            case WARNING: return "#FF5733";
            case IMPORTANT: return "#C70039";
            case TODO: return "#900C3F";
            case HOOK: return "#581845";
            case DECRYPT: return "#FFC300";
            case CRYPTO: return "#DAF7A6";
            case API_KEY: return "#33FFF5";
            case TRACKER: return "#FF33A1";
            case OBFUSCATED: return "#8E44AD";
            case NATIVE: return "#3498DB";
            case NOTE: default: return "#34495E";
        }
    }
}
