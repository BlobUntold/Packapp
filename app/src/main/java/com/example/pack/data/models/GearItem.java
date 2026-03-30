package com.example.pack.data.models;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "gear_items")
public class GearItem {

    @PrimaryKey(autoGenerate = true)
    public int id;

    // Basic fields
    public String name;
    public String brand;
    public String model;
    public double weight;
    public double volume;
    public String category;
    public String conditions;
    public int popularity;

    // ---------------------------------------------------------
    // 1. Gear Type (Determines which fields are shown)
    // ---------------------------------------------------------
    public String gearType;   // Tent, Sleeping Bag, Jacket, Stove, etc.

    // ---------------------------------------------------------
    // 2. Tent Fields
    // ---------------------------------------------------------
    public int capacity;          // Number of people
    public boolean fourSeason;    // True = 4-season tent
    public boolean freestanding;

    // ---------------------------------------------------------
    // 3. Sleeping Bag Fields
    // ---------------------------------------------------------
    public double comfortTemp;     // Comfort rating (°C/°F)
    public double extremeTemp;     // Survival rating
    public String insulationType;  // Down, Synthetic, Wool, None

    // ---------------------------------------------------------
    // 4. Sleeping Pad Fields
    // ---------------------------------------------------------
    public double rValue;          // R-value (insulation rating)

    // ---------------------------------------------------------
    // 5. Clothing & Jacket Weather Suitability
    // ---------------------------------------------------------
    public boolean goodForCold;
    public boolean goodForRain;
    public boolean goodForSnow;
    public boolean goodForWind;

    // ---------------------------------------------------------
    // 6. Condition & Notes (applies to ALL gear)
    // ---------------------------------------------------------
    public String condition;   // Good, Worn, Damaged, Leaking, Unsafe
    public String notes;       // Free text notes (e.g., “Zip broken”)

    public String getName() {
        return name;
    
    }
}