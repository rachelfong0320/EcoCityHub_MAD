package com.example.madassignment;

public class ApplicantsListHelper {
    String username, url, status, activityKey;

    public ApplicantsListHelper(String username, String url, String status, String activityKey){
        this.username = username;
        this.url = url;
        this.status = status;
        this.activityKey = activityKey;
    }

    public String getKey() {
        return activityKey;
    }

    public void setKey(String activityKey) {
        this.activityKey = activityKey;
    }

    public String getUsername(){
        return username;
    }

    public void setUsername(String username){
        this.username = username;
    }

    public String getUrl(){
        return url;
    }

    public void setUrl(String url){
        this.url = url;
    }

    public String getStatus(){
        return status;
    }

    public void setStatus(String status){
        this.status = status;
    }
}
