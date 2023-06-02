package com.example.carassistant;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.carassistant.databinding.FragmentCarsBinding;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;


public class CarsFragment extends Fragment {

    private FragmentCarsBinding binding;
    Disposable carListDisposable;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentCarsBinding.inflate(inflater, container, false);

        CarDB carDB = CarDB.getInstance(requireContext());
        CarDao carDao = carDB.carDao();

        carListDisposable = carDao
                .getAllCars()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::onCarsLoaded);

        binding.ButtonAddCar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Navigation.findNavController(binding.getRoot()).navigate(R.id.action_carsFragment_to_addCarFragment);
            }
        });

        return binding.getRoot();
    }

    private void onCarsLoaded(List<Car> cars) {
        CarAdapter carAdapter =new CarAdapter((ArrayList<Car>) cars, binding, this);
        binding.recyclerview.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.recyclerview.setAdapter(carAdapter);

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        carListDisposable.dispose();
    }
}