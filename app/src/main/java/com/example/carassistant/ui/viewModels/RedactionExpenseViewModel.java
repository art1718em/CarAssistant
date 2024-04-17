package com.example.carassistant.ui.viewModels;


import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.carassistant.core.Error;
import com.example.carassistant.core.Result;
import com.example.carassistant.core.Success;
import com.example.carassistant.data.models.Car;
import com.example.carassistant.data.models.Expense;
import com.example.carassistant.data.models.ExpenseDto;
import com.example.carassistant.data.models.GuestUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

public class RedactionExpenseViewModel extends ViewModel {

    private final FirebaseFirestore db = FirebaseFirestore.getInstance();

    public MutableLiveData<Result> resultOfRedaction = new MutableLiveData<>();

    public MutableLiveData<Result> resultOfLoadExpenseDescription = new MutableLiveData<>();

    public MutableLiveData<Result> resultOfEditExpenseInGuestUser = new MutableLiveData<>();

    public void loadExpenseDescription(String idExpense){
        db.collection("expenses").document(idExpense).get().addOnCompleteListener(task -> {
            if (task.isSuccessful()){
                Expense expense = task.getResult().toObject(Expense.class);
                resultOfLoadExpenseDescription.setValue(new Success<>(expense));
            }else
                resultOfLoadExpenseDescription.setValue(new Error(task.getException().getMessage()));
        });
    }

    public void redactExpenseInCar(String idCar, int indexExpense, Expense expense){
        db.collection("cars").document(idCar).get().addOnCompleteListener(task -> {
            if (task.isSuccessful()){
                Car car = task.getResult().toObject(Car.class);
                ExpenseDto expenseDto = car.getListExpenses().get(indexExpense);
                ExpenseDto newExpenseDto = new ExpenseDto(expenseDto.getId(), expense.getCategory(),
                        expense.getExpense());
                car.setExpense(newExpenseDto, indexExpense);
                setCar(idCar, car, expenseDto.getId(), expense);
            }else
                resultOfRedaction.setValue(new Error(task.getException().getMessage()));
        });
    }

    private void setCar(String idCar, Car car, String idExpense, Expense expense){
        db.collection("cars").document(idCar).set(car).addOnCompleteListener(task1 -> {
            if (task1.isSuccessful())
                setExpense(idExpense, expense);
            else
                resultOfRedaction.setValue(new Error(task1.getException().getMessage()));
        });
    }

    private void setExpense(String idExpense, Expense expense){
        db.collection("expenses").document(idExpense).set(expense).addOnCompleteListener(task -> {
            if (task.isSuccessful()){
                resultOfRedaction.setValue(new Success<>());
            }else
                resultOfRedaction.setValue(new Error(task.getException().getMessage()));
        });
    }

    public void editExpenseInGuestUser(String idGuestUser, String idCar, int indexExpense,
                                       Expense expense){
        db.collection("guestUsers").document(idGuestUser).get().addOnCompleteListener(task -> {
            if (task.isSuccessful()){
                GuestUser guestUser = task.getResult().toObject(GuestUser.class);
                ExpenseDto expenseDto = guestUser.getExpenseDtoList().get(indexExpense);
                ExpenseDto newExpenseDto = new ExpenseDto(expenseDto.getId(), expense.getCategory(), expense.getExpense());
                guestUser.setExpense(newExpenseDto, indexExpense);
                setGuestUser(idGuestUser, guestUser, idCar, expenseDto.getId(), expense);
            }else
                resultOfEditExpenseInGuestUser.setValue(new Error(task.getException().getMessage()));
        });
    }

    private void setGuestUser(String idGuestUser, GuestUser guestUser, String idCar, String idExpense, Expense expense){
        db.collection("guestUsers").document(idGuestUser).set(guestUser).addOnCompleteListener(task -> {
            if (task.isSuccessful()){
                getExpenseIndexInMainAcc(idCar, idExpense, expense);
            }else
                resultOfEditExpenseInGuestUser.setValue(new Error(task.getException().getMessage()));
        });
    }

    private void getExpenseIndexInMainAcc(String idCar, String idExpense, Expense expense){
        db.collection("cars").document(idCar).get().addOnCompleteListener(task -> {
            if (task.isSuccessful()){
                List<ExpenseDto> expenseDtoList = task.getResult().toObject(Car.class).getListExpenses();
                for (int i = 0; i < expenseDtoList.size(); i++){
                    if (expenseDtoList.get(i).getId().equals(idExpense)){
                        redactExpenseInCar(idCar, i, expense);
                        break;
                    }
                }
            }else
                resultOfEditExpenseInGuestUser.setValue(new Error(task.getException().getMessage()));
        });
    }
}
