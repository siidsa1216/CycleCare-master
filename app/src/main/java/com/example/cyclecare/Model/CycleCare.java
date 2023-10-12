package com.example.cyclecare.Model;

public class CycleCare {
    String unitID, unitName, unitNumber, unitLocation, receiverName, dateReceived, installerName, dateInstalled;

    public CycleCare() {
    }

    public CycleCare(String unitID, String unitName, String unitNumber, String unitLocation, String receiverName, String dateReceived, String installerName, String dateInstalled) {
        this.unitID = unitID;
        this.unitName = unitName;
        this.unitNumber = unitNumber;
        this.unitLocation = unitLocation;
        this.receiverName = receiverName;
        this.dateReceived = dateReceived;
        this.installerName = installerName;
        this.dateInstalled = dateInstalled;
    }

    public String getUnitID() {
        return unitID;
    }

    public void setUnitID(String unitID) {
        this.unitID = unitID;
    }

    public String getUnitName() {
        return unitName;
    }

    public void setUnitName(String unitName) {
        this.unitName = unitName;
    }

    public String getUnitNumber() {
        return unitNumber;
    }

    public void setUnitNumber(String unitNumber) {
        this.unitNumber = unitNumber;
    }

    public String getUnitLocation() {
        return unitLocation;
    }

    public void setUnitLocation(String unitLocation) {
        this.unitLocation = unitLocation;
    }

    public String getReceiverName() {
        return receiverName;
    }

    public void setReceiverName(String receiverName) {
        this.receiverName = receiverName;
    }

    public String getDateReceived() {
        return dateReceived;
    }

    public void setDateReceived(String dateReceived) {
        this.dateReceived = dateReceived;
    }

    public String getInstallerName() {
        return installerName;
    }

    public void setInstallerName(String installerName) {
        this.installerName = installerName;
    }

    public String getDateInstalled() {
        return dateInstalled;
    }

    public void setDateInstalled(String dateInstalled) {
        this.dateInstalled = dateInstalled;
    }
}
