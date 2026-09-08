package nika.ngipro.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.viewbinding.ViewBinding;
import androidx.viewbinding.ViewBindings;
import nika.ngipro.R;

/* loaded from: classes5.dex */
public final class ItemResultBinding implements ViewBinding {
    public final View resultDot;
    public final TextView resultText;
    private final LinearLayout rootView;

    private ItemResultBinding(LinearLayout rootView, View resultDot, TextView resultText) {
        this.rootView = rootView;
        this.resultDot = resultDot;
        this.resultText = resultText;
    }

    @Override // androidx.viewbinding.ViewBinding
    public LinearLayout getRoot() {
        return this.rootView;
    }

    public static ItemResultBinding inflate(LayoutInflater inflater) {
        return inflate(inflater, null, false);
    }

    public static ItemResultBinding inflate(LayoutInflater inflater, ViewGroup parent, boolean attachToParent) {
        View root = inflater.inflate(R.layout.item_result, parent, false);
        if (attachToParent) {
            parent.addView(root);
        }
        return bind(root);
    }

    public static ItemResultBinding bind(View rootView) {
        int id = R.id.resultDot;
        View resultDot = ViewBindings.findChildViewById(rootView, id);
        if (resultDot != null) {
            id = R.id.resultText;
            TextView resultText = (TextView) ViewBindings.findChildViewById(rootView, id);
            if (resultText != null) {
                return new ItemResultBinding((LinearLayout) rootView, resultDot, resultText);
            }
        }
        String missingId = rootView.getResources().getResourceName(id);
        throw new NullPointerException("Missing required view with ID: ".concat(missingId));
    }
}
