# NGI Pro - ProGuard Rules

# Keep new feature activities
-keep class nika.ngipro.SignatureVerifierActivity { *; }
-keep class nika.ngipro.PermissionAnalyzerActivity { *; }
-keep class nika.ngipro.SmaliEditorActivity { *; }
-keep class nika.ngipro.CodeSearchActivity { *; }
-keep class nika.ngipro.ReportGeneratorActivity { *; }
-keep class nika.ngipro.PluginManagerActivity { *; }

# Keep all activity classes
-keep class * extends android.app.Activity
-keep class * extends androidx.appcompat.app.AppCompatActivity

# Keep model classes
-keep class nika.ngipro.mod.** { *; }
-keep class nika.ngipro.auth.** { *; }

# Keep the recovered startup and AndroidX runtime entry points if a future
# release build enables shrinking. Debug is currently non-minified.
-keep class androidx.startup.** { *; }
-keep class androidx.emoji2.** { *; }
-keep class androidx.lifecycle.** { *; }
-keep class com.neomods.libdumper.jni.** { *; }
-keep class com.ngi_pro.core.** { *; }
-keepclasseswithmembernames,includedescriptorclasses class * {
    native <methods>;
}
