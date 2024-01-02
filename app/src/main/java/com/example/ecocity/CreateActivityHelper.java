package com.example.ecocity;

public class CreateActivityHelper {
    private String title;
    private String description;
    private String dateActivity;
    private String startTimeActivity;
    private String endTimeActivity;
    private String location;
    private String addressActivity;
    private String minimumAge;
    private String maximumAge;
    private String requirements;
    private String contactNo;
    private String points;

    public CreateActivityHelper(){
    }

    public CreateActivityHelper(String title, String description, String dateActivity, String startTimeActivity, String endTimeActivity, String location, String addressActivity, String minimumAge, String maximumAge, String requirements, String contactNo, String points) {
        this.title = title;
        this.description = description;
        this.dateActivity = dateActivity;
        this.startTimeActivity = startTimeActivity;
        this.endTimeActivity = endTimeActivity;
        this.location = location;
        this.addressActivity = addressActivity;
        this.minimumAge = minimumAge;
        this.maximumAge = maximumAge;
        this.requirements = requirements;
        this.contactNo = contactNo;
        this.points = points;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getDateActivity() {
        return dateActivity;
    }

    public String getStartTimeActivity() {
        return startTimeActivity;
    }

    public String getEndTimeActivity() {
        return endTimeActivity;
    }

    public String getLocation() {
        return location;
    }

    public String getAddressActivity() {
        return addressActivity;
    }

    public String getMinimumAge() {
        return minimumAge;
    }

    public String getMaximumAge() {
        return maximumAge;
    }

    public String getRequirements() {
        return requirements;
    }

    public String getContactNo() {
        return contactNo;
    }

    public String getPoints() {
        return points;
    }
}
