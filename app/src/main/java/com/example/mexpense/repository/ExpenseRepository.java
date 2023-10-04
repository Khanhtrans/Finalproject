package com.example.mexpense.repository;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.mexpense.entity.Expense;
import com.example.mexpense.services.SqlService;
import com.example.mexpense.ultilities.Constants;

import java.util.ArrayList;
import java.util.List;

public class ExpenseRepository {

    public static final String TABLE_NAME = "expenses_table";

    private Context context;

    public ExpenseRepository(Context context){
        this.context = context;
    }

    public List<Expense> getExpenses(int trip) {
        List<Expense> expenses = new ArrayList<>();
        SQLiteDatabase db = SqlService.getInstance(context).getReadableDatabase();
        Cursor c = db.rawQuery("SELECT * FROM " + TABLE_NAME, null);
        while (c.moveToNext()) {
            if (Integer.parseInt(c.getString(6)) == trip) {
                expenses.add(new Expense(c));
            }
        }
        return expenses;
    }

    public Expense getExpenseById(int id) {
        Expense e = new Expense();
        SQLiteDatabase db = SqlService.getInstance(context).getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_NAME + " WHERE " + Constants.COLUMN_ID_EXPENSE + "=" + id, null);
        while (cursor.moveToNext()) {
            e = new Expense(cursor);
        }
        return e;
    }

    public void addExpense(Expense expense) {
        SQLiteDatabase db = SqlService.getInstance(context).getWritableDatabase();
        db.insert(TABLE_NAME, Constants.COLUMN_COMMENT_EXPENSE, expense.toCV());
    }

    public void deleteExpenses(int id) {
        SQLiteDatabase db = SqlService.getInstance(context).getWritableDatabase();
        db.delete(TABLE_NAME, Constants.COLUMN_ID_EXPENSE + "=?", new String[]{String.valueOf(id)});
    }

    public void updateExpense(int id, Expense expense) {
        SQLiteDatabase db = SqlService.getInstance(context).getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(Constants.COLUMN_CATEGORY_EXPENSE, expense.getCategory());
        cv.put(Constants.COLUMN_COST_EXPENSE, expense.getCost());
        cv.put(Constants.COLUMN_AMOUNT_EXPENSE, expense.getAmount());
        cv.put(Constants.COLUMN_DATE_EXPENSE, expense.getDate());
        cv.put(Constants.COLUMN_COMMENT_EXPENSE, expense.getComment());
        cv.put(Constants.COLUMN_IMAGE_PATH_EXPENSE, expense.getImage());
        db.update(TABLE_NAME, cv, Constants.COLUMN_ID_EXPENSE + "=?", new String[]{String.valueOf(id)});
    }
}
