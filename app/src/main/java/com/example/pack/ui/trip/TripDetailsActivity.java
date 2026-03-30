package com.example.pack.ui.trip;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.pack.R;
import com.example.pack.data.models.Trip;
import com.example.pack.ui.ai.AIGearActivity;
import com.example.pack.viewmodel.TripViewModel;

import java.util.stream.Collectors;

public class TripDetailsActivity extends AppCompatActivity {

    private TextView txtEnvironment, txtWeather, txtDuration, txtTemps, txtPrecip,
            txtWind, txtDistance, txtElevation, txtDifficulty, txtNights,
            txtCampsite, txtCookMethod, txtGroupSize, txtExperience,
            txtNavigation, txtCoverage, txtAIEssentialGear, txtAIOptionalGear, txtAIWarnings;

    private Button btnGenerateAI;
    private ImageButton btnBack;

    private TripViewModel viewModel;
    private Trip trip;
    private int tripId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trip_details);

        System.out.println("DEBUG: TripDetailsActivity loaded tripId=" + tripId);

        tripId = getIntent().getIntExtra("trip_id", -1);

        viewModel = new ViewModelProvider(this).get(TripViewModel.class);
        trip = viewModel.getTrip(tripId);

        btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(v -> finish());

        btnGenerateAI = findViewById(R.id.btnGenerateAI);
        btnGenerateAI.setOnClickListener(v -> {
            Intent i = new Intent(TripDetailsActivity.this, AIGearActivity.class);
            i.putExtra("trip_id", tripId);
            startActivity(i);
        });

        txtEnvironment = findViewById(R.id.txtEnvironment);
        txtWeather = findViewById(R.id.txtWeather);
        txtDuration = findViewById(R.id.txtDuration);
        txtTemps = findViewById(R.id.txtTemps);
        txtPrecip = findViewById(R.id.txtPrecip);
        txtWind = findViewById(R.id.txtWind);
        txtDistance = findViewById(R.id.txtDistance);
        txtElevation = findViewById(R.id.txtElevation);
        txtDifficulty = findViewById(R.id.txtDifficulty);
        txtNights = findViewById(R.id.txtNights);
        txtCampsite = findViewById(R.id.txtCampsite);
        txtCookMethod = findViewById(R.id.txtCookMethod);
        txtGroupSize = findViewById(R.id.txtGroupSize);
        txtExperience = findViewById(R.id.txtExperience);
        txtNavigation = findViewById(R.id.txtNavigation);
        txtCoverage = findViewById(R.id.txtCoverage);
        txtAIEssentialGear = findViewById(R.id.ai_essential_gear_list);
        txtAIOptionalGear = findViewById(R.id.ai_optional_gear_list);
        txtAIWarnings = findViewById(R.id.ai_warnings_list);

        loadTripData();
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Reload data in case it was changed in AIGearActivity
        trip = viewModel.getTrip(tripId);
        loadTripData();
    }

    private void loadTripData() {
        txtEnvironment.setText(trip.environment);
        txtWeather.setText(trip.weather);
        txtDuration.setText(trip.duration + " days");
        txtTemps.setText(trip.minTemp + "° to " + trip.maxTemp + "°");
        txtPrecip.setText(trip.precipChance + "%");
        txtWind.setText(trip.windSpeed + " mph/kmh");
        txtDistance.setText(trip.distanceKm + " km");
        txtElevation.setText(trip.elevationGain + " m");
        txtDifficulty.setText(trip.terrainDifficulty);
        txtNights.setText(trip.nights + " nights");
        txtCampsite.setText(trip.campsiteType);
        txtCookMethod.setText(trip.cookMethod);
        txtGroupSize.setText(String.valueOf(trip.groupSize));
        txtExperience.setText(trip.experienceLevel);
        txtNavigation.setText(trip.navigationMethod);
        txtCoverage.setText(trip.cellCoverage);

        if (trip.aiEssentialGear != null && !trip.aiEssentialGear.isEmpty()) {
            txtAIEssentialGear.setText(trip.aiEssentialGear.stream().collect(Collectors.joining("\n• ", "• ", "")));
        } else {
            txtAIEssentialGear.setText("None");
        }

        if (trip.aiOptionalGear != null && !trip.aiOptionalGear.isEmpty()) {
            txtAIOptionalGear.setText(trip.aiOptionalGear.stream().collect(Collectors.joining("\n• ", "• ", "")));
        } else {
            txtAIOptionalGear.setText("None");
        }

        if (trip.aiWarnings != null && !trip.aiWarnings.isEmpty()) {
            StringBuilder sb = new StringBuilder();
            for (String w : trip.aiWarnings) {
                sb.append("\u26A0 ").append(w).append("\n");
            }
            txtAIWarnings.setText(sb.toString().trim());
            txtAIWarnings.setVisibility(View.VISIBLE);
        } else {
            txtAIWarnings.setText("");
            txtAIWarnings.setVisibility(View.GONE);
        }
    }
}
