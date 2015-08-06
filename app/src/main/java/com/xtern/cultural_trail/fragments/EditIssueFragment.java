package com.xtern.cultural_trail.fragments;


import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.xtern.cultural_trail.R;

/**
 * A simple {@link Fragment} subclass.
 */

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Switch;

import com.afollestad.materialdialogs.MaterialDialog;
import com.cloudinary.Cloudinary;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.parse.ParseUser;
import com.squareup.picasso.Picasso;
import com.xtern.cultural_trail.R;
import com.xtern.cultural_trail.activities.MainActivity;
import com.xtern.cultural_trail.models.CulturalTrailRestClient;
import com.xtern.cultural_trail.models.Issue;
import com.xtern.cultural_trail.models.Location;
import com.xtern.cultural_trail.models.PhotoAuth;
import com.xtern.cultural_trail.tasks.UploadImageTask;

import org.joda.time.LocalDate;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class EditIssueFragment extends android.support.v4.app.Fragment {

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
    private Issue issue;
    private String TAG = "EditIssueFragment";
    public static CreateIssueFragment newInstance() {
        CreateIssueFragment fragment = new CreateIssueFragment();
        return fragment;
    }

    public EditIssueFragment() {}

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_create_issue, container, false);

        Bundle args = getArguments();
        issue = (Issue) args.getSerializable("issue");


        priority = 0;

        final Toolbar toolbar = MainActivity.toolbar;
        toolbar.getMenu().clear();
        toolbar.setTitle("Edit Issue");
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_white_24dp);
        toolbar.inflateMenu(R.menu.menu_create);

        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                if (menuItem.getItemId() == R.id.action_submit) {
                    editIssue();
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
                .load(issue.picture)
                .resize(500,500)
                .into(issueImage);


        prioritySwitch = (Switch)v.findViewById(R.id.priority_switch);
        if(issue.priority == 1){
            prioritySwitch.setChecked(true);
        } else {
            prioritySwitch.setChecked(false);
        }
        prioritySwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(issue.priority == 0){
                    issue.priority = 1;
                } else if(issue.priority == 1){
                    issue.priority = 0;
                }
            }
        });

        issueDescriptionField = (EditText)v.findViewById(R.id.edit_issue_description_edit_text);
        issueDescriptionField.setText(issue.description);

        issue_edit = (EditText)v.findViewById(R.id.edit_issue_edit_view);
        issue_edit.setText(issue.name);
        issue_edit.setFocusable(false);

        issue_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleIssueName();
            }
        });

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
    }



    public void editIssue(){
        String issueName ;
        if(damageType == null || damageItem == null){
            issueName = issue.name;
        } else {
            issueName = damageType +" " + damageItem;
        }
        String issueDescription = issueDescriptionField.getText().toString();
        String reportedBy = ParseUser.getCurrentUser().getUsername();
        LocalDate localDate = new LocalDate();

        String imgUrl;
        if(mCurrentPhotoPath != null){
            imgUrl =cloudinary.url().generate(imageName);
        } else {
            imgUrl = issue.picture;
        }




        Issue newIssue = new Issue(issue._id,issueName, issueDescription, issue.priority, true, issue.location, issue.reportedBy, localDate.toString(), localDate.toString(), imgUrl);
        submitIssue(newIssue);
    }


    public void submitIssue(Issue issue){
        CulturalTrailRestClient.getClient().editIssue(issue, new Callback<Void>() {
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
