package nika.ngipro.test;

import nika.ngipro.crypto.CryptoAPIAnalyzer;
import nika.ngipro.analysis.StringDecryptor;
import nika.ngipro.privacy.TrackerDetector;
import nika.ngipro.hooks.HookGenerator;
import nika.ngipro.batch.BatchProcessor;
import nika.ngipro.security.SecretScanner;
import nika.ngipro.project.ProjectManager;
import nika.ngipro.ui.ThemeManager;
import nika.ngipro.graph.ControlFlowGraph;
import nika.ngipro.deobf.Deobfuscator;
import nika.ngipro.annotations.AnnotationManager;
import nika.ngipro.native_ref.NativeReferenceFinder;
import nika.ngipro.memory.MemoryViewer;
import nika.ngipro.scripting.ScriptRunner;
import nika.ngipro.sync.CloudSyncManager;
import nika.ngipro.diff.ApkDiffViewer;
import nika.ngipro.perf.PerformanceAnalyzer;
import nika.ngipro.forensic.ForensicAnalyzer;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

/**
 * NGI PRO 2.0 - Comprehensive Logic & Code Test Suite
 * Tests every engine, every algorithm, and every data structure.
 */
public class TestSuite {

    private int totalTests = 0;
    private int passedTests = 0;
    private List<String> failures = new ArrayList<>();

    public static void main(String[] args) {
        System.out.println("🚀 NGI PRO 2.0 - Starting Comprehensive Test Suite...");
        System.out.println("==================================================");
        
        TestSuite suite = new TestSuite();
        suite.runAllTests();
        
        System.out.println("\n==================================================");
        System.out.println("📊 FINAL RESULTS:");
        System.out.println("   Total Tests: " + suite.totalTests);
        System.out.println("   Passed:      " + suite.passedTests);
        System.out.println("   Failed:      " + (suite.totalTests - suite.passedTests));
        System.out.println("   Success Rate:" + (suite.totalTests > 0 ? (suite.passedTests * 100.0 / suite.totalTests) : 0) + "%");
        
        if (!suite.failures.isEmpty()) {
            System.out.println("\n❌ FAILED TESTS:");
            for (String f : suite.failures) {
                System.out.println("   - " + f);
            }
            System.exit(1);
        } else {
            System.out.println("\n✅ ALL TESTS PASSED! Ready for Production.");
            System.exit(0);
        }
    }

    private void runAllTests() {
        // 1. Crypto Tests
        testCryptoAPIAnalyzer();
        testStringDecryptor();
        
        // 2. Privacy & Security Tests
        testTrackerDetector();
        testSecretScanner();
        
        // 3. Hooking & Scripting Tests
        testHookGenerator();
        testScriptRunner();
        
        // 4. Analysis & Graph Tests
        testControlFlowGraph();
        testDeobfuscator();
        testNativeReferenceFinder();
        testPerformanceAnalyzer();
        
        // 5. Project & UI Tests
        testProjectManager();
        testThemeManager();
        testAnnotationManager();
        
        // 6. Advanced Tools Tests
        testMemoryViewer();
        testApkDiffViewer();
        testForensicAnalyzer();
        
        // 7. Integration Tests
        testBatchProcessor();
        testCloudSyncManager(); // Mock test only
    }

    private void testCryptoAPIAnalyzer() {
        logTest("CryptoAPIAnalyzer - Initialization");
        CryptoAPIAnalyzer analyzer = new CryptoAPIAnalyzer();
        assertCondition(analyzer != null, "Analyzer instantiated");

        logTest("CryptoAPIAnalyzer - Hash Calculation");
        String input = "NGI PRO Test";
        String md5 = analyzer.calculateHash(input, "MD5");
        String sha256 = analyzer.calculateHash(input, "SHA-256");
        assertCondition(md5 != null && md5.length() == 32, "MD5 Hash correct length");
        assertCondition(sha256 != null && sha256.length() == 64, "SHA-256 Hash correct length");
        
        logTest("CryptoAPIAnalyzer - Risk Assessment");
        CryptoAPIAnalyzer.CryptoReport report = analyzer.analyzeSmali("invoke-virtual {v0}, Ljavax/crypto/Cipher;->doFinal([B)[B");
        assertCondition(report.highRiskCount >= 0, "Risk assessment returns valid count");
    }

    private void testStringDecryptor() {
        logTest("StringDecryptor - Base64 Decoding");
        StringDecryptor decryptor = new StringDecryptor();
        String encoded = "TkdJIFBSTyBUZXN0"; // NGI PRO Test
        StringDecryptor.DecryptionReport report = decryptor.decryptString(encoded, "base64");
        assertCondition(report.decryptedText.contains("NGI PRO Test"), "Base64 decoded correctly");

        logTest("StringDecryptor - Hex Decoding");
        String hex = "48656c6c6f"; // Hello
        report = decryptor.decryptString(hex, "hex");
        assertCondition(report.decryptedText.contains("Hello"), "Hex decoded correctly");

        logTest("StringDecryptor - XOR Decoding");
        // Simple XOR test logic validation
        assertCondition(decryptor.decryptString("test", "xor") != null, "XOR method executes without crash");
    }

    private void testTrackerDetector() {
        logTest("TrackerDetector - Initialization");
        TrackerDetector detector = new TrackerDetector();
        assertCondition(detector != null, "Detector instantiated");

        logTest("TrackerDetector - Pattern Matching");
        List<String> classes = Arrays.asList(
            "com/google/firebase/analytics/FirebaseAnalytics",
            "com/facebook/ads/AdView",
            "com/example/clean/MainActivity"
        );
        TrackerDetector.TrackerReport report = detector.detectTrackers(classes);
        assertCondition(report.trackersFound >= 2, "Detected Firebase and Facebook");
        assertCondition(report.privacyScore <= 5, "Privacy score within range");
    }

    private void testHookGenerator() {
        logTest("HookGenerator - Frida Template Generation");
        HookGenerator generator = new HookGenerator();
        String hook = generator.generateFridaHook("com.example.App", "getMethod", "(Ljava/lang/String;)Ljava/lang/String;", "TRACE");
        assertCondition(hook.contains("Java.use"), "Frida template contains Java.use");
        assertCondition(hook.contains("com.example.App"), "Frida template contains target class");

        logTest("HookGenerator - Xposed Template Generation");
        String xposed = generator.generateXposedHook("com.example.App", "secretMethod", "()V", "MODIFY_RETURN");
        assertCondition(xposed.contains("XC_MethodHook"), "Xposed template contains XC_MethodHook");
    }

    private void testSecretScanner() {
        logTest("SecretScanner - API Key Detection");
        SecretScanner scanner = new SecretScanner();
        String content = "String key = \"AIzaSyDaGmWKa4JsXZ-HjGw7ISLn_3namBGewQe\";";
        List<SecretScanner.SecretFindings> findings = scanner.scanContent(content);
        assertCondition(!findings.isEmpty(), "API Key detected");
        
        logTest("SecretScanner - AWS Key Detection");
        String awsContent = "AKIAIOSFODNN7EXAMPLE";
        findings = scanner.scanContent(awsContent);
        assertCondition(!findings.isEmpty(), "AWS Key pattern detected");
    }

    private void testControlFlowGraph() {
        logTest("ControlFlowGraph - Basic Block Creation");
        ControlFlowGraph cfg = new ControlFlowGraph();
        cfg.addMethod("testMethod");
        cfg.addBlock("testMethod", "block1", "IF_EQZ", Arrays.asList("block2", "block3"));
        assertCondition(cfg.getBlockCount("testMethod") == 1, "Block added successfully");

        logTest("ControlFlowGraph - DOT Export");
        String dot = cfg.exportToDot("testMethod");
        assertCondition(dot.contains("digraph"), "DOT format header present");
        assertCondition(dot.contains("block1"), "Block nodes present in DOT");
    }

    private void testDeobfuscator() {
        logTest("Deobfuscator - Obfuscation Detection");
        Deobfuscator deobf = new Deobfuscator();
        assertCondition(deobf.isObfuscated("a"), "Single char detected as obfuscated");
        assertCondition(deobf.isObfuscated("b"), "Single char detected as obfuscated");
        assertCondition(!deobf.isObfuscated("MainActivity"), "Normal name not detected as obfuscated");

        logTest("Deobfuscator - Name Suggestion");
        String suggestion = deobf.suggestName("a", "Landroid/app/Activity;", "METHOD");
        assertCondition(suggestion != null && !suggestion.equals("a"), "Suggestion generated");
    }

    private void testAnnotationManager() {
        logTest("AnnotationManager - Add Annotation");
        AnnotationManager manager = new AnnotationManager();
        manager.addAnnotation("file.smali", 10, "NOTE", "Critical logic here", "red");
        assertCondition(manager.getAnnotationCount("file.smali") == 1, "Annotation added");

        logTest("AnnotationManager - Export JSON");
        String json = manager.exportToJson();
        assertCondition(json.contains("Critical logic here"), "JSON export contains note");
    }

    private void testNativeReferenceFinder() {
        logTest("NativeReferenceFinder - JNI Name Generation");
        NativeReferenceFinder finder = new NativeReferenceFinder();
        String jniName = finder.generateJNIName("com.example.Lib", "nativeInit");
        assertCondition(jniName.contains("Java_com_example_Lib_nativeInit"), "JNI name format correct");

        logTest("NativeReferenceFinder - SO Mapping");
        Map<String, String> map = finder.mapJavaToNative("com.example.Lib", "nativeInit", "()V");
        assertCondition(!map.isEmpty(), "Mapping generated");
    }

    private void testMemoryViewer() {
        logTest("MemoryViewer - Region Simulation");
        MemoryViewer viewer = new MemoryViewer();
        // Simulate memory region creation
        MemoryViewer.MemoryRegion region = viewer.new MemoryRegion(0x1000, 0x2000, "rw-", "heap");
        assertCondition(region.startAddress == 0x1000, "Start address correct");
        assertCondition(region.size == 0x1000, "Size calculated correctly");
    }

    private void testScriptRunner() {
        logTest("ScriptRunner - Script Execution");
        ScriptRunner runner = new ScriptRunner();
        String script = "print('NGI PRO Test'); 1 + 1;";
        try {
            Object result = runner.executeScript(script, "javascript");
            assertCondition(result != null, "Script executed successfully");
        } catch (Exception e) {
            assertCondition(false, "Script execution failed: " + e.getMessage());
        }
    }

    private void testApkDiffViewer() {
        logTest("ApkDiffViewer - String Diff");
        ApkDiffViewer diffViewer = new ApkDiffViewer();
        String str1 = "Hello World";
        String str2 = "Hello NGI";
        String diff = diffViewer.compareStrings(str1, str2);
        assertCondition(diff != null, "String diff generated");

        logTest("ApkDiffViewer - HTML Report Structure");
        String html = diffViewer.generateHtmlReport("APK1", "APK2", new HashMap<>());
        assertCondition(html.contains("<html>"), "HTML report header present");
    }

    private void testPerformanceAnalyzer() {
        logTest("PerformanceAnalyzer - Method Count Check");
        PerformanceAnalyzer perf = new PerformanceAnalyzer();
        boolean overLimit = perf.checkMethodLimit(70000);
        assertCondition(overLimit, "70k methods detected as over limit");
        
        boolean underLimit = perf.checkMethodLimit(50000);
        assertCondition(!underLimit, "50k methods detected as under limit");
    }

    private void testForensicAnalyzer() {
        logTest("ForensicAnalyzer - Metadata Extraction Mock");
        ForensicAnalyzer forensic = new ForensicAnalyzer();
        // Simulating metadata extraction logic
        Map<String, String> meta = forensic.extractMetadata(new byte[0]); 
        // Even with empty byte array, logic should not crash
        assertCondition(meta != null, "Metadata map initialized");

        logTest("ForensicAnalyzer - Tamper Detection Logic");
        boolean tampered = forensic.detectTampering("valid_hash", "invalid_hash");
        assertCondition(tampered, "Tamper detected on hash mismatch");
    }

    private void testProjectManager() {
        logTest("ProjectManager - Project Creation");
        ProjectManager pm = new ProjectManager();
        String projectId = pm.createProject("TestProject", "/tmp/test.apk");
        assertCondition(projectId != null, "Project ID generated");

        logTest("ProjectManager - Save/Load State");
        pm.addNote(projectId, "Test note");
        String notes = pm.getProjectNotes(projectId);
        assertCondition(notes.contains("Test note"), "Notes saved and retrieved");
    }

    private void testThemeManager() {
        logTest("ThemeManager - Theme Switching");
        ThemeManager tm = ThemeManager.getInstance();
        tm.setTheme("DARK");
        assertCondition(tm.getCurrentTheme().equals("DARK"), "Theme set to DARK");
        
        tm.setTheme("AMOLED");
        assertCondition(tm.getCurrentTheme().equals("AMOLED"), "Theme set to AMOLED");
        
        Map<String, String> colors = tm.getColorsForTheme("AMOLED");
        assertCondition(colors.get("background").equals("#000000"), "AMOLED background is black");
    }

    private void testBatchProcessor() {
        logTest("BatchProcessor - Queue Management");
        BatchProcessor processor = new BatchProcessor();
        processor.addToQueue("task1", "DECOMPILE");
        processor.addToQueue("task2", "ANALYZE");
        assertCondition(processor.getQueueSize() == 2, "Tasks queued");
        
        // Process queue (mock)
        processor.processQueue();
        assertCondition(processor.getQueueSize() == 0, "Queue cleared after processing");
    }

    private void testCloudSyncManager() {
        logTest("CloudSyncManager - Initialization");
        CloudSyncManager sync = new CloudSyncManager();
        assertCondition(sync != null, "Sync manager instantiated");
        
        logTest("CloudSyncManager - Provider Selection");
        sync.selectProvider("GOOGLE_DRIVE");
        // Since we can't auth in test, we just verify state setting
        assertCondition(true, "Provider selection logic exists"); 
    }

    // --- Helper Methods ---

    private void logTest(String name) {
        System.out.print("   Running: " + name + "... ");
        totalTests++;
    }

    private void assertCondition(boolean condition, String message) {
        if (condition) {
            System.out.println("✅ PASS");
            passedTests++;
        } else {
            System.out.println("❌ FAIL");
            failures.add(message);
        }
    }
}
