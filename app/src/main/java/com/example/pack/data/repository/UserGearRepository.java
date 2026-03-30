package com.example.pack.data.repository;

import android.content.Context;

import com.example.pack.data.database.AppDatabase;
import com.example.pack.data.models.UserGear;

import java.util.List;

public class UserGearRepository {

    private final AppDatabase db;

    public UserGearRepository(Context context) {
        db = AppDatabase.getInstance(context);
    }

    public long addUserGear(UserGear userGear) {
        return db.userGearDao().insert(userGear);
    }

    public void deleteUserGear(UserGear userGear) {
        db.userGearDao().delete(userGear);
    }

    public List<UserGear> getUserGear(int userId) {
        return db.userGearDao().getUserGear(userId);
    }
}