package com.example.carassistant.ui.view;

import android.content.res.ColorStateList;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.example.carassistant.R;
import com.example.carassistant.core.Error;
import com.example.carassistant.core.Result;
import com.example.carassistant.core.Success;
import com.example.carassistant.data.models.Car;
import com.example.carassistant.databinding.FragmentRedactionCarBinding;
import com.example.carassistant.ui.viewModels.RedactionCarViewModel;


public class RedactionCarFragment extends Fragment {

    private FragmentRedactionCarBinding binding;

    private Bundle bundle;

    private RedactionCarViewModel redactionCarViewModel;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentRedactionCarBinding.inflate(inflater, container, false);



        redactionCarViewModel = new ViewModelProvider(this).get(RedactionCarViewModel.class);

        bundle = requireArguments();


        redactionCarViewModel.loadCar(bundle.getString(AddCarFragment.carIdKey));

        redactionCarViewModel.resultOfLoadCar.observe(getViewLifecycleOwner(), result -> {
            if (result instanceof Success){
                Car car = ((Success<Car>) result).getData();
                binding.etColor.setText(car.getColor());
                binding.etCost.setText(String.valueOf(car.getCost()));
                binding.etMark.setText(car.getMark());
                binding.etMileage.setText(String.valueOf(car.getMileage()));
                binding.etModel.setText(car.getModel());
                binding.etYear.setText(String.valueOf(car.getYear()));
            }
        });

        redactionCarViewModel.resultOfRedactionCar.observe(getViewLifecycleOwner(), result -> {
            if (result instanceof Success)
                Navigation.findNavController(binding.getRoot())
                        .navigate(R.id.action_redactionCarFragment_to_carDescriptionFragment, bundle);
            else
                Toast.makeText(container.getContext(), ((Error)result).getMessage(), Toast.LENGTH_SHORT).show();
        });



        binding.btnRedactionCar.setOnClickListener(view -> {

            boolean flag = true;
            ColorStateList colorStateList = ColorStateList.valueOf(ContextCompat.getColor(requireContext(), R.color.red));
            ColorStateList primalColor = ColorStateList.valueOf(ContextCompat.getColor(requireContext(), R.color.background_edittext));
            EditText[] arrayEditText = new EditText[]{binding.etModel, binding.etMark, binding.etYear,
                    binding.etMileage, binding.etCost, binding.etColor};
            for (EditText editText: arrayEditText){
                if (editText.getText().toString().isEmpty()) {
                    flag = false;
                    editText.setBackgroundTintList(colorStateList);
                }
                else
                    editText.setBackgroundTintList(primalColor);
            }
            if(flag) {
                redactionCarViewModel.redactCar(
                        bundle.getString(AddCarFragment.carIdKey),
                        bundle.getInt(AccountFragment.indexCar),
                        binding.etMark.getText().toString(),
                        binding.etModel.getText().toString(),
                        Integer.parseInt(binding.etYear.getText().toString()),
                        Integer.parseInt(binding.etMileage.getText().toString()),
                        binding.etColor.getText().toString(),
                        Integer.parseInt(binding.etCost.getText().toString())
                );
            }
        });

        return binding.getRoot();
    }
}