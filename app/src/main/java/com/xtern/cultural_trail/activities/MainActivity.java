package com.xtern.cultural_trail.activities;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.google.gson.Gson;
import com.parse.Parse;
import com.parse.ParseUser;
import com.xtern.cultural_trail.R;
import com.xtern.cultural_trail.fragments.IssuesListFragment;
import com.xtern.cultural_trail.fragments.SplashFragment;
import com.xtern.cultural_trail.models.CulturalTrailRestClient;
import com.xtern.cultural_trail.models.ParseInfo;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;


public class MainActivity extends AppCompatActivity {

    ParseUser currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getParseInfo();
        Parse.enableLocalDatastore(this);
        setContentView(R.layout.activity_main);
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

    public void loginOrIssues(){
        Fragment fragment;
        if ( getCurrentUser().isAuthenticated() ){
            fragment = new IssuesListFragment();
        } else {
            fragment = new SplashFragment();
        }
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .commit();
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
