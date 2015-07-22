package com.xtern.cultural_trail.tasks;

import android.os.AsyncTask;
import android.util.Log;

import com.cloudinary.Cloudinary;
import com.xtern.cultural_trail.models.PhotoAuth;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by kyle on 7/13/15.
 */
public class UploadImageTask extends AsyncTask<Void,Void,Void> {
    private File photoFile;
    private PhotoAuth photoAuth;
    private  Cloudinary cloudinary;

    public UploadImageTask(File phptoFile, PhotoAuth photoAuth){
        this.photoFile = phptoFile;
        this.photoAuth = photoAuth;
        Map config = new HashMap();
        config.put("cloud_name", "dwyty6kfx");
        cloudinary = new Cloudinary(config);


    }
    @Override
    protected Void doInBackground(Void... params) {
        InputStream is = null;
        try {
            is = new FileInputStream(photoFile);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        try {
            cloudinary.uploader().upload(is, Cloudinary.asMap("public_id", photoAuth.public_id, "signature", photoAuth.signature, "timestamp", photoAuth.timestamp, "api_key", photoAuth.api_key));
        } catch (IOException e) {
            e.printStackTrace();
        }
        Log.d("TASK","upload "+cloudinary.url().generate(photoAuth.public_id));
        return null;
    }
}
