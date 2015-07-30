package com.xtern.cultural_trail.fragments;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.cloudinary.Cloudinary;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.xtern.cultural_trail.R;
import com.xtern.cultural_trail.interfaces.CulturalTrailAPI;
import com.xtern.cultural_trail.models.CulturalTrailRestClient;
import com.xtern.cultural_trail.models.Issue;
import com.xtern.cultural_trail.models.Location;
import com.xtern.cultural_trail.models.PhotoAuth;
import com.xtern.cultural_trail.tasks.UploadImageTask;

import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.w3c.dom.Text;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class CreateIssueFragment extends Fragment implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    private EditText issueNameField;
    private EditText issueDescriptionField;
    private RadioGroup priorityOptionsField;
    private TextView priorityTextView;
    private EditText reportedByField;
    private Button createIssueButton;
    private Button takePictureButton;
    private String imageName;
    private GoogleApiClient mGoogleApiClient;
    private Cloudinary cloudinary;
    static final int REQUEST_TAKE_PHOTO = 1;
    private File photoFile;
    String mCurrentPhotoPath;
    Map config = new HashMap();
    private String TAG = "CreateIssueFragment";


    public static CreateIssueFragment newInstance() {
        CreateIssueFragment fragment = new CreateIssueFragment();
        return fragment;
    }

    public CreateIssueFragment() {}

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_create_issue, container, false);
        config.put("cloud_name", "dwyty6kfx");
        cloudinary = new Cloudinary(config);
        imageName =  UUID.randomUUID().toString();
        issueNameField = (EditText) v.findViewById(R.id.issue_name_et);
        issueDescriptionField = (EditText) v.findViewById(R.id.issue_description_et);
        priorityOptionsField = (RadioGroup) v.findViewById(R.id.priority_option_rg);
        priorityTextView = (TextView) v.findViewById(R.id.priority_tv);
        reportedByField = (EditText) v.findViewById(R.id.reported_by_et);
        createIssueButton = (Button)v.findViewById(R.id.create_issue_button);
        takePictureButton =(Button)v.findViewById(R.id.take_picture_button);

        takePictureButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "Take Picture pressed");
                dispatchTakePictureIntent();
            }
        });

        createIssueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createNewIssue();
            }
        });

        priorityTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String current_priority = priorityTextView.getText().toString();
                if(current_priority.equals("Low Priority")) {
                    priorityTextView.setText("High Priority");
                }
                else {
                    priorityTextView.setText("Low Priority");
                }
            }
        });

        return v;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mGoogleApiClient = new GoogleApiClient.Builder(getActivity())
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
    }


    public void getLocation(){

    }

    public void createNewIssue(){
        String issueName = issueNameField.getText().toString();
        String issueDescription = issueDescriptionField.getText().toString();
        String reportedBy = reportedByField.getText().toString();
        int priorityId = priorityOptionsField.getCheckedRadioButtonId();
        RadioButton rb  = (RadioButton)getActivity().findViewById(priorityId);
        int priority = rb.getText().toString().equals("High Priority") ? 1 :0;
        int priority_test = priorityTextView.getText().toString().equals("High Priority") ? 1:0;
        LocalDate localDate = new LocalDate();
        android.location.Location lastLocation = LocationServices.FusedLocationApi.getLastLocation(
                mGoogleApiClient);
        Location location = null;
        if(lastLocation != null){
            location = new Location(lastLocation.getLatitude(),lastLocation.getLongitude());
        }


        Issue newIssue = new Issue(issueName, issueDescription, priority_test, true, location, localDate.toString(), localDate.toString(), cloudinary.url().generate(imageName));
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

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_issue_create, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public void onConnected(Bundle bundle) {
        Log.d(TAG, "GPS Connected");

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }

    @Override
    public void onStart() {
        super.onStart();
        mGoogleApiClient.connect();
    }
    @Override
    public void onStop() {
        mGoogleApiClient.disconnect();
        super.onStop();
    }



    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = "file:" + image.getAbsolutePath();
        return image;
    }

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getActivity().getPackageManager()) != null) {
            // Create the File where the photo should go
            photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                // Error occurred while creating the File

            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT,
                        Uri.fromFile(photoFile));
                startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if( requestCode == REQUEST_TAKE_PHOTO ) {
            Log.d(TAG,"Photo retrieved " + mCurrentPhotoPath);
            CulturalTrailRestClient.getClient().getPictureAuth(imageName, new Callback<PhotoAuth>() {
                @Override
                public void success(PhotoAuth photoAuth, Response response) {
                    Log.d(TAG, "auth=" + photoAuth.toString());
                    new UploadImageTask(photoFile,photoAuth).execute();
                }

                @Override
                public void failure(RetrofitError error) {

                }
            });

        }
    }
}
