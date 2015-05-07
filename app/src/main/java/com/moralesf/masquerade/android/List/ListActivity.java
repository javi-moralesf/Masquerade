package com.moralesf.masquerade.android.List;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.flurry.android.FlurryAgent;
import com.moralesf.masquerade.ApiHelper;
import com.moralesf.masquerade.FlurryHelper;
import com.moralesf.masquerade.GcmHelper;
import com.moralesf.masquerade.android.Join.JoinActivity;
import com.moralesf.masquerade.android.Mask.MaskActivity;
import com.moralesf.masquerade.R;
import com.moralesf.masquerade.android.data.AndroidDatabaseManager;
import com.moralesf.masquerade.android.data.MasqueradeApi;


public class ListActivity extends ActionBarActivity {

    static final String TAG = "ListActivity";

    GcmHelper gcmHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        FlurryAgent.init(this, FlurryHelper.APIKEY);

        super.onCreate(savedInstanceState);

        setContentView(R.layout.list_activity);

        ApiHelper api = new ApiHelper(this);
        gcmHelper = new GcmHelper(this, api);
    }

    @Override
    protected void onResume() {
        super.onResume();
        gcmHelper.checkPlayServices();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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
            //startActivity(new Intent(this, AndroidDatabaseManager.class));
            Toast.makeText(this, getString(R.string.author)
                    , Toast.LENGTH_LONG).show();
        }
        if (id == R.id.action_new_chat) {
            Intent i = new Intent(this, MaskActivity.class);
            i.setFlags(i.getFlags() | Intent.FLAG_ACTIVITY_NO_HISTORY);
            startActivity(i);
        }
        if (id == R.id.action_join) {
            Intent i = new Intent(this, JoinActivity.class);
            i.setFlags(i.getFlags() | Intent.FLAG_ACTIVITY_NO_HISTORY);
            startActivity(i);
        }

        return super.onOptionsItemSelected(item);
    }
}
