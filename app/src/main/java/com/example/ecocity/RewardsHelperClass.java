package com.example.ecocity;

public class RewardsHelperClass {
    String name, description;
    int point;

    public RewardsHelperClass(){

    }

    public RewardsHelperClass(String name, String description, int point) {
        this.name = name;
        this.description = description;
        this.point = point;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getPoint() {
        return point;
    }

    public void setPoint(int point) {
        this.point = point;
    }
}
