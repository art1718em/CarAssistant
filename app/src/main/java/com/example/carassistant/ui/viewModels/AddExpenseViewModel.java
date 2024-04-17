package com.example.carassistant.ui.viewModels;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.carassistant.core.Error;
import com.example.carassistant.core.ExpenseStatus;
import com.example.carassistant.core.Result;
import com.example.carassistant.core.Success;
import com.example.carassistant.data.models.Car;
import com.example.carassistant.data.models.Expense;
import com.example.carassistant.data.models.ExpenseDto;
import com.example.carassistant.data.models.GuestUser;
import com.google.firebase.firestore.FirebaseFirestore;

public class AddExpenseViewModel extends ViewModel {

    private final FirebaseFirestore db = FirebaseFirestore.getInstance();

    public MutableLiveData<Result> resultAddExpenseDto = new MutableLiveData<>();

    public MutableLiveData<Result> resultAddExpense = new MutableLiveData<>();

    public MutableLiveData<Result> resultAddExpenseGuest = new MutableLiveData<>();



    public void addExpense(String idGuestUser, String idCar, double expense, String category,
                           String data, String comment, int mileage, ExpenseStatus expenseStatus){
        Expense exp = new Expense(expense, category, data, comment, mileage, expenseStatus);
        db.collection("expenses").add(exp).addOnCompleteListener(task -> {
            if (task.isSuccessful()){
                if (expenseStatus == ExpenseStatus.UNDER_CONSIDERATION) {
                    Log.d("mytag", "if addExpense");
                    addExpenseGuest(idGuestUser, task.getResult().getId(), category, expense);
                }
                addExpenseDto(idCar, task.getResult().getId(), category, expense);
            }
            else
                resultAddExpense.setValue(new Error(task.getException().getMessage()));
        });
    }


    private void addExpenseDto(String idCar, String idExpense,
                               String  category, Double expense){
        ExpenseDto expenseDto = new ExpenseDto(idExpense, category, expense);
        db.collection("cars").document(idCar).get().addOnCompleteListener(task -> {
            if (task.isSuccessful()){
                Car car = task.getResult().toObject(Car.class);
                car.addExpense(expenseDto);
                setCar(idCar, car);
            }
            else{
                resultAddExpenseDto.setValue(new Error(task.getException().getMessage()));
            }
        });
    }

    private void setCar(String idCar, Car car){
        db.collection("cars").document(idCar).set(car).addOnCompleteListener(task -> {
            if (task.isSuccessful())
                resultAddExpenseDto.setValue(new Success<>());
            else
                resultAddExpenseDto.setValue(new Error(task.getException().getMessage()));
        });
    }


    private void addExpenseGuest(String idGuestUser, String idExpense,
                                 String  category, Double expense){
        ExpenseDto expenseDto = new ExpenseDto(idExpense, category, expense);
        db.collection("guestUsers").document(idGuestUser).get().addOnCompleteListener(task -> {
            if (task.isSuccessful()){
                GuestUser guestUser = task.getResult().toObject(GuestUser.class);
                guestUser.addExpense(expenseDto);
                setGuestUser(idGuestUser, guestUser);
            }else
                resultAddExpenseGuest.setValue(new Error(task.getException().getMessage()));
        });
    }

    private void setGuestUser(String idGuestUser, GuestUser guestUser){
        db.collection("guestUsers").document(idGuestUser).set(guestUser).addOnCompleteListener(task -> {
            if (!task.isSuccessful()) {
                resultAddExpenseGuest.setValue(new Error(task.getException().getMessage()));
            }
        });
    }

}
