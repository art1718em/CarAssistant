package com.example.carassistant;


import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;


import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.carassistant.databinding.FragmentExpenseDescriptionBinding;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class ExpenseDescriptionFragment extends Fragment {
    private FragmentExpenseDescriptionBinding binding;
    public static final String key = "expenseIdKey";
    private Bundle bundle;
    Disposable expenseListDisposable;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentExpenseDescriptionBinding.inflate(inflater, container, false);
        Bundle expenseBundle = requireArguments();
        ExpenseDB expenseDB = ExpenseDB.getInstance(requireContext());
        ExpenseDao expenseDao = expenseDB.expenseDao();
        expenseListDisposable = expenseDao
                .getExpenseById(expenseBundle.getInt("expenseIdKey", -1))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::onExpenseDescriptionLoaded, throwable -> {
                    Log.wtf("error", throwable.toString());
                });
        binding.iconBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(binding.getRoot()).popBackStack();

            }
        });
        binding.iconDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                expenseListDisposable = expenseDao
                        .deleteExpense(expenseBundle.getInt("expenseIdKey", -1))
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe();
                Navigation.findNavController(binding.getRoot()).navigate(R.id.action_expenseDescriptionFragment_to_expensesFragment);
            }
        });

        binding.buttonRedaction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(binding.getRoot()).navigate(R.id.action_expenseDescriptionFragment_to_redactionExpenseFragment, expenseBundle);
            }
        });

        return binding.getRoot();
    }
    private void onExpenseDescriptionLoaded(Expense expense) {
        binding.tvCategoryData.setText(expense.getCategory());
        binding.tvCostData.setText(expense.getExpense());
        binding.tvCommentData.setText(expense.getComment());
        binding.tvDateData.setText(expense.getData());
        binding.tvMileageData.setText(String.valueOf(expense.getMileage()));
    }
}