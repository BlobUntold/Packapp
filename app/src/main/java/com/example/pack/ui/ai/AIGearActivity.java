package com.example.pack.ui.ai;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pack.R;
import com.example.pack.data.models.Backpack;
import com.example.pack.data.models.GearItem;
import com.example.pack.data.models.Trip;
import com.example.pack.viewmodel.BackpackViewModel;
import com.example.pack.viewmodel.GearViewModel;
import com.example.pack.viewmodel.TripViewModel;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class AIGearActivity extends AppCompatActivity {

    private Trip trip;
    private List<GearItem> gearList = new ArrayList<>();
    private List<Backpack> backpacks = new ArrayList<>();

    private TextView txtStatus, txtBackpack, txtVolume;
    private RecyclerView recyclerView;
    private Button btnSave;

    private AIGearSectionedAdapter adapter;

    private TripViewModel tripViewModel;
    private GearViewModel gearViewModel;
    private BackpackViewModel backpackViewModel;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ai_gear);

        int tripId = getIntent().getIntExtra("trip_id", -1);

        tripViewModel = new ViewModelProvider(this).get(TripViewModel.class);
        gearViewModel = new ViewModelProvider(this).get(GearViewModel.class);
        backpackViewModel = new ViewModelProvider(this).get(BackpackViewModel.class);

        trip = tripViewModel.getTrip(tripId);
        if (trip == null) {
            finish();
            return;
        }

        if (gearViewModel.gearList.getValue() != null)
            gearList = gearViewModel.gearList.getValue();

        if (backpackViewModel.backpacks.getValue() != null)
            backpacks = backpackViewModel.backpacks.getValue();

        txtStatus = findViewById(R.id.txtStatus);
        txtBackpack = findViewById(R.id.txtBackpack);
        txtVolume = findViewById(R.id.txtVolume);
        recyclerView = findViewById(R.id.recyclerView);
        btnSave = findViewById(R.id.btnSave);

        ImageButton btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(v -> finish());

        btnSave.setOnClickListener(v -> savePackingList());

        txtStatus.setText("Talking to AI...");

        generatePackingList();
    }

    // -------------------------------------------------------------------------
    // AI Fetching
    // -------------------------------------------------------------------------

    private void generatePackingList() {
        if (trip.aiEssentialGear != null && !trip.aiEssentialGear.isEmpty()) {
            displaySavedResults();
        } else {
            String prompt = AIPromptBuilder.build(trip, gearList, backpacks);
            GeminiClient client = new GeminiClient();

            client.generate(prompt, new GeminiClient.GeminiResponseListener() {
                @Override
                public void onSuccess(String text) {
                    try {
                        System.out.println("AI raw response: " + text); // Debug
                        JSONObject json = new JSONObject(text);
                        AIPackingResult result = new AIPackingResult(
                                json.optString("backpack", ""),
                                jsonArrayToList(json.getJSONArray("essential")),
                                jsonArrayToList(json.getJSONArray("optional")),
                                jsonArrayToList(json.getJSONArray("warnings"))
                        );
                        runOnUiThread(() -> displayResults(result, true));
                    } catch (Exception e) {
                        e.printStackTrace();
                        runOnUiThread(() ->
                                txtStatus.setText("AI parsing error: " + e.getMessage())
                        );
                    }
                }

                @Override
                public void onError(String error) {
                    System.out.println("AI error: " + error); // Debug
                    runOnUiThread(() -> txtStatus.setText("Error: " + error));
                }
            });
        }
    }

    // -------------------------------------------------------------------------
    // Display Saved Results
    // -------------------------------------------------------------------------

    private void displaySavedResults() {
        AIPackingResult saved = new AIPackingResult(
                getBackpackName(trip.chosenBackpackId),
                trip.aiEssentialGear,                      // FIXED
                trip.aiOptionalGear,                       // FIXED
                trip.aiWarnings == null                    // FIXED
                        ? new ArrayList<>()
                        : trip.aiWarnings
        );

        // Debug log
        System.out.println("Loaded warnings: " + saved.warnings);   // FIXED

        displayResults(saved, false);   // FIXED
    }

    private String getBackpackName(int id) {
        for (Backpack b : backpacks) {
            if (b.id == id) return b.name;
        }
        return "";
    }

    // -------------------------------------------------------------------------
    // Handle Displaying Results with Drag-and-Drop
    // -------------------------------------------------------------------------

    private void displayResults(AIPackingResult result, boolean isNew) {
        txtStatus.setText("Packing List Ready");
        txtBackpack.setText(
                result.backpackName.isEmpty()
                        ? "Backpack: (none selected)"
                        : "Backpack: " + result.backpackName
        );

        List<String> other = new ArrayList<>();
        if (isNew) {
            List<String> allNames = new ArrayList<>();
            for (GearItem g : gearList) allNames.add(g.getName());
            allNames.removeAll(result.essential);
            allNames.removeAll(result.optional);
            other.addAll(allNames);
        }

        System.out.println("Essential: " + result.essential); // Debug
        System.out.println("Optional: " + result.optional); // Debug
        System.out.println("Other: " + other); // Debug

        adapter = new AIGearSectionedAdapter(result.essential, result.optional, other, result.warnings);
        adapter.setOnSectionChangedListener(this::updateVolume);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        ItemTouchHelper helper = new ItemTouchHelper(adapter.getTouchHelperCallback());
        helper.attachToRecyclerView(recyclerView);

        updateVolume();
        // Warnings are now handled by the adapter as a footer.
    }

    // -------------------------------------------------------------------------
    // Volume Calculation
    // -------------------------------------------------------------------------

    private void updateVolume() {
        double total = 0;
        double max = 0;

        for (String n : adapter.getEssentialItems()) {
            for (GearItem g : gearList) {
                if (g.getName().equals(n)) total += g.volume;
            }
        }

        for (Backpack b : backpacks) {
            if (("Backpack: " + b.name).equals(txtBackpack.getText().toString())) {
                max = b.maxVolume;
                break;
            }
        }

        txtVolume.setText(String.format("Volume: %.2f / %.2f L", total, max));
    }

    // -------------------------------------------------------------------------
    // Save Packing List + Warnings
    // -------------------------------------------------------------------------

    private void savePackingList() {
        if (trip != null) {

            trip.aiEssentialGear = new ArrayList<>(adapter.getEssentialItems());
            trip.aiOptionalGear = new ArrayList<>(adapter.getOptionalItems());

            // Save warnings from adapter
            trip.aiWarnings = new ArrayList<>();
            if (adapter != null) {
                trip.aiWarnings.addAll(adapter.getWarnings());
            }

            System.out.println("Saved warnings: " + trip.aiWarnings);

            tripViewModel.update(trip);

            Toast.makeText(this, "Packing list saved!", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Could not save packing list.", Toast.LENGTH_SHORT).show();
        }
    }

    // -------------------------------------------------------------------------
    // Helpers
    // -------------------------------------------------------------------------

    private List<String> jsonArrayToList(org.json.JSONArray arr) {
        List<String> out = new ArrayList<>();
        for (int i = 0; i < arr.length(); i++) {
            try {
                out.add(arr.getString(i));
            } catch (Exception ignored) {}
        }
        return out;
    }
}