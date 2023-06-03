package com.main.model;

public class CourseModel {
    private String name;
    private String capacity;    //Capacity is a string to account for when it needs to be N/A
    private String year;
    private String delivery;
    private String day;
    private  String time;
    private double duration;
    private String openclosed;
    private String dates;

    CourseModel(String name, String capacity, String openclosed, String year, String delivery, String day, String time, double duration, String dates) {
        this.name = name;
        this.year = year;
        this.delivery = delivery;
        this.day = day;
        this.time = time;
        this.duration = duration;
        this.openclosed = openclosed;
        this.dates = dates;

        if (delivery.equals("Online")) {
            this.capacity = "N/A";
        } else {
            this.capacity = capacity;
        }
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCapacity() {
        return capacity;
    }

    public void setCapacity(String capacity) {
        this.capacity = capacity;
    }

    public String getOpenclosed() {return openclosed;}

    public void setOpenclosed(String slotsLeft) {this.openclosed = slotsLeft;}

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getDelivery() {
        return delivery;
    }

    public void setDelivery(String delivery) {
        this.delivery = delivery;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public double getDuration() {
        return duration;
    }

    public void setDuration(double duration) {
        this.duration = duration;
    }

    public void setDates(String dates) {
        this.dates = dates;
    }

    public String getDates() {
        return dates;
    }

    @Override
    public String toString() {
        return name + " " + capacity + " " + year + " " + delivery + " " + day + " " + time + " " + duration;

    }
}
