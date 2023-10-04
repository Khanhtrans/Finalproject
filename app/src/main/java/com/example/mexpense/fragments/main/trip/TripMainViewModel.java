package com.example.mexpense.fragments.main.trip;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.mexpense.entity.Trip;
import com.example.mexpense.ultilities.Constants;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;

public class TripMainViewModel extends ViewModel {
    public MutableLiveData<List<Trip>> tripList = new MutableLiveData<>();

    public void sort() {
        List<Trip> list = tripList.getValue();
        Collections.reverse(list);
        tripList.setValue(list);
    }

}