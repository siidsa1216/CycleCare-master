package com.example.cyclecare.Model;

public class Bike {
    String bikeId, bikeName, bikeModel, bikeBrand, bikeImg;
    Boolean isparked;


    public Bike() {

    }

    public String getBikeId() {
        return bikeId;
    }

    public void setBikeId(String bikeId) {
        this.bikeId = bikeId;
    }

    public String getBikeName() {
        return bikeName;
    }

    public void setBikeName(String bikeName) {
        this.bikeName = bikeName;
    }

    public String getBikeModel() {
        return bikeModel;
    }

    public void setBikeModel(String bikeModel) {
        this.bikeModel = bikeModel;
    }

    public String getBikeBrand() {
        return bikeBrand;
    }

    public void setBikeBrand(String bikeBrand) {
        this.bikeBrand = bikeBrand;
    }

    public String getBikeImg() {
        return bikeImg;
    }

    public void setBikeImg(String bikeImg) {
        this.bikeImg = bikeImg;
    }

    public Boolean getIsparked() {
        return isparked;
    }

    public void setIsparked(Boolean isparked) {
        this.isparked = isparked;
    }
}