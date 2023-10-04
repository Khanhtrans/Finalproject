package com.example.mexpense.services;

import android.content.Context;

import androidx.lifecycle.MutableLiveData;

import com.example.mexpense.entity.Expense;
import com.example.mexpense.repository.ExpenseRepository;
import com.example.mexpense.ultilities.Utilities;

import java.util.List;

public class ExpenseService {
    ExpenseRepository repository;

    public ExpenseService(Context context) {
        repository = new ExpenseRepository(context);
    }

    public void getExpenses(MutableLiveData<List<Expense>> expenseList, int tripId) {
        List<Expense> expenses = repository.getExpenses(tripId);
        expenseList.setValue(expenses);
    }

    public void deleteExpense(int id) {
        repository.deleteExpenses(id);
    }

    public void updateExpense(int id, Expense expense) {
        try{
            expense.setDate(Utilities.convertDate(expense.getDate(), true));
        } catch (Exception ignored){}
        repository.updateExpense(id, expense);
    }

    public void getExpenseById(MutableLiveData<Expense> expense, int id) {
        Expense e;
        if (id == -1) {
            e = new Expense();
        } else e = repository.getExpenseById(id);
        expense.setValue(e);
    }

    public void addExpense(Expense expense) {
        repository.addExpense(expense);
    }

}
