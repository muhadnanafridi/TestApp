package com.valucart_project.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.io.Serializable;
import java.util.ArrayList;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Scheduled implements Serializable {

    int status;
    ArrayList<Data> data = new ArrayList<Data>();

    public class Data  implements Serializable {

        int order_id;
        String order_reference,schedule_next_date,schedule_interval;

        public int getOrder_id() {
            return order_id;
        }

        public void setOrder_id(int order_id) {
            this.order_id = order_id;
        }

        public String getOrder_reference() {
            return order_reference;
        }

        public void setOrder_reference(String order_reference) {
            this.order_reference = order_reference;
        }

        public String getSchedule_next_date() {
            return schedule_next_date;
        }

        public void setSchedule_next_date(String schedule_next_date) {
            this.schedule_next_date = schedule_next_date;
        }

        public String getSchedule_interval() {
            return schedule_interval;
        }

        public void setSchedule_interval(String schedule_interval) {
            this.schedule_interval = schedule_interval;
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

