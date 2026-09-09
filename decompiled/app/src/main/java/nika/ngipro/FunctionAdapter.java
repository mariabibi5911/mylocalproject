package nika.ngipro;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;
import java.util.Locale;

/* loaded from: classes4.dex */
public class FunctionAdapter extends RecyclerView.Adapter<FunctionAdapter.FuncHolder> {
    private final List<FunctionEntry> items;
    private final OnFunctionClick listener;

    /* loaded from: classes4.dex */
    public static class FunctionEntry {
        public long address;
        public String name;
        public long size;
    }

    /* loaded from: classes4.dex */
    public interface OnFunctionClick {
        void onClick(FunctionEntry functionEntry);
    }

    public FunctionAdapter(List<FunctionEntry> items, OnFunctionClick listener) {
        this.items = items;
        this.listener = listener;
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public FuncHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_function, parent, false);
        return new FuncHolder(v);
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public void onBindViewHolder(FuncHolder holder, int position) {
        final FunctionEntry e = this.items.get(position);
        holder.name.setText(e.name);
        holder.meta.setText(String.format(Locale.US, "0x%08X  •  size %d", Long.valueOf(e.address), Long.valueOf(e.size)));
        holder.itemView.setOnClickListener(new View.OnClickListener() { // from class: nika.ngipro.FunctionAdapter$$ExternalSyntheticLambda0
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                FunctionAdapter.this.m1734lambda$onBindViewHolder$0$nikangiproFunctionAdapter(e, view);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: lambda$onBindViewHolder$0$nika-ngipro-FunctionAdapter, reason: not valid java name */
    public /* synthetic */ void m1734lambda$onBindViewHolder$0$nikangiproFunctionAdapter(FunctionEntry e, View v) {
        this.listener.onClick(e);
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public int getItemCount() {
        return this.items.size();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes4.dex */
    public static class FuncHolder extends RecyclerView.ViewHolder {
        TextView meta;
        TextView name;

        FuncHolder(View itemView) {
            super(itemView);
            this.name = (TextView) itemView.findViewById(R.id.funcName);
            this.meta = (TextView) itemView.findViewById(R.id.funcMeta);
        }
    }
}
