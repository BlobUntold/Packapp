package com.example.pack.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.example.pack.data.models.UserGear;
import com.example.pack.data.repository.UserGearRepository;

import java.util.List;

public class UserGearViewModel extends AndroidViewModel {

    private final UserGearRepository repository;
    public MutableLiveData<List<UserGear>> userGearList = new MutableLiveData<>();

    public UserGearViewModel(@NonNull Application application) {
        super(application);
        repository = new UserGearRepository(application);
    }

    public void loadUserGear(int userId) {
        userGearList.setValue(repository.getUserGear(userId));
    }

    public void addUserGear(UserGear item) {
        repository.addUserGear(item);
    }

    public void deleteUserGear(UserGear item) {
        repository.deleteUserGear(item);
    }
}