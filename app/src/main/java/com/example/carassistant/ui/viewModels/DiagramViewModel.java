package com.example.carassistant.ui.viewModels;

import android.view.View;

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
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

public class DiagramViewModel extends ViewModel {

    private FirebaseAuth auth = FirebaseAuth.getInstance();
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    public MutableLiveData<Result> resultOfExpenses = new MutableLiveData<>();

    public MutableLiveData<Result> resultOfLoadActiveCar = new MutableLiveData<>();

    public void loadActiveCar(){
        db.collection("users").document(auth.getCurrentUser().getUid()).get().addOnCompleteListener(task -> {
            if (task.isSuccessful()){
                List<CarDto> listCars = task.getResult().toObject(User.class).getListCars();
                for (CarDto carDto: listCars){
                    if (carDto.isActiveCar())
                        resultOfLoadActiveCar.setValue(new Success<>(carDto.getId()));
                }
            }else
                resultOfLoadActiveCar.setValue(new Error(task.getException().getMessage()));
        });
    }

    public void getListExpenses(String id){
        db.collection("cars").document(id).get().addOnCompleteListener(task -> {
            if (task.isSuccessful()){
                Car car = task.getResult().toObject(Car.class);
                resultOfExpenses.setValue(new Success<>(car.getListExpenses()));
            }
            else
                resultOfExpenses.setValue(new Error(task.getException().getMessage()));
        });
    }
}
