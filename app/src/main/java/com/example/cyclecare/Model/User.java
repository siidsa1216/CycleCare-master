package com.example.cyclecare.Model;

public class User {

    private String username, email, phoneNum, password, profilePicUrl, usertype, status;

    public User(){

    }

    public User(String username, String email, String phoneNum, String password, String profilePicUrl, String usertype, String status) {
        this.username = username;
        this.email = email;
        this.phoneNum = phoneNum;
        this.password = password;
        this.profilePicUrl = profilePicUrl;
        this.usertype = usertype;
        this.status = status;
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

    public String getPhoneNum() {
        return phoneNum;
    }

    public void setPhoneNum(String phoneNum) {
        this.phoneNum = phoneNum;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getProfilePicUrl() {
        return profilePicUrl;
    }

    public void setProfilePicUrl(String profilePicUrl) {
        this.profilePicUrl= profilePicUrl;
    }

    public String getUsertype() {
        return usertype;
    }

    public void setUsertype(String usertype) {
        this.usertype = usertype;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
