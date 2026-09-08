package nika.ngipro.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.viewbinding.ViewBinding;
import androidx.viewbinding.ViewBindings;
import nika.ngipro.R;

/* loaded from: classes5.dex */
public final class ActivityProfileBinding implements ViewBinding {
    public final CardView btnProfileLogout;
    public final ImageView profileAvatar;
    public final TextView profileFilesCount;
    public final TextView profileUserName;
    private final LinearLayout rootView;
    public final Toolbar toolbarProfile;

    private ActivityProfileBinding(LinearLayout rootView, CardView btnProfileLogout, ImageView profileAvatar, TextView profileFilesCount, TextView profileUserName, Toolbar toolbarProfile) {
        this.rootView = rootView;
        this.btnProfileLogout = btnProfileLogout;
        this.profileAvatar = profileAvatar;
        this.profileFilesCount = profileFilesCount;
        this.profileUserName = profileUserName;
        this.toolbarProfile = toolbarProfile;
    }

    @Override // androidx.viewbinding.ViewBinding
    public LinearLayout getRoot() {
        return this.rootView;
    }

    public static ActivityProfileBinding inflate(LayoutInflater inflater) {
        return inflate(inflater, null, false);
    }

    public static ActivityProfileBinding inflate(LayoutInflater inflater, ViewGroup parent, boolean attachToParent) {
        View root = inflater.inflate(R.layout.activity_profile, parent, false);
        if (attachToParent) {
            parent.addView(root);
        }
        return bind(root);
    }

    public static ActivityProfileBinding bind(View rootView) {
        int id = R.id.btnProfileLogout;
        CardView btnProfileLogout = (CardView) ViewBindings.findChildViewById(rootView, id);
        if (btnProfileLogout != null) {
            id = R.id.profileAvatar;
            ImageView profileAvatar = (ImageView) ViewBindings.findChildViewById(rootView, id);
            if (profileAvatar != null) {
                id = R.id.profileFilesCount;
                TextView profileFilesCount = (TextView) ViewBindings.findChildViewById(rootView, id);
                if (profileFilesCount != null) {
                    id = R.id.profileUserName;
                    TextView profileUserName = (TextView) ViewBindings.findChildViewById(rootView, id);
                    if (profileUserName != null) {
                        id = R.id.toolbarProfile;
                        Toolbar toolbarProfile = (Toolbar) ViewBindings.findChildViewById(rootView, id);
                        if (toolbarProfile != null) {
                            return new ActivityProfileBinding((LinearLayout) rootView, btnProfileLogout, profileAvatar, profileFilesCount, profileUserName, toolbarProfile);
                        }
                    }
                }
            }
        }
        String missingId = rootView.getResources().getResourceName(id);
        throw new NullPointerException("Missing required view with ID: ".concat(missingId));
    }
}
