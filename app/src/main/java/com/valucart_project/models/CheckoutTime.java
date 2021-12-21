package com.valucart_project.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.io.Serializable;
import java.util.ArrayList;

@JsonIgnoreProperties(ignoreUnknown = true)
public class CheckoutTime  implements Serializable {

    int status;
    ArrayList<Data> data = new ArrayList<Data>();
    public class Data  implements Serializable {
        private int id;
        private String  from="",to = "";
        private String selectedItem="false";
        Boolean available = false;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getSelectedItem() {
            return selectedItem==null?"false":selectedItem;
        }

        public void setSelectedItem(String selectedItem1) {
            this.selectedItem = selectedItem1;
        }

        public String getFrom() {
            return from;
        }

        public void setFrom(String from) {
            this.from = from;
        }

        public String getTo() {
            return to;
        }

        public void setTo(String to) {
            this.to = to;
        }

        public Boolean getAvailable() {
            return available;
        }

        public void setAvailable(Boolean available) {
            this.available = available;
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
