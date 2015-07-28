package com.xtern.cultural_trail.interfaces;

import com.xtern.cultural_trail.models.Issue;
import com.xtern.cultural_trail.models.ParseInfo;

import java.util.List;

import retrofit.Callback;
import retrofit.http.GET;

/**
 * Created by kyle on 6/29/15.
 */
public interface CulturalTrailAPI {
    @GET("/issues")
    void getIssues(Callback<List<Issue>> callback);

    @GET("/parse/auth")
    void getLoginInfo(Callback<ParseInfo> callback);
}
