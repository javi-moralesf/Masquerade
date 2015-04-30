package com.moralesf.masquerade.android.Chat;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.moralesf.masquerade.R;
import com.moralesf.masquerade.android.data.MasqueradeContract;


public class ChatActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chat_activity);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_chat, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public static void startActivityByApiMaskId(Context context, int api_mask_id){
        ContentResolver contentResolver = context.getContentResolver();
        Cursor c = contentResolver.query(Uri.parse(MasqueradeContract.MaskEntry.CONTENT_URI + "/" + api_mask_id),
                null, null, null, null);
        c.moveToFirst();

        long _id = c.getLong(MasqueradeContract.MaskEntry.INDEX_COLUMN_ID);
        int api_id = c.getInt(MasqueradeContract.MaskEntry.INDEX_COLUMN_API_ID);
        String key = c.getString(MasqueradeContract.MaskEntry.INDEX_COLUMN_KEYGEN);
        String title = c.getString(MasqueradeContract.MaskEntry.INDEX_COLUMN_TITLE);
        startActivity(context, title, key, _id, api_id);
    }

    public static Intent createIntent(Context context, String title, String key, long mask_id, int api_mask_id){
        Intent intent = new Intent(context, ChatActivity.class);
        intent.putExtra(Intent.EXTRA_TITLE, title);
        intent.putExtra(Intent.EXTRA_UID, key);
        intent.putExtra(MasqueradeContract.ChatEntry.COLUMN_MASK_ID, mask_id);
        intent.putExtra(MasqueradeContract.MaskEntry.COLUMN_API_ID, api_mask_id);
        return intent;
    }

    public static void startActivity(Context context, String title, String key, long mask_id, int api_mask_id){
        Intent intent = createIntent(context, title, key, mask_id, api_mask_id);
        context.startActivity(intent);
    }
}
