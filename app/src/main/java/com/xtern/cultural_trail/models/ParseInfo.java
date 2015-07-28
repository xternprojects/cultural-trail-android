package com.xtern.cultural_trail.models;

/**
 * Created by tylorgarrett on 7/27/15.
 */
public class ParseInfo {
    private String app_id;
    private String client_key;

    public ParseInfo(String app_id, String client_key) {
        this.app_id = app_id;
        this.client_key = client_key;
    }

    public String getApp_id() {
        return app_id;
    }

    public String getClient_key() {
        return client_key;
    }
}
