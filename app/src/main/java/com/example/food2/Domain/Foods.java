package com.example.food2.Domain;

import java.io.Serializable;

public class Foods implements Serializable {
    private int CategoryId;
    private boolean BestFood;
    private int Id;
    private double Price;
    private String descENG;
    private String nameENG;
    private String descESP;
    private String nameESP;
    private String ImagePath;
    private int numberInCart;

    public String getDescENG() {
        return descENG;
    }

    public void setDescENG(String descENG) {
        this.descENG = descENG;
    }

    public String getNameENG() {
        return nameENG;
    }

    public void setNameENG(String nameENG) {
        this.nameENG = nameENG;
    }

    public String getDescESP() {
        return descESP;
    }

    public void setDescESP(String descESP) {
        this.descESP = descESP;
    }

    public String getNameESP() {
        return nameESP;
    }

    public void setNameESP(String nameESP) {
        this.nameESP = nameESP;
    }

    public Foods() {
    }

    @Override
    public String toString() {
        return nameESP;
    }

    public int getCategoryId() {
        return CategoryId;
    }

    public void setCategoryId(int categoryId) {
        CategoryId = categoryId;
    }

    public boolean isBestFood() {
        return BestFood;
    }

    public void setBestFood(boolean bestFood) {
        BestFood = bestFood;
    }

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    public double getPrice() {
        return Price;
    }

    public void setPrice(double price) {
        Price = price;
    }

    public String getImagePath() {
        return ImagePath;
    }

    public void setImagePath(String imagePath) {
        ImagePath = imagePath;
    }

    public int getNumberInCart() {
        return numberInCart;
    }

    public void setNumberInCart(int numberInCart) {
        this.numberInCart = numberInCart;
    }
}
