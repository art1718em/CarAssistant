package com.example.carassistant.ui.viewModels;


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

import java.util.HashMap;

public class CarDescriptionViewModel extends ViewModel {

    private final FirebaseAuth auth = FirebaseAuth.getInstance();
    private final FirebaseFirestore db = FirebaseFirestore.getInstance();

    public MutableLiveData<Result> resultOfLoadCarDescription = new MutableLiveData<>();

    public MutableLiveData<Result> resultOfDeleteCar = new MutableLiveData<>();

    public HashMap<Boolean, Result> resultOfDeleteCarExpenses = new HashMap<>();

    public MutableLiveData<Result> resultOfGetIdCarGuest = new MutableLiveData<>();

    public void loadCarDescription(String idCar){
        db.collection("cars").document(idCar).get().addOnCompleteListener(task -> {
            if (task.isSuccessful())
                resultOfLoadCarDescription.setValue(new Success<>(task.getResult().toObject(Car.class)));
            else
                resultOfLoadCarDescription.setValue(new Error(task.getException().getMessage()));
        });
    }

    public void getCar(String idCar, int indexCar){
        db.collection("cars").document(idCar).get().addOnCompleteListener(task -> {
            if (task.isSuccessful()){
                Car car = task.getResult().toObject(Car.class);
                for (ExpenseDto expenseDto: car.getListExpenses()){
                    deleteCarExpenses(expenseDto.getId());
                }
                if (!resultOfDeleteCarExpenses.containsKey(false)){
                    deleteCar(idCar, indexCar);
                }else{
                    resultOfDeleteCar.setValue(resultOfDeleteCarExpenses.get(false));
                }
            }else{
                resultOfDeleteCar.setValue(new Error(task.getException().getMessage()));
            }
        });
    }

    private void deleteCar(String idCar, int indexCar){
        db.collection("cars").document(idCar).delete().addOnCompleteListener(task1 -> {
            if (task1.isSuccessful())
                deleteCarInUser(indexCar);
            else
                resultOfDeleteCar.setValue(new Error(task1.getException().getMessage()));
        });
    }


    private void deleteCarInUser(int indexCar){
        db.collection("users").document(auth.getCurrentUser().getUid()).get().addOnCompleteListener(task -> {
            if (task.isSuccessful()){
                User user = task.getResult().toObject(User.class);
                if (user.getListCars().get(indexCar).isActiveCar()){
                    if (indexCar != 0 && user.getListCars().size() > 1){
                        CarDto carDto = user.getListCars().get(indexCar-1);
                        carDto.setActiveCar(true);
                        user.setCar(carDto, indexCar-1);
                    }else if(user.getListCars().size() > 1){
                        CarDto carDto = user.getListCars().get(indexCar+1);
                        carDto.setActiveCar(true);
                        user.setCar(carDto, indexCar+1);
                    }
                }
                user.deleteCar(indexCar);
                setUser(user);
            }else
                resultOfDeleteCar.setValue(new Error(task.getException().getMessage()));

        });
    }

    private void setUser(User user){
        db.collection("users").document(auth.getCurrentUser().getUid()).set(user).addOnCompleteListener(task1 -> {
            if (task1.isSuccessful())
                resultOfDeleteCar.setValue(new Success<>());
            else
                resultOfDeleteCar.setValue(new Error(task1.getException().getMessage()));
        });
    }

    private void deleteCarExpenses(String idExpenses){
        db.collection("expenses").document(idExpenses).delete().addOnCompleteListener(task -> {
            if (task.isSuccessful())
                resultOfDeleteCarExpenses.put(true, new Success<>());
            else
                resultOfDeleteCarExpenses.put(false, new Error(task.getException().getMessage()));
        });
    }

    public void getIdCarGuest(String idGuestUser){
        db.collection("guestUsers").document(idGuestUser).get().addOnCompleteListener(task -> {
            if (task.isSuccessful())
                resultOfGetIdCarGuest.setValue(new Success<>(task.getResult().toObject(GuestUser.class).getIdCar()));
            else
                resultOfGetIdCarGuest.setValue(new Error(task.getException().getMessage()));
        });
    }

}
