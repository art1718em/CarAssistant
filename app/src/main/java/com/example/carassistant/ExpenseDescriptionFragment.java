package com.example.carassistant;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.carassistant.databinding.FragmentExpenseDescriptionBinding;
import com.example.carassistant.databinding.FragmentExpensesBinding;

import java.util.ArrayList;
import java.util.List;

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
        Log.d("CarAssWork", "idKeyExpense " + String.valueOf(expenseBundle.getInt("expenseIdKey", -1)));
        expenseListDisposable = expenseDao
                .getExpenseById(expenseBundle.getInt("expenseIdKey", -1))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::onExpensesLoaded, throwable -> {
                    Log.wtf("error", throwable.toString());
                });
        binding.image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //carBundle.putInt("key_car", carBundle.getInt(DiagramFragment.key));
                Navigation.findNavController(binding.getRoot()).navigate(R.id.action_expenseDescriptionFragment_to_expensesFragment);
            }
        });
        binding.image2.setOnClickListener(new View.OnClickListener() {
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

        binding.btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(binding.getRoot()).navigate(R.id.action_expenseDescriptionFragment_to_redactionExpenseFragment, expenseBundle);
            }
        });

        return binding.getRoot();
    }
    private void onExpensesLoaded(Expense expense) {
        binding.tvCategory1.setText(expense.getCategory());
        binding.tvCost1.setText(expense.getExpense());
        binding.tvComment1.setText(expense.getComment());
        binding.tvData1.setText(expense.getData());
        binding.tvMileage1.setText(String.valueOf(expense.getMileage()));
    }
}