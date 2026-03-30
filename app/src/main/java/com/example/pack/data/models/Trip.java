package com.example.pack.data.models;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import com.example.pack.data.Converters;

import java.util.ArrayList;
import java.util.List;

@Entity(tableName = "trips")
@TypeConverters(Converters.class)
public class Trip {

    @PrimaryKey(autoGenerate = true)
    public int id;

    // Basic trip info
    public String environment;
    public String weather;
    public int duration;
    public int nights;
    public long timestamp;

    // Weather details
    public double minTemp;
    public double maxTemp;
    public int precipChance;   // %
    public double windSpeed;   // mph or km/h

    // Terrain & physical difficulty
    public double distanceKm;
    public double elevationGain;
    public String terrainDifficulty; // Easy / Moderate / Hard

    // Camping details
    public String campsiteType;     // Backcountry, Campground, Shelter
    public String cookMethod;       // Stove / Fire / No cook
    public boolean fireAllowed;

    // Group details
    public int groupSize;
    public boolean sharedGearAllowed;

    // User experience
    public String experienceLevel;
    public String fitnessLevel;

    // Safety & navigation
    public String cellCoverage;
    public String navigationMethod;
    public boolean mapsRequired;

    // AI calculation outputs
    public int chosenBackpackId;
    public double estimatedPackVolume;
    public List<String> aiEssentialGear;
    public List<String> aiOptionalGear;

    public List<String> aiWarnings = new ArrayList<>();
}
