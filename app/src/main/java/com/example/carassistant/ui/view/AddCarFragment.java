package com.example.carassistant.ui.view;

import android.content.Context;
import android.content.res.ColorStateList;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.example.carassistant.data.models.Car;
import com.example.carassistant.data.room.root.CarDB;
import com.example.carassistant.data.room.dao.CarDao;
import com.example.carassistant.R;
import com.example.carassistant.databinding.FragmentAddCarBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class AddCarFragment extends Fragment {

    private FragmentAddCarBinding binding;
    Disposable disposable;

    private int timeID;

    FirebaseAuth mAuth;
    FirebaseUser user;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentAddCarBinding.inflate(inflater, container, false);

        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
        Toast.makeText(container.getContext(), user.getEmail(), Toast.LENGTH_SHORT).show();


        CarDB carDB = CarDB.getInstance(requireContext());
        CarDao carDao = carDB.carDao();

        binding.btnAddCar.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                new Thread(() -> {
                    if (carDao.getListCar().isEmpty())
                        timeID = 1;
                    else
                        timeID = carDao.getListCar().get(carDao.getListCar().size() - 1).getId() + 1;
                }).start();
                boolean flag = true;
                ColorStateList colorStateList = ColorStateList.valueOf(ContextCompat.getColor(requireContext(), R.color.red));
                ColorStateList primalColor = ColorStateList.valueOf(ContextCompat.getColor(requireContext(), R.color.background_edittext));
                EditText[] arrayEditText = new EditText[]{binding.etMark, binding.etModel, binding.etYear,
                        binding.etMileage, binding.etCost, binding.etColor};
                for (EditText editText: arrayEditText){
                    if (editText.getText().toString().isEmpty()) {
                        flag = false;
                        editText.setBackgroundTintList(colorStateList);
                    } else
                        editText.setBackgroundTintList(primalColor);
                }
                if (flag) {
                    disposable = carDao
                            .addCar(
                                    new Car(
                                            timeID,
                                            binding.etMark.getText().toString(),
                                            binding.etModel.getText().toString(),
                                            binding.etYear.getText().toString(),
                                            Integer.parseInt(binding.etMileage.getText().toString()),
                                            Integer.parseInt(binding.etCost.getText().toString()),
                                            binding.etColor.getText().toString()
                                    )
                            )
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(this::onCarAdded);
                }
            }


            private void onCarAdded(){
                Bundle bundle = new Bundle();
                bundle.putInt(ExpensesFragment.key,timeID);
                requireActivity().getSharedPreferences("id", Context.MODE_PRIVATE).edit().putInt(ExpensesFragment.key,timeID).apply();
                Navigation.findNavController(binding.getRoot()).navigate(R.id.action_addCarFragment_to_panelFragment,
                bundle);
            }
        });

        return binding.getRoot();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (disposable != null)
            disposable.dispose();
    }
}