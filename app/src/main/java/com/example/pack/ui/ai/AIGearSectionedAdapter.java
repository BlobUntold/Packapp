package com.example.pack.ui.ai;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pack.R;

import java.util.ArrayList;
import java.util.List;

public class AIGearSectionedAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int TYPE_HEADER = 0;
    private static final int TYPE_ITEM = 1;
    private static final int TYPE_WARNING_FOOTER = 2;

    private List<String> essential;
    private List<String> optional;
    private List<String> other;
    private List<String> warnings = new ArrayList<>();

    // Combined rows (header + items)
    private List<Row> rows = new ArrayList<>();

    private static class Row {
        int type;       // header or item
        String text;    // item name or header title
        int section;    // 0 = essential, 1 = optional, 2 = other
    }

    public interface OnSectionChangedListener {
        void onSectionsUpdated();
    }

    private OnSectionChangedListener listener;

    public void setOnSectionChangedListener(OnSectionChangedListener l) {
        this.listener = l;
    }

    public AIGearSectionedAdapter(List<String> essential,
                                  List<String> optional,
                                  List<String> other,
                                  List<String> warnings) {
        this.essential = essential;
        this.optional = optional;
        this.other = other;
        if (warnings != null) this.warnings = warnings;
        rebuildRows();
    }

    private void rebuildRows() {
        rows.clear();
        addSection("Essential", essential, 0);
        addSection("Optional", optional, 1);
        addSection("Other", other, 2);
        // Add warning footer if warnings exist
        if (warnings != null && !warnings.isEmpty()) {
            Row warningFooter = new Row();
            warningFooter.type = TYPE_WARNING_FOOTER;
            warningFooter.text = null;
            warningFooter.section = -1;
            rows.add(warningFooter);
        }
        notifyDataSetChanged();
    }

    private void addSection(String title, List<String> items, int sectionIndex) {
        Row header = new Row();
        header.type = TYPE_HEADER;
        header.text = title;
        header.section = sectionIndex;
        rows.add(header);

        for (String name : items) {
            Row r = new Row();
            r.type = TYPE_ITEM;
            r.text = name;
            r.section = sectionIndex;
            rows.add(r);
        }
    }

    @Override
    public int getItemViewType(int position) {
        return rows.get(position).type;
    }

    @Override
    public int getItemCount() {
        return rows.size();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int type) {
        if (type == TYPE_HEADER) {
            View v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_ai_header, parent, false);
            return new HeaderHolder(v);
        } else if (type == TYPE_WARNING_FOOTER) {
            View v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_ai_warning_footer, parent, false);
            return new WarningFooterHolder(v);
        } else {
            View v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_ai_gear, parent, false);
            return new ItemHolder(v);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holderBase, int pos) {
        Row r = rows.get(pos);

        if (r.type == TYPE_HEADER) {
            HeaderHolder holder = (HeaderHolder) holderBase;
            holder.title.setText(r.text);
            if (r.section == 0) holder.icon.setImageResource(R.drawable.ic_essential);
            else if (r.section == 1) holder.icon.setImageResource(R.drawable.ic_optional);
            else holder.icon.setImageResource(R.drawable.ic_other);
        } else if (r.type == TYPE_WARNING_FOOTER) {
            WarningFooterHolder holder = (WarningFooterHolder) holderBase;
            holder.warningTitle.setVisibility(View.VISIBLE);
            StringBuilder sb = new StringBuilder();
            for (String w : warnings) {
                sb.append("\u26A0 ").append(w).append("\n");
            }
            holder.warningList.setText(sb.toString().trim());
        } else {
            ItemHolder holder = (ItemHolder) holderBase;
            holder.name.setText(r.text);
            holder.icon.setImageResource(R.drawable.ic_gear);
        }
    }

    static class HeaderHolder extends RecyclerView.ViewHolder {
        TextView title;
        ImageView icon;

        HeaderHolder(View v) {
            super(v);
            title = v.findViewById(R.id.header_text);
            icon = v.findViewById(R.id.header_icon);
        }
    }

    static class ItemHolder extends RecyclerView.ViewHolder {
        TextView name;
        ImageView icon;

        ItemHolder(View v) {
            super(v);
            name = v.findViewById(R.id.gear_name);
            icon = v.findViewById(R.id.icon_item);
        }
    }

    static class WarningFooterHolder extends RecyclerView.ViewHolder {
        TextView warningTitle;
        TextView warningList;
        WarningFooterHolder(View v) {
            super(v);
            warningTitle = v.findViewById(R.id.warning_title);
            warningList = v.findViewById(R.id.warning_list);
        }
    }

    // Drag and drop callback
    public ItemTouchHelper.Callback getTouchHelperCallback() {
        return new ItemTouchHelper.SimpleCallback(
                ItemTouchHelper.UP | ItemTouchHelper.DOWN,
                0
        ) {

            @Override
            public boolean isLongPressDragEnabled() {
                return true;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {}

            @Override
            public boolean onMove(@NonNull RecyclerView rv,
                                  @NonNull RecyclerView.ViewHolder vh,
                                  @NonNull RecyclerView.ViewHolder target) {

                int from = vh.getAdapterPosition();
                int to = target.getAdapterPosition();

                // Defensive: Check bounds
                if (from < 0 || from >= rows.size() || to < 0 || to >= rows.size()) return false;

                Row moving = rows.get(from);
                Row targetRow = rows.get(to);

                // Prevent dragging headers or dropping on headers
                if (moving.type == TYPE_HEADER || targetRow.type == TYPE_HEADER) {
                    return false;
                }

                // Move item in rows
                rows.remove(from);
                rows.add(to, moving);
                notifyItemMoved(from, to);

                return true;
            }

            @Override
            public void clearView(@NonNull RecyclerView rv,
                                  @NonNull RecyclerView.ViewHolder vh) {
                super.clearView(rv, vh);

                int pos = vh.getAdapterPosition();
                if (pos == RecyclerView.NO_POSITION)
                    return;

                Row r = rows.get(pos);
                r.section = findSectionForPosition(pos);

                rebuildSectionLists();
                rebuildRows();
            }
        };
    }

    private int findSectionForPosition(int pos) {
        for (int i = pos; i >= 0; i--) {
            Row r = rows.get(i);
            if (r.type == TYPE_HEADER) return r.section;
        }
        return 0;
    }

    private void rebuildSectionLists() {
        essential.clear();
        optional.clear();
        other.clear();

        for (Row r : rows) {
            if (r.type == TYPE_ITEM) {
                if (r.section == 0) essential.add(r.text);
                else if (r.section == 1) optional.add(r.text);
                else other.add(r.text);
            }
        }

        if (listener != null)
            listener.onSectionsUpdated();
    }

    public List<String> getEssentialItems() { return essential; }
    public List<String> getOptionalItems() { return optional; }
    public List<String> getOtherItems() { return other; }
    public List<String> getWarnings() { return warnings; }
}
