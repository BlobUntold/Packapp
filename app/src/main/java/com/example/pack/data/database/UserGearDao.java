package com.example.pack.data.database;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Delete;

import com.example.pack.data.models.UserGear;

import java.util.List;

@Dao
public interface UserGearDao {

    @Insert
    long insert(UserGear userGear);

    @Delete
    void delete(UserGear userGear);

    @Query("SELECT * FROM user_gear WHERE userId = :userId")
    List<UserGear> getUserGear(int userId);

    @Query("SELECT * FROM user_gear WHERE gearItemId = :gearItemId")
    List<UserGear> getByGearItem(int gearItemId);
}