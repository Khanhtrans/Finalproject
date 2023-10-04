package com.example.mexpense.repository;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.mexpense.entity.Trip;
import com.example.mexpense.services.SqlService;
import com.example.mexpense.ultilities.Constants;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class TripRepository {

    public static final String TABLE_NAME = "trips_table";

    private Context context;

    public TripRepository(Context context){
        this.context = context;
    }

    public List<Trip> getTrips() {
        SQLiteDatabase db = SqlService.getInstance(context).getReadableDatabase();
        String query = "SELECT * FROM " + TABLE_NAME;
        Cursor c = db.rawQuery(query, null);
        return getList(c);
    }

    public Trip getTripById(int id) {
        Trip t = new Trip();
        SQLiteDatabase db = SqlService.getInstance(context).getReadableDatabase();
        Cursor c = db.rawQuery("SELECT * FROM " + TABLE_NAME + " WHERE " + Constants.COLUMN_ID_TRIP + "=" + id, null);
        while (c.moveToNext()){
            t = new Trip(c);
        }
        return t;
    }

    public void addTrip(Trip trip) {
        SQLiteDatabase db = SqlService.getInstance(context).getWritableDatabase();
        db.insert(TABLE_NAME, Constants.COLUMN_DESCRIPTION_TRIP , trip.toCV());
    }

    public void deleteTrip(int id) {
        SQLiteDatabase db = SqlService.getInstance(context).getWritableDatabase();
        db.delete(TABLE_NAME, Constants.COLUMN_ID_TRIP  + "=?", new String[]{String.valueOf(id)});
        db.delete(Constants.EXPENSE_TABLE_NAME, Constants.COLUMN_TRIP_ID_EXPENSE  + "=?", new String[]{String.valueOf(id)});
    }

    public void updateTrip(int id, Trip trip){
        SQLiteDatabase db = SqlService.getInstance(context).getWritableDatabase();
        db.update(TABLE_NAME, trip.toCV(), Constants.COLUMN_ID_TRIP  + "=?", new String[]{String.valueOf(id)});
    }

    public void updateTotal(int id, double total){
        SQLiteDatabase db = SqlService.getInstance(context).getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(Constants.COLUMN_TOTAL_TRIP , total);
        db.update(TABLE_NAME, cv, Constants.COLUMN_ID_TRIP  + "=?", new String[]{String.valueOf(id)});
    }

    public void deleteAll() {
        SQLiteDatabase db = SqlService.getInstance(context).getWritableDatabase();
        db.execSQL("DELETE FROM " + TABLE_NAME);
        db.execSQL("DELETE FROM expenses_table");
    }

    public List<Trip> masterSearch(String name, String destination, String start, String end){
        SQLiteDatabase db = SqlService.getInstance(context).getReadableDatabase();
        String query = "SELECT * FROM " + TABLE_NAME + " WHERE ";
        boolean isFirst = true;
        if(!name.equals("")) {
            query += Constants.COLUMN_NAME_TRIP  + " LIKE '%" + name + "%'";
            isFirst = false;
        }
        if(!destination.equals("")){
            if(!isFirst){
                query += " AND ";
            }
            query += Constants.COLUMN_DESTINATION_TRIP  + " LIKE '%" + destination + "%'";
            isFirst = false;
        }
        if(!isFirst){
            query += " AND ";
        }
        query += Constants.COLUMN_START_DATE_TRIP  + " >= '" + start + "' AND "
                + Constants.COLUMN_END_DATE_TRIP  + " <= '" + end + "'";
        Cursor c = db.rawQuery(query, null);
        return getList(c);
    }

    public List<Trip> getList(Cursor c){
        List<Trip> trips = new ArrayList<>();
        while (c.moveToNext()){
            Trip t = new Trip(c);
            trips.add(t);
        }
        c.close();
        return trips;
    }

}