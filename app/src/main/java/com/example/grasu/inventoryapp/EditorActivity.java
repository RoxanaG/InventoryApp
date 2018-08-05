package com.example.grasu.inventoryapp;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import com.example.grasu.inventoryapp.data.BooksContract;
import com.example.grasu.inventoryapp.data.BooksDbHelper;

public class EditorActivity extends AppCompatActivity {

    private EditText productName;
    private EditText price;
    private EditText quantity;
    private EditText supplier;
    private EditText supplierPhone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editor);

        productName = findViewById(R.id.product_name);
        price = findViewById(R.id.price);
        quantity = findViewById(R.id.quantity);
        supplier = findViewById(R.id.supplier_name);
        supplierPhone = findViewById(R.id.supplier_phone);
    }

    private void insertBooks() {

        String productString = productName.getText().toString().trim();
        String priceString = price.getText().toString().trim();
        double price = 0.0;
        try {
            price = Double.parseDouble(priceString);
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }

        String quantityString = quantity.getText().toString().trim();
        int quantity = 0;
        try {
            quantity = Integer.parseInt(quantityString);
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }

        String supplierString = supplier.getText().toString().trim();
        String phoneString = supplierPhone.getText().toString().trim();



        ContentValues values = new ContentValues();
        values.put(BooksContract.BooksEntry.COLUMN_BOOKS_PRODUCT, productString);
        values.put(BooksContract.BooksEntry.COLUMN_BOOKS_PRICE, priceString);
        values.put(BooksContract.BooksEntry.COLUMN_BOOKS_QUANTITY, quantityString);
        values.put(BooksContract.BooksEntry.COLUMN_BOOKS_SUPPLIER, supplierString);
        values.put(BooksContract.BooksEntry.COLUMN_BOOKS_PHONE, phoneString);

        Uri newUri = getContentResolver().insert(BooksContract.BooksEntry.CONTENT_URI, values);

        if (newUri == null) {
            Toast.makeText(this, getString(R.string.editor_insert_book_failed), Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, getString(R.string.editor_insert_book_successfull) , Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_editor, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.save:
                if (productName.getText().toString().length() == 0 && price.getText().toString().length() == 0 && quantity.getText().toString().length() == 0 && supplierPhone.getText().toString().length() == 0) {
                    Toast.makeText(this, getString(R.string.nothing_saved), Toast.LENGTH_LONG).show();
                } else {
                    if (productName.getText().toString().length() == 0) {
                        Toast.makeText(this, getString(R.string.no_product_name), Toast.LENGTH_LONG).show();
                    } else {
                        if (price.getText().toString().length() == 0) {
                            Toast.makeText(this, getString(R.string.no_price), Toast.LENGTH_LONG).show();
                        } else {
                            insertBooks();
                            finish();
                        }
                    }
                }
                return true;
            case R.id.delete:
                return true;
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}


