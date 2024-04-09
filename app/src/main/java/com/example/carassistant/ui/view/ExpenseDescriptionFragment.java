package com.example.carassistant.ui.view;


import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.carassistant.core.Error;
import com.example.carassistant.core.Success;
import com.example.carassistant.data.models.Expense;
import com.example.carassistant.R;
import com.example.carassistant.databinding.FragmentExpenseDescriptionBinding;
import com.example.carassistant.ui.viewModels.ExpenseDescriptionViewModel;



public class ExpenseDescriptionFragment extends Fragment {
    private FragmentExpenseDescriptionBinding binding;
    public static final String key = "expenseIdKey";

    private ExpenseDescriptionViewModel expenseDescriptionViewModel;



    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentExpenseDescriptionBinding.inflate(inflater, container, false);

        expenseDescriptionViewModel = new ViewModelProvider(this).get(ExpenseDescriptionViewModel.class);

        Bundle expenseBundle = requireArguments();
        int indexExpense = expenseBundle.getInt(key);
        String idCar = requireActivity().getSharedPreferences("id", Context.MODE_PRIVATE).getString("carId", "");

        expenseDescriptionViewModel.loadExpenseDescription(idCar, indexExpense);

        expenseDescriptionViewModel.resultOfLoadExpenseDescription.observe(getViewLifecycleOwner(), result -> {
            if (result instanceof Success){
                Expense expense = ((Success<Expense>) result).getData();
                binding.tvCategoryData.setText(expense.getCategory());
                binding.tvCostData.setText(String.valueOf(expense.getExpense()));
                binding.tvCommentData.setText(expense.getComment());
                binding.tvDateData.setText(expense.getData());
                binding.tvMileageData.setText(String.valueOf(expense.getMileage()));
            }else
                Toast.makeText(container.getContext(), ((Error)result).getMessage(), Toast.LENGTH_SHORT).show();
        });

        expenseDescriptionViewModel.resultOfDeleteExpense.observe(getViewLifecycleOwner(), result -> {
            if (result instanceof Success) {
                Toast.makeText(container.getContext(), ((Success<String>) result).getData(), Toast.LENGTH_SHORT).show();
                Navigation.findNavController(binding.getRoot()).navigate(R.id.action_expenseDescriptionFragment_to_expensesFragment);
            }else
                Toast.makeText(container.getContext(), ((Error)result).getMessage(), Toast.LENGTH_SHORT).show();
        });



        binding.iconBack.setOnClickListener(v -> Navigation.findNavController(binding.getRoot()).popBackStack());

        binding.iconDelete.setOnClickListener(v -> expenseDescriptionViewModel.deleteExpense(idCar, indexExpense));


        binding.btnRedaction.setOnClickListener(v -> Navigation.findNavController(binding.getRoot())
                .navigate(R.id.action_expenseDescriptionFragment_to_redactionExpenseFragment, expenseBundle));

        return binding.getRoot();
    }
}