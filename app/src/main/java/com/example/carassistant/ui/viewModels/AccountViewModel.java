package com.example.carassistant.ui.viewModels;

import android.util.Pair;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.carassistant.core.Error;
import com.example.carassistant.core.Result;
import com.example.carassistant.core.Success;
import com.example.carassistant.data.models.CarDto;
import com.example.carassistant.data.models.GuestUser;
import com.example.carassistant.data.models.User;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

public class AccountViewModel extends ViewModel {

    private final FirebaseAuth auth = FirebaseAuth.getInstance();

    private final FirebaseFirestore db = FirebaseFirestore.getInstance();

    public MutableLiveData<String> resultOfLoadEmail = new MutableLiveData<>();

    public MutableLiveData<Result> resultOfLoadListCars = new MutableLiveData<>();

    public MutableLiveData<Result> resultOfLoadCarGuest = new MutableLiveData<>();

    public MutableLiveData<Result> resultOfChangeActiveCar = new MutableLiveData<>();

    public MutableLiveData<Result> resultOfCheckOldPassword = new MutableLiveData<>();

    public MutableLiveData<Result> resultOfChangePassword = new MutableLiveData<>();

    public MutableLiveData<Result> resultOfDeleteAccount = new MutableLiveData<>();

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

    public void loadCarGuest(String idGuestUser){
        db.collection("guestUsers").document(idGuestUser).get().addOnCompleteListener(task -> {
            if (task.isSuccessful())
                resultOfLoadCarGuest.setValue(new Success<>(task.getResult().toObject(GuestUser.class).getCarDto()));
            else
                resultOfLoadCarGuest.setValue(new Error(task.getException().getMessage()));
        });
    }

    public void clearResultOfChangeActiveCar(){
        resultOfChangeActiveCar = new MutableLiveData<>();
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
                setActiveCar(user, indexNewActiveCar);
            }else
                resultOfChangeActiveCar.setValue(new Error(task.getException().getMessage()));
        });
    }

    private void setActiveCar(User user, int indexNewActiveCar){
        db.collection("users").document(auth.getCurrentUser().getUid()).set(user).addOnCompleteListener(task1 -> {
            if (task1.isSuccessful())
                resultOfChangeActiveCar.setValue(new Success<>(indexNewActiveCar));
            else
                resultOfChangeActiveCar.setValue(new Error(task1.getException().getMessage()));
        });
    }

    public void checkOldPassword(String oldPassword){
        auth.getCurrentUser().reauthenticate(EmailAuthProvider
                .getCredential(auth.getCurrentUser().getEmail(), oldPassword)).addOnCompleteListener(task -> {
            if (task.isSuccessful())
                resultOfCheckOldPassword.setValue(new Success<>());
            else
                resultOfCheckOldPassword.setValue(new Error(task.getException().getMessage()));
        });
    }


    public void changePassword(String newPassword){
        auth.getCurrentUser().updatePassword(newPassword).addOnCompleteListener(task -> {
            if (task.isSuccessful())
                resultOfChangePassword.setValue(new Success<>());
            else
                resultOfChangePassword.setValue(new Error(task.getException().getMessage()));
        });
    }

    public void deleteAccount(){
        auth.getCurrentUser().delete().addOnCompleteListener(task -> {
            if (task.isSuccessful())
                resultOfDeleteAccount.setValue(new Success<>());
            else
                resultOfDeleteAccount.setValue(new Error(task.getException().getMessage()));
        });

    }
}
