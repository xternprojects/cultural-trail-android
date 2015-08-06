package com.xtern.cultural_trail.fragments;

import android.app.Activity;
import android.app.FragmentTransaction;
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
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Switch;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.cloudinary.Cloudinary;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.squareup.picasso.Picasso;
import com.xtern.cultural_trail.R;
import com.xtern.cultural_trail.activities.MainActivity;
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
import com.afollestad.materialdialogs.MaterialDialog.Builder;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class CreateIssueFragment extends Fragment implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    private EditText issueDescriptionField;
    private Switch prioritySwitch;
    private String damageItem;
    private String damageType;
    private ImageView issueImage;
    private EditText issue_edit;
    private String imageName;
    private int priority;
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

        priority = 0;

        final Toolbar toolbar = MainActivity.toolbar;
        toolbar.getMenu().clear();
        toolbar.setTitle("Create Issue");
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_white_24dp);
        toolbar.inflateMenu(R.menu.menu_create);

        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                if (menuItem.getItemId() == R.id.action_submit) {
                    createNewIssue();
                    toolbar.getMenu().clear();
                    toolbar.setTitle("Issues");
                    toolbar.inflateMenu(R.menu.menu_main);
                    getActivity().getSupportFragmentManager().popBackStack();
                }
                return false;
            }
        });



        config.put("cloud_name", "dwyty6kfx");
        cloudinary = new Cloudinary(config);
        imageName =  UUID.randomUUID().toString();
        issueImage = (ImageView)v.findViewById(R.id.issue_image);
        Picasso.with(getActivity())
                .load(R.drawable.placeholder)
                .resize(500,500)
                .into(issueImage);


        prioritySwitch = (Switch)v.findViewById(R.id.priority_switch);
        prioritySwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(priority == 0){
                    priority = 1;
                } else if(priority == 1){
                    priority = 0;
                }
            }
        });

        issueDescriptionField = (EditText)v.findViewById(R.id.issue_description_edit_text);

        issue_edit = (EditText)v.findViewById(R.id.issue_edit_view);


        issue_edit.setFocusable(false);

        issue_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleIssueName();
            }
        });

        issueImage = (ImageView)v.findViewById(R.id.issue_image);
        issueImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dispatchTakePictureIntent();
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



    public void createNewIssue(){
        String issueName = damageType + " " + damageItem;
        String issueDescription = issueDescriptionField.getText().toString();
        String reportedBy = "Kyle";
        LocalDate localDate = new LocalDate();
        android.location.Location lastLocation = LocationServices.FusedLocationApi.getLastLocation(
                mGoogleApiClient);
        Location location = null;
        if(lastLocation != null){
            location = new Location(lastLocation.getLatitude(),lastLocation.getLongitude());
        }


        Issue newIssue = new Issue(issueName, issueDescription, priority, true, location, localDate.toString(), localDate.toString(), cloudinary.url().generate(imageName));
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
            Picasso.with(getActivity())
                    .load(photoFile)
                    .resize(500,500)
                    .into(issueImage);
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


    private int getDamageArray(String item){
        switch(item){
            case "Light":
                return R.array.light_damages;
            case "Bench":
                return R.array.bench_damages;
            case "Crossing Signal":
                return R.array.crossing_signal_damages;
            case "Trail Sign":
                return R.array.sign_damages;
            case "Edging":
                return R.array.edging_damages;
            case "Bollard":
                return R.array.bollard_damages;
            case "Paver":
                return R.array.paver_damages;
            case "Crossing push button":
                return R.array.crossing_button_damages;
            case "Light control box":
                return R.array.control_box_damages;
            case "Sprinkler Box":
                return R.array.sprinkler_damages;
            case "Curb":
                return R.array.curb_damages;
            case "Trash Can":
                return R.array.trash_can_damages;
            case "Recycling Can":
                return R.array.recycling_can_damages;
            case "Glass/Debris":
                return R.array.glass_damages;
            case "Art Installation":
                return R.array.art_damages;
        }
        return 0;
    }

    public void handleIssueName(){
        final MaterialDialog.Builder builder = new MaterialDialog.Builder(getActivity());
        builder.title("Items");
        builder.items(R.array.main_items);
        final MaterialDialog dialog = builder.build();
        builder.itemsCallback(new MaterialDialog.ListCallback() {
            @Override
            public void onSelection(MaterialDialog materialDialog, View view, int i, CharSequence charSequence) {
                dialog.dismiss();
                damageItem = charSequence.toString();
                MaterialDialog.Builder damagerBuilder = new MaterialDialog.Builder(getActivity());
                damagerBuilder.title(charSequence);
                int damageArray = getDamageArray(charSequence.toString());
                Log.d(TAG, "DamageArray=" + damageArray);
                damagerBuilder.items(damageArray);
                final MaterialDialog damageDialog = damagerBuilder.build();
                damagerBuilder.itemsCallback(new MaterialDialog.ListCallback() {
                    @Override
                    public void onSelection(MaterialDialog materialDialog, View view, int i, CharSequence charSequence) {
                        damageType = charSequence.toString();
                        issue_edit.setText(damageType + " " + damageItem);
                        issue_edit.setFocusable(false);
                        damageDialog.dismiss();

                    }
                });
                damageDialog.show();
            }
        });
        dialog.show();

    }
}
