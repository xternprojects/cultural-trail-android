package com.xtern.cultural_trail.models;

import com.xtern.cultural_trail.interfaces.CulturalTrailAPI;

import retrofit.RestAdapter;

/**
 * Created by kyle on 6/29/15.
 */
public class CulturalTrailRestClient {
    private static CulturalTrailAPI REST_CLIENT;
    private static String API_ROOT = "http://culturaltrail.herokuapp.com";

    static {
        setupRestClient();
    }

    private CulturalTrailRestClient() {}

    public static CulturalTrailAPI getClient() {
        return REST_CLIENT;
    }

    private static void setupRestClient() {
        RestAdapter.Builder builder = new RestAdapter.Builder()
                .setEndpoint(API_ROOT)
                .setLogLevel(RestAdapter.LogLevel.BASIC);

        RestAdapter restAdapter = builder.build();
        REST_CLIENT = restAdapter.create(CulturalTrailAPI.class);
    }
}
