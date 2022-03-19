package com.learntodroid.androidminesweeper;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ReciclaTablero extends RecyclerView.Adapter<ReciclaTablero.MineTileViewHolder> {
    private List<Celda> celdas;
    private OnCellClickListener listener;

    public ReciclaTablero(List<Celda> celdas, OnCellClickListener listener) {
        this.celdas = celdas;
        this.listener = listener;
    }

    @NonNull
    @Override
    public MineTileViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_cell, parent, false);
        return new MineTileViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MineTileViewHolder holder, int position) {
        holder.bind(celdas.get(position));
        holder.setIsRecyclable(false);
    }

    @Override
    public int getItemCount() {
        return celdas.size();
    }

    public void setCells(List<Celda> celdas) {
        this.celdas = celdas;
        notifyDataSetChanged();
    }

    class MineTileViewHolder extends RecyclerView.ViewHolder {
        TextView valueTextView;

        public MineTileViewHolder(@NonNull View itemView) {
            super(itemView);

            valueTextView = itemView.findViewById(R.id.item_cell_value);
        }

        public void bind(final Celda celda) {
            itemView.setBackgroundColor(Color.GRAY);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listener.cellClick(celda);
                }
            });

            if (celda.isRevealed()) {
                if (celda.getValue() == Celda.BOMB) {
                    valueTextView.setText(R.string.bomb);
                } else if (celda.getValue() == Celda.BLANK) {
                    valueTextView.setText("");
                    itemView.setBackgroundColor(Color.WHITE);
                } else {
                    valueTextView.setText(String.valueOf(celda.getValue()));
                    if (celda.getValue() == 1) {
                        valueTextView.setTextColor(Color.BLACK);
                    } else if (celda.getValue() == 2) {
                        valueTextView.setTextColor(Color.BLACK);
                    } else if (celda.getValue() == 3) {
                        valueTextView.setTextColor(Color.BLACK);
                    }
                }
            } else if (celda.isFlagged()) {
                valueTextView.setText(R.string.flag);
            }
        }
    }
}
