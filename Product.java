package com.example.testdatabaselogtag;

public class Product {
    private String product;
    private int price;
    private String desc;
    private long id;
    public Product(String product, int price, String desc, long id) {
        this.product = product;
        this.price = price;
        this.desc = desc;
        this.id = id;
    }
    public String getProduct() {
        return product;
    }
    public void setProduct(String product) {
        this.product = product;
    }
    public int getPrice() {
        return price;
    }
    public void setPrice(int price) {
        this.price = price;
    }
    public String getDesc() {
        return desc;
    }
    public void setDesc(String desc) {
        this.desc = desc;
    }
    public long getId() {
        return id;
    }
    public void setId(long id) {
        this.id = id;
    }
    @Override
    public String toString() {
        String output =  product ;
        return output;
    }
}

