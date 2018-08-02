package com.example.grasu.inventoryapp.data;

import android.provider.BaseColumns;

public final class BooksContract {

    private BooksContract() {
    }

    public static final class BooksEntry implements BaseColumns {

        public final static String TABLE_NAME = "books";
        public final static String _ID = BaseColumns._ID;
        public final static String COLUMN_BOOKS_PRODUCT = "product";
        public final static String COLUMN_BOOKS_PRICE = "price";
        public final static String COLUMN_BOOKS_QUANTITY = "quantity";
        public final static String COLUMN_BOOKS_SUPPLIER = "supplier";
        public final static String COLUMN_BOOKS_PHONE = "phone";
    }
}

