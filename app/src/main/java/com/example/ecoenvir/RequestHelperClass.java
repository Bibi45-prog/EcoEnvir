package com.example.ecoenvir;

public class RequestHelperClass {
    private String description, date, time, phone, address, image, method, quantity ;

    public RequestHelperClass(String description, String quantity, String date, String time, String address,String phone, String image, String method) {
        this.description = description;
        this.date = date;
        this.time = time;
        this.phone = phone;
        this.address = address;
        this.image = image;
        this.method = method;
        this.quantity = quantity;

    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public void setPhone(String phone){this.phone = phone;}

    public void setAddress(String address) {
        this.address = address;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getDescription() {
        return description;
    }

    public String getDate() {
        return date;
    }

    public String getTime() {
        return time;
    }

    public String getPhone() {return phone;}

    public String getAddress() {
        return address;
    }

    public String getImage() {
        return image;
    }

    public String getMethod() {
        return method;
    }

    public String getQuantity() {
        return quantity;
    }

}
