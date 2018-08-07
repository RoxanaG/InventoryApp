package com.example.grasu.inventoryapp.data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.util.Log;

public class BooksProvider extends ContentProvider {

    public static final String LOG_TAG = BooksProvider.class.getSimpleName();
    private BooksDbHelper dbHelper;
    private static final int BOOKS = 100;
    private static final int BOOK_ID = 101;
    private static final UriMatcher sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    static {

        sUriMatcher.addURI(BooksContract.CONTENT_AUTHORITY, BooksContract.PATH_BOOKS, BOOKS);
        sUriMatcher.addURI(BooksContract.CONTENT_AUTHORITY, BooksContract.PATH_BOOKS + "/#", BOOK_ID);
    }

    @Override
    public boolean onCreate() {
        dbHelper = new BooksDbHelper(getContext());
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs,
                        String sortOrder) {
        SQLiteDatabase database = dbHelper.getReadableDatabase();
        Cursor cursor;
        int match = sUriMatcher.match(uri);

        switch (match) {
            case BOOKS:
                cursor = database.query(BooksContract.BooksEntry.TABLE_NAME, projection, selection, selectionArgs,
                        null, null, sortOrder);
                break;
            case BOOK_ID:
                selection = BooksContract.BooksEntry._ID + "=?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};

                cursor = database.query(BooksContract.BooksEntry.TABLE_NAME, projection, selection, selectionArgs,
                        null, null, sortOrder);
                break;
            default:
                throw new IllegalArgumentException("Cannot query unknown URI " + uri);
        }
        cursor.setNotificationUri(getContext().getContentResolver(), uri);
        return cursor;
    }

    @Override
    public Uri insert(Uri uri, ContentValues contentValues) {
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case BOOKS:
                return insertBooks(uri, contentValues);
            default:
                throw new IllegalArgumentException("Insertion is not supported for " + uri);
        }
    }

    private Uri insertBooks(Uri uri, ContentValues values) {

        String product = values.getAsString(BooksContract.BooksEntry.COLUMN_BOOKS_PRODUCT);
        if (product == null) {
            throw new IllegalArgumentException("The product is necessary");
        }
        Double price = values.getAsDouble(BooksContract.BooksEntry.COLUMN_BOOKS_PRICE);
        if (price == null) {
            throw new IllegalArgumentException("Book requires a price");
        }
        Integer quantity = values.getAsInteger(BooksContract.BooksEntry.COLUMN_BOOKS_QUANTITY);
        if (quantity != null && quantity < 0) {
            throw new IllegalArgumentException("Books requires valid quantity");
        }
        SQLiteDatabase database = dbHelper.getWritableDatabase();

        long id = database.insert(BooksContract.BooksEntry.TABLE_NAME, null, values);

        if (id == -1) {
            Log.e(LOG_TAG, "Failed to insert row for " + uri);
            return null;
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return ContentUris.withAppendedId(uri, id);
    }

    @Override
    public int update(Uri uri, ContentValues contentValues, String selection,
                      String[] selectionArgs) {
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case BOOKS:
                return updateBooks(uri, contentValues, selection, selectionArgs);
            case BOOK_ID:
                selection = BooksContract.BooksEntry._ID + "=?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};
                return updateBooks(uri, contentValues, selection, selectionArgs);
            default:
                throw new IllegalArgumentException("Update is not supported for " + uri);
        }
    }

    private int updateBooks(Uri uri, ContentValues values, String selection, String[] selectionArgs) {

        if (values.containsKey(BooksContract.BooksEntry.COLUMN_BOOKS_PRODUCT)) {
            String name = values.getAsString(BooksContract.BooksEntry.COLUMN_BOOKS_PRODUCT);
            if (name == null) {
                throw new IllegalArgumentException("Book requires a name");

            }
        }
        if (values.containsKey(BooksContract.BooksEntry.COLUMN_BOOKS_PRICE)) {
            Double price = values.getAsDouble(BooksContract.BooksEntry.COLUMN_BOOKS_PRICE);
            if (price == null) {
                throw new IllegalArgumentException("Book requires price");
            }
        }
        if (values.containsKey(BooksContract.BooksEntry.COLUMN_BOOKS_QUANTITY)) {
            Integer quantity = values.getAsInteger(BooksContract.BooksEntry.COLUMN_BOOKS_QUANTITY);
            if (quantity != null && quantity < 0) {
                throw new IllegalArgumentException("Book requires valid quantity");
            }
        }
        if (values.size() == 0) {
            return 0;
        }
        SQLiteDatabase database = dbHelper.getWritableDatabase();
        int rowsUpdated = database.update(BooksContract.BooksEntry.TABLE_NAME, values, selection, selectionArgs);
        if (rowsUpdated != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return rowsUpdated;

    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        SQLiteDatabase database = dbHelper.getWritableDatabase();
        int rowsDeleted;
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case BOOKS:
                rowsDeleted = database.delete(BooksContract.BooksEntry.TABLE_NAME, selection, selectionArgs);
                break;
            case BOOK_ID:
                selection = BooksContract.BooksEntry._ID + "=?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};
                rowsDeleted = database.delete(BooksContract.BooksEntry.TABLE_NAME, selection, selectionArgs);
                break;
            default:
                throw new IllegalArgumentException("Deletion is not supported for " + uri);
        }
        if (rowsDeleted != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return rowsDeleted;
    }

    @Override
    public String getType(Uri uri) {
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case BOOKS:
                return BooksContract.BooksEntry.CONTENT_LIST_TYPE;
            case BOOK_ID:
                return BooksContract.BooksEntry.CONTENT_ITEM_TYPE;
            default:
                throw new IllegalStateException("Unknown URI " + uri + " with match " + match);
        }
    }
}

