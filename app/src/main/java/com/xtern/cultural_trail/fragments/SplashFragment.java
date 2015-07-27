package com.xtern.cultural_trail.fragments;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.AppCompatButton;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.xtern.cultural_trail.R;
import com.xtern.cultural_trail.activities.MainActivity;

public class SplashFragment extends Fragment {

    private MainActivity mainActivity;

    private Button loginButton;
    private Button signUpButton;

    public static SplashFragment newInstance() {
        SplashFragment fragment = new SplashFragment();
        return fragment;
    }

    public SplashFragment() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mainActivity = (MainActivity) getActivity();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_splash, container, false);
        loginButton = (Button) v.findViewById(R.id.splash_login_button);
        signUpButton = (Button) v.findViewById(R.id.splash_signup_button);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mainActivity.performFragmentTransaction(LoginFragment.newInstance());
            }
        });
        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mainActivity.performFragmentTransaction(SignUpFragment.newInstance());
            }
        });
        return v;
    }

}
