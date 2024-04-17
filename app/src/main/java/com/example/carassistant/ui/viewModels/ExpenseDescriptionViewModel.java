package com.example.carassistant.ui.viewModels;

import android.util.Pair;

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

import java.util.List;

public class ExpenseDescriptionViewModel extends ViewModel {

    private final FirebaseFirestore db = FirebaseFirestore.getInstance();

    public MutableLiveData<Result> resultOfLoadExpenseDescription = new MutableLiveData<>();

    private String expenseId;

    public MutableLiveData<Result> resultOfDeleteExpense = new MutableLiveData<>();

    public MutableLiveData<Result> resultOfDeleteExpenseInGuestUser = new MutableLiveData<>();

    public MutableLiveData<Result> resultOfChangeExpenseStatus = new MutableLiveData<>();

    public void loadExpenseDescription(String idCar, int indexExpense){
        db.collection("cars").document(idCar).get().addOnCompleteListener(task -> {
            if (task.isSuccessful()){
                Car car = task.getResult().toObject(Car.class);
                ExpenseDto expenseDto = car.getListExpenses().get(indexExpense);
                expenseId = expenseDto.getId();
                loadExpense(expenseId, "", indexExpense);
            }else
                resultOfLoadExpenseDescription.setValue(new Error(task.getException().getMessage()));
        });
    }

    public void loadExpenseDescriptionGuest(String idGuestUser, int indexExpense){
        db.collection("guestUsers").document(idGuestUser).get().addOnCompleteListener(task -> {
            if (task.isSuccessful()){
                GuestUser guestUser = task.getResult().toObject(GuestUser.class);
                ExpenseDto expenseDto = guestUser.getExpenseDtoList().get(indexExpense);
                expenseId = expenseDto.getId();
                loadExpense(expenseId, idGuestUser, indexExpense);
            }else
                resultOfLoadExpenseDescription.setValue(new Error(task.getException().getMessage()));
        });
    }


    private void loadExpense(String id, String idGuestUser, int indexExpense){
        db.collection("expenses").document(id).get().addOnCompleteListener(task -> {
            if (!task.getResult().exists()){
                deleteExpenseInGuestUser(idGuestUser, indexExpense, false, "");
            }
            else{
                if (task.isSuccessful()){
                    Expense expense = task.getResult().toObject(Expense.class);
                    Pair<String, Expense> pair = new Pair<>(expenseId, expense);
                    resultOfLoadExpenseDescription.setValue(new Success<>(pair));
                }else
                    resultOfLoadExpenseDescription.setValue(new Error(task.getException().getMessage()));
            }
        });
    }

    public void deleteExpense(String idCar, int indexExpense) {
        db.collection("expenses").document(expenseId).delete().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                getExpenseInCar(idCar, indexExpense);
            } else
                resultOfDeleteExpense.setValue(new Error(task.getException().getMessage()));
        });
    }

    private void getExpenseInCar(String idCar, int indexExpense){
        db.collection("cars").document(idCar).get().addOnCompleteListener(task -> {
            if (task.isSuccessful()){
                Car car = task.getResult().toObject(Car.class);
                car.deleteExpense(car.getListExpenses().get(indexExpense));
                deleteExpenseInCar(idCar, car);
            }
            else
                resultOfDeleteExpense.setValue(new Error(task.getException().getMessage()));
        });
    }

    private void deleteExpenseInCar(String idCar, Car car){
        db.collection("cars").document(idCar).set(car).addOnCompleteListener(task -> {
            if (task.isSuccessful())
                resultOfDeleteExpense.setValue(new Success<>());
            else
                resultOfDeleteExpense.setValue(new Error(task.getException().getMessage()));
        });
    }

    public void deleteExpenseInGuestUser(String idGuestUser, int indexExpenseId, boolean isDeleteInMainAcc, String idCar){
        db.collection("guestUsers").document(idGuestUser).get().addOnCompleteListener(task -> {
            if (task.isSuccessful()){
                GuestUser guestUser = task.getResult().toObject(GuestUser.class);
                guestUser.deleteExpense(guestUser.getExpenseDtoList().get(indexExpenseId));
                setGuestUser(idGuestUser, guestUser, isDeleteInMainAcc, idCar);
            }
        });
    }

    private void setGuestUser(String idGuestUser, GuestUser guestUser, boolean isDeleteInMainAcc, String idCar){
        db.collection("guestUsers").document(idGuestUser).set(guestUser).addOnCompleteListener(task -> {
            if (task.isSuccessful()){
                if (isDeleteInMainAcc)
                    getExpenseIndexInMainAcc(idCar);
                else
                    resultOfDeleteExpenseInGuestUser.setValue(new Success<>());
            }else
                resultOfDeleteExpenseInGuestUser.setValue(new Error(task.getException().getMessage()));
        });
    }

    private void getExpenseIndexInMainAcc(String idCar){
        db.collection("cars").document(idCar).get().addOnCompleteListener(task -> {
            if (task.isSuccessful()){
                List<ExpenseDto> expenseDtoList = task.getResult().toObject(Car.class).getListExpenses();
                for (int i = 0; i < expenseDtoList.size(); i++){
                    if (expenseDtoList.get(i).getId().equals(expenseId)){
                        deleteExpense(idCar, i);
                        break;
                    }
                }
            }else
                resultOfDeleteExpenseInGuestUser.setValue(new Error(task.getException().getMessage()));
        });
    }



    public void changeExpenseStatus(String idCar, int indexExpense, ExpenseStatus expenseStatus){
        db.collection("expenses").document(expenseId).get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Expense expense = task.getResult().toObject(Expense.class);
                expense.setExpenseStatus(expenseStatus);
                setExpense(expense, idCar, indexExpense);
            } else
                resultOfChangeExpenseStatus.setValue(new Error(task.getException().getMessage()));
        });
    }

    private void setExpense(Expense expense, String idCar, int indexExpense){
        db.collection("expenses").document(expenseId).set(expense).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                if (expense.getExpenseStatus() == ExpenseStatus.REJECTED)
                    getExpenseInCar(idCar, indexExpense);
                else
                    resultOfChangeExpenseStatus.setValue(new Success<>());
            }
            else
                resultOfChangeExpenseStatus.setValue(new Error(task.getException().getMessage()));
        });
    }
}
