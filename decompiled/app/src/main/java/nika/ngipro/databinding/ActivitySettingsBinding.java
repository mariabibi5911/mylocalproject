package nika.ngipro.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.appcompat.widget.Toolbar;
import androidx.viewbinding.ViewBinding;
import androidx.viewbinding.ViewBindings;
import nika.ngipro.R;

/* loaded from: classes5.dex */
public final class ActivitySettingsBinding implements ViewBinding {
    public final LinearLayout colorSwatchRow;
    public final TextView currentLanguageLabel;
    private final LinearLayout rootView;
    public final LinearLayout rowCheckUpdate;
    public final LinearLayout rowLanguage;
    public final Toolbar toolbarSettings;
    public final TextView updateCheckStatus;

    private ActivitySettingsBinding(LinearLayout rootView, LinearLayout colorSwatchRow, TextView currentLanguageLabel, LinearLayout rowCheckUpdate, LinearLayout rowLanguage, Toolbar toolbarSettings, TextView updateCheckStatus) {
        this.rootView = rootView;
        this.colorSwatchRow = colorSwatchRow;
        this.currentLanguageLabel = currentLanguageLabel;
        this.rowCheckUpdate = rowCheckUpdate;
        this.rowLanguage = rowLanguage;
        this.toolbarSettings = toolbarSettings;
        this.updateCheckStatus = updateCheckStatus;
    }

    @Override // androidx.viewbinding.ViewBinding
    public LinearLayout getRoot() {
        return this.rootView;
    }

    public static ActivitySettingsBinding inflate(LayoutInflater inflater) {
        return inflate(inflater, null, false);
    }

    public static ActivitySettingsBinding inflate(LayoutInflater inflater, ViewGroup parent, boolean attachToParent) {
        View root = inflater.inflate(R.layout.activity_settings, parent, false);
        if (attachToParent) {
            parent.addView(root);
        }
        return bind(root);
    }

    public static ActivitySettingsBinding bind(View rootView) {
        int id = R.id.colorSwatchRow;
        LinearLayout colorSwatchRow = (LinearLayout) ViewBindings.findChildViewById(rootView, id);
        if (colorSwatchRow != null) {
            id = R.id.currentLanguageLabel;
            TextView currentLanguageLabel = (TextView) ViewBindings.findChildViewById(rootView, id);
            if (currentLanguageLabel != null) {
                id = R.id.rowCheckUpdate;
                LinearLayout rowCheckUpdate = (LinearLayout) ViewBindings.findChildViewById(rootView, id);
                if (rowCheckUpdate != null) {
                    id = R.id.rowLanguage;
                    LinearLayout rowLanguage = (LinearLayout) ViewBindings.findChildViewById(rootView, id);
                    if (rowLanguage != null) {
                        id = R.id.toolbarSettings;
                        Toolbar toolbarSettings = (Toolbar) ViewBindings.findChildViewById(rootView, id);
                        if (toolbarSettings != null) {
                            id = R.id.updateCheckStatus;
                            TextView updateCheckStatus = (TextView) ViewBindings.findChildViewById(rootView, id);
                            if (updateCheckStatus != null) {
                                return new ActivitySettingsBinding((LinearLayout) rootView, colorSwatchRow, currentLanguageLabel, rowCheckUpdate, rowLanguage, toolbarSettings, updateCheckStatus);
                            }
                        }
                    }
                }
            }
        }
        String missingId = rootView.getResources().getResourceName(id);
        throw new NullPointerException("Missing required view with ID: ".concat(missingId));
    }
}
