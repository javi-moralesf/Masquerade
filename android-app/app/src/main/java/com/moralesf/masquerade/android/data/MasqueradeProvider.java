package com.moralesf.masquerade.android.data;

import android.annotation.TargetApi;
import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;

/**
 * Created by javi_moralesf on 10/04/15.
 **/
public class MasqueradeProvider extends ContentProvider {

    // The URI Matcher used by this content provider.
    private static final UriMatcher sUriMatcher = buildUriMatcher();
    private MasqueradeDbHelper mOpenHelper;

    static final int MASK = 100;
    static final int MASK_WITH_API_ID = 101;
    static final int MASK_WITH_KEY = 102;
    static final int MASK_WITH_ID = 103;
    static final int CHAT = 200;
    static final int CHAT_WITH_MASK = 201;
    static final int CHAT_WITH_ID = 202;

    private static final SQLiteQueryBuilder sChatQueryBuilder;
    private static final SQLiteQueryBuilder sMaskQueryBuilder;

    static{
        sChatQueryBuilder = new SQLiteQueryBuilder();
        sChatQueryBuilder.setTables(MasqueradeContract.ChatEntry.TABLE_NAME);
    }

    static{
        sMaskQueryBuilder = new SQLiteQueryBuilder();
        sMaskQueryBuilder.setTables(MasqueradeContract.MaskEntry.TABLE_NAME);
    }
    
    //chat.mask_id = ?
    private static final String sChatMaskSelection =
            MasqueradeContract.ChatEntry.TABLE_NAME+
                    "." + MasqueradeContract.ChatEntry.COLUMN_MASK_ID + " = ? ";
    //chat._id = ?
    private static final String sChatIdSelection =
            MasqueradeContract.ChatEntry.TABLE_NAME+
                    "._id = ? ";
    //mask.id = ?
    private static final String sMaskApiIdSelection =
            MasqueradeContract.MaskEntry.TABLE_NAME+
                    "." + MasqueradeContract.MaskEntry.COLUMN_API_ID + " = ? ";
    //mask._id = ?
    private static final String sMaskIdSelection =
            MasqueradeContract.MaskEntry.TABLE_NAME+
                    "._id = ? ";
    //mask.keygen = ?
    private static final String sMaskKeySelection =
            MasqueradeContract.MaskEntry.TABLE_NAME+
                    "." + MasqueradeContract.MaskEntry.COLUMN_KEYGEN + " = ? ";
    //mask.deleted = 0
    private static final String sMaskNotDeletedSelection =
            MasqueradeContract.MaskEntry.TABLE_NAME+
                    "." + MasqueradeContract.MaskEntry.COLUMN_DELETED + " = 0 ";

    private Cursor getChatByMask(Uri uri, String[] projection, String sortOrder) {
        String mask_id = MasqueradeContract.ChatEntry.getMaskFromUri(uri);

        String[] selectionArgs;
        String selection;

        selection = sChatMaskSelection;
        selectionArgs = new String[]{mask_id};


        return sChatQueryBuilder.query(mOpenHelper.getReadableDatabase(),
                projection,
                selection,
                selectionArgs,
                null,
                null,
                sortOrder
        );
    }

    private Cursor getChatById(Uri uri, String[] projection, String sortOrder) {
        String id = MasqueradeContract.ChatEntry.getIdFromUri(uri);

        String[] selectionArgs;
        String selection;

        selection = sChatIdSelection;
        selectionArgs = new String[]{id};


        return sChatQueryBuilder.query(mOpenHelper.getReadableDatabase(),
                projection,
                selection,
                selectionArgs,
                null,
                null,
                sortOrder
        );
    }
    
    private Cursor getMaskByApiId(Uri uri, String[] projection, String sortOrder) {
        String mask_id = MasqueradeContract.MaskEntry.getApiIdFromUri(uri);

        String[] selectionArgs;
        String selection;

        selection = sMaskApiIdSelection;
        selectionArgs = new String[]{mask_id};


        return sMaskQueryBuilder.query(mOpenHelper.getReadableDatabase(),
                projection,
                selection,
                selectionArgs,
                null,
                null,
                sortOrder
        );
    }
    private Cursor getMaskById(Uri uri, String[] projection, String sortOrder) {
        String mask_id = MasqueradeContract.MaskEntry.getIdFromUri(uri);

        String[] selectionArgs;
        String selection;

        selection = sMaskIdSelection;
        selectionArgs = new String[]{mask_id};


        return sMaskQueryBuilder.query(mOpenHelper.getReadableDatabase(),
                projection,
                selection,
                selectionArgs,
                null,
                null,
                sortOrder
        );
    }
    private Cursor getMaskByKey(Uri uri, String[] projection, String sortOrder) {
        String keygen = MasqueradeContract.MaskEntry.getKeygenFromUri(uri);

        String[] selectionArgs;
        String selection;

        selection = sMaskKeySelection;
        selectionArgs = new String[]{keygen};


        return sMaskQueryBuilder.query(mOpenHelper.getReadableDatabase(),
                projection,
                selection,
                selectionArgs,
                null,
                null,
                sortOrder
        );
    }

    private Cursor getMasks(Uri uri, String[] projection, String sortOrder) {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();


        /*final String SQL_STATEMENT = "SELECT DISTINCT"+MasqueradeContract.MaskEntry.TABLE_NAME+".*, COUNT COUNT(*) FROM "+ MasqueradeContract.MaskEntry.TABLE_NAME+
                " WHERE uname=? AND pwd=?";*/

        final String SQL_STATEMENT ="SELECT "+MasqueradeContract.MaskEntry.TABLE_NAME+".*, (COUNT("+MasqueradeContract.ChatEntry.TABLE_NAME+"."+ MasqueradeContract.ChatEntry.COLUMN_READED+") - SUM("+MasqueradeContract.ChatEntry.TABLE_NAME+"."+ MasqueradeContract.ChatEntry.COLUMN_READED+")) AS unread_messages FROM ("+MasqueradeContract.MaskEntry.TABLE_NAME+
                " INNER JOIN "+MasqueradeContract.ChatEntry.TABLE_NAME+
                " ON "+MasqueradeContract.MaskEntry.TABLE_NAME+"._id="+MasqueradeContract.ChatEntry.TABLE_NAME+"."+MasqueradeContract.ChatEntry.COLUMN_MASK_ID+")"+
                " WHERE "+MasqueradeContract.MaskEntry.TABLE_NAME+"."+ MasqueradeContract.MaskEntry.COLUMN_DELETED+"=0"+
                " GROUP BY "+MasqueradeContract.MaskEntry.TABLE_NAME+".id";


        return db.rawQuery(SQL_STATEMENT, null);

    }



    static UriMatcher buildUriMatcher() {
        final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
        final String authority = MasqueradeContract.CONTENT_AUTHORITY;

        // For each type of URI you want to add, create a corresponding code.
        matcher.addURI(authority, MasqueradeContract.PATH_MASK, MASK);
        matcher.addURI(authority, MasqueradeContract.PATH_MASK+ "/key/*", MASK_WITH_KEY);
        matcher.addURI(authority, MasqueradeContract.PATH_MASK+ "/id/*", MASK_WITH_ID);
        matcher.addURI(authority, MasqueradeContract.PATH_MASK+ "/*", MASK_WITH_API_ID);
        matcher.addURI(authority, MasqueradeContract.PATH_CHAT, CHAT);
        matcher.addURI(authority, MasqueradeContract.PATH_CHAT + "/id/*", CHAT_WITH_ID);
        matcher.addURI(authority, MasqueradeContract.PATH_CHAT + "/*", CHAT_WITH_MASK);
        return matcher;
    }

    @Override
    public boolean onCreate() {
        mOpenHelper = new MasqueradeDbHelper(getContext());
        return true;
    }

    @Override
    public String getType(Uri uri) {

        // Use the Uri Matcher to determine what kind of URI this is.
        final int match = sUriMatcher.match(uri);

        switch (match) {
            case CHAT_WITH_MASK:
                return MasqueradeContract.ChatEntry.CONTENT_ITEM_TYPE;
            case MASK:
                return MasqueradeContract.ChatEntry.CONTENT_TYPE;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs,
                        String sortOrder) {

        Cursor retCursor;
        switch (sUriMatcher.match(uri)) {
            // "chat/*"
            case CHAT_WITH_MASK: {
                retCursor = getChatByMask(uri, projection, sortOrder);
                break;
            }
            // "chat/id/*"
            case CHAT_WITH_ID: {
                retCursor = getChatById(uri, projection, sortOrder);
                break;
            }
            // "mask/*"
            case MASK_WITH_API_ID: {
                retCursor = getMaskByApiId(uri, projection, sortOrder);
                break;
            }
            // "mask/*"
            case MASK_WITH_ID: {
                retCursor = getMaskById(uri, projection, sortOrder);
                break;
            }
            // "mask/key/*"
            case MASK_WITH_KEY: {
                retCursor = getMaskByKey(uri, projection, sortOrder);
                break;
            }
            // "mask"
            case MASK: {
                retCursor = getMasks(uri, projection, sortOrder);
                break;
            }
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        retCursor.setNotificationUri(getContext().getContentResolver(), uri);
        return retCursor;
    }




    /*
        Student: Add the ability to insert Locations to the implementation of this function.
     */
    @Override
    public Uri insert(Uri uri, ContentValues values) {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        Uri returnUri;

        switch (match) {
            case MASK: {
                long _id = db.insert(MasqueradeContract.MaskEntry.TABLE_NAME, null, values);
                if ( _id > 0 )
                    returnUri = Uri.parse(MasqueradeContract.MaskEntry.CONTENT_URI+"/id/"+String.valueOf(_id));
                else
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                break;
            }
            case CHAT: {
                long _id = db.insert(MasqueradeContract.ChatEntry.TABLE_NAME, null, values);
                if ( _id > 0 )
                    returnUri = Uri.parse(MasqueradeContract.ChatEntry.CONTENT_URI+"/id/"+String.valueOf(_id));
                else
                    throw new android.database.SQLException("Failed to insert row into " + uri);

                getContext().getContentResolver().notifyChange(MasqueradeContract.MaskEntry.CONTENT_URI, null);
                break;
            }
            case CHAT_WITH_MASK: {
                long _id = db.insert(MasqueradeContract.ChatEntry.TABLE_NAME, null, values);
                if ( _id > 0 )
                    returnUri = Uri.parse(MasqueradeContract.ChatEntry.CONTENT_URI+"/id/"+String.valueOf(_id));
                else
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                break;
            }
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return returnUri;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        int rowsDeleted;
        // this makes delete all rows return the number of rows deleted
        if ( null == selection ) selection = "1";
        switch (match) {
            case MASK:
                rowsDeleted = db.delete(
                        MasqueradeContract.MaskEntry.TABLE_NAME, selection, selectionArgs);
                break;

            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        // Because a null deletes all rows
        if (rowsDeleted != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return rowsDeleted;
    }

    @Override
    public int update(
            Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        int rowsUpdated;

        switch (match) {
            case MASK:
                rowsUpdated = db.update(MasqueradeContract.MaskEntry.TABLE_NAME, values, selection,
                        selectionArgs);
                break;
            case CHAT:
                rowsUpdated = db.update(MasqueradeContract.ChatEntry.TABLE_NAME, values, selection,
                        selectionArgs);
                if (rowsUpdated != 0) {
                    getContext().getContentResolver().notifyChange(MasqueradeContract.MaskEntry.CONTENT_URI, null);
                }
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        if (rowsUpdated != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return rowsUpdated;
    }

    @Override
    public int bulkInsert(Uri uri, ContentValues[] values) {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case MASK:
                db.beginTransaction();
                int returnCount = 0;
                try {
                    for (ContentValues value : values) {
                        long _id = db.insert(MasqueradeContract.MaskEntry.TABLE_NAME, null, value);
                        if (_id != -1) {
                            returnCount++;
                        }
                    }
                    db.setTransactionSuccessful();
                } finally {
                    db.endTransaction();
                }
                getContext().getContentResolver().notifyChange(uri, null);
                return returnCount;
            default:
                return super.bulkInsert(uri, values);
        }
    }

    // You do not need to call this method. This is a method specifically to assist the testing
    // framework in running smoothly. You can read more at:
    // http://developer.android.com/reference/android/content/ContentProvider.html#shutdown()
    @Override
    @TargetApi(11)
    public void shutdown() {
        mOpenHelper.close();
        super.shutdown();
    }
}