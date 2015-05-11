package com.moralesf.masquerade.android.join;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.flurry.android.FlurryAgent;
import com.moralesf.masquerade.FlurryHelper;
import com.moralesf.masquerade.R;

public class JoinActivity extends ActionBarActivity {

    private static final String TAG = "JoinACtivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        FlurryAgent.init(this, FlurryHelper.APIKEY);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.join_activity);

        if (savedInstanceState == null) {
            Bundle arguments = new Bundle();
            JoinFragment fragment = new JoinFragment();
            fragment.setArguments(arguments);

            getSupportFragmentManager().beginTransaction()
                    .add(R.id.join_container, fragment)
                    .commit();
        }

        Log.d(TAG, "In the onCreate() event");

    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        Log.d(TAG, "In the onSaveInstanceState() event");
        super.onSaveInstanceState(savedInstanceState);
        // Save UI state changes to the savedInstanceState.
        // This bundle will be passed to onCreate if the process is
        // killed and restarted.
        savedInstanceState.putString("test", "TEEEEEEST");
        // etc.
    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        // Restore UI state from the savedInstanceState.
        // This bundle has also been passed to onCreate.
        String test = savedInstanceState.getString("test");
        Log.d(TAG, "In the onRestoreInstanceState() event");
        Log.d(TAG, test);
    }

    public void onStart()
    {
        super.onStart();
        Log.d(TAG, "In the onStart() event");
    }
    public void onRestart()
    {
        super.onRestart();
        Log.d(TAG, "In the onRestart() event");
    }
    public void onResume()
    {
        super.onResume();
        Log.d(TAG, "In the onResume() event");
    }
    public void onPause()
    {
        super.onPause();
        Log.d(TAG, "In the onPause() event");
    }
    public void onStop()
    {
        super.onStop();
        Log.d(TAG, "In the onStop() event");
    }
    public void onDestroy()
    {
        super.onDestroy();
        Log.d(TAG, "In the onDestroy() event");
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.join_menu, menu);
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
}
