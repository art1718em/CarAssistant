package com.example.carassistant.data.models;


import java.util.ArrayList;
import java.util.List;


public class Car {
    private String mark;
    private String model;
    private int year;
    private int mileage;
    private int cost;
    private String color;

    private List<ExpenseDto> listExpenses;


    public Car(String mark, String model, int year, int mileage, int cost, String color, ArrayList<ExpenseDto> listExpenses) {
        this.mark = mark;
        this.model = model;
        this.year = year;
        this.mileage = mileage;
        this.cost = cost;
        this.color = color;
        this.listExpenses = listExpenses;
    }

    public List<ExpenseDto> getListExpenses() {
        return listExpenses;
    }

    public void setListExpenses(List<ExpenseDto> listExpenses) {
        this.listExpenses = listExpenses;
    }

    public String getMark() {
        return mark;
    }

    public String getModel() {
        return model;
    }

    public int getYear() {
        return year;
    }

    public int getMileage() {
        return mileage;
    }

    public int getCost() {
        return cost;
    }

    public String getColor() {
        return color;
    }

    public void addExpense(ExpenseDto expenseDto){
        listExpenses.add(expenseDto);
    }

    public void deleteExpense(ExpenseDto expenseDto){
        listExpenses.remove(expenseDto);
    }

    public void setExpense(ExpenseDto expenseDto, int index){
        listExpenses.set(index, expenseDto);
    }

    public Car() {
    }
}