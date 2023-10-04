package com.example.mexpense.fragments.main.expense;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.mexpense.entity.Expense;
import com.example.mexpense.entity.Trip;

import java.util.List;

public class ExpenseMainViewModel extends ViewModel {
    public MutableLiveData<List<Expense>> expenseList = new MutableLiveData<List<Expense>>();
    public MutableLiveData<Trip> trip = new MutableLiveData<>();
}