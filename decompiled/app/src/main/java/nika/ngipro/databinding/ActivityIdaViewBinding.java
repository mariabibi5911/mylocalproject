package nika.ngipro.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewbinding.ViewBinding;
import androidx.viewbinding.ViewBindings;
import nika.ngipro.R;

/* loaded from: classes5.dex */
public final class ActivityIdaViewBinding implements ViewBinding {
    public final LinearLayout detailContainer;
    public final TextView detailText;
    public final TextView functionCountText;
    public final LinearLayout functionListContainer;
    public final RecyclerView functionRecycler;
    private final FrameLayout rootView;
    public final TextView tabDisasm;
    public final TextView tabPseudo;
    public final Toolbar toolbarDetail;
    public final Toolbar toolbarList;

    private ActivityIdaViewBinding(FrameLayout rootView, LinearLayout detailContainer, TextView detailText, TextView functionCountText, LinearLayout functionListContainer, RecyclerView functionRecycler, TextView tabDisasm, TextView tabPseudo, Toolbar toolbarDetail, Toolbar toolbarList) {
        this.rootView = rootView;
        this.detailContainer = detailContainer;
        this.detailText = detailText;
        this.functionCountText = functionCountText;
        this.functionListContainer = functionListContainer;
        this.functionRecycler = functionRecycler;
        this.tabDisasm = tabDisasm;
        this.tabPseudo = tabPseudo;
        this.toolbarDetail = toolbarDetail;
        this.toolbarList = toolbarList;
    }

    @Override // androidx.viewbinding.ViewBinding
    public FrameLayout getRoot() {
        return this.rootView;
    }

    public static ActivityIdaViewBinding inflate(LayoutInflater inflater) {
        return inflate(inflater, null, false);
    }

    public static ActivityIdaViewBinding inflate(LayoutInflater inflater, ViewGroup parent, boolean attachToParent) {
        View root = inflater.inflate(R.layout.activity_ida_view, parent, false);
        if (attachToParent) {
            parent.addView(root);
        }
        return bind(root);
    }

    public static ActivityIdaViewBinding bind(View rootView) {
        int id = R.id.detailContainer;
        LinearLayout detailContainer = (LinearLayout) ViewBindings.findChildViewById(rootView, id);
        if (detailContainer != null) {
            id = R.id.detailText;
            TextView detailText = (TextView) ViewBindings.findChildViewById(rootView, id);
            if (detailText != null) {
                id = R.id.functionCountText;
                TextView functionCountText = (TextView) ViewBindings.findChildViewById(rootView, id);
                if (functionCountText != null) {
                    id = R.id.functionListContainer;
                    LinearLayout functionListContainer = (LinearLayout) ViewBindings.findChildViewById(rootView, id);
                    if (functionListContainer != null) {
                        id = R.id.functionRecycler;
                        RecyclerView functionRecycler = (RecyclerView) ViewBindings.findChildViewById(rootView, id);
                        if (functionRecycler != null) {
                            id = R.id.tabDisasm;
                            TextView tabDisasm = (TextView) ViewBindings.findChildViewById(rootView, id);
                            if (tabDisasm != null) {
                                id = R.id.tabPseudo;
                                TextView tabPseudo = (TextView) ViewBindings.findChildViewById(rootView, id);
                                if (tabPseudo != null) {
                                    id = R.id.toolbarDetail;
                                    Toolbar toolbarDetail = (Toolbar) ViewBindings.findChildViewById(rootView, id);
                                    if (toolbarDetail != null) {
                                        id = R.id.toolbarList;
                                        Toolbar toolbarList = (Toolbar) ViewBindings.findChildViewById(rootView, id);
                                        if (toolbarList != null) {
                                            return new ActivityIdaViewBinding((FrameLayout) rootView, detailContainer, detailText, functionCountText, functionListContainer, functionRecycler, tabDisasm, tabPseudo, toolbarDetail, toolbarList);
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
