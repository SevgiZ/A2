package com.main.a2;

public class Course {
    private String name;
    private String capacity;    //Capacity is a string to account for when it needs to be N/A
    private String year;
    private String delivery;
    private String day;
    private  String time;
    private double duration;
    private String slotsLeft;

    Course(String name, String capacity, String year, String delivery, String day, String time, double duration) {
        this.name = name;
        this.year = year;
        this.delivery = delivery;
        this.day = day;
        this.time = time;
        this.duration = duration;
        this.slotsLeft = "OPEN";

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

    public String getSlotsLeft() {return slotsLeft;}

    public void setSlotsLeft(String slotsLeft) {this.slotsLeft = slotsLeft;}

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

    @Override
    public String toString() {
        return name + " " + capacity + " " + year + " " + delivery + " " + day + " " + time + " " + duration;

    }
}
