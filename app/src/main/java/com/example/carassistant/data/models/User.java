package com.example.carassistant.data.models;

import java.util.List;

public class User {

    private List<CarDto> listCars;


    public User() {
    }

    public User(List<CarDto> listCars) {
        this.listCars = listCars;
    }

    public List<CarDto> getListCars() {
        return listCars;
    }

    public void setListCars(List<CarDto> listCars) {
        this.listCars = listCars;
    }
    public void addCar(CarDto carDto){
        listCars.add(carDto);
    }

    public void setCar(CarDto carDto, int indexCar){
        listCars.set(indexCar, carDto);
    }

    public void deleteCar(int indexCar){
        listCars.remove(indexCar);
    }
}
