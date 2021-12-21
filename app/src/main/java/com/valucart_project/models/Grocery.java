package com.valucart_project.models;

public class Grocery {

    public int productImage;
    public String productName,id,icon,image;
    public Boolean selectedItem=false;

    public Grocery(String productName, int productImage) {
        this.productImage = productImage;
        this.productName = productName;
    }
    public Grocery(String productName) {
        this.productName = productName;
    }

    public Grocery(String productName, String productId ,String icon1,String image1,Boolean selectedItem1) {
        this.icon = icon1;
        this.productName = productName;
        selectedItem = selectedItem1;
        id = productId;
        image = image1;
    }
    public Grocery(String productName, String productId ,String icon1,Boolean selectedItem1) {
        this.icon = icon1;
        this.productName = productName;
        selectedItem = selectedItem1;
        id = productId;
    }

    public Grocery(String productName, String productId ,int productImage,Boolean selectedItem1) {
        this.productImage = productImage;
        this.productName = productName;
        selectedItem = selectedItem1;
        id = productId;
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

    public Boolean getSelectedItem() {
        return selectedItem;
    }

    public void setSelectedItem(Boolean selectedItem) {
        this.selectedItem = selectedItem;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getImage() {
        return image == null ? "" :image;
    }

    public void setImage(String image) {
        this.image = image;
    }

}

