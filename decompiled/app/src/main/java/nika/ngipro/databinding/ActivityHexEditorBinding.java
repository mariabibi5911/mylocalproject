package nika.ngipro.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewbinding.ViewBinding;
import androidx.viewbinding.ViewBindings;
import nika.ngipro.R;

/* loaded from: classes5.dex */
public final class ActivityHexEditorBinding implements ViewBinding {
    public final TextView btnFindNext;
    public final TextView btnGoto;
    public final TextView btnSave;
    public final TextView btnSearch;
    public final TextView btnUndo;
    public final RecyclerView hexRecycler;
    public final TextView hexStatusText;
    public final TextView inspectorText;
    private final LinearLayout rootView;
    public final Toolbar toolbarHex;

    private ActivityHexEditorBinding(LinearLayout rootView, TextView btnFindNext, TextView btnGoto, TextView btnSave, TextView btnSearch, TextView btnUndo, RecyclerView hexRecycler, TextView hexStatusText, TextView inspectorText, Toolbar toolbarHex) {
        this.rootView = rootView;
        this.btnFindNext = btnFindNext;
        this.btnGoto = btnGoto;
        this.btnSave = btnSave;
        this.btnSearch = btnSearch;
        this.btnUndo = btnUndo;
        this.hexRecycler = hexRecycler;
        this.hexStatusText = hexStatusText;
        this.inspectorText = inspectorText;
        this.toolbarHex = toolbarHex;
    }

    @Override // androidx.viewbinding.ViewBinding
    public LinearLayout getRoot() {
        return this.rootView;
    }

    public static ActivityHexEditorBinding inflate(LayoutInflater inflater) {
        return inflate(inflater, null, false);
    }

    public static ActivityHexEditorBinding inflate(LayoutInflater inflater, ViewGroup parent, boolean attachToParent) {
        View root = inflater.inflate(R.layout.activity_hex_editor, parent, false);
        if (attachToParent) {
            parent.addView(root);
        }
        return bind(root);
    }

    public static ActivityHexEditorBinding bind(View rootView) {
        int id = R.id.btnFindNext;
        TextView btnFindNext = (TextView) ViewBindings.findChildViewById(rootView, id);
        if (btnFindNext != null) {
            id = R.id.btnGoto;
            TextView btnGoto = (TextView) ViewBindings.findChildViewById(rootView, id);
            if (btnGoto != null) {
                id = R.id.btnSave;
                TextView btnSave = (TextView) ViewBindings.findChildViewById(rootView, id);
                if (btnSave != null) {
                    id = R.id.btnSearch;
                    TextView btnSearch = (TextView) ViewBindings.findChildViewById(rootView, id);
                    if (btnSearch != null) {
                        id = R.id.btnUndo;
                        TextView btnUndo = (TextView) ViewBindings.findChildViewById(rootView, id);
                        if (btnUndo != null) {
                            id = R.id.hexRecycler;
                            RecyclerView hexRecycler = (RecyclerView) ViewBindings.findChildViewById(rootView, id);
                            if (hexRecycler != null) {
                                id = R.id.hexStatusText;
                                TextView hexStatusText = (TextView) ViewBindings.findChildViewById(rootView, id);
                                if (hexStatusText != null) {
                                    id = R.id.inspectorText;
                                    TextView inspectorText = (TextView) ViewBindings.findChildViewById(rootView, id);
                                    if (inspectorText != null) {
                                        id = R.id.toolbarHex;
                                        Toolbar toolbarHex = (Toolbar) ViewBindings.findChildViewById(rootView, id);
                                        if (toolbarHex != null) {
                                            return new ActivityHexEditorBinding((LinearLayout) rootView, btnFindNext, btnGoto, btnSave, btnSearch, btnUndo, hexRecycler, hexStatusText, inspectorText, toolbarHex);
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
