package nika.ngipro.ui;

import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.view.View;
import android.widget.Toast;

/**
 * Theme Manager - Dark/Light/AMOLED theme support
 * Handles theme switching and UI customization
 */
public class ThemeManager {
    
    public enum ThemeMode {
        LIGHT,
        DARK,
        AMOLED,
        SYSTEM_DEFAULT
    }
    
    public static class ThemeColors {
        // Primary colors
        public int primaryColor;
        public int primaryDarkColor;
        public int accentColor;
        
        // Background colors
        public int backgroundColor;
        public int surfaceColor;
        public int cardBackgroundColor;
        
        // Text colors
        public int primaryTextColor;
        public int secondaryTextColor;
        public int disabledTextColor;
        
        // Special colors
        public int errorColor;
        public int warningColor;
        public int successColor;
        public int infoColor;
        
        // Divider and border
        public int dividerColor;
        public int borderColor;
        
        // Code editor colors
        public int codeBackgroundColor;
        public int keywordColor;
        public int stringColor;
        public int commentColor;
        public int numberColor;
        public int operatorColor;
    }
    
    private static final ThemeColors LIGHT_THEME = new ThemeColors();
    private static final ThemeColors DARK_THEME = new ThemeColors();
    private static final ThemeColors AMOLED_THEME = new ThemeColors();
    
    static {
        // Light Theme
        LIGHT_THEME.primaryColor = Color.parseColor("#6200EE");
        LIGHT_THEME.primaryDarkColor = Color.parseColor("#3700B3");
        LIGHT_THEME.accentColor = Color.parseColor("#03DAC6");
        LIGHT_THEME.backgroundColor = Color.parseColor("#FFFFFF");
        LIGHT_THEME.surfaceColor = Color.parseColor("#FFFFFF");
        LIGHT_THEME.cardBackgroundColor = Color.parseColor("#FAFAFA");
        LIGHT_THEME.primaryTextColor = Color.parseColor("#000000");
        LIGHT_THEME.secondaryTextColor = Color.parseColor("#666666");
        LIGHT_THEME.disabledTextColor = Color.parseColor("#9E9E9E");
        LIGHT_THEME.errorColor = Color.parseColor("#B00020");
        LIGHT_THEME.warningColor = Color.parseColor("#FF9800");
        LIGHT_THEME.successColor = Color.parseColor("#4CAF50");
        LIGHT_THEME.infoColor = Color.parseColor("#2196F3");
        LIGHT_THEME.dividerColor = Color.parseColor("#E0E0E0");
        LIGHT_THEME.borderColor = Color.parseColor("#BDBDBD");
        LIGHT_THEME.codeBackgroundColor = Color.parseColor("#F5F5F5");
        LIGHT_THEME.keywordColor = Color.parseColor("#0000FF");
        LIGHT_THEME.stringColor = Color.parseColor("#008000");
        LIGHT_THEME.commentColor = Color.parseColor("#808080");
        LIGHT_THEME.numberColor = Color.parseColor("#FF00FF");
        LIGHT_THEME.operatorColor = Color.parseColor("#000000");
        
        // Dark Theme
        DARK_THEME.primaryColor = Color.parseColor("#BB86FC");
        DARK_THEME.primaryDarkColor = Color.parseColor("#3700B3");
        DARK_THEME.accentColor = Color.parseColor("#03DAC6");
        DARK_THEME.backgroundColor = Color.parseColor("#121212");
        DARK_THEME.surfaceColor = Color.parseColor("#1E1E1E");
        DARK_THEME.cardBackgroundColor = Color.parseColor("#2C2C2C");
        DARK_THEME.primaryTextColor = Color.parseColor("#FFFFFF");
        DARK_THEME.secondaryTextColor = Color.parseColor("#B0B0B0");
        DARK_THEME.disabledTextColor = Color.parseColor("#666666");
        DARK_THEME.errorColor = Color.parseColor("#CF6679");
        DARK_THEME.warningColor = Color.parseColor("#FFB74D");
        DARK_THEME.successColor = Color.parseColor("#81C784");
        DARK_THEME.infoColor = Color.parseColor("#64B5F6");
        DARK_THEME.dividerColor = Color.parseColor("#424242");
        DARK_THEME.borderColor = Color.parseColor("#616161");
        DARK_THEME.codeBackgroundColor = Color.parseColor("#2D2D2D");
        DARK_THEME.keywordColor = Color.parseColor("#CC7832");
        DARK_THEME.stringColor = Color.parseColor("#6A8759");
        DARK_THEME.commentColor = Color.parseColor("#808080");
        DARK_THEME.numberColor = Color.parseColor("#6897BB");
        DARK_THEME.operatorColor = Color.parseColor("#E8E8E8");
        
        // AMOLED Theme (Pure Black)
        AMOLED_THEME.primaryColor = Color.parseColor("#BB86FC");
        AMOLED_THEME.primaryDarkColor = Color.parseColor("#3700B3");
        AMOLED_THEME.accentColor = Color.parseColor("#03DAC6");
        AMOLED_THEME.backgroundColor = Color.parseColor("#000000");
        AMOLED_THEME.surfaceColor = Color.parseColor("#0A0A0A");
        AMOLED_THEME.cardBackgroundColor = Color.parseColor("#141414");
        AMOLED_THEME.primaryTextColor = Color.parseColor("#FFFFFF");
        AMOLED_THEME.secondaryTextColor = Color.parseColor("#B0B0B0");
        AMOLED_THEME.disabledTextColor = Color.parseColor("#666666");
        AMOLED_THEME.errorColor = Color.parseColor("#CF6679");
        AMOLED_THEME.warningColor = Color.parseColor("#FFB74D");
        AMOLED_THEME.successColor = Color.parseColor("#81C784");
        AMOLED_THEME.infoColor = Color.parseColor("#64B5F6");
        AMOLED_THEME.dividerColor = Color.parseColor("#333333");
        AMOLED_THEME.borderColor = Color.parseColor("#444444");
        AMOLED_THEME.codeBackgroundColor = Color.parseColor("#1A1A1A");
        AMOLED_THEME.keywordColor = Color.parseColor("#CC7832");
        AMOLED_THEME.stringColor = Color.parseColor("#6A8759");
        AMOLED_THEME.commentColor = Color.parseColor("#808080");
        AMOLED_THEME.numberColor = Color.parseColor("#6897BB");
        AMOLED_THEME.operatorColor = Color.parseColor("#E8E8E8");
    }
    
    private ThemeMode currentTheme = ThemeMode.SYSTEM_DEFAULT;
    private ThemeColors currentColors;
    private OnThemeChangeListener themeChangeListener;
    
    public interface OnThemeChangeListener {
        void onThemeChanged(ThemeMode newTheme);
    }
    
    public ThemeManager() {
        this.currentColors = DARK_THEME; // Default to dark theme
    }
    
    /**
     * Apply theme based on system setting or user preference
     */
    public void applyTheme(Context context) {
        if (currentTheme == ThemeMode.SYSTEM_DEFAULT) {
            int nightModeFlags = context.getResources().getConfiguration().uiMode 
                & Configuration.UI_MODE_NIGHT_MASK;
            
            if (nightModeFlags == Configuration.UI_MODE_NIGHT_YES) {
                currentColors = DARK_THEME;
            } else {
                currentColors = LIGHT_THEME;
            }
        } else {
            switch (currentTheme) {
                case LIGHT:
                    currentColors = LIGHT_THEME;
                    break;
                case DARK:
                    currentColors = DARK_THEME;
                    break;
                case AMOLED:
                    currentColors = AMOLED_THEME;
                    break;
                default:
                    currentColors = DARK_THEME;
            }
        }
    }
    
    /**
     * Set theme mode
     */
    public void setThemeMode(ThemeMode mode) {
        this.currentTheme = mode;
        if (themeChangeListener != null) {
            themeChangeListener.onThemeChanged(mode);
        }
    }
    
    /**
     * Get current theme mode
     */
    public ThemeMode getThemeMode() {
        return currentTheme;
    }
    
    /**
     * Toggle between light and dark theme
     */
    public void toggleTheme() {
        if (currentTheme == ThemeMode.LIGHT || currentTheme == ThemeMode.SYSTEM_DEFAULT) {
            setThemeMode(ThemeMode.DARK);
        } else {
            setThemeMode(ThemeMode.LIGHT);
        }
    }
    
    /**
     * Cycle through all themes
     */
    public void cycleTheme() {
        switch (currentTheme) {
            case LIGHT:
                setThemeMode(ThemeMode.DARK);
                break;
            case DARK:
                setThemeMode(ThemeMode.AMOLED);
                break;
            case AMOLED:
                setThemeMode(ThemeMode.LIGHT);
                break;
            default:
                setThemeMode(ThemeMode.DARK);
        }
    }
    
    /**
     * Get current theme colors
     */
    public ThemeColors getThemeColors() {
        return currentColors;
    }
    
    /**
     * Set theme change listener
     */
    public void setOnThemeChangeListener(OnThemeChangeListener listener) {
        this.themeChangeListener = listener;
    }
    
    /**
     * Apply background color to a view
     */
    public void applyBackground(View view) {
        view.setBackgroundColor(currentColors.backgroundColor);
    }
    
    /**
     * Apply surface color to a view
     */
    public void applySurface(View view) {
        view.setBackgroundColor(currentColors.surfaceColor);
    }
    
    /**
     * Apply card background to a view
     */
    public void applyCardBackground(View view) {
        view.setBackgroundColor(currentColors.cardBackgroundColor);
    }
    
    /**
     * Create a gradient drawable with theme colors
     */
    public GradientDrawable createGradientDrawable(int orientation) {
        GradientDrawable drawable = new GradientDrawable();
        drawable.setOrientation(GradientDrawable.Orientation.TOP_BOTTOM);
        drawable.setGradientType(GradientDrawable.LINEAR_GRADIENT);
        
        int[] colors;
        if (currentTheme == ThemeMode.AMOLED || currentTheme == ThemeMode.DARK) {
            colors = new int[]{
                currentColors.surfaceColor,
                currentColors.backgroundColor
            };
        } else {
            colors = new int[]{
                currentColors.primaryColor,
                currentColors.primaryDarkColor
            };
        }
        
        drawable.setColors(colors);
        return drawable;
    }
    
    /**
     * Get status bar color for current theme
     */
    public int getStatusBarColor() {
        return currentColors.primaryDarkColor;
    }
    
    /**
     * Get navigation bar color for current theme
     */
    public int getNavigationBarColor() {
        if (currentTheme == ThemeMode.AMOLED) {
            return Color.BLACK;
        }
        return currentColors.backgroundColor;
    }
    
    /**
     * Check if current theme is dark
     */
    public boolean isDarkTheme() {
        return currentTheme == ThemeMode.DARK || currentTheme == ThemeMode.AMOLED 
            || (currentTheme == ThemeMode.SYSTEM_DEFAULT && 
                isSystemInDarkMode());
    }
    
    /**
     * Check if system is in dark mode
     */
    public boolean isSystemInDarkMode() {
        // This would need a Context to check properly
        // Placeholder implementation
        return false;
    }
    
    /**
     * Show theme change toast notification
     */
    public void showThemeChangeToast(Context context) {
        String message;
        switch (currentTheme) {
            case LIGHT:
                message = "☀️ Light theme applied";
                break;
            case DARK:
                message = "🌙 Dark theme applied";
                break;
            case AMOLED:
                message = "⚫ AMOLED theme applied";
                break;
            case SYSTEM_DEFAULT:
                message = "🔄 System default theme";
                break;
            default:
                message = "Theme changed";
        }
        
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }
    
    /**
     * Get theme name for display
     */
    public String getThemeName() {
        switch (currentTheme) {
            case LIGHT:
                return "Light";
            case DARK:
                return "Dark";
            case AMOLED:
                return "AMOLED";
            case SYSTEM_DEFAULT:
                return "System Default";
            default:
                return "Unknown";
        }
    }
}
