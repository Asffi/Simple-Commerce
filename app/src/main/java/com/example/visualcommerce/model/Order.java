package com.example.visualcommerce.model;

public class Order {
    private String totalAmount,orderId,name,Phone,city,postalCode,address,date,time,state;

    Order(){

    }
    public Order(String totalAmount, String orderId, String name, String phone, String city, String postalCode, String address, String date, String time, String state) {
        this.totalAmount = totalAmount;
        this.orderId = orderId;
        this.name = name;
        Phone = phone;
        this.city = city;
        this.postalCode = postalCode;
        this.address = address;
        this.date = date;
        this.time = time;
        this.state = state;
    }

    public String getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(String totalAmount) {
        this.totalAmount = totalAmount;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return Phone;
    }

    public void setPhone(String phone) {
        Phone = phone;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }
}
