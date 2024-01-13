package com.example.ecocity;

public class ResourceDetails {
    private String downloadUri;
    private String selectedCondition;
    private String selectedLocation;
    private String resourceTitle;
    private String notesOnLocation;
    private String notesOnCondition;
    private String descriptionOnResource;



    // Default constructor (required by Firebase)
    public ResourceDetails() {
    }


    // Constructor to initialize the fields
    public ResourceDetails(String downloadUri, String selectedCondition, String selectedLocation, String resourceTitle,
                           String notesOnLocation, String notesOnCondition, String descriptionOnResource) {
        this.downloadUri = downloadUri;
        this.selectedCondition = selectedCondition;
        this.selectedLocation = selectedLocation;
        this.resourceTitle = resourceTitle;
        this.notesOnLocation = notesOnLocation;
        this.notesOnCondition = notesOnCondition;
        this.descriptionOnResource = descriptionOnResource;
    }

    // Getter and setter methods for each field

    public String getSelectedCondition() {
        return selectedCondition;
    }

    public void setSelectedCondition(String selectedCondition) {
        this.selectedCondition = selectedCondition;
    }

    public String getSelectedLocation() {
        return selectedLocation;
    }

    public void setSelectedLocation(String selectedLocation) {
        this.selectedLocation = selectedLocation;
    }

    public String getResourceTitle() {
        return resourceTitle;
    }

    public void setResourceTitle(String resourceTitle) {
        this.resourceTitle = resourceTitle;
    }

    public String getNotesOnLocation() {
        return notesOnLocation;
    }

    public void setNotesOnLocation(String notesOnLocation) {
        this.notesOnLocation = notesOnLocation;
    }

    public String getNotesOnCondition() {
        return notesOnCondition;
    }

    public void setNotesOnCondition(String notesOnCondition) {
        this.notesOnCondition = notesOnCondition;
    }

    public String getDescriptionOnResource() {
        return descriptionOnResource;
    }

    public void setDescriptionOnResource(String descriptionOnResource) {
        this.descriptionOnResource = descriptionOnResource;
    }
    public String getResourceUrl() {
        return downloadUri;
    }

    public void setResourceUrl(String resourceUrl) {
        this.downloadUri = resourceUrl;
    }
}

