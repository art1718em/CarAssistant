package com.example.carassistant.ui.viewModels;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import com.example.carassistant.core.Error;
import com.example.carassistant.core.Result;
import com.example.carassistant.core.Success;
import com.example.carassistant.data.models.Car;
import com.example.carassistant.data.models.CarDto;
import com.example.carassistant.data.models.GuestUser;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class LoginViewModel extends ViewModel {
    private final FirebaseAuth mAuth = FirebaseAuth.getInstance();

    private final FirebaseFirestore db = FirebaseFirestore.getInstance();

    public MutableLiveData<Result> resultOfLogin = new MutableLiveData<>();

    public MutableLiveData<Boolean> resultLogIn = new MutableLiveData<>();

    public MutableLiveData<Boolean> resultOfCheckCode = new MutableLiveData<>();

    public MutableLiveData<Result> resultOfLoginWithCode = new MutableLiveData<>();

    private final CarDto carDto = new CarDto();

    public void login(String email, String password){
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful())
                        resultOfLogin.setValue(new Success<>());
                    else
                        resultOfLogin.setValue(new Error(task.getException().getMessage()));
                });

    }

    public void checkCode(String idCar){
        db.collection("cars").document(idCar).get().addOnCompleteListener(task -> {
            if (task.getResult().exists()){
                Car car = task.getResult().toObject(Car.class);
                carDto.setId(idCar);
                carDto.setActiveCar(true);
                carDto.setMark(car.getMark());
                carDto.setModel(car.getModel());
                carDto.setColor(car.getColor());
                resultOfCheckCode.setValue(true);
            }
            else
                resultOfCheckCode.setValue(false);
        });
    }

    public void loginWithCode(String idCar){
        GuestUser guestUser = new GuestUser(idCar, carDto, new ArrayList<>());
        db.collection("guestUsers").add(guestUser).addOnCompleteListener(task -> {
            if (task.isSuccessful())
                resultOfLoginWithCode.setValue(new Success<>(task.getResult().getId()));
            else
                resultOfLoginWithCode.setValue(new Error(task.getException().getMessage()));
        });
    }



    public void isLogIn(){
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null)
            resultLogIn.setValue(true);
        else
            resultLogIn.setValue(false);
    }
}
