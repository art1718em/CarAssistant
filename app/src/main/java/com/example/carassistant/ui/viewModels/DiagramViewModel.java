package com.example.carassistant.ui.viewModels;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.carassistant.core.Error;
import com.example.carassistant.core.Result;
import com.example.carassistant.core.Success;
import com.example.carassistant.data.models.Car;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class DiagramViewModel extends ViewModel {

    private FirebaseAuth auth = FirebaseAuth.getInstance();
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    public MutableLiveData<Result> resultOfExpenses = new MutableLiveData<>();

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
