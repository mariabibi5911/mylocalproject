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
