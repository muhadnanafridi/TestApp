package com.valucart_project.models;

public class DeliveryDate {

    public String Day;
    public int Date;
    public String CompleteDate;
    public Boolean selectedItem=false;

    public DeliveryDate(int Date1, String Day1, Boolean selectedItem1,String CompleteDate1) {
        this.Day = Day1;
        this.Date = Date1;
        this.selectedItem = selectedItem1;
        this.CompleteDate = CompleteDate1;
    }

    public String getDay() {
        return Day;
    }

    public void setDay(String day) {
        Day = day;
    }

    public int getDate() {
        return Date;
    }

    public void setDate(int date) {
        Date = date;
    }

    public Boolean getSelectedItem() {
        return selectedItem;
    }

    public void setSelectedItem(Boolean selectedItem) {
        this.selectedItem = selectedItem;
    }

    public String getCompleteDate() {
        return CompleteDate;
    }

    public void setCompleteDate(String completeDate) {
        CompleteDate = completeDate;
    }
}
