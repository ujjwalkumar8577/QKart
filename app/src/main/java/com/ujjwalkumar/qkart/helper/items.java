package com.ujjwalkumar.qkart.helper;

public class items {
    private String detail;
    private String id;
    private String mrp;
    private String name;
    private String price;
    private String sellerid;
    private String status;
    
    public items(String detail,String id,String mrp,String name,String price,String sellerid,String status){
        this.detail=detail;
        this.id=id;
        this.mrp=mrp;
        this.name=name;
        this.price=price;
        this.sellerid=sellerid;
        this.status=status;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMrp() {
        return mrp;
    }

    public void setMrp(String mrp) {
        this.mrp = mrp;
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

    public String getSellerid() {
        return sellerid;
    }

    public void setSellerid(String sellerid) {
        this.sellerid = sellerid;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
