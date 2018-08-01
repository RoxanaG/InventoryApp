package com.example.grasu.inventoryapp;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.grasu.inventoryapp.data.BooksContract;
import com.example.grasu.inventoryapp.data.BooksDbHelper;

public class EditorActivity extends AppCompatActivity {

    /**
     * EditText field to enter the pet's name
     */
    private EditText productName;

    /**
     * EditText field to enter the pet's breed
     */
    private EditText price;

    /**
     * EditText field to enter the pet's weight
     */
    private EditText quantity;

    /**
     * EditText field to enter the pet's gender
     */
    private EditText supplier;
    private EditText supplierPhone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editor);

        // Find all relevant views that we will need to read user input from
        productName = (EditText) findViewById(R.id.product_name);
        price = (EditText) findViewById(R.id.price);
        quantity = (EditText) findViewById(R.id.quantity);
        supplier = (EditText) findViewById(R.id.supplier_name);
        supplierPhone = (EditText) findViewById(R.id.supplier_phone);

    }

    ;


    /**
     * Get user input from editor and save new pet into database.
     */
    private void insertBooks() {
        // Read from input fields
        // Use trim to eliminate leading or trailing white space
        String productString = productName.getText().toString().trim();
        String priceString = price.getText().toString().trim();
        int price = Integer.parseInt(priceString);
        String quantityString = quantity.getText().toString().trim();
        int quantity = Integer.parseInt(quantityString);
        String supplierString = supplier.getText().toString().trim();
        String phoneString = supplierPhone.getText().toString().trim();
        int phone = Integer.parseInt(phoneString);

        // Create database helper
        BooksDbHelper dbHelper = new BooksDbHelper(this);

        // Gets the database in write mode
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        // Create a ContentValues object where column names are the keys,
        // and pet attributes from the editor are the values.
        ContentValues values = new ContentValues();
        values.put(BooksContract.BooksEntry.COLUMN_BOOKS_PRODUCT, productString);
        values.put(BooksContract.BooksEntry.COLUMN_BOOKS_PRICE, priceString);
        values.put(BooksContract.BooksEntry.COLUMN_BOOKS_QUANTITY, quantityString);
        values.put(BooksContract.BooksEntry.COLUMN_BOOKS_SUPPLIER, supplierString);
        values.put(BooksContract.BooksEntry.COLUMN_BOOKS_PHONE, phoneString);

        // Insert a new row for pet in the database, returning the ID of that new row.
        long newRowId = db.insert(BooksContract.BooksEntry.TABLE_NAME, null, values);

        // Show a toast message depending on whether or not the insertion was successful
        if (newRowId == -1) {
            // If the row ID is -1, then there was an error with insertion.
            Toast.makeText(this, "Error with saving book", Toast.LENGTH_SHORT).show();
        } else {
            // Otherwise, the insertion was successful and we can display a toast with the row ID.
            Toast.makeText(this, "Book saved with row id: " + newRowId, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu options from the res/menu/menu_editor.xml file.
        // This adds menu items to the app bar.
        getMenuInflater().inflate(R.menu.menu_editor, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // User clicked on a menu option in the app bar overflow menu
        switch (item.getItemId()) {
            // Respond to a click on the "Save" menu option
            case R.id.save:
                          // Save pet to database
                insertBooks();
                // Exit activity
                finish();
                return true;
            // Respond to a click on the "Delete" menu option
            case R.id.delete:

                return true;
            // Respond to a click on the "Up" arrow button in the app bar
            case android.R.id.home:
                // Navigate back to parent activity (CatalogActivity)
                NavUtils.navigateUpFromSameTask(this);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}

