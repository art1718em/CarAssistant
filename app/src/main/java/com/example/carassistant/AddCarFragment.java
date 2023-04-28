package com.example.carassistant;

import android.os.Bundle;

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

    private int timeID;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,  Bundle savedInstanceState) {
        binding = FragmentAddCarBinding.inflate(inflater, container, false);

        CarDB carDB = CarDB.getInstance(requireContext());
        CarDao carDao = carDB.carDao();

        ExpenseDB expenseDB = ExpenseDB.getInstance(requireContext());
        ExpenseDao expenseDao = expenseDB.expenseDao();


        binding.btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread(()->{
                    if (carDao.getListCar().isEmpty())
                        timeID = 1;
                    else
                        timeID = carDao.getListCar().get(carDao.getListCar().size()-1).getId() + 1;
                }).start();
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


            private void onCarAdded(){
                Bundle bundle = new Bundle();
                bundle.putInt(ExpensesFragment.key,timeID);
                Navigation.findNavController(binding.getRoot()).navigate(R.id.action_addCarFragment_to_expensesFragment,
                bundle);
            }
        });

        return binding.getRoot();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        disposable.dispose();
    }
}