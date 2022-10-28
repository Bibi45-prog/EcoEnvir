package com.example.ecoenvir;

public class Reward {

    public String name, purchasePoint, rewardImg;


    public Reward() {
    }

    public Reward(String name, String purchasePoint, String rewardImg) {
        this.name = name;
        this.purchasePoint = purchasePoint;
        this.rewardImg = rewardImg;
    }

    public String getName() {
        return name;
    }

    public String getPurchasePoint() {
        return purchasePoint;
    }

    public String getImageUrl() {
        return rewardImg;
    }

    public void setImageUrl(String rewardImg) {
        this.rewardImg = rewardImg;
    }
}
