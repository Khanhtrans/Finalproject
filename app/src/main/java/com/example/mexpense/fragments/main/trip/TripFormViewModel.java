package com.example.mexpense.fragments.main.trip;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.mexpense.entity.Trip;

public class TripFormViewModel extends ViewModel {
    MutableLiveData<Trip> trip = new MutableLiveData<>();
}