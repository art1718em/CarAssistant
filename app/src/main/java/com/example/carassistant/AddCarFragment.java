package com.example.carassistant;

import android.content.Context;
import android.content.res.ColorStateList;
import android.os.Bundle;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.carassistant.databinding.FragmentAddCarBinding;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class AddCarFragment extends Fragment {

    private FragmentAddCarBinding binding;
    Disposable disposable;
    Disposable ExpenseDisposable;
    public static final String key = "expenseIdKey";

    private int timeID;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,  Bundle savedInstanceState) {
        binding = FragmentAddCarBinding.inflate(inflater, container, false);

        CarDB carDB = CarDB.getInstance(requireContext());
        CarDao carDao = carDB.carDao();



        ExpenseDB expenseDB = ExpenseDB.getInstance(requireContext());
        ExpenseDao expenseDao = expenseDB.expenseDao();


        binding.buttonAddCar.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                new Thread(() -> {
                    if (carDao.getListCar().isEmpty())
                        timeID = 1;
                    else
                        timeID = carDao.getListCar().get(carDao.getListCar().size() - 1).getId() + 1;
                }).start();
                boolean flag = true;
                ColorStateList colorStateList = ColorStateList.valueOf(ContextCompat.getColor(getContext(), R.color.red));
                ColorStateList primalColor = ColorStateList.valueOf(ContextCompat.getColor(getContext(), R.color.background_edittext));
                if (binding.etMark.getText().toString().equals("")) {
                    flag = false;
                    binding.etMark.setBackgroundTintList(colorStateList);
                } else
                    binding.etMark.setBackgroundTintList(primalColor);
                if (binding.etModel.getText().toString().equals("")) {
                    flag = false;
                    binding.etModel.setBackgroundTintList(colorStateList);
                } else
                    binding.etModel.setBackgroundTintList(primalColor);
                if (binding.etYear.getText().toString().equals("")) {
                    flag = false;
                    binding.etYear.setBackgroundTintList(colorStateList);
                } else
                    binding.etYear.setBackgroundTintList(primalColor);
                if (binding.etMileage.getText().toString().equals("")) {
                    flag = false;
                    binding.etMileage.setBackgroundTintList(colorStateList);
                } else
                    binding.etMileage.setBackgroundTintList(primalColor);
                if (binding.etCost.getText().toString().equals("")) {
                    flag = false;
                    binding.etCost.setBackgroundTintList(colorStateList);
                } else
                    binding.etCost.setBackgroundTintList(primalColor);
                if (binding.etColor.getText().toString().equals("")) {
                    flag = false;
                    binding.etColor.setBackgroundTintList(colorStateList);
                } else
                    binding.etColor.setBackgroundTintList(primalColor);
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
                getActivity().getSharedPreferences("id", Context.MODE_PRIVATE).edit().putInt(ExpensesFragment.key,timeID).apply();
                //Navigation.findNavController(binding.getRoot()).clearBackStack(R.id.action_addCarFragment_to_panelFragment);
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