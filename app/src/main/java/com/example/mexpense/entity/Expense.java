package com.example.mexpense.entity;

import android.content.ContentValues;
import android.database.Cursor;

import androidx.annotation.NonNull;

import com.example.mexpense.ultilities.Constants;
import com.google.gson.Gson;

public class Expense {
    private int id;
    private String name;
    private String date;
    private double cost;
    private int amount;
    private String comment;
    private int tripId;
    private double latitude;
    private double longitude;
    private String imagePath;

    public Expense(int id, String name, double cost, int amount, String date, String comment, int tripId, double latitude, double longitude, String imagePath) {
        this.id = id;
        this.name = name;
        this.date = date;
        this.cost = cost;
        this.amount = amount;
        this.comment = comment;
        this.tripId = tripId;
        this.latitude = latitude;
        this.longitude = longitude;
        this.imagePath = imagePath;
    }

    public Expense() {
        id = Constants.NEW_EXPENSE;
        name = "";
        date = "";
        cost = 0;
        amount = 0;
        comment = "";
        latitude = 0.0;
        longitude = 0.0;
        imagePath = "";
    }

    public Expense(Cursor c){
        setId(Integer.parseInt(c.getString(0)));
        setCategory(c.getString(1));
        setCost(Double.parseDouble(c.getString(2)));
        setAmount(Integer.parseInt(c.getString(3)));
        setDate(c.getString(4));
        setComment(c.getString(5));
        setTripId(Integer.parseInt(c.getString(6)));
        setLatitude(Double.parseDouble(c.getString(7)));
        setLongitude(Double.parseDouble(c.getString(8)));
        setImage(c.getString(9));
    }

    public ContentValues toCV(){
        ContentValues cv = new ContentValues();
        cv.put(Constants.COLUMN_CATEGORY_EXPENSE, getCategory());
        cv.put(Constants.COLUMN_COST_EXPENSE, getCost());
        cv.put(Constants.COLUMN_AMOUNT_EXPENSE, getAmount());
        cv.put(Constants.COLUMN_DATE_EXPENSE, getDate());
        cv.put(Constants.COLUMN_COMMENT_EXPENSE, getComment());
        cv.put(Constants.COLUMN_TRIP_ID_EXPENSE, getTripId());
        cv.put(Constants.COLUMN_LATITUDE_EXPENSE, getLatitude());
        cv.put(Constants.COLUMN_LONGITUDE_EXPENSE, getLongitude());
        cv.put(Constants.COLUMN_IMAGE_PATH_EXPENSE, getImage());
        return cv;
    }

    @NonNull
    @Override
    public String toString() {
        return "Expense{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", date='" + date + '\'' +
                ", cost=" + cost +
                ", amount=" + amount +
                ", comment='" + comment + '\'' +
                ", tripId=" + tripId +
                ", latitude=" + latitude +
                ", longitude=" + longitude +
                '}';
    }

    // Getter and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCategory() {
        return name;
    }

    public void setCategory(String category) {
        this.name = category;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public double getCost() {
        return cost;
    }

    public void setCost(double cost) {
        this.cost = cost;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public int getTripId() {
        return tripId;
    }

    public void setTripId(int tripId) {
        this.tripId = tripId;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public String getImage() {
        return imagePath;
    }

    public void setImage(String imagePath) {
        this.imagePath = imagePath;
    }
}
