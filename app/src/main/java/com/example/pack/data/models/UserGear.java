package com.example.pack.data.models;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "user_gear")
public class UserGear {

    @PrimaryKey(autoGenerate = true)
    public int id;

    public int userId;       // optional until you add user accounts
    public int gearItemId;   // reference to GearItem
    public String notes;     // user personal notes
}
