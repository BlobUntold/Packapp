package com.example.pack.ui.gear;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pack.R;
import com.example.pack.data.models.GearItem;

import java.util.ArrayList;
import java.util.List;

public class GearAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int TYPE_ITEM = 0;
    private static final int TYPE_WARNING_FOOTER = 1;

    private List<GearItem> gearList = new ArrayList<>();
    private List<String> warnings = new ArrayList<>();

    public void setWarnings(List<String> warnings) {
        this.warnings = warnings != null ? warnings : new ArrayList<>();
        notifyDataSetChanged();
    }

    public interface OnItemClickListener {
        void onItemClick(GearItem item);
    }

    private OnItemClickListener listener;

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    public void setGearList(List<GearItem> list) {
        this.gearList = list;
        notifyDataSetChanged();
    }

    @Override
    public int getItemViewType(int position) {
        if (position < gearList.size()) return TYPE_ITEM;
        return TYPE_WARNING_FOOTER;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == TYPE_ITEM) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_gear, parent, false);
            return new GearViewHolder(view);
        } else {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_packing_warning_footer, parent, false);
            return new WarningFooterViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof GearViewHolder) {
            GearItem item = gearList.get(position);
            GearViewHolder gearHolder = (GearViewHolder) holder;
            gearHolder.txtName.setText(item.name);
            gearHolder.txtDetails.setText(item.weight + " g | " + item.volume + " L");
            gearHolder.itemView.setOnClickListener(v -> {
                if (listener != null) listener.onItemClick(item);
            });
        } else if (holder instanceof WarningFooterViewHolder) {
            WarningFooterViewHolder warningHolder = (WarningFooterViewHolder) holder;
            warningHolder.warningTitle.setVisibility(View.VISIBLE);
            StringBuilder sb = new StringBuilder();
            for (String w : warnings) {
                sb.append("\u26A0 ").append(w).append("\n");
            }
            warningHolder.warningList.setText(sb.toString().trim());
        }
    }

    @Override
    public int getItemCount() {
        return gearList.size() + (warnings != null && !warnings.isEmpty() ? 1 : 0);
    }

    static class GearViewHolder extends RecyclerView.ViewHolder {
        TextView txtName, txtDetails;
        public GearViewHolder(@NonNull View itemView) {
            super(itemView);
            txtName = itemView.findViewById(R.id.gearName);
            txtDetails = itemView.findViewById(R.id.gearDetails);
        }
    }

    static class WarningFooterViewHolder extends RecyclerView.ViewHolder {
        TextView warningTitle;
        TextView warningList;
        public WarningFooterViewHolder(@NonNull View itemView) {
            super(itemView);
            warningTitle = itemView.findViewById(R.id.warning_title);
            warningList = itemView.findViewById(R.id.warning_list);
        }
    }
}
