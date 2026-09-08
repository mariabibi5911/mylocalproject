package nika.ngipro;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

/* loaded from: classes4.dex */
public class ToolsAdapter extends RecyclerView.Adapter<ToolViewHolder> {
    private final List<ToolItem> items;
    private final OnToolClick listener;

    /* loaded from: classes4.dex */
    public interface OnToolClick {
        void onClick(int i);
    }

    /* loaded from: classes4.dex */
    public static class ToolItem {
        public final int iconRes;
        public final int id;
        public final String label;

        public ToolItem(int id, int iconRes, String label) {
            this.id = id;
            this.iconRes = iconRes;
            this.label = label;
        }
    }

    public ToolsAdapter(List<ToolItem> items, OnToolClick listener) {
        this.items = items;
        this.listener = listener;
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public ToolViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_tool, parent, false);
        return new ToolViewHolder(v);
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public void onBindViewHolder(ToolViewHolder holder, int position) {
        final ToolItem item = this.items.get(position);
        holder.icon.setImageResource(item.iconRes);
        holder.label.setText(item.label);
        holder.itemView.setOnClickListener(new View.OnClickListener() { // from class: nika.ngipro.ToolsAdapter$$ExternalSyntheticLambda0
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                ToolsAdapter.this.m1820lambda$onBindViewHolder$0$nikangiproToolsAdapter(item, view);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: lambda$onBindViewHolder$0$nika-ngipro-ToolsAdapter, reason: not valid java name */
    public /* synthetic */ void m1820lambda$onBindViewHolder$0$nikangiproToolsAdapter(ToolItem item, View v) {
        this.listener.onClick(item.id);
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public int getItemCount() {
        return this.items.size();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes4.dex */
    public static class ToolViewHolder extends RecyclerView.ViewHolder {
        ImageView icon;
        TextView label;

        ToolViewHolder(View itemView) {
            super(itemView);
            this.icon = (ImageView) itemView.findViewById(R.id.toolIcon);
            this.label = (TextView) itemView.findViewById(R.id.toolLabel);
        }
    }
}
