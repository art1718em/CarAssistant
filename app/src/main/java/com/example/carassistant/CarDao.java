package com.example.carassistant;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Observable;

@Dao
public interface CarDao {
    @Insert
    Completable addCar(Car car);


    @Query("SELECT * FROM car_table WHERE id = :id")
    Observable<Car> getCarById(int id);

    // Метод чтения всех задач из таблицы
    @Query("SELECT * FROM car_table")
    Observable<List<Car>> getAllCars();

    @Query("SELECT * FROM car_table")
    List<Car> getListCar();
}
