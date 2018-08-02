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

    private EditText productName;

    private EditText price;

    private EditText quantity;

    private EditText supplier;
    private EditText supplierPhone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editor);

        productName = (EditText) findViewById(R.id.product_name);
        price = (EditText) findViewById(R.id.price);
        quantity = (EditText) findViewById(R.id.quantity);
        supplier = (EditText) findViewById(R.id.supplier_name);
        supplierPhone = (EditText) findViewById(R.id.supplier_phone);

    };

    private void insertBooks() {

        String productString = productName.getText().toString().trim();
        String priceString = price.getText().toString().trim();
        int price = Integer.parseInt(priceString);
        String quantityString = quantity.getText().toString().trim();
        int quantity = Integer.parseInt(quantityString);
        String supplierString = supplier.getText().toString().trim();
        String phoneString = supplierPhone.getText().toString().trim();
        int phone = Integer.parseInt(phoneString);

        BooksDbHelper dbHelper = new BooksDbHelper(this);

        SQLiteDatabase db = dbHelper.getWritableDatabase();
              ContentValues values = new ContentValues();
        values.put(BooksContract.BooksEntry.COLUMN_BOOKS_PRODUCT, productString);
        values.put(BooksContract.BooksEntry.COLUMN_BOOKS_PRICE, priceString);
        values.put(BooksContract.BooksEntry.COLUMN_BOOKS_QUANTITY, quantityString);
        values.put(BooksContract.BooksEntry.COLUMN_BOOKS_SUPPLIER, supplierString);
        values.put(BooksContract.BooksEntry.COLUMN_BOOKS_PHONE, phoneString);

        long newRowId = db.insert(BooksContract.BooksEntry.TABLE_NAME, null, values);
               if (newRowId == -1) {
             Toast.makeText(this, "Error with saving book", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Book saved with row id: " + newRowId, Toast.LENGTH_SHORT).show();
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
                }else{
                    if (productName.getText().toString().length() == 0){
                        Toast.makeText(this, getString(R.string.no_product_name), Toast.LENGTH_LONG).show();
                    }else {
                        if (price.getText().toString().length() == 0){
                            Toast.makeText(this, getString(R.string.no_price), Toast.LENGTH_LONG).show();
                        }else{
                            if (quantity.getText().toString().length() == 0){
                                Toast.makeText(this, getString(R.string.no_quantity), Toast.LENGTH_LONG).show();
                            }else {
                                if (supplierPhone.getText().toString().length() == 0) {
                                    Toast.makeText(this, getString(R.string.no_phone), Toast.LENGTH_LONG).show();
                                }else{
                                    insertBooks();

                                    finish();
                                }
                            }
                        }
                    }}

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

