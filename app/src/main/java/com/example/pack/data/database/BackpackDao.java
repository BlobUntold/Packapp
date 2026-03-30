package com.example.pack.data.database;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;
import androidx.room.Delete;

import com.example.pack.data.models.Backpack;

import java.util.List;

@Dao
public interface BackpackDao {

    @Insert
    long insert(Backpack backpack);

    @Update
    void update(Backpack backpack);

    @Delete
    void delete(Backpack backpack);

    @Query("SELECT * FROM backpacks")
    List<Backpack> getAllBackpacks();

    @Query("SELECT * FROM backpacks WHERE id = :id LIMIT 1")
    Backpack getById(int id);
}