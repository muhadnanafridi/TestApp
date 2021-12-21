package com.valucart_project.models;

import java.io.Serializable;

public class DeliveryTime implements Serializable {

    public int productImage;
    public String productName;
    public String selectedItem="false";

    public DeliveryTime(String productName, int productImage , String selectedItem1) {
        this.productImage = productImage;
        this.productName = productName;
        this.selectedItem = selectedItem1;
    }

    public int getProductImage() {
        return productImage;
    }

    public void setProductImage(int productImage) {
        this.productImage = productImage;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getSelectedItem() {
        return selectedItem;
    }

    public void setSelectedItem(String selectedItem) {
        this.selectedItem = selectedItem;
    }

}
