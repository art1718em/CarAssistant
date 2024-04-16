package com.example.carassistant.ui.view;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.res.ColorStateList;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.example.carassistant.core.Error;
import com.example.carassistant.core.Success;

import com.example.carassistant.R;
import com.example.carassistant.databinding.FragmentAddExpenseBinding;
import com.example.carassistant.ui.viewModels.AddExpenseViewModel;


import java.util.Calendar;
import java.util.Date;





public class AddExpenseFragment extends Fragment {

    private FragmentAddExpenseBinding binding;

    private NavController navController;

    DatePickerDialog.OnDateSetListener dateSetListener;

    private AddExpenseViewModel addExpenseViewModel;

    private Bundle bundle;


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        navController = Navigation.findNavController(binding.getRoot());
        super.onViewCreated(view, savedInstanceState);
    }

    @SuppressLint("RestrictedApi")
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentAddExpenseBinding.inflate(inflater, container, false);

        bundle = requireArguments();

        addExpenseViewModel = new ViewModelProvider(this).get(AddExpenseViewModel.class);

        addExpenseViewModel.resultAddExpense.observe(getViewLifecycleOwner(), result -> {
            if(result instanceof Error)
                Toast.makeText(container.getContext(), ((Error)result).getMessage(), Toast.LENGTH_SHORT).show();
        });

        addExpenseViewModel.resultAddExpenseDto.observe(getViewLifecycleOwner(), result -> {
            binding.progressBar.setVisibility(View.GONE);
            binding.darkOverlay.setVisibility(View.GONE);
            if(result instanceof Success){
                if(navController.getBackQueue().get(navController.getBackQueue().getSize()-2).getDestination().getId() == R.id.diagramFragment)
                    Navigation.findNavController(binding.getRoot()).navigate(R.id.action_addExpenseFragment_to_diagramFragment);
                else
                    Navigation.findNavController(binding.getRoot()).navigate(R.id.action_addExpenseFragment_to_expensesFragment, bundle);
            }

            else
                Toast.makeText(container.getContext(), ((Error)result).getMessage(), Toast.LENGTH_SHORT).show();
        });



        binding.iconCalendar.setOnClickListener(v -> {
            Calendar calendar = Calendar.getInstance();
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH);
            int day = calendar.get(Calendar.DAY_OF_MONTH);
            DatePickerDialog datePickerDialog = new DatePickerDialog(requireContext(), dateSetListener,
                    year, month, day);
            datePickerDialog.getDatePicker().setMaxDate(new Date().getTime());
            datePickerDialog.show();

        });



        dateSetListener = (view, year, month, dayOfMonth) -> {
            String selectedDate = dayOfMonth + "-" + (month + 1) + "-" + year + " ";
            binding.etDate.setText(selectedDate);
        };




        binding.btnAddExpense.setOnClickListener(v -> {
            boolean flag = true;
            ColorStateList colorStateList = ColorStateList.valueOf(ContextCompat.getColor(requireContext(), R.color.red));
            ColorStateList primalColor = ColorStateList.valueOf(ContextCompat.getColor(requireContext(), R.color.background_edittext));
            EditText[] arrayEditText = new EditText[]{binding.etSumExpense, binding.etDate, binding.etComment,
                    binding.etMileage};
            for (EditText editText : arrayEditText) {
                if (editText.getText().toString().isEmpty()) {
                    flag = false;
                    editText.setBackgroundTintList(colorStateList);
                } else
                    binding.etSumExpense.setBackgroundTintList(primalColor);
            }
            if (flag) {
                binding.darkOverlay.setVisibility(View.VISIBLE);
                binding.progressBar.setVisibility(View.VISIBLE);
                addExpenseViewModel.addExpense(
                        bundle.getString(AddCarFragment.carIdKey),
                        Double.parseDouble(binding.etSumExpense.getText().toString()),
                        binding.spinnerChoiceCategory.getSelectedItem().toString(),
                        binding.etDate.getText().toString(),
                        binding.etComment.getText().toString(),
                        Integer.parseInt(binding.etMileage.getText().toString())
                );

            }
        });
        return binding.getRoot();
    }


}