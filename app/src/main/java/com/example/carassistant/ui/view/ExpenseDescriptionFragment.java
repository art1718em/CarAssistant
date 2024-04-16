package com.example.carassistant.ui.view;


import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import android.util.Pair;
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

import java.util.HashMap;


public class ExpenseDescriptionFragment extends Fragment {
    private FragmentExpenseDescriptionBinding binding;
    public static final String expenseIndexKey = "expenseIndexKey";

    public static final String expenseIdKey = "expenseIdKey";

    private ExpenseDescriptionViewModel expenseDescriptionViewModel;

    private String expenseId;



    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentExpenseDescriptionBinding.inflate(inflater, container, false);

        binding.constantLayout.setVisibility(View.INVISIBLE);
        binding.progressBar.setVisibility(View.VISIBLE);

        expenseDescriptionViewModel = new ViewModelProvider(this).get(ExpenseDescriptionViewModel.class);

        Bundle expenseBundle = requireArguments();
        int indexExpense = expenseBundle.getInt(expenseIndexKey);
        String idCar = expenseBundle.getString(AddCarFragment.carIdKey);

        Bundle idCarBundle = new Bundle();
        idCarBundle.putString(AddCarFragment.carIdKey, expenseBundle.getString(AddCarFragment.carIdKey));

        expenseDescriptionViewModel.loadExpenseDescription(idCar, indexExpense);

        expenseDescriptionViewModel.resultOfLoadExpenseDescription.observe(getViewLifecycleOwner(), result -> {
            binding.progressBar.setVisibility(View.GONE);
            if (result instanceof Success){
                Pair<String, Expense> pair = ((Success<Pair<String, Expense>>) result).getData();
                Expense expense = pair.second;
                expenseId = pair.first;
                binding.tvCategoryData.setText(expense.getCategory());
                binding.tvCostData.setText(String.valueOf(expense.getExpense()));
                binding.tvCommentData.setText(expense.getComment());
                binding.tvDateData.setText(expense.getData());
                binding.tvMileageData.setText(String.valueOf(expense.getMileage()));
                binding.constantLayout.setVisibility(View.VISIBLE);
            }else
                Toast.makeText(container.getContext(), ((Error)result).getMessage(), Toast.LENGTH_SHORT).show();
        });

        expenseDescriptionViewModel.resultOfDeleteExpense.observe(getViewLifecycleOwner(), result -> {
            binding.progressBar.setVisibility(View.GONE);
            binding.darkOverlay.setVisibility(View.GONE);
            if (result instanceof Success) {
                Toast.makeText(container.getContext(), ((Success<String>) result).getData(), Toast.LENGTH_SHORT).show();
                Navigation.findNavController(binding.getRoot())
                        .navigate(R.id.action_expenseDescriptionFragment_to_expensesFragment, idCarBundle);
            }else
                Toast.makeText(container.getContext(), ((Error)result).getMessage(), Toast.LENGTH_SHORT).show();
        });



        binding.iconBack.setOnClickListener(v -> Navigation.findNavController(binding.getRoot()).popBackStack());

        binding.iconDelete.setOnClickListener(v -> {
            expenseDescriptionViewModel.deleteExpense(idCar, indexExpense);
            binding.darkOverlay.setVisibility(View.VISIBLE);
            binding.progressBar.setVisibility(View.VISIBLE);
        });


        binding.btnRedaction.setOnClickListener(v -> {
            Bundle bundle = new Bundle();
            bundle.putString(AddCarFragment.carIdKey, expenseBundle.getString(AddCarFragment.carIdKey));
            bundle.putString(expenseIdKey, expenseId);
            bundle.putInt(expenseIndexKey, indexExpense);
            Navigation.findNavController(binding.getRoot()).navigate(R.id.action_expenseDescriptionFragment_to_redactionExpenseFragment, bundle);
        });
        return binding.getRoot();
    }
}