package com.example.carassistant.ui.view;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.carassistant.data.models.Car;
import com.example.carassistant.data.room.root.CarDB;
import com.example.carassistant.data.room.dao.CarDao;
import com.example.carassistant.R;
import com.example.carassistant.databinding.FragmentCarsBinding;
import com.example.carassistant.ui.adapters.CarAdapter;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;


public class CarsFragment extends Fragment {

    private FragmentCarsBinding binding;
    Disposable carListDisposable;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentCarsBinding.inflate(inflater, container, false);

        CarDB carDB = CarDB.getInstance(requireContext());
        CarDao carDao = carDB.carDao();

        carListDisposable = carDao
                .getAllCars()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::onCarsLoaded);

        binding.btnAddCarFragmentCars.setOnClickListener(v -> Navigation.findNavController(binding.getRoot())
                .navigate(R.id.action_carsFragment_to_addCarFragment));

        return binding.getRoot();
    }

    private void onCarsLoaded(List<Car> cars) {
        CarAdapter carAdapter =new CarAdapter((ArrayList<Car>) cars, binding, this);
        binding.recyclerviewListCars.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.recyclerviewListCars.setAdapter(carAdapter);

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        carListDisposable.dispose();
    }
}