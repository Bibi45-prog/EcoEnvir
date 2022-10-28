package com.example.ecoenvir;

public class RewardHistory {
    String rewardId;
    Long timestamp;

    public RewardHistory(String rewardId, Long timestamp) {
        this.rewardId = rewardId;
        this.timestamp = timestamp;
    }

    public RewardHistory() {
    }

    public String getRewardId() {
        return rewardId;
    }

    public void setRewardId(String rewardId) {
        this.rewardId = rewardId;
    }

    public Long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }
}
