package com.example.carassistant.data.models;

import java.util.List;

public class GuestUser {
    private String idCar;
    private CarDto carDto;
    private List<ExpenseDto> expenseDtoList;

    public GuestUser() {
    }

    public GuestUser(String idCar, CarDto carDto, List<ExpenseDto> expenseDtoList) {
        this.idCar = idCar;
        this.carDto = carDto;
        this.expenseDtoList = expenseDtoList;
    }

    public String getIdCar() {
        return idCar;
    }

    public void setIdCar(String idCar) {
        this.idCar = idCar;
    }

    public CarDto getCarDto() {
        return carDto;
    }


    public List<ExpenseDto> getExpenseDtoList() {
        return expenseDtoList;
    }


    public void addExpense(ExpenseDto expenseDto){
        expenseDtoList.add(expenseDto);
    }

    public void deleteExpense(ExpenseDto expenseDto){
        expenseDtoList.remove(expenseDto);
    }

    public void setExpense(ExpenseDto expenseDto, int index){
        expenseDtoList.set(index, expenseDto);
    }
}
