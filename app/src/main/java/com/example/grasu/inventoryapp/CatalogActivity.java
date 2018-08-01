package com.example.grasu.inventoryapp;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.example.grasu.inventoryapp.data.BooksContract;
import com.example.grasu.inventoryapp.data.BooksDbHelper;


public class CatalogActivity extends AppCompatActivity {
    private BooksDbHelper dbHelper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_catalog);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.add_book);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CatalogActivity.this, EditorActivity.class);
                startActivity(intent);
            }
        });
        dbHelper = new BooksDbHelper(this);
    }
    @Override
    protected void onStart() {
        super.onStart();
        displayDatabaseInfo();
    }
    private void displayDatabaseInfo() {

        SQLiteDatabase db = dbHelper.getReadableDatabase();

        String[] projection = {
                BooksContract.BooksEntry._ID,
                BooksContract.BooksEntry.COLUMN_BOOKS_PRODUCT,
                BooksContract.BooksEntry.COLUMN_BOOKS_PRICE,
               BooksContract.BooksEntry.COLUMN_BOOKS_QUANTITY,
                BooksContract.BooksEntry.COLUMN_BOOKS_SUPPLIER,
                BooksContract.BooksEntry.COLUMN_BOOKS_PHONE };


        Cursor cursor = db.query(
                BooksContract.BooksEntry.TABLE_NAME,   // The table to query
                projection,            // The columns to return
                null,                  // The columns for the WHERE clause
                null,                  // The values for the WHERE clause
                null,                  // Don't group the rows
                null,                  // Don't filter by row groups
                null);                   // The sort order

        TextView displayView = (TextView) findViewById(R.id.text_book);

        try {

            displayView.setText("The books table contains " + cursor.getCount() + " books.\n\n");
            displayView.append(BooksContract.BooksEntry._ID + " - " +
                    BooksContract.BooksEntry.COLUMN_BOOKS_PRODUCT + " - " +
                    BooksContract.BooksEntry.COLUMN_BOOKS_PRICE + " - " +
                    BooksContract.BooksEntry.COLUMN_BOOKS_QUANTITY + " - " +
                    BooksContract.BooksEntry.COLUMN_BOOKS_SUPPLIER + " - " +
                    BooksContract.BooksEntry.COLUMN_BOOKS_PHONE + "\n");


            int idColumnIndex = cursor.getColumnIndex(BooksContract.BooksEntry._ID);
            int productColumnIndex = cursor.getColumnIndex(BooksContract.BooksEntry.COLUMN_BOOKS_PRODUCT);
            int priceColumnIndex = cursor.getColumnIndex(BooksContract.BooksEntry.COLUMN_BOOKS_PRICE);
            int quantityColumnIndex = cursor.getColumnIndex(BooksContract.BooksEntry.COLUMN_BOOKS_QUANTITY);
            int supplierColumnIndex = cursor.getColumnIndex(BooksContract.BooksEntry.COLUMN_BOOKS_SUPPLIER);
            int phoneColumnIndex = cursor.getColumnIndex(BooksContract.BooksEntry.COLUMN_BOOKS_PHONE);

            // Iterate through all the returned rows in the cursor
            while (cursor.moveToNext()) {
                // Use that index to extract the String or Int value of the word
                // at the current row the cursor is on.
                int currentID = cursor.getInt(idColumnIndex);
                String currentProduct = cursor.getString(productColumnIndex);
                Double currentPrice = cursor.getDouble(priceColumnIndex);
                int currentQuantity = cursor.getInt(quantityColumnIndex);
                String currentSupplier = cursor.getString(supplierColumnIndex);
                int currentPhone = cursor.getInt(phoneColumnIndex);
                // Display the values from each column of the current row in the cursor in the TextView
                displayView.append(("\n" + currentID + " - " +
                        currentProduct + " - " +
                        currentPrice + " - " +
                        currentQuantity + " - " +
                        currentSupplier + " - " +
                        currentPhone));
            }
        } finally {
            // Always close the cursor when you're done reading from it. This releases all its
            // resources and makes it invalid.
            cursor.close();
        }
    }

    /**
     * Helper method to insert hardcoded pet data into the database. For debugging purposes only.
     */
    private void insertBooks() {
        // Gets the database in write mode
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        // Create a ContentValues object where column names are the keys,
        // and Toto's pet attributes are the values.
        ContentValues values = new ContentValues();
        values.put(BooksContract.BooksEntry.COLUMN_BOOKS_PRODUCT, "Walks with men");
        values.put(BooksContract.BooksEntry.COLUMN_BOOKS_PRICE, 10.00);
        values.put(BooksContract.BooksEntry.COLUMN_BOOKS_QUANTITY, 2);
        values.put(BooksContract.BooksEntry.COLUMN_BOOKS_SUPPLIER, "Amazon");
        values.put(BooksContract.BooksEntry.COLUMN_BOOKS_PHONE, 727213658);


        long newRowId = db.insert(BooksContract.BooksEntry.TABLE_NAME, null, values);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu options from the res/menu/menu_catalog.xml file.
        // This adds menu items to the app bar.
        getMenuInflater().inflate(R.menu.menu_catalog, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // User clicked on a menu option in the app bar overflow menu
        switch (item.getItemId()) {
            // Respond to a click on the "Insert dummy data" menu option
            case R.id.insert_dummy_data:
                insertBooks();
                displayDatabaseInfo();
                return true;
            // Respond to a click on the "Delete all entries" menu option
            case R.id.delete_all_entries:
                // Do nothing for now
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}


