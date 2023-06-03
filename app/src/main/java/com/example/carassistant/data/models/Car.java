package com.example.carassistant.data.models;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "car_table")
public class Car {
    @PrimaryKey(autoGenerate = true)
    int id;
    private String mark;
    private String model;
    private String year;
    private int mileage;
    private int cost;
    private String color;



    public Car(int id, String mark, String model, String year, int mileage, int cost, String color){
        this.id = id;
        this.mark = mark;
        this.model = model;
        this.year = year;
        this.mileage = mileage;
        this.cost = cost;
        this.color = color;
    }

    public int getId() {return id;}

    public String getMark() {return mark;}

    public String getModel() {return model;}

    public String getYear() {return year;}

    public int getMileage() {return mileage;}

    public int getCost() {return cost;}

    public String getColor() {return color;}

    @Override
    public String toString() {
        return "Car{" +
                "id=" + id +
                ", mark='" + mark + '\'' +
                ", model='" + model + '\'' +
                ", year='" + year + '\'' +
                ", mileage=" + mileage +
                ", cost=" + cost +
                ", color='" + color + '\'' +
                '}';
    }
}