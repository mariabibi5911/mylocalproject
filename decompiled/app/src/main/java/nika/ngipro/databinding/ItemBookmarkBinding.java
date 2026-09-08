package nika.ngipro.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.viewbinding.ViewBinding;
import androidx.viewbinding.ViewBindings;
import nika.ngipro.R;

/* loaded from: classes5.dex */
public final class ItemBookmarkBinding implements ViewBinding {
    public final ImageView bmDelete;
    public final TextView bmLabel;
    public final TextView bmMeta;
    public final TextView bmNote;
    private final LinearLayout rootView;

    private ItemBookmarkBinding(LinearLayout rootView, ImageView bmDelete, TextView bmLabel, TextView bmMeta, TextView bmNote) {
        this.rootView = rootView;
        this.bmDelete = bmDelete;
        this.bmLabel = bmLabel;
        this.bmMeta = bmMeta;
        this.bmNote = bmNote;
    }

    @Override // androidx.viewbinding.ViewBinding
    public LinearLayout getRoot() {
        return this.rootView;
    }

    public static ItemBookmarkBinding inflate(LayoutInflater inflater) {
        return inflate(inflater, null, false);
    }

    public static ItemBookmarkBinding inflate(LayoutInflater inflater, ViewGroup parent, boolean attachToParent) {
        View root = inflater.inflate(R.layout.item_bookmark, parent, false);
        if (attachToParent) {
            parent.addView(root);
        }
        return bind(root);
    }

    public static ItemBookmarkBinding bind(View rootView) {
        int id = R.id.bmDelete;
        ImageView bmDelete = (ImageView) ViewBindings.findChildViewById(rootView, id);
        if (bmDelete != null) {
            id = R.id.bmLabel;
            TextView bmLabel = (TextView) ViewBindings.findChildViewById(rootView, id);
            if (bmLabel != null) {
                id = R.id.bmMeta;
                TextView bmMeta = (TextView) ViewBindings.findChildViewById(rootView, id);
                if (bmMeta != null) {
                    id = R.id.bmNote;
                    TextView bmNote = (TextView) ViewBindings.findChildViewById(rootView, id);
                    if (bmNote != null) {
                        return new ItemBookmarkBinding((LinearLayout) rootView, bmDelete, bmLabel, bmMeta, bmNote);
                    }
                }
            }
        }
        String missingId = rootView.getResources().getResourceName(id);
        throw new NullPointerException("Missing required view with ID: ".concat(missingId));
    }
}
