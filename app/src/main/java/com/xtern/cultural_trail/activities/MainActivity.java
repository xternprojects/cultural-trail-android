package com.xtern.cultural_trail.activities;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.google.gson.Gson;
import com.parse.Parse;
import com.parse.ParseUser;
import com.xtern.cultural_trail.R;
import com.xtern.cultural_trail.fragments.CreateIssueFragment;
import com.xtern.cultural_trail.fragments.IssuesListFragment;
import com.xtern.cultural_trail.fragments.SplashFragment;
import com.xtern.cultural_trail.models.CulturalTrailRestClient;
import com.xtern.cultural_trail.models.ParseInfo;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;


public class MainActivity extends ActionBarActivity {

    public static Toolbar toolbar;
    ParseUser currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getParseInfo();
        Parse.enableLocalDatastore(this);
        setContentView(R.layout.activity_main);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Issues");
        toolbar.inflateMenu(R.menu.menu_main);
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
               .replace(R.id.fragment_container, fragment)
               .addToBackStack(null)
               .commit();
    }

    public void loginOrIssues(){
        Fragment fragment;
        if (ParseUser.getCurrentUser() !=null){
            fragment = new IssuesListFragment();
        } else {
            fragment = new SplashFragment();
        }

        performFragmentTransaction(fragment);
    }

    public void getParseInfo(){
        CulturalTrailRestClient.getClient().getLoginInfo(new Callback<ParseInfo>() {
            @Override
            public void success(ParseInfo parseInfo, Response response) {
                Parse.initialize(getApplicationContext(), parseInfo.getApp_id(), parseInfo.getClient_key());
                loginOrIssues();
            }

            @Override
            public void failure(RetrofitError error) {
                Log.d("tylor", error.toString());
            }
        });
    }
}
