package com.example.pack.ui.trip;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageButton;

import com.example.pack.R;
import com.example.pack.data.models.Trip;
import com.example.pack.ui.backpack.BackpackListActivity;
import com.example.pack.ui.gear.GearListActivity;
import com.example.pack.viewmodel.TripViewModel;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.List;

public class TripListActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private TripListAdapter adapter;
    private TripViewModel viewModel;
    private ImageButton btnAddTrip;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trip_list);

        BottomNavigationView bottomNav = findViewById(R.id.bottomNav);


        bottomNav.setOnItemSelectedListener(item -> {
            if (item.getItemId() == R.id.nav_gear) {
                startActivity(new Intent(this, GearListActivity.class));
                return true;
            }
            else if (item.getItemId() == R.id.nav_backpacks) {
                startActivity(new Intent(this, BackpackListActivity.class));
                return true;
            }
            else if (item.getItemId() == R.id.nav_trips) {
                return true; // stay here
            }
            return false;
        });

        bottomNav.setSelectedItemId(R.id.nav_trips);   // REQUIRED


        recyclerView = findViewById(R.id.recyclerTrips);
        btnAddTrip = findViewById(R.id.btnAddTrip);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new TripListAdapter();
        recyclerView.setAdapter(adapter);

        viewModel = new ViewModelProvider(this).get(TripViewModel.class);

        viewModel.trips.observe(this, trips ->
                adapter.setTrips(trips)
        );

        btnAddTrip.setOnClickListener(v ->
                startActivity(new Intent(this, CreateTripActivity.class))
        );
        adapter.setOnTripClickListener(trip -> {
            Intent i = new Intent(this, TripDetailsActivity.class);
            i.putExtra("trip_id", trip.id);
            startActivity(i);
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        viewModel.loadAllTrips();
    }
}