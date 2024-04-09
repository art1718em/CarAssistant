package com.example.carassistant.ui.viewModels;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.carassistant.core.Error;
import com.example.carassistant.core.Result;
import com.example.carassistant.core.Success;
import com.example.carassistant.data.models.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;


public class RegistrationViewModel extends ViewModel {

    private FirebaseAuth mAuth = FirebaseAuth.getInstance();

    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    public MutableLiveData<Result> resultOfRegistration = new MutableLiveData<>();

    public MutableLiveData<Boolean> resultLogIn = new MutableLiveData<>();
    public MutableLiveData<Result> resultAddUser = new MutableLiveData<>();

    public void registration(String email, String password){
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        resultOfRegistration.setValue(new Success<>());
                    } else {
                        resultOfRegistration.setValue(new Error(task.getException().getMessage()));
                    }
                });

    }

    public void isLogIn(){
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null)
            resultLogIn.setValue(true);
        else
            resultLogIn.setValue(false);
    }

    public void addUserToFirestore(){
        User user = new User(new ArrayList<>());
        db.collection("users").document(mAuth.getCurrentUser().getUid()).set(user).addOnCompleteListener(task -> {
            if(task.isSuccessful())
                resultAddUser.setValue(new Success<>());
            else
                resultAddUser.setValue(new Error(task.getException().getMessage()));
        });
    }



}
