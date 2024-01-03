package com.example.ecocity;

public class ActivityHelperClass {
    String title, description, dateActivity, startTimeActivity, endTimeActivity, location, address, minimumAge, maximumAge, requirements, contactNo, points, username;

    public ActivityHelperClass(String title, String description, String dateActivity, String startTimeActivity, String endTimeActivity, String location, String address, String minimumAge, String maximumAge, String requirements, String contactNo, String points, String username) {
        this.title = title;
        this.description = description;
        this.dateActivity = dateActivity;
        this.startTimeActivity = startTimeActivity;
        this.endTimeActivity = endTimeActivity;
        this.location = location;
        this.address = address;
        this.minimumAge = minimumAge;
        this.maximumAge = maximumAge;
        this.requirements = requirements;
        this.contactNo = contactNo;
        this.points = points;
        this.username = username;
    }

    public ActivityHelperClass(){

    }

    public String getOrganizerID() {
        return username;
    }

    public void setOrganizerID(String username) {
        this.username = username;
    }

    public String getMinimumAge() {
        return minimumAge;
    }

    public void setMinimumAge(String minimumAge) {
        this.minimumAge = minimumAge;
    }

    public String getMaximumAge() {
        return maximumAge;
    }

    public void setMaximumAge(String maximumAge) {
        this.maximumAge = maximumAge;
    }

    public String getRequirements() {
        return requirements;
    }

    public void setRequirements(String requirements) {
        this.requirements = requirements;
    }

    public String getContactNo() {
        return contactNo;
    }

    public void setContactNo(String contactNo) {
        this.contactNo = contactNo;
    }

    public String getPoints() {
        return points;
    }

    public void setPoints(String points) {
        this.points = points;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDateActivity() {
        return dateActivity;
    }

    public void setDateActivity(String dateActivity) {
        this.dateActivity = dateActivity;
    }

    public String getStartTimeActivity() {
        return startTimeActivity;
    }

    public void setStartTimeActivity(String startTimeActivity) {
        this.startTimeActivity = startTimeActivity;
    }

    public String getEndTimeActivity() {
        return endTimeActivity;
    }

    public void setEndTimeActivity(String endTimeActivity) {
        this.endTimeActivity = endTimeActivity;
    }

    public String getLocation() {
        return this.location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getAddress() {
        return this.address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}