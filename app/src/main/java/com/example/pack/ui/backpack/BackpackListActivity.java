package com.example.pack.ui.backpack;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pack.R;
import com.example.pack.ui.gear.GearListActivity;
import com.example.pack.ui.trip.TripListActivity;
import com.example.pack.viewmodel.BackpackViewModel;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class BackpackListActivity extends AppCompatActivity {

    private RecyclerView recycler;
    private BackpackAdapter adapter;
    private BackpackViewModel viewModel;
    private ImageButton btnAdd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_backpack_list);

        recycler = findViewById(R.id.recyclerBackpack);
        btnAdd = findViewById(R.id.btnAddBackpack);

        recycler.setLayoutManager(new LinearLayoutManager(this));
        adapter = new BackpackAdapter();
        recycler.setAdapter(adapter);

        viewModel = new ViewModelProvider(this).get(BackpackViewModel.class);

        viewModel.backpacks.observe(this, list ->
                adapter.setBackpackList(list)
        );

        btnAdd.setOnClickListener(v ->
                startActivity(new Intent(this, AddBackpackActivity.class))
        );

        adapter.setOnItemClickListener(item -> {
            Intent i = new Intent(this, EditBackpackActivity.class);
            i.putExtra("backpack_id", item.id);
            startActivity(i);
        });

        // -------------------------------
        // BOTTOM NAVIGATION SETUP (inside onCreate)
        // -------------------------------
        BottomNavigationView bottomNav = findViewById(R.id.bottomNav);

        bottomNav.setOnItemSelectedListener(item -> {
            int id = item.getItemId();

            if (id == R.id.nav_gear) {
                startActivity(new Intent(this, GearListActivity.class));
                return true;
            }
            else if (id == R.id.nav_backpacks) {
                return true; // already here
            }
            else if (id == R.id.nav_trips) {
                startActivity(new Intent(this, TripListActivity.class));
                return true;
            }

            return false;
        });

        bottomNav.setSelectedItemId(R.id.nav_backpacks);
    }

    @Override
    protected void onResume() {
        super.onResume();
        viewModel.loadBackpacks();
    }
}