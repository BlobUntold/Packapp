package com.example.pack.data.models;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "backpacks")
public class Backpack {

    @PrimaryKey(autoGenerate = true)
    public int id;

    public String name;
    public double maxVolume;  // liters
    public double maxWeight;  // kg or lbs
}
