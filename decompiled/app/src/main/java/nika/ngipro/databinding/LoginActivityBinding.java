package nika.ngipro.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import androidx.viewbinding.ViewBinding;
import androidx.viewbinding.ViewBindings;
import nika.ngipro.R;

/* loaded from: classes5.dex */
public final class LoginActivityBinding implements ViewBinding {
    public final FrameLayout cardGithub;
    public final LinearLayout loadingContainer;
    public final TextView loadingText;
    public final LinearLayout loginContent;
    public final RelativeLayout mainContainer;
    private final RelativeLayout rootView;
    public final TextView titleText;
    public final FrameLayout webViewContainer;

    private LoginActivityBinding(RelativeLayout rootView, FrameLayout cardGithub, LinearLayout loadingContainer, TextView loadingText, LinearLayout loginContent, RelativeLayout mainContainer, TextView titleText, FrameLayout webViewContainer) {
        this.rootView = rootView;
        this.cardGithub = cardGithub;
        this.loadingContainer = loadingContainer;
        this.loadingText = loadingText;
        this.loginContent = loginContent;
        this.mainContainer = mainContainer;
        this.titleText = titleText;
        this.webViewContainer = webViewContainer;
    }

    @Override // androidx.viewbinding.ViewBinding
    public RelativeLayout getRoot() {
        return this.rootView;
    }

    public static LoginActivityBinding inflate(LayoutInflater inflater) {
        return inflate(inflater, null, false);
    }

    public static LoginActivityBinding inflate(LayoutInflater inflater, ViewGroup parent, boolean attachToParent) {
        View root = inflater.inflate(R.layout.login_activity, parent, false);
        if (attachToParent) {
            parent.addView(root);
        }
        return bind(root);
    }

    public static LoginActivityBinding bind(View rootView) {
        int id = R.id.cardGithub;
        FrameLayout cardGithub = (FrameLayout) ViewBindings.findChildViewById(rootView, id);
        if (cardGithub != null) {
            id = R.id.loadingContainer;
            LinearLayout loadingContainer = (LinearLayout) ViewBindings.findChildViewById(rootView, id);
            if (loadingContainer != null) {
                id = R.id.loadingText;
                TextView loadingText = (TextView) ViewBindings.findChildViewById(rootView, id);
                if (loadingText != null) {
                    id = R.id.loginContent;
                    LinearLayout loginContent = (LinearLayout) ViewBindings.findChildViewById(rootView, id);
                    if (loginContent != null) {
                        RelativeLayout mainContainer = (RelativeLayout) rootView;
                        id = R.id.titleText;
                        TextView titleText = (TextView) ViewBindings.findChildViewById(rootView, id);
                        if (titleText != null) {
                            id = R.id.webViewContainer;
                            FrameLayout webViewContainer = (FrameLayout) ViewBindings.findChildViewById(rootView, id);
                            if (webViewContainer != null) {
                                return new LoginActivityBinding((RelativeLayout) rootView, cardGithub, loadingContainer, loadingText, loginContent, mainContainer, titleText, webViewContainer);
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
