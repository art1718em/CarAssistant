package com.example.carassistant.ui.viewModels;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.carassistant.core.Error;
import com.example.carassistant.core.Result;
import com.example.carassistant.core.Success;
import com.example.carassistant.data.models.Car;
import com.example.carassistant.data.models.GuestUser;
import com.google.firebase.firestore.FirebaseFirestore;

public class ExpensesViewModel extends ViewModel {
    private final FirebaseFirestore db = FirebaseFirestore.getInstance();

    public MutableLiveData<Result> resultOfListExpenses = new MutableLiveData<>();

    public void loadListExpenses(String idCar){
        db.collection("cars").document(idCar).get().addOnCompleteListener(task -> {
            if (task.isSuccessful()){
                Car car = task.getResult().toObject(Car.class);
                resultOfListExpenses.setValue(new Success<>(car.getListExpenses()));
            }else
                resultOfListExpenses.setValue(new Error(task.getException().getMessage()));
        });
    }

    public void loadListExpensesGuest(String idGuestUser){
        db.collection("guestUsers").document(idGuestUser).get().addOnCompleteListener(task -> {
            if (task.isSuccessful()){
                GuestUser guestUser = task.getResult().toObject(GuestUser.class);
                resultOfListExpenses.setValue(new Success<>(guestUser.getExpenseDtoList()));
            }else
                resultOfListExpenses.setValue(new Error(task.getException().getMessage()));
        });
    }
    
}
