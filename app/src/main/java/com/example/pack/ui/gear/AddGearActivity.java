package com.example.pack.ui.gear;

import androidx.annotation.NonNull; import androidx.appcompat.app.AppCompatActivity; import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle; import android.view.MenuItem; import android.view.View; import android.widget.AdapterView; import android.widget.ArrayAdapter; import android.widget.Button; import android.widget.CheckBox; import android.widget.EditText; import android.widget.ImageButton; import android.widget.LinearLayout; import android.widget.Spinner; import android.widget.Toast;

import com.example.pack.R; import com.example.pack.data.models.GearItem; import com.example.pack.viewmodel.GearViewModel;

public class AddGearActivity extends AppCompatActivity {

    private EditText editName, editBrand, editModel, editWeight, editVolume;
    private Spinner spinnerCategory, spinnerConditions, spinnerGearType, spinnerInsulation, spinnerCondition;
    private EditText editTentCapacity, editComfortTemp, editExtremeTemp, editRValue, editNotes;
    private LinearLayout sectionTent, sectionSleepingBag, sectionSleepingPad, sectionWeather;

    private CheckBox checkFourSeason, checkFreestanding;
    private CheckBox checkGoodForCold, checkGoodForRain, checkGoodForSnow, checkGoodForWind;

    private Button btnSave;
    private GearViewModel gearViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_gear);


        sectionTent = findViewById(R.id.sectionTent);
        sectionSleepingBag = findViewById(R.id.sectionSleepingBag);
        sectionSleepingPad = findViewById(R.id.sectionSleepingPad);
        sectionWeather = findViewById(R.id.sectionWeather);

        editTentCapacity = findViewById(R.id.editTentCapacity);
        checkFourSeason = findViewById(R.id.checkFourSeason);
        checkFreestanding = findViewById(R.id.checkFreestanding);

        editComfortTemp = findViewById(R.id.editComfortTemp);
        editExtremeTemp = findViewById(R.id.editExtremeTemp);
        spinnerInsulation = findViewById(R.id.spinnerInsulation);

        editRValue = findViewById(R.id.editRValue);

        checkGoodForCold = findViewById(R.id.checkGoodForCold);
        checkGoodForRain = findViewById(R.id.checkGoodForRain);
        checkGoodForSnow = findViewById(R.id.checkGoodForSnow);
        checkGoodForWind = findViewById(R.id.checkGoodForWind);

        spinnerCondition = findViewById(R.id.spinnerCondition);
        editNotes = findViewById(R.id.editNotes);

        ImageButton btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(v -> finish());

        gearViewModel = new ViewModelProvider(this).get(GearViewModel.class);

        // Basic fields
        editName = findViewById(R.id.editName);
        editBrand = findViewById(R.id.editBrand);
        editModel = findViewById(R.id.editModel);
        editWeight = findViewById(R.id.editWeight);
        editVolume = findViewById(R.id.editVolume);

        spinnerCategory = findViewById(R.id.spinnerCategory);
        spinnerConditions = findViewById(R.id.spinnerConditions);
        spinnerGearType = findViewById(R.id.spinnerGearType);

        btnSave = findViewById(R.id.btnSaveGear);

        // Dynamic sections
        sectionTent = findViewById(R.id.sectionTent);
        sectionSleepingBag = findViewById(R.id.sectionSleepingBag);
        sectionSleepingPad = findViewById(R.id.sectionSleepingPad);
        sectionWeather = findViewById(R.id.sectionWeather);

        // Tent fields
        editTentCapacity = findViewById(R.id.editTentCapacity);
        checkFourSeason = findViewById(R.id.checkFourSeason);
        checkFreestanding = findViewById(R.id.checkFreestanding);

        // Sleeping bag fields
        editComfortTemp = findViewById(R.id.editComfortTemp);
        editExtremeTemp = findViewById(R.id.editExtremeTemp);
        spinnerInsulation = findViewById(R.id.spinnerInsulation);

        // Sleeping pad fields
        editRValue = findViewById(R.id.editRValue);

        // Weather suitability
        checkGoodForCold = findViewById(R.id.checkGoodForCold);
        checkGoodForRain = findViewById(R.id.checkGoodForRain);
        checkGoodForSnow = findViewById(R.id.checkGoodForSnow);
        checkGoodForWind = findViewById(R.id.checkGoodForWind);

        // Condition & Notes
        spinnerCondition = findViewById(R.id.spinnerCondition);
        editNotes = findViewById(R.id.editNotes);

        // Populate dropdowns
        setupSpinner(spinnerCategory, R.array.gear_categories);
        setupSpinner(spinnerConditions, R.array.gear_conditions);
        setupSpinner(spinnerGearType, R.array.gear_types);
        setupSpinner(spinnerInsulation, R.array.insulation_types);
        setupSpinner(spinnerCondition, R.array.gear_condition_status);


        // DYNAMIC FIELD LOGIC
        spinnerGearType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                String type = parent.getItemAtPosition(position).toString();

                // FIRST: Hide everything
                sectionTent.setVisibility(View.GONE);
                sectionSleepingBag.setVisibility(View.GONE);
                sectionSleepingPad.setVisibility(View.GONE);
                sectionWeather.setVisibility(View.GONE);

                // THEN enable the correct section(s)
                switch (type) {

                    case "Tent":
                        sectionTent.setVisibility(View.VISIBLE);
                        // Tent benefits from weather suitability
                        sectionWeather.setVisibility(View.VISIBLE);
                        break;

                    case "Sleeping Bag":
                        sectionSleepingBag.setVisibility(View.VISIBLE);
                        sectionWeather.setVisibility(View.VISIBLE);  // Fits cold/snow logic
                        break;

                    case "Sleeping Pad":
                        sectionSleepingPad.setVisibility(View.VISIBLE);
                        break;

                    case "Jacket":
                    case "Clothing":
                        sectionWeather.setVisibility(View.VISIBLE);
                        break;

                    // Stove, Filter, Misc → show nothing extra
                    default:
                        // No extra sections
                        break;
                }
            }


            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });


        btnSave.setOnClickListener(v -> saveGear());
    }


    private void setupSpinner(Spinner spinner, int arrayRes) {
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                this, arrayRes, android.R.layout.simple_spinner_item
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
    }


    private void saveGear() {

        String name = editName.getText().toString().trim();
        String brand = editBrand.getText().toString().trim();
        String model = editModel.getText().toString().trim();
        String weightStr = editWeight.getText().toString().trim();
        String volumeStr = editVolume.getText().toString().trim();

        if (name.isEmpty() || weightStr.isEmpty() || volumeStr.isEmpty()) {
            Toast.makeText(this, "Name, weight, and volume are required", Toast.LENGTH_SHORT).show();
            return;
        }

        GearItem item = new GearItem();
        item.name = name;
        item.brand = brand;
        item.model = model;
        item.weight = Double.parseDouble(weightStr);
        item.volume = Double.parseDouble(volumeStr);

        item.category = spinnerCategory.getSelectedItem().toString();
        item.conditions = spinnerConditions.getSelectedItem().toString();

        // Gear type
        item.gearType = spinnerGearType.getSelectedItem().toString();

        // Tent Fields
        if (item.gearType.equals("Tent")) {
            item.capacity = safeInt(editTentCapacity.getText().toString());
            item.fourSeason = checkFourSeason.isChecked();
            item.freestanding = checkFreestanding.isChecked();
        }

        // Sleeping Bag Fields
        if (item.gearType.equals("Sleeping Bag")) {
            item.comfortTemp = safeDouble(editComfortTemp.getText().toString());
            item.extremeTemp = safeDouble(editExtremeTemp.getText().toString());
            item.insulationType = spinnerInsulation.getSelectedItem().toString();
        }

        // Sleeping Pad Fields
        if (item.gearType.equals("Sleeping Pad")) {
            item.rValue = safeDouble(editRValue.getText().toString());
        }

        // Weather Suitability
        item.goodForCold = checkGoodForCold.isChecked();
        item.goodForRain = checkGoodForRain.isChecked();
        item.goodForSnow = checkGoodForSnow.isChecked();
        item.goodForWind = checkGoodForWind.isChecked();

        // Condition & Notes
        item.condition = spinnerCondition.getSelectedItem().toString();
        item.notes = editNotes.getText().toString().trim();


        // Save to DB
        gearViewModel.addGear(item);

        Toast.makeText(this, "Gear saved!", Toast.LENGTH_SHORT).show();
        finish();
    }


    private int safeInt(String s) {
        try { return Integer.parseInt(s); }
        catch (Exception e) { return 0; }
    }

    private double safeDouble(String s) {
        try { return Double.parseDouble(s); }
        catch (Exception e) { return 0; }
    }}