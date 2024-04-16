package com.example.carassistant.ui.view;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.carassistant.R;
import com.example.carassistant.core.Error;
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


        binding.constantLayout.setVisibility(View.INVISIBLE);
        binding.progressBar.setVisibility(View.VISIBLE);

        carDescriptionViewModel = new ViewModelProvider(this).get(CarDescriptionViewModel.class);

        bundle = requireArguments();

        carDescriptionViewModel.loadCarDescription(bundle.getString(AddCarFragment.carIdKey));

        carDescriptionViewModel.resultOfLoadCarDescription.observe(getViewLifecycleOwner(), result -> {
            binding.progressBar.setVisibility(View.GONE);
            if (result instanceof Success){
                Car car = ((Success<Car>) result).getData();
                binding.tvColorData.setText(car.getColor());
                binding.tvCostData.setText(String.valueOf(car.getCost()));
                binding.tvMileageData.setText(String.valueOf(car.getMileage()));
                binding.tvMarkData.setText(car.getMark());
                binding.tvModelData.setText(car.getModel());
                binding.tvYearData.setText(String.valueOf(car.getYear()));
                binding.constantLayout.setVisibility(View.VISIBLE);
            } else
                Toast.makeText(container.getContext(), ((Error)result).getMessage(), Toast.LENGTH_SHORT).show();
        });

        carDescriptionViewModel.resultOfDeleteCar.observe(getViewLifecycleOwner(), result -> {
            binding.darkOverlay.setVisibility(View.GONE);
            binding.progressBar.setVisibility(View.GONE);
            if (result instanceof Success)
                Navigation.findNavController(binding.getRoot()).popBackStack();
            else
                Toast.makeText(container.getContext(), ((Error)result).getMessage(), Toast.LENGTH_SHORT).show();
        });



        binding.iconDelete.setOnClickListener(view -> {
            carDescriptionViewModel
                    .getCar(bundle.getString(AddCarFragment.carIdKey), bundle.getInt(AccountFragment.indexCar));
            binding.progressBar.setVisibility(View.VISIBLE);
            binding.darkOverlay.setVisibility(View.VISIBLE);
        });



        binding.iconBack.setOnClickListener(v -> Navigation.findNavController(binding.getRoot())
                .navigate(R.id.action_carDescriptionFragment_to_accountFragment));

        binding.btnRedaction.setOnClickListener(view -> Navigation.findNavController(binding.getRoot())
                .navigate(R.id.action_carDescriptionFragment_to_redactionCarFragment, bundle));

        return binding.getRoot();
    }
}