package com.moralesf.masquerade.android.mask;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.flurry.android.FlurryAgent;
import com.moralesf.masquerade.ApiHelper;
import com.moralesf.masquerade.FlurryHelper;
import com.moralesf.masquerade.R;
import com.moralesf.masquerade.android.data.MasqueradeContract;
import com.moralesf.masquerade.java.Api.Mask.MaskLeaveRequest;
import com.moralesf.masquerade.java.Api.Mask.MaskLeaveResponse;

import rx.functions.Action1;


public class MaskActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        FlurryAgent.init(this, FlurryHelper.APIKEY);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mask_activity);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_new_chat, menu);
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
        if (id == R.id.action_chat_rename) {

        }

        return super.onOptionsItemSelected(item);
    }

    public static void deleteMask(final Context context, final long id, int mask_api_id) {
        ContentResolver contentResolver = context.getContentResolver();
        ContentValues values = new ContentValues();
        values.put(MasqueradeContract.MaskEntry.COLUMN_DELETED, 1);
        values.put(MasqueradeContract.MaskEntry.COLUMN_SYNC, 0);

        String where = "_id = " + String.valueOf(id);
        contentResolver.update(MasqueradeContract.MaskEntry.CONTENT_URI, values, where, null);

        ApiHelper apiHelper = new ApiHelper(context);
        String token = apiHelper.getToken();

        apiHelper.getApi().maskLeave(token, new MaskLeaveRequest(mask_api_id))
                .subscribe(new Action1<MaskLeaveResponse>() {
                    @Override
                    public void call(MaskLeaveResponse response) {
                        ContentResolver contentResolver = context.getContentResolver();
                        ContentValues values = new ContentValues();
                        values.put(MasqueradeContract.MaskEntry.COLUMN_SYNC, 1);

                        String where = "_id = " + String.valueOf(id);
                        contentResolver.update(MasqueradeContract.MaskEntry.CONTENT_URI, values, where, null);
                    }
                });
    }

    public static void editMaskTitle(final Context context, final long id, String title) {
        ContentValues values = new ContentValues();
        values.put(MasqueradeContract.MaskEntry.COLUMN_TITLE, title);

        ContentResolver contentResolver = context.getContentResolver();
        String where = "_id = " + String.valueOf(id);
        contentResolver.update(MasqueradeContract.MaskEntry.CONTENT_URI, values, where, null);
    }
}

