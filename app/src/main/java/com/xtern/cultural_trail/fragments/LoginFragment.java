package com.xtern.cultural_trail.fragments;


import android.app.ListFragment;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.xtern.cultural_trail.R;
import com.xtern.cultural_trail.activities.MainActivity;

public class LoginFragment extends Fragment {

    private MainActivity mainActivity;

    private Button loginButton;
    private EditText userField;
    private EditText passField;

    private String username;
    private String password;

    public LoginFragment() {}

    public static LoginFragment newInstance(){
        LoginFragment fragment = new LoginFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_login, container, false);
        mainActivity = (MainActivity) getActivity();
        loginButton = (Button) v.findViewById(R.id.login_button);
        userField = (EditText) v.findViewById(R.id.login_username_textview);
        passField = (EditText) v.findViewById(R.id.login_password_textview);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                beginLogin();
            }
        });
        return v;
    }

    public void beginLogin(){
        username = userField.getText().toString();
        password= passField.getText().toString();

        if ( !username.isEmpty() && !password.isEmpty() ){
            login();
        } else {
            Toast.makeText(getActivity(), "Please fill in both fields", Toast.LENGTH_LONG).show();
        }
    }

    public void login(){
        ParseUser.logInInBackground(username, password, new LogInCallback() {
            @Override
            public void done(ParseUser parseUser, ParseException e) {
                if ( e != null ){
                    Toast.makeText(getActivity(), "Login Error, please try again", Toast.LENGTH_LONG).show();
                } else {
                    mainActivity.performFragmentTransaction(new IssuesListFragment());
                }
            }
        });
    }


}
