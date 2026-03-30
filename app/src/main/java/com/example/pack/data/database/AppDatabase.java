package com.example.pack.data.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import com.example.pack.data.Converters;
import com.example.pack.data.models.GearItem;
import com.example.pack.data.models.UserGear;
import com.example.pack.data.models.Backpack;
import com.example.pack.data.models.Trip;

@Database(
        entities = {
                GearItem.class,
                UserGear.class,
                Backpack.class,
                Trip.class
        },
        version = 4,   // ← INCREMENT THIS
        exportSchema = false
)
@TypeConverters({Converters.class})
public abstract class AppDatabase extends RoomDatabase {

    private static volatile AppDatabase INSTANCE;

    // DAO getters
    public abstract GearItemDao gearItemDao();
    public abstract UserGearDao userGearDao();
    public abstract BackpackDao backpackDao();
    public abstract TripDao tripDao();

    // Singleton instance
    public static AppDatabase getInstance(Context context) {
        if (INSTANCE == null) {
            synchronized (AppDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(
                                    context.getApplicationContext(),
                                    AppDatabase.class,
                                    "camping_app_db"
                            )
                            .fallbackToDestructiveMigration()
                            .allowMainThreadQueries()   // OK for MVP — remove for production
                            .build();
                }
            }
        }
        return INSTANCE;
    }
}