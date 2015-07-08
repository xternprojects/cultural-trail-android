package com.xtern.cultural_trail.fragments;

import android.app.Activity;
import android.content.Context;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RadioGroup;

import com.xtern.cultural_trail.R;
import com.xtern.cultural_trail.interfaces.CulturalTrailAPI;
import com.xtern.cultural_trail.models.CulturalTrailRestClient;
import com.xtern.cultural_trail.models.Issue;

import org.joda.time.DateTime;
import org.joda.time.LocalDate;

import java.util.Date;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class CreateIssueFragment extends Fragment {

    private EditText issueNameField;
    private EditText issueDescriptionField;
    private RadioGroup priorityOptionsField;
    private EditText reportedByField;

    public static CreateIssueFragment newInstance() {
        CreateIssueFragment fragment = new CreateIssueFragment();
        return fragment;
    }

    public CreateIssueFragment() {}

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_create_issue, container, false);
        issueNameField = (EditText) v.findViewById(R.id.issue_name_et);
        issueDescriptionField = (EditText) v.findViewById(R.id.issue_description_et);
        priorityOptionsField = (RadioGroup) v.findViewById(R.id.priority_option_rg);
        reportedByField = (EditText) v.findViewById(R.id.reported_by_et);
        return v;
    }

    public void getLocation(){

    }

    public void createNewIssue(){
        String issueName = issueNameField.getText().toString();
        String issueDescription = issueDescriptionField.getText().toString();
        String reportedBy = reportedByField.getText().toString();
        int priority = priorityOptionsField.getCheckedRadioButtonId();
        LocalDate localDate = new LocalDate();
        Issue newIssue = new Issue(issueName, issueDescription, priority, true, null, localDate.toString(), null);
        submitIssue(newIssue);

    }


    public void submitIssue(Issue issue){
        CulturalTrailRestClient.getClient().postIssue(issue, new Callback<Void>() {
            @Override
            public void success(Void aVoid, Response response) {
                Log.d("API", "Success");
            }

            @Override
            public void failure(RetrofitError error) {
                Log.d("API", "Failure " + error.getResponse().toString());
            }
        });
    }
}
