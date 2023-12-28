package com.example.ecocity;

public class RatingHelperClass {
    Float rateProfileSystem, rateVolunteer, rateResource, ratePoint, rateOverall;


    public RatingHelperClass(Float rateProfileSystem, Float rateVolunteer, Float rateResource, Float ratePoint, Float rateOverall) {
        this.rateProfileSystem = rateProfileSystem;
        this.rateVolunteer = rateVolunteer;
        this.rateResource = rateResource;
        this.ratePoint = ratePoint;
        this.rateOverall = rateOverall;
    }

    public Float getRateProfileSystem() {
        return rateProfileSystem;
    }

    public void setRateProfileSystem(Float rateProfileSystem) {
        this.rateProfileSystem = rateProfileSystem;
    }

    public Float getRateVolunteer() {
        return rateVolunteer;
    }

    public void setRateVolunteer(Float rateVolunteer) {
        this.rateVolunteer = rateVolunteer;
    }

    public Float getRateResource() {
        return rateResource;
    }

    public void setRateResource(Float rateResource) {
        this.rateResource = rateResource;
    }

    public Float getRatePoint() {
        return ratePoint;
    }

    public void setRatePoint(Float ratePoint) {
        this.ratePoint = ratePoint;
    }

    public Float getRateOverall() {
        return rateOverall;
    }

    public void setRateOverall(Float rateOverall) {
        this.rateOverall = rateOverall;
    }
}
