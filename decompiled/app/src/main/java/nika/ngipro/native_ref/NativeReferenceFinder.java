package nika.ngipro.native_ref;

import org.jf.dexlib2.iface.ClassDef;
import org.jf.dexlib2.iface.Method;
import org.jf.dexlib2.iface.instruction.Instruction;
import org.jf.dexlib2.iface.instruction.ReferenceInstruction;

import java.util.*;
import java.util.regex.Pattern;

/**
 * Native Library Cross-Reference Finder for NGI PRO 2.0
 * Links Java JNI calls to native SO functions
 */
public class NativeReferenceFinder {
    
    public static class NativeCall {
        public String javaClass;
        public String javaMethod;
        public String nativeLibrary; // e.g., "native-lib"
        public String nativeFunction; // e.g., "Java_com_example_myapp_MainActivity_stringFromJNI"
        public String signature; // JNI signature
        public int lineNumber;
        public boolean isStatic;
        
        public NativeCall(String javaClass, String javaMethod, String nativeLib, 
                         String nativeFunc, String sig, boolean isStatic) {
            this.javaClass = javaClass;
            this.javaMethod = javaMethod;
            this.nativeLibrary = nativeLib;
            this.nativeFunction = nativeFunc;
            this.signature = sig;
            this.isStatic = isStatic;
        }
        
        public String getFullJavaName() {
            return javaClass + "." + javaMethod;
        }
        
        @Override
        public String toString() {
            return String.format("%s.%s -> %s:%s", javaClass, javaMethod, nativeLibrary, nativeFunction);
        }
    }
    
    public static class NativeAnalysisResult {
        public List<NativeCall> nativeCalls = new ArrayList<>();
        public Map<String, Integer> libraryUsageCount = new HashMap<>();
        public Set<String> unresolvedNatives = new HashSet<>();
        public int totalNativeMethods;
        public int totalClassesWithNatives;
        
        public String getSummary() {
            return String.format("Found %d native calls across %d libraries in %d classes",
                    nativeCalls.size(), libraryUsageCount.size(), totalClassesWithNatives);
        }
        
        public String toJsonFormat() {
            StringBuilder sb = new StringBuilder();
            sb.append("{\n");
            sb.append("  \"summary\": \"").append(getSummary()).append("\",\n");
            sb.append("  \"totalNativeMethods\": ").append(totalNativeMethods).append(",\n");
            sb.append("  \"libraries\": {\n");
            
            int count = 0;
            for (Map.Entry<String, Integer> entry : libraryUsageCount.entrySet()) {
                if (count > 0) sb.append(",\n");
                sb.append("    \"").append(entry.getKey()).append("\": ").append(entry.getValue());
                count++;
            }
            sb.append("\n  },\n");
            
            sb.append("  \"calls\": [\n");
            count = 0;
            for (NativeCall call : nativeCalls) {
                if (count > 0) sb.append(",\n");
                sb.append("    {\n");
                sb.append("      \"javaClass\": \"").append(call.javaClass).append("\",\n");
                sb.append("      \"javaMethod\": \"").append(call.javaMethod).append("\",\n");
                sb.append("      \"nativeLibrary\": \"").append(call.nativeLibrary).append("\",\n");
                sb.append("      \"nativeFunction\": \"").append(call.nativeFunction).append("\",\n");
                sb.append("      \"signature\": \"").append(call.signature).append("\",\n");
                sb.append("      \"isStatic\": ").append(call.isStatic).append("\n");
                sb.append("    }");
                count++;
            }
            sb.append("\n  ]\n");
            sb.append("}\n");
            
            return sb.toString();
        }
    }
    
    private static final Pattern NATIVE_METHOD_PATTERN = Pattern.compile(
        "L(.*?);->([a-zA-Z_][a-zA-Z0-9_]*)\\((.*?)\\)(.*)"
    );
    
    /**
     * Analyze DEX files for native method declarations and calls
     */
    public static NativeAnalysisResult analyzeNativeReferences(List<ClassDef> classes) {
        NativeAnalysisResult result = new NativeAnalysisResult();
        Set<String> classesWithNatives = new HashSet<>();
        
        for (ClassDef classDef : classes) {
            String className = classDef.getType();
            
            if (classDef.getMethods() != null) {
                for (Method method : classDef.getMethods()) {
                    // Check if method is declared as native
                    if ((method.getAccessFlags() & 0x0100) != 0) { // ACC_NATIVE
                        result.totalNativeMethods++;
                        classesWithNatives.add(className);
                        
                        String methodName = method.getName();
                        String methodSignature = buildJNISignature(method);
                        
                        // Generate expected native function name
                        String nativeFuncName = generateNativeFunctionName(className, methodName, methodSignature);
                        
                        // Try to determine which library this belongs to
                        String libraryName = inferLibraryName(classDef);
                        
                        NativeCall call = new NativeCall(
                            className.replace("/", ".").replace("L", "").replace(";", ""),
                            methodName,
                            libraryName,
                            nativeFuncName,
                            methodSignature,
                            (method.getAccessFlags() & 0x0008) != 0 // ACC_STATIC
                        );
                        
                        result.nativeCalls.add(call);
                        result.libraryUsageCount.merge(libraryName, 1, Integer::sum);
                    }
                    
                    // Also check for System.loadLibrary calls in method bodies
                    analyzeMethodForLibraryLoads(method, result, className);
                }
            }
        }
        
        result.totalClassesWithNatives = classesWithNatives.size();
        return result;
    }
    
    /**
     * Analyze method body for System.loadLibrary() calls
     */
    private static void analyzeMethodForLibraryLoads(Method method, NativeAnalysisResult result, String className) {
        try {
            if (method.getImplementation() == null) return;
            
            List<? extends Instruction> instructions = method.getImplementation().getInstructions();
            int lineNum = 0;
            
            for (Instruction instr : instructions) {
                lineNum++;
                
                if (instr instanceof ReferenceInstruction) {
                    ReferenceInstruction refInstr = (ReferenceInstruction) instr;
                    String reference = refInstr.getReference().toString();
                    
                    // Look for loadLibrary calls
                    if (reference.contains("loadLibrary")) {
                        // Extract library name from nearby string constants
                        // This is simplified - full implementation would track register values
                        String libName = extractLibraryNameFromContext(instructions, lineNum);
                        if (libName != null && !result.libraryUsageCount.containsKey(libName)) {
                            result.libraryUsageCount.put(libName, 0);
                        }
                    }
                }
            }
        } catch (Exception e) {
            // Ignore analysis errors
        }
    }
    
    /**
     * Build JNI signature for a method
     */
    private static String buildJNISignature(Method method) {
        StringBuilder sb = new StringBuilder();
        sb.append("(");
        
        // Parameter types
        if (method.getParameters() != null) {
            for (org.jf.dexlib2.iface.MethodParameter param : method.getParameters()) {
                sb.append(param.getType().replace("/", "."));
            }
        }
        
        sb.append(")");
        
        // Return type
        sb.append(method.getReturnType().replace("/", "."));
        
        return sb.toString();
    }
    
    /**
     * Generate expected native function name following JNI conventions
     */
    private static String generateNativeFunctionName(String className, String methodName, String signature) {
        // Remove L and ; from class name
        String cleanClassName = className.replace("L", "").replace(";", "");
        
        // Replace slashes with underscores
        String packagePath = cleanClassName.replace("/", "_");
        
        // Full JNI name: Java_package_class_method
        return "Java_" + packagePath + "_" + methodName;
    }
    
    /**
     * Infer library name from class context
     */
    private static String inferLibraryName(ClassDef classDef) {
        String className = classDef.getType();
        
        // Common patterns
        if (className.contains("jni")) return "jni-lib";
        if (className.contains("native")) return "native-lib";
        if (className.contains("crypto")) return "crypto-lib";
        
        // Use package name as hint
        int lastSlash = className.lastIndexOf('/');
        if (lastSlash > 0) {
            String packageName = className.substring(0, lastSlash);
            int firstSlash = packageName.indexOf('/');
            if (firstSlash >= 0) {
                return packageName.substring(firstSlash + 1).replace('/', '-');
            }
        }
        
        return "unknown-lib";
    }
    
    /**
     * Extract library name from instruction context (simplified)
     */
    private static String extractLibraryNameFromContext(List<? extends Instruction> instructions, int currentLine) {
        // Look at nearby instructions for string constants
        int start = Math.max(0, currentLine - 5);
        int end = Math.min(instructions.size(), currentLine + 5);
        
        for (int i = start; i < end; i++) {
            Instruction instr = instructions.get(i);
            if (instr instanceof ReferenceInstruction) {
                String ref = ((ReferenceInstruction) instr).getReference().toString();
                // Simple heuristic: look for common library names
                if (ref.matches("^[a-zA-Z0-9_-]+$") && ref.length() < 30) {
                    return ref;
                }
            }
        }
        
        return null;
    }
    
    /**
     * Find all usages of a specific native method
     */
    public static List<String> findNativeMethodUsages(List<ClassDef> classes, String nativeMethodName) {
        List<String> usages = new ArrayList<>();
        
        for (ClassDef classDef : classes) {
            if (classDef.getMethods() != null) {
                for (Method method : classDef.getMethods()) {
                    try {
                        if (method.getImplementation() != null) {
                            for (Instruction instr : method.getImplementation().getInstructions()) {
                                if (instr instanceof ReferenceInstruction) {
                                    String ref = ((ReferenceInstruction) instr).getReference().toString();
                                    if (ref.contains(nativeMethodName)) {
                                        usages.add(classDef.getType() + "." + method.getName());
                                        break;
                                    }
                                }
                            }
                        }
                    } catch (Exception e) {
                        // Continue on error
                    }
                }
            }
        }
        
        return usages;
    }
    
    /**
     * Generate Frida script to intercept native calls
     */
    public static String generateFridaInterceptor(NativeAnalysisResult result) {
        StringBuilder sb = new StringBuilder();
        sb.append("// NGI PRO 2.0 - Native Method Interceptor\n");
        sb.append("// Attach this script to intercept JNI calls\n\n");
        sb.append("Java.perform(function() {\n\n");
        
        for (NativeCall call : result.nativeCalls) {
            String javaClassName = call.javaClass;
            String javaMethodName = call.javaMethod;
            
            sb.append("    try {\n");
            sb.append("        var targetClass = Java.use(\"").append(javaClassName).append("\");\n");
            sb.append("        \n");
            sb.append("        targetClass.").append(javaMethodName).append(".implementation = function() {\n");
            sb.append("            console.log('[*] Calling native method: ").append(javaMethodName).append("');\n");
            sb.append("            console.log('    Arguments:', JSON.stringify(arguments));\n");
            sb.append("            \n");
            sb.append("            var result = this.").append(javaMethodName).append(".apply(this, arguments);\n");
            sb.append("            console.log('    Return value:', result);\n");
            sb.append("            \n");
            sb.append("            return result;\n");
            sb.append("        };\n");
            sb.append("    } catch(e) {\n");
            sb.append("        // Class not found: ").append(javaClassName).append("\n");
            sb.append("    }\n\n");
        }
        
        sb.append("});\n");
        return sb.toString();
    }
    
    /**
     * Export native call map for IDA/Ghidra integration
     */
    public static String exportToIDAFormat(NativeAnalysisResult result) {
        StringBuilder sb = new StringBuilder();
        sb.append("# NGI PRO 2.0 - Native Call Map for IDA Pro\n");
        sb.append("# Format: JavaClass.JavaMethod -> NativeFunction@Library\n\n");
        
        for (NativeCall call : result.nativeCalls) {
            sb.append(call.getFullJavaName())
              .append(" -> ")
              .append(call.nativeFunction)
              .append("@")
              .append(call.nativeLibrary)
              .append("\n");
        }
        
        return sb.toString();
    }
}
