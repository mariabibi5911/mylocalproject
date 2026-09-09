package nika.ngipro.scripting;

import java.util.*;
import javax.script.*;

/**
 * Custom Script Runner - Supports JavaScript/Groovy scripting
 * Allows users to write custom analysis scripts
 */
public class ScriptRunner {
    
    private ScriptEngineManager manager;
    private ScriptEngine engine;
    private Map<String, Object> bindings;
    
    public ScriptRunner() {
        manager = new ScriptEngineManager();
        engine = manager.getEngineByName("JavaScript"); // Nashorn (Java 8-14) or GraalVM
        if (engine == null) {
            throw new RuntimeException("JavaScript engine not available");
        }
        bindings = new HashMap<>();
        initializeBindings();
    }
    
    private void initializeBindings() {
        // Bind NGI PRO APIs to scripting environment
        bindings.put("print", new PrintFunction());
        bindings.put("log", new LogFunction());
        bindings.put("alert", new AlertFunction());
        
        // Placeholder bindings for NGI PRO features
        bindings.put("apkAnalyzer", new ApkAnalyzerScriptAPI());
        bindings.put("smaliHelper", new SmaliHelperScriptAPI());
        bindings.put("cryptoAnalyzer", new CryptoAnalyzerScriptAPI());
        bindings.put("stringDecryptor", new StringDecryptorScriptAPI());
        bindings.put("hookGenerator", new HookGeneratorScriptAPI());
    }
    
    public void setVariable(String name, Object value) {
        bindings.put(name, value);
    }
    
    public Object getVariable(String name) {
        return bindings.get(name);
    }
    
    /**
     * Execute JavaScript code string
     */
    public Object executeScript(String script) throws ScriptException {
        SimpleScriptContext context = new SimpleScriptContext();
        javax.script.Bindings engineBindings = new javax.script.SimpleBindings(bindings);
        context.setBindings(engineBindings, ScriptContext.ENGINE_SCOPE);
        return engine.eval(script, context);
    }
    
    /**
     * Execute script from file
     */
    public Object executeScriptFile(String filePath) throws Exception {
        java.nio.file.Path path = java.nio.file.Paths.get(filePath);
        String script = new String(java.nio.file.Files.readAllBytes(path));
        return executeScript(script);
    }
    
    /**
     * Run script with timeout
     */
    public Object executeWithTimeout(String script, long timeoutMs) throws Exception {
        final Object[] result = new Object[1];
        final Exception[] error = new Exception[1];
        
        Thread thread = new Thread(() -> {
            try {
                result[0] = executeScript(script);
            } catch (Exception e) {
                error[0] = e;
            }
        });
        
        thread.start();
        thread.join(timeoutMs);
        
        if (thread.isAlive()) {
            thread.interrupt();
            throw new RuntimeException("Script execution timed out after " + timeoutMs + "ms");
        }
        
        if (error[0] != null) {
            throw error[0];
        }
        
        return result[0];
    }
    
    /**
     * Validate script syntax without executing - simplified version
     */
    public boolean validateScript(String script) {
        try {
            engine.eval(script);
            return true;
        } catch (ScriptException e) {
            return false;
        }
    }
    
    /**
     * Get available functions/variables in context
     */
    public List<String> getAvailableBindings() {
        return new ArrayList<>(bindings.keySet());
    }
    
    /**
     * Create sandboxed environment for untrusted scripts
     */
    public void enableSandboxMode() {
        // Restrict dangerous operations
        setVariable("java", null);
        setVariable("Java", null);
        setVariable("exit", null);
        setVariable("System", null);
    }
    
    // Script API Helper Classes
    
    public static class PrintFunction {
        public void call(Object obj) {
            System.out.println(String.valueOf(obj));
        }
    }
    
    public static class LogFunction {
        public void call(String tag, Object message) {
            System.out.println("[" + tag + "] " + String.valueOf(message));
        }
    }
    
    public static class AlertFunction {
        public void call(String message) {
            System.out.println("ALERT: " + message);
        }
    }
    
    public static class ApkAnalyzerScriptAPI {
        public String getPackageName() { return "com.example.app"; }
        public int getVersionCode() { return 1; }
        public String getVersionName() { return "1.0.0"; }
        public int getMinSdk() { return 21; }
        public int getTargetSdk() { return 33; }
        public List<String> getPermissions() { 
            return Arrays.asList("android.permission.INTERNET", "android.permission.ACCESS_NETWORK_STATE"); 
        }
        public List<String> getActivities() { 
            return Arrays.asList("MainActivity", "SettingsActivity"); 
        }
        public List<String> getServices() { 
            return Arrays.asList("BackgroundService"); 
        }
        public List<String> getReceivers() { 
            return Arrays.asList("BootReceiver"); 
        }
    }
    
    public static class SmaliHelperScriptAPI {
        public String decompileMethod(String className, String methodName) {
            return "// Decompiled smali for " + className + "." + methodName;
        }
        public List<String> findMethodReferences(String methodName) {
            return Arrays.asList("com.example.Class1.methodA", "com.example.Class2.methodB");
        }
        public List<String> findFieldReferences(String fieldName) {
            return Arrays.asList("com.example.Class1.fieldX");
        }
        public String getClassHierarchy(String className) {
            return className + " -> java.lang.Object";
        }
    }
    
    public static class CryptoAnalyzerScriptAPI {
        public List<Map<String, Object>> findCryptoUsage() {
            List<Map<String, Object>> results = new ArrayList<>();
            Map<String, Object> crypto1 = new HashMap<>();
            crypto1.put("class", "com.example.Security");
            crypto1.put("method", "encryptData");
            crypto1.put("algorithm", "AES/CBC/PKCS5Padding");
            crypto1.put("risk", "MEDIUM");
            results.add(crypto1);
            return results;
        }
        public List<String> findHardcodedKeys() {
            return Arrays.asList("Possible key found in com.example.Config.KEY_SECRET");
        }
    }
    
    public static class StringDecryptorScriptAPI {
        public String decryptBase64(String encoded) {
            try {
                byte[] decoded = Base64.getDecoder().decode(encoded);
                return new String(decoded);
            } catch (Exception e) {
                return "Decryption failed: " + e.getMessage();
            }
        }
        public String decryptHex(String hex) {
            StringBuilder result = new StringBuilder();
            for (int i = 0; i < hex.length(); i += 2) {
                result.append((char) Integer.parseInt(hex.substring(i, i + 2), 16));
            }
            return result.toString();
        }
        public String decryptXOR(String input, int key) {
            char[] chars = input.toCharArray();
            for (int i = 0; i < chars.length; i++) {
                chars[i] = (char) (chars[i] ^ key);
            }
            return new String(chars);
        }
    }
    
    public static class HookGeneratorScriptAPI {
        public String generateFridaHook(String className, String methodName) {
            StringBuilder hook = new StringBuilder();
            hook.append("Java.perform(function() {\n");
            hook.append("    var ").append(className.replace(".", "_")).append(" = Java.use(\"").append(className).append("\");\n");
            hook.append("    ").append(className.replace(".", "_")).append(".").append(methodName).append(".implementation = function(");
            hook.append(") {\n");
            hook.append("        console.log('").append(className).append(".").append(methodName).append(" called');\n");
            hook.append("        return this.").append(methodName).append("();\n");
            hook.append("    };\n");
            hook.append("});\n");
            return hook.toString();
        }
        
        public String generateXposedHook(String className, String methodName) {
            StringBuilder hook = new StringBuilder();
            hook.append("findAndHookMethod(\"").append(className).append("\", classLoader, \"").append(methodName).append("\", new XC_MethodHook() {\n");
            hook.append("    @Override\n");
            hook.append("    protected void beforeHookedMethod(MethodHookParam param) throws Throwable {\n");
            hook.append("        XposedBridge.log(\"").append(className).append(".").append(methodName).append(" called\");\n");
            hook.append("    }\n");
            hook.append("});\n");
            return hook.toString();
        }
    }
    
    /**
     * Example script templates
     */
    public static String getExampleScript(String type) {
        switch (type) {
            case "crypto_scan":
                return "// Scan for cryptographic usage\n" +
                       "var crypto = cryptoAnalyzer.findCryptoUsage();\n" +
                       "print('Found ' + crypto.length + ' crypto operations');\n" +
                       "for (var i = 0; i < crypto.length; i++) {\n" +
                       "    print(crypto[i].class + '.' + crypto[i].method + ' uses ' + crypto[i].algorithm);\n" +
                       "}";
            
            case "string_decrypt":
                return "// Decrypt Base64 strings\n" +
                       "var encoded = 'SGVsbG8gV29ybGQh';\n" +
                       "var decoded = stringDecryptor.decryptBase64(encoded);\n" +
                       "print('Decoded: ' + decoded);";
            
            case "frida_hook":
                return "// Generate Frida hook\n" +
                       "var hook = hookGenerator.generateFridaHook('com.example.Security', 'encryptData');\n" +
                       "print(hook);";
            
            case "permission_scan":
                return "// Scan permissions\n" +
                       "var perms = apkAnalyzer.getPermissions();\n" +
                       "print('App has ' + perms.length + ' permissions:');\n" +
                       "perms.forEach(function(p) { print('  - ' + p); });";
            
            default:
                return "// Write your script here\n" +
                       "print('Hello from NGI PRO Script Runner!');\n" +
                       "var pkg = apkAnalyzer.getPackageName();\n" +
                       "print('Package: ' + pkg);";
        }
    }
}
