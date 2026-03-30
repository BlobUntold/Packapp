package com.example.pack.ui.gear;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.SearchView;
import android.widget.Toast;

import com.example.pack.R;
import com.example.pack.data.models.GearItem;
import com.example.pack.ui.backpack.BackpackListActivity;
import com.example.pack.ui.trip.TripListActivity;
import com.example.pack.viewmodel.GearViewModel;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class GearListActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private GearAdapter adapter;
    private GearViewModel gearViewModel;
    private ImageButton btnAddGear;
    private SearchView searchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gear_list);

        BottomNavigationView bottomNav = findViewById(R.id.bottomNav);

        bottomNav.setOnItemSelectedListener(item -> {
            int id = item.getItemId();

            if (id == R.id.nav_gear) {
                // already here
                return true;
            }
            else if (id == R.id.nav_backpacks) {
                startActivity(new Intent(GearListActivity.this, BackpackListActivity.class));
                return true;
            }
            else if (id == R.id.nav_trips) {
                startActivity(new Intent(this, TripListActivity.class));
                return true;
            }

            return false;
        });

// highlight selected tab
        bottomNav.setSelectedItemId(R.id.nav_gear);

        recyclerView = findViewById(R.id.recyclerGear);
        btnAddGear = findViewById(R.id.btnAddGear);
        searchView = findViewById(R.id.searchGear);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new GearAdapter();
        recyclerView.setAdapter(adapter);

        gearViewModel = new ViewModelProvider(this).get(GearViewModel.class);

        gearViewModel.gearList.observe(this, gearItems ->
                adapter.setGearList(gearItems)
        );

        btnAddGear.setOnClickListener(v -> {
            startActivity(new Intent(GearListActivity.this, AddGearActivity.class));
        });

        ImageButton btnBackpacks = findViewById(R.id.btnBackpacks);
        btnBackpacks.setOnClickListener(v ->
                startActivity(new Intent(GearListActivity.this, BackpackListActivity.class))
        );

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                adapter.setGearList(gearViewModel.searchGear(query));
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                adapter.setGearList(gearViewModel.searchGear(newText));
                return true;
            }

        });
        adapter.setOnItemClickListener(item -> {
            Intent i = new Intent(GearListActivity.this, EditGearActivity.class);
            i.putExtra("gear_id", item.id);
            startActivity(i);
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        gearViewModel.loadAllGear();
    }
}
