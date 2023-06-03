package com.example.carassistant.data.room.root;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.carassistant.data.models.Car;
import com.example.carassistant.data.room.dao.CarDao;

@Database(entities = {Car.class}, version = 1)
public abstract class CarDB extends RoomDatabase {

    private static CarDB instance;

    public abstract CarDao carDao();

    public static synchronized CarDB getInstance(Context context) {
        if (instance == null){
            instance = Room.databaseBuilder(
                    context.getApplicationContext(),
                    CarDB.class, "car_database").build();
        }
        return instance;
    }
}