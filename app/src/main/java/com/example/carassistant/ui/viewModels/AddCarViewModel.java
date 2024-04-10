package com.example.carassistant.ui.viewModels;

import android.util.ArrayMap;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.carassistant.core.Error;
import com.example.carassistant.core.Result;
import com.example.carassistant.core.Success;
import com.example.carassistant.data.models.Car;
import com.example.carassistant.data.models.CarDto;
import com.example.carassistant.data.models.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class AddCarViewModel extends ViewModel {
    public MutableLiveData<Result> resultAddCar = new MutableLiveData<>();

    public MutableLiveData<Result> resultAddCarDto = new MutableLiveData<>();



    private FirebaseAuth auth = FirebaseAuth.getInstance();

    private FirebaseFirestore db = FirebaseFirestore.getInstance();



    public void addCar(
            String mark,
            String model,
            int year,
            int mileage,
            int cost,
            String color
    ) {
        Car car = new Car(mark, model, year, mileage, cost, color, new ArrayList<>());
        db.collection("cars").add(car).addOnCompleteListener(task -> {
            if (task.isSuccessful()){
                addCarDto(task.getResult().getId(), mark, model, color);
            }
            else
                resultAddCar.setValue(new Error(task.getException().getMessage()));
        });
    }


    public void addCarDto(String id, String mark, String model, String color){
        CarDto carDto = new CarDto(id, mark, model, color);
        DocumentReference documentReference = db.collection("users").document(auth.getCurrentUser().getUid());
        documentReference.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()){
                User user = task.getResult().toObject(User.class);
                user.addCar(carDto);
                db.collection("users").document(auth.getCurrentUser().getUid()).set(user).addOnCompleteListener(task1 -> {
                    if (task1.isSuccessful())
                        resultAddCarDto.setValue(new Success<>(id));
                    else
                        resultAddCarDto.setValue(new Error(task1.getException().getMessage()));
                });
            }
            else{
                resultAddCarDto.setValue(new Error(task.getException().getMessage()));
            }
        });

    }

}