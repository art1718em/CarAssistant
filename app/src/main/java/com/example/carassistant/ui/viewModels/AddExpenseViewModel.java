package com.example.carassistant.ui.viewModels;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.carassistant.core.Error;
import com.example.carassistant.core.Result;
import com.example.carassistant.core.Success;
import com.example.carassistant.data.models.Car;
import com.example.carassistant.data.models.CarDto;
import com.example.carassistant.data.models.Expense;
import com.example.carassistant.data.models.ExpenseDto;
import com.example.carassistant.data.models.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class AddExpenseViewModel extends ViewModel {

    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    private FirebaseAuth auth = FirebaseAuth.getInstance();


    public MutableLiveData<Result> resultAddExpenseDto = new MutableLiveData<>();

    public MutableLiveData<Result> resultAddExpense = new MutableLiveData<>();

    public void addExpense(String idCar, double expense, String category, String data, String comment, int mileage){
        Expense exp = new Expense(expense, category, data, comment, mileage);
        db.collection("expenses").add(exp).addOnCompleteListener(task -> {
            if (task.isSuccessful()){
                addExpenseDto(idCar, task.getResult().getId(), category, expense);
            }
            else
                resultAddExpense.setValue(new Error(task.getException().getMessage()));
        });
    }



    public void addExpenseDto(String idCar, String idExpense, String  category, Double expense){
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
                resultAddExpenseDto.setValue(new Success());
            else
                resultAddExpenseDto.setValue(new Error(task.getException().getMessage()));
        });
    }

}
