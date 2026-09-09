package nika.ngipro.hooks;

import java.util.ArrayList;
import java.util.List;

/**
 * HookGenerator - Generates Frida and Xposed hook templates for reverse engineering.
 * Features:
 * - Generate Frida JavaScript hooks
 * - Generate Xposed module code
 * - Support for method tracing, parameter logging, return value modification
 */
public class HookGenerator {
    
    public enum HookType { FRIDA, XPOSED }
    public enum HookPurpose { TRACE, MODIFY_RETURN, MODIFY_PARAM, BLOCK, LOG }
    
    public static class HookTemplate {
        public String className;
        public String methodName;
        public String methodSignature;
        public HookType type;
        public HookPurpose purpose;
        public String code;
        public List<String> imports = new ArrayList<>();
        
        @Override
        public String toString() {
            return code;
        }
    }
    
    /**
     * Generate Frida hook script for a method
     */
    public static HookTemplate generateFridaHook(String className, String methodName, 
                                                  String methodSignature, HookPurpose purpose) {
        HookTemplate template = new HookTemplate();
        template.className = className;
        template.methodName = methodName;
        template.methodSignature = methodSignature;
        template.type = HookType.FRIDA;
        template.purpose = purpose;
        
        StringBuilder code = new StringBuilder();
        code.append("// Frida Hook Template\n");
        code.append("// Target: ").append(className).append("#").append(methodName).append("\n\n");
        code.append("Java.perform(function() {\n");
        code.append("    var targetClass = Java.use(\"").append(className.replace("/", ".")).append("\");\n\n");
        
        switch (purpose) {
            case TRACE:
                code.append(generateFridaTrace(template));
                break;
            case MODIFY_RETURN:
                code.append(generateFridaModifyReturn(template));
                break;
            case MODIFY_PARAM:
                code.append(generateFridaModifyParam(template));
                break;
            case BLOCK:
                code.append(generateFridaBlock(template));
                break;
            case LOG:
                code.append(generateFridaLog(template));
                break;
        }
        
        code.append("});\n");
        template.code = code.toString();
        template.imports.add("frida-java-bridge");
        
        return template;
    }
    
    private static String generateFridaTrace(HookTemplate template) {
        return "    targetClass." + template.methodName + ".implementation = function() {\n" +
               "        console.log('[*] Entering: " + template.className + "." + template.methodName + "()');\n" +
               "        console.log('    Arguments: ' + JSON.stringify(arguments));\n" +
               "        \n" +
               "        var result = this." + template.methodName + ".apply(this, arguments);\n" +
               "        \n" +
               "        console.log('[*] Exiting: " + template.className + "." + template.methodName + "()');\n" +
               "        console.log('    Return value: ' + result);\n" +
               "        \n" +
               "        return result;\n" +
               "    };\n";
    }
    
    private static String generateFridaModifyReturn(HookTemplate template) {
        return "    targetClass." + template.methodName + ".implementation = function() {\n" +
               "        console.log('[*] Intercepting: " + template.className + "." + template.methodName + "()');\n" +
               "        \n" +
               "        // Call original method\n" +
               "        var result = this." + template.methodName + ".apply(this, arguments);\n" +
               "        \n" +
               "        // TODO: Modify return value\n" +
               "        // Example: return true;\n" +
               "        // Example: return null;\n" +
               "        // Example: return Java.use('java.lang.String').$new('modified');\n" +
               "        \n" +
               "        return result; // Change this to return modified value\n" +
               "    };\n";
    }
    
    private static String generateFridaModifyParam(HookTemplate template) {
        return "    targetClass." + template.methodName + ".implementation = function() {\n" +
               "        console.log('[*] Intercepting parameters: " + template.className + "." + template.methodName + "()');\n" +
               "        \n" +
               "        // TODO: Modify arguments\n" +
               "        // Example: arguments[0] = 'modified_value';\n" +
               "        // Example: arguments[1] = Java.use('java.lang.Integer').$new(42);\n" +
               "        \n" +
               "        console.log('    Modified arguments: ' + JSON.stringify(arguments));\n" +
               "        \n" +
               "        return this." + template.methodName + ".apply(this, arguments);\n" +
               "    };\n";
    }
    
    private static String generateFridaBlock(HookTemplate template) {
        return "    targetClass." + template.methodName + ".implementation = function() {\n" +
               "        console.log('[!] BLOCKED: " + template.className + "." + template.methodName + "()');\n" +
               "        \n" +
               "        // TODO: Return appropriate default value\n" +
               "        // For boolean methods: return false;\n" +
               "        // For int methods: return 0;\n" +
               "        // For object methods: return null;\n" +
               "        \n" +
               "        return null; // Modify based on return type\n" +
               "    };\n";
    }
    
    private static String generateFridaLog(HookTemplate template) {
        return "    targetClass." + template.methodName + ".implementation = function() {\n" +
               "        var timestamp = new Date().toISOString();\n" +
               "        console.log('[' + timestamp + '] " + template.className + "." + template.methodName + "()');\n" +
               "        console.log('    Thread: ' + Process.id);\n" +
               "        console.log('    Arguments: ' + JSON.stringify(arguments));\n" +
               "        \n" +
               "        var startTime = Date.now();\n" +
               "        var result = this." + template.methodName + ".apply(this, arguments);\n" +
               "        var endTime = Date.now();\n" +
               "        \n" +
               "        console.log('    Execution time: ' + (endTime - startTime) + 'ms');\n" +
               "        console.log('    Return: ' + result);\n" +
               "        \n" +
               "        return result;\n" +
               "    };\n";
    }
    
    /**
     * Generate Xposed hook template
     */
    public static HookTemplate generateXposedHook(String className, String methodName, 
                                                   String methodSignature, HookPurpose purpose) {
        HookTemplate template = new HookTemplate();
        template.className = className;
        template.methodName = methodName;
        template.methodSignature = methodSignature;
        template.type = HookType.XPOSED;
        template.purpose = purpose;
        
        String simpleClassName = className.substring(className.lastIndexOf('/') + 1);
        
        StringBuilder code = new StringBuilder();
        code.append("// Xposed Hook Template\n");
        code.append("// Add to your XposedModule.java\n\n");
        code.append("@Override\n");
        code.append("public void handleLoadPackage(XC_LoadPackage.LoadPackageParam lpparam) throws Throwable {\n");
        code.append("    if (\"").append(className.substring(0, className.lastIndexOf('/')).replace("/", "."))
             .append("\".equals(lpparam.packageName)) {\n");
        code.append("        \n");
        
        switch (purpose) {
            case TRACE:
                code.append(generateXposedTrace(template, simpleClassName));
                break;
            case MODIFY_RETURN:
                code.append(generateXposedModifyReturn(template, simpleClassName));
                break;
            case MODIFY_PARAM:
                code.append(generateXposedModifyParam(template, simpleClassName));
                break;
            case BLOCK:
                code.append(generateXposedBlock(template, simpleClassName));
                break;
            case LOG:
                code.append(generateXposedLog(template, simpleClassName));
                break;
        }
        
        code.append("    }\n");
        code.append("}\n");
        template.code = code.toString();
        template.imports.add("de.robv.android.xposed.XC_MethodHook");
        template.imports.add("de.robv.android.xposed.XposedHelpers");
        template.imports.add("de.robv.android.xposed.XposedBridge");
        
        return template;
    }
    
    private static String generateXposedTrace(HookTemplate template, String simpleClassName) {
        return "        XposedHelpers.findAndHookMethod(" + simpleClassName + ".class, \"" + 
               template.methodName + "\", /* parameters */, new XC_MethodHook() {\n" +
               "            @Override\n" +
               "            protected void beforeHookedMethod(MethodHookParam param) throws Throwable {\n" +
               "                XposedBridge.log(\"[TRACE] Entering: " + template.className + "." + 
               template.methodName + "\");\n" +
               "                XposedBridge.log(\"    Args: \" + Arrays.toString(param.args));\n" +
               "            }\n" +
               "            \n" +
               "            @Override\n" +
               "            protected void afterHookedMethod(MethodHookParam param) throws Throwable {\n" +
               "                XposedBridge.log(\"[TRACE] Exiting: " + template.className + "." + 
               template.methodName + "\");\n" +
               "                XposedBridge.log(\"    Result: \" + param.getResult());\n" +
               "            }\n" +
               "        });\n";
    }
    
    private static String generateXposedModifyReturn(HookTemplate template, String simpleClassName) {
        return "        XposedHelpers.findAndHookMethod(" + simpleClassName + ".class, \"" + 
               template.methodName + "\", /* parameters */, new XC_MethodHook() {\n" +
               "            @Override\n" +
               "            protected void afterHookedMethod(MethodHookParam param) throws Throwable {\n" +
               "                // TODO: Modify return value\n" +
               "                // param.setResult(newValue);\n" +
               "                \n" +
               "                XposedBridge.log(\"[MODIFY] Original result: \" + param.getResult());\n" +
               "            }\n" +
               "        });\n";
    }
    
    private static String generateXposedModifyParam(HookTemplate template, String simpleClassName) {
        return "        XposedHelpers.findAndHookMethod(" + simpleClassName + ".class, \"" + 
               template.methodName + "\", /* parameters */, new XC_MethodHook() {\n" +
               "            @Override\n" +
               "            protected void beforeHookedMethod(MethodHookParam param) throws Throwable {\n" +
               "                // TODO: Modify parameters\n" +
               "                // param.args[0] = newValue;\n" +
               "                \n" +
               "                XposedBridge.log(\"[MODIFY] Modified args: \" + Arrays.toString(param.args));\n" +
               "            }\n" +
               "        });\n";
    }
    
    private static String generateXposedBlock(HookTemplate template, String simpleClassName) {
        return "        XposedHelpers.findAndHookMethod(" + simpleClassName + ".class, \"" + 
               template.methodName + "\", /* parameters */, new XC_MethodHook() {\n" +
               "            @Override\n" +
               "            protected void beforeHookedMethod(MethodHookParam param) throws Throwable {\n" +
               "                XposedBridge.log(\"[BLOCK] Preventing execution of: " + 
               template.className + "." + template.methodName + "\");\n" +
               "                \n" +
               "                // TODO: Set appropriate default result\n" +
               "                // param.setResult(false); // for boolean\n" +
               "                // param.setResult(0); // for int\n" +
               "                // param.setResult(null); // for objects\n" +
               "                \n" +
               "                param.setResult(null); // Modify based on return type\n" +
               "            }\n" +
               "        });\n";
    }
    
    private static String generateXposedLog(HookTemplate template, String simpleClassName) {
        return "        XposedHelpers.findAndHookMethod(" + simpleClassName + ".class, \"" + 
               template.methodName + "\", /* parameters */, new XC_MethodHook() {\n" +
               "            @Override\n" +
               "            protected void beforeHookedMethod(MethodHookParam param) throws Throwable {\n" +
               "                long startTime = System.currentTimeMillis();\n" +
               "                XposedBridge.log(\"[LOG] \" + " + simpleClassName + ".class.getSimpleName() + \".\"" + 
               template.methodName + " called with: \" + Arrays.toString(param.args));\n" +
               "                param.setObjectExtra(\"startTime\", startTime);\n" +
               "            }\n" +
               "            \n" +
               "            @Override\n" +
               "            protected void afterHookedMethod(MethodHookParam param) throws Throwable {\n" +
               "                long startTime = (Long) param.getObjectExtra(\"startTime\");\n" +
               "                long duration = System.currentTimeMillis() - startTime;\n" +
               "                XposedBridge.log(\"[LOG] \" + " + simpleClassName + ".class.getSimpleName() + \".\"" + 
               template.methodName + " returned: \" + param.getResult() + \" (\" + duration + \"ms)\");\n" +
               "            }\n" +
               "        });\n";
    }
}
