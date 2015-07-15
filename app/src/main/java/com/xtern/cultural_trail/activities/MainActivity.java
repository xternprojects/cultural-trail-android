package com.xtern.cultural_trail.activities;

import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.parse.SignUpCallback;
import com.xtern.cultural_trail.R;
import com.xtern.cultural_trail.fragments.IssuesListFragment;
import com.xtern.cultural_trail.fragments.LoginFragment;


public class MainActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Enable Local Datastore.
        Parse.enableLocalDatastore(this);
        Parse.initialize(this, "kBcWHoqqo1z6eUGreebgI5gcb1rxBPgmdaFpW4lf", "WBY96ta7jVdnq0p2sd4PlMg2ANc91Z2SJVMGUDYs");
        setContentView(R.layout.activity_main);
        performFragmentTransaction(new LoginFragment());
    }


    public void performFragmentTransaction(Fragment fragment){
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .addToBackStack(null)
                .commit();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

}