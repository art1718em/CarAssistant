package com.example.carassistant.ui.view;

import android.content.res.ColorStateList;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.example.carassistant.core.Error;
import com.example.carassistant.core.Success;


import com.example.carassistant.R;
import com.example.carassistant.databinding.FragmentAddCarBinding;
import com.example.carassistant.ui.viewModels.AddCarViewModel;


public class AddCarFragment extends Fragment {

    private FragmentAddCarBinding binding;

    private AddCarViewModel addCarViewModel;

    public static final String carIdKey = "carId";

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentAddCarBinding.inflate(inflater, container, false);
        addCarViewModel = new ViewModelProvider(this).get(AddCarViewModel.class);

        addCarViewModel.resultAddCar.observe(getViewLifecycleOwner(), result -> {
            if(result instanceof Error)
                Toast.makeText(container.getContext(), ((Error)result).getMessage(), Toast.LENGTH_SHORT).show();
        });

        addCarViewModel.resultAddCarDto.observe(getViewLifecycleOwner(), result -> {
            binding.progressBar.setVisibility(View.GONE);
            binding.darkOverlay.setVisibility(View.GONE);
            if(result instanceof Success)
                Navigation.findNavController(binding.getRoot()).popBackStack();
            else
                Toast.makeText(container.getContext(), ((Error)result).getMessage(), Toast.LENGTH_SHORT).show();
        });

        binding.btnAddCar.setOnClickListener(view -> {
            boolean flag = true;
            ColorStateList colorStateList = ColorStateList.valueOf(
                    ContextCompat.getColor(requireContext(),
                    R.color.red)
            );
            ColorStateList primalColor = ColorStateList.valueOf(
                    ContextCompat.getColor(requireContext(),
                            R.color.background_edittext)
            );
            EditText[] arrayEditText = new EditText[]{binding.etMark, binding.etModel, binding.etYear,
                    binding.etMileage, binding.etCost, binding.etColor};
            for (EditText editText: arrayEditText){
                if (editText.getText().toString().isEmpty()) {
                    flag = false;
                    editText.setBackgroundTintList(colorStateList);
                } else
                    editText.setBackgroundTintList(primalColor);
            }
            if (flag){
                binding.darkOverlay.setVisibility(View.VISIBLE);
                binding.progressBar.setVisibility(View.VISIBLE);
                addCarViewModel.addCar(
                        binding.etMark.getText().toString(),
                        binding.etModel.getText().toString(),
                        Integer.parseInt(binding.etYear.getText().toString()),
                        Integer.parseInt(binding.etMileage.getText().toString()),
                        Integer.parseInt(binding.etCost.getText().toString()),
                        binding.etColor.getText().toString()
                );
            }
        });

        return binding.getRoot();
    }

}