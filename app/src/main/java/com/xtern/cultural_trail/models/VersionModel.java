package com.xtern.cultural_trail.models;

/**
 * Created by kyle on 7/6/15.
 */
public class VersionModel {

    public String name;

    public static final String[] data = {"Cupcake", "Donut", "Eclair",
            "Froyo", "Gingerbread", "Honeycomb",
            "Icecream Sandwich", "Jelly Bean", "Kitkat", "Lollipop"};

    VersionModel(String name) {
        this.name = name;
    }
}
