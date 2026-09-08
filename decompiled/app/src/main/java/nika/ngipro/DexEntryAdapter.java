package nika.ngipro;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

/* loaded from: classes4.dex */
public class DexEntryAdapter extends RecyclerView.Adapter<VH> {
    private final List<DexEntry> items;
    private final OnEntryClick listener;

    /* loaded from: classes4.dex */
    public static class DexEntry {
        public int instructionCount;
        public int methodIndex;
        public String signature;
    }

    /* loaded from: classes4.dex */
    public interface OnEntryClick {
        void onClick(DexEntry dexEntry);
    }

    public DexEntryAdapter(List<DexEntry> items, OnEntryClick listener) {
        this.items = items;
        this.listener = listener;
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public VH onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_function, parent, false);
        return new VH(v);
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public void onBindViewHolder(VH holder, int position) {
        final DexEntry e = this.items.get(position);
        holder.name.setText(e.signature);
        holder.meta.setText(e.instructionCount + " instructions");
        holder.itemView.setOnClickListener(new View.OnClickListener() { // from class: nika.ngipro.DexEntryAdapter$$ExternalSyntheticLambda0
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                DexEntryAdapter.this.m1733lambda$onBindViewHolder$0$nikangiproDexEntryAdapter(e, view);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: lambda$onBindViewHolder$0$nika-ngipro-DexEntryAdapter, reason: not valid java name */
    public /* synthetic */ void m1733lambda$onBindViewHolder$0$nikangiproDexEntryAdapter(DexEntry e, View v) {
        this.listener.onClick(e);
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public int getItemCount() {
        return this.items.size();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes4.dex */
    public static class VH extends RecyclerView.ViewHolder {
        TextView meta;
        TextView name;

        VH(View itemView) {
            super(itemView);
            this.name = (TextView) itemView.findViewById(R.id.funcName);
            this.meta = (TextView) itemView.findViewById(R.id.funcMeta);
        }
    }
}
