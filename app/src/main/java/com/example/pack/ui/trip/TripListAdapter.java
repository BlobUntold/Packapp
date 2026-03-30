package com.example.pack.ui.trip;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pack.R;
import com.example.pack.data.models.Trip;

import java.util.ArrayList;
import java.util.List;

public class TripListAdapter extends RecyclerView.Adapter<TripListAdapter.TripVH> {

    public interface OnTripClickListener {
        void onTripClick(Trip trip);
    }

    private OnTripClickListener listener;
    private List<Trip> trips = new ArrayList<>();

    public void setOnTripClickListener(OnTripClickListener l) {
        this.listener = l;
    }

    public void setTrips(List<Trip> tripList) {
        this.trips = tripList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public TripVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_trip, parent, false);
        return new TripVH(v);
    }

    @Override
    public void onBindViewHolder(@NonNull TripVH holder, int position) {
        Trip t = trips.get(position);

        String title = t.environment + " • " + t.weather;
        String details = "Duration: " + t.duration + " days";

        holder.txtTitle.setText(title);
        holder.txtDetails.setText(details);

        holder.itemView.setOnClickListener(v -> {
            if (listener != null) listener.onTripClick(t);
        });
    }

    @Override
    public int getItemCount() {
        return trips.size();
    }

    static class TripVH extends RecyclerView.ViewHolder {

        TextView txtTitle, txtDetails;

        TripVH(@NonNull View itemView) {
            super(itemView);
            txtTitle = itemView.findViewById(R.id.txtTripTitle);
            txtDetails = itemView.findViewById(R.id.txtTripDetails);
        }
    }
}
