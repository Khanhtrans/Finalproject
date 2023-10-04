package com.example.mexpense.entity;

import android.content.ContentValues;
import android.database.Cursor;

import com.example.mexpense.ultilities.Constants;
import com.google.gson.Gson;

import java.util.Objects;

public class Trip {
    private int id;
    private String name;
    private String destination;
    private String startDate;
    private String endDate;
    private boolean requiredAssessment;
    private int participants;
    private String description;
    private double total;

    public Trip() {
        this.id = Constants.NEW_TRIP;
        this.name = "";
        this.destination = "";
        this.startDate = "";
        this.endDate = "";
        this.requiredAssessment = false;
        this.participants = 0;
        this.description = "";
    }

    public Trip(int id, String name, String destination, String startDate, String endDate, boolean requiredAssessment, int participants, String description) {
        this.id = id;
        this.name = name;
        this.destination = destination;
        this.startDate = startDate;
        this.endDate = endDate;
        this.requiredAssessment = requiredAssessment;
        this.participants = participants;
        this.description = description;
        this.total = 0;
    }

    public Trip(Cursor c){
        id = c.getInt(0);
        name = c.getString(1);
        destination = c.getString(2);
        startDate = c.getString(3);
        endDate = c.getString(4);
        requiredAssessment = Objects.equals(c.getString(5), "1");
        participants = Integer.parseInt(c.getString(6));
        description = c.getString(7);
        total = Double.parseDouble(c.getString(8));
    }

    public ContentValues toCV(){
        ContentValues cv = new ContentValues();
        cv.put(Constants.COLUMN_NAME_TRIP , getName());
        cv.put(Constants.COLUMN_DESTINATION_TRIP , getDestination());
        cv.put(Constants.COLUMN_START_DATE_TRIP , getStartDate());
        cv.put(Constants.COLUMN_END_DATE_TRIP , getEndDate());
        cv.put(Constants.COLUMN_REQUIRED_ASSESSMENT_TRIP , getRequiredAssessment() ? "1" : "0");
        cv.put(Constants.COLUMN_PARTICIPANT_TRIP , getParticipants());
        cv.put(Constants.COLUMN_DESCRIPTION_TRIP , getDescription());
        cv.put(Constants.COLUMN_TOTAL_TRIP, getTotal());
        return cv;
    }

    public Trip(int id, String name, String destination, String startDate, String endDate, boolean requiredAssessment, int participants, String description, Double total) {
        this.id = id;
        this.name = name;
        this.destination = destination;
        this.startDate = startDate;
        this.endDate = endDate;
        this.requiredAssessment = requiredAssessment;
        this.participants = participants;
        this.description = description;
        this.total = total;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String date) {
        this.startDate = date;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public boolean getRequiredAssessment() {
        return requiredAssessment;
    }

    public void setRequiredAssessment(boolean requiredAssessment) {
        this.requiredAssessment = requiredAssessment;
    }

    public int getParticipants() {
        return participants;
    }

    public void setParticipants(int participants) {
        this.participants = participants;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return "Trip{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", destination='" + destination + '\'' +
                ", startDate='" + startDate + '\'' +
                ", endDate='" + endDate + '\'' +
                ", requiredAssessment=" + requiredAssessment +
                ", participants=" + participants +
                ", description='" + description + '\'' +
                ", total=" + total +
                '}';
    }

}
