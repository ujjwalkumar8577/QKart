package com.ujjwalkumar.qkart.helper;
//this is a class named consumers which keep the details of a user.
public class consumers {
    private String address;
    private String contact;
    private String email;
    private String img;
    private String lat;
    private String lng;
    private String name;
    private String uid;

    //constructor of the class consumers
    public consumers(String address,String contact,String email,String img,String lat,String lng,String name,String uid){
        this.address=address;
        this.contact=contact;
        this.email=email;
        this.img=img;
        this.lat=lat;
        this.lng=lng;
        this.name=name;
        this.uid=uid;
    }
    //getter function for address
    public String getAddress() {
        return address;
    }
    //setter function for address
    public void setAddress(String address) {
        this.address = address;
    }
    //getter function for contact
    public String getContact() {
        return contact;
    }
    //setter function for contact
    public void setContact(String contact) {
        this.contact = contact;
    }
    //getter function for email
    public String getEmail() {
        return email;
    }
    //setter function for email
    public void setEmail(String email) {
        this.email = email;
    }
    //getter function for image
    public String getImg() {
        return img;
    }
    //setter function for image
    public void setImg(String img) {
        this.img = img;
    }
    //getter function for latitude
    public String getLat() {
        return lat;
    }
    //setter function for latitude
    public void setLat(String lat) {
        this.lat = lat;
    }
    //getter function for longitude
    public String getLng() {
        return lng;
    }
    //setter function for longitude
    public void setLng(String lng) {
        this.lng = lng;
    }
    //getter function for name
    public String getName() {
        return name;
    }
    //setter function for name
    public void setName(String name) {
        this.name = name;
    }
    //getter function for user id
    public String getUid() {
        return uid;
    }
    //setter function for user id
    public void setUid(String uid) {
        this.uid = uid;
    }
}
