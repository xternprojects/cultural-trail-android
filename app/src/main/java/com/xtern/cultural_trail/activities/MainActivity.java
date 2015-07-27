package com.xtern.cultural_trail.activities;

import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.xtern.cultural_trail.R;
import com.xtern.cultural_trail.fragments.CreateIssueFragment;
import com.xtern.cultural_trail.fragments.IssuesListFragment;


public class MainActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Issues");
        toolbar.setNavigationIcon(R.drawable.menu);
        toolbar.inflateMenu(R.menu.menu_main);
        performFragmentTransaction(new IssuesListFragment());
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    public void performFragmentTransaction(Fragment fragment){
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container,fragment)
                .addToBackStack(null)
                .commit();
    }
}
