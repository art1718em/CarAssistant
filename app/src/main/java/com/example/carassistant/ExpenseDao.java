package com.example.carassistant;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Observable;

@Dao
public interface ExpenseDao {
    @Insert
    Completable addExpense(Expense expense);


    @Query("SELECT * FROM expense_table WHERE id = :id")
    Observable<Expense> getCarById(int id);

    // Метод чтения всех задач из таблицы
    @Query("SELECT * FROM expense_table")
    Observable<List<Expense>> getAllExpenses();

    @Query("SELECT * FROM expense_table WHERE idCar = :id")
    Observable<List<Expense>> getALLThisId(int id);

    // Метод изменения значения isCompleted (статус выполнения текущей задачи) в имеющейся записи по id
//    @Query("UPDATE expense_table SET isCompleted = :isCompleted WHERE id = :id")
//    Completable setIsCompleted(boolean isCompleted, int id);
}
