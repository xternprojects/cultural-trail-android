package com.xtern.cultural_trail.models;

import android.util.Log;

import org.joda.time.DateTime;

/**
 * Created by kyle on 6/29/15.
 */
public class Issue {
    public final String name;
    public final String description;
    public final int priority;
    public final boolean open;
    public final Location location;
    public final String reportedDate;
    public final String resolvedDate;


    public Issue(String name, String description, int priority, boolean open, Location location, String reportedDate, String resolvedDate) {
        this.name = name;
        this.description = description;
        this.priority = priority;
        this.open = open;
        this.location = location;
        this.reportedDate = reportedDate;
        this.resolvedDate = resolvedDate;
    }
}
