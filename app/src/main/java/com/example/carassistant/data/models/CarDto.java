package com.example.carassistant.data.models;


public class CarDto {
    private String id;
    private String mark;
    private String model;

    private String color;

    private boolean activeCar;


    public CarDto() {
    }

    public CarDto(String id, String mark, String model, String color, boolean activeCar) {
        this.id = id;
        this.mark = mark;
        this.model = model;
        this.color = color;
        this.activeCar = activeCar;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMark() {
        return mark;
    }

    public void setMark(String mark) {
        this.mark = mark;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public boolean isActiveCar() {
        return activeCar;
    }

    public void setActiveCar(boolean activeCar) {
        this.activeCar = activeCar;
    }
}
