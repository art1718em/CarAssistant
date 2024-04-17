package com.example.carassistant.ui.view;

import android.app.DatePickerDialog;

import android.content.Context;
import android.content.SharedPreferences;
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

import com.example.carassistant.core.ExpenseStatus;
import com.example.carassistant.core.Success;
import com.example.carassistant.data.models.Expense;
import com.example.carassistant.R;

import com.example.carassistant.databinding.FragmentRedactionExpenseBinding;

import com.example.carassistant.ui.viewModels.RedactionExpenseViewModel;

import java.util.Calendar;
import java.util.Date;


public class RedactionExpenseFragment extends Fragment {

    private FragmentRedactionExpenseBinding binding;

    private DatePickerDialog.OnDateSetListener dateSetListener;

    private  Expense expense;

    private RedactionExpenseViewModel redactionExpenseViewModel;


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentRedactionExpenseBinding.inflate(inflater, container, false);

        binding.constantLayout.setVisibility(View.INVISIBLE);
        binding.progressBar.setVisibility(View.VISIBLE);

        redactionExpenseViewModel = new ViewModelProvider(this).get(RedactionExpenseViewModel.class);

        Bundle expenseBundle = requireArguments();
        String idExpense = expenseBundle.getString(ExpenseDescriptionFragment.expenseIdKey);
        String idCar = expenseBundle.getString(AddCarFragment.carIdKey);
        int indexExpense = expenseBundle.getInt(ExpenseDescriptionFragment.expenseIndexKey);

        redactionExpenseViewModel.loadExpenseDescription(idExpense);

        redactionExpenseViewModel.resultOfLoadExpenseDescription.observe(getViewLifecycleOwner(), result -> {
            if (result instanceof Success){
                expense = ((Success<Expense>) result).getData();
                onExpensesLoaded(expense);
            }else
                Toast.makeText(container.getContext(), ((Error)result).getMessage(), Toast.LENGTH_SHORT).show();
        });

        redactionExpenseViewModel.resultOfRedaction.observe(getViewLifecycleOwner(), result -> {
            binding.darkOverlay.setVisibility(View.GONE);
            binding.progressBar.setVisibility(View.GONE);
            if (result instanceof Success){
                Toast.makeText(container.getContext(), "Трата успешно отредактирована!", Toast.LENGTH_SHORT).show();
                Navigation.findNavController(binding.getRoot())
                        .navigate(R.id.action_redactionExpenseFragment_to_expenseDescriptionFragment, expenseBundle);
            }else
                Toast.makeText(container.getContext(), ((Success<String>) result).getData(), Toast.LENGTH_SHORT).show();
        });

        redactionExpenseViewModel.resultOfEditExpenseInGuestUser.observe(getViewLifecycleOwner(), result -> {
            binding.darkOverlay.setVisibility(View.GONE);
            binding.progressBar.setVisibility(View.GONE);
            if (result instanceof Success){
                Toast.makeText(container.getContext(), "Трата успешно отредактирована!", Toast.LENGTH_SHORT).show();
                Navigation.findNavController(binding.getRoot())
                        .navigate(R.id.action_redactionExpenseFragment_to_expenseDescriptionFragment, expenseBundle);
            }else
                Toast.makeText(container.getContext(), ((Success<String>) result).getData(), Toast.LENGTH_SHORT).show();
        });

        binding.iconCalendar.setOnClickListener(v -> {
            Calendar calendar = Calendar.getInstance();
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH);
            int day = calendar.get(Calendar.DAY_OF_MONTH);
            DatePickerDialog datePickerDialog = new DatePickerDialog(requireContext(), dateSetListener,
                    year,        month,
                    day);
            datePickerDialog.getDatePicker().setMaxDate(new Date().getTime());
            datePickerDialog.show();

        });

        dateSetListener = (view, year, month, dayOfMonth) -> {
            String selectedDate = dayOfMonth + "-" + (month + 1) + "-" + year + " ";
            binding.etDate.setText(selectedDate);
        };

        binding.btnRedaction.setOnClickListener(v -> {
            boolean flag = true;
            ColorStateList colorStateList = ColorStateList.valueOf(ContextCompat.getColor(requireContext(), R.color.red));
            ColorStateList primalColor = ColorStateList.valueOf(ContextCompat.getColor(requireContext(), R.color.background_edittext));
            EditText[] arrayEditText = new EditText[]{binding.etSumOfExpense, binding.etDate, binding.etComment,
                    binding.etMileage};
            for (EditText editText: arrayEditText){
                if (editText.getText().toString().isEmpty()) {
                    flag = false;
                    editText.setBackgroundTintList(colorStateList);
                }
                else
                    editText.setBackgroundTintList(primalColor);
            }
            if(flag) {
                binding.darkOverlay.setVisibility(View.VISIBLE);
                binding.progressBar.setVisibility(View.VISIBLE);

                SharedPreferences sharedPreferences = getContext()
                        .getSharedPreferences(LoginFragment.sharedPreferencesName, Context.MODE_PRIVATE);
                String idGuestUser = sharedPreferences.getString(LoginFragment.sharedPreferencesKey, "null");

                if (!idGuestUser.equals("null")){
                    Expense expense = new Expense(
                            Double.parseDouble(binding.etSumOfExpense.getText().toString()),
                            binding.spinnerChoiceCategory.getSelectedItem().toString(),
                            binding.etDate.getText().toString(),
                            binding.etComment.getText().toString(),
                            Integer.parseInt(binding.etMileage.getText().toString()),
                            ExpenseStatus.UNDER_CONSIDERATION
                    );
                    redactionExpenseViewModel.editExpenseInGuestUser(idGuestUser, idCar, indexExpense, expense);
                }else {
                    Expense expense = new Expense(
                            Double.parseDouble(binding.etSumOfExpense.getText().toString()),
                            binding.spinnerChoiceCategory.getSelectedItem().toString(),
                            binding.etDate.getText().toString(),
                            binding.etComment.getText().toString(),
                            Integer.parseInt(binding.etMileage.getText().toString()),
                            null
                    );
                    redactionExpenseViewModel.redactExpenseInCar(idCar, indexExpense, expense);
                }
            }
        });

        return binding.getRoot();
    }

    private void onExpensesLoaded(Expense expense) {
        switch (expense.getCategory()){
            case "Топливо":
                binding.spinnerChoiceCategory.setSelection(0);
                break;
            case "Запчасти":
                binding.spinnerChoiceCategory.setSelection(1);
                break;
            case "Шины":
                binding.spinnerChoiceCategory.setSelection(2);
                break;
            case "Диски":
                binding.spinnerChoiceCategory.setSelection(3);
                break;
            case "Работа сервиса":
                binding.spinnerChoiceCategory.setSelection(4);
                break;
            case "Автомойка":
                binding.spinnerChoiceCategory.setSelection(5);
                break;
            case "Другое":
                binding.spinnerChoiceCategory.setSelection(6);
                break;
        }
        binding.etSumOfExpense.setText(String.valueOf(expense.getExpense()));
        binding.etComment.setText(expense.getComment());
        binding.etDate.setText(expense.getData());
        binding.etMileage.setText(String.valueOf(expense.getMileage()));

        binding.constantLayout.setVisibility(View.VISIBLE);
        binding.progressBar.setVisibility(View.GONE);

    }

}