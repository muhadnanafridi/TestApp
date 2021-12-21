package com.valucart_project.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.ArrayList;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Addresses  implements Serializable {

    int status ;
    public ArrayList<Data> data = new ArrayList<>();

    public static class Data implements Serializable {

        int id;
        String name,street,building,floor,apartment,landmark,notes,villa_number,apartment_number,location_type,phone_number;
        public Area area = new Area();
        public Area state = new Area();

        public Area getState() {
            return state;
        }

        public void setState(Area state) {
            this.state = state;
        }

        public static class Area implements Serializable {
            int id;
            String name;

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }

            public String getName() {
                return name==null?"":name;
            }

            public void setName(String name) {
                this.name = name;
            }
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getName() {
            return name==null?"":name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getStreet() {
            return street==null?"":street;
        }

        public void setStreet(String street) {
            this.street = street;
        }

        public String getBuilding() {
            return building==null?"":building;
        }

        public void setBuilding(String building) {
            this.building = building;
        }

        public String getFloor() {
            return floor==null?"":floor;
        }

        public void setFloor(String floor) {
            this.floor = floor;
        }

        public String getApartment() {
            return apartment==null?"":apartment;
        }

        public void setApartment(String apartment) {
            this.apartment = apartment;
        }

        public String getLandmark() {
            return landmark==null?"":landmark;
        }

        public void setLandmark(String landmark) {
            this.landmark = landmark;
        }

        public String getNotes() {
            return notes==null?"":notes;
        }

        public void setNotes(String notes) {
            this.notes = notes;
        }

        public Area getArea() {
            return area;
        }

        public void setArea(Area area) {
            this.area = area;
        }


        public String getVilla_number() {
            return villa_number==null?"":villa_number;
        }

        public void setVilla_number(String villa_number) {
            this.villa_number = villa_number;
        }

        public String getApartment_number() {
            return apartment_number==null?"":apartment_number;
        }

        public void setApartment_number(String apartment_number) {
            this.apartment_number = apartment_number;
        }

        public String getLocation_type() {
            return location_type;
        }

        public void setLocation_type(String location_type) {
            this.location_type = location_type;
        }

        public String getPhone_number() {
            return phone_number;
        }

        public void setPhone_number(String phone_number) {
            this.phone_number = phone_number;
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

