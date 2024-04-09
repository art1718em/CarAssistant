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
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class ExpenseDescriptionViewModel extends ViewModel {

    private FirebaseAuth auth = FirebaseAuth.getInstance();
    private FirebaseFirestore db = FirebaseFirestore.getInstance();


    public MutableLiveData<Result> resultOfLoadExpenseDescription = new MutableLiveData<>();


    private String expenseId;

    public MutableLiveData<Result> resultOfDeleteExpense = new MutableLiveData<>();


    public void loadExpenseDescription(String idCar, int indexExpense){
        db.collection("cars").document(idCar).get().addOnCompleteListener(task -> {
            if (task.isSuccessful()){
                Car car = task.getResult().toObject(Car.class);
                ExpenseDto expenseDto = car.getListExpenses().get(indexExpense);
                expenseId = expenseDto.getId();
                loadExpense(expenseId);
            }else
                resultOfLoadExpenseDescription.setValue(new Error(task.getException().getMessage()));
        });
    }

    private void loadExpense(String id){
        db.collection("expenses").document(id).get().addOnCompleteListener(task -> {
            if (task.isSuccessful()){
                Expense expense = task.getResult().toObject(Expense.class);
                resultOfLoadExpenseDescription.setValue(new Success<>(expense));
            }else
                resultOfLoadExpenseDescription.setValue(new Error(task.getException().getMessage()));
        });
    }

    public void deleteExpense(String idCar, int indexExpense) {
        db.collection("expenses").document(expenseId).delete().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                deleteExpenseInCar(idCar, indexExpense);
            } else
                resultOfDeleteExpense.setValue(new Error(task.getException().getMessage()));
        });
    }

    private void deleteExpenseInCar(String idCar, int indexExpense){
        db.collection("cars").document(idCar).get().addOnCompleteListener(task -> {
            if (task.isSuccessful()){
                Car car = task.getResult().toObject(Car.class);
                car.deleteExpense(car.getListExpenses().get(indexExpense));
                db.collection("cars").document(idCar).set(car).addOnCompleteListener(task1 -> {
                    if (task.isSuccessful())
                        resultOfDeleteExpense.setValue(new Success<>("Трата была успешно удалена"));
                    else
                        resultOfDeleteExpense.setValue(new Error(task1.getException().getMessage()));
                });
            }
            else
                resultOfDeleteExpense.setValue(new Error(task.getException().getMessage()));
        });
    }
}
