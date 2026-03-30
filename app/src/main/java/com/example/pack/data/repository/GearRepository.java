package com.example.pack.data.repository;

import android.content.Context;

import com.example.pack.data.database.AppDatabase;
import com.example.pack.data.models.GearItem;

import java.util.List;

public class GearRepository {

    private final AppDatabase db;

    public GearRepository(Context context) {
        db = AppDatabase.getInstance(context);
    }

    public long addGear(GearItem gearItem) {
        return db.gearItemDao().insert(gearItem);
    }

    public void updateGear(GearItem gearItem) {
        db.gearItemDao().update(gearItem);
    }

    public void deleteGear(GearItem gearItem) {
        db.gearItemDao().delete(gearItem);
    }

    public List<GearItem> getAllGear() {
        return db.gearItemDao().getAllGear();
    }

    public GearItem getGearById(int id) {
        return db.gearItemDao().getById(id);
    }

    public List<GearItem> searchGear(String query) {
        return db.gearItemDao().searchByName(query);
    }

    public void increasePopularity(int id) {
        db.gearItemDao().incrementPopularity(id);
    }
}
