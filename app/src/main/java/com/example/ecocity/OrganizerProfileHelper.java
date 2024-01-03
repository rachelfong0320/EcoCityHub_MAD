package com.example.ecocity;

public class OrganizerProfileHelper {
    String username;
    String bio;

    public OrganizerProfileHelper(){

    }

    public OrganizerProfileHelper(String username, String bio){
        this.username = username;
        this.bio = bio;
    }

    public String getUsername(){
        return username;
    }

    public String getBio(){
        return bio;
    }

    public void setUsername (){
        this.username = username;
    }

    public void setBio (){
        this.bio = bio;
    }
}
