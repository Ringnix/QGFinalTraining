package com.example.translator;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.ViewHolder> {
    public void updateHistoryList(List<HistoryInfo> historyList) {
        this.historyList = historyList;
        notifyDataSetChanged();
    }
    private List<HistoryInfo> historyList;

    public HistoryAdapter(List<HistoryInfo> historyList) {
        this.historyList = historyList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.singleword, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        HistoryInfo historyInfo = historyList.get(position);
        holder.textViewOriginal.setText(historyInfo.getSrc());
        holder.textViewTranslated.setText(historyInfo.getDst());
        holder.eyeImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (holder.textViewTranslated.getVisibility() == View.GONE) {
                    holder.textViewTranslated.setVisibility(View.VISIBLE);
                    holder.eyeImageView.setImageResource(R.drawable.eye); // 显示eye图像
                } else {
                    holder.textViewTranslated.setVisibility(View.GONE);
                    holder.eyeImageView.setImageResource(R.drawable.uneye); // 显示uneye图像
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return historyList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView textViewOriginal;
        public TextView textViewTranslated;

        private ImageView eyeImageView;
        public ViewHolder(View itemView) {
            super(itemView);
            textViewOriginal = itemView.findViewById(R.id.oringinal);
            textViewTranslated = itemView.findViewById(R.id.translated);
            eyeImageView = itemView.findViewById(R.id.button1);
        }
    }

}
