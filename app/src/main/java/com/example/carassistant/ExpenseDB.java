package com.example.carassistant;


import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;


@Database(entities = {Expense.class}, version = 1)
public abstract class ExpenseDB extends RoomDatabase {

    private static ExpenseDB instance;

    public abstract ExpenseDao expenseDao();

    public static synchronized ExpenseDB getInstance(Context context) {
        if (instance == null){
            instance = Room.databaseBuilder(
                context.getApplicationContext(),
                ExpenseDB.class, "expense_database").build();
        }
        return instance;
    }
}
