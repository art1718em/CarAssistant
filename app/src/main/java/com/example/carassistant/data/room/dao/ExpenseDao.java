package com.example.carassistant.data.room.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.carassistant.data.models.Expense;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Observable;

@Dao
public interface ExpenseDao {
    @Insert
    Completable addExpense(Expense expense);

    @Query("DELETE FROM expense_table WHERE id = :id")
    Completable deleteExpense(int id);


    @Query("UPDATE expense_table SET expense = :expense , category= :category, data= :data, comment= :comment, mileage= :mileage WHERE id LIKE :id ")
    Completable updateExpense(int id, String expense,String category,String data, String comment, int mileage);


    @Query("SELECT * FROM expense_table WHERE idCar = :id")
    Observable<List<Expense>> getALLThisId(int id);

    @Query("SELECT * FROM expense_table WHERE id = :id")
    Observable<Expense> getExpenseById(int id);


}
