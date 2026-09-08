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
public final class ActivityDexViewBinding implements ViewBinding {
    public final LinearLayout detailContainer;
    public final TextView dexCountText;
    public final TextView dexDetailText;
    public final RecyclerView dexRecycler;
    public final LinearLayout listContainer;
    private final FrameLayout rootView;
    public final Toolbar toolbarDexDetail;
    public final Toolbar toolbarDexList;

    private ActivityDexViewBinding(FrameLayout rootView, LinearLayout detailContainer, TextView dexCountText, TextView dexDetailText, RecyclerView dexRecycler, LinearLayout listContainer, Toolbar toolbarDexDetail, Toolbar toolbarDexList) {
        this.rootView = rootView;
        this.detailContainer = detailContainer;
        this.dexCountText = dexCountText;
        this.dexDetailText = dexDetailText;
        this.dexRecycler = dexRecycler;
        this.listContainer = listContainer;
        this.toolbarDexDetail = toolbarDexDetail;
        this.toolbarDexList = toolbarDexList;
    }

    @Override // androidx.viewbinding.ViewBinding
    public FrameLayout getRoot() {
        return this.rootView;
    }

    public static ActivityDexViewBinding inflate(LayoutInflater inflater) {
        return inflate(inflater, null, false);
    }

    public static ActivityDexViewBinding inflate(LayoutInflater inflater, ViewGroup parent, boolean attachToParent) {
        View root = inflater.inflate(R.layout.activity_dex_view, parent, false);
        if (attachToParent) {
            parent.addView(root);
        }
        return bind(root);
    }

    public static ActivityDexViewBinding bind(View rootView) {
        int id = R.id.detailContainer;
        LinearLayout detailContainer = (LinearLayout) ViewBindings.findChildViewById(rootView, id);
        if (detailContainer != null) {
            id = R.id.dexCountText;
            TextView dexCountText = (TextView) ViewBindings.findChildViewById(rootView, id);
            if (dexCountText != null) {
                id = R.id.dexDetailText;
                TextView dexDetailText = (TextView) ViewBindings.findChildViewById(rootView, id);
                if (dexDetailText != null) {
                    id = R.id.dexRecycler;
                    RecyclerView dexRecycler = (RecyclerView) ViewBindings.findChildViewById(rootView, id);
                    if (dexRecycler != null) {
                        id = R.id.listContainer;
                        LinearLayout listContainer = (LinearLayout) ViewBindings.findChildViewById(rootView, id);
                        if (listContainer != null) {
                            id = R.id.toolbarDexDetail;
                            Toolbar toolbarDexDetail = (Toolbar) ViewBindings.findChildViewById(rootView, id);
                            if (toolbarDexDetail != null) {
                                id = R.id.toolbarDexList;
                                Toolbar toolbarDexList = (Toolbar) ViewBindings.findChildViewById(rootView, id);
                                if (toolbarDexList != null) {
                                    return new ActivityDexViewBinding((FrameLayout) rootView, detailContainer, dexCountText, dexDetailText, dexRecycler, listContainer, toolbarDexDetail, toolbarDexList);
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
