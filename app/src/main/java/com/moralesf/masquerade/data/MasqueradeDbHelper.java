package com.moralesf.masquerade.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.moralesf.masquerade.data.MasqueradeContract.MaskEntry;
import com.moralesf.masquerade.data.MasqueradeContract.ChatEntry;

/**
 * Created by javi_moralesf on 10/04/15.
 * Manages a local database for weather data.
 */
public class MasqueradeDbHelper extends SQLiteOpenHelper {

    // If you change the database schema, you must increment the database version.
    private static final int DATABASE_VERSION = 1;

    static final String DATABASE_NAME = "masquerade.db";

    public MasqueradeDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        // Create a table to hold locations.  A location consists of the string supplied in the
        // location setting, the city name, and the latitude and longitude
        final String SQL_CREATE_MASK_TABLE = "CREATE TABLE " + MaskEntry.TABLE_NAME + " (" +
                MaskEntry._ID + " INTEGER PRIMARY KEY," +
                MaskEntry.COLUMN_DATE + " INTEGER NOT NULL, " +
                MaskEntry.COLUMN_KEYGEN + " TEXT NOT NULL, " +
                MaskEntry.COLUMN_USER + " TEXT NOT NULL, " +
                MaskEntry.COLUMN_TITLE + " TEXT NOT NULL, " +
                " );";

        final String SQL_CREATE_CHAT_TABLE = "CREATE TABLE " + ChatEntry.TABLE_NAME + " (" +
                // Why AutoIncrement here, and not above?
                // Unique keys will be auto-generated in either case.  But for weather
                // forecasting, it's reasonable to assume the user will want information
                // for a certain date and all dates *following*, so the forecast data
                // should be sorted accordingly.
                ChatEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +

                // the ID of the location entry associated with this weather data
                ChatEntry.COLUMN_MASK_ID + " INTEGER NOT NULL, " +
                ChatEntry.COLUMN_DATE + " INTEGER NOT NULL, " +
                ChatEntry.COLUMN_TEXT + " TEXT NOT NULL, " +
                ChatEntry.COLUMN_MINE + " INTEGER NOT NULL," +

                // Set up the mask_id column as a foreign key to mask table.
                " FOREIGN KEY (" + ChatEntry.COLUMN_MASK_ID + ") REFERENCES " +
                MaskEntry.TABLE_NAME + " (" + MaskEntry._ID + "); ";

        sqLiteDatabase.execSQL(SQL_CREATE_MASK_TABLE);
        sqLiteDatabase.execSQL(SQL_CREATE_CHAT_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        //sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + ChatEntry.TABLE_NAME);
        //sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + MaskEntry.TABLE_NAME);
        //onCreate(sqLiteDatabase);
    }
}