package com.example.pack.data.repository;

import android.content.Context;

import com.example.pack.data.database.AppDatabase;
import com.example.pack.data.models.Trip;

import java.util.List;

public class TripRepository {

    private final AppDatabase db;

    public TripRepository(Context context) {
        db = AppDatabase.getInstance(context);
    }

    public long addTrip(Trip trip) {
        return db.tripDao().insert(trip);
    }

    public void update(Trip trip) {
        db.tripDao().update(trip);
    }

    public void deleteTrip(Trip trip) {
        db.tripDao().delete(trip);
    }

    public List<Trip> getAllTrips() {
        return db.tripDao().getAllTrips();
    }

    public Trip getTripById(int id) {
        return db.tripDao().getById(id);
    }
}
