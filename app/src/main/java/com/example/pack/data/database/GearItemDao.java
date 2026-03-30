package com.example.pack.data.database;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.pack.data.models.GearItem;

import java.util.List;

@Dao
public interface GearItemDao {

    @Insert
    long insert(GearItem gearItem);

    @Update
    void update(GearItem gearItem);

    @Delete
    void delete(GearItem gearItem);

    @Query("SELECT * FROM gear_items ORDER BY popularity DESC")
    List<GearItem> getAllGear();

    @Query("SELECT * FROM gear_items WHERE id = :id LIMIT 1")
    GearItem getById(int id);

    @Query("SELECT * FROM gear_items WHERE name LIKE '%' || :query || '%'")
    List<GearItem> searchByName(String query);

    @Query("UPDATE gear_items SET popularity = popularity + 1 WHERE id = :id")
    void incrementPopularity(int id);


}
