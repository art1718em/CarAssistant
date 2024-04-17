package com.example.carassistant.data.models;

import androidx.annotation.NonNull;

import com.example.carassistant.core.ExpenseStatus;


public class Expense {
    private double expense;
    private String category;
    private String data;
    private String comment;
    private int mileage;

    private ExpenseStatus expenseStatus;



    public Expense(double expense, String category, String data, String comment, int mileage, ExpenseStatus expenseStatus){
        this.expense = expense;
        this.category = category;
        this.data = data;
        this.comment = comment;
        this.mileage = mileage;
        this.expenseStatus = expenseStatus;
    }

    public Expense() {
    }

    public double getExpense() {return expense;}

    public String getCategory() {return category;}

    public String getData() {return data;}

    public int getMileage() {return mileage;}

    public String getComment() {return comment;}

    public ExpenseStatus getExpenseStatus() {
        return expenseStatus;
    }

    public void setExpenseStatus(ExpenseStatus expenseStatus) {
        this.expenseStatus = expenseStatus;
    }



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
