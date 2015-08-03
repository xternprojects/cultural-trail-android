package com.xtern.cultural_trail.cards;

import android.content.Context;
import android.graphics.Color;

import com.dexafree.materialList.cards.OnButtonPressListener;
import com.dexafree.materialList.cards.SimpleCard;
import com.xtern.cultural_trail.R;

/**
 * Created by kyle on 6/29/15.
 */
public class IssueListCard extends SimpleCard {
    private String title;
    private String description;
    private String imageUrl;

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    private String location;


    public IssueListCard(final Context context) {
        super(context);
    }

    @Override
    public int getLayout() {
        return R.layout.issue_list_card_item;
    }

    @Override
    public String getTitle() {
        return title;
    }

    @Override
    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public String getDescription() {
        return description;
    }

    @Override
    public void setDescription(String description) {
        this.description = description;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }


}
