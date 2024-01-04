package com.example.ecocity;

public class VolListHelper {
    private String title;
    private String location;
    private String dateActivity;
    private String key;
    private String startTimeActivity;
    private String organizerName;
    private String username;

    public VolListHelper(){
    }

    public VolListHelper(String title, String location, String dateActivity, String startTimeActivity, String key, String organizerName, String username) {
        this.title = title;
        this.location = location;
        this.startTimeActivity = startTimeActivity;
        this.dateActivity = dateActivity;
        this.key = key;
        this.organizerName = organizerName;
        this.username = username;
    }

    public String getTitle() {
        return title;
    }

    public String getLocation() {
        return location;
    }

    public String getStartTimeActivity(){
        return startTimeActivity;
    }

    public String getDateActivity() {
        return dateActivity;
    }

    public String getKey(){
        return key;
    }

    public String getOrganizerName() { return organizerName; }
    public String getUsername() { return username; }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setLocation(String location) {
        this.location = location;
    }


    public void setDateActivity(String date) {
        this.dateActivity = date;
    }

    public void setKey(String key){
        this.key = key;
    }

    public void setStartTimeActivity(String startTime){
        this.startTimeActivity = startTime;
    }

    public void setOrganizerName(String organizerName) {
        this.organizerName = organizerName;
    }

    public void setUsername(String username){this.username = username;}
}