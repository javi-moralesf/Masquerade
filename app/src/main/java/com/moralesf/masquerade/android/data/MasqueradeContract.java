package com.moralesf.masquerade.android.data;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by javi_moraçlesf on 10/04/15.
 * Defines table and column names for the weather database.
 */
public class MasqueradeContract {

    public static final String CONTENT_AUTHORITY = "com.moralesf.masquerade";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    public static final String PATH_MASK = "mask";
    public static final String PATH_CHAT = "chat";

    /* Inner class that defines the table contents of the location table */
    public static final class ChatEntry implements BaseColumns {

        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_CHAT).build();

        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_CHAT;
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_CHAT;

        // Table name
        public static final String TABLE_NAME = "chat";

        // Creation date.
        public static final String COLUMN_DATE = "date";
        public static final String COLUMN_MASK_ID = "mask_id";
        public static final String COLUMN_TEXT = "text";
        public static final String COLUMN_MINE = "mine";
        public static final String COLUMN_USER_ID = "user_id";

        public static Uri buildChatUri(long id) {

            return ContentUris.withAppendedId(CONTENT_URI, id);
        }

        public static String getMaskFromUri(Uri uri) {
            return uri.getPathSegments().get(1);
        }

    }

    /* Inner class that defines the table contents of the mask table */
    public static final class MaskEntry implements BaseColumns {

        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_MASK).build();

        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_MASK;
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_MASK;

        // Table name
        public static final String TABLE_NAME = "mask";

        // Creation date.
        public static final String COLUMN_API_ID = "id";
        public static final String COLUMN_DATE = "date";
        public static final String COLUMN_KEYGEN = "keygen";
        public static final String COLUMN_TITLE = "title";

        public static final int INDEX_COLUMN_ID = 0;
        public static final int INDEX_COLUMN_API_ID = 1;
        public static final int INDEX_COLUMN_DATE =2;
        public static final int INDEX_COLUMN_KEYGEN =3;
        public static final int INDEX_COLUMN_TITLE = 4;

        public static String getApiIdFromUri(Uri uri) { return uri.getPathSegments().get(1); }
    }
}