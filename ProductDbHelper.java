package com.example.testdatabaselogtag;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
public class ProductDbHelper extends SQLiteOpenHelper{
    private static final String LOG_TAG = ProductDbHelper.class.getSimpleName();
    public static final String DB_NAME = "test_cash_register.db";
    public static final int DB_VERSION = 1;
    public static final String TABLE_PRODUCTS = "products";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_PRODUCT = "name";
    public static final String COLUMN_PRICE = "price";
    public static final String COLUMN_DESC = "desc";
    public static final String SQL_CREATE =
            "CREATE TABLE " + TABLE_PRODUCTS +
                    "(" + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUMN_PRODUCT + " TEXT NOT NULL, " +
                    COLUMN_PRICE + " INTEGER NOT NULL,"+
                    COLUMN_DESC + " TEXT NOT NULL);";
    public ProductDbHelper(Context context) {
//super(context, "PLATZHALTER_DATENBANKNAME", null, 1);
        super(context, DB_NAME, null, DB_VERSION);
        Log.d(LOG_TAG, "DbHelper hat die Datenbank: " + getDatabaseName() + " erzeugt.");
    }
    // Die onCreate-Methode wird nur aufgerufen, falls die Datenbank noch nicht existiert
    @Override
    public void onCreate(SQLiteDatabase db) {
        try {
            Log.d(LOG_TAG, "Die Tabelle wird mit SQL-Befehl: " + SQL_CREATE + " angelegt.");
                    db.execSQL(SQL_CREATE);
        }
        catch (Exception ex) {
            Log.e(LOG_TAG, "Fehler beim Anlegen der Tabelle: " + ex.getMessage());
        }
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }
}