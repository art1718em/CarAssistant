package com.example.carassistant.ui.viewModels;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.carassistant.core.Error;
import com.example.carassistant.core.Result;
import com.example.carassistant.core.Success;
import com.example.carassistant.data.models.Car;
import com.example.carassistant.data.models.Expense;
import com.example.carassistant.data.models.ExpenseDto;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

public class RedactionExpenseViewModel extends ViewModel {

    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    public MutableLiveData<Result> resultOfRedaction = new MutableLiveData<>();

    public MutableLiveData<Result> resultOfLoadExpenseDescription = new MutableLiveData<>();

    public void loadExpenseDescription(String idExpense){
        db.collection("expenses").document(idExpense).get().addOnCompleteListener(task -> {
            if (task.isSuccessful()){
                Expense expense = task.getResult().toObject(Expense.class);
                resultOfLoadExpenseDescription.setValue(new Success<>(expense));
            }else
                resultOfLoadExpenseDescription.setValue(new Error(task.getException().getMessage()));
        });
    }


    public void redactExpenseInCar(
            String idCar,
            int indexExpense,
            double expense,
            String category,
            String data,
            String comment,
            int mileage
    ){
        db.collection("cars").document(idCar).get().addOnCompleteListener(task -> {
            if (task.isSuccessful()){
                Car car = task.getResult().toObject(Car.class);
                ExpenseDto expenseDto = car.getListExpenses().get(indexExpense);
                ExpenseDto newExpenseDto = new ExpenseDto(expenseDto.getId(), category, expense);
                car.setExpense(newExpenseDto, indexExpense);
                db.collection("cars").document(idCar).set(car).addOnCompleteListener(task1 -> {
                    if (task1.isSuccessful())
                        redactExpense(expenseDto.getId(), expense, category, data, comment, mileage);
                    else
                        resultOfRedaction.setValue(new Error(task1.getException().getMessage()));
                });
            }else
                resultOfRedaction.setValue(new Error(task.getException().getMessage()));
        });
    }

    

    private void redactExpense(
            String idExpense,
            double expense,
            String category,
            String data,
            String comment,
            int mileage
    ){
        Expense newExpense = new Expense(expense, category, data, comment, mileage);
        db.collection("expenses").document(idExpense).set(newExpense).addOnCompleteListener(task -> {
            if (task.isSuccessful()){
                resultOfRedaction.setValue(new Success<>("Трата успешно отредактирована!"));
            }else
                resultOfRedaction.setValue(new Error(task.getException().getMessage()));
        });
    }
}
