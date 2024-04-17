package com.example.carassistant.ui.viewModels;


import android.util.Pair;


import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.carassistant.core.Error;
import com.example.carassistant.core.Result;
import com.example.carassistant.core.Success;
import com.example.carassistant.data.models.Car;
import com.example.carassistant.data.models.CarDto;
import com.example.carassistant.data.models.ExpenseDto;
import com.example.carassistant.data.models.GuestUser;
import com.example.carassistant.data.models.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

public class DiagramViewModel extends ViewModel {

    private final FirebaseAuth auth = FirebaseAuth.getInstance();
    private final FirebaseFirestore db = FirebaseFirestore.getInstance();

    public MutableLiveData<Result> resultOfExpenses = new MutableLiveData<>();

    public MutableLiveData<Result> resultOfLoadActiveCar = new MutableLiveData<>();

    public MutableLiveData<Result> resultOfLoadExpensesGuest = new MutableLiveData<>();

    public MutableLiveData<Result> resultOfExistCar = new MutableLiveData<>();

    public MutableLiveData<Result> resultOfCheckCar = new MutableLiveData<>();

    public void loadActiveCar(){
        db.collection("users").document(auth.getCurrentUser().getUid()).get().addOnCompleteListener(task -> {
            if (task.isSuccessful()){
                List<CarDto> listCars = task.getResult().toObject(User.class).getListCars();
                if (listCars.isEmpty())
                    resultOfLoadActiveCar.setValue(new Success<>("-1"));
                else{
                    for (CarDto carDto: listCars) {
                        if (carDto.isActiveCar()) {
                            resultOfLoadActiveCar.setValue(new Success<>(carDto.getId()));
                            break;
                        }
                    }
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


    public void getListExpensesGuest(String idGuestUser){
        db.collection("guestUsers").document(idGuestUser).get().addOnCompleteListener(task -> {
            if (task.isSuccessful()){
                GuestUser guestUser = task.getResult().toObject(GuestUser.class);
                Pair<String, List<ExpenseDto>> pair = new Pair<>(guestUser.getIdCar(), guestUser.getExpenseDtoList());
                resultOfLoadExpensesGuest.setValue(new Success<>(pair));
            }else
                resultOfLoadExpensesGuest.setValue(new Error(task.getException().getMessage()));
        });
    }

    public void checkIsDeleteCar(String idGuestUser){
        db.collection("guestUsers").document(idGuestUser).get().addOnCompleteListener(task -> {
            if (task.isSuccessful()){
                GuestUser guestUser = task.getResult().toObject(GuestUser.class);
                String idCar = guestUser.getIdCar();
                checkCar(idCar);
            }else
                resultOfCheckCar.setValue(new Error(task.getException().getMessage()));
        });
    }

    private void checkCar(String idCar){
        db.collection("cars").document(idCar).get().addOnCompleteListener(task -> {
            if (task.getResult().exists()) {
                resultOfExistCar.setValue(new Success<>());
            }
            else
                resultOfExistCar.setValue(new Error(""));

        });
    }

    public void clearActiveCar(){
        resultOfLoadActiveCar = new MutableLiveData<>();
        resultOfExpenses = new MutableLiveData<>();
    }


}
