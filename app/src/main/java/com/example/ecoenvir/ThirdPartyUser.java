package com.example.ecoenvir;

public class ThirdPartyUser {
    String name, username, email, providerId;
    Integer points;

    public ThirdPartyUser(String name, String username, String email, String providerId, Integer points) {
        this.name = name;
        this.username = username;
        this.email = email;
        this.providerId = providerId;
        this.points = points;
    }

    public ThirdPartyUser() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getProviderId() {
        return providerId;
    }

    public void setProviderId(String providerId) {
        this.providerId = providerId;
    }

    public Integer getPoints() {
        return points;
    }

    public void setPoints(Integer points) {
        this.points = points;
    }
}
