package nika.ngipro;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;
import java.util.Locale;
import nika.ngipro.BookmarkManager;

/* loaded from: classes4.dex */
public class BookmarkAdapter extends RecyclerView.Adapter<BookmarkAdapter.VH> {
    private final List<BookmarkManager.Bookmark> items;
    private final OnClick listener;

    /* loaded from: classes4.dex */
    public interface OnClick {
        void onDelete(int i);

        void onOpen(int i);
    }

    public BookmarkAdapter(List<BookmarkManager.Bookmark> items, OnClick listener) {
        this.items = items;
        this.listener = listener;
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public VH onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_bookmark, parent, false);
        return new VH(v);
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public void onBindViewHolder(VH holder, final int position) {
        BookmarkManager.Bookmark b = this.items.get(position);
        holder.label.setText((b.label == null || b.label.isEmpty()) ? "(no label)" : b.label);
        holder.meta.setText(String.format(Locale.US, "0x%08X", Long.valueOf(b.address)));
        holder.note.setText(b.note == null ? "" : b.note);
        holder.note.setVisibility((b.note == null || b.note.isEmpty()) ? 8 : 0);
        holder.itemView.setOnClickListener(new View.OnClickListener() { // from class: nika.ngipro.BookmarkAdapter$$ExternalSyntheticLambda0
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                BookmarkAdapter.this.m1719lambda$onBindViewHolder$0$nikangiproBookmarkAdapter(position, view);
            }
        });
        holder.delete.setOnClickListener(new View.OnClickListener() { // from class: nika.ngipro.BookmarkAdapter$$ExternalSyntheticLambda1
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                BookmarkAdapter.this.m1720lambda$onBindViewHolder$1$nikangiproBookmarkAdapter(position, view);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: lambda$onBindViewHolder$0$nika-ngipro-BookmarkAdapter, reason: not valid java name */
    public /* synthetic */ void m1719lambda$onBindViewHolder$0$nikangiproBookmarkAdapter(int position, View v) {
        this.listener.onOpen(position);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: lambda$onBindViewHolder$1$nika-ngipro-BookmarkAdapter, reason: not valid java name */
    public /* synthetic */ void m1720lambda$onBindViewHolder$1$nikangiproBookmarkAdapter(int position, View v) {
        this.listener.onDelete(position);
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public int getItemCount() {
        return this.items.size();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes4.dex */
    public static class VH extends RecyclerView.ViewHolder {
        ImageView delete;
        TextView label;
        TextView meta;
        TextView note;

        VH(View itemView) {
            super(itemView);
            this.label = (TextView) itemView.findViewById(R.id.bmLabel);
            this.meta = (TextView) itemView.findViewById(R.id.bmMeta);
            this.note = (TextView) itemView.findViewById(R.id.bmNote);
            this.delete = (ImageView) itemView.findViewById(R.id.bmDelete);
        }
    }
}
