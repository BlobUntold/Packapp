package com.example.pack.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.example.pack.data.models.GearItem;
import com.example.pack.data.repository.GearRepository;

import java.util.List;

public class GearViewModel extends AndroidViewModel {

    private final GearRepository repository;
    public MutableLiveData<List<GearItem>> gearList = new MutableLiveData<>();

    public GearViewModel(@NonNull Application application) {
        super(application);
        repository = new GearRepository(application);
        loadAllGear();
    }

    public void loadAllGear() {
        gearList.setValue(repository.getAllGear());
    }

    public void addGear(GearItem gearItem) {
        repository.addGear(gearItem);
        loadAllGear();
    }

    public List<GearItem> searchGear(String query) {
        return repository.searchGear(query);
    }

    public void increasePopularity(int id) {
        repository.increasePopularity(id);
    }
    public GearItem getGearById(int id) {
        return repository.getGearById(id);
    }
    public void updateGear(GearItem gearItem) {
        repository.updateGear(gearItem);
        loadAllGear();
    }
    public void deleteGear(GearItem gearItem) {
        repository.deleteGear(gearItem);
        loadAllGear();
    }
}