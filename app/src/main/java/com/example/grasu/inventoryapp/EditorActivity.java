package com.example.grasu.inventoryapp;

import android.app.AlertDialog;
import android.app.LoaderManager;
import android.content.ContentValues;
import android.content.CursorLoader;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.grasu.inventoryapp.data.BooksContract;
import com.example.grasu.inventoryapp.data.BooksDbHelper;

public class EditorActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {
    private static final int EXISTING_BOOKS_LOADER = 0;

    private Uri currentBooksUri;


    private EditText productName;
    private EditText price;
    private EditText quantity;
    private EditText supplier;
    private EditText supplierPhone;
    private boolean booksChanged = false;

    private View.OnTouchListener touchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            booksChanged = true;
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editor);



Intent intent = getIntent();
 currentBooksUri = intent.getData();

if (currentBooksUri == null){
    setTitle(getString(R.string.editor_add_books));
    invalidateOptionsMenu();
}else {
    setTitle(getString(R.string.editor_edit_books));
    getLoaderManager().initLoader(EXISTING_BOOKS_LOADER, null, this);

}


        productName = findViewById(R.id.product_name);
        price = findViewById(R.id.price);
        quantity = findViewById(R.id.quantity);
        supplier = findViewById(R.id.supplier_name);
        supplierPhone = findViewById(R.id.supplier_phone);

       productName.setOnTouchListener(touchListener);
        price.setOnTouchListener(touchListener);
        quantity.setOnTouchListener(touchListener);
        supplier.setOnTouchListener(touchListener);
        supplierPhone.setOnTouchListener(touchListener);
    }


    private void saveBooks() {

        String productString = productName.getText().toString().trim();
        String priceString = price.getText().toString().trim();
        double price = 0.0;
        try {
            price = Double.parseDouble(priceString);
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }

        String quantityString = quantity.getText().toString().trim();


        String supplierString = supplier.getText().toString().trim();
        String phoneString = supplierPhone.getText().toString().trim();

        if (currentBooksUri == null &&
                TextUtils.isEmpty(productString) && TextUtils.isEmpty(priceString) &&
                TextUtils.isEmpty(quantityString) && TextUtils.isEmpty(supplierString) && TextUtils.isEmpty(phoneString)) {return;}

        if (!TextUtils.isEmpty(quantityString)) {
            int quantity = 0;
            quantity = Integer.parseInt(quantityString);
        }

        ContentValues values = new ContentValues();
        values.put(BooksContract.BooksEntry.COLUMN_BOOKS_PRODUCT, productString);
        values.put(BooksContract.BooksEntry.COLUMN_BOOKS_PRICE, priceString);
        values.put(BooksContract.BooksEntry.COLUMN_BOOKS_QUANTITY, quantityString);
        values.put(BooksContract.BooksEntry.COLUMN_BOOKS_SUPPLIER, supplierString);
        values.put(BooksContract.BooksEntry.COLUMN_BOOKS_PHONE, phoneString);

        if (currentBooksUri == null) {
        Uri newUri = getContentResolver().insert(BooksContract.BooksEntry.CONTENT_URI, values);

        if (newUri == null) {
            Toast.makeText(this, getString(R.string.editor_insert_book_failed), Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, getString(R.string.editor_insert_book_successfull) , Toast.LENGTH_SHORT).show();
        }} else {
            int rowsAffected = getContentResolver().update(currentBooksUri, values, null, null);
            // Show a toast message depending on whether or not the update was successful.
            if (rowsAffected == 0) {
                // If no rows were affected, then there was an error with the update.
                Toast.makeText(this, getString(R.string.editor_update_books_failed),
                        Toast.LENGTH_SHORT).show();
            } else {
                // Otherwise, the update was successful and we can display a toast.
                Toast.makeText(this, getString(R.string.editor_update_books_successful),
                        Toast.LENGTH_SHORT).show();
            }
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

                saveBooks();
                finish();


                return true;
            case R.id.delete:
                showDeleteConfirmationDialog();
                return true;
            case android.R.id.home:
                if (!booksChanged) {
                    NavUtils.navigateUpFromSameTask(EditorActivity.this);
                    return true;
                }

                // Otherwise if there are unsaved changes, setup a dialog to warn the user.
                // Create a click listener to handle the user confirming that
                // changes should be discarded.
                DialogInterface.OnClickListener discardButtonClickListener =
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                // User clicked "Discard" button, navigate to parent activity.
                                NavUtils.navigateUpFromSameTask(EditorActivity.this);
                            }
                        };

                // Show a dialog that notifies the user they have unsaved changes
                showUnsavedChangesDialog(discardButtonClickListener);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
    @Override
    public void onBackPressed() {
        // If the pet hasn't changed, continue with handling back button press
        if (!booksChanged) {
            super.onBackPressed();
            return;
        }
        // Otherwise if there are unsaved changes, setup a dialog to warn the user.
        // Create a click listener to handle the user confirming that changes should be discarded.
        DialogInterface.OnClickListener discardButtonClickListener =
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        // User clicked "Discard" button, close the current activity.
                        finish();
                    }
                };
        // Show dialog that there are unsaved changes
        showUnsavedChangesDialog(discardButtonClickListener);
    }



    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        String[] projection = {
                BooksContract.BooksEntry._ID,
                BooksContract.BooksEntry.COLUMN_BOOKS_PRODUCT,
                BooksContract.BooksEntry.COLUMN_BOOKS_PRICE,
                BooksContract.BooksEntry.COLUMN_BOOKS_QUANTITY,
                BooksContract.BooksEntry.COLUMN_BOOKS_SUPPLIER,
                BooksContract.BooksEntry.COLUMN_BOOKS_PHONE };

        // This loader will execute the ContentProvider's query method on a background thread
        return new CursorLoader(this,   // Parent activity context
                currentBooksUri,         // Query the content URI for the current pet
                projection,             // Columns to include in the resulting Cursor
                null,                   // No selection clause
                null,                   // No selection arguments
                null);                  // Default sort order
    }


    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        if (cursor == null || cursor.getCount() < 1) {
            return;
        }
        // Proceed with moving to the first row of the cursor and reading data from it
        // (This should be the only row in the cursor)
        if (cursor.moveToFirst()) {
            int productColumnIndex = cursor.getColumnIndex(BooksContract.BooksEntry.COLUMN_BOOKS_PRODUCT);
            int priceColumnIndex = cursor.getColumnIndex(BooksContract.BooksEntry.COLUMN_BOOKS_PRICE);
            int quantityColumnIndex = cursor.getColumnIndex(BooksContract.BooksEntry.COLUMN_BOOKS_QUANTITY);
            int supplierColumnIndex = cursor.getColumnIndex(BooksContract.BooksEntry.COLUMN_BOOKS_SUPPLIER);
            int phoneColumnIndex = cursor.getColumnIndex(BooksContract.BooksEntry.COLUMN_BOOKS_PHONE);

            String productString = cursor.getString(productColumnIndex);
            Double priceDouble = cursor.getDouble(priceColumnIndex);

            int quantityInt = cursor.getInt(quantityColumnIndex);
           String supplierString = cursor.getString(supplierColumnIndex);
            String phoneString = cursor.getString(phoneColumnIndex);

            productName.setText(productString);
            price.setText(Double.toString(priceDouble));
            quantity.setText(Integer.toString(quantityInt));
            supplier.setText(supplierString);
            supplierPhone.setText(phoneString);

        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }
    private void showUnsavedChangesDialog(
            DialogInterface.OnClickListener discardButtonClickListener) {
        // Create an AlertDialog.Builder and set the message, and click listeners
        // for the positive and negative buttons on the dialog.
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.unsaved_changes_dialog_msg);
        builder.setPositiveButton(R.string.discard, discardButtonClickListener);
        builder.setNegativeButton(R.string.keep_editing, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User clicked the "Keep editing" button, so dismiss the dialog
                // and continue editing the pet.
                if (dialog != null) {
                    dialog.dismiss();
                }
            }
        });

        // Create and show the AlertDialog
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        // If this is a new pet, hide the "Delete" menu item.
        if (currentBooksUri == null) {
            MenuItem menuItem = menu.findItem(R.id.delete);
            menuItem.setVisible(false);
        }
        return true;
    }

    private void showDeleteConfirmationDialog() {
        // Create an AlertDialog.Builder and set the message, and click listeners
        // for the postivie and negative buttons on the dialog.
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.delete_dialog_msg);
        builder.setPositiveButton(R.string.delete, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User clicked the "Delete" button, so delete the pet.
                deleteBooks();
            }
        });
        builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User clicked the "Cancel" button, so dismiss the dialog
                // and continue editing the pet.
                if (dialog != null) {
                    dialog.dismiss();
                }
            }
        });

        // Create and show the AlertDialog
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }
    private void deleteBooks() {
        if (currentBooksUri != null) {

            int rowsDeleted = getContentResolver().delete(currentBooksUri, null, null);
            if (rowsDeleted == 0) {
                // If no rows were deleted, then there was an error with the delete.
                Toast.makeText(this, getString(R.string.editor_delete_books_failed),
                        Toast.LENGTH_SHORT).show();
            } else {

                // Otherwise, the delete was successful and we can display a toast.
                Toast.makeText(this, getString(R.string.editor_delete_books_successful),
                        Toast.LENGTH_SHORT).show();
            }}
        finish();
    }
}


