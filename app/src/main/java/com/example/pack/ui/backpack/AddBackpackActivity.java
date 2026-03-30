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

public class AddBackpackActivity extends AppCompatActivity {

    private EditText editName, editVolume, editWeight;
    private Button btnSave;
    private ImageButton btnBack;
    private BackpackViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_backpack);

        editName = findViewById(R.id.editBackpackName);
        editVolume = findViewById(R.id.editMaxVolume);
        editWeight = findViewById(R.id.editMaxWeight);
        btnSave = findViewById(R.id.btnSaveBackpack);
        btnBack = findViewById(R.id.btnBack);

        viewModel = new ViewModelProvider(this).get(BackpackViewModel.class);

        btnBack.setOnClickListener(v -> finish());

        btnSave.setOnClickListener(v -> save());
    }

    private void save() {
        String name = editName.getText().toString().trim();
        String vol = editVolume.getText().toString().trim();
        String wt = editWeight.getText().toString().trim();

        if (name.isEmpty() || vol.isEmpty() || wt.isEmpty()) {
            Toast.makeText(this, "All fields required", Toast.LENGTH_SHORT).show();
            return;
        }

        Backpack b = new Backpack();
        b.name = name;
        b.maxVolume = Double.parseDouble(vol);
        b.maxWeight = Double.parseDouble(wt);

        viewModel.addBackpack(b);
        Toast.makeText(this, "Backpack saved", Toast.LENGTH_SHORT).show();
        finish();
    }
}

