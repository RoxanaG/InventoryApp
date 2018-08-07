package com.example.grasu.inventoryapp.adapters;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.grasu.inventoryapp.R;
import com.example.grasu.inventoryapp.data.BooksContract;

public class BooksCursorAdapter extends CursorAdapter {

    public BooksCursorAdapter(Context context, Cursor c) {
        super(context, c, 0);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.books_list_item, parent, false);
    }

    @Override
    public void bindView(View view, final Context context, Cursor cursor) {
        TextView productText = view.findViewById(R.id.productText);
        TextView priceText = view.findViewById(R.id.priceText);
        TextView quantityText = view.findViewById(R.id.quantityText);
        ImageView shopButton = view.findViewById(R.id.shop_button);

        int productColumnIndex = cursor.getColumnIndex(BooksContract.BooksEntry.COLUMN_BOOKS_PRODUCT);
        int priceColumnIndex = cursor.getColumnIndex(BooksContract.BooksEntry.COLUMN_BOOKS_PRICE);
        final int quantityColumnIndex = cursor.getColumnIndex(BooksContract.BooksEntry.COLUMN_BOOKS_QUANTITY);
        int idColumnIndex = cursor.getColumnIndex(BooksContract.BooksEntry._ID);
        final int booksId = cursor.getInt(idColumnIndex);
        String bookProduct = cursor.getString(productColumnIndex);
        String bookPrice = cursor.getString(priceColumnIndex);
        final int bookQuantity = cursor.getInt(quantityColumnIndex);

        productText.setText(bookProduct);
        priceText.setText(bookPrice);
        quantityText.setText(Integer.toString(bookQuantity));

        shopButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri currentUri = ContentUris.withAppendedId(BooksContract.BooksEntry.CONTENT_URI, booksId);
                buy(context, bookQuantity, currentUri);
            }
        });
    }

    private void buy(Context context, int bookQuantity, Uri uri) {
        if (bookQuantity == 0) {
            Toast.makeText(context, R.string.no_books, Toast.LENGTH_SHORT).show();
        } else {
            int newQuantity = bookQuantity - 1;
            ContentValues values = new ContentValues();
            values.put(BooksContract.BooksEntry.COLUMN_BOOKS_QUANTITY, newQuantity);
            context.getContentResolver().update(uri, values, null, null);
        }
    }
}

