package com.example.carassistant.ui.viewModels;

import android.speech.RecognitionService;
import android.util.Pair;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.carassistant.core.Error;
import com.example.carassistant.core.Result;
import com.example.carassistant.core.Success;
import com.example.carassistant.data.models.CarDto;
import com.example.carassistant.data.models.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Firebase;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

public class AccountViewModel extends ViewModel {

    private FirebaseAuth auth = FirebaseAuth.getInstance();

    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    public MutableLiveData<String> resultOfLoadEmail = new MutableLiveData<>();

    public MutableLiveData<Result> resultOfLoadListCars = new MutableLiveData<>();

    public MutableLiveData<Result> resultOfChangeActiveCar = new MutableLiveData<>();

    public void loadUserEmail(){
        resultOfLoadEmail.setValue(auth.getCurrentUser().getEmail());
    }


    public void loadCarList(){
        db.collection("users").document(auth.getCurrentUser().getUid()).get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                List<CarDto> listCars = task.getResult().toObject(User.class).getListCars();
                int index = -1;
                for (int i = 0; i < listCars.size(); i++){
                    if (listCars.get(i).isActiveCar())
                        index = i;
                }
                Pair<List<CarDto>, Integer> pair = new Pair<>(task.getResult().toObject(User.class).getListCars(), index);
                resultOfLoadListCars.setValue(new Success<>(pair));
            }else
                resultOfLoadListCars.setValue(new Error(task.getException().getMessage()));
        });
    }

    public void logOut(){
        auth.signOut();
    }

    public void changeActiveCar(int indexPreviousActiveCar, int indexNewActiveCar){
        db.collection("users").document(auth.getCurrentUser().getUid()).get().addOnCompleteListener(task -> {
            if (task.isSuccessful()){
                User user = task.getResult().toObject(User.class);
                List<CarDto> listCars = user.getListCars();
                CarDto previousCar = listCars.get(indexPreviousActiveCar);
                previousCar.setActiveCar(false);
                listCars.set(indexPreviousActiveCar, previousCar);
                CarDto newCar = listCars.get(indexNewActiveCar);
                newCar.setActiveCar(true);
                listCars.set(indexNewActiveCar, newCar);
                user.setListCars(listCars);

                db.collection("users").document(auth.getCurrentUser().getUid()).set(user).addOnCompleteListener(task1 -> {
                    if (task1.isSuccessful())
                        resultOfChangeActiveCar.setValue(new Success<>());
                    else
                        resultOfChangeActiveCar.setValue(new Error(task1.getException().getMessage()));
                });


            }else
                resultOfChangeActiveCar.setValue(new Error(task.getException().getMessage()));
        });
    }




}
