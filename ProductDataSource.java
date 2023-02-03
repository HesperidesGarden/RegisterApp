package com.example.testdatabaselogtag;


import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.content.ContentValues;
import android.database.Cursor;
import java.util.ArrayList;
import java.util.List;

public class ProductDataSource {
    private static final String LOG_TAG = ProductDataSource.class.getSimpleName();
    private SQLiteDatabase database;
    private ProductDbHelper dbHelper;

    //Columns of the Table!
    private String[] columns = {
            ProductDbHelper.COLUMN_ID,
            ProductDbHelper.COLUMN_PRODUCT,
            ProductDbHelper.COLUMN_PRICE,
            ProductDbHelper.COLUMN_DESC,

    };
    public ProductDataSource(Context context) {
        Log.d(LOG_TAG, "Unsere DataSource erzeugt jetzt den dbHelper.");
        dbHelper = new ProductDbHelper(context);
    }
    public void open() {
        Log.d(LOG_TAG, "Eine Referenz auf die Datenbank wird jetzt angefragt.");
        database = dbHelper.getWritableDatabase();
        Log.d(LOG_TAG, "Datenbank-Referenz erhalten. Pfad zur Datenbank: " + database.getPath());
    }
    public void close() {
        dbHelper.close();
        Log.d(LOG_TAG, "Datenbank mit Hilfe des DbHelpers geschlossen.");
    }

    public Product createProduct(String prodname, int prodprice, String proddesc) {
        ContentValues values = new ContentValues();
        values.put(ProductDbHelper.COLUMN_PRODUCT, prodname);
        values.put(ProductDbHelper.COLUMN_PRICE, prodprice);
        values.put(ProductDbHelper.COLUMN_DESC, proddesc);
        long insertId = database.insert(ProductDbHelper.TABLE_PRODUCTS, null, values);
                Cursor cursor = database.query(ProductDbHelper.TABLE_PRODUCTS,
                        columns, ProductDbHelper.COLUMN_ID + "=" + insertId,
                        null, null, null, null);
        cursor.moveToFirst();
        Product product = cursorToProduct(cursor);
        cursor.close();
        return product;
    }
    //cursorToShoppingMemo() vor. Wir verwenden
    //diese Methode, um Datensätze aus der Datenbank in ShoppingMemos umzuwandeln.

    private Product cursorToProduct(Cursor cursor) {
        int idIndex = cursor.getColumnIndex(ProductDbHelper.COLUMN_ID);
        int idProduct = cursor.getColumnIndex(ProductDbHelper.COLUMN_PRODUCT);
        int idPrice = cursor.getColumnIndex(ProductDbHelper.COLUMN_PRICE);
        int idDesc = cursor.getColumnIndex(ProductDbHelper.COLUMN_DESC);
        String product = cursor.getString(idProduct);
        int price = cursor.getInt(idPrice);
        String desc= cursor.getString(idDesc);
        long id = cursor.getLong(idIndex);
        Product shoppingMemo = new Product(product, price, desc, id);
        return shoppingMemo;
    }
    public List<Product> getAllProducts() {
        List<Product> productList = new ArrayList<>();
        Cursor cursor = database.query(ProductDbHelper.TABLE_PRODUCTS,
                columns, null, null, null, null, null);
        cursor.moveToFirst();
        Product product;
        while(!cursor.isAfterLast()) {
            product = cursorToProduct(cursor);
            productList.add(product);
            Log.d(LOG_TAG, "ID: " + product.getId() + ", Inhalt: " + product.toString());
                    cursor.moveToNext();
        }
        cursor.close();
        return productList;
    }
}



