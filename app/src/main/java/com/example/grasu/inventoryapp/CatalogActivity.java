package com.example.grasu.inventoryapp;

import android.app.AlertDialog;
import android.app.LoaderManager;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.CursorLoader;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;

import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.grasu.inventoryapp.adapters.BooksCursorAdapter;
import com.example.grasu.inventoryapp.data.BooksContract;


public class CatalogActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor>{
private static final int BOOKS_LOADER = 0;
BooksCursorAdapter cursorAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_catalog);

        FloatingActionButton fab = findViewById(R.id.add_book);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(CatalogActivity.this, EditorActivity.class);
                startActivity(intent);
            }
        });
        ListView booksListView = (ListView) findViewById(R.id.list);

        // Find and set empty view on the ListView, so that it only shows when the list has 0 items.
        View emptyView = findViewById(R.id.empty_view);
        booksListView.setEmptyView(emptyView);

        cursorAdapter = new BooksCursorAdapter(this,null);
        booksListView.setAdapter(cursorAdapter);
        booksListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
Intent intent = new Intent(CatalogActivity.this,EditorActivity.class);
Uri currentBooksUri = ContentUris.withAppendedId(BooksContract.BooksEntry.CONTENT_URI,id);
intent.setData(currentBooksUri);
startActivity(intent);
            }
        });
        getLoaderManager().initLoader(BOOKS_LOADER,null,this);
    }

    private void insertBooks() {

        ContentValues values = new ContentValues();
        values.put(BooksContract.BooksEntry.COLUMN_BOOKS_PRODUCT, "Walks with men");
        values.put(BooksContract.BooksEntry.COLUMN_BOOKS_PRICE, 10.00);
        values.put(BooksContract.BooksEntry.COLUMN_BOOKS_QUANTITY, 2);
        values.put(BooksContract.BooksEntry.COLUMN_BOOKS_SUPPLIER, "Amazon");
        values.put(BooksContract.BooksEntry.COLUMN_BOOKS_PHONE, 727213658);

        Uri newUri = getContentResolver().insert(BooksContract.BooksEntry.CONTENT_URI, values);
    }
    private void deleteAllBooks() {
        int rowsDeleted = getContentResolver().delete(BooksContract.BooksEntry.CONTENT_URI, null, null);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_catalog, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.insert_dummy_data:
                insertBooks();
                return true;
            case R.id.delete_all_entries:
                showDeleteConfirmationDialog();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        String[] projection = {
                BooksContract.BooksEntry._ID,
                BooksContract.BooksEntry.COLUMN_BOOKS_PRODUCT,
                BooksContract.BooksEntry.COLUMN_BOOKS_PRICE
        };
        return new CursorLoader(this,
                BooksContract.BooksEntry.CONTENT_URI,
                projection,
                null,
                null,
                null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
cursorAdapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
cursorAdapter.swapCursor(null);
    }
    private void showDeleteConfirmationDialog() {
        // Create an AlertDialog.Builder and set the message, and click listeners
        // for the postivie and negative buttons on the dialog.
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.delete_all_dialog_msg);
        builder.setPositiveButton(R.string.delete, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
              
                deleteAllBooks();
            }
        });
        builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {

                if (dialog != null) {
                    dialog.dismiss();
                }
            }
        });

        // Create and show the AlertDialog
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

}


