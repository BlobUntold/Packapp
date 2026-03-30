package com.example.pack.ui.backpack;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.pack.R;
import com.example.pack.data.models.Backpack;
import com.example.pack.viewmodel.BackpackViewModel;

public class EditBackpackActivity extends AppCompatActivity {

    private EditText editName, editVolume, editWeight;
    private Button btnSave, btnDelete;
    private ImageButton btnBack;

    private BackpackViewModel viewModel;
    private Backpack backpack;
    private int id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_backpack);

        id = getIntent().getIntExtra("backpack_id", -1);

        viewModel = new ViewModelProvider(this).get(BackpackViewModel.class);
        backpack = viewModel.getById(id);

        editName = findViewById(R.id.editBackpackName);
        editVolume = findViewById(R.id.editMaxVolume);
        editWeight = findViewById(R.id.editMaxWeight);
        btnSave = findViewById(R.id.btnSaveBackpack);
        btnDelete = findViewById(R.id.btnDeleteBackpack);
        btnBack = findViewById(R.id.btnBack);

        btnBack.setOnClickListener(v -> finish());

        loadData();

        btnSave.setOnClickListener(v -> save());
        btnDelete.setOnClickListener(v -> delete());
    }

    private void loadData() {
        if (backpack == null) {
            Toast.makeText(this, "Error loading backpack", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        editName.setText(backpack.name);
        editVolume.setText(String.valueOf(backpack.maxVolume));
        editWeight.setText(String.valueOf(backpack.maxWeight));
    }

    private void save() {
        backpack.name = editName.getText().toString();
        backpack.maxVolume = Double.parseDouble(editVolume.getText().toString());
        backpack.maxWeight = Double.parseDouble(editWeight.getText().toString());

        viewModel.updateBackpack(backpack);
        Toast.makeText(this, "Backpack updated!", Toast.LENGTH_SHORT).show();
        finish();
    }

    private void delete() {
        viewModel.deleteBackpack(backpack);
        Toast.makeText(this, "Backpack deleted", Toast.LENGTH_SHORT).show();
        finish();
    }
}
