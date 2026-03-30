package com.example.pack.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.example.pack.data.models.Trip;
import com.example.pack.data.repository.TripRepository;

import java.util.List;

public class TripViewModel extends AndroidViewModel {

    private final TripRepository repository;
    public MutableLiveData<List<Trip>> trips = new MutableLiveData<>();

    public TripViewModel(@NonNull Application application) {
        super(application);
        repository = new TripRepository(application);
        loadAllTrips();
    }

    public void loadAllTrips() {
        trips.setValue(repository.getAllTrips());
    }

    public void addTrip(Trip trip) {
        repository.addTrip(trip);
        loadAllTrips();
    }

    public Trip getTrip(int id) {
        return repository.getTripById(id);
    }

    public void update(Trip trip) {
        repository.update(trip);
        loadAllTrips();
    }
}