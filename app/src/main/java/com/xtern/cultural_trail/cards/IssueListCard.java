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
    private String subtitle;
    private String buttonText;
    private OnButtonPressListener mListener;
    private int titleColor = Color.BLACK;
    private int descriptionColor = Color.BLACK;
    private int subtitleColor = Color.BLACK;
    private int priorityColor = Color.BLACK;
    private int dividerColor = Color.parseColor("#608DFA");
    private int buttonTextColor = Color.BLACK;
    private int priority;

    public IssueListCard(final Context context) {
        super(context);
    }

    public String getSubtitle() {
        return subtitle;
    }

    public void setSubtitle(String subtitle) {
        this.subtitle = subtitle;
    }

    public OnButtonPressListener getOnButtonPressedListener() {
        return mListener;
    }

    public void setOnButtonPressedListener(OnButtonPressListener mListener) {
        this.mListener = mListener;
    }

    public String getButtonText() {
        return buttonText;
    }

    public int getSubtitleColor() {
        return subtitleColor;
    }

    public int getDividerColor() {
        return dividerColor;
    }

    public int getButtonTextColor() {
        return buttonTextColor;
    }

    public void setButtonText(int buttonTextId) {
        setButtonText(getString(buttonTextId));
    }

    public void setButtonText(String buttonText) {
        this.buttonText = buttonText;
    }

    public void setSubtitleColorRes(int colorId) {
        setSubtitleColor(getResources().getColor(colorId));
    }

    public void setSubtitleColor(int color) {
        this.subtitleColor = color;
    }

    public void setSubtitleColor(String color) {
        setSubtitleColor(Color.parseColor(color));
    }

    public void setDividerColorRes(int colorId) {
        setDividerColor(getResources().getColor(colorId));
    }

    public void setDividerColor(int color) {
        this.dividerColor = color;
    }

    public void setDividerColor(String color) {
        setDividerColor(Color.parseColor(color));
    }

    public void setButtonTextColorRes(int colorId) {
        setButtonTextColor(getResources().getColor(colorId));
    }

    public void setButtonTextColor(int color) {
        this.buttonTextColor = color;
    }

    public void setButtonTextColor(String color) {
        setButtonTextColor(Color.parseColor(color));
    }

    public void setTitleColor(int color){
        this.titleColor = color;
    }

    public void setTitleColor(String color){
        setTitleColor(Color.parseColor(color));
    }

    public int getTitleColor(){
        return this.titleColor;
    }

    public void setPriorityColor(int color){
        this.descriptionColor = color;
    }

    public void setPriorityColor(String color){
        setPriorityColor(Color.parseColor(color));
    }

    public int getPriorityColor(){
        return this.descriptionColor;
    }

    public void setPriority(int priority){
        this.priority = priority;
    }
    public int getPriority(){
        return priority;
    }

    public void setDescriptionColor(int color){
        this.descriptionColor = color;
    }

    public void setDescriptionColor(String color){
        setDescription(Color.parseColor(color));
    }

    public int getDescriptionColor(){
        return this.descriptionColor;
    }

    @Override
    public int getLayout() {
        return R.layout.issue_list_card_item;
    }
}
