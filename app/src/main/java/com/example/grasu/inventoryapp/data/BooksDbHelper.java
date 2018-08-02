package com.example.grasu.inventoryapp.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


public class BooksDbHelper extends SQLiteOpenHelper {

    public static final String LOG_TAG = BooksDbHelper.class.getSimpleName();

    private static final String DATABASE_NAME = "inventory.db";

      private static final int DATABASE_VERSION = 1;


    public BooksDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        String SQL_CREATE_BOOKS_TABLE =  "CREATE TABLE " + BooksContract.BooksEntry.TABLE_NAME + " ("
                + BooksContract.BooksEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + BooksContract.BooksEntry.COLUMN_BOOKS_PRODUCT + " TEXT NOT NULL, "
                + BooksContract.BooksEntry.COLUMN_BOOKS_PRICE + " DECIMAL NOT NULL DEFAULT 0, "
                + BooksContract.BooksEntry.COLUMN_BOOKS_QUANTITY + " INTEGER NOT NULL, "
                + BooksContract.BooksEntry.COLUMN_BOOKS_SUPPLIER + " TEXT, "
                + BooksContract.BooksEntry.COLUMN_BOOKS_PHONE + " INTEGER );";

              db.execSQL(SQL_CREATE_BOOKS_TABLE);
    }

       @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
