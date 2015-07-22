package com.xtern.cultural_trail.models;

/**
 * Created by kyle on 7/13/15.
 */
public class PhotoAuth {
    public final String signature;
    public final String public_id;
    public final String timestamp;
    public final String api_key;

    public PhotoAuth(String signature, String public_id, String timestamp,String api_key){
        this.signature = signature;
        this.public_id = public_id;
        this.timestamp = timestamp;
        this.api_key = api_key;
    }

}
