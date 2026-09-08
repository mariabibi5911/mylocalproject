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
public final class ItemFunctionBinding implements ViewBinding {
    public final TextView funcMeta;
    public final TextView funcName;
    private final LinearLayout rootView;

    private ItemFunctionBinding(LinearLayout rootView, TextView funcMeta, TextView funcName) {
        this.rootView = rootView;
        this.funcMeta = funcMeta;
        this.funcName = funcName;
    }

    @Override // androidx.viewbinding.ViewBinding
    public LinearLayout getRoot() {
        return this.rootView;
    }

    public static ItemFunctionBinding inflate(LayoutInflater inflater) {
        return inflate(inflater, null, false);
    }

    public static ItemFunctionBinding inflate(LayoutInflater inflater, ViewGroup parent, boolean attachToParent) {
        View root = inflater.inflate(R.layout.item_function, parent, false);
        if (attachToParent) {
            parent.addView(root);
        }
        return bind(root);
    }

    public static ItemFunctionBinding bind(View rootView) {
        int id = R.id.funcMeta;
        TextView funcMeta = (TextView) ViewBindings.findChildViewById(rootView, id);
        if (funcMeta != null) {
            id = R.id.funcName;
            TextView funcName = (TextView) ViewBindings.findChildViewById(rootView, id);
            if (funcName != null) {
                return new ItemFunctionBinding((LinearLayout) rootView, funcMeta, funcName);
            }
        }
        String missingId = rootView.getResources().getResourceName(id);
        throw new NullPointerException("Missing required view with ID: ".concat(missingId));
    }
}
