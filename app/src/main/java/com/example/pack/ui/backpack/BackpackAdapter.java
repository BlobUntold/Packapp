package com.example.pack.ui.backpack;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pack.R;
import com.example.pack.data.models.Backpack;

import java.util.ArrayList;
import java.util.List;

public class BackpackAdapter extends RecyclerView.Adapter<BackpackAdapter.BackpackVH> {

    public interface OnClick {
        void onItemClick(Backpack b);
    }

    private OnClick listener;
    private List<Backpack> list = new ArrayList<>();

    public void setOnItemClickListener(OnClick l) { listener = l; }

    public void setBackpackList(List<Backpack> l) {
        list = l;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public BackpackVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_backpack, parent, false);
        return new BackpackVH(v);
    }

    @Override
    public void onBindViewHolder(@NonNull BackpackVH holder, int pos) {
        Backpack b = list.get(pos);

        holder.txtName.setText(b.name);
        holder.txtDetails.setText(b.maxVolume + " L  |  " + b.maxWeight + " kg");

        holder.itemView.setOnClickListener(v -> {
            if (listener != null) listener.onItemClick(b);
        });
    }

    @Override
    public int getItemCount() { return list.size(); }

    static class BackpackVH extends RecyclerView.ViewHolder {
        TextView txtName, txtDetails;
        BackpackVH(View v) {
            super(v);
            txtName = v.findViewById(R.id.txtBackpackName);
            txtDetails = v.findViewById(R.id.txtBackpackDetails);
        }
    }
}
