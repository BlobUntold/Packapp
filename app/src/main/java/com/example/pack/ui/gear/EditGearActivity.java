package com.example.pack.ui.gear;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.pack.R;
import com.example.pack.data.models.GearItem;
import com.example.pack.viewmodel.GearViewModel;

public class EditGearActivity extends AppCompatActivity {

    private EditText editName, editBrand, editModel, editWeight, editVolume;
    private Spinner spinnerCategory, spinnerConditions, spinnerGearType, spinnerInsulation, spinnerCondition;
    private EditText editTentCapacity, editComfortTemp, editExtremeTemp, editRValue, editNotes;
    private LinearLayout sectionTent, sectionSleepingBag, sectionSleepingPad, sectionWeather;

    private CheckBox checkFourSeason, checkFreestanding;
    private CheckBox checkGoodForCold, checkGoodForRain, checkGoodForSnow, checkGoodForWind;

    private Button btnSave, btnDelete;
    private GearViewModel gearViewModel;
    private GearItem item;
    private int gearId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_gear);

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

        gearId = getIntent().getIntExtra("gear_id", -1);

        gearViewModel = new ViewModelProvider(this).get(GearViewModel.class);
        item = gearViewModel.getGearById(gearId);

        if (item == null) {
            Toast.makeText(this, "Error loading gear item", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        ImageButton btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(v -> finish());

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
        btnDelete = findViewById(R.id.btnDeleteGear);

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

        // Condition & notes
        spinnerCondition = findViewById(R.id.spinnerCondition);
        editNotes = findViewById(R.id.editNotes);

        // Populate dropdowns
        setupSpinner(spinnerCategory, R.array.gear_categories);
        setupSpinner(spinnerConditions, R.array.gear_conditions);
        setupSpinner(spinnerGearType, R.array.gear_types);
        setupSpinner(spinnerInsulation, R.array.insulation_types);
        setupSpinner(spinnerCondition, R.array.gear_condition_status);

        // LOAD ITEM INTO UI
        loadItemData();

        // DYNAMIC FIELD LOGIC (same as AddGearActivity)
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

        btnSave.setOnClickListener(v -> saveChanges());

        btnDelete.setOnClickListener(v -> {
            gearViewModel.deleteGear(item);
            Toast.makeText(this, "Gear deleted", Toast.LENGTH_SHORT).show();
            finish();
        });
    }

    private void setupSpinner(Spinner spinner, int arrayRes) {
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                this, arrayRes, android.R.layout.simple_spinner_item
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
    }

    private void updateVisibleSections(String type) {

        sectionTent.setVisibility(type.equals("Tent") ? View.VISIBLE : View.GONE);
        sectionSleepingBag.setVisibility(type.equals("Sleeping Bag") ? View.VISIBLE : View.GONE);
        sectionSleepingPad.setVisibility(type.equals("Sleeping Pad") ? View.VISIBLE : View.GONE);

        if (type.equals("Tent") || type.equals("Sleeping Bag")
                || type.equals("Jacket") || type.equals("Clothing")) {
            sectionWeather.setVisibility(View.VISIBLE);
        } else {
            sectionWeather.setVisibility(View.GONE);
        }
    }

    private void loadItemData() {

        // Basic fields
        editName.setText(item.name);
        editBrand.setText(item.brand);
        editModel.setText(item.model);
        editWeight.setText(String.valueOf(item.weight));
        editVolume.setText(String.valueOf(item.volume));

        // Spinners
        spinnerCategory.setSelection(((ArrayAdapter) spinnerCategory.getAdapter()).getPosition(item.category));
        spinnerConditions.setSelection(((ArrayAdapter) spinnerConditions.getAdapter()).getPosition(item.conditions));
        spinnerGearType.setSelection(((ArrayAdapter) spinnerGearType.getAdapter()).getPosition(item.gearType));

        updateVisibleSections(item.gearType);

        // Tent
        editTentCapacity.setText(String.valueOf(item.capacity));
        checkFourSeason.setChecked(item.fourSeason);
        checkFreestanding.setChecked(item.freestanding);

        // Sleeping Bag
        editComfortTemp.setText(String.valueOf(item.comfortTemp));
        editExtremeTemp.setText(String.valueOf(item.extremeTemp));
        spinnerInsulation.setSelection(((ArrayAdapter) spinnerInsulation.getAdapter()).getPosition(item.insulationType));

        // Sleeping Pad
        editRValue.setText(String.valueOf(item.rValue));

        // Weather
        checkGoodForCold.setChecked(item.goodForCold);
        checkGoodForRain.setChecked(item.goodForRain);
        checkGoodForSnow.setChecked(item.goodForSnow);
        checkGoodForWind.setChecked(item.goodForWind);

        // Condition
        spinnerCondition.setSelection(((ArrayAdapter) spinnerCondition.getAdapter()).getPosition(item.condition));

        // Notes
        editNotes.setText(item.notes);
    }

    private void saveChanges() {

        item.name = editName.getText().toString().trim();
        item.brand = editBrand.getText().toString().trim();
        item.model = editModel.getText().toString().trim();
        item.weight = safeDouble(editWeight.getText().toString());
        item.volume = safeDouble(editVolume.getText().toString());

        item.category = spinnerCategory.getSelectedItem().toString();
        item.conditions = spinnerConditions.getSelectedItem().toString();
        item.gearType = spinnerGearType.getSelectedItem().toString();

        // Tent
        if (item.gearType.equals("Tent")) {
            item.capacity = safeInt(editTentCapacity.getText().toString());
            item.fourSeason = checkFourSeason.isChecked();
            item.freestanding = checkFreestanding.isChecked();
        }

        // Sleeping Bag
        if (item.gearType.equals("Sleeping Bag")) {
            item.comfortTemp = safeDouble(editComfortTemp.getText().toString());
            item.extremeTemp = safeDouble(editExtremeTemp.getText().toString());
            item.insulationType = spinnerInsulation.getSelectedItem().toString();
        }

        // Sleeping Pad
        if (item.gearType.equals("Sleeping Pad")) {
            item.rValue = safeDouble(editRValue.getText().toString());
        }

        // Weather
        item.goodForCold = checkGoodForCold.isChecked();
        item.goodForRain = checkGoodForRain.isChecked();
        item.goodForSnow = checkGoodForSnow.isChecked();
        item.goodForWind = checkGoodForWind.isChecked();

        // Condition & notes
        item.condition = spinnerCondition.getSelectedItem().toString();
        item.notes = editNotes.getText().toString().trim();

        gearViewModel.updateGear(item);

        Toast.makeText(this, "Gear updated!", Toast.LENGTH_SHORT).show();
        finish();
    }

    private int safeInt(String s) {
        try { return Integer.parseInt(s); }
        catch (Exception e) { return 0; }
    }

    private double safeDouble(String s) {
        try { return Double.parseDouble(s); }
        catch (Exception e) { return 0; }
    }
}