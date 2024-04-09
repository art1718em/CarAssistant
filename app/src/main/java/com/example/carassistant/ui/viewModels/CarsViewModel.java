package com.example.carassistant.ui.viewModels;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.carassistant.core.Error;
import com.example.carassistant.core.Result;
import com.example.carassistant.core.Success;
import com.example.carassistant.data.models.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.nio.channels.MulticastChannel;

public class CarsViewModel extends ViewModel {

    private FirebaseAuth auth = FirebaseAuth.getInstance();
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    public  MutableLiveData<Result> resultOfCarsLoad = new MutableLiveData<>();

    public void loadCars(){
        db.collection("users").document(auth.getCurrentUser().getUid()).get().addOnCompleteListener(task -> {
            if (task.isSuccessful()){
                User user = task.getResult().toObject(User.class);
                resultOfCarsLoad.setValue(new Success(user.getListCars()));
            }
            else{
                resultOfCarsLoad.setValue(new Error(task.getException().getMessage()));
            }
        });
    }

}
