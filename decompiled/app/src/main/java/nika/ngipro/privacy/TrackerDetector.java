package nika.ngipro.privacy;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * TrackerDetector - Identifies analytics, advertising, and tracking SDKs in APK files.
 * Features:
 * - Detects common tracking libraries (Firebase, Facebook, AdMob, etc.)
 * - Privacy compliance checking (GDPR, CCPA)
 * - Risk assessment for user privacy
 */
public class TrackerDetector {
    
    private static final Map<String, String[]> TRACKER_SIGNATURES = new HashMap<>();
    
    static {
        // Analytics Trackers
        TRACKER_SIGNATURES.put("Firebase Analytics", new String[]{
            "com.google.firebase.analytics", "com.google.android.gms.measurement"
        });
        TRACKER_SIGNATURES.put("Google Analytics", new String[]{
            "com.google.android.gms.analytics", "com.google.analytics"
        });
        TRACKER_SIGNATURES.put("Facebook Analytics", new String[]{
            "com.facebook.appevents", "com.facebook.analytics"
        });
        TRACKER_SIGNATURES.put("Flurry Analytics", new String[]{
            "com.flurry.android", "com.flurry.analytics"
        });
        TRACKER_SIGNATURES.put("Mixpanel", new String[]{
            "com.mixpanel.android", "mixpanel"
        });
        TRACKER_SIGNATURES.put("Amplitude", new String[]{
            "com.amplitude.api", "amplitude"
        });
        
        // Advertising Networks
        TRACKER_SIGNATURES.put("AdMob", new String[]{
            "com.google.android.gms.ads", "com.google.admob"
        });
        TRACKER_SIGNATURES.put("Facebook Ads", new String[]{
            "com.facebook.ads", "com.facebook AudienceNetwork"
        });
        TRACKER_SIGNATURES.put("Unity Ads", new String[]{
            "com.unity3d.ads", "unity3d.ads"
        });
        TRACKER_SIGNATURES.put("AppLovin", new String[]{
            "com.applovin", "applovin"
        });
        TRACKER_SIGNATURES.put("IronSource", new String[]{
            "com.ironsource", "ironsource"
        });
        TRACKER_SIGNATURES.put("Vungle", new String[]{
            "com.vungle", "vungle"
        });
        
        // Crash Reporting
        TRACKER_SIGNATURES.put("Firebase Crashlytics", new String[]{
            "com.google.firebase.crashlytics", "com.crashlytics"
        });
        TRACKER_SIGNATURES.put("Bugsnag", new String[]{
            "com.bugsnag", "bugsnag"
        });
        TRACKER_SIGNATURES.put("Sentry", new String[]{
            "io.sentry", "sentry"
        });
        
        // Attribution & Marketing
        TRACKER_SIGNATURES.put("AppsFlyer", new String[]{
            "com.appsflyer", "appsflyer"
        });
        TRACKER_SIGNATURES.put("Adjust", new String[]{
            "com.adjust", "adjust"
        });
        TRACKER_SIGNATURES.put("Branch Metrics", new String[]{
            "io.branch", "branch"
        });
        
        // A/B Testing
        TRACKER_SIGNATURES.put("Optimizely", new String[]{
            "com.optimizely", "optimizely"
        });
        TRACKER_SIGNATURES.put("Leanplum", new String[]{
            "com.leanplum", "leanplum"
        });
    }
    
    public static class TrackerInfo {
        public String name;
        public String category; // ANALYTICS, ADVERTISING, CRASH_REPORTING, ATTRIBUTION, AB_TESTING
        public List<String> detectedClasses = new ArrayList<>();
        public int riskLevel; // 1-5 scale
        public String privacyConcern;
        
        public TrackerInfo(String name, String category) {
            this.name = name;
            this.category = category;
            this.riskLevel = calculateRisk(category);
            this.privacyConcern = getPrivacyConcern(category);
        }
        
        private int calculateRisk(String category) {
            switch (category) {
                case "ADVERTISING": return 5;
                case "ATTRIBUTION": return 4;
                case "ANALYTICS": return 3;
                case "AB_TESTING": return 2;
                case "CRASH_REPORTING": return 2;
                default: return 3;
            }
        }
        
        private String getPrivacyConcern(String category) {
            switch (category) {
                case "ADVERTISING": 
                    return "May collect device identifiers, browsing behavior, and personal data for targeted advertising";
                case "ATTRIBUTION": 
                    return "Tracks app installs and user actions across apps and websites";
                case "ANALYTICS": 
                    return "Collects usage data, events, and potentially personal information";
                case "AB_TESTING": 
                    return "May track user behavior to determine test group effectiveness";
                case "CRASH_REPORTING": 
                    return "Collects crash logs which may contain sensitive data";
                default: 
                    return "Unknown privacy implications";
            }
        }
        
        @Override
        public String toString() {
            return String.format("[%s] %s (Risk: %d/5) - %s", 
                category, name, riskLevel, privacyConcern);
        }
    }
    
    public static class PrivacyReport {
        public int totalTrackersFound;
        public List<TrackerInfo> trackers = new ArrayList<>();
        public Map<String, Integer> categoryCounts = new HashMap<>();
        public int overallPrivacyRisk; // 1-5 scale
        public List<String> gdprConcerns = new ArrayList<>();
        public List<String> ccpaConcerns = new ArrayList<>();
        public List<String> recommendations = new ArrayList<>();
        
        @Override
        public String toString() {
            StringBuilder sb = new StringBuilder();
            sb.append("=== PRIVACY & TRACKER DETECTION REPORT ===\n\n");
            sb.append("Total Trackers Found: ").append(totalTrackersFound).append("\n");
            sb.append("Overall Privacy Risk: ").append(overallPrivacyRisk).append("/5\n\n");
            
            if (!categoryCounts.isEmpty()) {
                sb.append("Trackers by Category:\n");
                for (Map.Entry<String, Integer> entry : categoryCounts.entrySet()) {
                    sb.append("  • ").append(entry.getKey()).append(": ").append(entry.getValue()).append("\n");
                }
                sb.append("\n");
            }
            
            sb.append("Detected Trackers:\n");
            for (TrackerInfo tracker : trackers) {
                sb.append("  ").append(tracker.toString()).append("\n");
                if (!tracker.detectedClasses.isEmpty()) {
                    sb.append("    Classes: ").append(String.join(", ", tracker.detectedClasses)).append("\n");
                }
            }
            sb.append("\n");
            
            if (!gdprConcerns.isEmpty()) {
                sb.append("GDPR Concerns:\n");
                for (String concern : gdprConcerns) {
                    sb.append("  ⚠ ").append(concern).append("\n");
                }
                sb.append("\n");
            }
            
            if (!ccpaConcerns.isEmpty()) {
                sb.append("CCPA Concerns:\n");
                for (String concern : ccpaConcerns) {
                    sb.append("  ⚠ ").append(concern).append("\n");
                }
                sb.append("\n");
            }
            
            if (!recommendations.isEmpty()) {
                sb.append("Recommendations:\n");
                for (String rec : recommendations) {
                    sb.append("  ✓ ").append(rec).append("\n");
                }
            }
            
            return sb.toString();
        }
    }
    
    /**
     * Analyze code/package list for trackers
     */
    public static PrivacyReport analyzePackages(List<String> packages) {
        PrivacyReport report = new PrivacyReport();
        
        for (String pkg : packages) {
            for (Map.Entry<String, String[]> entry : TRACKER_SIGNATURES.entrySet()) {
                for (String signature : entry.getValue()) {
                    if (pkg.contains(signature)) {
                        String trackerName = entry.getKey();
                        String category = getCategoryForTracker(trackerName);
                        
                        TrackerInfo tracker = null;
                        for (TrackerInfo t : report.trackers) {
                            if (t.name.equals(trackerName)) {
                                tracker = t;
                                break;
                            }
                        }
                        
                        if (tracker == null) {
                            tracker = new TrackerInfo(trackerName, category);
                            report.trackers.add(tracker);
                            report.totalTrackersFound++;
                            
                            report.categoryCounts.put(category, 
                                report.categoryCounts.getOrDefault(category, 0) + 1);
                        }
                        
                        if (!tracker.detectedClasses.contains(pkg)) {
                            tracker.detectedClasses.add(pkg);
                        }
                        
                        break;
                    }
                }
            }
        }
        
        calculateOverallRisk(report);
        generateComplianceConcerns(report);
        generateRecommendations(report);
        
        return report;
    }
    
    private static String getCategoryForTracker(String trackerName) {
        if (trackerName.contains("Ads") || trackerName.contains("AdMob") || 
            trackerName.contains("AppLovin") || trackerName.contains("IronSource") ||
            trackerName.contains("Vungle") || trackerName.contains("Unity")) {
            return "ADVERTISING";
        } else if (trackerName.contains("Analytics") || trackerName.contains("Mixpanel") ||
                   trackerName.contains("Amplitude") || trackerName.contains("Flurry")) {
            return "ANALYTICS";
        } else if (trackerName.contains("Crash") || trackerName.contains("Bugsnag") ||
                   trackerName.contains("Sentry")) {
            return "CRASH_REPORTING";
        } else if (trackerName.contains("AppsFlyer") || trackerName.contains("Adjust") ||
                   trackerName.contains("Branch")) {
            return "ATTRIBUTION";
        } else if (trackerName.contains("Optimizely") || trackerName.contains("Leanplum")) {
            return "AB_TESTING";
        }
        return "ANALYTICS";
    }
    
    private static void calculateOverallRisk(PrivacyReport report) {
        if (report.trackers.isEmpty()) {
            report.overallPrivacyRisk = 1;
            return;
        }
        
        int totalRisk = 0;
        for (TrackerInfo tracker : report.trackers) {
            totalRisk += tracker.riskLevel;
        }
        
        double avgRisk = (double) totalRisk / report.trackers.size();
        
        // Adjust based on number of trackers
        if (report.totalTrackersFound > 5) avgRisk += 1;
        if (report.totalTrackersFound > 10) avgRisk += 1;
        
        report.overallPrivacyRisk = Math.min(5, Math.max(1, (int) Math.round(avgRisk)));
    }
    
    private static void generateComplianceConcerns(PrivacyReport report) {
        boolean hasAdvertising = false;
        boolean hasAnalytics = false;
        boolean hasAttribution = false;
        
        for (TrackerInfo tracker : report.trackers) {
            if (tracker.category.equals("ADVERTISING")) hasAdvertising = true;
            if (tracker.category.equals("ANALYTICS")) hasAnalytics = true;
            if (tracker.category.equals("ATTRIBUTION")) hasAttribution = true;
        }
        
        if (hasAdvertising) {
            report.gdprConcerns.add("Advertising trackers require explicit user consent under GDPR Article 6");
            report.gdprConcerns.add("Device identifiers used for advertising may be considered personal data");
            report.ccpaConcerns.add("Users have right to opt-out of sale of personal information for advertising");
        }
        
        if (hasAnalytics) {
            report.gdprConcerns.add("Analytics data collection requires lawful basis under GDPR");
            report.gdprConcerns.add("Consider implementing data minimization for analytics events");
        }
        
        if (hasAttribution) {
            report.gdprConcerns.add("Cross-app tracking for attribution requires explicit consent");
            report.ccpaConcerns.add("Attribution tracking may constitute 'sale' under CCPA");
        }
        
        if (report.totalTrackersFound > 3) {
            report.gdprConcerns.add("Multiple trackers increase complexity of consent management");
        }
    }
    
    private static void generateRecommendations(PrivacyReport report) {
        if (report.overallPrivacyRisk >= 4) {
            report.recommendations.add("HIGH RISK: Consider reducing number of third-party trackers");
        }
        
        for (TrackerInfo tracker : report.trackers) {
            if (tracker.riskLevel >= 4) {
                report.recommendations.add("Review necessity of " + tracker.name + " tracker");
            }
        }
        
        report.recommendations.add("Implement a consent management platform (CMP) for GDPR compliance");
        report.recommendations.add("Provide clear privacy policy disclosure about data collection");
        report.recommendations.add("Allow users to opt-out of non-essential tracking");
        report.recommendations.add("Regular audit of tracker configurations and data flows");
        
        if (report.trackers.isEmpty()) {
            report.recommendations.add("No third-party trackers detected - excellent privacy posture!");
        }
    }

    /**
     * Detect trackers from APK file path - wrapper for test compatibility
     */
    public PrivacyReport detectTrackers(String apkPath) {
        // In real implementation, this would extract packages from the APK
        // For now, return an empty report as placeholder
        PrivacyReport report = new PrivacyReport();
        report.overallPrivacyRisk = 1;
        report.recommendations.add("APK analysis requires file extraction - use analyzePackages() with extracted class list");
        return report;
    }
    
    /**
     * Detect trackers from list of classes - wrapper for test compatibility
     */
    public PrivacyReport detectTrackers(List<String> classes) {
        return analyzePackages(classes);
    }
}
