package com.example.mexpense.ultilities;

public class Constants {
    public static final int NEW_EXPENSE = -1;
    public static final int NEW_TRIP = -1;

    public static final String DATE_FORMAT = "dd MMM, yyyy";
    public static final String DATE_FORMAT_DATABASE = "yyyy-MM-dd";

    public static final String[] categories = {"Travel", "Flight", "Telephone", "Mortgage", "Meals", "Refreshments", "Gifts", "Medical", "Printing"};
    public static final String[] trips = {"Meeting", "Conference", "Exhibit", "Event", "Team-building"};

    public static final String LIMIT_START_DATE = "1910-01-01";
    public static final String LIMIT_END_DATE = "2099-01-01";

    public static final String COLUMN_ID_TRIP = "trip_id";
    public static final String COLUMN_NAME_TRIP = "name"; // Required
    public static final String COLUMN_DESTINATION_TRIP = "destination"; // Required
    public static final String COLUMN_START_DATE_TRIP = "startDate"; // Required
    public static final String COLUMN_END_DATE_TRIP = "endDate"; // Required
    public static final String COLUMN_REQUIRED_ASSESSMENT_TRIP = "assessment"; // Optional
    public static final String COLUMN_PARTICIPANT_TRIP = "participants";
    public static final String COLUMN_DESCRIPTION_TRIP = "description";
    public static final String COLUMN_TOTAL_TRIP = "total";

    public static final String COLUMN_ID_EXPENSE = "expense_id";
    public static final String COLUMN_CATEGORY_EXPENSE = "category"; // Required
    public static final String COLUMN_COST_EXPENSE = "cost"; // Required
    public static final String COLUMN_AMOUNT_EXPENSE = "amount";
    public static final String COLUMN_DATE_EXPENSE = "date"; // Required
    public static final String COLUMN_COMMENT_EXPENSE = "comment"; // Optional
    public static final String COLUMN_TRIP_ID_EXPENSE = "trip_id";
    public static final String COLUMN_LATITUDE_EXPENSE = "latitude";
    public static final String COLUMN_LONGITUDE_EXPENSE = "longitude";
    public static final String COLUMN_IMAGE_PATH_EXPENSE = "image";
    public static final String EXPENSE_TABLE_NAME = "expenses_table";

    public static final String CHARACTERS_ONLY_MESSAGE = "Must not contain special characters";

    public static final String EMPTY_FIELD_MESSAGE = "Please fill this field";
    //user
    public static final String COLUMN_USER_ID = "user_id";
    public static final String COLUMN_USER_NAME = "user_name";
    public static final String COLUMN_USER_EMAIL = "user_email";
    public static final String COLUMN_USER_PASSWORD = "user_password";
    public static final String COLUMN_USER_TOTAL = "user_total";

    public static final String COLUMN_CATEGORY_ID = "category_id";
    public static final String COLUMN_CATEGORY_NAME = "category_name";

    //wallet
    public static final String COLUMN_WALLET_ID = "wallet_id";
    public static final String COLUMN_WALLET_CURRENCY = "wallet_currency";
    public static final String COLUMN_WALLET_NAME = "wallet_name";
    public static final String COLUMN_WALLET_INITIAL_BALANCE = "wallet_initial_balance";
    public static final String COLUMN_WALLET_CATEGORY = "wallet_category";
    public static final String COLUMN_WALLET_DAY_ADD = "wallet_day_add";
    public static final String COLUMN_WALLET_USER_ID = "user_id";

    // TRANSACTION
    public static final String COLUMN_TRANS_ID = "trans_id";
    public static final String COLUMN_TRANS_NAME = "trans_name";
    public static final String COLUMN_TRANS_CATEGORY = "trans_category";
    public static final String COLUMN_TRANS_AMOUNT = "trans_amount";
    public static final String COLUMN_TRANS_NOTE = "trans_note";
    public static final String COLUMN_TRANS_DATE = "trans_date";
    public static final String COLUMN_TRANS_RETURN_DATE = "trans_return_date";
    public static final String COLUMN_TRANS_DESTINATION = "trans_destination";
    public static final String COLUMN_TRANS_TRANSPORTATION = "trans_transportation";
    public static final String COLUMN_TRANS_BILL = "trans_bill";
    public static final String COLUMN_TRANS_STATUS = "trans_status";
    public static final String COLUMN_TRANS_WALLET_ID = "wallet_id";
    public static final String COLUMN_TRANS_USER_ID = "user_id";




// share preference

    public static final String SHARE_NAME = "my_share";
    public static final String KEY_NAME = "key_name";
    public static final String KEY_USER_ID = "key_user_id";
    public static final String KEY_TOTAL = "key_total";
    public static final String KEY_EMAIL = "key_email";
    public static final String KEY_IS_LOGIN = "key_is_login";


}
