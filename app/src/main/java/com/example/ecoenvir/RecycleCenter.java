package com.example.ecoenvir;

import java.util.HashMap;

public class RecycleCenter {
    String name, phone, address, website;
    HashMap<String,String> hours;

    public RecycleCenter() {
    }

    public RecycleCenter(String name, String phone, String address, String website, HashMap<String, String> hours) {
        this.name = name;
        this.phone = phone;
        this.address = address;
        this.website = website;
        this.hours = hours;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public HashMap<String, String> getHours() {
        return hours;
    }

    public void setHours(HashMap<String, String> hours) {
        this.hours = hours;
    }


    @Override
    public String toString() {
        return "RecycleCenter{" +
                "name='" + name + '\'' +
                ", phone='" + phone + '\'' +
                ", address='" + address + '\'' +
                ", website='" + website + '\'' +
                ", hours=" + hours +
                '}';
    }
}
