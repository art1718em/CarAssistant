package com.example.carassistant.ui.viewModels;


import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.carassistant.core.Error;
import com.example.carassistant.core.Result;
import com.example.carassistant.core.Success;
import com.example.carassistant.data.models.Car;
import com.example.carassistant.data.models.CarDto;
import com.example.carassistant.data.models.User;

import com.google.firebase.auth.FirebaseAuth;

import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

public class RedactionCarViewModel extends ViewModel {

    private final FirebaseAuth auth = FirebaseAuth.getInstance();

    private final FirebaseFirestore db = FirebaseFirestore.getInstance();

    public MutableLiveData<Result> resultOfLoadCar = new MutableLiveData<>();

    public MutableLiveData<Result> resultOfRedactionCar = new MutableLiveData<>();

    public void loadCar(String idCar){
        db.collection("cars").document(idCar).get().addOnCompleteListener(task -> {
            if (task.isSuccessful())
                resultOfLoadCar.setValue(new Success<>(task.getResult().toObject(Car.class)));
            else
                resultOfLoadCar.setValue(new Error(task.getException().getMessage()));
        });
    }

    public void redactCar(
            String idCar,
            int indexCar,
            String mark,
            String model,
            int year,
            int mileage,
            String color,
            int cost
    ){
        db.collection("cars").document(idCar).get().addOnCompleteListener(task -> {
            if (task.isSuccessful()){
                Car car = task.getResult().toObject(Car.class);
                car.setModel(model);
                car.setColor(color);
                car.setCost(cost);
                car.setMark(mark);
                car.setYear(year);
                car.setMileage(mileage);
                setCar(idCar, car, indexCar, mark, model, color);
            }else {
                resultOfRedactionCar.setValue(new Error(task.getException().getMessage()));
            }
        });
    }

    private void setCar(String idCar, Car car, int indexCar, String mark, String model, String color){
        db.collection("cars").document(idCar).set(car).addOnCompleteListener(task1 -> {
            if (task1.isSuccessful()){
                redactCarDto(indexCar, mark, model, color);
            }else
                resultOfRedactionCar.setValue(new Error(task1.getException().getMessage()));
        });
    }

    public void redactCarDto(
            int indexCar,
            String mark,
            String model,
            String color
    ){
        db.collection("users").document(auth.getCurrentUser().getUid()).get().addOnCompleteListener(task -> {
            if (task.isSuccessful()){
                User user = task.getResult().toObject(User.class);
                List<CarDto> listCars = user.getListCars();
                listCars.get(indexCar).setModel(model);
                listCars.get(indexCar).setMark(mark);
                listCars.get(indexCar).setColor(color);
                user.setListCars(listCars);
                setUser(user);
            }else
                resultOfRedactionCar.setValue(new Error(task.getException().getMessage()));
        });
    }

    private void setUser(User user){
        db.collection("users").document(auth.getCurrentUser().getUid()).set(user).addOnCompleteListener(task1 -> {
            if (task1.isSuccessful()){
                resultOfRedactionCar.setValue(new Success<>());
            }else
                resultOfRedactionCar.setValue(new Error(task1.getException().getMessage()));
        });
    }

}
