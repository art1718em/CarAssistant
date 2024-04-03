package com.example.carassistant.data.models;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "expense_table")
public class Expense {
    @PrimaryKey(autoGenerate = true)
    public int id;
    int idCar;
    private final String expense;
    private final String category;
    private final String data;
    private final String comment;
    private final int mileage;


    public Expense(int idCar, String expense, String category, String data, String comment, int mileage){
        this.idCar = idCar;
        this.expense = expense;
        this.category = category;
        this.data = data;
        this.comment = comment;
        this.mileage = mileage;
    }

    public int getIdCar() {
        return idCar;
    }

    public int getId() {
        return id;
    }

    public String getExpense() {return expense;}

    public String getCategory() {return category;}

    public String getData() {return data;}

    public int getMileage() {return mileage;}

    public String getComment() {return comment;}



    @NonNull
    @Override
    public String toString() {
        return "Expense{" +
                "id=" + id +
                ", expense='" + expense + '\'' +
                ", category='" + category + '\'' +
                ", data='" + data + '\'' +
                ", mileage=" + mileage +
                '}';
    }
}
