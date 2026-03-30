package com.example.pack.data.database;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;
import androidx.room.Delete;

import com.example.pack.data.models.Trip;

import java.util.List;

@Dao
public interface TripDao {

    @Insert
    long insert(Trip trip);

    @Update
    void update(Trip trip);

    @Delete
    void delete(Trip trip);

    @Query("SELECT * FROM trips ORDER BY timestamp DESC")
    List<Trip> getAllTrips();

    @Query("SELECT * FROM trips WHERE id = :id LIMIT 1")
    Trip getById(int id);
}