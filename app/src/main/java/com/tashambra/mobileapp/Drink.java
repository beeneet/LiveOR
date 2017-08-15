package com.tashambra.mobileapp;

import android.util.Log;

import static com.tashambra.mobileapp.GraphActivity.Gender;
import static com.tashambra.mobileapp.GraphActivity.Weight;


public class Drink {
    protected double AlcoholPercent;
    protected String name;
    protected double volume;
    protected long initialTime;
    protected double timePassed;
    protected double BACval;

    public long getInitialTime() {
        return initialTime;
    }

    public void setInitialTime(long initialTime) {
        this.initialTime = initialTime;
    }


    public double getTimePassed() {
        return timePassed;
    }

    public void setTimePassed(double timePassed) {
        this.timePassed = timePassed;
    }


    public double getBACval() {
        return BACval;
    }

    public void setBACval(double BACval) {
        this.BACval = BACval;
    }

    public Drink(){
        this.name = "Simple Drink";
        this.AlcoholPercent = 7;
        this.volume = 12;
        this.timePassed = 0;
        this.initialTime = System.currentTimeMillis();
        this.BACval = 0;

    }

    public Drink(String name){
        this.name = name;
        this.timePassed = 0;
        this.initialTime = System.currentTimeMillis();
        this.BACval = 0;
    }

    public Drink(String name, double AlcoholPercent){
        this.name = name;
        this.AlcoholPercent = AlcoholPercent;
        this.timePassed = 0;
        this.initialTime = System.currentTimeMillis();
        this.BACval = 0;
    }

    public Drink(String name, double AlcoholPercent, double Volume){
        this.name = name;
        this.AlcoholPercent = AlcoholPercent;
        this.volume = Volume;
        this.timePassed = 0;
        this.initialTime = System.currentTimeMillis();
        this.BACval = 0;
    }



    public double getAlcoholPercent() {
        return AlcoholPercent;
    }

    public void setAlcoholPercent(double alcoholPercent) {
        AlcoholPercent = alcoholPercent;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getVolume() {
        return volume;
    }

    public void setVolume(double volume) {
        this.volume = volume;
    }

    public double GetAlcoholContent(){
        double genValue;
        if (Gender.equals("male")){
            genValue = 0.73;
        } else {
            genValue = 0.66;
        }
        Log.i("VALUES", String.valueOf(Gender) + String.valueOf(Weight) + String.valueOf(this.getAlcoholPercent()) + String.valueOf(this.getVolume()) + String.valueOf(this.getTimePassed()));
        long currentTime = System.currentTimeMillis();
        long differenceTime = currentTime - this.getInitialTime();
        double newTime = getTimePassed() + (differenceTime * 0.000000277778);
        Log.i("TIMES N C", String.valueOf(newTime) + " " + String.valueOf(getInitialTime()));
        double alcoholAmount = (this.getVolume() * this.getAlcoholPercent())/100;
        double result = (alcoholAmount * (5.14/Weight)* genValue) - 0.015 * newTime;
        String res = String.valueOf(result);
        Log.i("CURR_BAC", res);
        setBACval(Math.max(0, result));
        return getBACval();
    }
}

