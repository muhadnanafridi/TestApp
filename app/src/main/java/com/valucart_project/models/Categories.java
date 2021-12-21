package com.valucart_project.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.io.Serializable;
import java.util.ArrayList;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Categories  implements Serializable {

    ArrayList<DataCategories> data = new ArrayList<>();
    int status;

    public static class DataCategories  implements Serializable {

        String id,name,icon,image;
        Boolean selectedItem=false;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public Boolean getSelectedItem() {
            return selectedItem;
        }

        public void setSelectedItem(Boolean selectedItem) {
            this.selectedItem = selectedItem;
        }

        public String getIcon() {
            return icon;
        }

        public void setIcon(String icon) {
            this.icon = icon;
        }

        public String getImage() {
            return image;
        }

        public void setImage(String image) {
            this.image = image;
        }

    }

    public ArrayList<DataCategories> getData() {
        return data;
    }

    public void setData(ArrayList<DataCategories> data) {
        this.data = data;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

}

