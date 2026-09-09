package nika.ngipro;

import android.content.res.ColorStateList;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;

/* loaded from: classes4.dex */
public class ResultsAdapter extends RecyclerView.Adapter<ResultsAdapter.ResultViewHolder> {
    private final List<String> lines = new ArrayList();

    public void submit(String rawText) {
        this.lines.clear();
        if (rawText != null) {
            for (String line : rawText.split("\n")) {
                if (!line.trim().isEmpty()) {
                    this.lines.add(line);
                }
            }
        }
        notifyDataSetChanged();
    }

    public void clear() {
        this.lines.clear();
        notifyDataSetChanged();
    }

    public boolean isEmpty() {
        return this.lines.isEmpty();
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public ResultViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_result, parent, false);
        return new ResultViewHolder(v);
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public void onBindViewHolder(ResultViewHolder holder, int position) {
        int color;
        String line = this.lines.get(position);
        holder.text.setText(line.replaceFirst("^(>>>|\\[\\+\\]|\\[-\\])\\s*", ""));
        if (line.contains("[-]") || line.toLowerCase().contains("error") || line.toLowerCase().contains("fail")) {
            color = -47776;
        } else if (line.contains("[+]") || line.toLowerCase().contains("success") || line.toLowerCase().contains("done")) {
            color = -16718432;
        } else {
            color = -16722689;
        }
        holder.dot.setBackgroundTintList(ColorStateList.valueOf(color));
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public int getItemCount() {
        return this.lines.size();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes4.dex */
    public static class ResultViewHolder extends RecyclerView.ViewHolder {
        View dot;
        TextView text;

        ResultViewHolder(View itemView) {
            super(itemView);
            this.text = (TextView) itemView.findViewById(R.id.resultText);
            this.dot = itemView.findViewById(R.id.resultDot);
        }
    }
}
