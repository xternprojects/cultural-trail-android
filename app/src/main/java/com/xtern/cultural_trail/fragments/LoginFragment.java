package com.xtern.cultural_trail.fragments;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;
import com.xtern.cultural_trail.R;
import com.xtern.cultural_trail.activities.MainActivity;

public class LoginFragment extends Fragment {

    private EditText loginEmail;
    private EditText loginPassword;
    private EditText loginConfirmPassword;
    private Button loginButton;
    private Button signUpButton;
    private MainActivity mainActivity;


    public static LoginFragment newInstance() {
        LoginFragment fragment = new LoginFragment();
        return fragment;
    }

    public LoginFragment() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_login, container, false);
        mainActivity = (MainActivity) getActivity();
        loginEmail = (EditText) view.findViewById(R.id.login_email);
        loginPassword = (EditText) view.findViewById(R.id.login_password);
        loginConfirmPassword = (EditText) view.findViewById(R.id.login_confirmPassword);
        loginButton = (Button) view.findViewById(R.id.login_button);
        signUpButton = (Button) view.findViewById(R.id.signUp_button);
        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                confirmPassword(v);
            }
        });
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginUser();
            }
        });
        return view;
    }

    public void confirmPassword(View v){
        loginConfirmPassword.setVisibility(View.VISIBLE);
        RelativeLayout.LayoutParams signupParams = (RelativeLayout.LayoutParams) v.getLayoutParams();
        RelativeLayout.LayoutParams loginParams = (RelativeLayout.LayoutParams) getActivity().findViewById(R.id.login_button).getLayoutParams();
        signupParams.addRule(RelativeLayout.BELOW, R.id.login_confirmPassword);
        loginParams.addRule(RelativeLayout.BELOW, R.id.login_confirmPassword);
        loginButton.setLayoutParams(loginParams);
        signUpButton.setLayoutParams(signupParams);
        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signUpUser();
            }
        });
    }

    public void signUpUser(){
        ParseUser parseUser = new ParseUser();
        String user = loginEmail.getText().toString();
        String pass = loginPassword.getText().toString();
        if ( !user.isEmpty() )
            parseUser.setUsername(user);
        if ( !pass.isEmpty() )
            parseUser.setPassword(pass);
        parseUser.signUpInBackground(new SignUpCallback() {
            @Override
            public void done(ParseException e) {
                if ( e == null ){
                    Toast.makeText(getActivity(), "Sign Up Complete\n Please Check Your Email", Toast.LENGTH_LONG).show();
                    mainActivity.performFragmentTransaction(new LoginFragment());
                } else {
                    Toast.makeText(getActivity(), e.getCode(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void loginUser(){
        String user = loginEmail.getText().toString();
        String pass = loginPassword.getText().toString();
        ParseUser.logInInBackground(user, pass, new LogInCallback() {
            @Override
            public void done(ParseUser parseUser, ParseException e) {
                if ( parseUser != null ){
                    mainActivity.performFragmentTransaction(new IssuesListFragment());
                } else {
                    Toast.makeText(getActivity(), "Login Failed", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


}
