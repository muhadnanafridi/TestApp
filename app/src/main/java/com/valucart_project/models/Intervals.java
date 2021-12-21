package com.valucart_project.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.io.Serializable;
import java.util.ArrayList;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Intervals implements Serializable {

    int status;
    ArrayList<Data> data = new ArrayList<Data>();

    public class Data  implements Serializable {

        int id;
        String name,interval;
        Boolean selectedItem=false;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getInterval() {
            return interval;
        }

        public void setInterval(String interval) {
            this.interval = interval;
        }

        public Boolean getSelectedItem() {
            return selectedItem==null?false:selectedItem;
        }

        public void setSelectedItem(Boolean selectedItem) {
            this.selectedItem = selectedItem;
        }
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public ArrayList<Data> getData() {
        return data;
    }

    public void setData(ArrayList<Data> data) {
        this.data = data;
    }

}

