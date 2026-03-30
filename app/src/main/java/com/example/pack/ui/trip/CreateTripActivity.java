package com.example.pack.ui.trip;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.pack.R;
import com.example.pack.data.models.Trip;
import com.example.pack.viewmodel.TripViewModel;

public class CreateTripActivity extends AppCompatActivity {

    private Spinner spinnerTerrain, spinnerWeather, spinnerDifficulty,
            spinnerCampsite, spinnerCookMethod, spinnerExperience,
            spinnerNavigation, spinnerCoverage;

    private EditText editDuration, editMinTemp, editMaxTemp, editPrecipChance,
            editWindSpeed, editDistance, editElevation,
            editNights, editGroupSize;

    private Button btnNext;
    private ImageButton btnBack;

    private TripViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_trip);

        viewModel = new ViewModelProvider(this).get(TripViewModel.class);

        btnBack = findViewById(R.id.btnBack);
        spinnerTerrain = findViewById(R.id.spinnerTerrain);
        spinnerWeather = findViewById(R.id.spinnerWeather);
        spinnerDifficulty = findViewById(R.id.spinnerDifficulty);
        spinnerCampsite = findViewById(R.id.spinnerCampsite);
        spinnerCookMethod = findViewById(R.id.spinnerCookMethod);
        spinnerExperience = findViewById(R.id.spinnerExperience);
        spinnerNavigation = findViewById(R.id.spinnerNavigation);
        spinnerCoverage = findViewById(R.id.spinnerCoverage);

        editDuration = findViewById(R.id.editDuration);
        editMinTemp = findViewById(R.id.editMinTemp);
        editMaxTemp = findViewById(R.id.editMaxTemp);
        editPrecipChance = findViewById(R.id.editPrecipChance);
        editWindSpeed = findViewById(R.id.editWindSpeed);
        editDistance = findViewById(R.id.editDistance);
        editElevation = findViewById(R.id.editElevation);
        editNights = findViewById(R.id.editNights);
        editGroupSize = findViewById(R.id.editGroupSize);
        btnNext = findViewById(R.id.btnNext);

        btnBack.setOnClickListener(v -> finish());

        // Load spinners
        setupSpinner(spinnerTerrain, R.array.terrain_types);
        setupSpinner(spinnerWeather, R.array.weather_types);
        setupSpinner(spinnerDifficulty, R.array.terrain_difficulty);
        setupSpinner(spinnerCampsite, R.array.campsite_types);
        setupSpinner(spinnerCookMethod, R.array.cook_methods);
        setupSpinner(spinnerExperience, R.array.experience_levels);
        setupSpinner(spinnerNavigation, R.array.navigation_methods);
        setupSpinner(spinnerCoverage, R.array.cell_coverage_levels);

        btnNext.setOnClickListener(v -> saveTrip());
    }

    private void setupSpinner(Spinner spinner, int arrayRes) {
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                this, arrayRes, android.R.layout.simple_spinner_item
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
    }

    // Safe parsing with default fallback
    private double parseDouble(EditText edit, double fallback) {
        String txt = edit.getText().toString().trim();
        if (txt.isEmpty()) return fallback;
        try {
            return Double.parseDouble(txt);
        } catch (Exception e) {
            return fallback;
        }
    }

    private int parseInt(EditText edit, int fallback) {
        String txt = edit.getText().toString().trim();
        if (txt.isEmpty()) return fallback;
        try {
            return Integer.parseInt(txt);
        } catch (Exception e) {
            return fallback;
        }
    }

    private void saveTrip() {

        // REQUIRED: duration, terrain, weather
        String durationStr = editDuration.getText().toString().trim();
        if (durationStr.isEmpty()) {
            Toast.makeText(this, "Duration is required", Toast.LENGTH_SHORT).show();
            return;
        }

        Trip trip = new Trip();

        trip.environment = spinnerTerrain.getSelectedItem().toString();
        trip.weather = spinnerWeather.getSelectedItem().toString();
        trip.duration = Integer.parseInt(durationStr);

        // Optional fields (safe parsing)
        trip.minTemp = parseDouble(editMinTemp, 0);
        trip.maxTemp = parseDouble(editMaxTemp, 0);
        trip.precipChance = parseInt(editPrecipChance, 0);
        trip.windSpeed = parseDouble(editWindSpeed, 0);

        trip.distanceKm = parseDouble(editDistance, 0);
        trip.elevationGain = parseDouble(editElevation, 0);
        trip.terrainDifficulty = spinnerDifficulty.getSelectedItem().toString();

        trip.nights = parseInt(editNights, 0);
        trip.campsiteType = spinnerCampsite.getSelectedItem().toString();
        trip.cookMethod = spinnerCookMethod.getSelectedItem().toString();

        trip.groupSize = parseInt(editGroupSize, 1);
        trip.sharedGearAllowed = (trip.groupSize > 1);

        trip.experienceLevel = spinnerExperience.getSelectedItem().toString();
        trip.fitnessLevel = "Medium"; // You can add this later

        trip.navigationMethod = spinnerNavigation.getSelectedItem().toString();
        trip.cellCoverage = spinnerCoverage.getSelectedItem().toString();
        trip.mapsRequired = trip.navigationMethod.equals("Map & Compass");

        trip.timestamp = System.currentTimeMillis();

        // AI will set these later
        trip.estimatedPackVolume = 0;
        trip.chosenBackpackId = -1;

        viewModel.addTrip(trip);

        Toast.makeText(this, "Trip Saved", Toast.LENGTH_SHORT).show();
        finish();
    }
}
