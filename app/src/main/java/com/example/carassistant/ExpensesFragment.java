package com.example.carassistant;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.carassistant.databinding.FragmentEntranceBinding;
import com.example.carassistant.databinding.FragmentExpensesBinding;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class ExpensesFragment extends Fragment {

    private FragmentExpensesBinding binding;
    Disposable expenseListDisposable;

    public static final String key = "carIdKey";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentExpensesBinding.inflate(inflater, container, false);

        Bundle carBundle = requireArguments();



    ExpenseDB expenseDB = ExpenseDB.getInstance(requireContext());
    ExpenseDao expenseDao = expenseDB.expenseDao();

    expenseListDisposable = expenseDao
            .getALLThisId(carBundle.getInt(key))
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(this::onExpensesLoaded, throwable -> {
                Log.wtf("error", throwable.toString());
            });


        binding.actionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Navigation.findNavController(binding.getRoot()).navigate(R.id.action_expensesFragment_to_addExpenseFragment,
                        carBundle);
            }
        });

        return binding.getRoot();
    }

    private void onExpensesLoaded(List<Expense> expenses) {
        ExpenseAdapter expenseAdapter =new ExpenseAdapter((ArrayList<Expense>) expenses);
        binding.recyclerview.setLayoutManager(new LinearLayoutManager(requireContext()));
        binding.recyclerview.setAdapter(expenseAdapter);

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        expenseListDisposable.dispose();
    }
}