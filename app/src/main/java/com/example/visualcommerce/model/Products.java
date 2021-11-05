package com.example.visualcommerce.model;

public class Products {
    private String description, name, price, quantity, image, pid,png;

    public Products() {

    }

    public Products(String description, String name, String price, String quantity, String image, String pid, String png) {
        this.description = description;
        this.name = name;
        this.price = price;
        this.quantity = quantity;
        this.image = image;
        this.pid = pid;
        this.png = png;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public String getPng() {
        return png;
    }

    public void setPng(String png) {
        this.png = png;
    }
}
