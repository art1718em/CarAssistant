package com.example.carassistant.data.models;

public class ExpenseDto {
    private String id;
    private String category;
    private Double expense;

    public ExpenseDto(String id, String category, Double expense) {
        this.id = id;
        this.category = category;
        this.expense = expense;
    }

    public ExpenseDto() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public Double getExpense() {
        return expense;
    }

    public void setExpense(Double expense) {
        this.expense = expense;
    }
}
