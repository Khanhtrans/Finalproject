package com.example.mexpense.services;


import static com.example.mexpense.ultilities.Constants.COLUMN_CATEGORY_ID;
import static com.example.mexpense.ultilities.Constants.COLUMN_CATEGORY_NAME;
import static com.example.mexpense.ultilities.Constants.COLUMN_USER_EMAIL;
import static com.example.mexpense.ultilities.Constants.COLUMN_USER_ID;
import static com.example.mexpense.ultilities.Constants.COLUMN_USER_NAME;
import static com.example.mexpense.ultilities.Constants.COLUMN_USER_PASSWORD;
import static com.example.mexpense.ultilities.Constants.COLUMN_WALLET_CATEGORY;
import static com.example.mexpense.ultilities.Constants.COLUMN_WALLET_CURRENCY;
import static com.example.mexpense.ultilities.Constants.COLUMN_WALLET_ID;
import static com.example.mexpense.ultilities.Constants.COLUMN_WALLET_INITIAL_BALANCE;
import static com.example.mexpense.ultilities.Constants.COLUMN_WALLET_NAME;
import static com.example.mexpense.ultilities.Constants.COLUMN_WALLET_USER_ID;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

import com.example.mexpense.entity.User;
import com.example.mexpense.entity.Wallet;
import com.example.mexpense.ultilities.Constants;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class SqlService extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "mExpense";
    public static final String TRIPS_TABLE_NAME = "trips_table";
    public static final String EXPENSE_TABLE_NAME = "expenses_table";
    public static final String TABLE_USER = "user_table";
    public static final String TABLE_WALLET = "wallet_table";
    public static final String TABLE_CATEGORY = "category_table";

    private SQLiteDatabase database;

    public static SQLiteDatabase db;
    private static SqlService instance = null;
    private static Context context;

    public static SqlService getInstance(Context context){
        if(instance == null){
            instance = new SqlService(context.getApplicationContext());
        }
        return instance;
    }

    public SqlService(@Nullable Context context) {
        super(context, DATABASE_NAME, null, 2);
        database = getWritableDatabase();
    }

    private static final String TRIP_DATABASE_CREATE = String.format(
            "CREATE TABLE %s ( %s INTEGER PRIMARY KEY AUTOINCREMENT," +
                    " %s TEXT, " +
                    " %s TEXT, " +
                    " %s TEXT, " +
                    " %s TEXT, " +
                    " %s TEXT, " +
                    " %s INTEGER," +
                    " %s TEXT, " +
                    " %s REAL);",
            TRIPS_TABLE_NAME, Constants.COLUMN_ID_TRIP, Constants.COLUMN_NAME_TRIP, Constants.COLUMN_DESTINATION_TRIP, Constants.COLUMN_START_DATE_TRIP, Constants.COLUMN_END_DATE_TRIP, Constants.COLUMN_REQUIRED_ASSESSMENT_TRIP, Constants.COLUMN_PARTICIPANT_TRIP, Constants.COLUMN_DESCRIPTION_TRIP, Constants.COLUMN_TOTAL_TRIP
    );

    private static final String EXPENSE_DATABASE_CREATE = String.format(
            "CREATE TABLE %s (" +
                    " %s INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    " %s TEXT, " +
                    " %s REAL, " +
                    " %s INTEGER, " +
                    " %s TEXT, " +
                    " %s TEXT, " +
                    " %s INTEGER," +
                    " %s REAL," +
                    " %s REAL," +
                    " %s TEXT," +
                    " FOREIGN KEY(trip_id) REFERENCES trips_table(trip_id) ON DELETE CASCADE )",
            EXPENSE_TABLE_NAME, Constants.COLUMN_ID_EXPENSE, Constants.COLUMN_CATEGORY_EXPENSE, Constants.COLUMN_COST_EXPENSE, Constants.COLUMN_AMOUNT_EXPENSE, Constants.COLUMN_DATE_EXPENSE, Constants.COLUMN_COMMENT_EXPENSE, Constants.COLUMN_TRIP_ID_EXPENSE, Constants.COLUMN_LATITUDE_EXPENSE, Constants.COLUMN_LONGITUDE_EXPENSE, Constants.COLUMN_IMAGE_PATH_EXPENSE
    );

    // create tbl wallet
    private static final String CREATE_WALLET_TABLE = "CREATE TABLE " + TABLE_WALLET + "("
            + COLUMN_WALLET_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + COLUMN_WALLET_NAME + " TEXT,"
            + COLUMN_WALLET_CURRENCY + " TEXT,"
            + COLUMN_WALLET_USER_ID + " TEXT,"
            + COLUMN_WALLET_INITIAL_BALANCE + " MONEY"
            + ")";
    // drop table sql query
    private static final String DROP_WALLET_TABLE = "DROP TABLE IF EXISTS " + TABLE_WALLET;
    //create user table
    private static final String CREATE_USER_TABLE = "CREATE TABLE " + TABLE_USER + "("
            + COLUMN_USER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + COLUMN_USER_NAME + " TEXT,"
            + COLUMN_USER_EMAIL + " TEXT," + COLUMN_USER_PASSWORD + " TEXT" + ")";
    // drop table sql query
    private static final String DROP_USER_TABLE = "DROP TABLE IF EXISTS " + TABLE_USER;

    private static final String CREATE_CATEGORY_TABLE = "CREATE TABLE " + TABLE_CATEGORY + "("
            + COLUMN_CATEGORY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + COLUMN_CATEGORY_NAME + " TEXT" + ")";
    // drop table sql query
    private static final String DROP_CATEGORY_TABLE = "DROP TABLE IF EXISTS " + TABLE_CATEGORY;

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        this.database = sqLiteDatabase;
        database.execSQL(TRIP_DATABASE_CREATE);
        database.execSQL(EXPENSE_DATABASE_CREATE);
        database.execSQL(CREATE_USER_TABLE);
        database.execSQL(CREATE_WALLET_TABLE);
        database.execSQL(CREATE_CATEGORY_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        try{
            database.execSQL("DROP TABLE IF EXISTS " + EXPENSE_TABLE_NAME);
            database.execSQL("DROP TABLE IF EXISTS " + TRIPS_TABLE_NAME);
            database.execSQL(DROP_USER_TABLE);
            database.execSQL(DROP_USER_TABLE);
            database.execSQL(DROP_CATEGORY_TABLE);
        }
        catch (Exception e){
            Log.i("SQLITE DATABASE", "onUpgrade: " + e);
        }
        onCreate(database);
    }

    public boolean doesTableExist(String tableName) {
        Cursor cursor = database.rawQuery("select DISTINCT tbl_name from sqlite_master where tbl_name = '" + tableName + "'", null);

        if (cursor != null) {
            if (cursor.getCount() > 0) {
                cursor.close();
                return true;
            }
            cursor.close();
        }
        return false;
    }

    public List<String> fetchPayload(){
        List<String> payload = new ArrayList<>();
        Gson g = new Gson();
        database = this.getReadableDatabase();
        Cursor c = database.rawQuery( String.format("SELECT %s AS name, %s AS date, %s AS trip, %s AS amount, %s AS cost, %s AS destination, %s AS comment FROM %s a, %s b WHERE a.%s = b.%s",
                Constants.COLUMN_CATEGORY_EXPENSE, Constants.COLUMN_DATE_EXPENSE, Constants.COLUMN_NAME_TRIP,
                Constants.COLUMN_AMOUNT_EXPENSE, Constants.COLUMN_COST_EXPENSE, Constants.COLUMN_DESTINATION_TRIP,
                Constants.COLUMN_COMMENT_EXPENSE, EXPENSE_TABLE_NAME, TRIPS_TABLE_NAME, Constants.COLUMN_TRIP_ID_EXPENSE, Constants.COLUMN_ID_TRIP), null);
        while (c.moveToNext()){
            payload.add(g.toJson(new CloudData(c)));
        }
        return payload;
    }

    /**
     * This method is to create user record
     *
     * @param user
     */
    public void addUser(User user) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_USER_NAME, user.getName());
        values.put(COLUMN_USER_EMAIL, user.getEmail());
        values.put(COLUMN_USER_PASSWORD, user.getPassword());
        // Inserting Row
        db.insert(TABLE_USER, null, values);
        db.close();
    }
    /**
     * This method is to fetch all user and return the list of user records
     *
     * @return list
     */
    public List<User> getAllUser() {
        // array of columns to fetch
        String[] columns = {
                COLUMN_USER_ID,
                COLUMN_USER_EMAIL,
                COLUMN_USER_NAME,
                COLUMN_USER_PASSWORD
        };
        // sorting orders
        String sortOrder =
                COLUMN_USER_NAME + " ASC";
        List<User> userList = new ArrayList<User>();
        SQLiteDatabase db = this.getReadableDatabase();
        // query the user table
        /**
         * Here query function is used to fetch records from user table this function works like we use sql query.
         * SQL query equivalent to this query function is
         * SELECT user_id,user_name,user_email,user_password FROM user ORDER BY user_name;
         */
        Cursor cursor = db.query(TABLE_USER, //Table to query
                columns,    //columns to return
                null,        //columns for the WHERE clause
                null,        //The values for the WHERE clause
                null,       //group the rows
                null,       //filter by row groups
                sortOrder); //The sort order
        // Traversing through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                User user = new User();
                user.setId(Integer.parseInt(cursor.getString(cursor.getColumnIndex(COLUMN_USER_ID))));
                user.setName(cursor.getString(cursor.getColumnIndex(COLUMN_USER_NAME)));
                user.setEmail(cursor.getString(cursor.getColumnIndex(COLUMN_USER_EMAIL)));
                user.setPassword(cursor.getString(cursor.getColumnIndex(COLUMN_USER_PASSWORD)));
                // Adding user record to list
                userList.add(user);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        // return user list
        return userList;
    }
    /**
     * This method to update user record
     *
     * @param user
     */
    public void updateUser(User user) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_USER_NAME, user.getName());
        values.put(COLUMN_USER_EMAIL, user.getEmail());
        values.put(COLUMN_USER_PASSWORD, user.getPassword());
        // updating row
        db.update(TABLE_USER, values, COLUMN_USER_ID + " = ?",
                new String[]{String.valueOf(user.getId())});
        db.close();
    }
    /**
     * This method is to delete user record
     *
     * @param user
     */
    public void deleteUser(User user) {
        SQLiteDatabase db = this.getWritableDatabase();
        // delete user record by id
        db.delete(TABLE_USER, COLUMN_USER_ID + " = ?",
                new String[]{String.valueOf(user.getId())});
        db.close();
    }
    /**
     * This method to check user exist or not
     *
     * @param email
     * @return true/false
     */
    public boolean checkUser(String email) {
        // array of columns to fetch
        String[] columns = {
                COLUMN_USER_ID
        };
        SQLiteDatabase db = this.getReadableDatabase();
        // selection criteria
        String selection = COLUMN_USER_EMAIL + " = ?";
        // selection argument
        String[] selectionArgs = {email};
        // query user table with condition
        /**
         * Here query function is used to fetch records from user table this function works like we use sql query.
         * SQL query equivalent to this query function is
         * SELECT user_id FROM user WHERE user_email = 'jack@androidtutorialshub.com';
         */
        Cursor cursor = db.query(TABLE_USER, //Table to query
                columns,                    //columns to return
                selection,                  //columns for the WHERE clause
                selectionArgs,              //The values for the WHERE clause
                null,                       //group the rows
                null,                      //filter by row groups
                null);                      //The sort order
        int cursorCount = cursor.getCount();
        cursor.close();
        db.close();
        if (cursorCount > 0) {
            return true;
        }
        return false;
    }
    /**
     * This method to check user exist or not
     *
     * @param email
     * @param password
     * @return true/false
     */
    public boolean checkUser(String email, String password) {
        // array of columns to fetch
        String[] columns = {
                COLUMN_USER_ID
        };
        SQLiteDatabase db = this.getReadableDatabase();
        // selection criteria
        String selection = COLUMN_USER_EMAIL + " = ?" + " AND " + COLUMN_USER_PASSWORD + " = ?";
        // selection arguments
        String[] selectionArgs = {email, password};
        // query user table with conditions
        /**
         * Here query function is used to fetch records from user table this function works like we use sql query.
         * SQL query equivalent to this query function is
         * SELECT user_id FROM user WHERE user_email = 'jack@androidtutorialshub.com' AND user_password = 'qwerty';
         */
        Cursor cursor = db.query(TABLE_USER, //Table to query
                columns,                    //columns to return
                selection,                  //columns for the WHERE clause
                selectionArgs,              //The values for the WHERE clause
                null,                       //group the rows
                null,                       //filter by row groups
                null);                      //The sort order
        int cursorCount = cursor.getCount();
        cursor.close();
        db.close();
        if (cursorCount > 0) {
            return true;
        }
        return false;
    }

    /**
     * This method is to create wallet record
     *
     * @param wallet
     */
    public void addWallet(Wallet wallet) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_WALLET_NAME, wallet.getName());
        values.put(COLUMN_WALLET_CURRENCY, wallet.getCurrency());
        values.put(COLUMN_WALLET_INITIAL_BALANCE, wallet.getInitialBalance());
        values.put(COLUMN_WALLET_CATEGORY, wallet.getCategory());
        values.put(COLUMN_WALLET_USER_ID, wallet.getUserId());
        // Inserting Row
        db.insert(TABLE_USER, null, values);
        db.close();
    }
    /**
     * This method is to fetch all wallet and return the list of wallet records
     *
     * @return list
     */
    public List<Wallet> getAllWallet() {
        // array of columns to fetch
        String[] columns = {
                COLUMN_WALLET_ID,
                COLUMN_WALLET_NAME,
                COLUMN_WALLET_CURRENCY,
                COLUMN_WALLET_INITIAL_BALANCE,
                COLUMN_WALLET_CATEGORY,
                COLUMN_WALLET_USER_ID
        };
        // sorting orders
        String sortOrder =
                COLUMN_WALLET_NAME + " ASC";
        List<Wallet> walletList = new ArrayList<Wallet>();
        SQLiteDatabase db = this.getReadableDatabase();
        // query the wallet table
        Cursor cursor = db.query(TABLE_WALLET, //Table to query
                columns,    //columns to return
                null,        //columns for the WHERE clause
                null,        //The values for the WHERE clause
                null,       //group the rows
                null,       //filter by row groups
                sortOrder); //The sort order
        // Traversing through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Wallet wallet = new Wallet();
                wallet.setId(Integer.parseInt(cursor.getString(cursor.getColumnIndex(COLUMN_WALLET_ID))));
                wallet.setName(cursor.getString(cursor.getColumnIndex(COLUMN_WALLET_NAME)));
                wallet.setCurrency(cursor.getString(cursor.getColumnIndex(COLUMN_WALLET_CURRENCY)));
                wallet.setInitialBalance(cursor.getLong(cursor.getColumnIndex(COLUMN_WALLET_INITIAL_BALANCE)));
                wallet.setCategory(cursor.getString(cursor.getColumnIndex(COLUMN_WALLET_CATEGORY)));
                // Adding wallet record to list
                walletList.add(wallet);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        // return user list
        return walletList;
    }

    public List<Wallet> getMyWallets(int userID) {
        List<Wallet> wallets = getAllWallet();
        List<Wallet> mine = wallets.stream().filter(w -> w.getUserId() == userID).collect(Collectors.toList());
        return mine;
    }

    public Long totalMoney(int userID){
        List<Wallet> wallets = getMyWallets(userID);
        long total = 0;
        for (int i = 0; i < wallets.size(); i++) {
            total += wallets.get(i).getInitialBalance();
        }
        return total;
    }
}

class CloudData {
    private String name;
    private String date;
    private String trip;
    private int amount;
    private double cost;
    private String destination;
    private String comment;

    public CloudData(Cursor c){
        name = c.getString(0);
        date = c.getString(1);
        trip = c.getString(2);
        amount = c.getInt(3);
        cost = c.getDouble(4);
        destination = c.getString(5);
        comment = c.getString(6);
    }
}

