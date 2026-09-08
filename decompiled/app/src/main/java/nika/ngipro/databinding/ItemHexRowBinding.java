package nika.ngipro.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.viewbinding.ViewBinding;
import nika.ngipro.R;

/* loaded from: classes5.dex */
public final class ItemHexRowBinding implements ViewBinding {
    public final TextView hexRowText;
    private final TextView rootView;

    private ItemHexRowBinding(TextView rootView, TextView hexRowText) {
        this.rootView = rootView;
        this.hexRowText = hexRowText;
    }

    @Override // androidx.viewbinding.ViewBinding
    public TextView getRoot() {
        return this.rootView;
    }

    public static ItemHexRowBinding inflate(LayoutInflater inflater) {
        return inflate(inflater, null, false);
    }

    public static ItemHexRowBinding inflate(LayoutInflater inflater, ViewGroup parent, boolean attachToParent) {
        View root = inflater.inflate(R.layout.item_hex_row, parent, false);
        if (attachToParent) {
            parent.addView(root);
        }
        return bind(root);
    }

    public static ItemHexRowBinding bind(View rootView) {
        if (rootView == null) {
            throw new NullPointerException("rootView");
        }
        TextView hexRowText = (TextView) rootView;
        return new ItemHexRowBinding((TextView) rootView, hexRowText);
    }
}
