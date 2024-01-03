package com.example.ecocity;

public class ActivityHelper {
    private String title;
    private String location;
    private String dateActivity;
    private String key;
    private String startTimeActivity;

    public ActivityHelper(){
    }

    public ActivityHelper(String title, String location, String dateActivity, String key) {
        this.title = title;
        this.location = location;
        this.dateActivity = dateActivity;
        this.key = key;
    }

    public String getTitle() {
        return title;
    }

    public String getLocation() {
        return location;
    }

    public String getDateActivity() {
        return dateActivity;
    }

    public String getKey(){
        return key;
    }

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
    public String getStartTimeActivity(){
        return startTimeActivity;
    }

    public void setStartTimeActivity(String startTime){
        this.startTimeActivity = startTime;
    }
}
