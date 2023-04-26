package com.example.carassistant;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "expense_table")
public class Expense {
    @PrimaryKey(autoGenerate = true)
    int id;
    private String expense;
    private String category;
    private String data;
    private String comment;
    private int mileage;


    public Expense(String expense, String category, String data, String comment, int mileage){
        this.expense = expense;
        this.category = category;
        this.data = data;
        this.comment = comment;
        this.mileage = mileage;
    }

    public int getId() {
        return id;
    }

    public String getExpense() {return expense;}

    public String getCategory() {return category;}

    public String getData() {return data;}

    public int getMileage() {return mileage;}

    public String getComment() {return comment;}

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