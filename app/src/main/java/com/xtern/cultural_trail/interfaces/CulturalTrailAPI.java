package com.xtern.cultural_trail.interfaces;

import com.xtern.cultural_trail.models.Issue;
import com.xtern.cultural_trail.models.PhotoAuth;

import java.util.List;

import retrofit.Callback;
import retrofit.http.Body;
import retrofit.http.GET;
import retrofit.http.POST;
import retrofit.http.Query;

/**
 * Created by kyle on 6/29/15.
 */
public interface CulturalTrailAPI {
    @GET("/issues")
    void getIssues(Callback<List<Issue>> callback);

    @POST("/issues")
    void postIssue(@Body Issue newIssue, Callback<Void> callback);

    @GET("/picture/auth")
    void getPictureAuth(@Query("public_id") String public_id, Callback<PhotoAuth> callback);
}
