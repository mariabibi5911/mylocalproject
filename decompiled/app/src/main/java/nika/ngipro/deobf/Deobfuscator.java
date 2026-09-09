package nika.ngipro.deobf;

import org.jf.dexlib2.iface.ClassDef;
import org.jf.dexlib2.iface.Method;
import org.jf.dexlib2.iface.Field;

import java.util.*;
import java.util.regex.Pattern;

/**
 * Deobfuscation Assistant for NGI PRO 2.0
 * Helps rename obfuscated classes, methods, and fields
 */
public class Deobfuscator {
    
    public static class DeobfResult {
        public Map<String, String> classNameMapping = new LinkedHashMap<>();
        public Map<String, Map<String, String>> methodNameMappings = new LinkedHashMap<>();
        public Map<String, Map<String, String>> fieldNameMappings = new LinkedHashMap<>();
        public List<ObfuscationPattern> detectedPatterns = new ArrayList<>();
        public int totalClasses;
        public int totalMethods;
        public int totalFields;
        public int obfuscatedClasses;
        public int obfuscatedMethods;
        public int obfuscatedFields;
        
        public String getSummary() {
            return String.format("Analyzed: %d classes, %d methods, %d fields | Obfuscated: %d classes, %d methods, %d fields",
                    totalClasses, totalMethods, totalFields,
                    obfuscatedClasses, obfuscatedMethods, obfuscatedFields);
        }
        
        public String toProguardFormat() {
            StringBuilder sb = new StringBuilder();
            sb.append("# NGI PRO 2.0 Deobfuscation Mapping\n");
            sb.append("# Generated mapping file for ProGuard/R8\n\n");
            
            for (Map.Entry<String, String> entry : classNameMapping.entrySet()) {
                sb.append(entry.getKey()).append(" -> ").append(entry.getValue()).append(":\n");
                
                // Add method mappings
                if (methodNameMappings.containsKey(entry.getKey())) {
                    for (Map.Entry<String, String> method : methodNameMappings.get(entry.getKey()).entrySet()) {
                        sb.append("    ").append(method.getKey()).append(" -> ").append(method.getValue()).append(";\n");
                    }
                }
                
                // Add field mappings
                if (fieldNameMappings.containsKey(entry.getKey())) {
                    for (Map.Entry<String, String> field : fieldNameMappings.get(entry.getKey()).entrySet()) {
                        sb.append("    ").append(field.getKey()).append(" -> ").append(field.getValue()).append(";\n");
                    }
                }
                
                sb.append("\n");
            }
            
            return sb.toString();
        }
        
        public String toJsonFormat() {
            StringBuilder sb = new StringBuilder();
            sb.append("{\n");
            sb.append("  \"summary\": \"").append(getSummary()).append("\",\n");
            
            sb.append("  \"classMappings\": {\n");
            int count = 0;
            for (Map.Entry<String, String> entry : classNameMapping.entrySet()) {
                if (count > 0) sb.append(",\n");
                sb.append("    \"").append(entry.getKey()).append("\": \"").append(entry.getValue()).append("\"");
                count++;
            }
            sb.append("\n  },\n");
            
            sb.append("  \"patterns\": [\n");
            count = 0;
            for (ObfuscationPattern pattern : detectedPatterns) {
                if (count > 0) sb.append(",\n");
                sb.append("    {\n");
                sb.append("      \"type\": \"").append(pattern.type).append("\",\n");
                sb.append("      \"pattern\": \"").append(pattern.pattern).append("\",\n");
                sb.append("      \"count\": ").append(pattern.count).append("\n");
                sb.append("    }");
                count++;
            }
            sb.append("\n  ]\n");
            
            sb.append("}\n");
            return sb.toString();
        }
    }
    
    public static class ObfuscationPattern {
        public String type; // CLASS, METHOD, FIELD
        public String pattern;
        public int count;
        
        public ObfuscationPattern(String type, String pattern, int count) {
            this.type = type;
            this.pattern = pattern;
            this.count = count;
        }
    }
    
    private static final Pattern OBFUSCATED_NAME_PATTERN = Pattern.compile("^[a-zA-Z]\\w{0,2}$|^[a-z]{1,3}\\d*$");
    private static final Pattern MEANINGLESS_PATTERN = Pattern.compile("^[abcdezijlmnopqrsuvxy]+\\d*$", Pattern.CASE_INSENSITIVE);
    
    /**
     * Analyze classes for obfuscation patterns
     */
    public static DeobfResult analyzeObfuscation(List<ClassDef> classes) {
        DeobfResult result = new DeobfResult();
        Map<String, Integer> nameFrequency = new HashMap<>();
        Map<String, Integer> packagePatterns = new HashMap<>();
        
        for (ClassDef classDef : classes) {
            result.totalClasses++;
            String className = classDef.getType();
            String simpleName = extractSimpleName(className);
            
            if (isObfuscated(simpleName)) {
                result.obfuscatedClasses++;
                result.classNameMapping.put(className, generateMeaningfulName(className, "Class"));
                
                // Track patterns
                String pattern = detectPattern(simpleName);
                nameFrequency.merge(pattern, 1, Integer::sum);
                
                // Extract package pattern
                String pkg = extractPackage(className);
                if (!pkg.isEmpty()) {
                    packagePatterns.merge(pkg, 1, Integer::sum);
                }
            }
            
            // Analyze methods
            if (classDef.getMethods() != null) {
                for (Method method : classDef.getMethods()) {
                    result.totalMethods++;
                    String methodName = method.getName();
                    
                    if (isObfuscated(methodName) && !isConstructor(methodName)) {
                        result.obfuscatedMethods++;
                        
                        result.methodNameMappings
                            .computeIfAbsent(className, k -> new LinkedHashMap<>())
                            .put(methodName, generateMethodHint(method));
                    }
                }
            }
            
            // Analyze fields
            if (classDef.getFields() != null) {
                for (Field field : classDef.getFields()) {
                    result.totalFields++;
                    String fieldName = field.getName();
                    
                    if (isObfuscated(fieldName)) {
                        result.obfuscatedFields++;
                        
                        result.fieldNameMappings
                            .computeIfAbsent(className, k -> new LinkedHashMap<>())
                            .put(fieldName, generateFieldHint(field));
                    }
                }
            }
        }
        
        // Detect common patterns
        for (Map.Entry<String, Integer> entry : nameFrequency.entrySet()) {
            if (entry.getValue() >= 3) {
                result.detectedPatterns.add(new ObfuscationPattern("CLASS", entry.getKey(), entry.getValue()));
            }
        }
        
        // Detect package-level obfuscation
        for (Map.Entry<String, Integer> entry : packagePatterns.entrySet()) {
            if (entry.getValue() >= 5) {
                result.detectedPatterns.add(new ObfuscationPattern("PACKAGE", entry.getKey(), entry.getValue()));
            }
        }
        
        return result;
    }
    
    /**
     * Check if a name appears to be obfuscated
     */
    public static boolean isObfuscated(String name) {
        if (name == null || name.isEmpty()) return true;
        if (name.equals("<init>") || name.equals("<clinit>")) return false;
        
        // Very short names (1-3 chars) are likely obfuscated
        if (name.length() <= 3 && OBFUSCATED_NAME_PATTERN.matcher(name).matches()) {
            return true;
        }
        
        // Names matching meaningless patterns
        if (MEANINGLESS_PATTERN.matcher(name).matches()) {
            return true;
        }
        
        // Single letter names
        if (name.length() == 1 && Character.isLetter(name.charAt(0))) {
            return true;
        }
        
        return false;
    }
    
    /**
     * Generate meaningful name suggestion based on context
     */
    public static String generateMeaningfulName(String originalName, String prefix) {
        // Extract hash or unique identifier from original name
        String simpleName = extractSimpleName(originalName);
        String hash = simpleName.length() > 2 ? simpleName.substring(Math.max(0, simpleName.length() - 2)) : "01";
        
        return prefix + "_" + hash.toUpperCase();
    }
    
    /**
     * Generate method name hint based on signature analysis
     */
    public static String generateMethodHint(Method method) {
        String returnType = method.getReturnType();
        List<? extends org.jf.dexlib2.iface.MethodParameter> params = method.getParameters();
        
        // Heuristic naming based on signature
        if (returnType.contains("String") && params.isEmpty()) {
            return "getStringValue";
        } else if (returnType.contains("boolean") && params.isEmpty()) {
            return "checkCondition";
        } else if (returnType.contains("int") && params.isEmpty()) {
            return "getValue";
        } else if (returnType.equals("void") && params.size() == 1 && params.get(0).getType().contains("Context")) {
            return "initializeWithContext";
        } else if (params.size() >= 2 && params.get(0).getType().contains("byte[]")) {
            return "processData";
        } else if (method.getName().startsWith("access")) {
            return "syntheticAccess";
        }
        
        // Default generic name
        return "method_" + Math.abs(method.hashCode() % 1000);
    }
    
    /**
     * Generate field name hint based on type
     */
    public static String generateFieldHint(Field field) {
        String fieldType = field.getType();
        
        if (fieldType.contains("String")) {
            return "stringValue";
        } else if (fieldType.contains("int") || fieldType.contains("long")) {
            return "numericValue";
        } else if (fieldType.contains("boolean")) {
            return "flagValue";
        } else if (fieldType.contains("List") || fieldType.contains("Array")) {
            return "collectionValue";
        } else if (fieldType.contains("Map")) {
            return "mapValue";
        } else if (fieldType.contains("Context")) {
            return "contextRef";
        } else if (fieldType.contains("View") || fieldType.contains("Activity")) {
            return "uiReference";
        }
        
        return "field_" + Math.abs(field.hashCode() % 1000);
    }
    
    /**
     * Detect obfuscation pattern in a name
     */
    private static String detectPattern(String name) {
        if (name.matches("^[a-z]$")) return "single_lowercase";
        if (name.matches("^[A-Z]$")) return "single_uppercase";
        if (name.matches("^[a-z]{2,3}$")) return "short_lowercase";
        if (name.matches("^[A-Z]{2,3}$")) return "short_uppercase";
        if (name.matches("^[a-z]+\\d+$")) return "letters_numbers";
        if (name.matches("^[a-z][A-Z][a-z]$")) return "camelCase_short";
        return "unknown_pattern";
    }
    
    /**
     * Extract simple name from full class descriptor
     */
    private static String extractSimpleName(String classDescriptor) {
        if (classDescriptor.startsWith("L") && classDescriptor.endsWith(";")) {
            classDescriptor = classDescriptor.substring(1, classDescriptor.length() - 1);
        }
        
        int lastSlash = classDescriptor.lastIndexOf('/');
        if (lastSlash >= 0) {
            return classDescriptor.substring(lastSlash + 1);
        }
        return classDescriptor;
    }
    
    /**
     * Extract package from class descriptor
     */
    private static String extractPackage(String classDescriptor) {
        if (classDescriptor.startsWith("L") && classDescriptor.endsWith(";")) {
            classDescriptor = classDescriptor.substring(1, classDescriptor.length() - 1);
        }
        
        int lastSlash = classDescriptor.lastIndexOf('/');
        if (lastSlash >= 0) {
            return classDescriptor.substring(0, lastSlash);
        }
        return "";
    }
    
    /**
     * Check if method name indicates constructor
     */
    private static boolean isConstructor(String name) {
        return name.equals("<init>") || name.equals("<clinit>");
    }
    
    /**
     * Batch rename suggestions for all obfuscated items
     */
    public static Map<String, String> generateBatchRenameSuggestions(DeobfResult result, String prefix) {
        Map<String, String> suggestions = new LinkedHashMap<>();
        int counter = 1;
        
        for (String className : result.classNameMapping.keySet()) {
            String simpleName = extractSimpleName(className);
            String newName = prefix + "_" + String.format("%03d", counter);
            suggestions.put(className, newName);
            counter++;
        }
        
        return suggestions;
    }
    
    /**
     * Export mapping as Frida script for runtime renaming
     */
    public static String exportAsFridaScript(DeobfResult result) {
        StringBuilder sb = new StringBuilder();
        sb.append("// NGI PRO 2.0 - Frida Script for Deobfuscation\n");
        sb.append("// Attach this script to see deobfuscated names at runtime\n\n");
        sb.append("Java.perform(function() {\n");
        
        for (Map.Entry<String, String> entry : result.classNameMapping.entrySet()) {
            String originalClass = entry.getKey().replace("/", ".").replace("L", "").replace(";", "");
            String newName = entry.getValue();
            
            sb.append("    try {\n");
            sb.append("        var ").append(newName).append(" = Java.use(\"").append(originalClass).append("\");\n");
            sb.append("        console.log('Loaded: ").append(newName).append("');\n");
            sb.append("    } catch(e) {}\n\n");
        }
        
        sb.append("});\n");
        return sb.toString();
    }
}
