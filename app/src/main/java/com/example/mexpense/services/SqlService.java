package com.example.mexpense.services;


import static com.example.mexpense.ultilities.Constants.COLUMN_NOTIFY_CONTENT;
import static com.example.mexpense.ultilities.Constants.COLUMN_NOTIFY_DATE;
import static com.example.mexpense.ultilities.Constants.COLUMN_NOTIFY_ID;
import static com.example.mexpense.ultilities.Constants.COLUMN_NOTIFY_USER_ID;
import static com.example.mexpense.ultilities.Constants.COLUMN_TRANS_AMOUNT;
import static com.example.mexpense.ultilities.Constants.COLUMN_TRANS_BILL;
import static com.example.mexpense.ultilities.Constants.COLUMN_TRANS_CATEGORY;
import static com.example.mexpense.ultilities.Constants.COLUMN_TRANS_DATE;
import static com.example.mexpense.ultilities.Constants.COLUMN_TRANS_DESTINATION;
import static com.example.mexpense.ultilities.Constants.COLUMN_TRANS_ID;
import static com.example.mexpense.ultilities.Constants.COLUMN_TRANS_NAME;
import static com.example.mexpense.ultilities.Constants.COLUMN_TRANS_NOTE;
import static com.example.mexpense.ultilities.Constants.COLUMN_TRANS_RETURN_DATE;
import static com.example.mexpense.ultilities.Constants.COLUMN_TRANS_STATUS;
import static com.example.mexpense.ultilities.Constants.COLUMN_TRANS_TRANSPORTATION;
import static com.example.mexpense.ultilities.Constants.COLUMN_TRANS_USER_ID;
import static com.example.mexpense.ultilities.Constants.COLUMN_TRANS_WALLET_ID;
import static com.example.mexpense.ultilities.Constants.COLUMN_USER_EMAIL;
import static com.example.mexpense.ultilities.Constants.COLUMN_USER_ID;
import static com.example.mexpense.ultilities.Constants.COLUMN_USER_NAME;
import static com.example.mexpense.ultilities.Constants.COLUMN_USER_PASSWORD;
import static com.example.mexpense.ultilities.Constants.COLUMN_USER_TOTAL;
import static com.example.mexpense.ultilities.Constants.COLUMN_WALLET_CATEGORY;
import static com.example.mexpense.ultilities.Constants.COLUMN_WALLET_CURRENCY;
import static com.example.mexpense.ultilities.Constants.COLUMN_WALLET_DAY_ADD;
import static com.example.mexpense.ultilities.Constants.COLUMN_WALLET_ID;
import static com.example.mexpense.ultilities.Constants.COLUMN_WALLET_INITIAL_BALANCE;
import static com.example.mexpense.ultilities.Constants.COLUMN_WALLET_NAME;
import static com.example.mexpense.ultilities.Constants.COLUMN_WALLET_USER_ID;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.example.mexpense.entity.Notification;
import com.example.mexpense.entity.Transaction;
import com.example.mexpense.entity.User;
import com.example.mexpense.entity.Wallet;
import com.example.mexpense.ultilities.Constants;
import com.google.gson.Gson;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.stream.Collectors;

public class SqlService extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "mExpense";
    public static final String TRIPS_TABLE_NAME = "trips_table";
    public static final String EXPENSE_TABLE_NAME = "expenses_table";
    public static final String TABLE_USER = "user_table";
    public static final String TABLE_WALLET = "wallet_table";
    public static final String TABLE_TRANSACION = "transaction_table";
    public static final String TABLE_NOTIFICATION = "notify_table";

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
            + COLUMN_WALLET_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + COLUMN_WALLET_NAME + " TEXT,"
            + COLUMN_WALLET_CURRENCY + " TEXT,"
            + COLUMN_WALLET_CATEGORY + " TEXT,"
            + COLUMN_WALLET_USER_ID + " TEXT,"
            + COLUMN_WALLET_DAY_ADD + " TEXT,"
            + COLUMN_WALLET_INITIAL_BALANCE + " MONEY"
            + ")";
    // drop table sql query
    private static final String DROP_WALLET_TABLE = "DROP TABLE IF EXISTS " + TABLE_WALLET;

    // create tbl transaction
    private static final String CREATE_TRANSACTION_TABLE = "CREATE TABLE " + TABLE_TRANSACION + "("
            + COLUMN_TRANS_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + COLUMN_TRANS_NAME + " TEXT,"
            + COLUMN_TRANS_AMOUNT + " MONEY,"
            + COLUMN_TRANS_CATEGORY + " TEXT,"
            + COLUMN_TRANS_DESTINATION + " TEXT,"
            + COLUMN_TRANS_NOTE + " TEXT,"
            + COLUMN_TRANS_TRANSPORTATION + " TEXT,"
            + COLUMN_TRANS_BILL + " TEXT,"
            + COLUMN_TRANS_STATUS + " TEXT,"
            + COLUMN_TRANS_USER_ID + " INTEGER,"
            + COLUMN_TRANS_WALLET_ID + " INTEGER,"
            + COLUMN_TRANS_DATE + " TEXT,"
            + COLUMN_TRANS_RETURN_DATE + " TEXT"
            + ")";
    // drop table sql query
    private static final String DROP_TRANS_TABLE = "DROP TABLE IF EXISTS " + TABLE_WALLET;
    //create user table
    private static final String CREATE_USER_TABLE = "CREATE TABLE "
            + TABLE_USER + "("
            + COLUMN_USER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + COLUMN_USER_NAME + " TEXT,"
            + COLUMN_USER_EMAIL + " TEXT," +
            COLUMN_USER_PASSWORD + " TEXT," +
            COLUMN_USER_TOTAL + " MONEY"
            + ")";
    // drop table sql query
    private static final String DROP_USER_TABLE = "DROP TABLE IF EXISTS " + TABLE_USER;

    private static final String CREATE_NOTIFY_TABLE = "CREATE TABLE " + TABLE_NOTIFICATION + "("
            + COLUMN_NOTIFY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + COLUMN_NOTIFY_CONTENT + " TEXT,"
            + COLUMN_NOTIFY_DATE + " TEXT,"
            + COLUMN_NOTIFY_USER_ID + " INTEGER"
            + ")";
    // drop table sql query
    private static final String DROP_NOTIFY_TABLE = "DROP TABLE IF EXISTS " + TABLE_NOTIFICATION;

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        this.database = sqLiteDatabase;
        database.execSQL(TRIP_DATABASE_CREATE);
        database.execSQL(EXPENSE_DATABASE_CREATE);
        database.execSQL(CREATE_USER_TABLE);
        database.execSQL(CREATE_WALLET_TABLE);
        database.execSQL(CREATE_NOTIFY_TABLE);
        database.execSQL(CREATE_TRANSACTION_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        try{
            database.execSQL("DROP TABLE IF EXISTS " + EXPENSE_TABLE_NAME);
            database.execSQL("DROP TABLE IF EXISTS " + TRIPS_TABLE_NAME);
            database.execSQL(DROP_USER_TABLE);
            database.execSQL(DROP_WALLET_TABLE);
            database.execSQL(DROP_NOTIFY_TABLE);
            database.execSQL(DROP_TRANS_TABLE);
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

    //add notify record
    public void addNotify(Notification notify) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_NOTIFY_CONTENT, notify.getContent());
        values.put(COLUMN_NOTIFY_DATE, notify.getDate());
        values.put(COLUMN_NOTIFY_USER_ID,notify.getUser_id());
        // Inserting Row
        db.insert(TABLE_NOTIFICATION, null, values);
        db.close();
    }

    /**
     * This method is to fetch all notifications and return the list of notification records
     *
     * @return list
     */
    @SuppressLint("Range")
    public List<Notification> getAllNotifications() {
        // array of columns to fetch
        String[] columns = {
                COLUMN_NOTIFY_ID,
                COLUMN_NOTIFY_CONTENT,
                COLUMN_NOTIFY_DATE,
                COLUMN_NOTIFY_USER_ID
        };
        // sorting orders
        String sortOrder =
                COLUMN_NOTIFY_DATE + " ASC";
        List<Notification> notifyList = new ArrayList<Notification>();
        SQLiteDatabase db = this.getReadableDatabase();
        // query the user table

        Cursor cursor = db.query(TABLE_NOTIFICATION, //Table to query
                columns,    //columns to return
                null,        //columns for the WHERE clause
                null,        //The values for the WHERE clause
                null,       //group the rows
                null,       //filter by row groups
                sortOrder); //The sort order
        // Traversing through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Notification notification = new Notification();
                notification.setId(Integer.parseInt(cursor.getString(cursor.getColumnIndex(COLUMN_NOTIFY_ID))));
                notification.setContent(cursor.getString(cursor.getColumnIndex(COLUMN_NOTIFY_CONTENT)));
                notification.setDate(cursor.getString(cursor.getColumnIndex(COLUMN_NOTIFY_DATE)));
                notification.setUser_id(cursor.getInt(cursor.getColumnIndex(COLUMN_NOTIFY_USER_ID)));
                // Adding user record to list
                notifyList.add(notification);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return notifyList;
    }

    public List<Notification> getMyNotify(int userID) {
        List<Notification> transactions = getAllNotifications();
        List<Notification> mine = new ArrayList<>();
        try {
            mine = transactions.stream().filter(w -> w.getUser_id() == userID).collect(Collectors.toList());
        } catch ( Exception e) {
            Log.e("Exception","No transaction found");
        }
        return mine;
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
        values.put(COLUMN_USER_TOTAL, user.getTotal());
        // Inserting Row
        db.insert(TABLE_USER, null, values);
        db.close();
    }

    @SuppressLint("Range")
    public User getUser(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        String sql = "SELECT * FROM " + TABLE_USER
                + " WHERE " + COLUMN_USER_ID + " = " + id;
        Cursor cursor = db.rawQuery(sql, null);
        User user = new User();

        // Read data, I simplify cursor in one line
        if (cursor.moveToFirst()) {

            // Get imageData in byte[]. Easy, right?
            user.setId(Integer.parseInt(cursor.getString(cursor.getColumnIndex(COLUMN_USER_ID))));
            user.setEmail(cursor.getString(cursor.getColumnIndex(COLUMN_USER_EMAIL)));
            user.setName(cursor.getString(cursor.getColumnIndex(COLUMN_USER_NAME)));
            user.setPassword(cursor.getString(cursor.getColumnIndex(COLUMN_USER_PASSWORD)));
            user.setTotal(cursor.getLong(cursor.getColumnIndex(COLUMN_USER_TOTAL)));
        }
        cursor.close();
        db.close();
        return user;
    }
    /**
     * This method is to fetch all user and return the list of user records
     *
     * @return list
     */
    @SuppressLint("Range")
    public List<User> getAllUser() {
        // array of columns to fetch
        String[] columns = {
                COLUMN_USER_ID,
                COLUMN_USER_EMAIL,
                COLUMN_USER_NAME,
                COLUMN_USER_PASSWORD,
                COLUMN_USER_TOTAL
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
                user.setTotal(cursor.getLong(cursor.getColumnIndex(COLUMN_USER_TOTAL)));
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
        values.put(COLUMN_USER_TOTAL, user.getTotal());
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
     * This method is to create transaction record
     *
     * @param transaction
     */
    public void addTrans(Transaction transaction) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        DateFormat sdf = new SimpleDateFormat("DD-MMM-YYYY", Locale.US);
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_TRANS_NAME, transaction.getName());
        values.put(COLUMN_TRANS_NOTE, transaction.getNote());
        values.put(COLUMN_TRANS_AMOUNT, transaction.getAmount());
        values.put(COLUMN_TRANS_CATEGORY, transaction.getCategory());

        values.put(COLUMN_TRANS_DATE, transaction.getDate() );
        values.put(COLUMN_TRANS_RETURN_DATE, transaction.getReturnDate() );


        values.put(COLUMN_TRANS_DESTINATION, transaction.getDestination());
        values.put(COLUMN_TRANS_TRANSPORTATION, transaction.getTransportation());
        values.put(COLUMN_TRANS_BILL, transaction.getBill());

        values.put(COLUMN_TRANS_STATUS, transaction.getStatus());

        values.put(COLUMN_TRANS_USER_ID, transaction.getUserId());

        values.put(COLUMN_TRANS_WALLET_ID, transaction.getWallet());
        // Inserting Row
        db.insert(TABLE_TRANSACION, null, values);
        db.close();
    }
    /**
     * This method is to fetch all wallet and return the list of wallet records
     *
     * @return list
     */
    @SuppressLint("Range")
    public List<Transaction> getAllTransaction() {
        // array of columns to fetch
        String[] columns = {
                COLUMN_TRANS_ID,
                COLUMN_TRANS_NAME,
                COLUMN_TRANS_NOTE,
                COLUMN_TRANS_AMOUNT,
                COLUMN_TRANS_CATEGORY,
                COLUMN_TRANS_DATE,
                COLUMN_TRANS_RETURN_DATE,
                COLUMN_TRANS_DESTINATION,
                COLUMN_TRANS_TRANSPORTATION,
                COLUMN_TRANS_BILL,
                COLUMN_TRANS_STATUS,
                COLUMN_TRANS_USER_ID,
                COLUMN_TRANS_WALLET_ID,
        };
        // sorting orders
        String sortOrder =
                COLUMN_TRANS_NAME + " ASC";
        List<Transaction> transactions = new ArrayList<Transaction>();
        SQLiteDatabase db = this.getReadableDatabase();
        // query the wallet table
        Cursor cursor = db.query(TABLE_TRANSACION, //Table to query
                columns,    //columns to return
                null,        //columns for the WHERE clause
                null,        //The values for the WHERE clause
                null,       //group the rows
                null,       //filter by row groups
                sortOrder); //The sort order
        // Traversing through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Transaction transaction = new Transaction();
                transaction.setId(Integer.parseInt(cursor.getString(cursor.getColumnIndex(COLUMN_TRANS_ID))));
                transaction.setName(cursor.getString(cursor.getColumnIndex(COLUMN_TRANS_NAME)));
                transaction.setNote(cursor.getString(cursor.getColumnIndex(COLUMN_TRANS_NOTE)));
                transaction.setAmount(cursor.getLong(cursor.getColumnIndex(COLUMN_TRANS_AMOUNT)));
                transaction.setDestination(cursor.getString(cursor.getColumnIndex(COLUMN_TRANS_DESTINATION)));
                transaction.setTransportation(cursor.getString(cursor.getColumnIndex(COLUMN_TRANS_TRANSPORTATION)));
                transaction.setBill(cursor.getString(cursor.getColumnIndex(COLUMN_TRANS_BILL)));
                transaction.setCategory(cursor.getString(cursor.getColumnIndex(COLUMN_TRANS_CATEGORY)));
                transaction.setDate(cursor.getString(cursor.getColumnIndex(COLUMN_TRANS_DATE)));
                transaction.setStatus(Boolean.parseBoolean(cursor.getString(cursor.getColumnIndex(COLUMN_TRANS_STATUS))));
                transaction.setReturnDate(cursor.getString(cursor.getColumnIndex(COLUMN_TRANS_RETURN_DATE)));
                transaction.setUserId(Integer.parseInt(cursor.getString(cursor.getColumnIndex(COLUMN_TRANS_USER_ID))));
                transaction.setUserId(Integer.parseInt(cursor.getString(cursor.getColumnIndex(COLUMN_TRANS_WALLET_ID))));
                // Adding wallet record to list
                transactions.add(transaction);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        // return user list
        return transactions;
    }

    public List<Transaction> getMyTransaction(int userID) {
        List<Transaction> transactions = getAllTransaction();
        List<Transaction> mine = new ArrayList<>();
        try {
            mine = transactions.stream().filter(w -> w.getUserId() == userID).collect(Collectors.toList());
        } catch ( Exception e) {
            Log.e("Exception","No transaction found");
        }
        return mine;
    }

    public Long getMySpend(int userID) {
        List<Transaction> myTrans = getMyTransaction(userID);
        Long total = 0L;
        for (int i =0;i <myTrans.size();i++) {
            total+= myTrans.get(i).getAmount();
        }
        return total;
    }

    public @Nullable Wallet getWalletFromID(int walletID) {
        List<Wallet> wallet = getAllWallet();
        return wallet.stream()
                .filter(w -> w.getId() == walletID)
                .findFirst()
                .orElse(null);
    }

    public Boolean checkNewTransAvaiToAdd(int walletId, int userId, Long newAmount) {
        List<Transaction> trans = getMyTransaction(userId);
        Wallet wallet = getWalletFromID(walletId);
        long totalSpend = 0L;
        for (int i = 0; i< trans.size(); i++) {
            if (trans.get(i).getWallet() != null && trans.get(i).getWallet() == walletId) {
                totalSpend += trans.get(i).getAmount();
            }
        }

        return newAmount < (Objects.requireNonNull(wallet).getInitialBalance() - totalSpend);
    }

    public Long totalMoney(int userID){
        List<Wallet> wallets = getMyWallets(userID);
        long total = 0;
        for (int i = 0; i < wallets.size(); i++) {
            total += wallets.get(i).getInitialBalance();
        }
        return total;
    }

    public void updateWallet(Wallet wallet) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_WALLET_NAME, wallet.getName());
        values.put(COLUMN_WALLET_CURRENCY, wallet.getCurrency());
        values.put(COLUMN_WALLET_INITIAL_BALANCE, wallet.getInitialBalance());
        values.put(COLUMN_WALLET_CATEGORY, wallet.getCategory());
        values.put(COLUMN_WALLET_DAY_ADD, wallet.getDayAdd());
        values.put(COLUMN_WALLET_USER_ID, wallet.getUserId());
        // updating row
        db.update(TABLE_WALLET, values, COLUMN_WALLET_ID + " = ?",
                new String[]{String.valueOf(wallet.getId())});
        db.close();
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
        values.put(COLUMN_WALLET_DAY_ADD, wallet.getDayAdd());
        values.put(COLUMN_WALLET_USER_ID, wallet.getUserId());
        // Inserting Row
        db.insert(TABLE_WALLET, null, values);
        db.close();
    }
    /**
     * This method is to fetch all wallet and return the list of wallet records
     *
     * @return list
     */
    @SuppressLint("Range")
    public List<Wallet> getAllWallet() {
        // array of columns to fetch
        String[] columns = {
                COLUMN_WALLET_ID,
                COLUMN_WALLET_NAME,
                COLUMN_WALLET_CURRENCY,
                COLUMN_WALLET_INITIAL_BALANCE,
                COLUMN_WALLET_CATEGORY,
                COLUMN_WALLET_DAY_ADD,
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
                wallet.setDayAdd(cursor.getString(cursor.getColumnIndex(COLUMN_WALLET_DAY_ADD)));
                wallet.setUserId(Integer.parseInt(cursor.getString(cursor.getColumnIndex(COLUMN_WALLET_USER_ID))));
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

