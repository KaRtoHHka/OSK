package com.oleg.oskfin.data;

import java.io.Serializable;

public class Store implements Serializable {
    private String nameOfProduct, aboutProduct, imageOfProduct;
    private int costOfProduct;

    public String getNameOfProduct() {
        return nameOfProduct;
    }

    public void setNameOfProduct(String nameOfProduct) {
        this.nameOfProduct = nameOfProduct;
    }

    public String getAboutProduct() {
        return aboutProduct;
    }

    public void setAboutProduct(String aboutProduct) {
        this.aboutProduct = aboutProduct;
    }

    public String getImageOfProduct() {
        return imageOfProduct;
    }

    public void setImageOfProduct(String imageOfProduct) {
        this.imageOfProduct = imageOfProduct;
    }

    public int getCostOfProduct() {
        return costOfProduct;
    }

    public void setCostOfProduct(int costOfProduct) {
        this.costOfProduct = costOfProduct;
    }
}
