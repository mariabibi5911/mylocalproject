package nika.ngipro.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewbinding.ViewBinding;
import androidx.viewbinding.ViewBindings;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import nika.ngipro.R;

/* loaded from: classes5.dex */
public final class ActivityMainBinding implements ViewBinding {
    public final FloatingActionButton btnSelect;
    public final DrawerLayout drawerLayout;
    public final LinearLayout emptyState;
    public final ImageView emptyStateIcon;
    public final ProgressBar emptyStateProgress;
    public final TextView emptyStateText;
    public final ImageView homeAvatar;
    public final TextView homeUserName;
    public final NavigationView navView;
    public final CardView profileStrip;
    public final RecyclerView resultsList;
    private final DrawerLayout rootView;
    public final View statusDot;
    public final TextView statusFileText;
    public final Toolbar toolbar;
    public final RecyclerView toolsGrid;

    private ActivityMainBinding(DrawerLayout rootView, FloatingActionButton btnSelect, DrawerLayout drawerLayout, LinearLayout emptyState, ImageView emptyStateIcon, ProgressBar emptyStateProgress, TextView emptyStateText, ImageView homeAvatar, TextView homeUserName, NavigationView navView, CardView profileStrip, RecyclerView resultsList, View statusDot, TextView statusFileText, Toolbar toolbar, RecyclerView toolsGrid) {
        this.rootView = rootView;
        this.btnSelect = btnSelect;
        this.drawerLayout = drawerLayout;
        this.emptyState = emptyState;
        this.emptyStateIcon = emptyStateIcon;
        this.emptyStateProgress = emptyStateProgress;
        this.emptyStateText = emptyStateText;
        this.homeAvatar = homeAvatar;
        this.homeUserName = homeUserName;
        this.navView = navView;
        this.profileStrip = profileStrip;
        this.resultsList = resultsList;
        this.statusDot = statusDot;
        this.statusFileText = statusFileText;
        this.toolbar = toolbar;
        this.toolsGrid = toolsGrid;
    }

    @Override // androidx.viewbinding.ViewBinding
    public DrawerLayout getRoot() {
        return this.rootView;
    }

    public static ActivityMainBinding inflate(LayoutInflater inflater) {
        return inflate(inflater, null, false);
    }

    public static ActivityMainBinding inflate(LayoutInflater inflater, ViewGroup parent, boolean attachToParent) {
        View root = inflater.inflate(R.layout.activity_main, parent, false);
        if (attachToParent) {
            parent.addView(root);
        }
        return bind(root);
    }

    public static ActivityMainBinding bind(View rootView) {
        View statusDot;
        int id = R.id.btnSelect;
        FloatingActionButton btnSelect = (FloatingActionButton) ViewBindings.findChildViewById(rootView, id);
        if (btnSelect != null) {
            DrawerLayout drawerLayout = (DrawerLayout) rootView;
            id = R.id.emptyState;
            LinearLayout emptyState = (LinearLayout) ViewBindings.findChildViewById(rootView, id);
            if (emptyState != null) {
                id = R.id.emptyStateIcon;
                ImageView emptyStateIcon = (ImageView) ViewBindings.findChildViewById(rootView, id);
                if (emptyStateIcon != null) {
                    id = R.id.emptyStateProgress;
                    ProgressBar emptyStateProgress = (ProgressBar) ViewBindings.findChildViewById(rootView, id);
                    if (emptyStateProgress != null) {
                        id = R.id.emptyStateText;
                        TextView emptyStateText = (TextView) ViewBindings.findChildViewById(rootView, id);
                        if (emptyStateText != null) {
                            id = R.id.homeAvatar;
                            ImageView homeAvatar = (ImageView) ViewBindings.findChildViewById(rootView, id);
                            if (homeAvatar != null) {
                                id = R.id.homeUserName;
                                TextView homeUserName = (TextView) ViewBindings.findChildViewById(rootView, id);
                                if (homeUserName != null) {
                                    id = R.id.nav_view;
                                    NavigationView navView = (NavigationView) ViewBindings.findChildViewById(rootView, id);
                                    if (navView != null) {
                                        id = R.id.profileStrip;
                                        CardView profileStrip = (CardView) ViewBindings.findChildViewById(rootView, id);
                                        if (profileStrip != null) {
                                            id = R.id.resultsList;
                                            RecyclerView resultsList = (RecyclerView) ViewBindings.findChildViewById(rootView, id);
                                            if (resultsList != null && (statusDot = ViewBindings.findChildViewById(rootView, (id = R.id.statusDot))) != null) {
                                                id = R.id.statusFileText;
                                                TextView statusFileText = (TextView) ViewBindings.findChildViewById(rootView, id);
                                                if (statusFileText != null) {
                                                    id = R.id.toolbar;
                                                    Toolbar toolbar = (Toolbar) ViewBindings.findChildViewById(rootView, id);
                                                    if (toolbar != null) {
                                                        id = R.id.toolsGrid;
                                                        RecyclerView toolsGrid = (RecyclerView) ViewBindings.findChildViewById(rootView, id);
                                                        if (toolsGrid != null) {
                                                            return new ActivityMainBinding((DrawerLayout) rootView, btnSelect, drawerLayout, emptyState, emptyStateIcon, emptyStateProgress, emptyStateText, homeAvatar, homeUserName, navView, profileStrip, resultsList, statusDot, statusFileText, toolbar, toolsGrid);
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
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
