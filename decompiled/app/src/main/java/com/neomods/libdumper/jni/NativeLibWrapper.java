package com.neomods.libdumper.jni;

import androidx.core.os.EnvironmentCompat;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/* loaded from: classes6.dex */
public class NativeLibWrapper {

    /* loaded from: classes6.dex */
    public static class DumpConfig {
        public boolean extractSymtab = true;
        public boolean extractDynsym = true;
        public boolean extractExported = true;
        public boolean extractImported = true;
        public boolean dumpRawNames = true;
        public boolean generateCppReconstruction = true;
        public boolean groupMethodsIntoClasses = true;
        public boolean groupStaticMethods = true;
        public boolean detectConstructors = true;
        public boolean detectDestructors = true;
        public boolean detectOverloadedMethods = true;
        public boolean detectNamespaces = true;
        public boolean generateComments = true;
        public boolean includeMethodSignatures = true;
        public boolean includeReturnTypes = true;
        public boolean includeParameterTypes = true;
        public boolean attemptInheritanceDetection = true;
        public boolean includeVirtualAddresses = true;
        public boolean includeRva = true;
        public boolean includeFileOffsets = true;
        public boolean includeSymbolSizes = true;
        public boolean includeSectionNames = true;
        public boolean generateDumpCpp = true;
        public boolean generateSymbolTable = true;
        public boolean generateCredits = true;
        public boolean generateDumpInfo = true;
        public boolean generateJson = true;
    }

    /* loaded from: classes6.dex */
    public static class DumpStats {
        public boolean dumpCppWritten;
        public boolean jsonWritten;
        public boolean symbolTableWritten;
        public int totalClasses;
        public int totalNamespaces;
        public int totalSymbols;
    }

    /* loaded from: classes6.dex */
    public static class ElfInfo {
        public String architecture;
        public int bitWidth;
        public String elfType;
        public String endian;
        public long entryPoint;
        public String fileName;
        public String filePath;
        public long fileSize;
        public boolean isSharedLibrary;
        public boolean isValid;
        public String machine;

        static ElfInfo fromJson(String json) {
            try {
                JSONObject o = new JSONObject(json);
                ElfInfo e = new ElfInfo();
                e.fileName = o.optString("file_name");
                e.filePath = o.optString("file_path");
                e.fileSize = o.optLong("file_size");
                e.architecture = o.optString("architecture");
                e.bitWidth = o.optInt("bit_width");
                e.endian = o.optString("endian");
                e.elfType = o.optString("elf_type");
                e.machine = o.optString("machine");
                e.entryPoint = o.optLong("entry_point");
                e.isValid = o.optBoolean("is_valid");
                e.isSharedLibrary = o.optBoolean("is_shared_library");
                return e;
            } catch (JSONException e2) {
                return null;
            }
        }
    }

    /* loaded from: classes6.dex */
    public static class Companion {
        private static String lastClassesJson;
        private static String lastNamespacesJson;
        private static String lastSymbolsJson;
        private static boolean nativeLoaded;

        private static native String nativeDetectNamespaces(String str, String str2, boolean z);

        private static native String nativeExtractSymbols(String str, boolean z, boolean z2, boolean z3, boolean z4, boolean z5, boolean z6, boolean z7, boolean z8, boolean z9, boolean z10, boolean z11, boolean z12, boolean z13, boolean z14, boolean z15, boolean z16, boolean z17, boolean z18, boolean z19, boolean z20, boolean z21, boolean z22, boolean z23, boolean z24, boolean z25, boolean z26, boolean z27);

        private static native String nativeGenerateDumpCpp(String str, String str2, boolean z, boolean z2, boolean z3, boolean z4, boolean z5, boolean z6, boolean z7, boolean z8);

        private static native String nativeGenerateJsonExport(String str, String str2, String str3, String str4);

        private static native String nativeGenerateSymbolTable(String str, boolean z, boolean z2, boolean z3, boolean z4, boolean z5);

        private static native String nativeGetVersion();

        private static native String nativeLoadElf(String str);

        private static native String nativeReconstructClasses(String str, boolean z, boolean z2, boolean z3, boolean z4, boolean z5, boolean z6, boolean z7);

        private static native String nativeRunDump(String str, String str2, boolean z, boolean z2, boolean z3, boolean z4, boolean z5, boolean z6, boolean z7, boolean z8, boolean z9, boolean z10, boolean z11, boolean z12, boolean z13, boolean z14, boolean z15, boolean z16, boolean z17, boolean z18, boolean z19, boolean z20, boolean z21, boolean z22, boolean z23, boolean z24, boolean z25, boolean z26, boolean z27);

        private static native boolean nativeValidateElf(String str);

        static {
            nativeLoaded = false;
            try {
                System.loadLibrary("NeoLibDumper");
                nativeLoaded = true;
            } catch (UnsatisfiedLinkError e) {
                e.printStackTrace();
                nativeLoaded = false;
            }
        }

        public static boolean isNativeAvailable() {
            return nativeLoaded;
        }

        public static ElfInfo loadElf(String path) {
            if (!nativeLoaded) {
                throw new IllegalStateException("Native library not loaded");
            }
            try {
                return ElfInfo.fromJson(nativeLoadElf(path));
            } catch (Throwable e) {
                e.printStackTrace();
                return null;
            }
        }

        public static boolean validateElf(String path) {
            if (!nativeLoaded) {
                return false;
            }
            try {
                return nativeValidateElf(path);
            } catch (Throwable th) {
                return false;
            }
        }

        public static int extractSymbols(String path, DumpConfig c) {
            if (nativeLoaded) {
                try {
                    String json = nativeExtractSymbols(path, c.extractSymtab, c.extractDynsym, c.extractExported, c.extractImported, c.dumpRawNames, c.generateCppReconstruction, c.groupMethodsIntoClasses, c.groupStaticMethods, c.detectConstructors, c.detectDestructors, c.detectOverloadedMethods, c.detectNamespaces, c.generateComments, c.includeMethodSignatures, c.includeReturnTypes, c.includeParameterTypes, c.attemptInheritanceDetection, c.includeVirtualAddresses, c.includeRva, c.includeFileOffsets, c.includeSymbolSizes, c.includeSectionNames, c.generateDumpCpp, c.generateSymbolTable, c.generateCredits, c.generateDumpInfo, c.generateJson);
                    lastSymbolsJson = json;
                    lastClassesJson = null;
                    lastNamespacesJson = null;
                    return countJsonArrayItems(json);
                } catch (Throwable e) {
                    e.printStackTrace();
                    return 0;
                }
            }
            throw new IllegalStateException("Native library not loaded");
        }

        public static int reconstructClasses(DumpConfig c) {
            if (!nativeLoaded) {
                throw new IllegalStateException("Native library not loaded");
            }
            String str = lastSymbolsJson;
            if (str == null) {
                return 0;
            }
            try {
                String json = nativeReconstructClasses(str, c.generateCppReconstruction, c.groupMethodsIntoClasses, c.groupStaticMethods, c.detectConstructors, c.detectDestructors, c.detectOverloadedMethods, c.attemptInheritanceDetection);
                lastClassesJson = json;
                return countJsonArrayItems(json);
            } catch (Throwable e) {
                e.printStackTrace();
                return 0;
            }
        }

        public static int detectNamespaces(DumpConfig c) {
            String str;
            if (!nativeLoaded) {
                throw new IllegalStateException("Native library not loaded");
            }
            String str2 = lastSymbolsJson;
            if (str2 == null || (str = lastClassesJson) == null) {
                return 0;
            }
            try {
                String json = nativeDetectNamespaces(str2, str, c.detectNamespaces);
                lastNamespacesJson = json;
                return countJsonArrayItems(json);
            } catch (Throwable e) {
                e.printStackTrace();
                return 0;
            }
        }

        public static String generateDumpCpp(DumpConfig c) {
            if (!nativeLoaded) {
                throw new IllegalStateException("Native library not loaded");
            }
            String str = lastClassesJson;
            if (str == null) {
                return "// No class data";
            }
            try {
                String str2 = lastNamespacesJson;
                if (str2 == null) {
                    str2 = "[]";
                }
                String namespacesJson = str2;
                return nativeGenerateDumpCpp(str, namespacesJson, c.generateComments, c.includeMethodSignatures, c.includeVirtualAddresses, c.includeRva, c.includeFileOffsets, c.includeSymbolSizes, c.includeSectionNames, c.detectNamespaces);
            } catch (Throwable e) {
                return "// Error generating Dump.cpp: " + e.getMessage();
            }
        }

        public static String generateSymbolTable(DumpConfig c) {
            if (!nativeLoaded) {
                throw new IllegalStateException("Native library not loaded");
            }
            String str = lastSymbolsJson;
            if (str == null) {
                return "// No symbol data";
            }
            try {
                return nativeGenerateSymbolTable(str, c.includeVirtualAddresses, c.includeRva, c.includeFileOffsets, c.includeSymbolSizes, c.includeSectionNames);
            } catch (Throwable e) {
                return "// Error generating SymbolTable.txt: " + e.getMessage();
            }
        }

        public static String getVersion() {
            if (!nativeLoaded) {
                return "unavailable (native not loaded)";
            }
            try {
                return nativeGetVersion();
            } catch (Throwable th) {
                return EnvironmentCompat.MEDIA_UNKNOWN;
            }
        }

        public static DumpStats runDump(String libPath, String outputDir, DumpConfig c) {
            if (nativeLoaded) {
                try {
                    String json = nativeRunDump(libPath, outputDir, c.extractSymtab, c.extractDynsym, c.extractExported, c.extractImported, c.dumpRawNames, c.generateCppReconstruction, c.groupMethodsIntoClasses, c.groupStaticMethods, c.detectConstructors, c.detectDestructors, c.detectOverloadedMethods, c.detectNamespaces, c.generateComments, c.includeMethodSignatures, c.includeReturnTypes, c.includeParameterTypes, c.attemptInheritanceDetection, c.includeVirtualAddresses, c.includeRva, c.includeFileOffsets, c.includeSymbolSizes, c.includeSectionNames, c.generateDumpCpp, c.generateSymbolTable, c.generateCredits, c.generateDumpInfo, c.generateJson);
                    JSONObject o = new JSONObject(json);
                    DumpStats s = new DumpStats();
                    s.totalSymbols = o.optInt("total_symbols");
                    s.totalClasses = o.optInt("total_classes");
                    s.totalNamespaces = o.optInt("total_namespaces");
                    s.dumpCppWritten = o.optBoolean("dump_cpp_written");
                    s.symbolTableWritten = o.optBoolean("symbol_table_written");
                    s.jsonWritten = o.optBoolean("json_written");
                    return s;
                } catch (Throwable e) {
                    e.printStackTrace();
                    return null;
                }
            }
            throw new IllegalStateException("Native library not loaded");
        }

        private static int countJsonArrayItems(String json) {
            try {
                return new JSONArray(json).length();
            } catch (Throwable th) {
                return 0;
            }
        }
    }
}
