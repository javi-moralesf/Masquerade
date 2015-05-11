package com.moralesf.masquerade.android.tutorial;

import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.moralesf.masquerade.R;
import com.viewpagerindicator.CirclePageIndicator;

public class TutorialActivity extends ActionBarActivity {

    private CustomPagerAdapter mCustomPagerAdapter;
    private ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tutorial_activity);

        mCustomPagerAdapter = new CustomPagerAdapter(this);

        mViewPager = (ViewPager) findViewById(R.id.pager);
        mViewPager.setAdapter(mCustomPagerAdapter);

        CirclePageIndicator indicator = (CirclePageIndicator)findViewById(R.id.titles);
        indicator.setFillColor(getResources().getColor(R.color.primary));
        indicator.setStrokeColor(getResources().getColor(R.color.primary_dark));
        indicator.setRadius(20f);
        indicator.setViewPager(mViewPager);




    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.tutorial_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_tutorial_done) {
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
