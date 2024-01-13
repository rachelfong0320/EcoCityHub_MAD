package com.example.ecocity;

import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
public class DailyDiscoverItem {
    private String username;
    private String profileImageUrl;
    private String selectedLocation;
    private String selectedCondition;
    private String resourceUrl;
    private String resourceTitle;
    private String resourceId;
    private String uploaderUsername;
    private String descriptionOnResource;
    private String notesOnCondition;
    private String notesOnLocation;


    public DailyDiscoverItem() {
    }

    public DailyDiscoverItem(String username, String profileImageUrl, String selectedLocation, String selectedCondition, String resourceUrl, String resourceTitle, String descriptionOnResource, String notesOnCondition, String notesOnLocation) {
        this.username = username;
        //this.profileImageUrl = profileImageUrl;
        this.selectedLocation = selectedLocation;
        this.selectedCondition = selectedCondition;
        this.resourceUrl = resourceUrl;
        this.resourceTitle = resourceTitle;
        this.descriptionOnResource = descriptionOnResource;
        this.notesOnCondition = notesOnCondition;
        this.notesOnLocation = notesOnLocation;
    }

    public String getResourceId(){
        return resourceId;
    }

    public String getUploaderName(){
        return uploaderUsername;
    }

    // Add getters and setters for all fields

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getprofileImageUrl() {
        return profileImageUrl;
    }

    public void setprofileImageUrl(String profileImageUrl) {
        this.profileImageUrl = profileImageUrl;
    }

    public String getselectedLocation() {
        return selectedLocation;
    }

    public void setselectedLocation(String selectedLocation) {
        this.selectedLocation = selectedLocation;
    }

    public String getselectedCondition() {
        return selectedCondition;
    }

    public void setselectedCondition(String selectedCondition) {
        this.selectedCondition = selectedCondition;
    }

    public String getresourceUrl() {
        return resourceUrl;
    }

    public void setresourceUrl(String resourceUrl) {
        this.resourceUrl = resourceUrl;
    }

    public String getresourceTitle() {
        return resourceTitle;
    }

    public void setresourceTitle(String resourceTitle) {
        this.resourceTitle = resourceTitle;
    }


    public String getDescriptionOnResource() {
        return descriptionOnResource;
    }

    public void setDescriptionOnResource(String descriptionOnResource) {
        this.descriptionOnResource = descriptionOnResource;
    }

    public String getNotesOnCondition() {
        return notesOnCondition;
    }

    public void setNotesOnCondition(String notesOnCondition) {
        this.notesOnCondition = notesOnCondition;
    }

    public String getNotesOnLocation() {
        return notesOnLocation;
    }

    public void setNotesOnLocation(String notesOnLocation) {
        this.notesOnLocation = notesOnLocation;
    }
}
