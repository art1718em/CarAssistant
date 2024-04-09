package com.example.carassistant.data.models;

import androidx.annotation.NonNull;



public class Expense {
    private double expense;
    private String category;
    private String data;
    private String comment;
    private int mileage;


    public Expense(double expense, String category, String data, String comment, int mileage){
        this.expense = expense;
        this.category = category;
        this.data = data;
        this.comment = comment;
        this.mileage = mileage;
    }

    public Expense() {
    }

    public double getExpense() {return expense;}

    public String getCategory() {return category;}

    public String getData() {return data;}

    public int getMileage() {return mileage;}

    public String getComment() {return comment;}



    @NonNull
    @Override
    public String toString() {
        return "Expense{" +
                "expense='" + expense + '\'' +
                ", category='" + category + '\'' +
                ", data='" + data + '\'' +
                ", mileage=" + mileage +
                '}';
    }
}
