package com.main.model;

public class TimeConversion {
    public Double StringTimeToDouble(String inputTime) {
        //Use doubles becuase we are working with 'duration' which needs to be a double

        String[] timeSplit = inputTime.split(":");
        double dHour = Double.parseDouble(timeSplit[0]);
        double dMinute = Double.parseDouble(timeSplit[1]) / 100;

        Double realTime = dHour + dMinute;

        System.out.println(realTime);

        return realTime;
    }

    //To convert duration only. Start time is already in real time.
    public double DurationToRealTime(double inputTime) {
        double leftOver = inputTime % 1;
        double hour = inputTime - leftOver;
        double realLeftOver = (leftOver * (60/1) / 100);
        double realTime = hour + realLeftOver;

        System.out.println(realTime);
        return realTime;
    }
}
