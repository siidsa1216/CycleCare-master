package com.example.cyclecare.Model;

public class Bike {
    String bikeId, bikeName, bikeModel, bikeBrand, bikeImg;


    public Bike() {

    }

    public Bike(String bikeId, String bikeName, String bikeModel, String bikeBrand, String bikeImg) {
        this.bikeId = bikeId;
        this.bikeName = bikeName;
        this.bikeModel = bikeModel;
        this.bikeBrand = bikeBrand;
        this.bikeImg = bikeImg;
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
}
