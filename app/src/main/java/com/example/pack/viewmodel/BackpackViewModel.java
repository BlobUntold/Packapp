package com.example.pack.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.example.pack.data.models.Backpack;
import com.example.pack.data.repository.BackpackRepository;

import java.util.List;

public class BackpackViewModel extends AndroidViewModel {

    private final BackpackRepository repo;
    public MutableLiveData<List<Backpack>> backpacks = new MutableLiveData<>();

    public BackpackViewModel(@NonNull Application app) {
        super(app);
        repo = new BackpackRepository(app);
        loadBackpacks();
    }

    public void loadBackpacks() {
        backpacks.setValue(repo.getAllBackpacks());
    }

    public void addBackpack(Backpack b) {
        repo.addBackpack(b);
        loadBackpacks();
    }

    public void updateBackpack(Backpack b) {
        repo.updateBackpack(b);
        loadBackpacks();
    }

    public void deleteBackpack(Backpack b) {
        repo.deleteBackpack(b);
        loadBackpacks();
    }

    public Backpack getById(int id) {
        return repo.getById(id);
    }
}