package com.xtern.cultural_trail.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;
import com.xtern.cultural_trail.R;
import com.xtern.cultural_trail.activities.MainActivity;

public class SignUpFragment extends Fragment {
    private MainActivity mainActivity;

    private Button signUpButton;
    private EditText usernameEditText;
    private EditText passwordEditText;
    private EditText confirmEditText;
    private EditText emailEditText;
    private TextView errorMessage;

    public SignUpFragment() {}

    public static SignUpFragment newInstance(){
        SignUpFragment fragment = new SignUpFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mainActivity = (MainActivity) getActivity();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_sign_up, container, false);

        signUpButton = (Button) v.findViewById(R.id.signup_button);
        usernameEditText = (EditText) v.findViewById(R.id.signup_username_edittext);
        passwordEditText = (EditText) v.findViewById(R.id.signup_password_edittext);
        confirmEditText = (EditText) v.findViewById(R.id.signup_confirm_edittext);
        errorMessage = (TextView) v.findViewById(R.id.signup_error_textview);
        emailEditText = (EditText)v.findViewById(R.id.signup_email_edit_text);



        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String username = usernameEditText.getText().toString();
                final String password = passwordEditText.getText().toString();
                final String confirm = confirmEditText.getText().toString();
                final String email  = emailEditText.getText().toString();
                if ( password.equals(confirm) ) {
                    signUpUser(username, password, email);
                } else {
                    errorMessage.setText("Please Try Again");
                }
            }
        });
        return v;
    }

    public void signUpUser(String user, String pass, String email){
        ParseUser parseUser = new ParseUser();
        if ( !user.isEmpty() )
            parseUser.setUsername(user);
            parseUser.setEmail(user);
        if ( !pass.isEmpty() )
            parseUser.setPassword(pass);

        if(!email.isEmpty()){
            parseUser.setEmail(email);
        }

        parseUser.signUpInBackground(new SignUpCallback() {
            @Override
            public void done(ParseException e) {
                if ( e == null ){
                    Toast.makeText(getActivity(), "Sign Up Complete\n Please Check Your Email", Toast.LENGTH_LONG).show();
                    mainActivity.performFragmentTransaction(LoginFragment.newInstance());
                } else {
                    Log.d("Signup", "Error:"+e.getCode());
                    Toast.makeText(getActivity(), e.getCode(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


}
