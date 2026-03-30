package com.example.pack.ui.ai;

import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pack.R;

import java.util.Collections;
import java.util.List;

public class AIGearAdapter extends RecyclerView.Adapter<AIGearAdapter.ViewHolder> {

    private List<String> gearList;
    private final OnStartDragListener onStartDragListener;

    public interface OnStartDragListener {
        void onStartDrag(RecyclerView.ViewHolder viewHolder);
    }

    public AIGearAdapter(List<String> gearList, OnStartDragListener onStartDragListener) {
        this.gearList = gearList;
        this.onStartDragListener = onStartDragListener;
    }

    @NonNull
    @Override
    public AIGearAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_ai_gear, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull AIGearAdapter.ViewHolder holder, int position) {

        holder.name.setText(gearList.get(position));

        // ⭐ DRAG HANDLE TOUCH LOGIC (FIXED)
        holder.dragHandle.setOnTouchListener((v, event) -> {
            if (event.getActionMasked() == MotionEvent.ACTION_DOWN) {
                onStartDragListener.onStartDrag(holder);
            }
            return false;
        });
    }

    @Override
    public int getItemCount() {
        return gearList.size();
    }

    // ⭐ Drag reorder support
    public void onItemMove(int from, int to) {
        if (from < to) {
            for (int i = from; i < to; i++) {
                Collections.swap(gearList, i, i + 1);
            }
        } else {
            for (int i = from; i > to; i--) {
                Collections.swap(gearList, i, i - 1);
            }
        }
        notifyItemMoved(from, to);
    }

    public void removeItemByName(String name) {
        int index = gearList.indexOf(name);
        if (index != -1) {
            gearList.remove(index);
            notifyItemRemoved(index);
        }
    }

    public void addItem(int position, String item) {
        gearList.add(position, item);
        notifyItemInserted(position);
    }

    public List<String> getGearList() {
        return gearList;
    }

    // -------------------------------------------------------------------------
    // ViewHolder
    // -------------------------------------------------------------------------
    public static class ViewHolder extends RecyclerView.ViewHolder {

        TextView name;
        ImageView dragHandle;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.gear_name);
            //dragHandle = itemView.findViewById(R.id.drag_handle);
        }
    }
}

