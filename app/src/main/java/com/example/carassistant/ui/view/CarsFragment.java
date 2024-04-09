package com.example.carassistant.ui.view;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.carassistant.core.Error;

import com.example.carassistant.core.Success;


import com.example.carassistant.R;
import com.example.carassistant.data.models.CarDto;
import com.example.carassistant.databinding.FragmentCarsBinding;
import com.example.carassistant.ui.adapters.CarAdapter;
import com.example.carassistant.ui.viewModels.CarsViewModel;


import java.util.List;



public class CarsFragment extends Fragment {

    private FragmentCarsBinding binding;


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentCarsBinding.inflate(inflater, container, false);
        CarsViewModel carsViewModel = new ViewModelProvider(this).get(CarsViewModel.class);

        carsViewModel.loadCars();

        carsViewModel.resultOfCarsLoad.observe(getViewLifecycleOwner(), result -> {
            if (result instanceof Success){
                CarAdapter carAdapter = new CarAdapter(((Success<List<CarDto>>) result).getData(), binding, this);
                binding.recyclerviewListCars.setLayoutManager(new LinearLayoutManager(getContext()));
                binding.recyclerviewListCars.setAdapter(carAdapter);
            }
            else
                Toast.makeText(container.getContext(), ((Error)result).getMessage(), Toast.LENGTH_SHORT).show();
        });


        binding.btnAddCarFragmentCars.setOnClickListener(v -> Navigation.findNavController(binding.getRoot())
                .navigate(R.id.action_carsFragment_to_addCarFragment));

        return binding.getRoot();
    }


}