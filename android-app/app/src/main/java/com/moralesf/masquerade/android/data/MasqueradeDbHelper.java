package com.moralesf.masquerade.android.data;

import android.content.Context;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.moralesf.masquerade.android.data.MasqueradeContract.MaskEntry;
import com.moralesf.masquerade.android.data.MasqueradeContract.ChatEntry;

import java.util.ArrayList;

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
                MaskEntry.COLUMN_API_ID + " INTEGER DEFAULT NULL," +
                MaskEntry.COLUMN_DATE + " TIMESTAMP DEFAULT CURRENT_TIMESTAMP, " +
                MaskEntry.COLUMN_KEYGEN + " TEXT NOT NULL, " +
                MaskEntry.COLUMN_TITLE + " TEXT NOT NULL, " +
                MaskEntry.COLUMN_SYNC + " INTEGER DEFAULT 0," +
                MaskEntry.COLUMN_DELETED + " INTEGER DEFAULT 0" +
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
                ChatEntry.COLUMN_DATE + " TIMESTAMP DEFAULT CURRENT_TIMESTAMP, " +
                ChatEntry.COLUMN_TEXT + " TEXT NOT NULL, " +
                ChatEntry.COLUMN_TYPE + " INTEGER NOT NULL," +
                ChatEntry.COLUMN_USER_ID + " INTEGER NOT NULL," +
                ChatEntry.COLUMN_READED + " INTEGER DEFAULT 0," +
                ChatEntry.COLUMN_SYNC + " INTEGER DEFAULT 0," +

                // Set up the mask_id column as a foreign key to mask table.
                " FOREIGN KEY (" + ChatEntry.COLUMN_MASK_ID + ") REFERENCES " +
                MaskEntry.TABLE_NAME + " (" + MaskEntry._ID + ")); ";

        sqLiteDatabase.execSQL(SQL_CREATE_MASK_TABLE);
        sqLiteDatabase.execSQL(SQL_CREATE_CHAT_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        //sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + ChatEntry.TABLE_NAME);
        //sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + MaskEntry.TABLE_NAME);
        //onCreate(sqLiteDatabase);
    }

    public ArrayList<Cursor> getData(String Query){
        //get writable database
        SQLiteDatabase sqlDB = this.getWritableDatabase();
        String[] columns = new String[] { "mesage" };
        //an array list of cursor to save two cursors one has results from the query
        //other cursor stores error message if any errors are triggered
        ArrayList<Cursor> alc = new ArrayList<Cursor>(2);
        MatrixCursor Cursor2= new MatrixCursor(columns);
        alc.add(null);
        alc.add(null);


        try{
            String maxQuery = Query ;
            //execute the query results will be save in Cursor c
            Cursor c = sqlDB.rawQuery(maxQuery, null);


            //add value to cursor2
            Cursor2.addRow(new Object[] { "Success" });

            alc.set(1,Cursor2);
            if (null != c && c.getCount() > 0) {


                alc.set(0,c);
                c.moveToFirst();

                return alc ;
            }
            return alc;
        } catch(SQLException sqlEx){
            Log.d("printing exception", sqlEx.getMessage());
            //if any exceptions are triggered save the error message to cursor an return the arraylist
            Cursor2.addRow(new Object[] { ""+sqlEx.getMessage() });
            alc.set(1,Cursor2);
            return alc;
        } catch(Exception ex){

            Log.d("printing exception", ex.getMessage());

            //if any exceptions are triggered save the error message to cursor an return the arraylist
            Cursor2.addRow(new Object[] { ""+ex.getMessage() });
            alc.set(1,Cursor2);
            return alc;
        }


    }

    @Override
    public void onOpen(SQLiteDatabase db) {
        super.onOpen(db);
        if (!db.isReadOnly()) {
            // Enable foreign key constraints
            db.execSQL("PRAGMA foreign_keys=ON;");
        }
    }
}