package nika.ngipro.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.cardview.widget.CardView;
import androidx.viewbinding.ViewBinding;
import androidx.viewbinding.ViewBindings;
import nika.ngipro.R;

/* loaded from: classes5.dex */
public final class ItemToolBinding implements ViewBinding {
    private final CardView rootView;
    public final CardView toolCard;
    public final ImageView toolIcon;
    public final TextView toolLabel;

    private ItemToolBinding(CardView rootView, CardView toolCard, ImageView toolIcon, TextView toolLabel) {
        this.rootView = rootView;
        this.toolCard = toolCard;
        this.toolIcon = toolIcon;
        this.toolLabel = toolLabel;
    }

    @Override // androidx.viewbinding.ViewBinding
    public CardView getRoot() {
        return this.rootView;
    }

    public static ItemToolBinding inflate(LayoutInflater inflater) {
        return inflate(inflater, null, false);
    }

    public static ItemToolBinding inflate(LayoutInflater inflater, ViewGroup parent, boolean attachToParent) {
        View root = inflater.inflate(R.layout.item_tool, parent, false);
        if (attachToParent) {
            parent.addView(root);
        }
        return bind(root);
    }

    public static ItemToolBinding bind(View rootView) {
        CardView toolCard = (CardView) rootView;
        int id = R.id.toolIcon;
        ImageView toolIcon = (ImageView) ViewBindings.findChildViewById(rootView, id);
        if (toolIcon != null) {
            id = R.id.toolLabel;
            TextView toolLabel = (TextView) ViewBindings.findChildViewById(rootView, id);
            if (toolLabel != null) {
                return new ItemToolBinding((CardView) rootView, toolCard, toolIcon, toolLabel);
            }
        }
        String missingId = rootView.getResources().getResourceName(id);
        throw new NullPointerException("Missing required view with ID: ".concat(missingId));
    }
}
