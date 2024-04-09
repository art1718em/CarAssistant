package com.example.carassistant.ui.viewModels;

import android.widget.Toast;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import androidx.navigation.Navigation;

import com.example.carassistant.R;
import com.example.carassistant.core.Error;
import com.example.carassistant.core.Result;
import com.example.carassistant.core.Success;
import com.example.carassistant.data.models.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;

public class LoginViewModel extends ViewModel {
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();

    public MutableLiveData<Result> resultOfLogin = new MutableLiveData<>();

    public MutableLiveData<Boolean> resultLogIn = new MutableLiveData<>();

    public void login(String email, String password){
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful())
                        resultOfLogin.setValue(new Success<>());
                    else
                        resultOfLogin.setValue(new Error(task.getException().getMessage()));
                });

    }

    public void isLogIn(){
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null)
            resultLogIn.setValue(true);
        else
            resultLogIn.setValue(false);
    }
}
