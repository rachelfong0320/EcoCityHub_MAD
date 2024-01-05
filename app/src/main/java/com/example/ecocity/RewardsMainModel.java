package com.example.ecocity;

public class RewardsMainModel {

    String Image, Reward_name, Re_Desc;
    int Reward_point;

    public RewardsMainModel(){

    }

    public RewardsMainModel(String image, String reward_name, String re_Desc, int reward_point) {
        Image = image;
        Reward_name = reward_name;
        Re_Desc = re_Desc;
        Reward_point = reward_point;
    }

    public String getImage() {
        return Image;
    }

    public void setImage(String image) {
        Image = image;
    }

    public String getReward_name() {
        return Reward_name;
    }

    public void setReward_name(String reward_name) {
        Reward_name = reward_name;
    }

    public String getRe_Desc() {
        return Re_Desc;
    }

    public void setRe_Desc(String re_Desc) {
        Re_Desc = re_Desc;
    }

    public int getReward_point() {
        return Reward_point;
    }

    public void setReward_point(int reward_point) {
        Reward_point = reward_point;
    }
}
