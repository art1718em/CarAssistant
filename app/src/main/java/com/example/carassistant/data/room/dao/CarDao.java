package com.example.carassistant.data.room.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.carassistant.data.models.Car;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Observable;

@Dao
public interface CarDao {
    @Insert
    Completable addCar(Car car);




    // Метод чтения всех задач из таблицы
    @Query("SELECT * FROM car_table")
    Observable<List<Car>> getAllCars();

    @Query("SELECT * FROM car_table")
    List<Car> getListCar();
}
