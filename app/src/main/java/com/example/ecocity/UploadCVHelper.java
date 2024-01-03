package com.example.ecocity;

public class UploadCVHelper {

    public String activityID, status, fileName, url;

    public UploadCVHelper(){}

    public UploadCVHelper(String activityID, String status, String fileName, String url) {
        this.activityID = activityID;
        this.status = status;
        this.fileName = fileName;
        this.url = url;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public UploadCVHelper(String fileName, String url) {
        this.fileName = fileName;
        this.url = url;
    }

    public String getActivityID() {
        return activityID;
    }

    public void setActivityID(String activityID) {
        this.activityID = activityID;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }
}
