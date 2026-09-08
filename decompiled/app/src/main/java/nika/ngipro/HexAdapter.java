package nika.ngipro;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;

/* loaded from: classes4.dex */
public class HexAdapter extends RecyclerView.Adapter<RowHolder> {
    public static final int BYTES_PER_ROW = 16;
    private final byte[] data;
    private int highlightedRow = -1;
    private final OnRowClick listener;

    /* loaded from: classes4.dex */
    public interface OnRowClick {
        void onClick(int i);
    }

    public HexAdapter(byte[] data, OnRowClick listener) {
        this.data = data;
        this.listener = listener;
    }

    public void highlightRow(int row) {
        int old = this.highlightedRow;
        this.highlightedRow = row;
        if (old >= 0) {
            notifyItemChanged(old);
        }
        if (row >= 0) {
            notifyItemChanged(row);
        }
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public RowHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_hex_row, parent, false);
        return new RowHolder(v);
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public void onBindViewHolder(RowHolder holder, final int position) {
        int start = position * 16;
        int end = Math.min(start + 16, this.data.length);
        StringBuilder hex = new StringBuilder();
        StringBuilder ascii = new StringBuilder();
        int i = start;
        while (true) {
            if (i >= start + 16) {
                break;
            }
            if (i < end) {
                int b = this.data[i] & 255;
                hex.append(String.format("%02X ", Integer.valueOf(b)));
                ascii.append((b < 32 || b >= 127) ? '.' : (char) b);
            } else {
                hex.append("   ");
                ascii.append(' ');
            }
            i++;
        }
        String row = String.format("%08X  %s %s", Integer.valueOf(start), hex.toString(), ascii.toString());
        holder.text.setText(row);
        holder.text.setBackgroundColor(position == this.highlightedRow ? Color.parseColor("#3300D4FF") : 0);
        holder.itemView.setOnClickListener(new View.OnClickListener() { // from class: nika.ngipro.HexAdapter$$ExternalSyntheticLambda0
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                HexAdapter.this.m1735lambda$onBindViewHolder$0$nikangiproHexAdapter(position, view);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: lambda$onBindViewHolder$0$nika-ngipro-HexAdapter, reason: not valid java name */
    public /* synthetic */ void m1735lambda$onBindViewHolder$0$nikangiproHexAdapter(int position, View v) {
        this.listener.onClick(position);
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public int getItemCount() {
        return (int) Math.ceil(this.data.length / 16.0d);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes4.dex */
    public static class RowHolder extends RecyclerView.ViewHolder {
        TextView text;

        RowHolder(View itemView) {
            super(itemView);
            this.text = (TextView) itemView;
        }
    }
}
