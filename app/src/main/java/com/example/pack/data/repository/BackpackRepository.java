package com.example.pack.data.repository;

import android.content.Context;

import com.example.pack.data.database.AppDatabase;
import com.example.pack.data.models.Backpack;

import java.util.List;
public class BackpackRepository {

    private final AppDatabase db;

    public BackpackRepository(Context context) {
        db = AppDatabase.getInstance(context);
    }

    public long addBackpack(Backpack backpack) {
        return db.backpackDao().insert(backpack);
    }

    public void updateBackpack(Backpack backpack) {
        db.backpackDao().update(backpack);
    }

    public void deleteBackpack(Backpack backpack) {
        db.backpackDao().delete(backpack);
    }

    public List<Backpack> getAllBackpacks() {
        return db.backpackDao().getAllBackpacks();
    }

    public Backpack getById(int id) {
        return db.backpackDao().getById(id);
    }
}
