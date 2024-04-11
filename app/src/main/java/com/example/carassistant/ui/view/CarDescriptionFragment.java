package com.example.carassistant.ui.view;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.carassistant.R;
import com.example.carassistant.core.Error;
import com.example.carassistant.core.Result;
import com.example.carassistant.core.Success;
import com.example.carassistant.data.models.Car;
import com.example.carassistant.databinding.FragmentCarDescriptionBinding;
import com.example.carassistant.ui.viewModels.CarDescriptionViewModel;

public class CarDescriptionFragment extends Fragment {

    private FragmentCarDescriptionBinding binding;

    private CarDescriptionViewModel carDescriptionViewModel;

    private Bundle bundle;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentCarDescriptionBinding.inflate(inflater, container, false);
        carDescriptionViewModel = new ViewModelProvider(this).get(CarDescriptionViewModel.class);

        bundle = requireArguments();

        carDescriptionViewModel.loadCarDescription(bundle.getString(AddCarFragment.carIdKey));

        carDescriptionViewModel.resultOfLoadCarDescription.observe(getViewLifecycleOwner(), result -> {
            if (result instanceof Success){
                Car car = ((Success<Car>) result).getData();
                binding.tvColorData.setText(car.getColor());
                binding.tvCostData.setText(String.valueOf(car.getCost()));
                binding.tvMileageData.setText(String.valueOf(car.getMileage()));
                binding.tvMarkData.setText(car.getMark());
                binding.tvModelData.setText(car.getModel());
                binding.tvYearData.setText(String.valueOf(car.getYear()));
            } else
                Toast.makeText(container.getContext(), ((Error)result).getMessage(), Toast.LENGTH_SHORT).show();
        });

        carDescriptionViewModel.resultOfDeleteCar.observe(getViewLifecycleOwner(), result -> {
            if (result instanceof Success)
                Navigation.findNavController(binding.getRoot()).popBackStack();
            else
                Toast.makeText(container.getContext(), ((Error)result).getMessage(), Toast.LENGTH_SHORT).show();
        });



        binding.iconDelete.setOnClickListener(view -> carDescriptionViewModel
                .deleteCar(bundle.getString(AddCarFragment.carIdKey), bundle.getInt(AccountFragment.indexCar)));



        binding.iconBack.setOnClickListener(v -> Navigation.findNavController(binding.getRoot())
                .navigate(R.id.action_carDescriptionFragment_to_accountFragment));

        binding.btnRedaction.setOnClickListener(view -> Navigation.findNavController(binding.getRoot())
                .navigate(R.id.action_carDescriptionFragment_to_redactionCarFragment, bundle));

        return binding.getRoot();
    }
}