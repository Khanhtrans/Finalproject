package com.example.mexpense.services;

import android.content.Context;
import android.widget.Toast;

import androidx.lifecycle.MutableLiveData;

import com.example.mexpense.entity.Trip;
import com.example.mexpense.repository.TripRepository;
import com.example.mexpense.ultilities.Constants;
import com.example.mexpense.ultilities.Utilities;

import java.util.List;

public class TripService {
    TripRepository repository;

    public TripService(Context context) {
        repository = new TripRepository(context);
    }

    public void getTrips(MutableLiveData<List<Trip>> tripList) {
        List<Trip> trips = repository.getTrips();
        tripList.setValue(trips);
    }

    public void getTrip(MutableLiveData<Trip> trip, int id) {
        Trip t;
        if (id == -1) {
            t = new Trip();
        } else {
            t = repository.getTripById(id);
        }
        trip.setValue(t);
    }

    public void addTrip(Trip trip) {
        repository.addTrip(trip);
    }

    public void updateTrip(int id, Trip trip) {
        repository.updateTrip(id, trip);
    }

    public void updateTotal(int id, double total) {
        repository.updateTotal(id, total);
    }

    public void deleteTrip(int tripId) {
        repository.deleteTrip(tripId);
    }

    public void resetDatabase() {
        repository.deleteAll();
    }

    public void masterSearch(MutableLiveData<List<Trip>> trips, String name, String destination, String start, String end){
        if (start.equals("")) {
            start = Constants.LIMIT_START_DATE;
        } else {
            start = Utilities.convertDate(start, true);
        }
        if (end.equals("")) {
            end = Constants.LIMIT_END_DATE;
        } else {
            end = Utilities.convertDate(end, true);
        }
        trips.setValue(repository.masterSearch(name, destination, start, end));
    }
}
