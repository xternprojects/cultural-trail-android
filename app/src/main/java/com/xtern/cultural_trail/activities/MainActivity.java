package com.xtern.cultural_trail.activities;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.google.gson.Gson;
import com.parse.Parse;
import com.parse.ParseUser;
import com.xtern.cultural_trail.R;
import com.xtern.cultural_trail.fragments.IssuesListFragment;
import com.xtern.cultural_trail.fragments.SplashFragment;


public class MainActivity extends AppCompatActivity {

    ParseUser currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Parse.enableLocalDatastore(this);
        Parse.initialize(this, "kBcWHoqqo1z6eUGreebgI5gcb1rxBPgmdaFpW4lf", "WBY96ta7jVdnq0p2sd4PlMg2ANc91Z2SJVMGUDYs");
        setContentView(R.layout.activity_main);
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container, SplashFragment.newInstance())
                .commit();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    public void setCurrentUser(ParseUser user){
        Gson gson = new Gson();
        String json = gson.toJson(user);
        SharedPreferences sharedPreferences = this.getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("CurrentUser", json);
        editor.apply();
        this.currentUser = user;
    }

    public ParseUser getCurrentUser(){
        SharedPreferences sharedPreferences = this.getPreferences(Context.MODE_PRIVATE);
        Gson gson = new Gson();
        String json = sharedPreferences.getString("CurrentUser", "");
        currentUser = gson.fromJson(json, ParseUser.class);
        return currentUser;
    }


    public void performFragmentTransaction(Fragment fragment){
       getSupportFragmentManager()
               .beginTransaction()
               .replace(R.id.fragment_container, fragment)
               .addToBackStack(null)
               .commit();
    }
}
