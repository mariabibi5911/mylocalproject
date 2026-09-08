package nika.ngipro.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewbinding.ViewBinding;
import androidx.viewbinding.ViewBindings;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import nika.ngipro.R;

/* loaded from: classes5.dex */
public final class ActivityBookmarksBinding implements ViewBinding {
    public final TextView bookmarksEmpty;
    public final RecyclerView bookmarksRecycler;
    public final FloatingActionButton btnAddBookmark;
    private final FrameLayout rootView;
    public final Toolbar toolbarBookmarks;

    private ActivityBookmarksBinding(FrameLayout rootView, TextView bookmarksEmpty, RecyclerView bookmarksRecycler, FloatingActionButton btnAddBookmark, Toolbar toolbarBookmarks) {
        this.rootView = rootView;
        this.bookmarksEmpty = bookmarksEmpty;
        this.bookmarksRecycler = bookmarksRecycler;
        this.btnAddBookmark = btnAddBookmark;
        this.toolbarBookmarks = toolbarBookmarks;
    }

    @Override // androidx.viewbinding.ViewBinding
    public FrameLayout getRoot() {
        return this.rootView;
    }

    public static ActivityBookmarksBinding inflate(LayoutInflater inflater) {
        return inflate(inflater, null, false);
    }

    public static ActivityBookmarksBinding inflate(LayoutInflater inflater, ViewGroup parent, boolean attachToParent) {
        View root = inflater.inflate(R.layout.activity_bookmarks, parent, false);
        if (attachToParent) {
            parent.addView(root);
        }
        return bind(root);
    }

    public static ActivityBookmarksBinding bind(View rootView) {
        int id = R.id.bookmarksEmpty;
        TextView bookmarksEmpty = (TextView) ViewBindings.findChildViewById(rootView, id);
        if (bookmarksEmpty != null) {
            id = R.id.bookmarksRecycler;
            RecyclerView bookmarksRecycler = (RecyclerView) ViewBindings.findChildViewById(rootView, id);
            if (bookmarksRecycler != null) {
                id = R.id.btnAddBookmark;
                FloatingActionButton btnAddBookmark = (FloatingActionButton) ViewBindings.findChildViewById(rootView, id);
                if (btnAddBookmark != null) {
                    id = R.id.toolbarBookmarks;
                    Toolbar toolbarBookmarks = (Toolbar) ViewBindings.findChildViewById(rootView, id);
                    if (toolbarBookmarks != null) {
                        return new ActivityBookmarksBinding((FrameLayout) rootView, bookmarksEmpty, bookmarksRecycler, btnAddBookmark, toolbarBookmarks);
                    }
                }
            }
        }
        String missingId = rootView.getResources().getResourceName(id);
        throw new NullPointerException("Missing required view with ID: ".concat(missingId));
    }
}
